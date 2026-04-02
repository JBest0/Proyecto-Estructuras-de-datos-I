package client.data;

/**
 * Iterador simple para recorrer estructuras sin usar java.util.Iterator.
 *
 * @param <T> tipo de elementos iterados.
 */
public interface Iterator<T> {

    /**
     * Indica si existe un siguiente elemento.
     *
     * @return true si hay siguiente elemento.
     */
    boolean hasNext();

    /**
     * Retorna el siguiente elemento y avanza el cursor.
     *
     * @return siguiente elemento.
     */
    T next();
}

