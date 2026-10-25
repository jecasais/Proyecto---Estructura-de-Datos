/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoestrucdatos;

/**
 *
 * @author Elohym Casais
 * Representa el vertice de un grafo que contiene una Estacion y su lista de adyacencias.
 */

public class Vertice {
    private Estacion estacion;
    private ListaEnlazada<Arista> adyacentes;
    
    public Vertice (Estacion estacion){
        this.estacion = estacion;
        this.adyacentes = new ListaEnlazada <> ();
    }
    
    public Estacion getEstacion() {
        return estacion;
    }

    public ListaEnlazada<Arista> getAdyacentes() {
        return adyacentes;
    }

    /**
     * Agrega una conexión (arista) hacia otra estación con un tiempo determinado.
     * @param destino
     * @param tiempoMinutos
     */
    
    public void agregarArista(Estacion destino, int tiempoMinutos) {
        // Verificar si la conexión ya existe para actualizar el tiempo
        Nodo<Arista> aux = adyacentes.getCabeza();
        while (aux != null) {
            if (aux.getDato().getDestino().getId().equalsIgnoreCase(destino.getId())) {
                aux.getDato().setTiempoMinutos(tiempoMinutos);
                return;
            }
            aux = aux.getSiguiente();
        }
        // Si no existe, se agrega una nueva arista
        adyacentes.agregar(new Arista(destino, tiempoMinutos));
    }

    /**
     * Elimina la conexión hacia una estación destino.
     * @param idDestino
     * @return 
     */
   
    public boolean eliminarArista(String idDestino) {
        Nodo<Arista> aux = adyacentes.getCabeza();
        while (aux != null) {
            if (aux.getDato().getDestino().getId().equalsIgnoreCase(idDestino)) {
                adyacentes.eliminar(aux.getDato());
                return true;
            }
            aux = aux.getSiguiente();
        }
        return false;
    }

    /**
     * Calcula el grado de la estación (número de conexiones directas).
     * @return 
     */
    
    public int getGrado() {
        return adyacentes.getTamano();
    }
}
