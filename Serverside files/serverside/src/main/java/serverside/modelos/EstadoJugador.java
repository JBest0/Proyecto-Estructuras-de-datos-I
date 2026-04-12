package serverside.modelos;

public class EstadoJugador { 
    //encapsulamiento
    private int hp;
    private int score;
    private int nivel;

    public EstadoJugador(int hp, int score, int nivel) {
        this.hp = hp;
        this.score = score;
        this.nivel = nivel;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

}