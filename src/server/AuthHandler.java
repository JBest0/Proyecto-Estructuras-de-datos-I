package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.model.UserRecord;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Maneja la autenticación de un cliente recién conectado.
 * Lee una línea JSON, procesa LOGIN o REGISTER y responde con AUTH_OK o AUTH_FAIL.
 * NO cierra el socket — lo devuelve al servidor para la sesión de juego.
 */
public class AuthHandler {

    private final Socket socket;
    private final DatabaseManager db;
    private final Gson gson;

    public AuthHandler(Socket socket, DatabaseManager db) {
        this.socket = socket;
        this.db     = db;
        this.gson   = new Gson();
    }

    /**
     * Procesa el intercambio de autenticación.
     *
     * @return el username autenticado, o null si falló la autenticación.
     */
    public String handle() {
        try {
            BufferedReader lector = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter escritor = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            String lineaJson = lector.readLine();
            if (lineaJson == null || lineaJson.isBlank()) {
                enviarFallo(escritor, "Solicitud vacía");
                return null;
            }

            JsonObject req = JsonParser.parseString(lineaJson).getAsJsonObject();
            String tipo     = req.get("type").getAsString();
            String username = req.get("username").getAsString().trim();
            String passHash = req.get("password").getAsString(); // ya viene hasheado desde el cliente

            if (username.isEmpty()) {
                enviarFallo(escritor, "El nombre de usuario no puede estar vacío");
                return null;
            }

            if ("REGISTER".equalsIgnoreCase(tipo)) {
                return procesarRegistro(escritor, username, passHash);
            } else if ("LOGIN".equalsIgnoreCase(tipo)) {
                return procesarLogin(escritor, username, passHash);
            } else {
                enviarFallo(escritor, "Tipo de solicitud desconocido: " + tipo);
                return null;
            }

        } catch (IOException ex) {
            System.err.println("[Auth] Error de E/S con cliente: " + ex.getMessage());
            return null;
        }
    }

    // ----------------------------------------------------------------
    //  Registro de nuevo usuario
    // ----------------------------------------------------------------
    private String procesarRegistro(PrintWriter escritor, String username, String passHash) throws IOException {
        PrintWriter escritorRef = escritor; // para uso dentro de lambda-like blocks

        if (db.findUser(username) != null) {
            enviarFallo(escritorRef, "El nombre de usuario ya existe");
            return null;
        }

        // El hash ya viene del cliente; el servidor lo almacena tal cual
        // (doble hash deliberado evitado: el cliente envía SHA-256 del password)
        String hashAlmacenado = hashSHA256(passHash);

        UserRecord nuevo = new UserRecord(username, hashAlmacenado, "");
        db.registerUser(nuevo);

        JsonObject respuesta = new JsonObject();
        respuesta.addProperty("type", "AUTH_OK");
        respuesta.addProperty("username", username);
        escritorRef.println(gson.toJson(respuesta));

        System.out.println("[Auth] Usuario registrado: " + username);
        return username;
    }

    // ----------------------------------------------------------------
    //  Login de usuario existente
    // ----------------------------------------------------------------
    private String procesarLogin(PrintWriter escritor, String username, String passHash) throws IOException {
        UserRecord usuario = db.findUser(username);

        if (usuario == null) {
            enviarFallo(escritor, "Usuario no encontrado");
            return null;
        }

        // El cliente envía SHA-256(password). El servidor almacena SHA-256(SHA-256(password)).
        String hashCalculado = hashSHA256(passHash);

        if (!hashCalculado.equals(usuario.getPasswordHash())) {
            enviarFallo(escritor, "Contraseña incorrecta");
            return null;
        }

        JsonObject respuesta = new JsonObject();
        respuesta.addProperty("type", "AUTH_OK");
        respuesta.addProperty("username", username);
        escritor.println(gson.toJson(respuesta));

        System.out.println("[Auth] Login exitoso: " + username);
        return username;
    }

    // ----------------------------------------------------------------
    //  Helpers
    // ----------------------------------------------------------------
    private void enviarFallo(PrintWriter escritor, String razon) {
        JsonObject respuesta = new JsonObject();
        respuesta.addProperty("type", "AUTH_FAIL");
        respuesta.addProperty("reason", razon);
        escritor.println(gson.toJson(respuesta));
    }

    /**
     * Calcula el hash SHA-256 de una cadena y lo devuelve como hex en minúsculas.
     */
    public static String hashSHA256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            // SHA-256 siempre está disponible en Java estándar
            throw new RuntimeException("SHA-256 no disponible", ex);
        }
    }
}
