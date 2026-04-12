package serverside.modelos;
import java.net.Socket;

public class ClienteConectado {
    private Usuario usuario;
    private Socket socket;

    public ClienteConectado(Usuario usuario, Socket socket) {
        this.usuario = usuario;
        this.socket = socket;
    }

    public Usuario getUsuario() { return usuario; }
    public Socket getSocket() { return socket; }
}