package serverside.modelos;

public class ConfiguracionJuego {
    //encapsulamiento
    private int initialHp;
    private int baseSpawnRate;
    private int scorePerKill;

    public ConfiguracionJuego() {
        // Valores predeterminados de inicio
        this.initialHp = 100;
        this.baseSpawnRate = 2000; 
        this.scorePerKill = 10;
    }

    public int getInitialHp() {
        return initialHp;
    }

    public void setInitialHp(int initialHp) {
        this.initialHp = initialHp;
    }

    public int getBaseSpawnRate() {
        return baseSpawnRate;
    }

    public void setBaseSpawnRate(int baseSpawnRate) {
        this.baseSpawnRate = baseSpawnRate;
    }

    public int getScorePerKill() {
        return scorePerKill;
    }

    public void setScorePerKill(int scorePerKill) {
        this.scorePerKill = scorePerKill;
    }

}