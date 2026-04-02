package client.ui;

import client.engine.GameEngine;
import client.managers.InputManager;
import client.models.KeyboardEvent;
import client.models.KeyboardEvent.KeyType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Pantalla principal de juego en JavaFX.
 */
public final class GameScreenFX {

    private GameScreenFX() {
    }

    /**
     * Crea la escena principal de juego y registra eventos de teclado.
     *
     * @param stage stage principal.
     * @param engine motor del juego.
     * @return escena configurada.
     */
    public static Scene crearPantalla(Stage stage, GameEngine engine) {
        Group root = new Group();
        Scene scene = new Scene(root, 1136, 944);

        scene.setOnKeyPressed(event -> {
            InputManager inputManager = engine.getInputManager();
            switch (event.getCode()) {
                case LEFT:
                    inputManager.encolarEvento(new KeyboardEvent(KeyType.ARROW_LEFT));
                    break;
                case RIGHT:
                    inputManager.encolarEvento(new KeyboardEvent(KeyType.ARROW_RIGHT));
                    break;
                case Q:
                    inputManager.encolarEvento(new KeyboardEvent(KeyType.DEFENSE_Q));
                    break;
                case W:
                    inputManager.encolarEvento(new KeyboardEvent(KeyType.DEFENSE_W));
                    break;
                case E:
                    inputManager.encolarEvento(new KeyboardEvent(KeyType.DEFENSE_E));
                    break;
                default:
                    break;
            }
        });

        return scene;
    }
}

