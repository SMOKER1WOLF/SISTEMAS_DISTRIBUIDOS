/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author USUARIO
 */
public class Cliente {
    
    private String idCliente;
    private String apellidos;
    private String nombres;
    private String direccion;
    private String DNI;
    private String telefono;
    private String movil;

    public Cliente() {
    }

    public Cliente(String idCliente, String apellidos, String nombres, String direccion, String DNI, String telefono, String movil) {
        this.idCliente = idCliente;
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.direccion = direccion;
        this.DNI = DNI;
        this.telefono = telefono;
        this.movil = movil;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }
    
    
    
}
