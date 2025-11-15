<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Semana 07</title>
        <link rel="stylesheet" type="text/css" href="css/styles_index.css">
    </head>

    <header>
        <h2>Bienvenido: ${usuarioLogueado.nombres} - ${usuarioLogueado.permisos}</h2>
        <nav>   
            <a href="UsuarioControlador?Op=CerrarSesion">Cerrar sesión</a>
        </nav>
    </header>

    <body>
        <div class="main-content">
            <div class="container">
                <h1>Menú Principal</h1>

                <h2>Clientes</h2>
                <p><a href="ClienteControlador?Op=Listar">Listar Clientes</a></p>
                <p><a href="nuevoCliente.jsp">Nuevo Cliente</a></p>

                <hr>

                <h2>Productos</h2>
                <p><a href="ProductoControlador?op=listar">Listar Productos</a></p>
                <p><a href="nuevoProducto.jsp">Nuevo Producto</a></p>
                
                <hr>
                <h2>Pedidos</h2>
                <p><a href="PedidoControlador?Op=MostrarFormulario">Registrar Pedido</a></p>

            </div>
        </div>
    </body>
</html>
