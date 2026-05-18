package modelo; 

public class Espacio {
    private int idEspacio;
    private int idParqueadero;
    private String numeroEspacio;
    private boolean estado; 

    public Espacio() {
    }

    public Espacio(int idEspacio, int idParqueadero, String numeroEspacio, boolean estado) {
        this.idEspacio = idEspacio;
        this.idParqueadero = idParqueadero;
        this.numeroEspacio = numeroEspacio;
        this.estado = estado;
    }

    public int getIdEspacio() {
        return idEspacio;
    }

    public void setIdEspacio(int idEspacio) {
        this.idEspacio = idEspacio;
    }

    public int getIdParqueadero() {
        return idParqueadero;
    }

    public void setIdParqueadero(int idParqueadero) {
        this.idParqueadero = idParqueadero;
    }

    public String getNumeroEspacio() {
        return numeroEspacio;
    }

    public void setNumeroEspacio(String numeroEspacio) {
        this.numeroEspacio = numeroEspacio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}

