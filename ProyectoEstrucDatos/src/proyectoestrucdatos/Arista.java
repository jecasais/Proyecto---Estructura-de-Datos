package proyectoestrucdatos;

/**
 * Conexión ponderada hacia una estación destino dentro de una lista de adyacencia.
 *
 * @author Elohym Casais
 */
public class Arista {
    private Estacion destino;
    private int tiempoMinutos;

    /**
     * Crea una arista.
     *
     * @param destino estación a la que conduce
     * @param tiempoMinutos tiempo de traslado en minutos
     */
    public Arista(Estacion destino, int tiempoMinutos) {
        this.destino = destino;
        this.tiempoMinutos = tiempoMinutos;
    }

    /**
     * @return estación destino
     */
    public Estacion getDestino() {
        return destino;
    }

    /**
     * @param destino nueva estación destino
     */
    public void setDestino(Estacion destino) {
        this.destino = destino;
    }

    /**
     * @return tiempo de traslado en minutos
     */
    public int getTiempoMinutos() {
        return tiempoMinutos;
    }

    /**
     * @param tiempoMinutos nuevo tiempo en minutos
     */
    public void setTiempoMinutos(int tiempoMinutos) {
        this.tiempoMinutos = tiempoMinutos;
    }
}
