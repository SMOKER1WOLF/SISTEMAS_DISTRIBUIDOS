<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Nuevo Producto</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    </head>

    <body class="bg-light">

        <div class="container mt-5">
            <h2 class="text-center mb-4">Nuevo Producto</h2>

            <!-- Mensajes exitosos -->
            <% 
                if (request.getAttribute("mensaje") != null) { 
            %>
            <div class="alert alert-success">
                <%= request.getAttribute("mensaje") %>
            </div>
            <% 
                }
            %>

            <!-- Mensajes de error -->
            <% 
                if (request.getAttribute("error") != null) { 
            %>
            <div class="alert alert-danger">
                <%= request.getAttribute("error") %>
            </div>
            <% 
                } 
            %>


            <!-- Formulario -->
            <form action="<%= request.getContextPath() %>/ProductoControlador" method="post" class="card p-4 shadow-sm">
                <input type="hidden" name="op" value="insertar">

                <div class="mb-3">
                    <label class="form-label">Nombre del Producto</label>
                    <input type="text" name="nombre" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Cantidad</label>
                    <input type="number" name="cantidad" class="form-control" min="0" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Precio</label>
                    <input type="number" step="0.01" name="precio" class="form-control" min="0" required>
                </div>

                <!-- Botones -->
                <div class="text-center">
                    <button type="submit" class="btn btn-primary">Guardar Producto</button>

                    <a href="${pageContext.request.contextPath}/UsuarioControlador?Op=VolverInicio" class="btn btn-secondary">Atras</a>
                    <a href="${pageContext.request.contextPath}/ProductoControlador?op=listar" 
                       class="btn btn-secondary">Lista de productos</a>
                </div>

            </form>
        </div>

    </body>
</html>
