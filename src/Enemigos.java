import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;

public class Enemigos {

    double posicionX = Math.random()*936;

    public double getPosicionX(){
        return posicionX;
    }


    public String generarTipoAleatorio() {
        int numero = (int)(Math.random()*3);
        if (numero == 0){
            return "DDoS";
        }
        else if (numero == 1){
            return "Malware";
        }
        else {
            return "Credential Attack";
        }
    }

    public ImageView generarEnemigo(String tipo, Pane raizJuego) {
    
        Image enemigo;
        if(tipo.equals("DDoS")){
            enemigo = new Image("file:media/DDoS.png");
        }

        else if(tipo.equals("Malware")){
            enemigo = new Image("file:media/Malware.png");
        }

        else{
            enemigo = new Image("file:media/Credential Attack.png");
        }
        
        ImageView verEnemigo = new ImageView(enemigo);


        verEnemigo.setLayoutX(Math.random()*936);
        verEnemigo.setLayoutY(0);
        verEnemigo.setFitHeight(200);
        verEnemigo.setFitWidth(200);
        verEnemigo.setPreserveRatio(true);

        raizJuego.getChildren().add(verEnemigo);

        return verEnemigo;
    }

    
    
}
