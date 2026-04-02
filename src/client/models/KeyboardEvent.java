package client.models;

/**
 * Evento de teclado encolado para procesar durante el game loop.
 */
public class KeyboardEvent {

    /**
     * Tipos de teclas relevantes para el juego.
     */
    public enum KeyType {
        ARROW_LEFT,
        ARROW_RIGHT,
        DEFENSE_Q,
        DEFENSE_W,
        DEFENSE_E
    }

    private final KeyType tipo;
    private final long timestamp;

    /**
     * Construye un evento de teclado con marca de tiempo actual.
     *
     * @param tipo tipo de tecla capturada.
     */
    public KeyboardEvent(KeyType tipo) {
        this.tipo = tipo;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Retorna el tipo de tecla.
     *
     * @return tipo del evento.
     */
    public KeyType getTipo() {
        return tipo;
    }

    /**
     * Retorna el instante de creaciÃ³n.
     *
     * @return timestamp en milisegundos.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Convierte una tecla de defensa a su enum de defensa correspondiente.
     *
     * @return defensa asociada o null si no es una tecla de defensa.
     */
    public Defensa toDefensa() {
        if (tipo == KeyType.DEFENSE_Q) {
            return Defensa.FIREWALL;
        }
        if (tipo == KeyType.DEFENSE_W) {
            return Defensa.ANTIVIRUS;
        }
        if (tipo == KeyType.DEFENSE_E) {
            return Defensa.CRYPTO_SHIELD;
        }
        return null;
    }
}

