package client.models;

/**
 * Estado local del jugador.
 */
public class Player {

    private int hp;
    private int score;
    private int nivel;
    private double x;
    private final String username;
    private final String avatar;

    /**
     * Construye un jugador con estado inicial.
     *
     * @param username nombre del jugador.
     * @param avatar nombre del avatar.
     * @param initialHp vida inicial.
     */
    public Player(String username, String avatar, int initialHp) {
        this.username = username;
        this.avatar = avatar;
        this.hp = initialHp;
        this.score = 0;
        this.nivel = 1;
        this.x = 0.0;
    }

    public int getHp() { return hp; }

    public int getScore() { return score; }

    public int getNivel() { return nivel; }

    public double getX() { return x; }

    public String getUsername() { return username; }

    public String getAvatar() { return avatar; }

    public void setHp(int hp) { this.hp = hp; }

    public void setScore(int score) { this.score = score; }

    public void setNivel(int nivel) { this.nivel = nivel; }

    public void setX(double x) { this.x = x; }

    /**
     * Suma puntos al score actual.
     *
     * @param puntos cantidad a sumar.
     */
    public void sumarPuntos(int puntos) {
        this.score += puntos;
    }

    /**
     * Resta vida al jugador.
     *
     * @param dano daÃ±o recibido.
     */
    public void restarVida(int dano) {
        this.hp -= dano;
    }

    /**
     * Mueve al jugador hacia la izquierda.
     *
     * @param distancia distancia en pixeles.
     */
    public void moverIzquierda(double distancia) {
        this.x -= distancia;
    }

    /**
     * Mueve al jugador hacia la derecha.
     *
     * @param distancia distancia en pixeles.
     */
    public void moverDerecha(double distancia) {
        this.x += distancia;
    }

    /**
     * Determina si el jugador tiene vida.
     *
     * @return true si hp es mayor que cero.
     */
    public boolean estaVivo() {
        return this.hp > 0;
    }
}

