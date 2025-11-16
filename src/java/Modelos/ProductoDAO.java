package Modelos;

import Entidades.Producto;
import Herramientas.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductoDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    // 🔹 LISTAR PRODUCTOS
    public ArrayList<Producto> listar() throws SQLException {
        ArrayList<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto";

        try {
            con = cn.establecerConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdArticulo(rs.getString("idProducto"));
                p.setNombre(rs.getString("nombre"));
                p.setCantidad(rs.getString("cantidad"));
                p.setPrecio(rs.getString("precio"));
                lista.add(p);
            }

            System.out.println("✅ Productos listados correctamente. Total: " + lista.size());
        } catch (SQLException e) {
            System.out.println("❌ Error al listar productos: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return lista;
    }

    // 🔹 INSERTAR PRODUCTO
    public void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO producto (idProducto, nombre, cantidad, precio) VALUES (?, ?, ?, ?)";
        try {
            con = cn.establecerConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getIdArticulo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCantidad());
            ps.setString(4, p.getPrecio());
            ps.executeUpdate();

            System.out.println("✅ Producto insertado correctamente: " + p.getNombre());
        } catch (SQLException e) {
            System.out.println("❌ Error al insertar producto: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

// Insertar con ID autogenerado (PRO001, PRO002, ...)
    public String insertarConIdAuto(Producto p) throws SQLException {

        String nuevoId = null;

        String sqlId = "SELECT idProducto FROM producto "
                + "WHERE idProducto LIKE 'PRO%' "
                + "ORDER BY CAST(SUBSTRING(idProducto, 4) AS UNSIGNED) DESC LIMIT 1";

        String sqlInsert = "INSERT INTO producto (idProducto, nombre, cantidad, precio) VALUES (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement psId = null;
        PreparedStatement psInsert = null;
        ResultSet rsId = null;

        try {
            con = cn.establecerConexion();

            // Obtener último ID
            psId = con.prepareStatement(sqlId);
            rsId = psId.executeQuery();

            if (rsId.next()) {
                String ultimoId = rsId.getString("idProducto"); // PRO012
                int numero = Integer.parseInt(ultimoId.substring(3)); // 12
                nuevoId = String.format("PRO%03d", numero + 1);        // PRO013
            } else {
                nuevoId = "PRO001";
            }

            // Insertar registro con ID generado
            psInsert = con.prepareStatement(sqlInsert);
            psInsert.setString(1, nuevoId);
            psInsert.setString(2, p.getNombre());
            psInsert.setString(3, p.getCantidad());
            psInsert.setString(4, p.getPrecio());
            psInsert.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al insertar con ID autogenerado: " + e.getMessage());
            throw e; // IMPORTANTE: relanzar para ver el error real en el servlet
        } finally {
            if (rsId != null) {
                rsId.close();
            }
            if (psId != null) {
                psId.close();
            }
            if (psInsert != null) {
                psInsert.close();
            }
            if (con != null) {
                Herramientas.Conexion.disconnect();
            }
        }

        return nuevoId;
    }

    // 🔹 OBTENER PRODUCTO POR ID (para MODIFICAR)
    public Producto obtenerPorId(String idArticulo) throws SQLException {
        Producto p = null;
        String sql = "SELECT * FROM producto WHERE idProducto = ?";
        try {
            con = cn.establecerConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, idArticulo);
            rs = ps.executeQuery();

            if (rs.next()) {
                p = new Producto();
                p.setIdArticulo(rs.getString("idProducto"));
                p.setNombre(rs.getString("nombre"));
                p.setCantidad(rs.getString("cantidad"));
                p.setPrecio(rs.getString("precio"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener producto por ID: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return p;
    }

    // 🔹 ACTUALIZAR PRODUCTO (MODIFICAR)
    public void actualizar(Producto p) throws SQLException {
        String sql = "UPDATE producto SET nombre=?, cantidad=?, precio=? WHERE idProducto=?";
        try {
            con = cn.establecerConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCantidad());
            ps.setString(3, p.getPrecio());
            ps.setString(4, p.getIdArticulo());
            ps.executeUpdate();

            System.out.println("✅ Producto actualizado correctamente: " + p.getIdArticulo());
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar producto: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    // 🔹 ELIMINAR PRODUCTO
    public void eliminar(String idArticulo) throws SQLException {
        String sql = "DELETE FROM producto WHERE idProducto=?";
        try {
            con = cn.establecerConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, idArticulo);
            ps.executeUpdate();

            System.out.println("🗑️ Producto eliminado: " + idArticulo);
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar producto: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    // 🔍 NUEVO: BUSCAR PRODUCTOS POR NOMBRE O ID
    public ArrayList<Producto> buscar(String texto) throws SQLException {
        ArrayList<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE idProducto LIKE ? OR nombre LIKE ?";

        try {
            con = cn.establecerConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + texto + "%");
            ps.setString(2, "%" + texto + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdArticulo(rs.getString("idProducto"));
                p.setNombre(rs.getString("nombre"));
                p.setCantidad(rs.getString("cantidad"));
                p.setPrecio(rs.getString("precio"));
                lista.add(p);
            }

            System.out.println("🔎 Resultados encontrados: " + lista.size());
        } catch (SQLException e) {
            System.out.println("❌ Error al buscar productos: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return lista;
    }
}
