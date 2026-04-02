package client.models;

/**
 * Snapshot ligero del estado del jugador para enviar al servidor.
 */
public class PlayerState {

    private final int hp;
    private final int score;
    private final int nivel;

    /**
     * Crea una instantanea inmutable de estado.
     *
     * @param hp vida actual.
     * @param score puntaje actual.
     * @param nivel nivel actual.
     */
    public PlayerState(int hp, int score, int nivel) {
        this.hp = hp;
        this.score = score;
        this.nivel = nivel;
    }

    public int getHp() { return hp; }

    public int getScore() { return score; }

    public int getNivel() { return nivel; }
}

