package proyectoestrucdatos;

/**
 * Servicio con los algoritmos analíticos sobre el {@link Grafo}: ruta de menor tiempo (Dijkstra),
 * subredes aisladas (BFS) e identificación de hubs por centralidad de grado.
 *
 * @author Maria Andreina Piñero
 */
//1*
public class AnalizadorRed {
    private static final int INFINITO = Integer.MAX_VALUE;
//3*
    private Vertice[] aArreglo(Grafo grafo) {
        Vertice[] arreglo = new Vertice[grafo.getNumVertices()];
        Nodo<Vertice> aux = grafo.getVertices().getCabeza();
        int i = 0;
        while (aux != null) {
            arreglo[i++] = aux.getDato();
            aux = aux.getSiguiente();
        }
        return arreglo;
    }
//2*
    private int indiceDe(Vertice[] vertices, String id) {
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].getEstacion().getId().equalsIgnoreCase(id)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Calcula la ruta de menor tiempo entre dos estaciones con el algoritmo de Dijkstra.
     *
     * @param grafo grafo a analizar
     * @param idOrigen id de la estación de origen
     * @param idDestino id de la estación de destino
     * @return resultado con el camino y el tiempo total; sin ruta si no hay conexión o los ids no existen
     */
   //55*
    public ResultadoRuta rutaMinima(Grafo grafo, String idOrigen, String idDestino) {
        Vertice[] vertices = aArreglo(grafo);
        int n = vertices.length;
        int origen = indiceDe(vertices, idOrigen);
        //4*
        int destino = indiceDe(vertices, idDestino);
        ListaEnlazada<Estacion> camino = new ListaEnlazada<>();
        if (origen < 0 || destino < 0) {
            return new ResultadoRuta(camino, -1);
        }
        int[] distancia = new int[n];
        int[] previo = new int[n];
        boolean[] visitado = new boolean[n];
        for (int i = 0; i < n; i++) {
            distancia[i] = INFINITO;
            previo[i] = -1;
        }
        distancia[origen] = 0;
        for (int iter = 0; iter < n; iter++) {
            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && distancia[i] != INFINITO && (u == -1 || distancia[i] < distancia[u])) {
                    u = i;
                }
            }
            if (u == -1 || u == destino) {
                break;
            }
            visitado[u] = true;
            Nodo<Arista> a = vertices[u].getAdyacentes().getCabeza();
            while (a != null) {
                int v = indiceDe(vertices, a.getDato().getDestino().getId());
                int nueva = distancia[u] + a.getDato().getTiempoMinutos();
                if (v >= 0 && !visitado[v] && nueva < distancia[v]) {
                    distancia[v] = nueva;
                    previo[v] = u;
                }
                a = a.getSiguiente();
            }
        }
        if (distancia[destino] == INFINITO) {
            return new ResultadoRuta(camino, -1);
        }
        int[] inverso = new int[n];
        int largo = 0;
        for (int actual = destino; actual != -1; actual = previo[actual]) {
            inverso[largo++] = actual;
        }
        for (int i = largo - 1; i >= 0; i--) {
            camino.agregar(vertices[inverso[i]].getEstacion());
        }
        return new ResultadoRuta(camino, distancia[destino]);
    }

    /**
     * Identifica los componentes conexos (subredes) mediante búsqueda en anchura.
     *
     * @param grafo grafo a analizar
     * @return lista de subredes; cada una es la lista de sus estaciones
     */
    //6*
    public ListaEnlazada<ListaEnlazada<Estacion>> componentesConexos(Grafo grafo) {
        Vertice[] vertices = aArreglo(grafo);
        int n = vertices.length;
        boolean[] visitado = new boolean[n];
        int[] cola = new int[n];
        ListaEnlazada<ListaEnlazada<Estacion>> componentes = new ListaEnlazada<>();
        for (int inicio = 0; inicio < n; inicio++) {
            if (visitado[inicio]) {
                continue;
            }
            ListaEnlazada<Estacion> componente = new ListaEnlazada<>();
            int cabeza = 0;
            int fin = 0;
            cola[fin++] = inicio;
            visitado[inicio] = true;
            while (cabeza < fin) {
                int u = cola[cabeza++];
                componente.agregar(vertices[u].getEstacion());
                Nodo<Arista> a = vertices[u].getAdyacentes().getCabeza();
                while (a != null) {
                    int v = indiceDe(vertices, a.getDato().getDestino().getId());
                    if (v >= 0 && !visitado[v]) {
                        visitado[v] = true;
                        cola[fin++] = v;
                    }
                    a = a.getSiguiente();
                }
            }
            componentes.agregar(componente);
        }
        return componentes;
    }

    /**
     * Ordena las estaciones por grado descendente (centralidad de grado).
     *
     * @param grafo grafo a analizar
     * @return vértices ordenados de mayor a menor grado
     */
    //7*
    public Vertice[] rankingPorGrado(Grafo grafo) {
        Vertice[] v = aArreglo(grafo);
        for (int i = 1; i < v.length; i++) {
            Vertice clave = v[i];
            int j = i - 1;
            while (j >= 0 && v[j].getGrado() < clave.getGrado()) {
                v[j + 1] = v[j];
                j--;
            }
            v[j + 1] = clave;
        }
        return v;
    }

    /**
     * Obtiene los hubs de transferencia: las estaciones con el mayor número de conexiones directas.
     *
     * @param grafo grafo a analizar
     * @return estaciones con grado máximo (vacío si el grafo no tiene conexiones)
     */
    public ListaEnlazada<Vertice> hubs(Grafo grafo) {
        ListaEnlazada<Vertice> resultado = new ListaEnlazada<>();
        Vertice[] ranking = rankingPorGrado(grafo);
        if (ranking.length == 0 || ranking[0].getGrado() == 0) {
            return resultado;
        }
        int maximo = ranking[0].getGrado();
        for (int i = 0; i < ranking.length && ranking[i].getGrado() == maximo; i++) {
            resultado.agregar(ranking[i]);
        }
        return resultado;
    }
}
