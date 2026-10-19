/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoestrucdatos;

/**
 *
 * @author Elohym
 */

public class EntradaHash {
    private String clave;
    private Estacion valor;
    
    public EntradaHash (String clave, Estacion valor){
        this.clave = clave;
        this.valor = valor;
    }
    
    public String getClave(){
        return clave;
    }
    
    public void setClave(String clave){
        this.clave = clave;
    }
    
    public Estacion getValor (){
        return valor;
    }
    
    public void setValor(Estacion valor){
        this.valor = valor;
    }
       
}
