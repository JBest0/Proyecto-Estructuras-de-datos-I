package serverside.datos;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import serverside.estructuras.LinkedList;
import serverside.modelos.Usuario;

// adapta lista enlazada de usuarios a formato JSON para guardarla en un archivo y viceversa

public class GestorDatos {
    private static final String RUTA_ARCHIVO = "database.json";
    private Gson gson;

    public GestorDatos() {
        this.gson = new Gson();
    }

    // devuelve lista enlazada de usuarios desde el archivo, si no existe devuelve una lista vacía
    public LinkedList<Usuario> cargarUsuarios() {
        LinkedList<Usuario> listaUsuarios = new LinkedList<>();
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            Usuario[] arregloUsuarios = gson.fromJson(reader, Usuario[].class);
            if (arregloUsuarios != null) {
                // arreglo a lista enlazada
                for (Usuario u : arregloUsuarios) {
                    listaUsuarios.add(u);
                }
            }
        } catch (IOException e) {
            System.out.println("Base de datos no encontrada, se creará una nueva.");
        }
        return listaUsuarios;
    }

    // guarda la lista enlazada de usuarios en el archivo, sobreescribiendo lo que haya
    public void guardarUsuarios(LinkedList<Usuario> listaUsuarios) {
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            // lista enlazada a arreglo
            int size = listaUsuarios.size();
            Usuario[] arregloUsuarios = new Usuario[size];
            for (int i = 0; i < size; i++) {
                arregloUsuarios[i] = listaUsuarios.get(i);
            }
            
            gson.toJson(arregloUsuarios, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}