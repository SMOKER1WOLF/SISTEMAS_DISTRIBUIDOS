package Entidades;

import java.util.Date;

public class Pedido {

    private int idPedido;
    private String codPedido;
    private String idCliente;
    private Date fechaPedido;
    private double montoTotal;
    private String estado;
    private String nombreProducto;

    public Pedido() {
    }

    public Pedido(int idPedido, String codPedido, String idCliente, Date fechaPedido, double montoTotal, String estado) {
        this.idPedido = idPedido;
        this.codPedido = codPedido;
        this.idCliente = idCliente;
        this.fechaPedido = fechaPedido;
        this.montoTotal = montoTotal;
        this.estado = estado;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }
    
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
