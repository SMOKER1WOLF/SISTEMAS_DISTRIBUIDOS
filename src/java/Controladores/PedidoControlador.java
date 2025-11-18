package Controladores;

import Entidades.Cliente;
import Entidades.ItemPedido;
import Entidades.Pedido;
import Entidades.Producto;
import Modelos.PedidoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "PedidoControlador", urlPatterns = {"/PedidoControlador"})
public class PedidoControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String op = request.getParameter("Op");

        if (op == null) {
            op = "MostrarFormulario";
        }

        switch (op) {
            case "MostrarFormulario":
                mostrarFormulario(request, response);
                break;

            case "MarcarEntregado":
                marcarPedidoEntregado(request, response);
                break;

            case "Registrar":
                registrarPedido(request, response);
                break;

            case "Listar":  // <-- NUEVA OPCIÓN
                listarPedidos(request, response);
                break;

            case "Ver":  // Nuevo caso para ver pedido
                verPedido(request, response);
                break;

            case "Eliminar":  // Nuevo caso para ver pedido
                eliminarPedido(request, response);
                break;

            default:
                mostrarFormulario(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // -----------------------------------------------------------
// MOSTRAR FORMULARIO DE REGISTRO
// -----------------------------------------------------------
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            PedidoDAO dao = new PedidoDAO();

            List<Cliente> listaClientes = dao.listarClientes();
            List<Producto> listaProductos = dao.listarProductos();

            request.setAttribute("listaClientes", listaClientes);
            request.setAttribute("listaProductos", listaProductos);

            request.setAttribute("fechaVisible", Herramientas.Persistencias.getFecha());

//        int codigoPedido = Herramientas.GeneradorCode.generarCodigoPedido();
            String codigoPedido = dao.codPedido();
            request.setAttribute("codigoPedido", codigoPedido);

            request.getRequestDispatcher("/Vistas/registroPedido.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar el formulario: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

// -----------------------------------------------------------
// REGISTRAR PEDIDO
// -----------------------------------------------------------
    private void registrarPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idCliente = request.getParameter("idCliente");

            // validar monto
            String montoTotalStr = request.getParameter("montoTotal");
            double montoTotal = 0;

            if (montoTotalStr != null && !montoTotalStr.isEmpty()) {
                montoTotal = Double.parseDouble(montoTotalStr);
            }

            String fechaStr = request.getParameter("fechaPedido");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = sdf.parse(fechaStr);

            PedidoDAO dao = new PedidoDAO();

            Pedido p = new Pedido();
            p.setIdCliente(idCliente);
            String codPedido = dao.codPedido();
            p.setCodPedido(codPedido);
            p.setFechaPedido(fecha);
            p.setMontoTotal(montoTotal);
            p.setEstado("Pendiente");

            // Obtener los items del pedido desde el request
            List<ItemPedido> items = obtenerItemsPedido(request, codPedido);

            if (items.isEmpty()) {
                request.setAttribute("error", "El pedido debe tener al menos un producto.");
                mostrarFormulario(request, response);
                return;
            }

            // Insertar pedido con items (transacción manejada en el DAO)
            if (dao.insertarPedidoConItems(p, items)) {
                request.setAttribute("mensaje", "Pedido registrado correctamente.");
            } else {
                request.setAttribute("error", "No se pudo registrar el pedido.");
            }

            // logs
            System.out.println("idCliente = " + idCliente);
            System.out.println("montoTotal = " + montoTotalStr);
            System.out.println("fechaPedido = " + fechaStr);
            System.out.println("Número de items: " + items.size());

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al registrar pedido: " + e.getMessage());

            // logs completos
            System.out.println("ERROR EN registrarPedido:");
            System.out.println("idCliente = " + request.getParameter("idCliente"));
            System.out.println("montoTotal = " + request.getParameter("montoTotal"));
            System.out.println("fechaPedido = " + request.getParameter("fechaPedido"));
        }

        // siempre volver a cargar formulario
        mostrarFormulario(request, response);
    }

// Método para extraer los items del request
    private List<ItemPedido> obtenerItemsPedido(HttpServletRequest request, String codPedido) {
        List<ItemPedido> items = new ArrayList<>();

        try {
            // Obtener los arrays de parámetros del formulario
            String[] idArticulos = request.getParameterValues("idArticulo");
            String[] cantidades = request.getParameterValues("cantidad");
            String[] precios = request.getParameterValues("precio");

            // DEBUG: Ver todos los parámetros recibidos
            System.out.println("=== DEBUG PARÁMETROS RECIBIDOS ===");
            java.util.Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                System.out.println(paramName + ": " + java.util.Arrays.toString(paramValues));
            }
            System.out.println("===================================");

            if (idArticulos == null || cantidades == null || precios == null) {
                System.out.println("No se encontraron items en el pedido");
                return items;
            }

            System.out.println("Número de items encontrados: " + idArticulos.length);

            // Recorrer todos los items y crear objetos ItemPedido
            for (int i = 0; i < idArticulos.length; i++) {
                String idProducto = idArticulos[i];
                String cantidadStr = cantidades[i];
                String precioStr = precios[i];

                // ✅ VALIDAR que los valores no estén vacíos
                if (idProducto == null || idProducto.trim().isEmpty()
                        || cantidadStr == null || cantidadStr.trim().isEmpty()
                        || precioStr == null || precioStr.trim().isEmpty()) {
                    System.out.println("Item " + (i + 1) + " omitido - valores vacíos");
                    continue; // Saltar este item
                }

                try {
                    int cantidad = Integer.parseInt(cantidadStr);
                    double precioUnitario = Double.parseDouble(precioStr);

                    // Validar valores numéricos
                    if (cantidad <= 0 || precioUnitario <= 0) {
                        System.out.println("Item " + (i + 1) + " omitido - valores inválidos: cantidad=" + cantidad + ", precio=" + precioUnitario);
                        continue;
                    }

                    double subtotal = precioUnitario * cantidad;
                    double igv = subtotal * 0.18; // Asumiendo 18% de IGV
                    double total = subtotal + igv;

                    ItemPedido item = new ItemPedido();
                    item.setCodPedido(codPedido);
                    item.setIdProducto(idProducto);
                    item.setCantidad(cantidad);
                    item.setPrecioUnitario(precioUnitario);
                    item.setIgv(igv);
                    item.setSubtotal(subtotal);
                    item.setTotal(total);

                    items.add(item);

                    System.out.println("Item " + (i + 1) + " agregado: " + idProducto + " - Cant: " + cantidad + " - Precio: " + precioUnitario);

                } catch (NumberFormatException e) {
                    System.out.println("Error parseando números en item " + (i + 1) + ": id=" + idProducto + ", cantidad=" + cantidadStr + ", precio=" + precioStr);
                    continue; // Saltar este item
                }
            }

            System.out.println("Total de items válidos: " + items.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

// -----------------------------------------------------------
// LISTAR PEDIDOS
// -----------------------------------------------------------
    private void listarPedidos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            PedidoDAO dao = new PedidoDAO();
            List<Pedido> lista = dao.listarPedidos();

            request.setAttribute("listaPedidos", lista);
            request.getRequestDispatcher("/Vistas/listarPedidos.jsp").forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();
            request.setAttribute("error", "Error al listar pedidos: " + e.getMessage());
            request.getRequestDispatcher("/Vistas/error.jsp").forward(request, response);
        }
    }

// -----------------------------------------------------------
// CAMBIAR ESTADO A ENTREGADO
// -----------------------------------------------------------
    private void marcarPedidoEntregado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idPedido = request.getParameter("id");

            if (idPedido == null || idPedido.trim().isEmpty()) {
                request.setAttribute("error", "ID de pedido no válido");
                listarPedidos(request, response);
                return;
            }

            PedidoDAO dao = new PedidoDAO();
            String nuevoEstado = "Entregado";

            if (dao.actualizarEstadoPedido(idPedido, nuevoEstado)) {
                request.setAttribute("mensaje", "Pedido marcado como entregado correctamente");
            } else {
                request.setAttribute("error", "No se pudo actualizar el estado del pedido");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al marcar pedido como entregado: " + e.getMessage());
        }

        listarPedidos(request, response);
    }

// -----------------------------------------------------------
// VER PEDIDO
// -----------------------------------------------------------
    private void verPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idPedido = request.getParameter("id");

            if (idPedido == null || idPedido.trim().isEmpty()) {
                request.setAttribute("error", "ID de pedido no válido");
                listarPedidos(request, response);
                return;
            }

            PedidoDAO dao = new PedidoDAO();

            // Obtener el pedido principal
            Pedido pedido = dao.obtenerPedidoPorId(idPedido);

            if (pedido == null) {
                request.setAttribute("error", "No se encontró el pedido solicitado");
                listarPedidos(request, response);
                return;
            }

            // Obtener los items del pedido
            List<ItemPedido> items = dao.obtenerItemsPorPedido(pedido.getCodPedido());

            // Enviar datos al JSP
            request.setAttribute("pedido", pedido);
            request.setAttribute("itemsPedido", items);

            // Forward a la página de detalle
            request.getRequestDispatcher("Vistas/detallePedido.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar el pedido: " + e.getMessage());
            listarPedidos(request, response);
        }
    }

// -----------------------------------------------------------
// ELIMINAR PEDIDO
// -----------------------------------------------------------
    private void eliminarPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idPedido = request.getParameter("id");

            if (idPedido == null || idPedido.trim().isEmpty()) {
                request.setAttribute("error", "ID de pedido no válido");
                listarPedidos(request, response);
                return;
            }

            PedidoDAO dao = new PedidoDAO();

            // Usar el nuevo método que elimina todo
            if (dao.eliminarPedidoCompleto(idPedido)) {
                request.setAttribute("mensaje", "Pedido eliminado correctamente. Stock reintegrado.");
            } else {
                request.setAttribute("error", "No se pudo eliminar el pedido");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al eliminar pedido: " + e.getMessage());
        }

        listarPedidos(request, response);
    }
}
