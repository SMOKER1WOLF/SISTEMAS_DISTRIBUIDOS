<%-- 
    Document   : consultaCliente
    Created on : 12 oct. 2025, 19:36:07
    Author     : USUARIO
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Consulta de Cliente</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/styles_lista.css">
    <a href="<%= request.getContextPath() %>/UsuarioControlador?Op=VolverInicio">Inicio</a>
</head>
<body class="bg-light">

<%
    // Recuperar atributos enviados desde el controlador
    String error = (String) request.getAttribute("error");
    Entidades.Cliente cliente = (Entidades.Cliente) request.getAttribute("cliente");
%>

<div class="container mt-5">
    <h2 class="text-center mb-4">Consulta de Cliente</h2>

    <% if (error != null) { %>
        <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <% if (cliente != null) { %>
        <table class="table table-bordered">
            <tr><th>ID Cliente</th><td><%= cliente.getIdCliente() %></td></tr>
            <tr><th>Apellidos</th><td><%= cliente.getApellidos() %></td></tr>
            <tr><th>Nombres</th><td><%= cliente.getNombres() %></td></tr>
            <tr><th>Dirección</th><td><%= cliente.getDireccion() %></td></tr>
            <tr><th>DNI</th><td><%= cliente.getDNI() %></td></tr>
            <tr><th>Teléfono</th><td><%= cliente.getTelefono() %></td></tr>
            <tr><th>Móvil</th><td><%= cliente.getMovil() %></td></tr>
        </table>
    <% } else { %>
        <div class="alert alert-warning text-center">
            No se encontró ningún cliente con ese ID.
        </div>
    <% } %>

    <div class="mt-4 text-center">
        <a href="<%= request.getContextPath() %>/ClienteControlador?Op=Listar" class="btn btn-secondary">Volver</a>
    </div>
</div>

</body>
</html>
