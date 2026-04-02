package client.models;

/**
 * Modelo de un ataque activo en pantalla.
 */
public class Attack {

    private final String id;
    private final AttackType tipo;
    private double x;
    private double y;
    private final double velocidad;
    private final long tiempoCreacion;

    /**
     * Construye un ataque con sus datos iniciales.
     *
     * @param tipo tipo de ataque.
     * @param x coordenada X inicial.
     * @param y coordenada Y inicial.
     * @param velocidad velocidad vertical por frame.
     */
    public Attack(AttackType tipo, double x, double y, double velocidad) {
        this.id = "atk-" + System.nanoTime();
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.tiempoCreacion = System.currentTimeMillis();
    }

    /**
     * Retorna el tipo del ataque.
     *
     * @return tipo de ataque.
     */
    public AttackType getTipo() {
        return tipo;
    }

    /**
     * Retorna la posiciÃ³n X del ataque.
     *
     * @return coordenada X.
     */
    public double getX() {
        return x;
    }

    /**
     * Retorna la posiciÃ³n Y del ataque.
     *
     * @return coordenada Y.
     */
    public double getY() {
        return y;
    }

    /**
     * Retorna la velocidad vertical del ataque.
     *
     * @return velocidad por frame.
     */
    public double getVelocidad() {
        return velocidad;
    }

    /**
     * Retorna el identificador Ãºnico del ataque.
     *
     * @return id Ãºnico.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna el timestamp de creaciÃ³n.
     *
     * @return tiempo en milisegundos.
     */
    public long getTiempoCreacion() {
        return tiempoCreacion;
    }

    /**
     * Actualiza la coordenada X del ataque.
     *
     * @param x nueva coordenada X.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Actualiza la coordenada Y del ataque.
     *
     * @param y nueva coordenada Y.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Mueve el ataque hacia abajo segÃºn su velocidad.
     */
    public void actualizarPosicion() {
        this.y += this.velocidad;
    }

    /**
     * Determina si el ataque saliÃ³ del mapa.
     *
     * @param alturaPantalla alto mÃ¡ximo visible.
     * @return true si la coordenada Y supera la altura.
     */
    public boolean estaFueraDelMapa(int alturaPantalla) {
        return this.y > alturaPantalla;
    }

    @Override
    public String toString() {
        return "Attack{" +
            "id='" + id + '\'' +
            ", tipo=" + tipo +
            ", x=" + x +
            ", y=" + y +
            ", velocidad=" + velocidad +
            ", tiempoCreacion=" + tiempoCreacion +
            '}';
    }
}

