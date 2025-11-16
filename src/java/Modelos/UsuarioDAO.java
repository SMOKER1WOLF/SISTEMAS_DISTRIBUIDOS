package Modelos;

import Entidades.Usuario;
import Herramientas.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    
    public UsuarioDAO() {
        Conexion conect = new Conexion();
        this.con = conect.establecerConexion();
    }
    
    public UsuarioDAO(Connection con) {
        this.con = con;
    }
    
    public void cerrarConexion() {
        try {
            if (con != null && !con.isClosed()) {
                Herramientas.Conexion.disconnect();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
    
    public Usuario buscarUsuarioPorUsername(String username) throws SQLException {
        Usuario user = null;
        try {
            String sql = "SELECT d.*, c.Estados, c.Num_Ingresos, c.Permisos FROM datosusuarios d "
                    + "JOIN catusuario c ON d.codUsuario = c.codUsuario "
                    + "WHERE d.usuario = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new Usuario();
                user.setCodUsuario(rs.getString("codUsuario"));
                user.setUsuario(rs.getString("usuario"));
                user.setPassword(rs.getString("password"));
                user.setNombres(rs.getString("nombres"));
                user.setApellidos(rs.getString("apellidos"));
                user.setEmail(rs.getString("email"));
                user.setEstado(rs.getString("Estados"));
                user.setPermisos(rs.getString("Permisos"));
                user.setNumIngresos(rs.getInt("Num_Ingresos"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
        return user;
    }
    
    public void bloquearUsuario(String codUsuario) throws SQLException {
        try {
            String sql = "UPDATE catusuario SET Estados = 'Bloqueado' WHERE codUsuario = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, codUsuario);
            ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
        }
    }
    
    public void actualizarLoginExitoso(String codUsuario) throws SQLException {
        try {
            // Actualiza EnLinea y NumIngresos
            String sql = "UPDATE catusuario SET EnLinea = 1, Num_Ingresos = Num_Ingresos + 1 WHERE codUsuario = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, codUsuario);
            ps.executeUpdate();
            
            // Actualiza la hora y fecha de último acceso
            String sql2 = "UPDATE altusuario SET Fec_UltimoAcceso = ?, Hor_UltimoAcceso = ? WHERE codUsuario = ?";
            ps = con.prepareStatement(sql2);
            ps.setString(1, Herramientas.Persistencias.getFecha());
            ps.setString(2, Herramientas.Persistencias.getHora());
            ps.setString(3, codUsuario);
            ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
        }
    }
    
    public void actualizarLogout(String codUsuario) throws SQLException {
        try {
            String sql = "UPDATE catusuario SET EnLinea = 0 WHERE codUsuario = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, codUsuario);
            ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
        }
    }
}