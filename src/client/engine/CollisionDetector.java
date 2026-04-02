
package client.engine;

import client.models.Attack;
import client.models.AttackType;
import client.models.Defensa;

/**
 * Detecta colisiones entre jugador/defensas y ataques.
 */
public class CollisionDetector {

    /**
     * Crea una instancia de detector de colisiones.
     */
    public CollisionDetector() {
    }

    /**
     * EvalÃºa si un ataque colisiona con una posiciÃ³n horizontal.
     *
     * @param ataque ataque a evaluar.
     * @param posicionJugador posiciÃ³n X del jugador.
     * @param rango rango permitido de colisiÃ³n.
     * @return true si hay colisiÃ³n.
     */
    public boolean colisiona(Attack ataque, double posicionJugador, double rango) {
        return Math.abs(ataque.getX() - posicionJugador) <= rango;
    }

    /**
     * EvalÃºa si una defensa neutraliza el tipo de ataque.
     *
     * @param ataque ataque entrante.
     * @param defensaActiva defensa seleccionada.
     * @return true si la defensa corresponde al ataque.
     */
    public boolean ataqueColisionalConDefensa(Attack ataque, Defensa defensaActiva) {
        if (ataque.getTipo() == AttackType.DDOS) {
            return defensaActiva == Defensa.FIREWALL;
        }

        if (ataque.getTipo() == AttackType.MALWARE) {
            return defensaActiva == Defensa.ANTIVIRUS;
        }

        return defensaActiva == Defensa.CRYPTO_SHIELD;
    }
}

