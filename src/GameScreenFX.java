import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import client.models.GameConfig;

public class GameScreenFX {

    // Asumo que estas clases existen en el proyecto de tus compañeros
    // Ataques attack = new Ataques();
    // Enemigos enemy = new Enemigos();

    private long tiempoInicio = 0;
    private Label labelTiempo;

    private double vidaActual = 100.0;
    private ProgressBar barraVida;
    private Label labelVida;
    private Label labelGameOver;
    private Label labelOponente;

    private int scoreActual = 0;
    private int nivelActual = 1;
    private int scorePorKill = 10;
    private int scorePasoNivel = 100;
    private long ultimoSyncEstadoMs = 0;
    private AnimationTimer timerGlobal;
    private boolean gameOver = false;

    public Scene crearPantalla(Stage stagePrincipal, String mapa, String avatar, String username, GameConfig configPartida) {

        if (configPartida != null) {
            vidaActual = configPartida.getInitialHp();
            scorePorKill = configPartida.getScorePerKill();
            // baseSpawnRate se usaría en la lógica de enemigos
        }

        Pane root = new Pane();
        Scene escena = new Scene(root, 1136, 944);

        root.setStyle("-fx-background-color: #2b2b2b;");

        // Título o nombre de usuario
        Label lblUsername = new Label("Jugador: " + username);
        lblUsername.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblUsername.setTextFill(Color.WHITE);
        lblUsername.setLayoutX(20);
        lblUsername.setLayoutY(20);

        // Barra de Vida Jugador
        barraVida = new ProgressBar(1.0);
        barraVida.setLayoutX(20);
        barraVida.setLayoutY(50);
        barraVida.setPrefWidth(300);
        barraVida.setStyle("-fx-accent: #4CAF50;");

        labelVida = new Label("HP: " + (int) vidaActual);
        labelVida.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        labelVida.setTextFill(Color.WHITE);
        labelVida.setLayoutX(330);
        labelVida.setLayoutY(50);

        // Estado Oponente
        labelOponente = new Label("Oponente HP: ? | Score: ? | Nivel: ?");
        labelOponente.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        labelOponente.setTextFill(Color.web("#FFaa00"));
        labelOponente.setLayoutX(800);
        labelOponente.setLayoutY(20);

        // Tiempo
        labelTiempo = new Label("Tiempo: 0s");
        labelTiempo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        labelTiempo.setTextFill(Color.WHITE);
        labelTiempo.setLayoutX(500);
        labelTiempo.setLayoutY(20);

        // Game Over Label
        labelGameOver = new Label("GAME OVER");
        labelGameOver.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        labelGameOver.setTextFill(Color.RED);
        labelGameOver.setLayoutX(380);
        labelGameOver.setLayoutY(400);
        labelGameOver.setVisible(false);

        root.getChildren().addAll(lblUsername, barraVida, labelVida, labelOponente, labelTiempo, labelGameOver);

        escena.setOnKeyPressed(event -> {
            if (gameOver) {
                if (event.getCode() == KeyCode.SPACE) {
                    // volverAlMenu(stagePrincipal);
                }
                return;
            }

            if (event.getCode() == KeyCode.Q || event.getCode() == KeyCode.W || event.getCode() == KeyCode.E) {
                registrarEliminacion();
            } else if (event.getCode() == KeyCode.SPACE) {
                recibirDano(10);
            }
        });

        tiempoInicio = System.currentTimeMillis();

        timerGlobal = new AnimationTimer() {
            @Override
            public void handle(long nowMs) {
                if (gameOver) return;

                long msActual = System.currentTimeMillis();
                long segundos = (msActual - tiempoInicio) / 1000;
                labelTiempo.setText("Tiempo: " + segundos + "s | Score: " + scoreActual + " | Nivel: " + nivelActual);

                // --- RECIBIR ESTADO OPONENTE ---
                String estadoOp = ServerConnection.leerMensajeAsync();
                if (estadoOp != null) {
                    int hpOp = extraerCampoEntero(estadoOp, "hp", 0);
                    int scoreOp = extraerCampoEntero(estadoOp, "score", 0);
                    int nivelOp = extraerCampoEntero(estadoOp, "nivel", 1);
                    
                    labelOponente.setText("Oponente HP: " + hpOp + " | Score: " + scoreOp + " | Nivel: " + nivelOp);
                }

                // --- ENVIAR MI ESTADO (Aprox cada 100ms) ---
                if (msActual - ultimoSyncEstadoMs > 100) {
                    String miEstadoJson = "{\"hp\":" + (int)vidaActual + ",\"score\":" + scoreActual + ",\"nivel\":" + nivelActual + "}";
                    ServerConnection.enviarEstado(miEstadoJson);
                    ultimoSyncEstadoMs = msActual;
                }
            }
        };
        timerGlobal.start();

        return escena;
    }

    private void recibirDano(double cantidad) {
        if (gameOver) return;
        vidaActual -= cantidad;
        if (vidaActual < 0) vidaActual = 0;
        
        barraVida.setProgress(vidaActual / 100.0);
        labelVida.setText("HP: " + (int) vidaActual);

        if (vidaActual <= 30) barraVida.setStyle("-fx-accent: red;");

        if (vidaActual <= 0 && !gameOver) {
            gameOver = true;
            labelGameOver.setVisible(true);
            
            // Avisar al servidor que perdí
            String miEstadoJson = "{\"hp\":0,\"score\":" + scoreActual + ",\"nivel\":" + nivelActual + "}";
            ServerConnection.enviarEstado(miEstadoJson);
            
            System.out.println("¡GAME OVER! Esperando finalización...");
        }
    }

    private int extraerCampoEntero(String json, String campo, int valorDefecto) {
        if (json == null) return valorDefecto;
        String clave = "\"" + campo + "\":";
        int inicio = json.indexOf(clave);
        if (inicio == -1) return valorDefecto;
        inicio += clave.length();

        int fin = inicio;
        while (fin < json.length()) {
            char c = json.charAt(fin);
            if ((c >= '0' && c <= '9') || c == '-') {
                fin++;
            } else {
                break;
            }
        }

        if (fin <= inicio) return valorDefecto;
        try {
            return Integer.parseInt(json.substring(inicio, fin));
        } catch (NumberFormatException ex) {
            return valorDefecto;
        }
    }

    private void registrarEliminacion() {
        scoreActual += scorePorKill;
        if (scorePasoNivel > 0) {
            nivelActual = 1 + (scoreActual / scorePasoNivel);
        }
    }
}