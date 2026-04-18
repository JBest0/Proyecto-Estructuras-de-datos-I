import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Gestiona la conexión del cliente con el servidor de juego.
 * El socket se mantiene abierto como campo estático para reutilizarlo
 * en las actualizaciones de estado durante la partida.
 */
public class ServerConnection {

    private static String HOST  = "localhost";
    private static final int    PUERTO = 5001;
    private static final int    TIMEOUT_LECTURA_MS = 12000;
    private static final int    TIMEOUT_CONEXION_MS = 5000;

    /** Socket persistente reutilizado después de la autenticación */
    private static Socket socketActivo = null;
    private static DataInputStream lectorActivo = null;
    private static DataOutputStream escritorActivo = null;

    private ServerConnection() {}

    // Permite cambiar la IP del servidor y forzar reconexion
    public static synchronized void setHost(String ip) {
        HOST = (ip == null || ip.isBlank()) ? "localhost" : ip.trim();

        try {
            if (socketActivo != null && !socketActivo.isClosed()) {
                socketActivo.close();
            }
        } catch (IOException ignored) {
            // Ignorado: se fuerza nueva conexion igual
        }

        socketActivo = null;
        lectorActivo = null;
        escritorActivo = null;
    }

    // ----------------------------------------------------------------
    //  Envía una solicitud de autenticación y devuelve la respuesta JSON
    // ----------------------------------------------------------------
    /**
     * Envía una solicitud LOGIN o REGISTRO al servidor.
     *
     * @param tipo     "LOGIN" o "REGISTRO"
     * @param username nombre de usuario
     * @param password contraseña en texto plano (el servidor la hashea)
     * @return la línea JSON de respuesta del servidor
     */
    public static synchronized String sendAuth(String tipo, String username, String password) {
        try {
            asegurarConexionActiva();

            // Formato esperado por el servidor: COMANDO;{"username":"...","password":"...","avatar":"..."}
            String json = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"avatar\":\"default\"}",
                    escaparJson(username), escaparJson(password));

            String mensajeEnvio = tipo + ";" + json;
            
            escritorActivo.writeUTF(mensajeEnvio);
            escritorActivo.flush();

            return lectorActivo.readUTF();

        } catch (SocketTimeoutException e) {
            limpiarConexion();
            return "ERROR_TIMEOUT";
        } catch (ConnectException e) {
            limpiarConexion();
            return "ERROR_CONEXION";
        } catch (Exception e) {
            limpiarConexion();
            return "ERROR_CONEXION";
        }
    }

    //  Envío y lectura para el bucle
    public static void enviarEstado(String jsonEstado) {
        try {
            if (escritorActivo != null) {
                escritorActivo.writeUTF(jsonEstado);
                escritorActivo.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String leerMensaje() {
        try {
            if (lectorActivo != null) {
                // Quitamos el timeout para esperar al rival el tiempo que sea necesario
                socketActivo.setSoTimeout(0);
                String msg = lectorActivo.readUTF();
                socketActivo.setSoTimeout(TIMEOUT_LECTURA_MS); // Lo volvemos a poner por seguridad
                return msg;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String leerMensajeAsync() {
        try {
            if (lectorActivo != null && socketActivo != null && !socketActivo.isClosed()) {
                int timeoutOriginal = socketActivo.getSoTimeout();
                try {
                    // Intento no bloqueante corto para detectar mensajes entrantes del rival.
                    socketActivo.setSoTimeout(5);
                    return lectorActivo.readUTF();
                } catch (SocketTimeoutException ignored) {
                    return null;
                } finally {
                    socketActivo.setSoTimeout(timeoutOriginal);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //  Métodos Internos
    private static synchronized void asegurarConexionActiva() throws IOException {
        if (socketActivo == null || socketActivo.isClosed()) {
            socketActivo = new Socket();
            socketActivo.connect(new InetSocketAddress(HOST, PUERTO), TIMEOUT_CONEXION_MS);
            socketActivo.setSoTimeout(TIMEOUT_LECTURA_MS);
        }
        if (escritorActivo == null) {
            escritorActivo = new DataOutputStream(socketActivo.getOutputStream());
        }
        if (lectorActivo == null) {
            lectorActivo = new DataInputStream(socketActivo.getInputStream());
        }
    }

    private static String escaparJson(String valor) {
        return valor.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    private static void limpiarConexion() {
        try {
            if (socketActivo != null && !socketActivo.isClosed()) {
                socketActivo.close();
            }
        } catch (IOException ignored) {
            // Ignorado: estamos limpiando estado de error
        }
        socketActivo = null;
        lectorActivo = null;
        escritorActivo = null;
    }
}