package proyectoestrucdatos;

/**
 * Resumen de una operación de lectura de archivo: contadores y líneas descartadas.
 *
 * @author Maria Andreina Piñero
 */
public class ResultadoCarga {
    private int estaciones;
    private int conexiones;
    private final ListaEnlazada<String> advertencias = new ListaEnlazada<>();

    /**
     * Suma una estación leída.
     */
    public void sumarEstacion() {
        estaciones++;
    }

    /**
     * Suma una conexión leída.
     */
    public void sumarConexion() {
        conexiones++;
    }

    /**
     * Registra una línea descartada.
     *
     * @param mensaje descripción del problema
     */
    public void agregarAdvertencia(String mensaje) {
        advertencias.agregar(mensaje);
    }

    /**
     * @return estaciones cargadas
     */
    public int getEstaciones() {
        return estaciones;
    }

    /**
     * @return conexiones cargadas
     */
    public int getConexiones() {
        return conexiones;
    }

    /**
     * @return lista de advertencias
     */
    public ListaEnlazada<String> getAdvertencias() {
        return advertencias;
    }
}
