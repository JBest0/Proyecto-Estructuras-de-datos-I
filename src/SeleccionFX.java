import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SeleccionFX {

    String[] nombrePersonajes = {"Captain Firewall", "Byte Ninja", "Malware Muncher",
    "Crypto Llama", "Packet Pirate", "Null Pointer Paladin"};

    String[] imagenPersonajes = {"media/Captain Firewall.png", "media/Byte Ninja.png", "media/Malware Muncher.png",
    "media/Crypto Llama.png", "media/Packet Pirate.png", "media/Null Pointer Paladin.png"};

    int indiceSeleccion = 0;

    public Scene crearPantalla(Stage inicio, Scene escenaAnterior, String username) {

        Group raizSeleccion = new Group();
        Scene escenario2 = new Scene(raizSeleccion,1136,944,Color.BEIGE);

        //Imagen de personajes
        Image sprites = new Image("file:" + imagenPersonajes[indiceSeleccion]);
        ImageView verSprites = new ImageView(sprites);
        verSprites.setLayoutX(420);
        verSprites.setLayoutY(310);


        //Imagen fondo
        Image fondo = new Image("file:media/Fondo Seleccion.png");
        ImageView verfondo = new ImageView(fondo);


        //TEXTO de arriba
        Label texto = new Label("Selecciona a tu personaje");
        texto.setLayoutX(500);

        Label nombres = new Label(nombrePersonajes[indiceSeleccion]);
        nombres.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");
        nombres.setLayoutX(470);
        nombres.setLayoutY(250);

        //BOTONES y su configuracion
        Button derecha = new Button(">");

        derecha.setLayoutX(800);
        derecha.setLayoutY(450);

        derecha.setOnAction(e -> {
            indiceSeleccion++;

            if (indiceSeleccion >= nombrePersonajes.length){
                indiceSeleccion = 0;
            }
            nombres.setText(nombrePersonajes[indiceSeleccion]);
            Image nuevoSprite = new Image("file:" + imagenPersonajes[indiceSeleccion]);
            verSprites.setImage(nuevoSprite);
        });

        Button izquierda = new Button("<");

        izquierda.setLayoutX(300);
        izquierda.setLayoutY(450);

        izquierda.setOnAction(e ->{
            indiceSeleccion--;

            if (indiceSeleccion<0){
                indiceSeleccion = 5;
            }
            nombres.setText(nombrePersonajes[indiceSeleccion]);
            Image nuevoSprite = new Image("file:" + imagenPersonajes[indiceSeleccion]);
            verSprites.setImage(nuevoSprite);
        });


        Button volver = new Button("Volver");
        volver.setOnAction(e -> {
            inicio.setScene(escenaAnterior);
        });
        volver.setLayoutX(0);
        volver.setLayoutY(0);

        Button continuar = new Button("Elegir mapa");
        continuar.setLayoutX(520);
        continuar.setLayoutY(760);
        continuar.setPrefSize(100, 40);
        continuar.setOnAction(e -> {
            MapSeleccionFX pantallaMapas = new MapSeleccionFX();
            inicio.setScene(pantallaMapas.crearPantalla(inicio, escenario2, nombrePersonajes[indiceSeleccion], username));
        });



        //añadiendo lo visual
        raizSeleccion.getChildren().add(verfondo);
        raizSeleccion.getChildren().add(derecha);
        raizSeleccion.getChildren().add(izquierda);
        raizSeleccion.getChildren().add(volver);
        raizSeleccion.getChildren().add(continuar);
        raizSeleccion.getChildren().add(texto);
        raizSeleccion.getChildren().add(nombres);
        raizSeleccion.getChildren().add(verSprites);
        return escenario2;



    }


}
