package modelo;

public class Parqueadero {
    private int id;
    private String nombre;
    private String direccion;
    private int capacidad;
    private double tarifaPorHora;

    public Parqueadero() {}

    public Parqueadero(int id, String nombre, String direccion, int capacidad, double tarifaPorHora) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidad = capacidad;
        this.tarifaPorHora = tarifaPorHora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; } 
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDireccion() { return direccion; } 
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    
    public double getTarifaPorHora() { return tarifaPorHora; } 
    public void setTarifaPorHora(double tarifaPorHora) { this.tarifaPorHora = tarifaPorHora; }
}