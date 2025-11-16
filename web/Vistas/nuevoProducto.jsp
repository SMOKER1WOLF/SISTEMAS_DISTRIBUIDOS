<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

            <c:if test="${not empty mensaje}">
                <div class="alert alert-success">${mensaje}</div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/ProductoControlador" method="post" class="card p-4 shadow-sm">
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

                <div class="text-center">
                    <button type="submit" class="btn btn-primary">Guardar Producto</button>

                    <!-- Corrección del botón ATRÁS -->
                    <a href="${pageContext.request.contextPath}/UsuarioControlador?Op=VolverInicio" class="btn btn-secondary">Atras</a>
                    <a href="${pageContext.request.contextPath}/ProductoControlador?op=listar" 
               class="btn btn-secondary">Lista de productos</a>
                </div>
            </form>
        </div>

    </body>
</html>
