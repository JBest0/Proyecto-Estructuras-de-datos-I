import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;


 //Pantalla principal de juego en JavaFX.
 
public class GameScreenFX {

    Ataques attack = new Ataques();
    Enemigos enemy = new Enemigos();
    

    public Scene crearPantalla(Stage inicio, String mapa,String avatar){
    
    //creacion de escena
    Pane raizJuego = new Pane();
    Scene juego = new Scene(raizJuego,1136,944);
    


    //creacion del mapa seleccionado
    Image mapaSeleccion = new Image("file:media/"+mapa+".png");
    ImageView verMapaSeleccion = new ImageView(mapaSeleccion);
    verMapaSeleccion.setFitWidth(1136);
    verMapaSeleccion.setFitHeight(944);
    verMapaSeleccion.setPreserveRatio(false);


    //Creacion del personaje seleccionado
    Image personaje = new Image("file:media/"+avatar+".png");
    ImageView verPersonaje = new ImageView(personaje);
    verPersonaje.setFitWidth(200);
    verPersonaje.setFitHeight(200);
    verPersonaje.setPreserveRatio(true);
    verPersonaje.setLayoutX(468);
    verPersonaje.setLayoutY(644);

    Personaje player = new Personaje();


    ImageView[] enemigoActual = new ImageView[1];
    String[] tipoEnemigoActual = new String[1];
    tipoEnemigoActual[0] = enemy.generarTipoAleatorio();
    enemigoActual[0] = enemy.generarEnemigo(tipoEnemigoActual[0], raizJuego);

    AnimationTimer moverEnemigo = new AnimationTimer(){
        @Override
        public void handle(long now){
            enemigoActual[0].setLayoutY(enemigoActual[0].getLayoutY() + 2);

            if (enemigoActual[0].getLayoutY() > 944){
                raizJuego.getChildren().remove(enemigoActual[0]);

                tipoEnemigoActual[0] = enemy.generarTipoAleatorio();
                enemigoActual[0] = enemy.generarEnemigo(tipoEnemigoActual[0],raizJuego);
            }

        }
    };


    



    //Funciones durante el juego
    juego.setOnKeyPressed(e -> {


        //Mueve al personaje a la izquierda
    if (e.getCode() == KeyCode.A) {
        if (verPersonaje.getLayoutX()> -40){
            verPersonaje.setLayoutX(verPersonaje.getLayoutX() - player.getVelocidad());
        }
    }

    //Mueve al personaje a la derecha
    if (e.getCode() == KeyCode.D) {

        if (verPersonaje.getLayoutX()< 976){
        verPersonaje.setLayoutX(verPersonaje.getLayoutX() + player.getVelocidad());
      }
    }

    //Genera el ataque "Firewall"
    if (e.getCode() == KeyCode.Q) {

        long ahora = System.nanoTime();
        if (ahora - attack.getUltimoDisparo() < attack.getCooldown()){
            return;
        }
        attack.setUltimoDisparo(ahora) ;
    
        ImageView verFirewall = attack.generarAtaque("file:media/Firewall.png", 220, 220, -20, -20, 
            raizJuego, verPersonaje, true);
        
        AnimationTimer moverFirewall = new AnimationTimer() {
            @Override
            public void handle(long now) {
                attack.moverAtaque(verFirewall);

                if (verFirewall.getLayoutY()+ verFirewall.getFitHeight() < 0){
                    raizJuego.getChildren().remove(verFirewall);
                    stop();
                    return;
                }

                if (tipoEnemigoActual[0].equals("DDoS") && verFirewall.getBoundsInParent().intersects(enemigoActual[0].getBoundsInParent())){

                    raizJuego.getChildren().removeAll(verFirewall, enemigoActual[0]);

                    tipoEnemigoActual[0] = enemy.generarTipoAleatorio();
                    enemigoActual[0] = enemy.generarEnemigo(tipoEnemigoActual[0], raizJuego);
                    stop();
                }
            }
        };

        moverFirewall.start();

    }



    //Genera el ataque "Antivirus"
    if (e.getCode()== KeyCode.W) {

        long ahora = System.nanoTime();
        if (ahora - attack.getUltimoDisparo() < attack.getCooldown()){
            return;
        }
        attack.setUltimoDisparo(ahora) ;

        ImageView verAntivirus = attack.generarAtaque("file:media/Antivirus.png", 100, 130, 
        30, -20, raizJuego, verPersonaje, false);


        AnimationTimer moverAntivirus = new AnimationTimer() {
            @Override
            public void handle(long now) {
                attack.moverAtaque(verAntivirus);

                if (verAntivirus.getLayoutY()+ verAntivirus.getFitHeight() < 0){
                    raizJuego.getChildren().remove(verAntivirus);
                    stop();
                    return;
                }
                if (tipoEnemigoActual[0].equals("Malware") && verAntivirus.getBoundsInParent().intersects(enemigoActual[0].getBoundsInParent())){

                    raizJuego.getChildren().removeAll(verAntivirus, enemigoActual[0]);

                    tipoEnemigoActual[0] = enemy.generarTipoAleatorio();
                    enemigoActual[0] = enemy.generarEnemigo(tipoEnemigoActual[0], raizJuego);
                    stop();
                }
            }
        };
        moverAntivirus.start();


    }



    //Genera el ataque "Crypto Shield"
    if (e.getCode()== KeyCode.E) {

        long ahora = System.nanoTime();
        if (ahora - attack.getUltimoDisparo() < attack.getCooldown()){
            return;
        }
        attack.setUltimoDisparo(ahora) ;


        ImageView verCryptoShield = attack.generarAtaque("file:media/Crypto Shield.png", 140, 140,
         30, -20, raizJuego, verPersonaje, true);

         AnimationTimer moverCryptoShield = new AnimationTimer() {
            @Override
            public void handle(long now) {
                attack.moverAtaque(verCryptoShield);

                if (verCryptoShield.getLayoutY()+ verCryptoShield.getFitHeight() < 0){
                    raizJuego.getChildren().remove(verCryptoShield);
                    stop();
                    return;
                }

                if (tipoEnemigoActual[0].equals("Credential Attack") && verCryptoShield.getBoundsInParent().intersects(enemigoActual[0].getBoundsInParent())){

                    raizJuego.getChildren().removeAll(verCryptoShield, enemigoActual[0]);

                    tipoEnemigoActual[0] = enemy.generarTipoAleatorio();
                    enemigoActual[0] = enemy.generarEnemigo(tipoEnemigoActual[0], raizJuego);
                    stop();
                }
            }   
        };

        moverCryptoShield.start();


    }
    });


    
    
    raizJuego.getChildren().add(verMapaSeleccion);
    raizJuego.getChildren().add(verPersonaje);

    moverEnemigo.start();
    

    return juego;  
  }

}

