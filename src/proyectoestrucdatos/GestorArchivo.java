package proyectoestrucdatos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * Controlador de entrada y salida de archivos CSV. Acepta líneas de estaciones
 * (ID_Estacion,Nombre,Linea,Zona,Capacidad_Pasajeros_Hora) y de conexiones
 * (Estacion_Origen,Estacion_Destino,Tiempo_Minutos) en el mismo archivo o en archivos separados.
 *
 * @author Maria Andreina Piñero
 */
public class GestorArchivo {
    private static final String ENCABEZADO_ESTACIONES = "ID_Estacion,Nombre,Linea,Zona,Capacidad_Pasajeros_Hora";
    private static final String ENCABEZADO_CONEXIONES = "Estacion_Origen,Estacion_Destino,Tiempo_Minutos";
    //17*
    private static final String RECURSO_MAESTRO = "metro_caracas.csv";
    private static final String RUTA_MAESTRO = "datos" + File.separator + "metro_caracas.csv";

    private String rutaArchivoActual;
    private boolean incluirEstaciones = true;

    /**
     * @return ruta del archivo cargado actualmente, o null
     */
    public String getRutaArchivoActual() {
        return rutaArchivoActual;
    }

    /**
     * Fuerza que el archivo guardado incluya la sección de estaciones.
     */
    public void marcarInformacionEstaciones() {
        incluirEstaciones = true;
    }

    /**
     * Carga el archivo maestro por defecto. Si no existe en disco, lo crea a partir del recurso embebido.
     *
     * @param red red donde se cargan los datos
     * @return resumen de la carga
     * @throws IOException si no se puede leer o crear el archivo
     */
    public ResultadoCarga cargarPorDefecto(RedTransporte red) throws IOException {
       //18*
        File archivo = new File(RUTA_MAESTRO);
        if (!archivo.exists()) {
            InputStream recurso = GestorArchivo.class.getResourceAsStream(RECURSO_MAESTRO);
            if (recurso == null) {
                throw new IOException("No se encontró el archivo maestro " + RUTA_MAESTRO);
            }
            File carpeta = archivo.getAbsoluteFile().getParentFile();
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }
           //19*
            try (InputStream in = recurso; FileOutputStream out = new FileOutputStream(archivo)) {
                byte[] buffer = new byte[4096];
                int n;
                while ((n = in.read(buffer)) > 0) {
                    out.write(buffer, 0, n);
                }
            }
        }
        return cargar(red, new File[]{archivo});
    }

    /**
     * Reemplaza el contenido de la red con el de uno o varios archivos CSV.
     * Primero se procesan todas las estaciones y luego las conexiones. Las líneas inválidas se
     * descartan y se reportan como advertencias. Si ningún archivo aporta datos válidos la red
     * anterior se conserva.
     *
     * @param red red a poblar
     * @param archivos archivos a leer
     * @return resumen de la carga
     * @throws IOException si algún archivo no puede leerse o no contiene datos válidos
     */
    //20*
    public ResultadoCarga cargar(RedTransporte red, File[] archivos) throws IOException {
        ResultadoCarga resultado = new ResultadoCarga();
        ListaEnlazada<String[]> estaciones = new ListaEnlazada<>();
        ListaEnlazada<String[]> conexiones = new ListaEnlazada<>();
        for (File archivo : archivos) {
            leerLineas(archivo, estaciones, conexiones, resultado);
        }
        if (estaciones.estaVacia() && conexiones.estaVacia()) {
            throw new IOException("El archivo no contiene estaciones ni conexiones válidas.");
        }
        red.vaciar();
        boolean hayEstaciones = false;
        Nodo<String[]> nodo = estaciones.getCabeza();
        while (nodo != null) {
            //21*
            String[] c = nodo.getDato();
            int capacidad = Integer.parseInt(c[4]);
            if (red.agregarEstacion(new Estacion(c[0], c[1], c[2], c[3], capacidad))) {
                resultado.sumarEstacion();
                hayEstaciones = true;
            } else {
                resultado.agregarAdvertencia("Estación duplicada ignorada: " + c[0] + " / " + c[1]);
            }
            nodo = nodo.getSiguiente();
        }
        nodo = conexiones.getCabeza();
        while (nodo != null) {
            String[] c = nodo.getDato();
            Estacion origen = obtenerOCrear(red, c[0], resultado);
            Estacion destino = obtenerOCrear(red, c[1], resultado);
            if (origen != null && destino != null
                    && red.agregarConexion(origen.getId(), destino.getId(), Integer.parseInt(c[2]))) {
                resultado.sumarConexion();
            } else {
                resultado.agregarAdvertencia("Conexión ignorada: " + c[0] + " - " + c[1]);
            }
            nodo = nodo.getSiguiente();
        }
        incluirEstaciones = hayEstaciones;
        rutaArchivoActual = archivos[0].getAbsolutePath();
        return resultado;
    }

    private Estacion obtenerOCrear(RedTransporte red, String clave, ResultadoCarga resultado) {
        Estacion e = red.buscar(clave);
        if (e == null) {
            e = new Estacion(clave, clave, "Sin línea", "Sin zona", 0);
            if (!red.agregarEstacion(e)) {
                return null;
            }
            resultado.sumarEstacion();
        }
        return e;
    }

    //22*
    private void leerLineas(File archivo, ListaEnlazada<String[]> estaciones,
            ListaEnlazada<String[]> conexiones, ResultadoCarga resultado) throws IOException {
        try (Reader lector = new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(lector)) {
            String linea;
            int numero = 0;
            while ((linea = br.readLine()) != null) {
                numero++;
                //23*
                if (numero == 1 && !linea.isEmpty() && linea.charAt(0) == '﻿') {
                    linea = linea.substring(1);
                }
                linea = linea.trim();
                if (linea.isEmpty() || linea.charAt(0) == '#') {
                    continue;
                }
                String[] campos = dividir(linea);
                if (campos.length == 5 && esEntero(campos[4])) {
                    if (campos[0].isEmpty() || campos[1].isEmpty() || Integer.parseInt(campos[4]) < 0) {
                        resultado.agregarAdvertencia(archivo.getName() + " línea " + numero + ": estación inválida");
                    } else {
                        estaciones.agregar(campos);
                    }
                } else if (campos.length == 3 && esEntero(campos[2])) {
                    if (campos[0].isEmpty() || campos[1].isEmpty() || campos[0].equalsIgnoreCase(campos[1])
                            || Integer.parseInt(campos[2]) <= 0) {
                        resultado.agregarAdvertencia(archivo.getName() + " línea " + numero + ": conexión inválida");
                    } else {
                        conexiones.agregar(campos);
                    }
                } else if (!esEncabezado(campos)) {
                    resultado.agregarAdvertencia(archivo.getName() + " línea " + numero + ": formato no reconocido");
                }
            }
        }
    }

    private boolean esEncabezado(String[] campos) {
        return (campos.length == 5 || campos.length == 3) && !esEntero(campos[campos.length - 1]);
    }

    private boolean esEntero(String texto) {
        if (texto.isEmpty() || texto.length() > 9) {
            return false;
        }
        //24
        for (int i = 0; i < texto.length(); i++) {
            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private String[] dividir(String linea) {
        int partes = 1;
        for (int i = 0; i < linea.length(); i++) {
            if (linea.charAt(i) == ',') {
                partes++;
            }
        }
        String[] campos = new String[partes];
        int indice = 0;
        int inicio = 0;
        for (int i = 0; i <= linea.length(); i++) {
            if (i == linea.length() || linea.charAt(i) == ',') {
                campos[indice++] = linea.substring(inicio, i).trim();
                inicio = i + 1;
            }
        }
        return campos;
    }

    /**
     * Escribe el contenido de la red en el archivo fuente actual.
     *
     * @param red red a guardar
     * @throws IOException si no hay archivo asociado o falla la escritura
     */
    public void guardar(RedTransporte red) throws IOException {
        if (rutaArchivoActual == null) {
            throw new IOException("No hay un archivo asociado para guardar.");
        }
        guardarEn(red, new File(rutaArchivoActual));
    }

    /**
     * Escribe el contenido de la red en el archivo indicado y lo establece como archivo actual.
     *
     * @param red red a guardar
     * @param destino archivo de salida
     * @throws IOException si falla la escritura
     */
   //25*
    public void guardarEn(RedTransporte red, File destino) throws IOException {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(destino), StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(w)) {
            if (incluirEstaciones) {
                bw.write(ENCABEZADO_ESTACIONES);
                bw.newLine();
                Nodo<Vertice> v = red.getGrafo().getVertices().getCabeza();
                while (v != null) {
                    Estacion e = v.getDato().getEstacion();
                    bw.write(e.getId() + "," + e.getNombre() + "," + e.getLinea() + "," + e.getZona() + ","
                            + e.getCapacidadPasajeros());
                    bw.newLine();
                    v = v.getSiguiente();
                }
            }
            bw.write(ENCABEZADO_CONEXIONES);
            bw.newLine();
            Nodo<Vertice> v = red.getGrafo().getVertices().getCabeza();
            while (v != null) {
                String idOrigen = v.getDato().getEstacion().getId();
                Nodo<Arista> a = v.getDato().getAdyacentes().getCabeza();
                while (a != null) {
                    //26*
                    String idDestino = a.getDato().getDestino().getId();
                    if (idOrigen.compareToIgnoreCase(idDestino) < 0) {
                        bw.write(idOrigen + "," + idDestino + "," + a.getDato().getTiempoMinutos());
                        bw.newLine();
                    }
                    a = a.getSiguiente();
                }
                v = v.getSiguiente();
            }
        }
        rutaArchivoActual = destino.getAbsolutePath();
    }
}
