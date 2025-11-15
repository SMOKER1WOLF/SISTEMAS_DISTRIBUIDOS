<%-- 
    Document   : Logueo
    Created on : 28 sep. 2025, 12:23:35
    Author     : USUARIO
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.*, javax.servlet.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/styles_login.css">
        <title>Login</title>
    </head>
    <body>
        <div class="login-container">

            <h2>Iniciar Sesión</h2>
            <form action="UsuarioControlador" method="get">
                <input type="hidden" name="Op" value="Loguin">
                <input type="text" name="username" placeholder="Usuario" required>
                <input type="password" name="password" placeholder="Contraseña" required>
                <input type="submit" value="Iniciar Sesión">
            </form>

            <c:if test="${not empty errorLogin}">
                <div class="error-message">${errorLogin}</div>
            </c:if>
    </body>
</html>
