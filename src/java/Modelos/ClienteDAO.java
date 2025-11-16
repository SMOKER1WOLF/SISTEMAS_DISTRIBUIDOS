package Modelos;

import Entidades.Cliente;
import Herramientas.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    
    public ClienteDAO() {
        // No establecer conexión en el constructor
    }
    
    private Connection obtenerConexion() throws SQLException {
        if (con == null || con.isClosed()) {
            Conexion conect = new Conexion();
            con = conect.establecerConexion();
        }
        return con;
    }
    
    public void cerrarConexion() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null && !con.isClosed()) {
                Herramientas.Conexion.disconnect();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
    
//    public void cerrarRecursos() {
//        try {
//            if (rs != null) rs.close();
//            if (ps != null) ps.close();
//        } catch (SQLException e) {
//            System.out.println("Error al cerrar recursos: " + e.getMessage());
//        }
//    }
    
    // Listar todos los clientes
    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> listaCliente = new ArrayList<>();
        try {
            String sql = "SELECT * FROM cliente";
            ps = obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setIdCliente(rs.getString("idCliente"));
                cli.setApellidos(rs.getString("apellidos"));
                cli.setNombres(rs.getString("nombres"));
                cli.setDireccion(rs.getString("direccion"));
                cli.setDNI(rs.getString("DNI"));
                cli.setTelefono(rs.getString("telefono"));
                cli.setMovil(rs.getString("movil"));
                listaCliente.add(cli);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
        return listaCliente;
    }
    
    // Consultar cliente por ID
    public Cliente consultarCliente(String idCliente) throws SQLException {
        Cliente cliente = null;
        try {
            String sql = "SELECT * FROM cliente WHERE idCliente = ?";
            ps = obtenerConexion().prepareStatement(sql);
            ps.setString(1, idCliente);
            rs = ps.executeQuery();

            if (rs.next()) {
                cliente = new Cliente();
                cliente.setIdCliente(rs.getString("idCliente"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setNombres(rs.getString("nombres"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setDNI(rs.getString("DNI"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setMovil(rs.getString("movil"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
        return cliente;
    }
    
    // Insertar nuevo cliente
    public boolean insertarCliente(Cliente cliente) throws SQLException {
        try {
            String sql = "INSERT INTO cliente (idCliente, apellidos, nombres, direccion, DNI, telefono, movil) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = obtenerConexion().prepareStatement(sql);
            ps.setString(1, cliente.getIdCliente());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getNombres());
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getDNI());
            ps.setString(6, cliente.getTelefono());
            ps.setString(7, cliente.getMovil());
            
            return ps.executeUpdate() > 0;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }
    
    // Actualizar cliente
    public boolean actualizarCliente(Cliente cliente) throws SQLException {
        try {
            String sql = "UPDATE cliente SET apellidos=?, nombres=?, direccion=?, DNI=?, telefono=?, movil=? WHERE idCliente=?";
            ps = obtenerConexion().prepareStatement(sql);
            ps.setString(1, cliente.getApellidos());
            ps.setString(2, cliente.getNombres());
            ps.setString(3, cliente.getDireccion());
            ps.setString(4, cliente.getDNI());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getMovil());
            ps.setString(7, cliente.getIdCliente());
            
            return ps.executeUpdate() > 0;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }
    
    // Eliminar cliente
    public boolean eliminarCliente(String idCliente) throws SQLException {
        try {
            String sql = "DELETE FROM cliente WHERE idCliente = ?";
            ps = obtenerConexion().prepareStatement(sql);
            ps.setString(1, idCliente);
            
            return ps.executeUpdate() > 0;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }
    
    // Generar nuevo ID automático
    public String generarNuevoId() throws SQLException {
        String nuevoId = "CLI001";
        try {
            String sql = "SELECT idCliente FROM cliente ORDER BY CAST(SUBSTRING(idCliente, 4) AS UNSIGNED) DESC LIMIT 1";
            ps = obtenerConexion().prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                String ultimoId = rs.getString("idCliente");
                if (ultimoId != null && ultimoId.startsWith("CLI")) {
                    int numero = Integer.parseInt(ultimoId.substring(3));
                    nuevoId = String.format("CLI%03d", numero + 1);
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
        return nuevoId;
    }
}