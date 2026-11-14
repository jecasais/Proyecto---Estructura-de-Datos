package proyectoestrucdatos;

/**
 * Entidad de dominio que representa una estación o parada de la red de transporte.
 *
 * @author Elohym Casais
 */
public class Estacion {
    private String id;
    private String nombre;
    private String linea;
    private String zona;
    private int capacidadPasajeros;

    /**
     * Crea una estación.
     *
     * @param id identificador único
     * @param nombre nombre de la estación
     * @param linea línea de transporte a la que pertenece
     * @param zona zona de la ciudad
     * @param capacidadPasajeros capacidad de pasajeros por hora
     */
    public Estacion(String id, String nombre, String linea, String zona, int capacidadPasajeros) {
        this.id = id;
        this.nombre = nombre;
        this.linea = linea;
        this.zona = zona;
        this.capacidadPasajeros = capacidadPasajeros;
    }

    /**
     * @return identificador de la estación
     */
    public String getId() {
        return id;
    }

    /**
     * @param id nuevo identificador
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return nombre de la estación
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return línea de transporte
     */
    public String getLinea() {
        return linea;
    }

    /**
     * @param linea nueva línea
     */
    public void setLinea(String linea) {
        this.linea = linea;
    }

    /**
     * @return zona de la ciudad
     */
    public String getZona() {
        return zona;
    }

    /**
     * @param zona nueva zona
     */
    public void setZona(String zona) {
        this.zona = zona;
    }

    /**
     * @return capacidad de pasajeros por hora
     */
    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    /**
     * @param capacidadPasajeros nueva capacidad de pasajeros por hora
     */
    public void setCapacidadPasajeros(int capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }

    /**
     * Descripción corta de la estación.
     *
     * @return texto con nombre, línea y zona
     */
    public String getInformacion() {
        return nombre + " (" + linea + " - " + zona + ")";
    }

    @Override
    public String toString() {
        return nombre + " [" + id + "]";
    }
}
