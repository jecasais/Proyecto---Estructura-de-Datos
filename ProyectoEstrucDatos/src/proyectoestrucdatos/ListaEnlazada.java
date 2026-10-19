/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoestrucdatos;

/**
 *
 * @author Elohym
 * @param <T> tipo de dato de los elementos en la lista
 */

public class ListaEnlazada<T> {
    private Nodo<T> cabeza;
    private int tamano;
    
    public ListaEnlazada() {
        this.cabeza = null;
        this.tamano = 0;
    }

    /**
     * Agrega un elemento al final de la lista.
     * @param dato
     */
    
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> aux = cabeza;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nuevo);
        }
        tamano++;
    }

    /**
     * Elimina la primera ocurrencia del dato en la lista.
     * @param dato
     * @return 
     */
    
    public boolean eliminar(T dato) {
        if (cabeza == null) return false;

        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            tamano--;
            return true;
        }

        Nodo<T> aux = cabeza;
        while (aux.getSiguiente() != null) {
            if (aux.getSiguiente().getDato().equals(dato)) {
                aux.setSiguiente(aux.getSiguiente().getSiguiente());
                tamano--;
                return true;
            }
            aux = aux.getSiguiente();
        }
        return false;
    }

    /**
     * Obtiene el elemento en un índice específico.
     * @param indice
     * @return 
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

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamano() {
        return tamano;
    }

    public Nodo<T> getCabeza() {
        return cabeza;
    }

}
