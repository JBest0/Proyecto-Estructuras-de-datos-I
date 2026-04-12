package serverside.logica;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import com.google.gson.Gson;
import serverside.modelos.*;
import serverside.datos.GestorDatos;
import serverside.estructuras.LinkedList;

public class SesionJuego {
    private Socket socketJ1, socketJ2;
    private Usuario u1, u2; // Ocupamos los objetos para actualizar score
    private GestorDatos gestorDatos;
    private Gson gson;

    public SesionJuego(Socket s1, Socket s2, Usuario u1, Usuario u2, GestorDatos gd) {
        this.socketJ1 = s1;
        this.socketJ2 = s2;
        this.u1 = u1;
        this.u2 = u2;
        this.gestorDatos = gd;
        this.gson = new Gson();
    }

    public void iniciar() {
        // enviar configuración inicial a ambos (HP, SpawnRate, etc.)
        ConfiguracionJuego config = new ConfiguracionJuego();
        String jsonConfig = gson.toJson(config);
        
        enviarMensaje(socketJ1, jsonConfig);
        enviarMensaje(socketJ2, jsonConfig);

        // crea los hilos
        //  J1 a J2
        Thread puenteJ1aJ2 = new Thread(() -> gestionarComunicacion(socketJ1, socketJ2, "Jugador 1"));
        
        // J2 a J1
        Thread puenteJ2aJ1 = new Thread(() -> gestionarComunicacion(socketJ2, socketJ1, "Jugador 2"));

        puenteJ1aJ2.start();
        puenteJ2aJ1.start();
    }

    private void gestionarComunicacion(Socket origen, Socket destino, String etiqueta) {
        try (DataInputStream entrada = new DataInputStream(origen.getInputStream());
            DataOutputStream salida = new DataOutputStream(destino.getOutputStream())) {
            
            while (!origen.isClosed() && !destino.isClosed()) {
                String jsonRecibido = entrada.readUTF();
                
                // 1. Convertimos el JSON para revisar el estado
                EstadoJugador estado = gson.fromJson(jsonRecibido, EstadoJugador.class);
                
                // 2. Reenvío al oponente
                salida.writeUTF(jsonRecibido);
                salida.flush(); 

                // se actualizan estadisticas al morir
                if (estado.getHp() <= 0) {
                    System.out.println("El " + etiqueta + " ha sido derrotado. Guardando stats...");
                    actualizarStatsFinales();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Sesión finalizada: " + etiqueta);
        }
    }

    private synchronized void actualizarStatsFinales() {
        // lista del json
        LinkedList<Usuario> listaActual = gestorDatos.cargarUsuarios();
        
        // se añaden a los jugadores en la lista
        for (int i = 0; i < listaActual.size(); i++) {
            Usuario registrado = listaActual.get(i);
            
            if (registrado.getUsername().equals(u1.getUsername())) {
                registrado.setPartidasJugadas(registrado.getPartidasJugadas() + 1);
                registrado.setScoreTotal(registrado.getScoreTotal() + u1.getScoreTotal());
            }
            if (registrado.getUsername().equals(u2.getUsername())) {
                registrado.setPartidasJugadas(registrado.getPartidasJugadas() + 1);
                registrado.setScoreTotal(registrado.getScoreTotal() + u2.getScoreTotal());
            }
        }

        gestorDatos.guardarUsuarios(listaActual);
    }

    private void enviarMensaje(Socket socket, String mensaje) {
        try {
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            salida.writeUTF(mensaje);
            salida.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}