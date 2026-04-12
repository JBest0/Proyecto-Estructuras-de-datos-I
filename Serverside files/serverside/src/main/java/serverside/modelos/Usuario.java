package serverside.modelos;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class Usuario {
    private String username;
    private String password;
    private String avatar;
    private int scoreTotal;
    private int partidasJugadas;

    public Usuario(String username, String password, String avatar) {
        this.username = username;
        this.password = generarHash(password); // hash de seguridad
        this.avatar = avatar;
        this.scoreTotal = 0;
        this.partidasJugadas = 0;
    }

    private String generarHash(String original) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(original.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return original; // respaldo de emergencia en caso de error
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getScoreTotal() {
        return scoreTotal;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setScoreTotal(int scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public void setPartidasJugadas(int partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

}