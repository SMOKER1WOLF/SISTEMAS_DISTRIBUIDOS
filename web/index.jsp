<%-- 
    Document   : index
    Created on : 15 nov. 2025, 02:19:09
    Author     : USUARIO
--%>

<%@ page language="java" %>
<%
    String error =request.getParameter("errorLogin");
    request.setAttribute("errorLogin", error);
    response.sendRedirect("Vistas/Login.jsp");
%>