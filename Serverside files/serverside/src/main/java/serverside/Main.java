package serverside;

import serverside.logica.Emparejador;
import serverside.red.GestorServidor;
import serverside.datos.GestorDatos;

public class Main {
    public static void main(String[] args) {
        // Se crea el gestor de datos para los usuarios
        GestorDatos gestorDatos = new GestorDatos();
        
       //se crea el emparejador, pasándole el gestor de datos para las sesiones de juego
        Emparejador emparejador = new Emparejador(gestorDatos);
        
        //se crea el servidor, pasandole el emparejador y el gestor de datos para la autenticación
        GestorServidor servidor = new GestorServidor(5000, emparejador, gestorDatos);
        
        servidor.iniciar();
    }
}