package proyectoestrucdatos;

/**
 * Modelo de la red: mantiene sincronizados el {@link Grafo} y las tablas hash
 * que indexan las estaciones por id y por nombre.
 *
 * @author Maria Andreina Piñero
 */
public class RedTransporte {
    private final Grafo grafo;
    private final TablaHash porId;
    private final TablaHash porNombre;

    /**
     * Crea una red vacía.
     */
    public RedTransporte() {
        grafo = new Grafo();
        porId = new TablaHash();
        porNombre = new TablaHash();
    }

    /**
     * Agrega una estación al grafo y a ambos índices.
     *
     * @param estacion estación a agregar
     * @return false si el id o el nombre ya existen o son vacíos
     */
    public boolean agregarEstacion(Estacion estacion) {
        if (estacion == null || estacion.getId().isEmpty() || estacion.getNombre().isEmpty()) {
            return false;
        }
        if (porId.contiene(estacion.getId()) || porNombre.contiene(estacion.getNombre())) {
            return false;
        }
        grafo.agregarEstacion(estacion);
        porId.insertar(estacion.getId(), estacion);
        porNombre.insertar(estacion.getNombre(), estacion);
        return true;
    }

    /**
     * Busca una estación por id o por nombre usando las tablas hash.
     *
     * @param clave id o nombre
     * @return la estación o null
     */
    public Estacion buscar(String clave) {
        Estacion e = porId.buscar(clave);
        return e != null ? e : porNombre.buscar(clave);
    }

    /**
     * Agrega o actualiza una conexión entre dos estaciones.
     *
     * @param claveA id o nombre de una estación
     * @param claveB id o nombre de la otra estación
     * @param minutos tiempo de traslado (mayor que cero)
     * @return true si se pudo registrar
     */
    public boolean agregarConexion(String claveA, String claveB, int minutos) {
        Estacion a = buscar(claveA);
        Estacion b = buscar(claveB);
        if (a == null || b == null) {
            return false;
        }
        return grafo.agregarConexion(a.getId(), b.getId(), minutos);
    }

    /**
     * Elimina una estación del grafo y de ambos índices.
     *
     * @param clave id o nombre de la estación
     * @return true si existía
     */
    public boolean eliminarEstacion(String clave) {
        Estacion e = buscar(clave);
        if (e == null) {
            return false;
        }
        grafo.eliminarEstacion(e.getId());
        porId.eliminar(e.getId());
        porNombre.eliminar(e.getNombre());
        return true;
    }

    /**
     * Elimina la conexión entre dos estaciones.
     *
     * @param claveA id o nombre de una estación
     * @param claveB id o nombre de la otra estación
     * @return true si existía la conexión
     */
    public boolean eliminarConexion(String claveA, String claveB) {
        Estacion a = buscar(claveA);
        Estacion b = buscar(claveB);
        if (a == null || b == null) {
            return false;
        }
        return grafo.eliminarConexion(a.getId(), b.getId());
    }

    /**
     * Elimina todo el contenido de la red.
     */
    public void vaciar() {
        grafo.vaciar();
        porId.limpiar();
        porNombre.limpiar();
    }

    /**
     * @return grafo subyacente
     */
    public Grafo getGrafo() {
        return grafo;
    }

    /**
     * @return número de estaciones
     */
    public int getNumEstaciones() {
        return grafo.getNumVertices();
    }

    /**
     * @return número de conexiones
     */
    public int getNumConexiones() {
        return grafo.getNumAristas();
    }
}
