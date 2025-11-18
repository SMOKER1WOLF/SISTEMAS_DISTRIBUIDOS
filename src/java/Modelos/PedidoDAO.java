package Modelos;

import Entidades.Cliente;
import Entidades.ItemPedido;
import Entidades.Pedido;
import Entidades.Producto;
import Herramientas.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private final Conexion cn = new Conexion();
    private Connection con;

    // ------------------------------------------------------------
    // LISTAR CLIENTES
    // ------------------------------------------------------------
    public List<Cliente> listarClientes() throws SQLException {
        ClienteDAO cdao = new ClienteDAO();

        return cdao.listarClientes();
    }

    // ------------------------------------------------------------
    // LISTAR PRODUCTOS
    // ------------------------------------------------------------
    public List<Producto> listarProductos() throws SQLException {
        ProductoDAO pdao = new ProductoDAO();
        return pdao.listar(); // reutilizamos tu DAO existente
    }

    // ------------------------------------------------------------
    // INSERTAR PEDIDO
    // ------------------------------------------------------------
    public boolean insertarPedido(Pedido p) {
        String sql = "INSERT INTO pedido(codPedido, idCliente, fechaPedido, montoTotal, estado) VALUES (?, ?, ?, ?, ?)";

        try {
            con = cn.establecerConexion();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, p.getIdCliente());
            ps.setString(2, p.getCodPedido());
            ps.setDate(3, new java.sql.Date(p.getFechaPedido().getTime()));
            ps.setDouble(4, p.getMontoTotal());
            ps.setString(5, p.getEstado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error registrando pedido: " + e.getMessage());
            return false;

        } finally {
            cerrarConexion();
        }
    }

    // ------------------------------------------------------------
    // TRAE EL ID DEL ULTIMO PEDIDO
    // ------------------------------------------------------------
    public String codPedido() {
        String codigo = "PED0001"; // valor por defecto si no hay registros
        String sql = "SELECT MAX(idPedido) FROM pedido";

        try {
            con = cn.establecerConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int ultimo = rs.getInt(1); // número de pedido
                int nuevo = ultimo + 1;    // siguiente correlativo

                // Formato: PED + número de 4 dígitos con ceros a la izquierda
                // A FUTURO SI SE DESEA AUMENTAR EL NUMERO TOTAL DE DIGITOS SE MODIFICA "%04d"
                // LA BD PUEDE GUARDAR COMO MAXIMO 10 CARACTERES INCLUYENDO "PED...."
                codigo = "PED" + String.format("%04d", nuevo);
            }

        } catch (Exception e) {
            System.out.println("Error en codPedido(): " + e.getMessage());
        }
        cerrarConexion();
        return codigo;
    }

    // ------------------------------------------------------------
    // REGISTRO DE ITEM DE PEDIDO
    // ------------------------------------------------------------
    public boolean insertaritemPedido(ItemPedido ip) {
        ProductoDAO pdao = new ProductoDAO();
        String sql = "INSERT INTO itempedido(codPedido, idProducto, cantidad, precioUnitario, igv, subtotal) VALUES (?, ?, ?, ?, ?,?)";

        try {
            con = cn.establecerConexion();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, codPedido());
            ps.setString(2, ip.getIdProducto());
            ps.setInt(3, ip.getCantidad());
            ps.setDouble(4, ip.getPrecioUnitario());
            ps.setDouble(5, ip.getIgv());
            ps.setDouble(6, ip.getSubtotal());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error registrando Item del pedido: " + e.getMessage());
            return false;

        } finally {
            cerrarConexion();
        }
    }

    // ------------------------------------------------------------
    // LISTAR PEDIDOS  (NUEVO)
    // ------------------------------------------------------------
    public List<Pedido> listarPedidos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido ORDER BY fechaPedido DESC, idPedido DESC";

        try {
            con = cn.establecerConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("idPedido"));
                p.setCodPedido("codPedido");
                p.setIdCliente(rs.getString("idCliente"));
                p.setFechaPedido(rs.getDate("fechaPedido"));
                p.setMontoTotal(rs.getDouble("montoTotal"));
                p.setEstado(rs.getString("estado"));

                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error listando pedidos: " + e.getMessage());
        } finally {
            cerrarConexion();
        }

        return lista;
    }

    // ------------------------------------------------------------
    // CERRAR CONEXIÓN
    // ------------------------------------------------------------
    private void cerrarConexion() {
        try {
            if (con != null) {
                Herramientas.Conexion.disconnect();
            }
        } catch (Exception e) {
            System.out.println("Error cerrando conexión: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // INSERTAR PEDIDO E ITEMPEDIDO
    // ------------------------------------------------------------
    public boolean insertarPedidoConItems(Pedido pedido, List<ItemPedido> items) {
        Connection con = null;
        try {
            con = cn.establecerConexion();
            con.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar el pedido principal
            String sqlPedido = "INSERT INTO pedido(codPedido, idCliente, fechaPedido, montoTotal, estado) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psPedido = con.prepareStatement(sqlPedido);

            psPedido.setString(1, pedido.getCodPedido());
            psPedido.setString(2, pedido.getIdCliente());
            psPedido.setDate(3, new java.sql.Date(pedido.getFechaPedido().getTime()));
            psPedido.setDouble(4, pedido.getMontoTotal());
            psPedido.setString(5, pedido.getEstado());

            int filasPedido = psPedido.executeUpdate();
            psPedido.close();

            if (filasPedido <= 0) {
                con.rollback();
                return false;
            }

            // 2. Insertar todos los items del pedido
            String sqlItem = "INSERT INTO itempedido(codPedido, idProducto, cantidad, precioVenta, igv, subtotal, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psItem = con.prepareStatement(sqlItem);

            for (ItemPedido item : items) {
                psItem.setString(1, item.getCodPedido());
                psItem.setString(2, item.getIdProducto());
                psItem.setInt(3, item.getCantidad());
                psItem.setDouble(4, item.getPrecioUnitario());
                psItem.setDouble(5, item.getIgv());
                psItem.setDouble(6, item.getSubtotal());
                psItem.setDouble(7, item.getTotal());
                psItem.addBatch();
            }

            int[] resultados = psItem.executeBatch();
            psItem.close();

            // Verificar que todos los items se insertaron correctamente
            for (int resultado : resultados) {
                if (resultado <= 0) {
                    con.rollback();
                    return false;
                }
            }

            // ✅ 3. ACTUALIZAR STOCK (versión simple)
            String sqlUpdateStock = "UPDATE producto SET cantidad = cantidad - ? WHERE idProducto = ?";
            PreparedStatement psUpdateStock = con.prepareStatement(sqlUpdateStock);

            for (ItemPedido item : items) {
                psUpdateStock.setInt(1, item.getCantidad());
                psUpdateStock.setString(2, item.getIdProducto());
                psUpdateStock.addBatch();
                System.out.println("🔄 Actualizando stock - Producto: " + item.getIdProducto() + ", Cantidad a restar: " + item.getCantidad());
            }

            int[] resultadosStock = psUpdateStock.executeBatch();
            psUpdateStock.close();

            for (int resultado : resultadosStock) {
                if (resultado <= 0) {
                    con.rollback();
                    return false;
                }
            }

            // Si todo salió bien, confirmar la transacción
            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Error en transacción de pedido: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    Herramientas.Conexion.disconnect();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ------------------------------------------------------------
    // ELIMINAR PEDIDO E ITEMPEDIDO + REINTEGRAR STOCK
    // ------------------------------------------------------------
    public boolean eliminarPedidoCompleto(String idPedido) {
    Connection con = null;
    try {
        con = cn.establecerConexion();
        con.setAutoCommit(false); // Iniciar transacción

        // 1. Primero obtener el código del pedido y los items para reintegrar stock
        String sqlObtenerPedido = "SELECT codPedido FROM pedido WHERE idPedido = ?";
        PreparedStatement psObtener = con.prepareStatement(sqlObtenerPedido);
        psObtener.setString(1, idPedido);
        
        ResultSet rs = psObtener.executeQuery();
        String codPedido = null;
        
        if (rs.next()) {
            codPedido = rs.getString("codPedido");
        } else {
            con.rollback();
            psObtener.close();
            System.out.println("❌ No se encontró el pedido con ID: " + idPedido);
            return false;
        }
        rs.close();
        psObtener.close();

        // 2. Obtener los items del pedido para reintegrar stock
        List<ItemPedido> items = new ArrayList<>();
        String sqlObtenerItems = "SELECT idProducto, cantidad FROM itempedido WHERE codPedido = ?";
        PreparedStatement psItems = con.prepareStatement(sqlObtenerItems);
        psItems.setString(1, codPedido);
        
        ResultSet rsItems = psItems.executeQuery();
        while (rsItems.next()) {
            ItemPedido item = new ItemPedido();
            item.setIdProducto(rsItems.getString("idProducto"));
            item.setCantidad(rsItems.getInt("cantidad"));
            items.add(item);
        }
        rsItems.close();
        psItems.close();

        // 3. REINTEGRAR STOCK a los productos
        String sqlUpdateStock = "UPDATE producto SET cantidad = cantidad + ? WHERE idProducto = ?";
        PreparedStatement psUpdateStock = con.prepareStatement(sqlUpdateStock);
        
        for (ItemPedido item : items) {
            psUpdateStock.setInt(1, item.getCantidad());
            psUpdateStock.setString(2, item.getIdProducto());
            psUpdateStock.addBatch();
            System.out.println("🔄 Reintegrando stock - Producto: " + item.getIdProducto() + ", Cantidad: " + item.getCantidad());
        }
        
        int[] resultadosStock = psUpdateStock.executeBatch();
        psUpdateStock.close();

        // Verificar actualización de stock
        for (int resultado : resultadosStock) {
            if (resultado <= 0) {
                System.out.println("⚠️ No se pudo reintegrar stock para algún producto");
            }
        }

        // 4. ELIMINAR ITEMS del pedido
        String sqlEliminarItems = "DELETE FROM itempedido WHERE codPedido = ?";
        PreparedStatement psEliminarItems = con.prepareStatement(sqlEliminarItems);
        psEliminarItems.setString(1, codPedido);
        
        int itemsEliminados = psEliminarItems.executeUpdate();
        psEliminarItems.close();
        
        System.out.println("🗑️ Items eliminados: " + itemsEliminados);

        // 5. FINALMENTE ELIMINAR EL PEDIDO
        String sqlEliminarPedido = "DELETE FROM pedido WHERE idPedido = ?";
        PreparedStatement psEliminarPedido = con.prepareStatement(sqlEliminarPedido);
        psEliminarPedido.setString(1, idPedido);
        
        int pedidosEliminados = psEliminarPedido.executeUpdate();
        psEliminarPedido.close();

        if (pedidosEliminados > 0) {
            con.commit(); // Confirmar transacción
            System.out.println("✅ Pedido eliminado exitosamente - ID: " + idPedido);
            System.out.println("✅ Stock reintegrado para " + items.size() + " productos");
            return true;
        } else {
            con.rollback(); // Revertir si no se pudo eliminar el pedido
            System.out.println("❌ No se pudo eliminar el pedido");
            return false;
        }

    } catch (SQLException e) {
        try {
            if (con != null) con.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("❌ Error al eliminar pedido completo: " + e.getMessage());
        e.printStackTrace();
        return false;
    } finally {
        try {
            if (con != null) {
                con.setAutoCommit(true);
                cerrarConexion();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    // ------------------------------------------------------------
    // CAMBIAR PEDIDO A ENTREGADO
    // ------------------------------------------------------------
    public boolean actualizarEstadoPedido(String idPedido, String nuevoEstado) {
        String sql = "UPDATE pedido SET estado = ? WHERE idPedido = ?";

        try {
            con = cn.establecerConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setString(2, idPedido);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("✅ Estado del pedido " + idPedido + " actualizado a: " + nuevoEstado);
                return true;
            } else {
                System.out.println("❌ No se encontró el pedido con ID: " + idPedido);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar estado del pedido: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarConexion();
        }
    }

    // ------------------------------------------------------------
    // TRAER ITEMS DE PEDIDO POR EL codPedido
    // ------------------------------------------------------------
    public List<ItemPedido> obtenerItemsPorPedido(String codPedido) {
    List<ItemPedido> items = new ArrayList<>();
    String sql = "SELECT ip.*, p.nombre " +
                 "FROM itempedido ip " +
                 "INNER JOIN producto p ON ip.idProducto = p.idProducto " +
                 "WHERE ip.codPedido = ?";
    
    try {
        con = cn.establecerConexion();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, codPedido);
        
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            ItemPedido item = new ItemPedido();
            item.setCodPedido(rs.getString("codPedido"));
            item.setIdProducto(rs.getString("idProducto"));
            item.setCantidad(rs.getInt("cantidad"));
            item.setPrecioUnitario(rs.getDouble("precioVenta"));
            item.setIgv(rs.getDouble("igv"));
            item.setSubtotal(rs.getDouble("subtotal"));
            item.setTotal(rs.getDouble("total"));
            item.setNombreProducto(rs.getString("nombre")); // De la tabla producto
            
            items.add(item);
            
            // Log para verificar
            System.out.println("📦 Item - ID Producto: " + item.getIdProducto() + 
                             ", Nombre: " + item.getNombreProducto() + 
                             ", Cantidad: " + item.getCantidad() +
                             ", Precio: " + item.getPrecioUnitario());
        }
        
        rs.close();
        ps.close();
        
        System.out.println("✅ Items encontrados para pedido " + codPedido + ": " + items.size());
        
    } catch (SQLException e) {
        System.out.println("❌ Error al obtener items del pedido: " + e.getMessage());
        e.printStackTrace();
    } finally {
        cerrarConexion();
    }
    
    return items;
}
    
    public Pedido obtenerPedidoPorId(String idPedido) {
    Pedido pedido = null;
    String sql = "SELECT * FROM pedido WHERE idPedido = ?";
    
    try {
        con = cn.establecerConexion();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, idPedido);
        
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            pedido = new Pedido();
            pedido.setIdPedido(rs.getInt("idPedido"));
            pedido.setCodPedido(rs.getString("codPedido"));
            pedido.setIdCliente(rs.getString("idCliente"));
            pedido.setFechaPedido(rs.getDate("fechaPedido"));
            pedido.setMontoTotal(rs.getDouble("montoTotal"));
            pedido.setEstado(rs.getString("estado"));
        }
        
        rs.close();
        ps.close();
        
    } catch (SQLException e) {
        System.out.println("❌ Error al obtener pedido: " + e.getMessage());
        e.printStackTrace();
    } finally {
        cerrarConexion();
    }
    
    return pedido;
}
}
