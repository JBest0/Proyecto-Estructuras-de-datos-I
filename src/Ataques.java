
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Ataques {

    private int velocidadDeAtaque = 5;
    private long ultimoDisparo = 0;
    private long cooldown = 300_000_000;

    public int getVelocidad(){
        return velocidadDeAtaque;
    }

    public long getCooldown(){
        return cooldown;
    }
    
    public long getUltimoDisparo(){
        return ultimoDisparo;
    }

    public void setUltimoDisparo(long tiempo){
        ultimoDisparo = tiempo;
    }

    public ImageView generarAtaque(String imagen, int alto, int ancho, int desplazamientoX, int desplazamientoY, Pane raiz,
        ImageView personaje, boolean mantenerAspecto){

        Image ataque = new Image(imagen);
        ImageView verAtaque = new ImageView(ataque);

        verAtaque.setFitHeight(alto);
        verAtaque.setFitWidth(ancho);
        verAtaque.setPreserveRatio(mantenerAspecto);
        verAtaque.setLayoutX(personaje.getLayoutX()+ desplazamientoX);
        verAtaque.setLayoutY(personaje.getLayoutY()+ desplazamientoY);

        raiz.getChildren().add(verAtaque);

        return verAtaque;

    }

    public void moverAtaque(ImageView ataque){
    ataque.setLayoutY(ataque.getLayoutY() - velocidadDeAtaque);
    }


}
