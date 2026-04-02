
package client.engine;

import client.models.Attack;
import client.models.AttackType;
import client.models.GameConfig;
import client.utils.Constants;

/**
 * Genera ataques para el flujo del juego.
 */
public class AttackSpawner {

    private final GameConfig config;

    /**
     * Construye el spawner con configuraciÃ³n base.
     *
     * @param config configuraciÃ³n del juego.
     */
    public AttackSpawner(GameConfig config) {
        this.config = config;
    }

    /**
     * Genera un ataque de tipo pseudoaleatorio bÃ¡sico.
     *
     * @return nuevo ataque generado.
     */
    public Attack generarAaque() {
        long selector = System.nanoTime() % 3;
        AttackType tipo;

        if (selector == 0) {
            tipo = AttackType.DDOS;
        } else if (selector == 1) {
            tipo = AttackType.MALWARE;
        } else {
            tipo = AttackType.CREDENTIAL_ATTACK;
        }

        return generarAaque(tipo);
    }

    /**
     * Genera un ataque de un tipo especÃ­fico.
     *
     * @param tipo tipo de ataque.
     * @return nuevo ataque del tipo indicado.
     */
    public Attack generarAaque(AttackType tipo) {
        double randomX = (System.nanoTime() % Constants.SCREEN_WIDTH);
        return new Attack(tipo, randomX, Constants.ATTACK_SPAWN_Y, config.getBaseAttackSpeed());
    }
}

