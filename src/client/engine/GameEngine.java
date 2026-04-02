
package client.engine;

import client.managers.AttackManager;
import client.managers.DefenseHistoryManager;
import client.managers.InputManager;
import client.managers.PlayerStateManager;
import client.models.GameConfig;
import client.models.Player;
import client.utils.Constants;

/**
 * Orquestador principal del bucle de juego.
 */
public class GameEngine {

    private final AttackManager attackManager;
    private final InputManager inputManager;
    private final DefenseHistoryManager defenseHistoryManager;
    private final PlayerStateManager playerStateManager;

    private final GameConfig config;

    private boolean gameRunning;
    private long ultimoSpawn;

    /**
     * Construye el motor de juego con su configuraciÃ³n y jugador.
     *
     * @param config configuraciÃ³n de partida.
     * @param player jugador local.
     */
    public GameEngine(GameConfig config, Player player) {
        this.config = config;
        this.attackManager = new AttackManager();
        this.inputManager = new InputManager(Constants.INITIAL_PLAYER_X, Constants.DEFAULT_PLAYER_SPEED);
        this.defenseHistoryManager = new DefenseHistoryManager();
        this.playerStateManager = new PlayerStateManager(player);
        this.gameRunning = false;
        this.ultimoSpawn = 0L;
    }

    /**
     * Inicializa recursos del motor.
     */
    public void init() {
        gameRunning = true;
        ultimoSpawn = System.currentTimeMillis();
    }

    /**
     * Actualiza un frame de lÃ³gica de juego.
     */
    public void update() {
        // ImplementaciÃ³n futura del game loop completo.
    }

    /**
     * Renderiza el estado actual en la UI.
     */
    public void render() {
        // ImplementaciÃ³n futura de render.
    }

    /**
     * Libera recursos y detiene el motor.
     */
    public void shutdown() {
        gameRunning = false;
        attackManager.limpiarAtaques();
        inputManager.limpiarBuffer();
    }

    /**
     * Retorna el manager de ataques.
     *
     * @return instancia de AttackManager.
     */
    public AttackManager getAttackManager() {
        return attackManager;
    }

    /**
     * Retorna el manager de inputs.
     *
     * @return instancia de InputManager.
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * Retorna el manager de estado del jugador.
     *
     * @return instancia de PlayerStateManager.
     */
    public PlayerStateManager getPlayerStateManager() {
        return playerStateManager;
    }

    /**
     * Retorna el nivel actual del jugador.
     *
     * @return nivel actual.
     */
    public int getNivelActual() {
        return playerStateManager.getNivel();
    }
}

