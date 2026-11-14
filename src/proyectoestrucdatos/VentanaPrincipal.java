package proyectoestrucdatos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Ventana principal de UrbanRoute. Coordina la interacción del usuario y delega en
 * {@link GestorArchivo}, {@link AnalizadorRed}, {@link RedTransporte} y {@link PanelGrafo}.
 *
 * @author Maria Andreina Piñero
 */
//42*
public class VentanaPrincipal extends JFrame {
    private final RedTransporte red = new RedTransporte();
    private final GestorArchivo gestorArchivo = new GestorArchivo();
    private final AnalizadorRed analizador = new AnalizadorRed();
    private final PanelGrafo panelGrafo = new PanelGrafo();
    private final JComboBox<Estacion> comboOrigen = new JComboBox<>();
    private final JComboBox<Estacion> comboDestino = new JComboBox<>();
    private final JTextArea areaResultados = new JTextArea();
    private final JLabel etiquetaArchivo = new JLabel("Archivo: (ninguno)");
    private final JLabel etiquetaEstado = new JLabel(" ");
    private boolean modificado;
    private String ultimaOrigen;
    private String ultimoDestino;

    /**
     * Construye la ventana y carga el archivo maestro por defecto.
     */
    public VentanaPrincipal() {
        super("UrbanRoute - Análisis de Redes de Transporte Multimodal");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            etiquetaEstado.setText("Se usa el aspecto visual por defecto.");
        }
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1280, 780);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);
        construirInterfaz();
        //43*
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salir();
            }
        });
        cargarArchivoInicial();
    }

    //44*
    private void construirInterfaz() {
        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        lateral.setPreferredSize(new Dimension(300, 100));

        JPanel archivo = seccion("Archivo", 3);
        archivo.add(boton("Cargar archivo CSV...", "Selecciona uno o varios archivos CSV", e -> cargarArchivo()));
        archivo.add(boton("Actualizar repositorio", "Guarda en el archivo fuente los cambios en memoria",
                e -> actualizarRepositorio()));
        etiquetaArchivo.setFont(etiquetaArchivo.getFont().deriveFont(Font.PLAIN, 11f));
        archivo.add(etiquetaArchivo);
        lateral.add(archivo);

        JPanel modificar = seccion("Modificar red", 4);
        modificar.add(boton("Agregar estación...", "Nueva estación con sus conexiones", e -> agregarEstacion()));
        modificar.add(boton("Agregar conexión...", "Conecta dos estaciones existentes", e -> agregarConexion()));
        modificar.add(boton("Eliminar estación (cierre)...", "Simula el cierre de una estación",
                e -> eliminarEstacion()));
        modificar.add(boton("Eliminar conexión...", "Quita la conexión entre dos estaciones",
                e -> eliminarConexion()));
        lateral.add(modificar);

        JPanel analisis = seccion("Análisis", 7);
        analisis.add(new JLabel("Estación origen:"));
        analisis.add(comboOrigen);
        analisis.add(new JLabel("Estación destino:"));
        analisis.add(comboDestino);
        analisis.add(boton("Ruta de menor tiempo", "Algoritmo de Dijkstra", e -> calcularRuta()));
        analisis.add(boton("Subredes aisladas", "Componentes conexos mediante BFS", e -> mostrarSubredes()));
        analisis.add(boton("Hubs de transferencia", "Estaciones con mayor grado", e -> mostrarHubs()));
        lateral.add(analisis);

        JPanel vista = seccion("Vista", 1);
        vista.add(boton("Limpiar resaltado", "Vuelve a colorear por línea", e -> panelGrafo.limpiarResaltado()));
        lateral.add(vista);

        areaResultados.setEditable(false);
        areaResultados.setLineWrap(true);
        areaResultados.setWrapStyleWord(true);
        areaResultados.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaResultados.setMargin(new java.awt.Insets(6, 8, 6, 8));
        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        scrollResultados.setPreferredSize(new Dimension(100, 190));

        JSplitPane centro = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelGrafo, scrollResultados);
        centro.setResizeWeight(0.8);

        etiquetaEstado.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(new JScrollPane(lateral, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.WEST);
        raiz.add(centro, BorderLayout.CENTER);
        raiz.add(etiquetaEstado, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private JPanel seccion(String titulo, int filas) {
        JPanel p = new JPanel(new GridLayout(0, 1, 0, 5));
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(titulo),
                BorderFactory.createEmptyBorder(2, 6, 6, 6)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40 + filas * 34));
        return p;
    }

    private JButton boton(String texto, String ayuda, java.awt.event.ActionListener accion) {
        JButton b = new JButton(texto);
        b.setToolTipText(ayuda);
        b.addActionListener(accion);
        return b;
    }

    //45*
    private void mensaje(String texto, int tipo) {
        JOptionPane.showMessageDialog(this, texto, "UrbanRoute", tipo);
    }

    private void cargarArchivoInicial() {
        try {
            ResultadoCarga r = gestorArchivo.cargarPorDefecto(red);
            etiquetaEstado.setText("Archivo maestro cargado: " + r.getEstaciones() + " estaciones, "
                    + r.getConexiones() + " conexiones.");
        } catch (IOException ex) {
            mensaje("No se pudo cargar el archivo maestro:\n" + ex.getMessage()
                    + "\nUse \"Cargar archivo CSV...\" para continuar.", JOptionPane.WARNING_MESSAGE);
        }
        refrescar();
        areaResultados.setText(textoSubredes());
    }

    private void refrescar() {
        etiquetaArchivo.setText("Archivo: " + (gestorArchivo.getRutaArchivoActual() == null ? "(ninguno)"
                : new File(gestorArchivo.getRutaArchivoActual()).getName()) + (modificado ? "  *sin guardar*" : ""));
        recargarCombo(comboOrigen);
        recargarCombo(comboDestino);
        if (comboDestino.getItemCount() > 1) {
            comboDestino.setSelectedIndex(comboDestino.getItemCount() - 1);
        }
        panelGrafo.limpiarResaltado();
        panelGrafo.actualizar(red.getGrafo());
    }

    private void recargarCombo(JComboBox<Estacion> combo) {
        Object seleccion = combo.getSelectedItem();
        combo.removeAllItems();
        Nodo<Vertice> v = red.getGrafo().getVertices().getCabeza();
        while (v != null) {
            combo.addItem(v.getDato().getEstacion());
            v = v.getSiguiente();
        }
        if (seleccion != null) {
            combo.setSelectedItem(seleccion);
        }
    }

    private void cargarArchivo() {
        if (red.getNumEstaciones() > 0) {
            Object[] opciones = {"Guardar y continuar", "Continuar sin guardar", "Cancelar"};
            int r = JOptionPane.showOptionDialog(this,
                    "Hay datos en memoria. Cargar un nuevo archivo los sobrescribirá.\n"
                    + "Se recomienda guardar antes de continuar.",
                    "Guardar antes de cargar", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                    opciones, opciones[0]);
            if (r == 0) {
                if (!guardar()) {
                    return;
                }
            } else if (r != 1) {
                return;
            }
        }
        //46*
        JFileChooser selector = new JFileChooser(new File("datos").isDirectory() ? new File("datos") : new File("."));
        selector.setDialogTitle("Seleccionar archivo(s) CSV");
        selector.setMultiSelectionEnabled(true);
        selector.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
        if (selector.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File[] archivos = selector.getSelectedFiles();
        try {
            ResultadoCarga r = gestorArchivo.cargar(red, archivos);
            modificado = false;
            ultimaOrigen = null;
            ultimoDestino = null;
            refrescar();
            StringBuilder sb = new StringBuilder("Archivo cargado: " + r.getEstaciones() + " estaciones, "
                    + r.getConexiones() + " conexiones.\n");
            Nodo<String> adv = r.getAdvertencias().getCabeza();
            int mostradas = 0;
            while (adv != null && mostradas < 15) {
                sb.append("- ").append(adv.getDato()).append('\n');
                mostradas++;
                adv = adv.getSiguiente();
            }
            if (r.getAdvertencias().getTamano() > 0) {
                sb.insert(0, r.getAdvertencias().getTamano() + " línea(s) ignoradas:\n");
                mensaje(sb.toString(), JOptionPane.WARNING_MESSAGE);
            }
            areaResultados.setText("Archivo cargado: " + r.getEstaciones() + " estaciones, "
                    + r.getConexiones() + " conexiones.");
            etiquetaEstado.setText("Carga completada.");
        } catch (IOException ex) {
            mensaje("No se pudo cargar el archivo:\n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean guardar() {
        try {
            if (gestorArchivo.getRutaArchivoActual() == null) {
                JFileChooser selector = new JFileChooser(new File("."));
                selector.setDialogTitle("Guardar red como CSV");
                selector.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
                if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                    return false;
                }
                File destino = selector.getSelectedFile();
                if (!destino.getName().toLowerCase().endsWith(".csv")) {
                    destino = new File(destino.getPath() + ".csv");
                }
                gestorArchivo.guardarEn(red, destino);
            } else {
                gestorArchivo.guardar(red);
            }
            modificado = false;
            refrescarEtiquetaArchivo();
            etiquetaEstado.setText("Repositorio actualizado.");
            return true;
        } catch (IOException ex) {
            mensaje("No se pudo guardar el archivo:\n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void refrescarEtiquetaArchivo() {
        etiquetaArchivo.setText("Archivo: " + (gestorArchivo.getRutaArchivoActual() == null ? "(ninguno)"
                : new File(gestorArchivo.getRutaArchivoActual()).getName()) + (modificado ? "  *sin guardar*" : ""));
    }

    private void actualizarRepositorio() {
        if (red.getNumEstaciones() == 0) {
            mensaje("No hay datos en memoria para guardar.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String destino = gestorArchivo.getRutaArchivoActual();
        if (destino != null) {
            int r = JOptionPane.showConfirmDialog(this,
                    "Se sobrescribirá el archivo:\n" + destino + "\n¿Desea continuar?",
                    "Actualizar repositorio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (r != JOptionPane.YES_OPTION) {
                return;
            }
        }
        if (guardar()) {
            mensaje("Archivo actualizado correctamente.", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void tras() {
        modificado = true;
        refrescar();
        reanalizar();
    }

    private void reanalizar() {
        StringBuilder sb = new StringBuilder();
        if (ultimaOrigen != null && red.buscar(ultimaOrigen) != null && red.buscar(ultimoDestino) != null) {
            sb.append(textoRuta(red.buscar(ultimaOrigen), red.buscar(ultimoDestino))).append("\n\n");
        }
        sb.append(textoSubredes());
        areaResultados.setText(sb.toString());
        areaResultados.setCaretPosition(0);
    }

    private void agregarEstacion() {
        FormularioEstacion form = new FormularioEstacion(red);
        while (true) {
            int r = JOptionPane.showConfirmDialog(this, form, "Agregar estación",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (r != JOptionPane.OK_OPTION) {
                return;
            }
            String error = form.validar();
            if (error == null) {
                break;
            }
            mensaje(error, JOptionPane.WARNING_MESSAGE);
        }
        Estacion nueva = form.construirEstacion();
        if (!red.agregarEstacion(nueva)) {
            mensaje("No se pudo agregar la estación.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Nodo<Estacion> v = form.getVecinos().getCabeza();
        Nodo<Integer> t = form.getTiempos().getCabeza();
        while (v != null) {
            red.agregarConexion(nueva.getId(), v.getDato().getId(), t.getDato());
            v = v.getSiguiente();
            t = t.getSiguiente();
        }
        gestorArchivo.marcarInformacionEstaciones();
        tras();
        etiquetaEstado.setText("Estación agregada: " + nueva.getNombre());
    }

    private Estacion[] estacionesComoArreglo() {
        Estacion[] a = new Estacion[red.getNumEstaciones()];
        Nodo<Vertice> v = red.getGrafo().getVertices().getCabeza();
        int i = 0;
        while (v != null) {
            a[i++] = v.getDato().getEstacion();
            v = v.getSiguiente();
        }
        return a;
    }

    private void agregarConexion() {
        if (red.getNumEstaciones() < 2) {
            mensaje("Se necesitan al menos dos estaciones.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JComboBox<Estacion> a = new JComboBox<>(estacionesComoArreglo());
        JComboBox<Estacion> b = new JComboBox<>(estacionesComoArreglo());
        b.setSelectedIndex(1);
        javax.swing.JSpinner tiempo = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(2, 1, 999, 1));
        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        panel.add(new JLabel("Estación A:"));
        panel.add(a);
        panel.add(new JLabel("Estación B:"));
        panel.add(b);
        panel.add(new JLabel("Tiempo (min):"));
        panel.add(tiempo);
        if (JOptionPane.showConfirmDialog(this, panel, "Agregar conexión", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            return;
        }
        Estacion ea = (Estacion) a.getSelectedItem();
        Estacion eb = (Estacion) b.getSelectedItem();
        if (ea == eb) {
            mensaje("Debe elegir dos estaciones distintas.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean existia = red.getGrafo().existeConexion(ea.getId(), eb.getId());
        if (red.agregarConexion(ea.getId(), eb.getId(), (Integer) tiempo.getValue())) {
            tras();
            etiquetaEstado.setText((existia ? "Conexión actualizada: " : "Conexión agregada: ")
                    + ea.getNombre() + " - " + eb.getNombre());
        } else {
            mensaje("No se pudo agregar la conexión.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEstacion() {
        if (red.getNumEstaciones() == 0) {
            mensaje("No hay estaciones.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Estacion e = (Estacion) JOptionPane.showInputDialog(this, "Estación a cerrar:", "Eliminar estación",
                JOptionPane.QUESTION_MESSAGE, null, estacionesComoArreglo(), comboOrigen.getSelectedItem());
        if (e == null) {
            return;
        }
        int gradoPrevio = red.getGrafo().buscarVertice(e.getId()).getGrado();
        int r = JOptionPane.showConfirmDialog(this, "Se cerrará \"" + e.getNombre() + "\" y se eliminarán sus "
                + gradoPrevio + " conexiones.\n¿Continuar?", "Confirmar cierre", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        red.eliminarEstacion(e.getId());
        tras();
        etiquetaEstado.setText("Estación eliminada: " + e.getNombre());
    }

    private void eliminarConexion() {
        if (red.getNumConexiones() == 0) {
            mensaje("No hay conexiones.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JComboBox<Estacion> a = new JComboBox<>(estacionesComoArreglo());
        JComboBox<Estacion> b = new JComboBox<>();
        Runnable cargarVecinos = () -> {
            b.removeAllItems();
            Estacion origen = (Estacion) a.getSelectedItem();
            if (origen != null) {
                Nodo<Arista> n = red.getGrafo().buscarVertice(origen.getId()).getAdyacentes().getCabeza();
                while (n != null) {
                    b.addItem(n.getDato().getDestino());
                    n = n.getSiguiente();
                }
            }
        };
        a.addActionListener(e -> cargarVecinos.run());
        cargarVecinos.run();
        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        panel.add(new JLabel("Estación A:"));
        panel.add(a);
        panel.add(new JLabel("Conectada con:"));
        panel.add(b);
        if (JOptionPane.showConfirmDialog(this, panel, "Eliminar conexión", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            return;
        }
        Estacion ea = (Estacion) a.getSelectedItem();
        Estacion eb = (Estacion) b.getSelectedItem();
        if (ea == null || eb == null) {
            mensaje("La estación elegida no tiene conexiones.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (red.eliminarConexion(ea.getId(), eb.getId())) {
            tras();
            etiquetaEstado.setText("Conexión eliminada: " + ea.getNombre() + " - " + eb.getNombre());
        }
    }

    private String textoRuta(Estacion origen, Estacion destino) {
        ResultadoRuta r = analizador.rutaMinima(red.getGrafo(), origen.getId(), destino.getId());
        if (!r.existe()) {
            return "No existe ruta entre " + origen.getNombre() + " y " + destino.getNombre() + ".";
        }
        StringBuilder sb = new StringBuilder("RUTA DE MENOR TIEMPO: " + r.getTiempoTotal() + " min\n");
        Nodo<Estacion> n = r.getCamino().getCabeza();
        while (n != null) {
            sb.append(n.getDato().getNombre());
            if (n.getSiguiente() != null) {
                sb.append(" -> ");
            }
            n = n.getSiguiente();
        }
        sb.append("\n(").append(r.getCamino().getTamano()).append(" estaciones)");
        return sb.toString();
    }

    private void calcularRuta() {
        Estacion origen = (Estacion) comboOrigen.getSelectedItem();
        Estacion destino = (Estacion) comboDestino.getSelectedItem();
        if (origen == null || destino == null) {
            mensaje("Cargue una red y seleccione origen y destino.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (origen == destino) {
            mensaje("El origen y el destino deben ser distintos.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ultimaOrigen = origen.getId();
        ultimoDestino = destino.getId();
        ResultadoRuta r = analizador.rutaMinima(red.getGrafo(), origen.getId(), destino.getId());
        areaResultados.setText(textoRuta(origen, destino));
        areaResultados.setCaretPosition(0);
        panelGrafo.limpiarResaltado();
        if (r.existe()) {
            panelGrafo.resaltarRuta(r.getCamino());
        }
    }

    private String textoSubredes() {
        ListaEnlazada<ListaEnlazada<Estacion>> comps = analizador.componentesConexos(red.getGrafo());
        StringBuilder sb = new StringBuilder("SUBREDES (componentes conexos): " + comps.getTamano() + "\n");
        Nodo<ListaEnlazada<Estacion>> c = comps.getCabeza();
        int k = 1;
        while (c != null) {
            sb.append("Subred ").append(k++).append(" (").append(c.getDato().getTamano()).append(" estaciones): ");
            Nodo<Estacion> e = c.getDato().getCabeza();
            while (e != null) {
                sb.append(e.getDato().getNombre());
                if (e.getSiguiente() != null) {
                    sb.append(", ");
                }
                e = e.getSiguiente();
            }
            sb.append('\n');
            c = c.getSiguiente();
        }
        return sb.toString();
    }

    private void mostrarSubredes() {
        areaResultados.setText(textoSubredes());
        areaResultados.setCaretPosition(0);
        panelGrafo.limpiarResaltado();
        panelGrafo.resaltarSubredes(analizador.componentesConexos(red.getGrafo()));
    }

    private void mostrarHubs() {
        ListaEnlazada<Vertice> hubs = analizador.hubs(red.getGrafo());
        if (hubs.estaVacia()) {
            areaResultados.setText("No hay conexiones en la red, por lo que no hay hubs.");
            return;
        }
        StringBuilder sb = new StringBuilder("HUBS DE TRANSFERENCIA (grado máximo = "
                + hubs.getCabeza().getDato().getGrado() + "):\n");
        Nodo<Vertice> h = hubs.getCabeza();
        while (h != null) {
            sb.append("* ").append(h.getDato().getEstacion().getNombre()).append(" (")
                    .append(h.getDato().getEstacion().getLinea()).append(")\n");
            h = h.getSiguiente();
        }
        sb.append("\nRanking de las 10 estaciones con más conexiones:\n");
        Vertice[] ranking = analizador.rankingPorGrado(red.getGrafo());
        //48
        for (int i = 0; i < ranking.length && i < 10; i++) {
            sb.append(String.format("%2d. %-26s %d conexiones%n", i + 1,
                    ranking[i].getEstacion().getNombre(), ranking[i].getGrado()));
        }
        areaResultados.setText(sb.toString());
        areaResultados.setCaretPosition(0);
        panelGrafo.limpiarResaltado();
        ListaEnlazada<Estacion> estaciones = new ListaEnlazada<>();
        h = hubs.getCabeza();
        while (h != null) {
            estaciones.agregar(h.getDato().getEstacion());
            h = h.getSiguiente();
        }
        panelGrafo.resaltarRuta(estaciones);
    }

    private void salir() {
        if (modificado) {
            Object[] opciones = {"Guardar y salir", "Salir sin guardar", "Cancelar"};
            int r = JOptionPane.showOptionDialog(this, "Hay cambios sin guardar en el repositorio.",
                    "Salir", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);
            if (r == 0) {
                if (!guardar()) {
                    return;
                }
            } else if (r != 1) {
                return;
            }
        }
        //48*
        panelGrafo.cerrar();
        dispose();
        System.exit(0);
    }
}
