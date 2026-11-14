package proyectoestrucdatos;

/**
 * Par clave-valor almacenado en cada cadena de la {@link TablaHash}.
 *
 * @author Elohym Casais
 */
public class EntradaHash {
    private String clave;
    private Estacion valor;

    /**
     * Crea una entrada.
     *
     * @param clave clave de indexación
     * @param valor estación asociada
     */
    public EntradaHash(String clave, Estacion valor) {
        this.clave = clave;
        this.valor = valor;
    }

    /**
     * @return clave de la entrada
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave nueva clave
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * @return estación asociada
     */
    public Estacion getValor() {
        return valor;
    }

    /**
     * @param valor nueva estación asociada
     */
    public void setValor(Estacion valor) {
        this.valor = valor;
    }
}
