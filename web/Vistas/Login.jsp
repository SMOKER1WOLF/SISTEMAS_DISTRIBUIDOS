<%-- 
    Document   : Logueo
    Created on : 28 sep. 2025, 12:23:35
    Author     : USUARIO
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.*, jakarta.servlet.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles_login.css">
        <title>Login</title>
    </head>
    <body>
        <div class="login-container">
            <h2>Iniciar Sesión</h2>
            <form action="${pageContext.request.contextPath}/UsuarioControlador" method="get">
                <input type="hidden" name="Op" value="Loguin">
                <input type="text" name="username" placeholder="Usuario" required>
                <input type="password" name="password" placeholder="Contraseña" required>
                <input type="submit" value="Iniciar Sesión">
            </form>

            <%
                String errorLogin = request.getParameter("errorLogin");
                if (errorLogin != null && !errorLogin.trim().isEmpty()) {
            %>
            <div class="error-message"><%= errorLogin %></div>
            <%
                }
            %>
        </div>
    </body>
</html>