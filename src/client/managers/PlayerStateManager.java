
package client.managers;

import client.models.Player;
import client.models.PlayerState;

/**
 * Encapsula y gestiona el estado del jugador.
 */
public class PlayerStateManager {

    private final Player player;

    /**
     * Construye el manager con el jugador a administrar.
     *
     * @param player jugador actual.
     */
    public PlayerStateManager(Player player) {
        this.player = player;
    }

    public int getHp() { return player.getHp(); }

    public int getScore() { return player.getScore(); }

    public int getNivel() { return player.getNivel(); }

    public double getPosicionX() { return player.getX(); }

    public String getUsername() { return player.getUsername(); }

    public String getAvatar() { return player.getAvatar(); }

    /**
     * Aumenta el score del jugador.
     *
     * @param puntos cantidad a sumar.
     */
    public void aumentarScore(int puntos) {
        player.sumarPuntos(puntos);
    }

    /**
     * Resta vida al jugador.
     *
     * @param dano daÃ±o recibido.
     */
    public void restarVida(int dano) {
        player.restarVida(dano);
    }

    /**
     * Actualiza posiciÃ³n horizontal del jugador.
     *
     * @param x nueva coordenada X.
     */
    public void setPosicionX(double x) {
        player.setX(x);
    }

    /**
     * Actualiza el nivel del jugador.
     *
     * @param nivel nuevo nivel.
     */
    public void setNivel(int nivel) {
        player.setNivel(nivel);
    }

    /**
     * Determina si el jugador sigue con vida.
     *
     * @return true si hp > 0.
     */
    public boolean estaVivo() {
        return player.estaVivo();
    }

    /**
     * Determina si el jugador estÃ¡ muerto.
     *
     * @return true si hp <= 0.
     */
    public boolean estaMuerto() {
        return !player.estaVivo();
    }

    /**
     * Construye el estado para envÃ­o al servidor.
     *
     * @return snapshot de hp, score y nivel.
     */
    public PlayerState getEstadoParaEnviar() {
        return new PlayerState(player.getHp(), player.getScore(), player.getNivel());
    }
}

