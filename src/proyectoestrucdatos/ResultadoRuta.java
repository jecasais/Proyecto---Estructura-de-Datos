package proyectoestrucdatos;

/**
 * Resultado de una búsqueda de ruta de menor tiempo.
 *
 * @author Maria Andreina Piñero
 */
public class ResultadoRuta {
    private final ListaEnlazada<Estacion> camino;
    private final int tiempoTotal;

    /**
     * Crea un resultado.
     *
     * @param camino estaciones del recorrido en orden, vacío si no hay ruta
     * @param tiempoTotal tiempo total en minutos, -1 si no hay ruta
     */
    public ResultadoRuta(ListaEnlazada<Estacion> camino, int tiempoTotal) {
        this.camino = camino;
        this.tiempoTotal = tiempoTotal;
    }

    /**
     * @return true si existe una ruta
     */
    public boolean existe() {
        return tiempoTotal >= 0;
    }

    /**
     * @return estaciones del recorrido
     */
    public ListaEnlazada<Estacion> getCamino() {
        return camino;
    }

    /**
     * @return tiempo total en minutos o -1
     */
    public int getTiempoTotal() {
        return tiempoTotal;
    }
}
