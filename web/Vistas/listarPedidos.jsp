<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Lista de Pedidos</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            
            <a class="btn btn-secondary mb-3" href="<%= request.getContextPath() %>/UsuarioControlador?Op=VolverInicio">Inicio</a>

            <h2 class="text-center mb-4">Lista de Pedidos</h2>

            <%
                java.util.List<Entidades.Pedido> listaPedidos = (java.util.List<Entidades.Pedido>) request.getAttribute("listaPedidos");
                String mensaje = (String) request.getAttribute("mensaje");
                String error = (String) request.getAttribute("error");
                
                // Mostrar mensajes
                if (mensaje != null) {
            %>
                <div class="alert alert-success text-center"><%= mensaje %></div>
            <%
                }
                if (error != null) {
            %>
                <div class="alert alert-danger text-center"><%= error %></div>
            <%
                }
            %>

            <!-- Si no hay pedidos -->
            <%
                if (listaPedidos == null || listaPedidos.isEmpty()) {
            %>
                <div class="alert alert-warning text-center">
                    No hay pedidos registrados.
                </div>
            <%
                } else {
            %>
            <!-- Tabla de pedidos -->
            <table class="table table-striped table-hover shadow-sm">
                <thead class="table-primary text-center">
                    <tr>
                        <th>ID Pedido</th>
                        <th>ID Cliente</th>
                        <th>Fecha</th>
                        <th>Monto Total</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>

                <tbody class="text-center">
                    <%
                        for (Entidades.Pedido p : listaPedidos) {
                    %>
                        <tr>
                            <td><%= p.getIdPedido() %></td>
                            <td><%= p.getIdCliente() %></td>
                            <td><%= p.getFechaPedido() %></td>
                            <td>S/. <%= p.getMontoTotal() %></td>
                            <td><%= p.getEstado() %></td>

                            <td>
                                <a href="PedidoControlador?Op=Ver&id=<%= p.getIdPedido() %>"
                                   class="btn btn-warning btn-sm opacity-75">
                                    🔎 Ver Pedido
                                </a>

                                <%
                                    if (!"Entregado".equals(p.getEstado())) {
                                %>
                                    <a href="PedidoControlador?Op=MarcarEntregado&id=<%= p.getIdPedido() %>"
                                       class="btn btn-success btn-sm"
                                       onclick="return confirm('¿Marcar pedido <%= p.getIdPedido() %> como entregado?');">
                                        ✓ Entregado
                                    </a>
                                <%
                                    }
                                %>

                                <%
                                    if (!"Entregado".equals(p.getEstado())) {
                                %>
                                    <a href="PedidoControlador?Op=Eliminar&id=<%= p.getIdPedido() %>"
                                       class="btn btn-danger btn-sm"
                                       onclick="return confirm('¿Seguro que deseas eliminar este pedido?\\n\\nSe eliminarán todos los items y se reintegrará el stock a los productos.');">
                                        🗑️ Eliminar
                                    </a>
                                <%
                                    }
                                %>
                            </td>
                        </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
            <%
                }
            %>

            <div class="text-center mt-4">
                <a href="<%= request.getContextPath() %>/UsuarioControlador?Op=VolverInicio" class="btn btn-secondary">Volver al Menú</a>
            </div>

        </div>
    </body>
</html>