/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoestrucdatos;

/**
 *
 * @author Elohym Casais
 */

public class Grafo {
    private ListaEnlazada<Vertice> vertices;

    public Grafo() {
        this.vertices = new ListaEnlazada<>();
    }

    /**
     * Agrega un nuevo vértice al grafo.
     * @param estacion
     */
    
    public void agregarEstacion(Estacion estacion) {
        if (buscarVertice(estacion.getId()) == null) {
            vertices.agregar(new Vertice(estacion));
        }
    }

    /**
     * Busca un vértice por el ID o Nombre de la estación.
     * @param clave
     * @return 
     */
    
    public Vertice buscarVertice(String clave) {
        Nodo<Vertice> aux = vertices.getCabeza();
        while (aux != null) {
            Estacion e = aux.getDato().getEstacion();
            if (e.getId().equalsIgnoreCase(clave) || e.getNombre().equalsIgnoreCase(clave)) {
                return aux.getDato();
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    /**
     * Agrega una conexión bidireccional (no dirigida) entre dos estaciones con su tiempo.
     * @param idOrigen
     * @param idDestino
     * @param tiempoMinutos
     * @return 
     */
    
    public boolean agregarConexion(String idOrigen, String idDestino, int tiempoMinutos) {
        Vertice vOrigen = buscarVertice(idOrigen);
        Vertice vDestino = buscarVertice(idDestino);

        if (vOrigen != null && vDestino != null) {
            vOrigen.agregarArista(vDestino.getEstacion(), tiempoMinutos);
            vDestino.agregarArista(vOrigen.getEstacion(), tiempoMinutos); // Grafo no dirigido
            return true;
        }
        return false;
    }

    /**
     * Elimina una estación del grafo y borra todas las aristas adyacentes a ella.
     * @param clave
     * @return 
     */
    
    public boolean eliminarEstacion(String clave) {
        Vertice vEliminar = buscarVertice(clave);
        if (vEliminar == null) return false;

        String idEliminar = vEliminar.getEstacion().getId();

        // 1. Eliminar las aristas hacia esta estación en todos los demás vértices
        Nodo<Vertice> aux = vertices.getCabeza();
        while (aux != null) {
            aux.getDato().eliminarArista(idEliminar);
            aux = aux.getSiguiente();
        }

        // 2. Eliminar el vértice de la lista
        return vertices.eliminar(vEliminar);
    }

    /**
     * Elimina únicamente la conexión entre dos estaciones.
     * @param idOrigen
     * @param idDestino
     * @return 
     */
    
    public boolean eliminarConexion(String idOrigen, String idDestino) {
        Vertice vOrigen = buscarVertice(idOrigen);
        Vertice vDestino = buscarVertice(idDestino);

        if (vOrigen != null && vDestino != null) {
            boolean e1 = vOrigen.eliminarArista(vDestino.getEstacion().getId());
            boolean e2 = vDestino.eliminarArista(vOrigen.getEstacion().getId());
            return e1 || e2;
        }
        return false;
    }

    public ListaEnlazada<Vertice> getVertices() {
        return vertices;
    }

    public int getNumVertices() {
        return vertices.getTamano();
    }
}
