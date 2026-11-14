package proyectoestrucdatos;

/**
 * Nodo de una lista enlazada simple.
 *
 * @author Elohym Casais
 * @param <T> tipo de dato que se almacena en el nodo
 */
public class Nodo<T> {
    private T dato;
    private Nodo<T> siguiente;

    /**
     * Crea un nodo sin sucesor.
     *
     * @param dato valor almacenado
     */
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    /**
     * @return valor almacenado
     */
    public T getDato() {
        return dato;
    }

    /**
     * @param dato nuevo valor
     */
    public void setDato(T dato) {
        this.dato = dato;
    }

    /**
     * @return nodo siguiente o null
     */
    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    /**
     * @param siguiente nuevo nodo siguiente
     */
    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}
