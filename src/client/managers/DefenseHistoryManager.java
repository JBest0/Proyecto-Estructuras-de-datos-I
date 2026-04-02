
package client.managers;

import client.data.Stack;
import client.models.Defensa;

/**
 * Gestiona el historial de defensas utilizadas.
 */
public class DefenseHistoryManager {

    private final Stack<Defensa> historialDefensas;
    private int contadorFirewall;
    private int contadorAntivirus;
    private int contadorCryptoShield;

    /**
     * Construye un historial vacÃ­o.
     */
    public DefenseHistoryManager() {
        this.historialDefensas = new Stack<>();
    }

    /**
     * Registra una defensa en el historial.
     *
     * @param defensa defensa a registrar.
     */
    public void registrarDefensa(Defensa defensa) {
        if (defensa == null) {
            return;
        }

        historialDefensas.push(defensa);

        if (defensa == Defensa.FIREWALL) {
            contadorFirewall++;
        } else if (defensa == Defensa.ANTIVIRUS) {
            contadorAntivirus++;
        } else {
            contadorCryptoShield++;
        }
    }

    /**
     * Retorna la Ãºltima defensa usada sin removerla.
     *
     * @return Ãºltima defensa o null si no hay historial.
     */
    public Defensa obtenerUltimaDefensa() {
        if (historialDefensas.isEmpty()) {
            return null;
        }
        return historialDefensas.peek();
    }

    /**
     * Retorna la cantidad de defensas registradas.
     *
     * @return cantidad total.
     */
    public int getCantidadDefensas() {
        return historialDefensas.size();
    }

    /**
     * Retorna cuÃ¡ntas veces se usÃ³ una defensa.
     *
     * @param tipo tipo de defensa.
     * @return conteo del tipo solicitado.
     */
    public int getConteo(Defensa tipo) {
        if (tipo == Defensa.FIREWALL) {
            return contadorFirewall;
        }
        if (tipo == Defensa.ANTIVIRUS) {
            return contadorAntivirus;
        }
        return contadorCryptoShield;
    }

    /**
     * Retorna la defensa mÃ¡s utilizada hasta el momento.
     *
     * @return defensa con mayor conteo.
     */
    public Defensa getEspecializacion() {
        if (contadorFirewall >= contadorAntivirus && contadorFirewall >= contadorCryptoShield) {
            return Defensa.FIREWALL;
        }
        if (contadorAntivirus >= contadorFirewall && contadorAntivirus >= contadorCryptoShield) {
            return Defensa.ANTIVIRUS;
        }
        return Defensa.CRYPTO_SHIELD;
    }

    /**
     * Retorna el porcentaje de uso de un tipo de defensa.
     *
     * @param tipo tipo de defensa.
     * @return porcentaje en rango 0..100.
     */
    public double getProcentaje(Defensa tipo) {
        int total = getCantidadDefensas();
        if (total == 0) {
            return 0.0;
        }

        return (getConteo(tipo) * 100.0) / total;
    }

    /**
     * Limpia historial y contadores.
     */
    public void resetear() {
        historialDefensas.clear();
        contadorFirewall = 0;
        contadorAntivirus = 0;
        contadorCryptoShield = 0;
    }
}

