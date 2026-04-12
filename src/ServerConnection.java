import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Gestiona la conexión del cliente con el servidor de juego.
 * El socket se mantiene abierto como campo estático para reutilizarlo
 * en las actualizaciones de estado durante la partida.
 */
public class ServerConnection {

    private static final String HOST  = "localhost";
    private static final int    PUERTO = 5000;

    /** Socket persistente reutilizado después de la autenticación */
    private static Socket socketActivo = null;
    private static BufferedReader lectorActivo = null;
    private static PrintWriter escritorActivo = null;

    private ServerConnection() {}

    // ----------------------------------------------------------------
    //  Envía una solicitud de autenticación y devuelve la respuesta JSON
    // ----------------------------------------------------------------
    /**
     * Envía una solicitud LOGIN o REGISTER al servidor.
     *
     * @param tipo     "LOGIN" o "REGISTER"
     * @param username nombre de usuario
     * @param password contraseña en texto plano (se hashea aquí antes de enviar)
     * @return la línea JSON de respuesta del servidor
     */
    public static synchronized String sendAuth(String tipo, String username, String password) {
        try {
            asegurarConexionActiva();

            // Hashear la contraseña en el cliente antes de enviar
            String passwordHash = hashSHA256(password);

            // Construir JSON manualmente (sin Gson en la capa cliente UI)
            String solicitud = "{\"type\":\"" + tipo + "\","
                + "\"username\":\"" + escaparJson(username) + "\","
                + "\"password\":\"" + passwordHash + "\"}";

            escritorActivo.println(solicitud);

            // Leer respuesta del servidor
            String respuesta = lectorActivo.readLine();
            if (respuesta == null) {
                return "{\"type\":\"AUTH_FAIL\",\"reason\":\"El servidor cerró la conexión\"}";
            }
            return respuesta;

        } catch (IOException ex) {
            // Cerrar socket roto para que el próximo intento reconecte
            cerrarSocket();
            return "{\"type\":\"AUTH_FAIL\",\"reason\":\"No se pudo conectar al servidor\"}";
        }
    }

    /**
     * Espera la línea CONFIG enviada por el servidor tras AUTH_OK.
     *
     * @return JSON de configuración o null si no fue posible leerlo.
     */
    public static synchronized String esperarConfig() {
        try {
            if (socketActivo == null || socketActivo.isClosed() || lectorActivo == null) {
                return null;
            }
            return lectorActivo.readLine();
        } catch (IOException ex) {
            cerrarSocket();
            return null;
        }
    }

    /**
     * Envía el estado local del jugador por el socket ya autenticado.
     */
    public static synchronized void sendEstado(int hp, int score, int nivel) {
        try {
            if (socketActivo == null || socketActivo.isClosed()) {
                return;
            }
            asegurarConexionActiva();
            String json = "{\"type\":\"STATE\",\"hp\":" + hp
                + ",\"score\":" + score
                + ",\"nivel\":" + nivel + "}";
            escritorActivo.println(json);
        } catch (IOException ex) {
            cerrarSocket();
        }
    }

    public static synchronized void sendGameOver(int score, int nivel) {
        try {
            if (socketActivo == null || socketActivo.isClosed()) return;
            asegurarConexionActiva();
            String json = "{\"type\":\"GAME_OVER\",\"score\":" + score + ",\"nivel\":" + nivel + "}";
            escritorActivo.println(json);
        } catch (IOException ex) {
            cerrarSocket();
        }
    }

    /**
     * Intenta leer una línea del socket sin bloquear de forma prolongada.
     *
     * @return JSON recibido o null si no hubo datos en el timeout corto.
     */
    public static synchronized String leerEstadoOponente() {
        if (socketActivo == null || socketActivo.isClosed() || lectorActivo == null) {
            return null;
        }

        try {
            int timeoutAnterior = socketActivo.getSoTimeout();
            socketActivo.setSoTimeout(50);
            try {
                return lectorActivo.readLine();
            } finally {
                socketActivo.setSoTimeout(timeoutAnterior);
            }
        } catch (SocketTimeoutException ex) {
            return null;
        } catch (IOException ex) {
            cerrarSocket();
            return null;
        }
    }

    // ----------------------------------------------------------------
    //  Helpers
    // ----------------------------------------------------------------

    /** Cierra el socket activo y lo limpia. */
    public static void cerrarSocket() {
        if (socketActivo != null) {
            try { socketActivo.close(); } catch (IOException ignored) {}
        }
        socketActivo = null;
        lectorActivo = null;
        escritorActivo = null;
    }

    private static void asegurarConexionActiva() throws IOException {
        if (socketActivo == null || socketActivo.isClosed()) {
            socketActivo = new Socket(HOST, PUERTO);
        }
        if (escritorActivo == null) {
            escritorActivo = new PrintWriter(
                new OutputStreamWriter(socketActivo.getOutputStream(), StandardCharsets.UTF_8), true);
        }
        if (lectorActivo == null) {
            lectorActivo = new BufferedReader(
                new InputStreamReader(socketActivo.getInputStream(), StandardCharsets.UTF_8));
        }
    }

    /**
     * Calcula el hash SHA-256 de una cadena y lo devuelve en hex minúsculas.
     * Mismo algoritmo que server.AuthHandler para garantizar coherencia.
     */
    private static String hashSHA256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("SHA-256 no disponible", ex);
        }
    }

    /**
     * Escapa caracteres especiales JSON básicos para evitar inyección
     * en el JSON construido manualmente.
     */
    private static String escaparJson(String valor) {
        return valor.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
