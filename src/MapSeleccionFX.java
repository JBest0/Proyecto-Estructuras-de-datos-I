import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MapSeleccionFX {

    private String mapaSeleccionado;

    public Scene crearPantalla(Stage inicio, Scene escenaAnterior, String avatarSeleccionado) {
        Group raiz = new Group();
        Scene escena = new Scene(raiz, 1136, 944, Color.BEIGE);

        Label titulo = new Label("Selecciona tu mapa");
        titulo.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #1d1d1d;");
        titulo.setLayoutX(380);
        titulo.setLayoutY(70);

        Label subtitulo = new Label("Elige el escenario para defender la red");
        subtitulo.setStyle("-fx-font-size: 18px; -fx-text-fill: #3a3a3a;");
        subtitulo.setLayoutX(385);
        subtitulo.setLayoutY(130);

        Image dojoImg = new Image("file:media/Data Center Dojo.png");
        ImageView dojoView = new ImageView(dojoImg);
        dojoView.setLayoutX(160);
        dojoView.setLayoutY(260);
        dojoView.setFitWidth(360);
        dojoView.setFitHeight(360);
        dojoView.setPreserveRatio(false);

        Rectangle dojoRect = new Rectangle(160, 260, 360, 360);
        dojoRect.setArcWidth(20);
        dojoRect.setArcHeight(20);
        dojoRect.setFill(Color.TRANSPARENT);
        dojoRect.setStroke(Color.TRANSPARENT);
        dojoRect.setStrokeWidth(6);

        Label dojoNombre = new Label("Data Center Dojo");
        dojoNombre.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");
        dojoNombre.setLayoutX(195);
        dojoNombre.setLayoutY(640);

        Label dojoSabor = new Label("Pasillos de servidores y reflejos de neon.");
        dojoSabor.setStyle("-fx-font-size: 16px; -fx-text-fill: #2d2d2d;");
        dojoSabor.setLayoutX(165);
        dojoSabor.setLayoutY(680);

        Image bayImg = new Image("file:media/Packet Bay Carnival.png");
        ImageView bayView = new ImageView(bayImg);
        bayView.setLayoutX(616);
        bayView.setLayoutY(260);
        bayView.setFitWidth(360);
        bayView.setFitHeight(360);
        bayView.setPreserveRatio(false);

        Rectangle bayRect = new Rectangle(616, 260, 360, 360);
        bayRect.setArcWidth(20);
        bayRect.setArcHeight(20);
        bayRect.setFill(Color.TRANSPARENT);
        bayRect.setStroke(Color.TRANSPARENT);
        bayRect.setStrokeWidth(6);

        Label bayNombre = new Label("Packet Bay Carnival");
        bayNombre.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");
        bayNombre.setLayoutX(633);
        bayNombre.setLayoutY(640);

        Label baySabor = new Label("Luces electricas y trafico en marea alta.");
        baySabor.setStyle("-fx-font-size: 16px; -fx-text-fill: #2d2d2d;");
        baySabor.setLayoutX(620);
        baySabor.setLayoutY(680);

        Label estadoSeleccion = new Label("Mapa seleccionado: ninguno");
        estadoSeleccion.setStyle("-fx-font-size: 18px; -fx-text-fill: #1f1f1f;");
        estadoSeleccion.setLayoutX(430);
        estadoSeleccion.setLayoutY(760);

        Button confirmar = new Button("Confirmar");
        confirmar.setLayoutX(540);
        confirmar.setLayoutY(830);
        confirmar.setPrefSize(110, 44);
        confirmar.setDisable(true);

        Button volver = new Button("Volver");
        volver.setLayoutX(30);
        volver.setLayoutY(25);
        volver.setOnAction(e -> inicio.setScene(escenaAnterior));

        dojoRect.setOnMouseClicked(e -> {
            mapaSeleccionado = "Data Center Dojo";
            estadoSeleccion.setText("Mapa seleccionado: " + mapaSeleccionado);
            dojoRect.setStroke(Color.web("#f39c12"));
            bayRect.setStroke(Color.TRANSPARENT);
            confirmar.setDisable(false);
        });

        bayRect.setOnMouseClicked(e -> {
            mapaSeleccionado = "Packet Bay Carnival";
            estadoSeleccion.setText("Mapa seleccionado: " + mapaSeleccionado);
            bayRect.setStroke(Color.web("#00d084"));
            dojoRect.setStroke(Color.TRANSPARENT);
            confirmar.setDisable(false);
        });

        GameScreenFX pantallaJuego = new GameScreenFX();

        confirmar.setOnAction(e -> {

            inicio.setScene(pantallaJuego.crearPantalla(inicio,mapaSeleccionado,avatarSeleccionado));
        });

        raiz.getChildren().add(titulo);
        raiz.getChildren().add(subtitulo);
        raiz.getChildren().add(dojoView);
        raiz.getChildren().add(dojoRect);
        raiz.getChildren().add(dojoNombre);
        raiz.getChildren().add(dojoSabor);
        raiz.getChildren().add(bayView);
        raiz.getChildren().add(bayRect);
        raiz.getChildren().add(bayNombre);
        raiz.getChildren().add(baySabor);
        raiz.getChildren().add(estadoSeleccion);
        raiz.getChildren().add(confirmar);
        raiz.getChildren().add(volver);

        return escena;
    }
}