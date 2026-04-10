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
        Label titulo = new Label("Cyber Defense Duel");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titulo.setTextFill(Color.WHITE);
        titulo.setLayoutX(390);
        titulo.setLayoutY(268);

        Label subtitulo = new Label("Inicia sesión para jugar");
        subtitulo.setFont(Font.font("Arial", 14));
        subtitulo.setTextFill(Color.LIGHTGRAY);
        subtitulo.setLayoutX(432);
        subtitulo.setLayoutY(300);

        // ====================== CAMPO USUARIO ======================
        Label labelUsuario = new Label("Usuario:");
        labelUsuario.setFont(Font.font("Arial", 14));
        labelUsuario.setTextFill(Color.WHITE);
        labelUsuario.setLayoutX(400);
        labelUsuario.setLayoutY(345);

        TextField campoUsuario = new TextField();
        campoUsuario.setPromptText("Tu nombre de usuario");
        campoUsuario.setLayoutX(400);
        campoUsuario.setLayoutY(365);
        campoUsuario.setPrefWidth(336);
        campoUsuario.setPrefHeight(38);

        // ====================== CAMPO CONTRASEÑA ======================
        Label labelContrasena = new Label("Contraseña:");
        labelContrasena.setFont(Font.font("Arial", 14));
        labelContrasena.setTextFill(Color.WHITE);
        labelContrasena.setLayoutX(400);
        labelContrasena.setLayoutY(418);

        PasswordField campoContrasena = new PasswordField();
        campoContrasena.setPromptText("Tu contraseña");
        campoContrasena.setLayoutX(400);
        campoContrasena.setLayoutY(438);
        campoContrasena.setPrefWidth(336);
        campoContrasena.setPrefHeight(38);

        // ====================== LABEL DE ERROR ======================
        Label labelError = new Label("");
        labelError.setFont(Font.font("Arial", 13));
        labelError.setTextFill(Color.web("#ff6b6b"));
        labelError.setLayoutX(400);
        labelError.setLayoutY(487);
        labelError.setPrefWidth(336);
        labelError.setWrapText(true);

        // ====================== BOTÓN INICIAR SESIÓN ======================
        Button botonLogin = new Button("Iniciar sesión");
        botonLogin.setLayoutX(400);
        botonLogin.setLayoutY(520);
        botonLogin.setPrefWidth(336);
        botonLogin.setPrefHeight(44);
        botonLogin.setStyle(
            "-fx-background-color: #00d084; -fx-text-fill: white; " +
            "-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 8;"
        );

        // ====================== BOTÓN REGISTRARSE ======================
        Button botonRegistro = new Button("Registrarse");
        botonRegistro.setLayoutX(400);
        botonRegistro.setLayoutY(576);
        botonRegistro.setPrefWidth(336);
        botonRegistro.setPrefHeight(44);
        botonRegistro.setStyle(
            "-fx-background-color: #3a86ff; -fx-text-fill: white; " +
            "-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 8;"
        );

        // ====================== BOTÓN MODO TEST ======================
        Button botonTest = new Button("Modo Test (sin servidor)");
        botonTest.setLayoutX(400);
        botonTest.setLayoutY(632);
        botonTest.setPrefWidth(336);
        botonTest.setPrefHeight(36);
        botonTest.setStyle(
            "-fx-background-color: #6c757d; -fx-text-fill: white; " +
            "-fx-font-size: 13px; -fx-background-radius: 8;"
        );
        botonTest.setOnAction(e -> {
            String usuario = campoUsuario.getText().trim();
            if (usuario.isEmpty()) usuario = "Jugador";
            SeleccionFX pantallaSeleccion = new SeleccionFX();
            inicio.setScene(pantallaSeleccion.crearPantalla(inicio, escena, usuario));
        });

        // ====================== BOTÓN VOLVER ======================
        Button botonVolver = new Button("Volver");
        botonVolver.setLayoutX(0);
        botonVolver.setLayoutY(0);
        botonVolver.setOnAction(e -> inicio.setScene(escenaAnterior));

        // ====================== ACCIONES DE BOTONES ======================

        botonLogin.setOnAction(e -> {
            String usuario     = campoUsuario.getText().trim();
            String contrasena  = campoContrasena.getText();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                labelError.setText("Por favor completa todos los campos.");
                return;
            }

            labelError.setText("Conectando...");
            labelError.setTextFill(Color.LIGHTGRAY);
            botonLogin.setDisable(true);
            botonRegistro.setDisable(true);

            new Thread(() -> {
                String respuesta = ServerConnection.sendAuth("LOGIN", usuario, contrasena);
                Platform.runLater(() -> {
                    botonLogin.setDisable(false);
                    botonRegistro.setDisable(false);
                    procesarRespuesta(respuesta, usuario, inicio, escena, labelError);
                });
            }).start();
        });

        botonRegistro.setOnAction(e -> {
            String usuario    = campoUsuario.getText().trim();
            String contrasena = campoContrasena.getText();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                labelError.setText("Por favor completa todos los campos.");
                labelError.setTextFill(Color.web("#ff6b6b"));
                return;
            }

            labelError.setText("Registrando...");
            labelError.setTextFill(Color.LIGHTGRAY);
            botonLogin.setDisable(true);
            botonRegistro.setDisable(true);

            new Thread(() -> {
                String respuesta = ServerConnection.sendAuth("REGISTER", usuario, contrasena);
                Platform.runLater(() -> {
                    botonLogin.setDisable(false);
                    botonRegistro.setDisable(false);
                    procesarRespuesta(respuesta, usuario, inicio, escena, labelError);
                });
            }).start();
        });

        // ====================== CONSTRUIR ESCENA ======================
        raiz.getChildren().add(verLogo);
        raiz.getChildren().add(panel);
        raiz.getChildren().add(titulo);
        raiz.getChildren().add(subtitulo);
        raiz.getChildren().add(labelUsuario);
        raiz.getChildren().add(campoUsuario);
        raiz.getChildren().add(labelContrasena);
        raiz.getChildren().add(campoContrasena);
        raiz.getChildren().add(labelError);
        raiz.getChildren().add(botonLogin);
        raiz.getChildren().add(botonRegistro);
        raiz.getChildren().add(botonTest);
        raiz.getChildren().add(botonVolver);

        return escena;
    }

    // ----------------------------------------------------------------
    //  Procesa la respuesta JSON del servidor (parsing manual simple)
    // ----------------------------------------------------------------
    private void procesarRespuesta(String jsonRespuesta, String username,
                                   Stage inicio, Scene escenaActual, Label labelError) {
        if (jsonRespuesta == null) {
            labelError.setText("Error: respuesta nula del servidor.");
            labelError.setTextFill(Color.web("#ff6b6b"));
            return;
        }

        if (jsonRespuesta.contains("\"AUTH_OK\"")) {
            // Transición a la pantalla de selección de personaje
            SeleccionFX pantallaSeleccion = new SeleccionFX();
            inicio.setScene(pantallaSeleccion.crearPantalla(inicio, escenaActual, username));

        } else {
            // Extraer el campo "reason" del JSON de forma simple
            String razon = extraerCampo(jsonRespuesta, "reason");
            if (razon == null || razon.isEmpty()) {
                razon = "Error de autenticación desconocido.";
            }
            labelError.setText(razon);
            labelError.setTextFill(Color.web("#ff6b6b"));
        }
    }

    /**
     * Extrae el valor de un campo JSON de texto plano.
     * Ejemplo: {"type":"AUTH_FAIL","reason":"Usuario no encontrado"}
     *          → extraerCampo(json, "reason") → "Usuario no encontrado"
     */
    private String extraerCampo(String json, String campo) {
        String clave = "\"" + campo + "\":\"";
        int inicio = json.indexOf(clave);
        if (inicio == -1) return null;
        inicio += clave.length();
        int fin = json.indexOf("\"", inicio);
        if (fin == -1) return null;
        return json.substring(inicio, fin);
    }
}
