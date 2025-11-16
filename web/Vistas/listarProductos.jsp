<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de Productos</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>

<body class="bg-light">

<%
    java.util.List<Entidades.Producto> listaProductos =
        (java.util.List<Entidades.Producto>) request.getAttribute("listaProductos");
%>

<div class="container mt-5">
    <h2 class="text-center mb-4">Lista de Productos</h2>

    <!-- 🔍 Formulario de búsqueda -->
    <form action="ProductoControlador" method="get" class="d-flex mb-4 justify-content-center">
        <input type="hidden" name="op" value="buscar">
        <input type="text" name="texto" class="form-control w-50 me-2" 
               placeholder="Buscar por ID o nombre" required>
        <button type="submit" class="btn btn-primary">Buscar</button>
    </form>

    <!-- Mensaje cuando no hay resultados -->
    <% if (listaProductos == null || listaProductos.isEmpty()) { %>
        <div class="alert alert-warning text-center" role="alert">
            No se encontraron productos que coincidan con la búsqueda.
        </div>
    <% } %>

    <!-- 📋 Tabla de productos -->
    <% if (listaProductos != null && !listaProductos.isEmpty()) { %>
        <table class="table table-striped table-hover shadow-sm">
            <thead class="table-primary text-center">
                <tr>
                    <th>ID Artículo</th>
                    <th>Nombre</th>
                    <th>Cantidad</th>
                    <th>Precio</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody class="text-center">
                <% for (Entidades.Producto p : listaProductos) { %>
                    <tr>
                        <td><%= p.getIdArticulo() %></td>
                        <td><%= p.getNombre() %></td>
                        <td><%= p.getCantidad() %></td>
                        <td><%= p.getPrecio() %></td>
                        <td>
                            <!-- Botón Modificar -->
                            <a href="<%= request.getContextPath() %>/ProductoControlador?op=obtener&id=<%= p.getIdArticulo() %>"
                               class="btn btn-warning btn-sm me-2">
                                Modificar
                            </a>

                            <!-- Botón Eliminar -->
                            <a href="<%= request.getContextPath() %>/ProductoControlador?op=eliminar&id=<%= p.getIdArticulo() %>"
                               class="btn btn-danger btn-sm"
                               onclick="return confirm('¿Seguro que deseas eliminar este producto?');">
                                Eliminar
                            </a>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>

    <!-- 🔁 Botones de acción -->
    <div class="text-center mt-4">
        <a href="<%= request.getContextPath() %>/ProductoControlador?op=listar" class="btn btn-outline-secondary me-2">
            Mostrar todos
        </a>
        <a href="<%= request.getContextPath() %>/UsuarioControlador?Op=VolverInicio" class="btn btn-secondary">
            Volver al Menú
        </a>
    </div>
</div>

</body>
</html>
