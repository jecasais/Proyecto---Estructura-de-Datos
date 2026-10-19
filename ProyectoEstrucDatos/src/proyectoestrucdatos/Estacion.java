/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoestrucdatos;

/**
 *
 * @author Elohym
 */

public class Estacion {
    private String id;
    private String nombre;
    private String linea;
    private String zona;
    private int capacidadPasajeros;
    
    public Estacion (String id, String nombre, String linea, String zona, int capacidadPasajeros){
        this.id = id;
        this.nombre = nombre;
        this.linea = linea;
        this.zona = zona;
        this.capacidadPasajeros = capacidadPasajeros;
    }
    
// Getters y Setters primitivos
    public String getId() { 
        return id; 
    }
    
    public void setId(String id) { 
        this.id = id; 
    }

    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getLinea() { 
        return linea; 
    }
    
    public void setLinea(String linea) { 
        this.linea = linea; 
    }

    public String getZona() { 
        return zona; 
    }
    
    public void setZona(String zona) { 
        this.zona = zona; 
    }

    public int getCapacidadPasajeros() { 
        return capacidadPasajeros; 
    }
    
    public void setCapacidadPasajeros(int capacidadPasajeros) { 
        this.capacidadPasajeros = capacidadPasajeros; 
    }

    public String getInformacion() {
        return nombre + " (" + linea + " - " + zona + ")";
    }
}