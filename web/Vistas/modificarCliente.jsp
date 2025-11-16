<%-- 
    Document   : modificarCliente
    Created on : 12 oct. 2025, 20:01:55
    Author     : USUARIO
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Modificar Cliente</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/styles_lista.css">
    <a href="<%= request.getContextPath() %>/UsuarioControlador?Op=VolverInicio">Inicio</a>
</head>
<body class="bg-light">

<%
    String error = (String) request.getAttribute("error");
    String mensaje = (String) request.getAttribute("mensaje");
    Entidades.Cliente cliente = (Entidades.Cliente) request.getAttribute("cliente");
%>

<div class="container mt-5">
    <h2 class="text-center mb-4">Modificar Cliente</h2>

    <% if (error != null) { %>
        <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <% if (mensaje != null) { %>
        <div class="alert alert-success"><%= mensaje %></div>
    <% } %>

    <% if (cliente != null) { %>
        <form action="ClienteControlador" method="get" class="card p-4 shadow-sm">
            <input type="hidden" name="Op" value="Modificar">
            <input type="hidden" name="idCliente" value="<%= cliente.getIdCliente() %>">

            <div class="mb-3">
                <label class="form-label">Apellidos</label>
                <input type="text" name="apellidos" class="form-control" value="<%= cliente.getApellidos() %>" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Nombres</label>
                <input type="text" name="nombres" class="form-control" value="<%= cliente.getNombres() %>" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Dirección</label>
                <input type="text" name="direccion" class="form-control" value="<%= cliente.getDireccion() %>">
            </div>

            <div class="mb-3">
                <label class="form-label">DNI</label>
                <input type="text" name="DNI" class="form-control" value="<%= cliente.getDNI() %>" maxlength="8">
            </div>

            <div class="mb-3">
                <label class="form-label">Teléfono</label>
                <input type="text" name="telefono" class="form-control" value="<%= cliente.getTelefono() %>">
            </div>

            <div class="mb-3">
                <label class="form-label">Móvil</label>
                <input type="text" name="movil" class="form-control" value="<%= cliente.getMovil() %>">
            </div>

            <div class="text-center">
                <button type="submit" class="btn btn-primary">Modificar</button>
                <a href="<%= request.getContextPath() %>/ClienteControlador?Op=Listar" 
                   class="btn btn-secondary">Volver</a>
            </div>
        </form>
    <% } else { %>
        <div class="alert alert-warning text-center">
            No se encontró ningún cliente con ese ID.
        </div>
    <% } %>

</div>

</body>
</html>
