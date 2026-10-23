# Proyecto---Estructura-de-Datos

---

## Descripción del Proyecto
UrbanRoute es una herramienta de software desarrollada en Java que permite modelar, analizar y simular la red de transporte público urbano como un grafo ponderado[cite: 1]. El sistema facilita la búsqueda de rutas de tiempo mínimo, la identificación de estaciones clave (*hubs*) y la detección de zonas aisladas ante interrupciones en el servicio, apoyándose en un TDA Hash Table para la gestión e indexación eficiente de datos en $O(1)

---

## Integrantes del Equipo
* **Elohym Casais**
* **Maria Andreina Piñero**
* **Alex Sistiaga**

---

## Arquitectura e Implementación

El proyecto fue diseñado bajo el principio de responsabilidad única (evitando clases "Dios") y cumpliendo la restricción técnica estricta de no utilizar estructuras preconstruidas de `java.util`

### Módulo de Datos Base
* **`Estacion`**: Clase de dominio que encapsula la información técnica de cada punto de transporte (ID, nombre, línea, zona y capacidad de pasajeros/hora)
* **`Nodo<T>`**: Componente elemental genérico utilizado para construir listas enlazadas
* **`ListaEnlazada<T>`**: TDA de lista enlazada dinámica desarrollada completamente desde cero para reemplazar `java.util.LinkedList` Soporta inserción, eliminación, obtención por índice y recorridos secuenciales

### Módulo de Indexación Hash
* **`EntradaHash`**: Clase contenedora de par clave-valor para el manejo interno de la tabla
* **`TablaHash`**: TDA de Tabla de Dispersión propio con manejo de colisiones por **encadenamiento separado** (`ListaEnlazada[]`) Permite insertar, eliminar y consultar estaciones en tiempo promedio $O(1)$ utilizando una función hash polinomial
