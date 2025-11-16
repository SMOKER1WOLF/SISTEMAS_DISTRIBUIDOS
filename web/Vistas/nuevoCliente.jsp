<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar nuevo cliente</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
%>

<div class="container mt-5">
    <h2 class="text-center mb-4">Registrar nuevo cliente</h2>

    <% if (mensaje != null) { %>
        <div class="alert alert-success mt-3 text-center"><%= mensaje %></div>
    <% } %>

    <% if (error != null) { %>
        <div class="alert alert-danger mt-3 text-center"><%= error %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/ClienteControlador" method="post" class="card p-4 shadow-sm">
        <input type="hidden" name="Op" value="Nuevo">

        <div class="mb-3">
            <label class="form-label">Apellidos</label>
            <input type="text" name="apellidos" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Nombres</label>
            <input type="text" name="nombres" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Dirección</label>
            <input type="text" name="direccion" class="form-control">
        </div>

        <div class="mb-3">
            <label class="form-label">DNI</label>
            <input type="text" name="DNI" class="form-control" maxlength="8">
        </div>

        <div class="mb-3">
            <label class="form-label">Teléfono</label>
            <input type="text" name="telefono" class="form-control">
        </div>

        <div class="mb-3">
            <label class="form-label">Móvil</label>
            <input type="text" name="movil" class="form-control">
        </div>

        <div class="text-center">
            <button type="submit" class="btn btn-primary">Registrar</button>
            <a href="<%= request.getContextPath() %>/UsuarioControlador?Op=VolverInicio"
               class="btn btn-secondary">Volver al Inicio</a>
        </div>
    </form>

</div>

</body>
</html>
