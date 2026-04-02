
package client.managers;

import client.data.Iterator;
import client.data.LinkedList;
import client.models.Attack;

/**
 * Administra los ataques activos usando una LinkedList propia.
 */
public class AttackManager {

    private final LinkedList<Attack> ataques;

    /**
     * Crea un manager con lista vacÃ­a de ataques.
     */
    public AttackManager() {
        this.ataques = new LinkedList<>();
    }

    /**
     * Agrega un nuevo ataque a la lista.
     *
     * @param ataque ataque a agregar.
     * @throws NullPointerException si el ataque es null.
     */
    public void agregarAtaque(Attack ataque) {
        if (ataque == null) {
            throw new NullPointerException("El ataque no puede ser null");
        }
        ataques.add(ataque);
    }

    /**
     * Remueve un ataque especÃ­fico si existe.
     *
     * @param ataque ataque objetivo.
     */
    public void removerAtaque(Attack ataque) {
        int index = ataques.indexOf(ataque);
        if (index != -1) {
            ataques.remove(index);
        }
    }

    /**
     * Remueve un ataque por Ã­ndice si el Ã­ndice es vÃ¡lido.
     *
     * @param index Ã­ndice a remover.
     */
    public void removerAtaqueEnIndice(int index) {
        try {
            ataques.remove(index);
        } catch (IndexOutOfBoundsException ignored) {
            // Ignoramos Ã­ndices invÃ¡lidos para no romper el game loop.
        }
    }

    /**
     * Retorna la cantidad de ataques activos.
     *
     * @return nÃºmero de ataques.
     */
    public int getCantidadAtaques() {
        return ataques.size();
    }

    /**
     * Obtiene un ataque por Ã­ndice.
     *
     * @param index Ã­ndice del ataque.
     * @return ataque encontrado o null si no existe.
     */
    public Attack getAtaqueEnIndice(int index) {
        try {
            return ataques.get(index);
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    /**
     * Retorna todos los ataques activos.
     *
     * @return lista enlazada de ataques.
     */
    public LinkedList<Attack> getTodosAtaques() {
        return ataques;
    }

    /**
     * Verifica si un ataque estÃ¡ en la lista.
     *
     * @param ataque ataque a buscar.
     * @return true si existe.
     */
    public boolean contieneAtaque(Attack ataque) {
        return ataques.contains(ataque);
    }

    /**
     * Actualiza la posiciÃ³n de todos los ataques activos.
     */
    public void actualizarTodasPosiciones() {
        Iterator<Attack> it = ataques.iterator();
        while (it.hasNext()) {
            Attack ataque = it.next();
            ataque.actualizarPosicion();
        }
    }

    /**
     * Retorna los ataques que estÃ¡n fuera de la pantalla.
     *
     * @param alturaPantalla altura lÃ­mite del mapa.
     * @return lista de ataques fuera del mapa.
     */
    public LinkedList<Attack> getAtaquesAFueraDelMapa(int alturaPantalla) {
        LinkedList<Attack> fueraDelMapa = new LinkedList<>();

        Iterator<Attack> it = ataques.iterator();
        while (it.hasNext()) {
            Attack ataque = it.next();
            if (ataque.estaFueraDelMapa(alturaPantalla)) {
                fueraDelMapa.add(ataque);
            }
        }

        return fueraDelMapa;
    }

    /**
     * Limpia todos los ataques activos.
     */
    public void limpiarAtaques() {
        ataques.clear();
    }

    /**
     * Busca el primer ataque que colisione con una posiciÃ³n.
     *
     * @param x coordenada X objetivo.
     * @param y coordenada Y objetivo.
     * @param rango radio de colisiÃ³n.
     * @return primer ataque encontrado dentro del rango o null.
     */
    public Attack getAtaqueEnPosicion(double x, double y, double rango) {
        Iterator<Attack> it = ataques.iterator();
        while (it.hasNext()) {
            Attack ataque = it.next();
            double deltaX = ataque.getX() - x;
            double deltaY = ataque.getY() - y;
            double distancia = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

            if (distancia <= rango) {
                return ataque;
            }
        }

        return null;
    }
}

