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
        <link rel="stylesheet" href="../css/styles_login.css">
        <title>Login</title>
    </head>
    <body>
        <div class="login-container">

            <h2>Iniciar Sesión</h2>
<input type="text" id="username" placeholder="Usuario" required>
<input type="password" id="password" placeholder="Contraseña" required>
<a href="#" id="loginLink">Iniciar Sesión</a>

            <c:if test="${not empty errorLogin}">
                <div class="error-message">${errorLogin}</div>
            </c:if>
    </body>
</html>
<script>
document.getElementById("loginLink").addEventListener("click", function(e) {
    e.preventDefault(); // Evita que el enlace se abra inmediatamente

    const username = encodeURIComponent(document.getElementById("username").value);
    const password = encodeURIComponent(document.getElementById("password").value);

    if (!username || !password) {
        alert("Todos los campos son obligatorios");
        return;
    }

    // Construye la URL con parámetros GET
    const url = "../UsuarioControlador?Op=Loguin&username=" + username + "&password=" + password;
    // Redirige
    window.location.href = url;
});
</script>