
package client.managers;

import client.data.Queue;
import client.models.KeyboardEvent;

/**
 * Administra eventos de entrada y la posiciÃ³n del jugador.
 */
public class InputManager {

    private final Queue<KeyboardEvent> inputBuffer;
    private double posicionJugador;
    private final double velocidadMovimiento;

    /**
     * Crea un InputManager con su estado inicial.
     *
     * @param initialX posiciÃ³n inicial del jugador.
     * @param velocidadMov velocidad de movimiento por paso.
     */
    public InputManager(double initialX, double velocidadMov) {
        this.inputBuffer = new Queue<>();
        this.posicionJugador = initialX;
        this.velocidadMovimiento = velocidadMov;
    }

    /**
     * Encola un evento de teclado.
     *
     * @param evento evento capturado por la UI.
     */
    public void encolarEvento(KeyboardEvent evento) {
        inputBuffer.enqueue(evento);
    }

    /**
     * Procesa y retorna el siguiente evento pendiente.
     *
     * @return siguiente evento o null si no hay eventos.
     */
    public KeyboardEvent procesarSiguienteEvento() {
        if (inputBuffer.isEmpty()) {
            return null;
        }
        return inputBuffer.dequeue();
    }

    /**
     * Indica si hay eventos pendientes por procesar.
     *
     * @return true si hay eventos en cola.
     */
    public boolean hayEventosPendientes() {
        return !inputBuffer.isEmpty();
    }

    /**
     * Retorna la cantidad de eventos pendientes.
     *
     * @return tamaÃ±o del buffer.
     */
    public int getCantidadEventosPendientes() {
        return inputBuffer.size();
    }

    /**
     * Retorna la posiciÃ³n horizontal actual del jugador.
     *
     * @return coordenada X del jugador.
     */
    public double getPosicionJugador() {
        return posicionJugador;
    }

    /**
     * Mueve al jugador hacia la izquierda.
     */
    public void moverIzquierda() {
        posicionJugador -= velocidadMovimiento;
    }

    /**
     * Mueve al jugador hacia la derecha.
     */
    public void moverDerecha() {
        posicionJugador += velocidadMovimiento;
    }

    /**
     * Ajusta la posiciÃ³n para que el jugador no salga de pantalla.
     *
     * @param anchoPantalla ancho lÃ­mite de pantalla.
     */
    public void validarLimites(double anchoPantalla) {
        if (posicionJugador < 0) {
            posicionJugador = 0;
        }
        if (posicionJugador > anchoPantalla) {
            posicionJugador = anchoPantalla;
        }
    }

    /**
     * Limpia todos los eventos pendientes.
     */
    public void limpiarBuffer() {
        inputBuffer.clear();
    }
}

