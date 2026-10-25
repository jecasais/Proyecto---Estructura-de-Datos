/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoestrucdatos;

/**
 *
 * @author Elohym Casais
 * Representa una conexion ponderada entre dos estaciones en la red de transporte
 */

public class Arista {
    private Estacion destino;
    private int tiempoMinutos;
    
    public Arista (Estacion destino, int tiempoMinutos){
        this.destino = destino;
        this.tiempoMinutos = tiempoMinutos;
    }
    
    public Estacion getDestino() {
        return destino;
    }

    public void setDestino(Estacion destino) {
        this.destino = destino;
    }

    public int getTiempoMinutos() {
        return tiempoMinutos;
    }

    public void setTiempoMinutos(int tiempoMinutos) {
        this.tiempoMinutos = tiempoMinutos;
    }
}
