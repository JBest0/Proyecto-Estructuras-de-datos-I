package serverside.estructuras;

/**
 * Estructura FIFO basada en LinkedList propia.
 *
 * @param <T> tipo de elemento de la cola.
 */
public class Queue<T> {

    private final LinkedList<T> elements;

    /**
     * Crea una cola vacÃ­a.
     */
    public Queue() {
        this.elements = new LinkedList<>();
    }

    /**
     * Encola un elemento al final.
     *
     * @param elemento elemento a encolar.
     */
    public void enqueue(T elemento) {
        // LinkedList.add inserta al inicio, por eso reconstruimos para mantener FIFO.
        LinkedList<T> auxiliar = new LinkedList<>();
        while (!elements.isEmpty()) {
            auxiliar.add(elements.remove(0));
        }

        elements.add(elemento);

        while (!auxiliar.isEmpty()) {
            elements.add(auxiliar.remove(0));
        }
    }

    /**
     * Desencola el elemento al frente.
     *
     * @return primer elemento encolado.
     */
    public T dequeue() {
        return elements.removeLast();
    }

    /**
     * Consulta el frente sin removerlo.
     *
     * @return primer elemento en cola.
     */
    public T peek() {
        return elements.get(elements.size() - 1);
    }

    /**
     * Indica si la cola estÃ¡ vacÃ­a.
     *
     * @return true si no hay elementos.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Retorna la cantidad de elementos.
     *
     * @return tamaÃ±o de la cola.
     */
    public int size() {
        return elements.size();
    }

    /**
     * Limpia la cola.
     */
    public void clear() {
        elements.clear();
    }
}

