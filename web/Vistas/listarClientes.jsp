<%@page import="java.util.List"%>
<%@page import="Entidades.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 
    List<Cliente> Lista = (List<Cliente>) request.getAttribute("Lista");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista de clientes</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles_lista.css">
        <a href="${pageContext.request.contextPath}/UsuarioControlador?Op=VolverInicio">Inicio</a>
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
            <%
                if (Lista != null) {
                    for (Cliente campo : Lista) {
            %>
            <tr>
                <td><%= campo.getIdCliente() %></td>
                <td><%= campo.getApellidos() %></td>
                <td><%= campo.getNombres() %></td>
                <td><%= campo.getDNI() %></td>
                <td><a href="${pageContext.request.contextPath}/ClienteControlador?Op=Consultar&idCliente=<%= campo.getIdCliente() %>&pagina=consultaCliente.jsp">Consultar</a></td>
                <td><a href="${pageContext.request.contextPath}/ClienteControlador?Op=Consultar&idCliente=<%= campo.getIdCliente() %>&pagina=modificarCliente.jsp">Modificar</a></td>
                <td><a href="${pageContext.request.contextPath}/ClienteControlador?Op=Eliminar&idCliente=<%= campo.getIdCliente() %>" 
                       onclick="return confirm('¿Seguro que deseas eliminar este cliente?');">
                        Eliminar
                    </a>
                </td>
            </tr>
            <%
                    }
                }
            %>
        </table>
        <div class="boton">
            <a href="${pageContext.request.contextPath}/UsuarioControlador?Op=VolverInicio" class="btn-volver">Volver al Inicio</a>
        </div>
    </body>
</html>