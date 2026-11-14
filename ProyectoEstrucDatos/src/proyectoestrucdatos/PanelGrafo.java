package proyectoestrucdatos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.Viewer;

/**
 * Panel de visualización de la red con GraphStream. Refleja el {@link Grafo} en memoria,
 * colorea las estaciones por línea y permite resaltar rutas y subredes.
 *
 * @author Maria Andreina Piñero
 */
public class PanelGrafo extends JPanel {
    private static final String[] PALETA = {
        "#D32F2F", "#1976D2", "#F57C00", "#388E3C", "#7B1FA2", "#00897B", "#C2185B", "#5D4037"
    };
    private static final String COLOR_RUTA = "#FFD600";
    private static final String COLOR_ALERTA = "#212121";
    private static final String HOJA_ESTILO =
            "graph { fill-color: #FAFAFA; padding: 40px; }"
            + "node { size: 16px; text-size: 11px; text-alignment: above; text-style: bold; "
            + "stroke-mode: plain; stroke-color: #37474F; stroke-width: 1px; fill-color: #90A4AE; }"
            + "edge { size: 2px; fill-color: #90A4AE; text-size: 9px; text-color: #78909C; }";

    private final Graph grafoVisual;
    private final Viewer visor;
    private final JPanel leyenda;
    private Grafo grafoActual;
    private ListaEnlazada<Estacion> rutaResaltada;
    private ListaEnlazada<ListaEnlazada<Estacion>> subredesResaltadas;

    /**
     * Crea el panel con un grafo visual vacío.
     */
    public PanelGrafo() {
        super(new BorderLayout());
        System.setProperty("org.graphstream.ui", "swing");
        grafoVisual = new SingleGraph("UrbanRoute");
        grafoVisual.setAttribute("ui.stylesheet", HOJA_ESTILO);
        grafoVisual.setAttribute("ui.quality");
        grafoVisual.setAttribute("ui.antialias");
        visor = new SwingViewer(grafoVisual, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        visor.enableAutoLayout();
        Component vista = (Component) visor.addDefaultView(false);
        add(vista, BorderLayout.CENTER);
        leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        leyenda.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xCFD8DC)));
        add(leyenda, BorderLayout.SOUTH);
    }

    /**
     * Sincroniza la visualización con el grafo en memoria y reaplica los resaltados vigentes.
     *
     * @param grafo grafo a mostrar
     */
    public void actualizar(Grafo grafo) {
        grafoActual = grafo;
        sincronizarNodos(grafo);
        sincronizarAristas(grafo);
        aplicarColores();
        actualizarLeyenda(grafo);
    }

    private void sincronizarNodos(Grafo grafo) {
        for (int i = grafoVisual.getNodeCount() - 1; i >= 0; i--) {
            Node n = grafoVisual.getNode(i);
            if (grafo.buscarVertice(n.getId()) == null) {
                grafoVisual.removeNode(n.getId());
            }
        }
        Nodo<Vertice> v = grafo.getVertices().getCabeza();
        while (v != null) {
            Estacion e = v.getDato().getEstacion();
            Node n = grafoVisual.getNode(e.getId());
            if (n == null) {
                n = grafoVisual.addNode(e.getId());
            }
            n.setAttribute("ui.label", e.getNombre());
            v = v.getSiguiente();
        }
    }

    private void sincronizarAristas(Grafo grafo) {
        for (int i = grafoVisual.getEdgeCount() - 1; i >= 0; i--) {
            Edge e = grafoVisual.getEdge(i);
            String a = e.getNode0().getId();
            String b = e.getNode1().getId();
            Vertice va = grafo.buscarVertice(a);
            if (va == null || va.buscarArista(b) == null) {
                grafoVisual.removeEdge(e.getId());
            }
        }
        Nodo<Vertice> v = grafo.getVertices().getCabeza();
        while (v != null) {
            String idA = v.getDato().getEstacion().getId();
            Nodo<Arista> a = v.getDato().getAdyacentes().getCabeza();
            while (a != null) {
                String idB = a.getDato().getDestino().getId();
                if (idA.compareToIgnoreCase(idB) < 0) {
                    String idArista = idA + "--" + idB;
                    Edge e = grafoVisual.getEdge(idArista);
                    if (e == null) {
                        e = grafoVisual.addEdge(idArista, idA, idB);
                    }
                    e.setAttribute("ui.label", a.getDato().getTiempoMinutos() + " min");
                }
                a = a.getSiguiente();
            }
            v = v.getSiguiente();
        }
    }

    private String colorLinea(String linea) {
        int numero = 0;
        boolean hayDigitos = false;
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (Character.isDigit(c)) {
                numero = numero * 10 + (c - '0');
                hayDigitos = true;
            }
        }
        int indice = hayDigitos ? (numero + PALETA.length - 1) % PALETA.length
                : (linea.toLowerCase().hashCode() & 0x7fffffff) % PALETA.length;
        return PALETA[indice];
    }

    private int posicionEnLista(ListaEnlazada<Estacion> lista, Estacion e) {
        int i = 0;
        Nodo<Estacion> n = lista.getCabeza();
        while (n != null) {
            if (n.getDato() == e) {
                return i;
            }
            i++;
            n = n.getSiguiente();
        }
        return -1;
    }

    private void aplicarColores() {
        if (grafoActual == null) {
            return;
        }
        Nodo<Vertice> v = grafoActual.getVertices().getCabeza();
        while (v != null) {
            Estacion e = v.getDato().getEstacion();
            Node n = grafoVisual.getNode(e.getId());
            if (n != null) {
                String color = colorLinea(e.getLinea());
                String borde = "#37474F";
                String tamano = "16px";
                if (subredesResaltadas != null) {
                    color = colorSubred(e);
                }
                if (rutaResaltada != null && posicionEnLista(rutaResaltada, e) >= 0) {
                    color = COLOR_RUTA;
                    borde = COLOR_ALERTA;
                    tamano = "24px";
                }
                n.setAttribute("ui.style", "fill-color: " + color + "; stroke-color: " + borde + "; size: " + tamano + ";");
            }
            v = v.getSiguiente();
        }
        for (int i = 0; i < grafoVisual.getEdgeCount(); i++) {
            Edge e = grafoVisual.getEdge(i);
            boolean enRuta = false;
            if (rutaResaltada != null) {
                Estacion a = grafoActual.buscarVertice(e.getNode0().getId()).getEstacion();
                Estacion b = grafoActual.buscarVertice(e.getNode1().getId()).getEstacion();
                int pa = posicionEnLista(rutaResaltada, a);
                int pb = posicionEnLista(rutaResaltada, b);
                enRuta = pa >= 0 && pb >= 0 && (pa - pb == 1 || pb - pa == 1);
            }
            e.setAttribute("ui.style", enRuta
                    ? "fill-color: " + COLOR_ALERTA + "; size: 5px;"
                    : "fill-color: #90A4AE; size: 2px;");
        }
    }

    private String colorSubred(Estacion e) {
        int k = 0;
        Nodo<ListaEnlazada<Estacion>> c = subredesResaltadas.getCabeza();
        while (c != null) {
            if (posicionEnLista(c.getDato(), e) >= 0) {
                return PALETA[k % PALETA.length];
            }
            k++;
            c = c.getSiguiente();
        }
        return "#90A4AE";
    }

    private void actualizarLeyenda(Grafo grafo) {
        leyenda.removeAll();
        ListaEnlazada<String> lineas = new ListaEnlazada<>();
        Nodo<Vertice> v = grafo.getVertices().getCabeza();
        while (v != null) {
            String linea = v.getDato().getEstacion().getLinea();
            if (!lineas.contiene(linea)) {
                lineas.agregar(linea);
            }
            v = v.getSiguiente();
        }
        if (subredesResaltadas != null) {
            leyenda.add(new JLabel("Colores = subredes aisladas"));
        } else {
            Nodo<String> n = lineas.getCabeza();
            while (n != null) {
                JLabel etiqueta = new JLabel(n.getDato());
                etiqueta.setIcon(new IconoColor(Color.decode(colorLinea(n.getDato()))));
                leyenda.add(etiqueta);
                n = n.getSiguiente();
            }
        }
        JLabel ayuda = new JLabel("   Arrastre los nodos para reorganizar; rueda del ratón para acercar.");
        ayuda.setForeground(Color.GRAY);
        leyenda.add(ayuda);
        leyenda.revalidate();
        leyenda.repaint();
    }

    /**
     * Resalta una ruta sobre el grafo.
     *
     * @param ruta estaciones del recorrido en orden
     */
    public void resaltarRuta(ListaEnlazada<Estacion> ruta) {
        rutaResaltada = ruta;
        aplicarColores();
    }

    /**
     * Colorea cada subred con un color distinto.
     *
     * @param subredes lista de subredes
     */
    public void resaltarSubredes(ListaEnlazada<ListaEnlazada<Estacion>> subredes) {
        subredesResaltadas = subredes;
        aplicarColores();
        if (grafoActual != null) {
            actualizarLeyenda(grafoActual);
        }
    }

    /**
     * Quita todos los resaltados y vuelve a colorear por línea.
     */
    public void limpiarResaltado() {
        rutaResaltada = null;
        subredesResaltadas = null;
        aplicarColores();
        if (grafoActual != null) {
            actualizarLeyenda(grafoActual);
        }
    }

    /**
     * Libera el visor de GraphStream.
     */
    public void cerrar() {
        visor.close();
    }

    private static class IconoColor implements javax.swing.Icon {
        private final Color color;

        IconoColor(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, java.awt.Graphics g, int x, int y) {
            g.setColor(color);
            g.fillOval(x, y, 12, 12);
        }

        @Override
        public int getIconWidth() {
            return 14;
        }

        @Override
        public int getIconHeight() {
            return 12;
        }
    }
}
