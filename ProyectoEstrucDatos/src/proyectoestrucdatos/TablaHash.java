package proyectoestrucdatos;

/**
 * Tabla de dispersión propia con encadenamiento separado que indexa objetos {@link Estacion}
 * por una clave de texto. Las claves no distinguen mayúsculas de minúsculas.
 * Se redimensiona automáticamente para conservar acceso en tiempo promedio O(1).
 *
 * @author Elohym Casais
 */
public class TablaHash {
    private static final double FACTOR_CARGA_MAXIMO = 0.75;

    private ListaEnlazada<EntradaHash>[] tabla;
    private int capacidad;
    private int tamano;

    /**
     * Crea una tabla con la capacidad inicial indicada.
     *
     * @param capacidad número inicial de casillas (mínimo 1)
     */
    public TablaHash(int capacidad) {
        this.capacidad = capacidad < 1 ? 1 : capacidad;
        this.tamano = 0;
        this.tabla = crearArreglo(this.capacidad);
    }

    /**
     * Crea una tabla con capacidad inicial 101.
     */
    public TablaHash() {
        this(101);
    }

    @SuppressWarnings("unchecked")
    private ListaEnlazada<EntradaHash>[] crearArreglo(int n) {
        ListaEnlazada<EntradaHash>[] arreglo = new ListaEnlazada[n];
        for (int i = 0; i < n; i++) {
            arreglo[i] = new ListaEnlazada<>();
        }
        return arreglo;
    }

    private int calcularIndice(String clave, int modulo) {
        long hash = 7;
        for (int i = 0; i < clave.length(); i++) {
            hash = (31 * hash + Character.toLowerCase(clave.charAt(i))) % modulo;
        }
        return (int) hash;
    }

    private void redimensionar() {
        ListaEnlazada<EntradaHash>[] anterior = tabla;
        int nuevaCapacidad = capacidad * 2 + 1;
        ListaEnlazada<EntradaHash>[] nueva = crearArreglo(nuevaCapacidad);
        for (int i = 0; i < capacidad; i++) {
            Nodo<EntradaHash> actual = anterior[i].getCabeza();
            while (actual != null) {
                EntradaHash entrada = actual.getDato();
                nueva[calcularIndice(entrada.getClave(), nuevaCapacidad)].agregar(entrada);
                actual = actual.getSiguiente();
            }
        }
        tabla = nueva;
        capacidad = nuevaCapacidad;
    }

    /**
     * Inserta una estación o actualiza la existente con la misma clave.
     *
     * @param clave clave de indexación (id o nombre)
     * @param estacion estación a almacenar
     */
    public void insertar(String clave, Estacion estacion) {
        ListaEnlazada<EntradaHash> lista = tabla[calcularIndice(clave, capacidad)];
        Nodo<EntradaHash> actual = lista.getCabeza();
        while (actual != null) {
            EntradaHash entrada = actual.getDato();
            if (entrada.getClave().equalsIgnoreCase(clave)) {
                entrada.setValor(estacion);
                return;
            }
            actual = actual.getSiguiente();
        }
        lista.agregar(new EntradaHash(clave, estacion));
        tamano++;
        if ((double) tamano / capacidad > FACTOR_CARGA_MAXIMO) {
            redimensionar();
        }
    }

    /**
     * Busca una estación por su clave.
     *
     * @param clave clave a buscar
     * @return la estación asociada, o null si no existe
     */
    public Estacion buscar(String clave) {
        if (clave == null) {
            return null;
        }
        Nodo<EntradaHash> actual = tabla[calcularIndice(clave, capacidad)].getCabeza();
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
     * Indica si existe una estación con la clave dada.
     *
     * @param clave clave a consultar
     * @return true si está indexada
     */
    public boolean contiene(String clave) {
        return buscar(clave) != null;
    }

    /**
     * Elimina la entrada con la clave dada.
     *
     * @param clave clave a eliminar
     * @return true si existía y fue eliminada
     */
    public boolean eliminar(String clave) {
        if (clave == null) {
            return false;
        }
        ListaEnlazada<EntradaHash> lista = tabla[calcularIndice(clave, capacidad)];
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

    /**
     * Elimina todas las entradas.
     */
    public void limpiar() {
        capacidad = 101;
        tabla = crearArreglo(capacidad);
        tamano = 0;
    }

    /**
     * Cantidad de entradas almacenadas.
     *
     * @return número de entradas
     */
    public int getTamano() {
        return tamano;
    }

    /**
     * Número actual de casillas de la tabla.
     *
     * @return capacidad
     */
    public int getCapacidad() {
        return capacidad;
    }
}
