package client.models;

/**
 * Configuración base de una partida.
 * Se quitaron los modificadores 'final' para permitir que el LoginFX 
 * actualice los valores con lo que mande el servidor.
 */
public class GameConfig {

    private int initialHp;
    private double baseSpawnRate;
    private double baseAttackSpeed;
    private int scorePerKill;
    private int difficultyStepScore;
    private double spawnMultiplierPerLevel;
    private double speedAddPerLevel;
    private int ddosDamage;
    private int malwareDamage;
    private int credentialDamage;

    /**
     * CONSTRUCTOR VACÍO (Necesario para el LoginFX)
     * Inicializa con valores seguros por si el servidor falla.
     */
    public GameConfig() {
        this.initialHp = 100;
        this.baseSpawnRate = 2000;
        this.baseAttackSpeed = 1.0;
        this.scorePerKill = 10;
        this.difficultyStepScore = 100;
        this.spawnMultiplierPerLevel = 0.9;
        this.speedAddPerLevel = 0.2;
        this.ddosDamage = 10;
        this.malwareDamage = 15;
        this.credentialDamage = 20;
    }

    /**
     * Constructor completo (se mantiene por compatibilidad)
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

    // ====================== GETTERS ======================
    public int getInitialHp() { return initialHp; }
    public double getBaseSpawnRate() { return baseSpawnRate; }
    public double getBaseAttackSpeed() { return baseAttackSpeed; }
    public int getScorePerKill() { return scorePerKill; }
    public int getDifficultyStepScore() { return difficultyStepScore; }
    public double getSpawnMultiplierPerLevel() { return spawnMultiplierPerLevel; }
    public double getSpeedAddPerLevel() { return speedAddPerLevel; }

    /**
     * Retorna el daño asociado a un tipo de ataque.
     */
    public int getDamageFor(AttackType tipo) {
        if (tipo == AttackType.DDOS) return ddosDamage;
        if (tipo == AttackType.MALWARE) return malwareDamage;
        return credentialDamage;
    }

    // ====================== SETTERS (NUEVOS) ======================
    public void setInitialHp(int hp) { this.initialHp = hp; }
    public void setBaseSpawnRate(double rate) { this.baseSpawnRate = rate; }
    public void setBaseAttackSpeed(double speed) { this.baseAttackSpeed = speed; }
    public void setScorePerKill(int score) { this.scorePerKill = score; }
    public void setDifficultyStepScore(int score) { this.difficultyStepScore = score; }
    public void setSpawnMultiplierPerLevel(double mult) { this.spawnMultiplierPerLevel = mult; }
    public void setSpeedAddPerLevel(double add) { this.speedAddPerLevel = add; }
    public void setDdosDamage(int damage) { this.ddosDamage = damage; }
    public void setMalwareDamage(int damage) { this.malwareDamage = damage; }
    public void setCredentialDamage(int damage) { this.credentialDamage = damage; } }