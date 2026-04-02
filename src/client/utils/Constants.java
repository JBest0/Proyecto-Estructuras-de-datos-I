package client.utils;

/**
 * Constantes generales del juego.
 */
public final class Constants {

    private Constants() {
    }

    // Pantalla
    public static final int SCREEN_WIDTH = 1136;
    public static final int SCREEN_HEIGHT = 944;

    // Juego
    public static final int INITIAL_HP = 100;
    public static final double DEFAULT_PLAYER_SPEED = 5.0;
    public static final int FRAME_RATE = 60;
    public static final long FRAME_DURATION_MS = 1000 / 60;

    // Posiciones iniciales
    public static final double INITIAL_PLAYER_X = 500;
    public static final double ATTACK_SPAWN_Y = 0;

    // Rango de colisiÃ³n
    public static final double COLLISION_RANGE = 30.0;
}
