import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    Ataques attack = new Ataques();
    Enemigos enemy = new Enemigos();

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

    private AnimationTimer moverEnemigoTimer;
    private AnimationTimer timerGlobal;
    private boolean gameOver = false;

    public Scene crearPantalla(Stage stagePrincipal, String mapa, String avatar, String username, GameConfig configPartida) {

        if (configPartida != null) {
            vidaActual = configPartida.getInitialHp();
            scorePorKill = configPartida.getScorePerKill();
            scorePasoNivel = configPartida.getDifficultyStepScore();
        }

        Pane raizJuego = new Pane();
        Scene juego = new Scene(raizJuego, 1136, 944);

        // ====================== MAPA ======================
        Image mapaSeleccion = new Image("file:media/" + mapa + ".png");
        ImageView verMapaSeleccion = new ImageView(mapaSeleccion);
        verMapaSeleccion.setFitWidth(1136);
        verMapaSeleccion.setFitHeight(944);

        // ====================== PERSONAJE ======================
        Image personaje = new Image("file:media/" + avatar + ".png");
        ImageView verPersonaje = new ImageView(personaje);
        verPersonaje.setFitWidth(200);
        verPersonaje.setFitHeight(200);
        verPersonaje.setPreserveRatio(true);
        verPersonaje.setLayoutX(468);
        verPersonaje.setLayoutY(644);

        // ====================== CONTADOR DE TIEMPO ======================
        labelTiempo = new Label("Tiempo: 0:00");
        labelTiempo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        labelTiempo.setTextFill(Color.WHITE);
        labelTiempo.setLayoutX(420);
        labelTiempo.setLayoutY(20);

        // ====================== BARRA DE VIDA ======================
        barraVida = new ProgressBar(1.0);
        barraVida.setPrefWidth(300);
        barraVida.setLayoutX(20);
        barraVida.setLayoutY(60);
        barraVida.setStyle("-fx-accent: limegreen;");

        labelVida = new Label("Vida: 100%");
        labelVida.setFont(Font.font("Arial", 18));
        labelVida.setTextFill(Color.WHITE);
        labelVida.setLayoutX(20);
        labelVida.setLayoutY(85);

        // ====================== ETIQUETA DE JUGADOR ======================
        Label labelJugador = new Label("Jugador: " + username);
        labelJugador.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        labelJugador.setTextFill(Color.web("#00d084"));
        labelJugador.setLayoutX(20);
        labelJugador.setLayoutY(20);

        // ====================== ESTADO OPONENTE ======================
        labelOponente = new Label("Oponente HP: ? | Score: ? | Nivel: ?");
        labelOponente.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        labelOponente.setTextFill(Color.web("#FFaa00"));
        labelOponente.setLayoutX(750);
        labelOponente.setLayoutY(20);

        // ====================== GAME OVER ======================
        labelGameOver = new Label("GAME OVER!");
        labelGameOver.setFont(Font.font("Arial", FontWeight.BOLD, 72));
        labelGameOver.setTextFill(Color.RED);
        labelGameOver.setLayoutX(400);
        labelGameOver.setLayoutY(380);
        labelGameOver.setVisible(false);

        raizJuego.getChildren().add(labelGameOver);

        // ====================== ENEMIGO ======================
        ImageView[] enemigoActual = new ImageView[1];
        String[] tipoEnemigoActual = new String[1];

        // ====================== MOVIMIENTO DEL ENEMIGO ======================
        moverEnemigoTimer = new AnimationTimer() {
            private boolean primerCiclo = true;

            @Override
            public void handle(long now) {
                if (gameOver) return;

                if (enemigoActual[0] == null) {
                    tipoEnemigoActual[0] = enemy.generarTipoAleatorio();
                    enemigoActual[0] = enemy.generarEnemigo(tipoEnemigoActual[0], raizJuego);
                    enemigoActual[0].setLayoutY(-280);
                    primerCiclo = true;
                    return;
                }

                enemigoActual[0].setLayoutY(enemigoActual[0].getLayoutY() + 2);

                if (primerCiclo) {
                    primerCiclo = false;
                    return;
                }

                if (enemigoActual[0].getLayoutY() > 780) {
                    raizJuego.getChildren().remove(enemigoActual[0]);

                    vidaActual -= 60;
                    if (vidaActual < 0) vidaActual = 0;
                    actualizarBarraVida();

                    tipoEnemigoActual[0] = enemy.generarTipoAleatorio();
                    enemigoActual[0] = enemy.generarEnemigo(tipoEnemigoActual[0], raizJuego);
                    enemigoActual[0].setLayoutY(-280);
                    primerCiclo = true;
                }
            }
        };

        // ====================== TIMER GLOBAL ======================
        timerGlobal = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (tiempoInicio == 0) tiempoInicio = now;
                if (gameOver) return;

                long segundosTotal = (now - tiempoInicio) / 1_000_000_000;
                long minutos = segundosTotal / 60;
                long segundos = segundosTotal % 60;
                labelTiempo.setText(String.format("Tiempo: %d:%02d | Score: %d | Nivel: %d", minutos, segundos, scoreActual, nivelActual));

                // Recibir estado oponente
                String estadoOp = ServerConnection.leerMensajeAsync();
                if (estadoOp != null) {
                    int hpOp = extraerCampoEntero(estadoOp, "hp", 0);
                    int scoreOp = extraerCampoEntero(estadoOp, "score", 0);
                    int nivelOp = extraerCampoEntero(estadoOp, "nivel", 1);
                    labelOponente.setText("Oponente HP: " + hpOp + " | Score: " + scoreOp + " | Nivel: " + nivelOp);
                }

                // Enviar mi estado cada 100ms
                long msActual = System.currentTimeMillis();
                if (msActual - ultimoSyncEstadoMs > 100) {
                    String miEstado = "{\"hp\":" + (int) vidaActual + ",\"score\":" + scoreActual + ",\"nivel\":" + nivelActual + "}";
                    ServerConnection.enviarEstado(miEstado);
                    ultimoSyncEstadoMs = msActual;
                }
            }
        };

        // ====================== CONTROLES DE TECLADO ======================
        juego.setOnKeyPressed(e -> {
            if (gameOver) {
                if (e.getCode() == KeyCode.SPACE) {
                    volverAlMenu(stagePrincipal);
                }
                return;
            }

            if (e.getCode() == KeyCode.A) {
                if (verPersonaje.getLayoutX() > -40)
                    verPersonaje.setLayoutX(verPersonaje.getLayoutX() - 10);
            }
            if (e.getCode() == KeyCode.D) {
                if (verPersonaje.getLayoutX() < 976)
                    verPersonaje.setLayoutX(verPersonaje.getLayoutX() + 10);
            }

            // Ataque Q - Firewall (DDoS)
            if (e.getCode() == KeyCode.Q) {
                long ahora = System.nanoTime();
                if (ahora - attack.getUltimoDisparo() < attack.getCooldown()) return;
                attack.setUltimoDisparo(ahora);

                ImageView verFirewall = attack.generarAtaque("file:media/Firewall.png", 220, 220, -20, -20,
                        raizJuego, verPersonaje, true);

                AnimationTimer moverFirewall = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if (gameOver) { stop(); return; }
                        attack.moverAtaque(verFirewall);

                        if (verFirewall.getLayoutY() + verFirewall.getFitHeight() < 0) {
                            raizJuego.getChildren().remove(verFirewall);
                            stop();
                            return;
                        }

                        if (tipoEnemigoActual[0] != null && tipoEnemigoActual[0].equals("DDoS") &&
                                verFirewall.getBoundsInParent().intersects(enemigoActual[0].getBoundsInParent())) {

                            raizJuego.getChildren().removeAll(verFirewall, enemigoActual[0]);
                            registrarEliminacion();
                            tipoEnemigoActual[0] = enemy.generarTipoAleatorio();
                            enemigoActual[0] = enemy.generarEnemigo(tipoEnemigoActual[0], raizJuego);
                            enemigoActual[0].setLayoutY(-250);
                            stop();
                        }
                    }
                };
                moverFirewall.start();
            }

            // Ataque W - Antivirus (Malware)
            if (e.getCode() == KeyCode.W) {
                long ahora = System.nanoTime();
                if (ahora - attack.getUltimoDisparo() < attack.getCooldown()) return;
                attack.setUltimoDisparo(ahora);

                ImageView verAntivirus = attack.generarAtaque("file:media/Antivirus.png", 100, 130,
                        30, -20, raizJuego, verPersonaje, false);

                AnimationTimer moverAntivirus = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if (gameOver) { stop(); return; }
                        attack.moverAtaque(verAntivirus);

                        if (verAntivirus.getLayoutY() + verAntivirus.getFitHeight() < 0) {
                            raizJuego.getChildren().remove(verAntivirus);
                            stop();
                            return;
                        }

                        if (tipoEnemigoActual[0] != null && tipoEnemigoActual[0].equals("Malware") &&
                                verAntivirus.getBoundsInParent().intersects(enemigoActual[0].getBoundsInParent())) {

                            raizJuego.getChildren().removeAll(verAntivirus, enemigoActual[0]);
                            registrarEliminacion();
                            tipoEnemigoActual[0] = enemy.generarTipoAleatorio();
                            enemigoActual[0] = enemy.generarEnemigo(tipoEnemigoActual[0], raizJuego);
                            enemigoActual[0].setLayoutY(-250);
                            stop();
                        }
                    }
                };
                moverAntivirus.start();
            }

            // Ataque E - Crypto Shield (Credential Attack)
            if (e.getCode() == KeyCode.E) {
                long ahora = System.nanoTime();
                if (ahora - attack.getUltimoDisparo() < attack.getCooldown()) return;
                attack.setUltimoDisparo(ahora);

                ImageView verCryptoShield = attack.generarAtaque("file:media/Crypto Shield.png", 140, 140,
                        30, -20, raizJuego, verPersonaje, true);

                AnimationTimer moverCryptoShield = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if (gameOver) { stop(); return; }
                        attack.moverAtaque(verCryptoShield);

                        if (verCryptoShield.getLayoutY() + verCryptoShield.getFitHeight() < 0) {
                            raizJuego.getChildren().remove(verCryptoShield);
                            stop();
                            return;
                        }

                        if (tipoEnemigoActual[0] != null && tipoEnemigoActual[0].equals("Credential Attack") &&
                                verCryptoShield.getBoundsInParent().intersects(enemigoActual[0].getBoundsInParent())) {

                            raizJuego.getChildren().removeAll(verCryptoShield, enemigoActual[0]);
                            registrarEliminacion();
                            tipoEnemigoActual[0] = enemy.generarTipoAleatorio();
                            enemigoActual[0] = enemy.generarEnemigo(tipoEnemigoActual[0], raizJuego);
                            enemigoActual[0].setLayoutY(-250);
                            stop();
                        }
                    }
                };
                moverCryptoShield.start();
            }
        });

        raizJuego.getChildren().addAll(verMapaSeleccion, verPersonaje, labelTiempo, barraVida, labelVida, labelJugador, labelOponente);

        moverEnemigoTimer.start();
        timerGlobal.start();

        return juego;
    }

    private void actualizarBarraVida() {
        double progreso = vidaActual / 100.0;
        barraVida.setProgress(progreso);
        labelVida.setText("Vida: " + (int) vidaActual + "%");

        if (vidaActual > 50) barraVida.setStyle("-fx-accent: limegreen;");
        else if (vidaActual > 25) barraVida.setStyle("-fx-accent: orange;");
        else barraVida.setStyle("-fx-accent: red;");

        if (vidaActual <= 0 && !gameOver) {
            gameOver = true;
            labelGameOver.setVisible(true);
            String miEstado = "{\"hp\":0,\"score\":" + scoreActual + ",\"nivel\":" + nivelActual + "}";
            ServerConnection.enviarEstado(miEstado);
            System.out.println("¡GAME OVER! Presiona ESPACIO para volver al menú");
        }
    }

    private void registrarEliminacion() {
        scoreActual += scorePorKill;
        if (scorePasoNivel > 0) {
            nivelActual = 1 + (scoreActual / scorePasoNivel);
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
            if ((c >= '0' && c <= '9') || c == '-') fin++;
            else break;
        }

        if (fin <= inicio) return valorDefecto;
        try {
            return Integer.parseInt(json.substring(inicio, fin));
        } catch (NumberFormatException ex) {
            return valorDefecto;
        }
    }

    private void volverAlMenu(Stage stagePrincipal) {
        Platform.runLater(() -> {
            try {
                MainFX main = new MainFX();
                main.start(stagePrincipal);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
