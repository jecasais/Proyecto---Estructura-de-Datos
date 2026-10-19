/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoestrucdatos;


/**
 *
 * @author Elohym
 */
public class TablaHash {
    private ListaEnlazada<EntradaHash>[] tabla;
    private int capacidad;
    private int tamano;
    
    public TablaHash(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new ListaEnlazada[capacidad]; 
        this.tamano = 0;

        for (int i = 0; i < capacidad; i++) {
            this.tabla[i] = new ListaEnlazada<>();
        }
    }

    public TablaHash() {
        this(101);
    }

    /**
     * Función hash polinomial simple sobre la clave.
     */
    private int calcularIndice(String clave) {
        int hash = 0;
        for (int i = 0; i < clave.length(); i++) {
            hash = (31 * hash + clave.charAt(i)) % capacidad;
        }
        if (hash < 0) {
            hash = hash * -1;
        }
        return hash;
    }

    /**
     * Inserta una estación en la tabla Hash o la actualiza si ya existe.
     * @param clave
     * @param estacion
     */
    
    public void insertar(String clave, Estacion estacion) {
        int indice = calcularIndice(clave);
        ListaEnlazada<EntradaHash> lista = tabla[indice];

        Nodo<EntradaHash> actual = lista.getCabeza();
        while (actual != null) {
            EntradaHash entrada = actual.getDato();
            if (entrada.getClave().equalsIgnoreCase(clave)) {
                entrada.setValor(estacion);
                return;
            }
            actual = actual.getSiguiente();
        }

        EntradaHash nuevaEntrada = new EntradaHash(clave, estacion);
        lista.agregar(nuevaEntrada);
        tamano++;
    }

    /**
     * Busca una estación por clave en tiempo O(1).
     * @param clave
     * @return 
     */
    
    public Estacion buscar(String clave) {
        int indice = calcularIndice(clave);
        ListaEnlazada<EntradaHash> lista = tabla[indice];

        Nodo<EntradaHash> actual = lista.getCabeza();
        while (actual != null) {
            EntradaHash entrada = actual.getDato();
            if (entrada.getClave().equalsIgnoreCase(clave)) {
                return entrada.getValor();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    /**
     * Elimina una estación de la tabla por su clave.
     * @param clave
     * @return 
     */
    
    public boolean eliminar(String clave) {
        int indice = calcularIndice(clave);
        ListaEnlazada<EntradaHash> lista = tabla[indice];

        Nodo<EntradaHash> actual = lista.getCabeza();
        while (actual != null) {
            EntradaHash entrada = actual.getDato();
            if (entrada.getClave().equalsIgnoreCase(clave)) {
                lista.eliminar(entrada);
                tamano--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public int getTamano() {
        return tamano;
    }

    public int getCapacidad() {
        return capacidad;
    }
}
