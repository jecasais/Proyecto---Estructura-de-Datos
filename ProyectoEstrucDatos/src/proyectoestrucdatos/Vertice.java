package proyectoestrucdatos;

/**
 * Vértice del grafo: una {@link Estacion} junto con su lista de adyacencia.
 *
 * @author Elohym Casais
 */
public class Vertice {
    private final Estacion estacion;
    private final ListaEnlazada<Arista> adyacentes;

    /**
     * Crea un vértice sin conexiones.
     *
     * @param estacion estación que representa
     */
    public Vertice(Estacion estacion) {
        this.estacion = estacion;
        this.adyacentes = new ListaEnlazada<>();
    }

    /**
     * @return estación del vértice
     */
    public Estacion getEstacion() {
        return estacion;
    }

    /**
     * @return lista de adyacencia
     */
    public ListaEnlazada<Arista> getAdyacentes() {
        return adyacentes;
    }

    /**
     * Agrega una conexión hacia otra estación; si ya existe, actualiza su tiempo.
     *
     * @param destino estación destino
     * @param tiempoMinutos tiempo de traslado en minutos
     */
    public void agregarArista(Estacion destino, int tiempoMinutos) {
        Nodo<Arista> aux = adyacentes.getCabeza();
        while (aux != null) {
            if (aux.getDato().getDestino().getId().equalsIgnoreCase(destino.getId())) {
                aux.getDato().setTiempoMinutos(tiempoMinutos);
                return;
            }
            aux = aux.getSiguiente();
        }
        adyacentes.agregar(new Arista(destino, tiempoMinutos));
    }

    /**
     * Elimina la conexión hacia una estación.
     *
     * @param idDestino id de la estación destino
     * @return true si la conexión existía
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
     * Busca la arista hacia una estación.
     *
     * @param idDestino id de la estación destino
     * @return la arista o null si no existe
     */
    public Arista buscarArista(String idDestino) {
        Nodo<Arista> aux = adyacentes.getCabeza();
        while (aux != null) {
            if (aux.getDato().getDestino().getId().equalsIgnoreCase(idDestino)) {
                return aux.getDato();
            }
            aux = aux.getSiguiente();
        }
        return null;
    }

    /**
     * Grado del vértice.
     *
     * @return número de conexiones directas
     */
    public int getGrado() {
        return adyacentes.getTamano();
    }
}
