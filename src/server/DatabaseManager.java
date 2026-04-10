package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.model.UserRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * Administra la persistencia de usuarios en server/database.json.
 * No usa ninguna colección de java.util — los usuarios se guardan en un
 * arreglo UserRecord[] que crece copiando.
 */
public class DatabaseManager {

    // Ruta al archivo JSON relativa al directorio de trabajo del servidor
    private static final String DB_PATH = "server/database.json";

    private final Gson gson;

    /** Arreglo interno de usuarios (sin java.util.ArrayList) */
    private UserRecord[] usuarios;

    public DatabaseManager() {
        this.gson    = new GsonBuilder().setPrettyPrinting().create();
        this.usuarios = new UserRecord[0];
        cargar();
    }

    // --------------------------------------------------------
    //  Carga el archivo JSON al iniciar; lo crea si no existe
    // --------------------------------------------------------
    private void cargar() {
        File archivo = new File(DB_PATH);

        // Crear directorios y archivo vacío si no existen
        if (!archivo.exists()) {
            try {
                archivo.getParentFile().mkdirs();
                try (Writer w = new FileWriter(archivo, StandardCharsets.UTF_8)) {
                    w.write("{\"users\":[]}");
                }
            } catch (IOException ex) {
                System.err.println("[DB] No se pudo crear " + DB_PATH + ": " + ex.getMessage());
                return;
            }
        }

        // Leer y deserializar
        try {
            String contenido = Files.readString(archivo.toPath(), StandardCharsets.UTF_8);
            DatabaseSnapshot snap = gson.fromJson(contenido, DatabaseSnapshot.class);
            if (snap != null && snap.users != null) {
                usuarios = snap.users;
            }
        } catch (IOException ex) {
            System.err.println("[DB] Error al leer " + DB_PATH + ": " + ex.getMessage());
        }
    }

    // --------------------------------------------------------
    //  Busca un usuario por nombre (null si no existe)
    // --------------------------------------------------------
    public UserRecord findUser(String username) {
        for (UserRecord u : usuarios) {
            if (u != null && u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    // --------------------------------------------------------
    //  Registra un nuevo usuario (false si el nombre ya existe)
    // --------------------------------------------------------
    public boolean registerUser(UserRecord user) {
        if (findUser(user.getUsername()) != null) {
            return false;
        }

        // Crecer el arreglo copiando (sin ArrayList)
        UserRecord[] nuevo = new UserRecord[usuarios.length + 1];
        System.arraycopy(usuarios, 0, nuevo, 0, usuarios.length);
        nuevo[usuarios.length] = user;
        usuarios = nuevo;

        save();
        return true;
    }

    // --------------------------------------------------------
    //  Escribe el estado actual al archivo JSON
    // --------------------------------------------------------
    public void save() {
        try (Writer w = new FileWriter(DB_PATH, StandardCharsets.UTF_8)) {
            DatabaseSnapshot snap = new DatabaseSnapshot();
            snap.users = usuarios;
            gson.toJson(snap, w);
        } catch (IOException ex) {
            System.err.println("[DB] Error al guardar " + DB_PATH + ": " + ex.getMessage());
        }
    }

    // --------------------------------------------------------
    //  Clase auxiliar para envolver el arreglo en JSON raíz
    // --------------------------------------------------------
    private static class DatabaseSnapshot {
        UserRecord[] users;
    }
}
