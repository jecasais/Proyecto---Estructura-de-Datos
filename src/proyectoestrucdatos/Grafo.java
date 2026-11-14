package proyectoestrucdatos;

/**
 * Grafo ponderado no dirigido implementado con listas de adyacencia propias.
 * Los vértices se identifican por el id de su estación.
 *
 * @author Elohym Casais
 */
//27*
public class Grafo {
    private final ListaEnlazada<Vertice> vertices;

    /**
     * Crea un grafo vacío.
     */
    public Grafo() {
        this.vertices = new ListaEnlazada<>();
    }

    /**
     * Agrega un vértice si no existe otro con el mismo id.
     *
     * @param estacion estación a agregar
     * @return true si fue agregada
     */
    public boolean agregarEstacion(Estacion estacion) {
        if (buscarVertice(estacion.getId()) != null) {
            return false;
        }
        vertices.agregar(new Vertice(estacion));
        return true;
    }

    /**
     * Busca un vértice por el id de su estación (sin distinguir mayúsculas).
     *
     * @param id id de la estación
     * @return el vértice o null
     */
    public Vertice buscarVertice(String id) {
        if (id == null) {
            return null;
        }
        Nodo<Vertice> aux = vertices.getCabeza();
        while (aux != null) {
            if (aux.getDato().getEstacion().getId().equalsIgnoreCase(id)) {
                return aux.getDato();
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    /**
     * Agrega una conexión bidireccional entre dos estaciones; si existe actualiza su tiempo.
     *
     * @param idOrigen id de una estación
     * @param idDestino id de la otra estación
     * @param tiempoMinutos tiempo de traslado en minutos (mayor que cero)
     * @return true si ambas estaciones existen, son distintas y el tiempo es válido
     */
    //28*
    public boolean agregarConexion(String idOrigen, String idDestino, int tiempoMinutos) {
        if (tiempoMinutos <= 0 || idOrigen == null || idOrigen.equalsIgnoreCase(idDestino)) {
            return false;
        }
        Vertice vOrigen = buscarVertice(idOrigen);
        Vertice vDestino = buscarVertice(idDestino);
        if (vOrigen == null || vDestino == null) {
            return false;
        }
        vOrigen.agregarArista(vDestino.getEstacion(), tiempoMinutos);
        vDestino.agregarArista(vOrigen.getEstacion(), tiempoMinutos);
        return true;
    }

    /**
     * Elimina una estación y todas las conexiones que llegan a ella.
     *
     * @param id id de la estación
     * @return true si existía
     */
    public boolean eliminarEstacion(String id) {
        Vertice vEliminar = buscarVertice(id);
        if (vEliminar == null) {
            return false;
        }
        String idEliminar = vEliminar.getEstacion().getId();
        Nodo<Vertice> aux = vertices.getCabeza();
        while (aux != null) {
            aux.getDato().eliminarArista(idEliminar);
            aux = aux.getSiguiente();
        }
        return vertices.eliminar(vEliminar);
    }

    /**
     * Elimina la conexión entre dos estaciones.
     *
     * @param idOrigen id de una estación
     * @param idDestino id de la otra estación
     * @return true si la conexión existía
     */
    public boolean eliminarConexion(String idOrigen, String idDestino) {
        Vertice vOrigen = buscarVertice(idOrigen);
        Vertice vDestino = buscarVertice(idDestino);
        if (vOrigen == null || vDestino == null) {
            return false;
        }
        boolean e1 = vOrigen.eliminarArista(vDestino.getEstacion().getId());
        boolean e2 = vDestino.eliminarArista(vOrigen.getEstacion().getId());
        return e1 || e2;
    }

    /**
     * Indica si existe conexión directa entre dos estaciones.
     *
     * @param idOrigen id de una estación
     * @param idDestino id de la otra estación
     * @return true si están conectadas
     */
    public boolean existeConexion(String idOrigen, String idDestino) {
        Vertice v = buscarVertice(idOrigen);
        return v != null && v.buscarArista(idDestino) != null;
    }

    /**
     * Cuenta las conexiones del grafo (cada conexión no dirigida cuenta una vez).
     *
     * @return número de aristas
     */
    public int getNumAristas() {
        int suma = 0;
        Nodo<Vertice> aux = vertices.getCabeza();
        while (aux != null) {
            suma += aux.getDato().getGrado();
            aux = aux.getSiguiente();
        }
        return suma / 2;
    }

    /**
     * Elimina todos los vértices y conexiones.
     */
    public void vaciar() {
        vertices.limpiar();
    }

    /**
     * @return lista de vértices
     */
    public ListaEnlazada<Vertice> getVertices() {
        return vertices;
    }

    /**
     * @return número de vértices
     */
    public int getNumVertices() {
        return vertices.getTamano();
    }
}
