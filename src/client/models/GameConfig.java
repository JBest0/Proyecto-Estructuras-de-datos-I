package client.models;

/**
 * ConfiguraciÃ³n base de una partida.
 */
public class GameConfig {

    private final int initialHp;
    private final double baseSpawnRate;
    private final double baseAttackSpeed;
    private final int scorePerKill;
    private final int difficultyStepScore;
    private final double spawnMultiplierPerLevel;
    private final double speedAddPerLevel;
    private final int ddosDamage;
    private final int malwareDamage;
    private final int credentialDamage;

    /**
     * Construye la configuraciÃ³n del juego.
     *
     * @param hp vida inicial.
     * @param spawnRate spawn rate base.
     * @param attackSpeed velocidad base de ataque.
     * @param scorePerKill puntos por destruir ataque.
     * @param difficultyStepScore puntaje para subir nivel.
     * @param spawnMultiplierPerLevel multiplicador por nivel para spawn.
     * @param speedAddPerLevel incremento de velocidad por nivel.
     * @param ddosDamage daÃ±o de DDOS.
     * @param malwareDamage daÃ±o de MALWARE.
     * @param credentialDamage daÃ±o de CREDENTIAL_ATTACK.
     */
    public GameConfig(
        int hp,
        double spawnRate,
        double attackSpeed,
        int scorePerKill,
        int difficultyStepScore,
        double spawnMultiplierPerLevel,
        double speedAddPerLevel,
        int ddosDamage,
        int malwareDamage,
        int credentialDamage
    ) {
        this.initialHp = hp;
        this.baseSpawnRate = spawnRate;
        this.baseAttackSpeed = attackSpeed;
        this.scorePerKill = scorePerKill;
        this.difficultyStepScore = difficultyStepScore;
        this.spawnMultiplierPerLevel = spawnMultiplierPerLevel;
        this.speedAddPerLevel = speedAddPerLevel;
        this.ddosDamage = ddosDamage;
        this.malwareDamage = malwareDamage;
        this.credentialDamage = credentialDamage;
    }

    public int getInitialHp() { return initialHp; }

    public double getBaseSpawnRate() { return baseSpawnRate; }

    public double getBaseAttackSpeed() { return baseAttackSpeed; }

    public int getScorePerKill() { return scorePerKill; }

    public int getDifficultyStepScore() { return difficultyStepScore; }

    public double getSpawnMultiplierPerLevel() { return spawnMultiplierPerLevel; }

    public double getSpeedAddPerLevel() { return speedAddPerLevel; }

    /**
     * Retorna el daÃ±o asociado a un tipo de ataque.
     *
     * @param tipo tipo de ataque.
     * @return daÃ±o configurado para ese tipo.
     */
    public int getDamageFor(AttackType tipo) {
        if (tipo == AttackType.DDOS) {
            return ddosDamage;
        }
        if (tipo == AttackType.MALWARE) {
            return malwareDamage;
        }
        return credentialDamage;
    }
}

