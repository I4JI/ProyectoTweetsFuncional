package model;

/**
 *
 * @author jose
 */
public class Tweet {
    private String id;
    private String entidad;
    private String sentimiento;
    private String texto;

    public Tweet(String id, String entidad, String sentimiento, String texto) {
        this.id = id;
        this.entidad = entidad;
        this.sentimiento = sentimiento;
        this.texto = texto;
    }

    public String getId() {
        return id;
    }

    public String getEntidad() {
        return entidad;
    }

    public String getSentimiento() {
        return sentimiento;
    }

    public String getTexto() {
        return texto;
    }    

    @Override
    public String toString() {
        return "Tweet{" + "id=" + id + ", entidad=" + entidad + ", sentimiento=" + sentimiento + ", texto=" + texto + '}';
    }
}
