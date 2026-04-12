import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import client.models.GameConfig;

/**
 * Pantalla de inicio de sesión / registro.
 * Primer punto de entrada al juego después de la pantalla del logo.
 */
public class LoginFX {

    public Scene crearPantalla(Stage inicio, Scene escenaAnterior) {

        Group raiz = new Group();
        Scene escena = new Scene(raiz, 1136, 944);

        // ====================== FONDO ======================
        Image logo = new Image("file:media/Logo.png");
        ImageView verLogo = new ImageView(logo);
        verLogo.setX(0);
        verLogo.setY(0);

        // ====================== PANEL SEMITRANSPARENTE ======================
        javafx.scene.shape.Rectangle panel = new javafx.scene.shape.Rectangle(368, 250, 400, 420);
        panel.setArcWidth(20);
        panel.setArcHeight(20);
        panel.setFill(Color.color(0, 0, 0, 0.65));

        // ====================== TÍTULO ======================
        Label lblTitulo = new Label("BIENVENIDO");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        lblTitulo.setTextFill(Color.WHITE);
        lblTitulo.setLayoutX(480);
        lblTitulo.setLayoutY(280);

        // ====================== CAMPOS DE TEXTO ======================
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario");
        txtUsuario.setLayoutX(434);
        txtUsuario.setLayoutY(360);
        txtUsuario.setPrefWidth(268);
        txtUsuario.setPrefHeight(40);
        txtUsuario.setStyle("-fx-font-size: 16px; -fx-background-radius: 10;");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");
        txtPassword.setLayoutX(434);
        txtPassword.setLayoutY(430);
        txtPassword.setPrefWidth(268);
        txtPassword.setPrefHeight(40);
        txtPassword.setStyle("-fx-font-size: 16px; -fx-background-radius: 10;");

        Label lblMensaje = new Label();
        lblMensaje.setTextFill(Color.web("#FF5555"));
        lblMensaje.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblMensaje.setLayoutX(434);
        lblMensaje.setLayoutY(480);
        lblMensaje.setPrefWidth(268);
        lblMensaje.setAlignment(javafx.geometry.Pos.CENTER);

        // ====================== BOTONES ======================
        Button btnEntrar = new Button("INICIAR SESIÓN");
        btnEntrar.setLayoutX(434);
        btnEntrar.setLayoutY(520);
        btnEntrar.setPrefWidth(268);
        btnEntrar.setPrefHeight(45);
        btnEntrar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10;");

        Button btnRegistrar = new Button("REGISTRARSE");
        btnRegistrar.setLayoutX(434);
        btnRegistrar.setLayoutY(580);
        btnRegistrar.setPrefWidth(268);
        btnRegistrar.setPrefHeight(40);
        btnRegistrar.setStyle("-fx-background-color: transparent; -fx-text-fill: #88C0D0; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: #88C0D0; -fx-border-radius: 10;");

        // ====================== EVENTOS ======================
        btnEntrar.setOnAction(e -> {
            String user = txtUsuario.getText().trim();
            String pass = txtPassword.getText().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                lblMensaje.setText("Llene ambos campos.");
                return;
            }

            lblMensaje.setText("Conectando...");
            String respuesta = ServerConnection.sendAuth("LOGIN", user, pass);

            if ("AUTH_OK".equals(respuesta)) {
                lblMensaje.setText("Esperando oponente...");
                btnEntrar.setText("Buscando oponente...");
                btnEntrar.setDisable(true);
                btnRegistrar.setDisable(true);

                new Thread(() -> {
                    String configJson = ServerConnection.leerMensaje();
                    
                    if (configJson != null && configJson.contains("initialHp")) {
                        GameConfig config = new GameConfig();
                        config.setInitialHp(extraerCampoEntero(configJson, "initialHp", 100));
                        config.setBaseSpawnRate(extraerCampoEntero(configJson, "baseSpawnRate", 2000));
                        config.setScorePerKill(extraerCampoEntero(configJson, "scorePerKill", 10));

                        Platform.runLater(() -> {
                            GameScreenFX juego = new GameScreenFX();
                            Scene escenaJuego = juego.crearPantalla(inicio, "Mapa 1", "Avatar 1", user, config);
                            inicio.setScene(escenaJuego);
                        });
                    } else {
                        Platform.runLater(() -> {
                            lblMensaje.setText("Error leyendo config del server.");
                            btnEntrar.setText("INICIAR SESIÓN");
                            btnEntrar.setDisable(false);
                            btnRegistrar.setDisable(false);
                        });
                    }
                }).start();

            } else {
                lblMensaje.setText("Error: " + respuesta);
            }
        });

        btnRegistrar.setOnAction(e -> {
            String user = txtUsuario.getText().trim();
            String pass = txtPassword.getText().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                lblMensaje.setText("Llene ambos campos para registrar.");
                return;
            }

            // AHORA MANDA 'REGISTRO' QUE ES LO QUE ESPERA EL SERVER
            String respuesta = ServerConnection.sendAuth("REGISTRO", user, pass);

            if ("AUTH_OK".equals(respuesta)) {
                lblMensaje.setTextFill(Color.web("#55FF55"));
                lblMensaje.setText("Registro exitoso. Inicie sesión.");
                txtPassword.clear();
            } else {
                lblMensaje.setTextFill(Color.web("#FF5555"));
                lblMensaje.setText("Error: " + respuesta);
            }
        });

        // ====================== AGREGAR AL GRUPO ======================
        raiz.getChildren().addAll(verLogo, panel, lblTitulo, txtUsuario, txtPassword, lblMensaje, btnEntrar, btnRegistrar);

        return escena;
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
}