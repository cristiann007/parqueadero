
package modelo;



import java.sql.Timestamp;

public class Carro {
    private int idCarro;
    private String placa;
    private String marca;
    private String modelo;
    private String color;
    private Integer idEspacio; 
    private Timestamp fechaIngreso;

    public Carro() {
    }

    public Carro(int idCarro, String placa, String marca, String modelo, String color, Integer idEspacio, Timestamp fechaIngreso) {
        this.idCarro = idCarro;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.idEspacio = idEspacio;
        this.fechaIngreso = fechaIngreso;
    }

    public int getIdCarro() { return idCarro; }
    public void setIdCarro(int idCarro) { this.idCarro = idCarro; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Integer getIdEspacio() { return idEspacio; }
    public void setIdEspacio(Integer idEspacio) { this.idEspacio = idEspacio; }
    public Timestamp getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(Timestamp fechaIngreso) { this.fechaIngreso = fechaIngreso; }
}


