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

public class MainFX extends Application {

    @Override
    public void start(Stage INICIO) throws Exception {
 
        Group raiz = new Group();  //grupo para modificar (aun no lo entiendo muy bien)

        
        Image icono = new Image("file:Logo.png");  //icono de la ventana


        Scene escenario = new Scene(raiz, 1136, 944);  //Escenario, su tamaño esta definido aquí


        Image logo = new Image("file:Logo.png");        // Imagen de fondo
        ImageView verLogo = new ImageView(logo);   
        verLogo.setX(0);                           //Posicionamiento de la imagen de fondo
        verLogo.setY(0);


        SeleccionFX pantallaSeleccion =new SeleccionFX(); //Debido a que el metodo "crear pantalla" no es static hay que crear este objeto



        Button botonInicio = new Button("Iniciar"); //Boton de inicio
        botonInicio.setLayoutX(545);                //Posicionamiento en el eje X y Y del boton
        botonInicio.setLayoutY(500);
        botonInicio.setPrefSize(70,50);             //Dimensiones del boton.
        botonInicio.setOnAction(e -> {              //Al realizar el evento presionar boton realiza el metodo de adentro
            INICIO.setScene(pantallaSeleccion.crearPantalla(INICIO,escenario));
        });

        
        
        INICIO.getIcons().add(icono);          //Aqui se crea el icono de la ventana
        INICIO.setScene(escenario);            //Aqui se crea el escenario
        INICIO.setTitle("Cyber Defense Duel"); //Aqui se pone un titulo personalizado a la ventana
        INICIO.setResizable(false);            //Esto evita que la ventana cambie de dimensiones
        raiz.getChildren().add(verLogo);       //Aqui se crea la imagen del fondo en la ventana
        raiz.getChildren().add(botonInicio);   //Aqui se crea el boton, se pone de ultimo para que "sobresalga"

    
        INICIO.show(); //Funcion que inicia la ventana con las caracteristicas

        
 
    }

    public static void main(String[] args) { //Funcion principal de Java la cual sirve para llamar la creacion de la ventana
        launch(args);
    }

    
}
