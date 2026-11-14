# Fundamentación Técnica de Elementos Adicionales (`// *1` – `// *48`)

Este documento detalla y justifica técnicamente todas las librerías externas, APIs estándar de Java (Swing/AWT, Java I/O, NIO.2, Colecciones/Utilidades) y características avanzadas del lenguaje empleadas en el proyecto **UrbanRoute (Metro de Caracas)** que exceden el contenido teórico visto en clase.

---

## 1. AnalizadorRed.java

### `// *1` — Representación de infinito mediante `Integer.MAX_VALUE`
* **Código de referencia:**
  ```java
  int[] dist = new int[n];
  Arrays.fill(dist, Integer.MAX_VALUE);
  ```
* **Explicación:** En la implementación del algoritmo de Dijkstra sobre arreglos de enteros primitivos, se emplea la constante de clase `Integer.MAX_VALUE` (2147483647) para representar una distancia inicial infinita antes de relajar las aristas. Se verifica previamente `dist[u] != Integer.MAX_VALUE` para evitar desbordamiento aritmético (*integer overflow*) al sumar el peso de la arista.
* **Fundamento oficial (Oracle - Integer.MAX_VALUE):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Integer.html#MAX_VALUE

### `// *2` — Comparación de cadenas sin distinción de mayúsculas con `String.equalsIgnoreCase()`
* **Código de referencia:**
  ```java
  if (estacion.getId().equalsIgnoreCase(id)) {
  ```
* **Explicación:** Método de la clase `java.lang.String` que compara lexicográficamente dos cadenas ignorando diferencias entre letras mayúsculas y minúsculas, garantizando búsquedas resilientes frente a variaciones de formato en los identificadores de estaciones.
* **Fundamento oficial (Oracle - String.equalsIgnoreCase):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html#equalsIgnoreCase(java.lang.String)

### `// *3` — Inferencia de tipos en instanciación genérica mediante el operador diamante (`<>`)
* **Código de referencia:**
  ```java
  ListaEnlazada<Estacion> camino = new ListaEnlazada<>();
  ```
* **Explicación:** Introducido en Java 7, el operador diamante (`<>`) permite al compilador inferir automáticamente los argumentos de tipo genérico en el constructor a partir del contexto de declaración de la variable, reduciendo la verbosidad sin perder seguridad de tipos en tiempo de compilación.
* **Fundamento oficial (Oracle - Inferencia de tipos en Genéricos):** https://docs.oracle.com/javase/tutorial/java/generics/genTypeInference.html

### `// *4` — Inicialización masiva de arreglos con `java.util.Arrays.fill()`
* **Código de referencia:**
  ```java
  Arrays.fill(dist, Integer.MAX_VALUE);
  Arrays.fill(prev, -1);
  ```
* **Explicación:** Método estático de utilidad perteneciente al paquete `java.util.Arrays` que asigna un valor específico a cada posición de un arreglo unidimensional en una sola llamada, reemplazando bucles explícitos de inicialización.
* **Fundamento oficial (Oracle - Arrays.fill):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Arrays.html#fill(int%5B%5D,int)

### `// *5` — Bucle mejorado `for-each` sobre arreglos
* **Código de referencia:**
  ```java
  for (Vertice v : vertices) {
  ```
* **Explicación:** Estructura sintáctica introducida en Java 5 (*Enhanced for loop*) que permite recorrer secuencialmente todos los elementos de un arreglo o colección `Iterable` sin manipular índices explícitos, evitando errores de límites fuera de rango.
* **Fundamento oficial (Oracle - Bucle for mejorado):** https://docs.oracle.com/javase/tutorial/java/nutsandbolts/for.html

### `// *6` — Ordenamiento por inserción manual sobre arreglos de objetos
* **Código de referencia:**
  ```java
  Vertice clave = arreglo[i];
  int j = i - 1;
  while (j >= 0 && arreglo[j].getGrado() < clave.getGrado()) {
      arreglo[j + 1] = arreglo[j];
      j--;
  }
  arreglo[j + 1] = clave;
  ```
* **Explicación:** Para generar el ranking descendente de estaciones por grado de conectividad sin depender de comparadores externos (`Comparator`), se implementa el algoritmo de ordenamiento por inserción (*Insertion Sort*) directamente sobre referencias de objetos `Vertice`.
* **Fundamento oficial (Oracle - Referencia de ordenamiento en java.util.Arrays):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Arrays.html

---

## 2. Estacion.java

### `// *7` — Anotación `@Override` y sobrescritura de `Object.toString()`
* **Código de referencia:**
  ```java
  @Override
  public String toString() {
      return nombre + " (" + id + ")";
  }
  ```
* **Explicación:** La anotación `@Override` indica al compilador que el método redefine una implementación heredada de `java.lang.Object`. Sobrescribir `toString()` permite que componentes gráficos de Swing como `JComboBox<Estacion>` rendericen automáticamente un texto legible en lugar de la dirección de memoria por defecto.
* **Fundamento oficial (Oracle - Sobrescritura de métodos y @Override):** https://docs.oracle.com/javase/tutorial/java/IandI/override.html

### `// *8` — Contrato de igualdad lógica mediante `Object.equals(Object)` y operador `instanceof`
* **Código de referencia:**
  ```java
  @Override
  public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof Estacion)) return false;
      Estacion otra = (Estacion) obj;
      return id != null && id.equalsIgnoreCase(otra.id);
  }
  ```
* **Explicación:** Redefine la igualdad de objetos para evaluar identidad de negocio (mismo `id` de estación) en lugar de identidad de referencia en memoria. Emplea el operador de comprobación de tipos en tiempo de ejecución `instanceof` antes de realizar un *downcasting* seguro a `Estacion`.
* **Fundamento oficial (Oracle - Object.equals):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#equals(java.lang.Object)

### `// *9` — Consistencia del contrato `equals()` / `hashCode()` con `java.util.Objects.hash()`
* **Código de referencia:**
  ```java
  @Override
  public int hashCode() {
      return Objects.hash(id == null ? null : id.toLowerCase());
  }
  ```
* **Explicación:** Según la especificación del lenguaje Java, toda clase que sobrescribe `equals()` debe sobrescribir `hashCode()` para garantizar que dos objetos iguales produzcan el mismo código hash. Se utiliza `Objects.hash()` junto con `toLowerCase()` para mantener coherencia con `equalsIgnoreCase()`.
* **Fundamento oficial (Oracle - Objects.hash):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Objects.html#hash(java.lang.Object...)

### `// *10` — Operador condicional ternario (`?:`)
* **Código de referencia:**
  ```java
  return Objects.hash(id == null ? null : id.toLowerCase());
  ```
* **Explicación:** Operador ternario de evaluación condicional en línea (`condición ? exprSiTrue : exprSiFalse`) que previene una `NullPointerException` antes de invocar `toLowerCase()` sobre el identificador.
* **Fundamento oficial (Oracle - Operadores condicionales):** https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html

---

## 3. FormularioEstacion.java

### `// *11` — Contenedor gráfico `javax.swing.JPanel` y gestor de diseño `java.awt.BorderLayout`
* **Código de referencia:**
  ```java
  public class FormularioEstacion extends JPanel {
  ```
* **Explicación:** `JPanel` es un contenedor ligero de Swing utilizado para agrupar componentes de interfaz gráfica. Al extender de `JPanel`, el formulario puede incrustarse directamente dentro de un diálogo modal de `JOptionPane`, organizando sus regiones internas mediante `BorderLayout` (norte, centro, sur).
* **Fundamento oficial (Oracle - Uso de JPanel):** https://docs.oracle.com/javase/tutorial/uiswing/components/panel.html

### `// *12` — Campos de texto de una línea (`JTextField`) y lista desplegable tipada (`JComboBox<E>`)
* **Código de referencia:**
  ```java
  private final JTextField campoId = new JTextField(12);
  private final JComboBox<Estacion> comboVecino = new JComboBox<>();
  ```
* **Explicación:** `JTextField` permite la captura y edición de texto en una sola línea, mientras que `JComboBox<Estacion>` ofrece una lista desplegable parametrizada con genéricos para seleccionar estaciones existentes de forma segura.
* **Fundamento oficial (Oracle - Uso de JTextField):** https://docs.oracle.com/javase/tutorial/uiswing/components/textfield.html

### `// *13` — Selector numérico acotado con `JSpinner` y `SpinnerNumberModel`
* **Código de referencia:**
  ```java
  private final JSpinner spinnerCapacidad = new JSpinner(new SpinnerNumberModel(15000, 100, 500000, 500));
  ```
* **Explicación:** `JSpinner` es un campo de entrada numérica con botones de incremento/decremento respaldado por un `SpinnerNumberModel`, el cual restringe automáticamente el rango válido (`mínimo`, `máximo` y `paso`) evitando entradas no numéricas del usuario.
* **Fundamento oficial (Oracle - Uso de JSpinner):** https://docs.oracle.com/javase/tutorial/uiswing/components/spinner.html

### `// *14` — Modelo de lista dinámico (`DefaultListModel<E>`) y vista desplazable (`JList<E>` + `JScrollPane`)
* **Código de referencia:**
  ```java
  private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
  private final JList<String> listaVisual = new JList<>(modeloLista);
  ```
* **Explicación:** Sigue la arquitectura Modelo-Vista-Controlador (MVC) de Swing: `DefaultListModel` gestiona dinámicamente los elementos en memoria (`addElement`, `remove`, `clear`), `JList` los renderiza visualmente en pantalla y `JScrollPane` añade barras de desplazamiento cuando el contenido supera el área visible.
* **Fundamento oficial (Oracle - Uso de JList):** https://docs.oracle.com/javase/tutorial/uiswing/components/list.html

### `// *15` — Expresiones Lambda e interfaz funcional `java.awt.event.ActionListener`
* **Código de referencia:**
  ```java
  btnAgregarVecino.addActionListener(e -> agregarVecino());
  ```
* **Explicación:** Introducidas en Java 8, las expresiones lambda (`parámetro -> expresión`) implementan de manera concisa interfaces funcionales (interfaces con un único método abstracto), como `ActionListener.actionPerformed(ActionEvent)`, para responder a clics en botones.
* **Fundamento oficial (Oracle - Expresiones Lambda):** https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html

### `// *16` — Construcción eficiente de cadenas mutables con `java.lang.StringBuilder`
* **Código de referencia:**
  ```java
  StringBuilder errores = new StringBuilder();
  ```
* **Explicación:** A diferencia de `String` (que es inmutable y crea un nuevo objeto en cada concatenación `+`), `StringBuilder` mantiene un búfer interno mutable de caracteres para acumular mensajes de validación sin penalización de memoria.
* **Fundamento oficial (Oracle - Clase StringBuilder):** https://docs.oracle.com/javase/tutorial/java/data/buffers.html

### `// *17` — Sanitización y conversión de cadenas (`String.trim()` y `String.toUpperCase()`)
* **Código de referencia:**
  ```java
  String id = campoId.getText().trim().toUpperCase();
  ```
* **Explicación:** `trim()` elimina los espacios en blanco iniciales y finales ingresados accidentalmente por el usuario, y `toUpperCase()` normaliza el identificador a mayúsculas antes de registrarlo en la red.
* **Fundamento oficial (Oracle - String.trim):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html#trim()

---

## 4. GestorArchivo.java

### `// *18` — Rutas del sistema de archivos multiplataforma con NIO.2 (`java.nio.file.Path` y `Paths`)
* **Código de referencia:**
  ```java
  Path ruta = Paths.get("datos", "metro_caracas.csv");
  ```
* **Explicación:** La API `java.nio.file` (NIO.2) abstrae las rutas de archivos de forma independiente al sistema operativo (resolviendo separadores `/` o `\`), reemplazando a la antigua clase `java.io.File` para operaciones modernas de entrada/salida.
* **Fundamento oficial (Oracle - Operaciones con Path en NIO.2):** https://docs.oracle.com/javase/tutorial/essential/io/pathOps.html

### `// *19` — Codificación estándar UTF-8 con `java.nio.charset.StandardCharsets`
* **Código de referencia:**
  ```java
  try (BufferedReader br = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
  ```
* **Explicación:** `StandardCharsets.UTF_8` garantiza que la lectura y escritura de archivos CSV interprete correctamente caracteres propios del español (tildes, diéresis y la letra `ñ`) independientemente de la codificación predeterminada del sistema operativo.
* **Fundamento oficial (Oracle - StandardCharsets):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/charset/StandardCharsets.html

### `// *20` — Gestión automática de recursos con `try-with-resources` (`AutoCloseable`)
* **Código de referencia:**
  ```java
  try (BufferedReader br = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
  ```
* **Explicación:** Sentencia introducida en Java 7 que asegura el cierre automático (`close()`) de flujos de lectura/escritura al finalizar el bloque `try`, incluso si ocurre una excepción, previniendo fugas de descriptores de archivo.
* **Fundamento oficial (Oracle - Sentencia try-with-resources):** https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html

### `// *21` — Lectura y escritura con búfer mediante `BufferedReader`, `BufferedWriter` y `Files`
* **Código de referencia:**
  ```java
  BufferedWriter bw = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)
  ```
* **Explicación:** Los flujos con búfer (`BufferedReader` / `BufferedWriter`) minimizan las llamadas directas al disco agrupando caracteres en memoria intermedia, permitiendo leer línea por línea (`readLine()`) y escribir eficientemente registros CSV.
* **Fundamento oficial (Oracle - Flujos con búfer en Java I/O):** https://docs.oracle.com/javase/tutorial/essential/io/file.html#textfiles

### `// *22` — Eliminación de marca de orden de bytes UTF-8 (BOM `\uFEFF`)
* **Código de referencia:**
  ```java
  if (linea.startsWith("\uFEFF")) {
      linea = linea.substring(1);
  }
  ```
* **Explicación:** Algunos editores como Excel o el Bloc de notas de Windows insertan el carácter invisible *Byte Order Mark* (`\uFEFF`) al inicio de archivos UTF-8. Esta verificación lo detecta y recorta mediante `substring(1)` para evitar que corrompa el primer encabezado o identificador leído.
* **Fundamento oficial (Oracle - Manipulación de cadenas y subcadenas):** https://docs.oracle.com/javase/tutorial/java/data/manipstrings.html

### `// *23` — División de registros CSV mediante `String.split(regex, limit)`
* **Código de referencia:**
  ```java
  String[] partes = linea.split(",", -1);
  ```
* **Explicación:** Divide una línea de texto usando la coma como delimitador. El segundo parámetro negativo (`-1`) instruye al motor de expresiones regulares a conservar columnas vacías al final de la línea en lugar de descartarlas.
* **Fundamento oficial (Oracle - String.split):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html#split(java.lang.String,int)

### `// *24` — Conversión de texto a entero y manejo de `NumberFormatException`
* **Código de referencia:**
  ```java
  try {
      capacidad = Integer.parseInt(partes[4].trim());
  } catch (NumberFormatException ex) {
  ```
* **Explicación:** `Integer.parseInt()` analiza una cadena de dígitos y devuelve su valor primitivo `int`. Si el campo CSV contiene texto no numérico, lanza la excepción no verificada `NumberFormatException`, que se captura para registrar una advertencia sin detener la carga del archivo.
* **Fundamento oficial (Oracle - Integer.parseInt):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Integer.html#parseInt(java.lang.String)

### `// *25` — Creación automática de directorios padre con `Files.createDirectories()`
* **Código de referencia:**
  ```java
  if (ruta.getParent() != null) {
      Files.createDirectories(ruta.getParent());
  }
  ```
* **Explicación:** Crea de forma recursiva toda la jerarquía de carpetas inexistentes antes de escribir un archivo de salida, sin lanzar error si los directorios ya existían previamente.
* **Fundamento oficial (Oracle - Files.createDirectories):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/Files.html#createDirectories(java.nio.file.Path,java.nio.file.attribute.FileAttribute...)

### `// *26` — Propagación declarada de excepciones verificadas (`throws IOException`)
* **Código de referencia:**
  ```java
  public ResultadoCarga cargar(RedTransporte red, File[] archivos) throws IOException {
  ```
* **Explicación:** Siguiendo el modelo *Catch or Specify Requirement* de Java, las operaciones de entrada/salida que pueden fallar por permisos o archivos ausentes declaran `throws IOException` en su firma para delegar el manejo visual del error a la capa de interfaz gráfica (`VentanaPrincipal`).
* **Fundamento oficial (Oracle - Manejo y declaración de excepciones):** https://docs.oracle.com/javase/tutorial/essential/exceptions/catchOrDeclare.html

---

## 5. Grafo.java

### `// *27` — Inmutabilidad de referencias mediante el modificador `final`
* **Código de referencia:**
  ```java
  private final ListaEnlazada<Vertice> vertices;
  ```
* **Explicación:** El modificador `final` en un atributo de instancia garantiza que la referencia asignada en el constructor no pueda reasignarse a otro objeto durante todo el ciclo de vida de la instancia, reforzando el encapsulamiento y la seguridad frente a modificaciones accidentales.
* **Fundamento oficial (Oracle - Variables y clases final):** https://docs.oracle.com/javase/tutorial/java/IandI/final.html

---

## 6. ListaEnlazada.java

### `// *28` — Implementación de la interfaz `java.lang.Iterable<T>` y clase interna `Iterator<T>`
* **Código de referencia:**
  ```java
  public class ListaEnlazada<T> implements Iterable<T> {
      @Override
      public Iterator<T> iterator() {
  ```
* **Explicación:** Al implementar la interfaz estándar `Iterable<T>` y proveer un objeto `java.util.Iterator<T>` (con sus métodos `hasNext()` y `next()`), la estructura de datos propia `ListaEnlazada` adquiere compatibilidad nativa con el bucle `for-each` de Java.
* **Fundamento oficial (Oracle - Interfaz Iterable):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Iterable.html

### `// *29` — Clases y métodos genéricos parametrizados (`<T>`) y `NoSuchElementException`
* **Código de referencia:**
  ```java
  if (!hasNext()) {
      throw new NoSuchElementException();
  }
  ```
* **Explicación:** Los genéricos (`<T>`) permiten parametrizar tipos de datos manteniendo verificación estricta en tiempo de compilación sin necesidad de conversiones explícitas (*casts*). Adicionalmente, el iterador cumple con el contrato oficial lanzando `java.util.NoSuchElementException` cuando se solicita un elemento habiendo alcanzado el final de la secuencia.
* **Fundamento oficial (Oracle - Tipos Genéricos):** https://docs.oracle.com/javase/tutorial/java/generics/types.html

---

## 7. PanelGrafo.java

### `// *30` — Librería externa GraphStream (`SingleGraph`) para modelado visual de grafos
* **Código de referencia:**
  ```java
  import org.graphstream.graph.implementations.SingleGraph;
  ```
* **Explicación:** **GraphStream** es una librería externa de código abierto especializada en el modelado, análisis dinámico y visualización interactiva de grafos. `SingleGraph` implementa un grafo simple (sin aristas paralelas entre el mismo par de nodos) sobre el cual se sincronizan los vértices y conexiones de la red de transporte.
* **Fundamento oficial (GraphStream - Getting Started & Graph API):** https://graphstream-project.org/doc/Tutorials/Getting-Started/

### `// *31` — Configuración del motor de renderizado Swing con `System.setProperty()`
* **Código de referencia:**
  ```java
  System.setProperty("org.graphstream.ui", "swing");
  ```
* **Explicación:** Establece una propiedad global de entorno en la Máquina Virtual de Java (`System.setProperty`) antes de inicializar el visor, indicando a GraphStream que utilice el renderizador compatible con **Java Swing** (`gs-ui-swing`).
* **Fundamento oficial (Oracle - System.setProperty):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.html#setProperty(java.lang.String,java.lang.String)

### `// *32` — Incrustación del lienzo interactivo `SwingViewer` y `ViewPanel` dentro de un `JPanel`
* **Código de referencia:**
  ```java
  SwingViewer visor = new SwingViewer(grafoVisual, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
  ViewPanel vista = (ViewPanel) visor.addDefaultView(false);
  ```
* **Explicación:** `SwingViewer` gestiona el renderizado continuo y el algoritmo de distribución física de nodos (*auto-layout* basado en fuerzas de atracción/repulsión) en un hilo separado (`GRAPH_IN_ANOTHER_THREAD`), mientras que `addDefaultView(false)` devuelve un componente `ViewPanel` incrustable dentro de la ventana Swing principal en lugar de abrir una ventana independiente.
* **Fundamento oficial (GraphStream - Graph Visualisation & SwingViewer):** https://graphstream-project.org/doc/Tutorials/Graph-Visualisation/

### `// *33` — Estilización visual mediante hojas de estilo CSS de GraphStream (`ui.stylesheet`)
* **Código de referencia:**
  ```java
  grafoVisual.setAttribute("ui.stylesheet", HOJA_ESTILO);
  grafoVisual.setAttribute("ui.quality");
  grafoVisual.setAttribute("ui.antialias");
  ```
* **Explicación:** GraphStream permite definir la apariencia de nodos, aristas, etiquetas, colores de línea y estados resaltados mediante reglas declarativas inspiradas en **CSS** aplicadas a través del atributo `ui.stylesheet`, activando además suavizado de bordes (`ui.antialias`) y renderizado de alta calidad (`ui.quality`).
* **Fundamento oficial (GraphStream - CSS Reference):** https://graphstream-project.org/doc/FAQ/Attributes/GraphStream-CSS-Reference/

### `// *34` — Zoom interactivo con la rueda del ratón mediante `MouseWheelListener`
* **Código de referencia:**
  ```java
  vista.addMouseWheelListener(e -> {
      double factor = e.getWheelRotation() < 0 ? 0.9 : 1.1;
      vista.getCamera().setViewPercent(vista.getCamera().getViewPercent() * factor);
  });
  ```
* **Explicación:** Se registra un escuchador de eventos de rueda del ratón (`java.awt.event.MouseWheelListener`) sobre el `ViewPanel` para ajustar dinámicamente el porcentaje de acercamiento de la cámara virtual (`Camera.setViewPercent`), permitiendo hacer zoom fluido sobre el mapa de estaciones.
* **Fundamento oficial (Oracle - MouseWheelListener):** https://docs.oracle.com/javase/tutorial/uiswing/events/mousewheellistener.html

### `// *35` — Expresiones regulares (`String.replaceAll`) y bloques de texto multilínea (`"""`)
* **Código de referencia:**
  ```java
  String normalizada = linea.toLowerCase().replaceAll("[^a-z0-9]", "");
  ```
* **Explicación:** `replaceAll()` sustituye todas las subcadenas que coinciden con una expresión regular (`[^a-z0-9]`, cualquier carácter no alfanumérico) para normalizar los nombres de línea a clases CSS válidas. Asimismo, la hoja de estilo utiliza **Text Blocks (`"""`)** (estandarizados en Java 15) para declarar cadenas multilínea limpias sin concatenaciones.
* **Fundamento oficial (Oracle - String.replaceAll):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html#replaceAll(java.lang.String,java.lang.String)
* **Fundamento oficial (Oracle - Text Blocks):** https://docs.oracle.com/javase/tutorial/java/data/textblocks.html

---

## 8. ProyectoEstrucDatos.java

### `// *36` — Paquete oficial de componentes gráficos `javax.swing.SwingUtilities`
* **Código de referencia:**
  ```java
  import javax.swing.SwingUtilities;
  ```
* **Explicación:** Importación de la clase utilitaria `SwingUtilities` del framework gráfico **Java Swing**, encargada de gestionar operaciones seguras entre hilos en interfaces de usuario de escritorio.
* **Fundamento oficial (Oracle - javax.swing.SwingUtilities):** https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/javax/swing/SwingUtilities.html

### `// *37` — Despacho de la interfaz gráfica en el Event Dispatch Thread (EDT) con `invokeLater()`
* **Código de referencia:**
  ```java
  SwingUtilities.invokeLater(() -> {
      new VentanaPrincipal().setVisible(true);
  });
  ```
* **Explicación:** Los componentes de Swing no son seguros para múltiples hilos (*not thread-safe*). `SwingUtilities.invokeLater()` encola la creación y visualización de la ventana principal dentro del **Event Dispatch Thread (EDT)**, el hilo único dedicado al procesamiento de eventos y renderizado gráfico en Swing.
* **Fundamento oficial (Oracle - Concurrencia en Swing / Initial Threads):** https://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html

### `// *38` — Visualización del marco principal en pantalla mediante `Window.setVisible(true)`
* **Código de referencia:**
  ```java
  new VentanaPrincipal().setVisible(true);
  ```
* **Explicación:** Método heredado de `java.awt.Window` que inicializa el recurso nativo del sistema operativo (*peer*), valida la jerarquía de componentes contenidos y hace visible la ventana en pantalla.
* **Fundamento oficial (Oracle - Window.setVisible):** https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/java/awt/Window.html#setVisible(boolean)

---

## 9. TablaHash.java

### `// *39` — Sobrecarga de constructores e invocación encadenada con `this()`
* **Código de referencia:**
  ```java
  public TablaHash() {
      this(101);
  }
  ```
* **Explicación:** En Java se permite definir múltiples constructores con distintas listas de parámetros (sobrecarga). El constructor por defecto delega la inicialización al constructor parametrizado mediante la sentencia `this(101)`, evitando duplicación de código y fijando `101` (número primo) como capacidad inicial.
* **Fundamento oficial (Oracle - Uso de la palabra clave this con constructores):** https://docs.oracle.com/javase/tutorial/java/javaOO/thiskey.html

### `// *40` — Anotación `@SuppressWarnings("unchecked")` y creación de arreglos genéricos
* **Código de referencia:**
  ```java
  @SuppressWarnings("unchecked")
  private ListaEnlazada<EntradaHash>[] crearArreglo(int n) {
      ListaEnlazada<EntradaHash>[] arreglo = new ListaEnlazada[n];
  ```
* **Explicación:** Debido al borrado de tipos (*Type Erasure*) en Java, el compilador prohíbe instanciar arreglos con tipos genéricos parametrizados directamente (`new ListaEnlazada<EntradaHash>[n]`). Se instancia un arreglo de tipo crudo (`new ListaEnlazada[n]`) y se aplica `@SuppressWarnings("unchecked")` para suprimir la advertencia de conversión no verificada del compilador.
* **Fundamento oficial (Oracle - Restricciones en Genéricos / Creación de arreglos):** https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#createArrays

### `// *41` — Promoción de tipos primitivos (`long`) y conversión de estrechamiento (`(int)`)
* **Código de referencia:**
  ```java
  long hash = 7;
  for (int i = 0; i < clave.length(); i++) {
      hash = (31 * hash + Character.toLowerCase(clave.charAt(i))) % modulo;
  }
  return (int) hash;
  ```
* **Explicación:** Se emplea una variable acumuladora de 64 bits (`long`) para calcular el polinomio de dispersión y evitar desbordamientos aritméticos intermedios antes de aplicar el operador módulo (`%`). Posteriormente, se realiza una conversión explícita de estrechamiento (*Narrowing Primitive Conversion*) mediante `(int) hash` para obtener un índice válido de 32 bits.
* **Fundamento oficial (Oracle - Tipos de datos primitivos y Casting):** https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html

---

## 10. VentanaPrincipal.java

### `// *42` — Ventana principal de escritorio con `javax.swing.JFrame` y Look and Feel Nimbus
* **Código de referencia:**
  ```java
  public class VentanaPrincipal extends JFrame {
  ...
  UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
  ```
* **Explicación:** `JFrame` es el contenedor de nivel superior de una aplicación de escritorio Swing (con barra de título, bordes y botones de control). En su constructor se aplica el tema visual **Nimbus** mediante `UIManager.setLookAndFeel()`, se centra la ventana con `setLocationRelativeTo(null)` y se configura `DO_NOTHING_ON_CLOSE` para interceptar el cierre de forma controlada.
* **Fundamento oficial (Oracle - Creación de ventanas con JFrame):** https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
* **Fundamento oficial (Oracle - Look and Feel Nimbus):** https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/nimbus.html

### `// *43` — Manejo de eventos de cierre de ventana con clase anónima y `WindowAdapter`
* **Código de referencia:**
  ```java
  addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
          salir();
      }
  });
  ```
* **Explicación:** Para interceptar la acción de cierre de la ventana y advertir si existen cambios sin guardar, se registra un escuchador mediante una clase interna anónima que extiende de `WindowAdapter`, permitiendo sobrescribir únicamente `windowClosing()` sin implementar todos los métodos de la interfaz `WindowListener`.
* **Fundamento oficial (Oracle - Clases Anónimas):** https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html
* **Fundamento oficial (Oracle - Escritura de WindowListeners):** https://docs.oracle.com/javase/tutorial/uiswing/events/windowlistener.html

### `// *44` — Gestores de distribución `BoxLayout`, `GridLayout` y divisor `JSplitPane`
* **Código de referencia:**
  ```java
  lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
  ...
  JSplitPane centro = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelGrafo, scrollResultados);
  ```
* **Explicación:** Combina administradores de diseño para estructurar la interfaz: `BoxLayout` (`Y_AXIS`) para apilar verticalmente los grupos de controles laterales, `GridLayout` para distribuir botones uniformemente dentro de cada sección y `JSplitPane` (`VERTICAL_SPLIT`) para dividir el área central en dos paneles redimensionables por el usuario.
* **Fundamento oficial (Oracle - Uso de BoxLayout):** https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html
* **Fundamento oficial (Oracle - Uso de JSplitPane):** https://docs.oracle.com/javase/tutorial/uiswing/components/splitpane.html

### `// *45` — Cuadros de diálogo modales con `javax.swing.JOptionPane`
* **Código de referencia:**
  ```java
  JOptionPane.showMessageDialog(this, texto, "UrbanRoute", tipo);
  ```
* **Explicación:** `JOptionPane` facilita la creación de ventanas emergentes modales estándar: `showMessageDialog()` para alertas informativas, `showOptionDialog()` para botones personalizados, `showConfirmDialog()` para confirmaciones o incrustación de formularios completos, y `showInputDialog()` para listas desplegables rápidas.
* **Fundamento oficial (Oracle - Creación de diálogos con JOptionPane):** https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html

### `// *46` — Selector nativo de archivos con `JFileChooser` y `FileNameExtensionFilter`
* **Código de referencia:**
  ```java
  JFileChooser selector = new JFileChooser(new File("datos").isDirectory() ? new File("datos") : new File("."));
  selector.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
  ```
* **Explicación:** `javax.swing.JFileChooser` despliega el explorador de archivos del sistema tanto en modo apertura (`showOpenDialog` con selección múltiple) como en modo guardado (`showSaveDialog`), filtrando los archivos visibles mediante `FileNameExtensionFilter` para restringir la selección a documentos `.csv`.
* **Fundamento oficial (Oracle - Uso de JFileChooser):** https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html

### `// *47` — Formateo tabular de cadenas mediante `String.format()`
* **Código de referencia:**
  ```java
  sb.append(String.format("%2d. %-26s %d conexiones%n", i + 1,
          ranking[i].getEstacion().getNombre(), ranking[i].getGrado()));
  ```
* **Explicación:** El método estático `String.format()` aplica especificadores de formato (`%2d` entero alineado a la derecha en 2 posiciones, `%-26s` cadena alineada a la izquierda con ancho fijo de 26 caracteres y `%n` salto de línea portable) para alinear columnas en el área de resultados monoespaciada.
* **Fundamento oficial (Oracle - Formateo de salida numérica y cadenas):** https://docs.oracle.com/javase/tutorial/java/data/numberformat.html

### `// *48` — Liberación de recursos gráficos con `Window.dispose()` y cierre de la JVM con `System.exit(0)`
* **Código de referencia:**
  ```java
  panelGrafo.cerrar();
  dispose();
  System.exit(0);
  ```
* **Explicación:** Al confirmar la salida de la aplicación, se detiene el visor de GraphStream, se liberan los recursos nativos de la ventana Swing mediante `dispose()` y se finaliza explícitamente el proceso de la Máquina Virtual de Java con estado `0` (terminación normal) mediante `System.exit(0)`.
* **Fundamento oficial (Oracle - Window.dispose):** https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/java/awt/Window.html#dispose()
* **Fundamento oficial (Oracle - System.exit):** https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.html#exit(int)

---

## 11. Clases de Soporte y Archivo de Datos

Las siguientes clases y recursos del repositorio implementan exclusivamente Programación Orientada a Objetos básica (encapsulamiento, constructores, *getters* y *setters*) o estructuras de datos vistas en clase, reutilizando en algunos casos sintaxis elemental de Java ya fundamentada en los puntos anteriores (`final`, genéricos `<T>`, operador diamante `<>` y `equalsIgnoreCase()`):

* **`Arista.java`:** Modela una arista ponderada (estación destino y tiempo en minutos) mediante encapsulamiento en POO básica.
* **`EntradaHash.java`:** Modela un par clave-valor (`String`, `Estacion`) para la tabla de dispersión con encadenamiento separado.
* **`Estacion.java` (versión POO básica):** Entidad de dominio con atributos privados, constructor, *getters* y *setters*.
* **`Nodo.java`:** Nodo genérico (`Nodo<T>`) con referencia al siguiente elemento para listas simplemente enlazadas.
* **`ListaEnlazada.java` (versión básica):** Implementación clásica de lista simplemente enlazada vista en clase.
* **`Grafo.java` (versión básica):** Implementación clásica de grafo no dirigido mediante listas de adyacencia vista en clase.
* **`ResultadoRuta.java`:** Objeto de transferencia de datos (DTO) inmutable con el camino resultante (`ListaEnlazada<Estacion>`) y el tiempo total.
* **`Vertice.java`:** Representa un vértice del grafo junto con su lista de adyacencia (`ListaEnlazada<Arista>`) y operaciones sobre aristas.
* **`RedTransporte.java`:** Fachada de dominio que coordina las operaciones entre `Grafo` y `TablaHash` usando lógica estándar vista en clase.
* **`ResultadoCarga.java`:** Contenedor de estadísticas y advertencias generadas durante la lectura de archivos CSV.
* **`datos/metro_caracas.csv`:** Archivo de datos planos en formato CSV (Estándar IETF RFC 4180: https://www.rfc-editor.org/rfc/rfc4180) codificado en UTF-8 que almacena el catálogo inicial de estaciones y conexiones de la red.
