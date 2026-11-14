package proyectoestrucdatos;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * Formulario para registrar una nueva estación junto con sus conexiones iniciales.
 *
 * @author Maria Andreina Piñero
 */
//9*
public class FormularioEstacion extends JPanel {
    private final RedTransporte red;
    //10*
    private final JTextField campoId = new JTextField(14);
    private final JTextField campoNombre = new JTextField(14);
    private final JTextField campoLinea = new JTextField(14);
    private final JTextField campoZona = new JTextField(14);
    private final JSpinner campoCapacidad = new JSpinner(new SpinnerNumberModel(10000, 0, 10000000, 500));
    private final JComboBox<Estacion> comboVecino = new JComboBox<>();
    private final JSpinner campoTiempo = new JSpinner(new SpinnerNumberModel(2, 1, 999, 1));
    private final JTextArea listaConexiones = new JTextArea(4, 22);
    private final ListaEnlazada<Estacion> vecinos = new ListaEnlazada<>();
    private final ListaEnlazada<Integer> tiempos = new ListaEnlazada<>();

    /**
     * Crea el formulario.
     *
     * @param red red existente, usada para elegir las estaciones a conectar
     */
   //11*
    public FormularioEstacion(RedTransporte red) {
        super(new GridBagLayout());
        this.red = red;
        Nodo<Vertice> v = red.getGrafo().getVertices().getCabeza();
        while (v != null) {
            comboVecino.addItem(v.getDato().getEstacion());
            v = v.getSiguiente();
        }
        listaConexiones.setEditable(false);
        agregarFila(0, "ID (sin comas):", campoId);
        agregarFila(1, "Nombre:", campoNombre);
        agregarFila(2, "Línea:", campoLinea);
        agregarFila(3, "Zona:", campoZona);
        agregarFila(4, "Capacidad pasajeros/hora:", campoCapacidad);
        agregarFila(5, "Conectar con:", comboVecino);
        agregarFila(6, "Tiempo (min):", campoTiempo);
       //12*
        JButton anadir = new JButton("Añadir conexión");
        anadir.addActionListener(e -> anadirConexion());
        GridBagConstraints c = restricciones(7, 1);
        add(anadir, c);
        c = restricciones(8, 0);
        c.gridwidth = 2;
        add(new JScrollPane(listaConexiones), c);
    }

    private GridBagConstraints restricciones(int fila, int columna) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = columna;
        c.gridy = fila;
        c.insets = new Insets(3, 4, 3, 4);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        return c;
    }

    private void agregarFila(int fila, String texto, java.awt.Component campo) {
        add(new JLabel(texto), restricciones(fila, 0));
        add(campo, restricciones(fila, 1));
    }
//13*
    private void anadirConexion() {
        Estacion vecino = (Estacion) comboVecino.getSelectedItem();
        if (vecino == null) {
            return;
        }
        int minutos = (Integer) campoTiempo.getValue();
        Nodo<Estacion> n = vecinos.getCabeza();
        Nodo<Integer> t = tiempos.getCabeza();
        while (n != null) {
            if (n.getDato() == vecino) {
                t.setDato(minutos);
                refrescarLista();
                return;
            }
            n = n.getSiguiente();
            t = t.getSiguiente();
        }
        vecinos.agregar(vecino);
        tiempos.agregar(minutos);
        refrescarLista();
    }
//14*
    private void refrescarLista() {
        StringBuilder sb = new StringBuilder();
        Nodo<Estacion> n = vecinos.getCabeza();
        Nodo<Integer> t = tiempos.getCabeza();
        while (n != null) {
            sb.append(n.getDato().getNombre()).append(" - ").append(t.getDato()).append(" min\n");
            n = n.getSiguiente();
            t = t.getSiguiente();
        }
        listaConexiones.setText(sb.toString());
    }

    /**
     * Valida los datos del formulario.
     *
     * @return mensaje de error, o null si los datos son válidos
     */
   //15*
    public String validar() {
        String id = campoId.getText().trim();
        String nombre = campoNombre.getText().trim();
        if (id.isEmpty() || nombre.isEmpty()) {
            return "El ID y el nombre son obligatorios.";
        }
        if (id.indexOf(',') >= 0 || nombre.indexOf(',') >= 0 || campoLinea.getText().indexOf(',') >= 0
                || campoZona.getText().indexOf(',') >= 0) {
            return "Los campos no pueden contener comas.";
        }
        if (red.buscar(id) != null) {
            return "Ya existe una estación con el ID o nombre \"" + id + "\".";
        }
        if (red.buscar(nombre) != null) {
            return "Ya existe una estación con el nombre \"" + nombre + "\".";
        }
        return null;
    }

    /**
     * Construye la estación con los datos del formulario.
     *
     * @return estación nueva
     */
    public Estacion construirEstacion() {
        String linea = campoLinea.getText().trim();
        String zona = campoZona.getText().trim();
       //16*
        return new Estacion(campoId.getText().trim(), campoNombre.getText().trim(),
                linea.isEmpty() ? "Sin línea" : linea, zona.isEmpty() ? "Sin zona" : zona,
                (Integer) campoCapacidad.getValue());
    }

    /**
     * @return estaciones a conectar con la nueva estación
     */
    public ListaEnlazada<Estacion> getVecinos() {
        return vecinos;
    }

    /**
     * @return tiempos asociados a cada vecino, en el mismo orden
     */
    public ListaEnlazada<Integer> getTiempos() {
        return tiempos;
    }
}
