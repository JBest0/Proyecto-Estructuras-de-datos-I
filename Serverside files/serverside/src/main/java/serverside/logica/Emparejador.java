package serverside.logica;

import serverside.estructuras.Queue;
import serverside.modelos.ClienteConectado;
import serverside.datos.GestorDatos; 

public class Emparejador {
    private Queue<ClienteConectado> colaEspera;
    private GestorDatos gestorDatos; 

    // Se inyecta el gestor para acceder a la lista de usuarios y actualizar estadísticas
    public Emparejador(GestorDatos gestorDatos) {
        this.colaEspera = new Queue<>();
        this.gestorDatos = gestorDatos;
    }

    //cliente en modo de espera hasta que ingrese segundo usuario
    public void agregarAJuego(ClienteConectado cliente) {
        colaEspera.enqueue(cliente);
        System.out.println(cliente.getUsuario().getUsername() + " entró a la cola.");
        intentarEmparejar();
    }

    private void intentarEmparejar() {
        if (colaEspera.size() >= 2) { 
            ClienteConectado c1 = colaEspera.dequeue();
            ClienteConectado c2 = colaEspera.dequeue();
            
            // Ahora 'gestorDatos' ya no debería salir en rojo
            SesionJuego nuevaSesion = new SesionJuego(
                c1.getSocket(), 
                c2.getSocket(), 
                c1.getUsuario(), 
                c2.getUsuario(), 
                gestorDatos
            );
            nuevaSesion.iniciar();
        }
    }
}