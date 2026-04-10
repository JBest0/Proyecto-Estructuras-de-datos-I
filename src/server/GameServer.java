package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Servidor TCP del juego.
 * Escucha en el puerto 5000, autentica clientes y los empareja de a dos.
 *
 * Cola de espera implementada con un arreglo de tamaño 2 (sin java.util).
 */
public class GameServer {

    private static final int PUERTO = 5000;

    // Arreglo de tamaño fijo para la cola de espera (máx. 2 jugadores)
    private final Socket[]  colaEspera   = new Socket[2];
    private final String[]  colaUsuarios = new String[2];
    private int jugadoresEnEspera = 0;

    private final DatabaseManager db;
    private final Gson gson;

    public GameServer() {
        this.db   = new DatabaseManager();
        this.gson = new Gson();
    }

    // ----------------------------------------------------------------
    //  Punto de entrada del servidor
    // ----------------------------------------------------------------
    public static void main(String[] args) {
        new GameServer().iniciar();
    }

    public void iniciar() {
        System.out.println("[Servidor] Iniciando en puerto " + PUERTO + "...");

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("[Servidor] Esperando conexiones...");

            while (true) {
                Socket cliente = serverSocket.accept();
                System.out.println("[Servidor] Nueva conexión: " + cliente.getRemoteSocketAddress());

                // Cada cliente se maneja en su propio hilo
                Thread hilo = new Thread(() -> manejarCliente(cliente));
                hilo.setDaemon(true);
                hilo.start();
            }

        } catch (IOException ex) {
            System.err.println("[Servidor] Error fatal: " + ex.getMessage());
        }
    }

    // ----------------------------------------------------------------
    //  Maneja un cliente: autenticación → cola de espera → sesión
    // ----------------------------------------------------------------
    private void manejarCliente(Socket socket) {
        AuthHandler auth = new AuthHandler(socket, db);
        String username = auth.handle();

        if (username == null) {
            // Autenticación fallida: cerrar conexión
            try { socket.close(); } catch (IOException ignored) {}
            return;
        }

        System.out.println("[Servidor] " + username + " autenticado. Esperando pareja...");
        agregarAColaYVerificar(socket, username);
    }

    // ----------------------------------------------------------------
    //  Agrega el cliente a la cola y, si hay dos, inicia la sesión
    // ----------------------------------------------------------------
    private synchronized void agregarAColaYVerificar(Socket socket, String username) {
        if (jugadoresEnEspera < 2) {
            colaEspera[jugadoresEnEspera]   = socket;
            colaUsuarios[jugadoresEnEspera] = username;
            jugadoresEnEspera++;
        }

        if (jugadoresEnEspera == 2) {
            Socket  s1 = colaEspera[0];   String u1 = colaUsuarios[0];
            Socket  s2 = colaEspera[1];   String u2 = colaUsuarios[1];

            // Limpiar la cola para la siguiente ronda
            colaEspera[0]   = null;  colaEspera[1]   = null;
            colaUsuarios[0] = null;  colaUsuarios[1] = null;
            jugadoresEnEspera = 0;

            System.out.println("[Servidor] Par encontrado: " + u1 + " vs " + u2 + ". Enviando CONFIG...");
            iniciarSesion(s1, u1, s2, u2);
        }
    }

    // ----------------------------------------------------------------
    //  Envía el mensaje CONFIG a ambos clientes y arranca la sesión
    // ----------------------------------------------------------------
    private void iniciarSesion(Socket s1, String u1, Socket s2, String u2) {
        String configJson = construirConfig();

        enviarMensaje(s1, configJson);
        enviarMensaje(s2, configJson);

        System.out.println("[Servidor] Sesión iniciada entre " + u1 + " y " + u2);
        // Aquí se conectaría el GameSessionManager (fase futura)
    }

    // ----------------------------------------------------------------
    //  Construye el JSON de configuración de partida
    // ----------------------------------------------------------------
    private String construirConfig() {
        JsonObject damageByType = new JsonObject();
        damageByType.addProperty("DDOS",   5);
        damageByType.addProperty("MALWARE", 8);
        damageByType.addProperty("CRED",   10);

        JsonObject config = new JsonObject();
        config.addProperty("type",                  "CONFIG");
        config.addProperty("initialHp",             100);
        config.addProperty("baseSpawnRate",         1.0);
        config.addProperty("baseAttackSpeed",       2.0);
        config.addProperty("scorePerKill",          10);
        config.addProperty("difficultyStepScore",   100);
        config.addProperty("spawnMultiplierPerLevel", 1.15);
        config.addProperty("speedAddPerLevel",      0.3);
        config.add("damageByType", damageByType);

        return gson.toJson(config);
    }

    // ----------------------------------------------------------------
    //  Envía una línea JSON a un socket (sin cerrar el stream)
    // ----------------------------------------------------------------
    private void enviarMensaje(Socket socket, String mensaje) {
        try {
            PrintWriter escritor = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            escritor.println(mensaje);
        } catch (IOException ex) {
            System.err.println("[Servidor] Error enviando mensaje: " + ex.getMessage());
        }
    }
}
