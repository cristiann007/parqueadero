package modelo;

import java.sql.Timestamp;

public class Movimiento {
    private int idMovimiento;
    private int idCarro;
    private int idEspacio;
    private Timestamp fechaIngreso;
    private Timestamp fechaSalida; 
    private double totalPagar;     
    private String estado;         

    public Movimiento() {
    }

    public Movimiento(int idMovimiento, int idCarro, int idEspacio, Timestamp fechaIngreso, Timestamp fechaSalida, double totalPagar, String estado) {
        this.idMovimiento = idMovimiento;
        this.idCarro = idCarro;
        this.idEspacio = idEspacio;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.totalPagar = totalPagar;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public int getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(int idCarro) {
        this.idCarro = idCarro;
    }

    public int getIdEspacio() {
        return idEspacio;
    }

    public void setIdEspacio(int idEspacio) {
        this.idEspacio = idEspacio;
    }

    public Timestamp getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Timestamp fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Timestamp getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Timestamp fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
