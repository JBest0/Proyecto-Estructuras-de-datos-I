
package client.engine;

import client.models.GameConfig;

/**
 * Calcula nivel y parÃ¡metros de dificultad segÃºn score.
 */
public class DifficultyCalculator {

    private final GameConfig config;
    private int nivelActual;
    private double spawnRateActual;

    /**
     * Crea una calculadora con configuraciÃ³n base.
     *
     * @param config configuraciÃ³n del juego.
     */
    public DifficultyCalculator(GameConfig config) {
        this.config = config;
        this.nivelActual = 1;
        this.spawnRateActual = config.getBaseSpawnRate();
    }

    /**
     * Calcula nivel en funciÃ³n del score.
     *
     * @param score score acumulado.
     * @return nivel resultante.
     */
    public int calcularNivel(int score) {
        int paso = config.getDifficultyStepScore();
        if (paso <= 0) {
            nivelActual = 1;
            return nivelActual;
        }

        nivelActual = (score / paso) + 1;
        return nivelActual;
    }

    /**
     * Calcula spawn rate para un nivel.
     *
     * @param nivel nivel de dificultad.
     * @return spawn rate resultante.
     */
    public double calcularSpawnRate(int nivel) {
        spawnRateActual = config.getBaseSpawnRate() * Math.pow(config.getSpawnMultiplierPerLevel(), nivel - 1);
        return spawnRateActual;
    }

    /**
     * Calcula velocidad de ataques para un nivel.
     *
     * @param nivel nivel de dificultad.
     * @return velocidad de ataque.
     */
    public double calcularAttackSpeed(int nivel) {
        return config.getBaseAttackSpeed() + ((nivel - 1) * config.getSpeedAddPerLevel());
    }

    /**
     * Retorna el nivel calculado mÃ¡s reciente.
     *
     * @return nivel actual.
     */
    public int getNivelActual() {
        return nivelActual;
    }

    /**
     * Retorna el spawn rate calculado mÃ¡s reciente.
     *
     * @return spawn rate actual.
     */
    public double getSpawnRateActual() {
        return spawnRateActual;
    }
}

