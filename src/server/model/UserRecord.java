package server.model;

/**
 * Representa un usuario registrado en el sistema.
 * Se serializa/deserializa desde database.json usando Gson.
 */
public class UserRecord {

    private String username;
    private String passwordHash;
    private String avatar;
    private Stats stats;

    public UserRecord() {
        this.stats = new Stats();
    }

    public UserRecord(String username, String passwordHash, String avatar) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.avatar = avatar;
        this.stats = new Stats();
    }

    // --- Getters y Setters ---

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public Stats getStats() { return stats; }
    public void setStats(Stats stats) { this.stats = stats; }

    // =========================================================
    //  Clase anidada: estadísticas del jugador
    // =========================================================
    public static class Stats {
        private int totalScore;
        private int gamesPlayed;
        private int xpNetwork;
        private int xpMalware;
        private int xpCrypto;

        public Stats() {
            this.totalScore  = 0;
            this.gamesPlayed = 0;
            this.xpNetwork   = 0;
            this.xpMalware   = 0;
            this.xpCrypto    = 0;
        }

        public int getTotalScore()  { return totalScore; }
        public void setTotalScore(int totalScore)   { this.totalScore = totalScore; }

        public int getGamesPlayed() { return gamesPlayed; }
        public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = gamesPlayed; }

        public int getXpNetwork()   { return xpNetwork; }
        public void setXpNetwork(int xpNetwork)     { this.xpNetwork = xpNetwork; }

        public int getXpMalware()   { return xpMalware; }
        public void setXpMalware(int xpMalware)     { this.xpMalware = xpMalware; }

        public int getXpCrypto()    { return xpCrypto; }
        public void setXpCrypto(int xpCrypto)       { this.xpCrypto = xpCrypto; }
    }
}
