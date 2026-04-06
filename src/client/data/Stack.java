package client.data;

/**
 * Estructura LIFO basada en LinkedList propia.
 *
 * @param <T> tipo de elemento del stack.
 */
public class Stack<T> {

    private final LinkedList<T> elements;

    /**
     * Crea un stack vacÃ­o.
     */
    public Stack() {
        this.elements = new LinkedList<>();
    }

    /**
     * Inserta un elemento en el tope.
     *
     * @param elemento elemento a insertar.
     */
    public void push(T elemento) {
        elements.add(elemento);
    }

    /**
     * Remueve y retorna el elemento del tope.
     *
     * @return elemento removido.
     */
    public T pop() {
        return elements.remove(0);
    }

    /**
     * Retorna el elemento del tope sin removerlo.
     *
     * @return elemento del tope.
     */
    public T peek() {
        return elements.get(0);
    }

    /**
     * Indica si estÃ¡ vacÃ­o.
     *
     * @return true si no hay elementos.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Retorna la cantidad de elementos.
     *
     * @return tamaÃ±o del stack.
     */
    public int size() {
        return elements.size();
    }

    /**
     * Elimina todos los elementos.
     */
    public void clear() {
        elements.clear();
    }

    /**
     * Verifica si un elemento existe en el stack.
     *
     * @param elemento elemento a buscar.
     * @return true si existe.
     */
    public boolean contains(T elemento) {
        return elements.contains(elemento);
    }
}

