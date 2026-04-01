import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SeleccionFX {

    public static Scene crearPantalla(Stage inicio, Scene escenaAnterior) {

        Group raizSeleccion = new Group();
        Scene escenario2 = new Scene(raizSeleccion,1136,944,Color.BEIGE);

        //BOTONES y su configuracion
        Button derecha = new Button(">");
        Button izquierda = new Button("<");
        derecha.setLayoutX(836);
        derecha.setLayoutY(450);
        izquierda.setLayoutX(336);
        izquierda.setLayoutY(450);

        Button volver = new Button("Volver");
        volver.setOnAction(e -> {
            inicio.setScene(escenaAnterior);
        });
        volver.setLayoutX(0);
        volver.setLayoutY(0);


        //TEXTO de arriba
        Label texto = new Label("Selecciona a tu personaje");
        texto.setLayoutX(550);


        //añadiendo lo visual
        raizSeleccion.getChildren().add(derecha);
        raizSeleccion.getChildren().add(izquierda);
        raizSeleccion.getChildren().add(volver);
        raizSeleccion.getChildren().add(texto);

        return escenario2;



    }
    
   
}
