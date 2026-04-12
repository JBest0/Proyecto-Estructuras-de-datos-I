package serverside.red;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.Gson;
import serverside.logica.Emparejador;
import serverside.modelos.ClienteConectado;
import serverside.modelos.Usuario;
import serverside.datos.GestorDatos;
import serverside.estructuras.LinkedList;

public class GestorServidor {
    private int puerto;
    private Emparejador emparejador;
    private GestorDatos gestorDatos;
    private Gson gson;

    public GestorServidor(int puerto, Emparejador emparejador, GestorDatos gestorDatos) {
        this.puerto = puerto;
        this.emparejador = emparejador;
        this.gestorDatos = gestorDatos;
        this.gson = new Gson();
    }

    public void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor escuchando en el puerto " + puerto + "...");
            while (true) {
                Socket socketCliente = serverSocket.accept();
                new Thread(() -> manejarAutenticacion(socketCliente)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void manejarAutenticacion(Socket socket) {
        try {
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

            //Recibir comando y JSON.
            String mensajeCompleto = entrada.readUTF();
            String[] partes = mensajeCompleto.split(";");
            String comando = partes[0];
            Usuario usuarioCliente = gson.fromJson(partes[1], Usuario.class);

            // Cargar lista base de datos
            LinkedList<Usuario> listaRegistrados = gestorDatos.cargarUsuarios();
            Usuario usuarioFinal = null;

            if (comando.equals("REGISTRO")) {
                // logica de registro, se añade cliente
                listaRegistrados.add(usuarioCliente);
                gestorDatos.guardarUsuarios(listaRegistrados);
                usuarioFinal = usuarioCliente;
                salida.writeUTF("AUTH_OK");
            } 
            else if (comando.equals("LOGIN")) {
                // logica de inisio de seción si coincide con base de datos
                usuarioFinal = buscarEnLista(listaRegistrados, usuarioCliente);
                
                if (usuarioFinal != null) {
                    salida.writeUTF("AUTH_OK");
                } else {
                    salida.writeUTF("ERROR_AUTH");
                    socket.close();
                    return;
                }
            }

            // 3. Si pasó la validación, va para la cola
            ClienteConectado nuevoCliente = new ClienteConectado(usuarioFinal, socket);
            emparejador.agregarAJuego(nuevoCliente);

        } catch (Exception e) {
            System.out.println("Error en fase de login: " + e.getMessage());
        }
    }

    // Método auxiliar para buscar en tu LinkedList manual
    private Usuario buscarEnLista(LinkedList<Usuario> lista, Usuario buscado) {
        for (int i = 0; i < lista.size(); i++) {
            Usuario actual = lista.get(i);
            if (actual.getUsername().equals(buscado.getUsername()) && 
                actual.getPassword().equals(buscado.getPassword())) {
                return actual;
            }
        }
        return null;
    }
}