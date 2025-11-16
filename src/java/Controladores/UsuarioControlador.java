package Controladores;

import Entidades.Usuario;
import Herramientas.Conexion;
import Modelos.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "UsuarioControlador", urlPatterns = {"/UsuarioControlador"})
public class UsuarioControlador extends HttpServlet {

    private final String pagLogin = "/Vistas/Login.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String Op = request.getParameter("Op");
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {
            switch (Op) {

                case "Loguin":
                    procesarLogin(request, response, usuarioDAO);
                    break;

                case "CerrarSesion":
                    procesarLogout(request, response, usuarioDAO);
                    break;

                case "VolverInicio":
                    request.getRequestDispatcher("Vistas/Index.jsp").forward(request, response);
                    break;

                case "Consultar":
                    // Implementar consulta si es necesario
                    break;

                case "Nuevo":
                    // Implementar creación de usuario
                    break;

                case "Modificar":
                    // Implementar modificación de usuario
                    break;

                case "Eliminar":
                    // Implementar eliminación de usuario
                    break;

                default:
                    System.out.println("Opción no válida: " + Op);
            }
        } finally {
            usuarioDAO.cerrarConexion();
        }
    }

    protected void verLogin(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {

        try {
            String url = request.getContextPath() + "/Vistas/Login.jsp";

            if (error != null && !error.trim().isEmpty()) {
                // Codificar el error para URL
                String encodedError = java.net.URLEncoder.encode(error, "UTF-8");
                url += "?errorLogin=" + encodedError;
            }

            response.sendRedirect(url);

        } catch (Exception e) {
            // Fallback en caso de error
            response.sendRedirect(request.getContextPath() + "/Vistas/Login.jsp");
        }
    }

    private void procesarLogin(HttpServletRequest request, HttpServletResponse response, UsuarioDAO usuarioDAO)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validar campos vacíos
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            verLogin(request, response, "Todos los campos son obligatorios.");
            return;
        }

        try {
            HttpSession sesion = request.getSession();
            Integer intentosFallidos = (Integer) sesion.getAttribute("intentosFallidos_" + username);
            if (intentosFallidos == null) {
                intentosFallidos = 0;
            }

            // Buscar usuario en el modelo
            Usuario user = usuarioDAO.buscarUsuarioPorUsername(username);

            if (user == null) {
                verLogin(request, response, "Usuario no existe");
                return;
            }

            // Verificar si está bloqueado o eliminado
            if ("Bloqueado".equalsIgnoreCase(user.getEstado()) || "Eliminado".equalsIgnoreCase(user.getEstado())) {
                verLogin(request, response, "Tu cuenta está inactiva. Contacta al administrador.");
                return;
            }

            // Validar password
            if (!password.equals(user.getPassword())) {
                intentosFallidos++;
                sesion.setAttribute("intentosFallidos_" + username, intentosFallidos);

                if (intentosFallidos >= 3) {
                    usuarioDAO.bloquearUsuario(user.getCodUsuario());
                    verLogin(request, response, "Has superado los 3 intentos. Tu cuenta ha sido bloqueada.");
                } else {
                    verLogin(request, response, "Contraseña incorrecta. Intento " + intentosFallidos + " de 3.");
                }
                return;
            }

            // Login exitoso
            sesion.setAttribute("intentosFallidos_" + username, 0);
            usuarioDAO.actualizarLoginExitoso(user.getCodUsuario());

            Herramientas.Persistencias.setUser(user);
            sesion.setAttribute("usuarioLogueado", user);

            // Redirigir al index después de login exitoso
            request.getRequestDispatcher("Vistas/Index.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            verLogin(request, response, "Error en la base de datos: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            verLogin(request, response, "Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private void procesarLogout(HttpServletRequest request, HttpServletResponse response, UsuarioDAO usuarioDAO)
            throws ServletException, IOException {

        try {
            String codUsuario = Herramientas.Persistencias.getUser().getCodUsuario();
            usuarioDAO.actualizarLogout(codUsuario);

            Herramientas.Persistencias.setUser(null);
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/index.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorLogout", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("Vistas/Login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorLogout", "Ocurrió un error inesperado: " + e.getMessage());
            request.getRequestDispatcher("Vistas/Login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador de usuarios";
    }
}
