<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Detalle del Pedido</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <h4 class="mb-0">📋 Detalle del Pedido</h4>
            </div>
            <div class="card-body">
                
                <%-- Mensajes --%>
                <%
                    String mensaje = (String) request.getAttribute("mensaje");
                    String error = (String) request.getAttribute("error");
                    
                    if (mensaje != null) {
                %>
                    <div class="alert alert-success"><%= mensaje %></div>
                <%
                    }
                    if (error != null) {
                %>
                    <div class="alert alert-danger"><%= error %></div>
                <%
                    }
                %>

                <%-- Información del pedido --%>
                <%
                    Entidades.Pedido pedido = (Entidades.Pedido) request.getAttribute("pedido");
                    if (pedido != null) {
                %>
                <div class="row mb-4">
                    <div class="col-md-6">
                        <h5>Información del Pedido</h5>
                        <table class="table table-bordered">
                            <tr>
                                <th>ID Pedido:</th>
                                <td><%= pedido.getIdPedido() %></td>
                            </tr>
                            <tr>
                                <th>Código Pedido:</th>
                                <td><%= pedido.getCodPedido() %></td>
                            </tr>
                            <tr>
                                <th>Cliente:</th>
                                <td><%= pedido.getIdCliente() %></td>
                            </tr>
                            <tr>
                                <th>Fecha:</th>
                                <td><%= pedido.getFechaPedido() %></td>
                            </tr>
                            <tr>
                                <th>Estado:</th>
                                <td>
                                    <%
                                        String badgeClass = "bg-secondary";
                                        if ("Entregado".equals(pedido.getEstado())) {
                                            badgeClass = "bg-success";
                                        } else if ("Pendiente".equals(pedido.getEstado())) {
                                            badgeClass = "bg-warning";
                                        }
                                    %>
                                    <span class="badge <%= badgeClass %>">
                                        <%= pedido.getEstado() %>
                                    </span>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="col-md-6">
                        <h5>Resumen Financiero</h5>
                        <table class="table table-bordered">
                            <tr>
                                <th>Monto Total:</th>
                                <td class="fw-bold">S/. <%= pedido.getMontoTotal() %></td>
                            </tr>
                        </table>
                    </div>
                </div>

                <%-- Items del pedido --%>
                <h5>Productos del Pedido</h5>
                <%
                    java.util.List<Entidades.ItemPedido> itemsPedido = (java.util.List<Entidades.ItemPedido>) request.getAttribute("itemsPedido");
                    if (itemsPedido != null && !itemsPedido.isEmpty()) {
                %>
                    <table class="table table-striped table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>Producto</th>
                                <th>Cantidad</th>
                                <th>Precio Unitario</th>
                                <th>IGV</th>
                                <th>Subtotal</th>
                                <th>Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (Entidades.ItemPedido item : itemsPedido) {
                            %>
                                <tr>
                                    <td>
                                        <%
                                            if (item.getNombreProducto() != null && !item.getNombreProducto().trim().isEmpty()) {
                                        %>
                                            <%= item.getNombreProducto() %>
                                        <%
                                            } else {
                                        %>
                                            <%= item.getIdProducto() %>
                                        <%
                                            }
                                        %>
                                    </td>
                                    <td><%= item.getCantidad() %></td>
                                    <td>S/. <%= item.getPrecioUnitario() %></td>
                                    <td>S/. <%= item.getIgv() %></td>
                                    <td>S/. <%= item.getSubtotal() %></td>
                                    <td class="fw-bold">S/. <%= item.getTotal() %></td>
                                </tr>
                            <%
                                }
                            %>
                        </tbody>
                        <tfoot>
                            <tr class="table-primary">
                                <td colspan="4" class="text-end fw-bold">Total General:</td>
                                <td colspan="2" class="fw-bold">S/. <%= pedido.getMontoTotal() %></td>
                            </tr>
                        </tfoot>
                    </table>
                <%
                    } else {
                %>
                    <div class="alert alert-warning">
                        No se encontraron productos para este pedido.
                    </div>
                <%
                    }
                %>

                <%-- Botones de acción --%>
                <div class="mt-4">
                    <a href="PedidoControlador?Op=Listar" class="btn btn-secondary">← Volver a la lista</a>
                    
                    <%
                        if (!"Entregado".equals(pedido.getEstado())) {
                    %>
                        <a href="PedidoControlador?Op=MarcarEntregado&id=<%= pedido.getIdPedido() %>" 
                           class="btn btn-success"
                           onclick="return confirm('¿Marcar pedido como entregado?');">
                            ✓ Marcar como Entregado
                        </a>
                    <%
                        }
                    %>
                </div>
                <%
                    } else {
                %>
                    <div class="alert alert-danger">
                        No se encontró información del pedido.
                    </div>
                    <div class="mt-4">
                        <a href="PedidoControlador?Op=Listar" class="btn btn-secondary">← Volver a la lista</a>
                    </div>
                <%
                    }
                %>

            </div>
        </div>
    </div>
</body>
</html>