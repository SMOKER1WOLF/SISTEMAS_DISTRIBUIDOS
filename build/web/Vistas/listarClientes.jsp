<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import ="java.util.List"%>
<%@page import ="Entidades.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% List <Cliente> Lista = (List<Cliente>) request.getAttribute("Lista");%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista de clientes</title>
        <link rel="stylesheet" type="text/css" href="css/styles_lista.css">
    <a href="UsuarioControlador?Op=VolverInicio" > Inicio </a>
</head>
<body>
    <h1>Lista de clientes</h1>
    <table>
        <tr>
            <th>Id_Cliente</th>
            <th>Apellidos</th>
            <th>Nombres</th>
            <th>DNI</th>
            <th colspan="3">Acciones</th>
        </tr>
        <c:forEach items="${Lista}" var="campo">
            <tr>
                <td>${campo.idCliente}</td>
                <td>${campo.apellidos}</td>
                <td>${campo.nombres}</td>
                <td>${campo.DNI}</td>
                <td><a href="ClienteControlador?Op=Consultar&idCliente=${campo.idCliente}&pagina=consultaCliente.jsp">Consultar</a></td>
                <td><a href="ClienteControlador?Op=Consultar&idCliente=${campo.idCliente}&pagina=modificarCliente.jsp">Modificar</a></td>
                <td><a href="ClienteControlador?Op=Eliminar&idCliente=${campo.idCliente}" 
                       onclick="return confirm('¿Seguro que deseas eliminar este cliente?');">
                        Eliminar
                    </a>
                </td>

            </tr>
        </c:forEach>
    </table>
    <div class="boton">
        <a href="UsuarioControlador?Op=VolverInicio" class="btn-volver">Volver al Inicio</a>
    </div>
</body>
</html>
