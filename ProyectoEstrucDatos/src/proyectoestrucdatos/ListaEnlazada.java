package proyectoestrucdatos;

/**
 * Lista enlazada simple genérica implementada desde cero.
 * Mantiene referencias a la cabeza y a la cola para que la inserción al final sea O(1).
 *
 * @author Elohym Casais
 * @param <T> tipo de dato de los elementos en la lista
 */
public class ListaEnlazada<T> {
    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamano;

    /**
     * Crea una lista vacía.
     */
    public ListaEnlazada() {
        this.cabeza = null;
        this.cola = null;
        this.tamano = 0;
    }

    /**
     * Agrega un elemento al final de la lista.
     *
     * @param dato elemento a agregar
     */
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            cola.setSiguiente(nuevo);
        }
        cola = nuevo;
        tamano++;
    }

    /**
     * Elimina la primera ocurrencia del dato en la lista.
     *
     * @param dato elemento a eliminar
     * @return true si el elemento existía y fue eliminado
     */
    public boolean eliminar(T dato) {
        Nodo<T> previo = null;
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.getDato() == dato || (actual.getDato() != null && actual.getDato().equals(dato))) {
                if (previo == null) {
                    cabeza = actual.getSiguiente();
                } else {
                    previo.setSiguiente(actual.getSiguiente());
                }
                if (actual == cola) {
                    cola = previo;
                }
                tamano--;
                return true;
            }
            previo = actual;
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Obtiene el elemento en un índice específico.
     *
     * @param indice posición del elemento (desde 0)
     * @return elemento en esa posición
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        Nodo<T> aux = cabeza;
        for (int i = 0; i < indice; i++) {
            aux = aux.getSiguiente();
        }
        return aux.getDato();
    }

    /**
     * Indica si la lista contiene el elemento dado (comparación con equals).
     *
     * @param dato elemento a buscar
     * @return true si está presente
     */
    public boolean contiene(T dato) {
        Nodo<T> aux = cabeza;
        while (aux != null) {
            if (aux.getDato() == dato || (aux.getDato() != null && aux.getDato().equals(dato))) {
                return true;
            }
            aux = aux.getSiguiente();
        }
        return false;
    }

    /**
     * Vacía la lista.
     */
    public void limpiar() {
        cabeza = null;
        cola = null;
        tamano = 0;
    }

    /**
     * Indica si la lista no tiene elementos.
     *
     * @return true si está vacía
     */
    public boolean estaVacia() {
        return cabeza == null;
    }

    /**
     * Devuelve la cantidad de elementos.
     *
     * @return tamaño de la lista
     */
    public int getTamano() {
        return tamano;
    }

    /**
     * Devuelve el primer nodo para recorrer la lista.
     *
     * @return nodo cabeza, o null si la lista está vacía
     */
    public Nodo<T> getCabeza() {
        return cabeza;
    }
}
