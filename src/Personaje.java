public class Personaje {

    private int velocidadDePersonaje = 10;
    private int HP = 100;

    public int getVelocidad(){
        return velocidadDePersonaje;
    }

    public void setVelocidad(int num){
        velocidadDePersonaje += num;
    }
    
    public int getHP(){
        return HP;
    }


}
