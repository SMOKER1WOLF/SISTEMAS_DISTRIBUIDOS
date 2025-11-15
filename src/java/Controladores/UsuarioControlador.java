package Controladores;

//import Entidades.Cliente;
import Entidades.Usuario;
import Herramientas.Conexion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "UsuarioControlador", urlPatterns = {"/UsuarioControlador"})
public class UsuarioControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String Op = request.getParameter("Op");
//        ArrayList<Cliente> listaCliente = new ArrayList<Cliente>();

        Conexion conect = new Conexion();
        Connection con = conect.establecerConexion();

        PreparedStatement ps;
        ResultSet rs;

        switch (Op) {
//            case "Listar":
//            try {
//                String sql = "Select * from cliente";
//                ps = con.prepareStatement(sql);
//                rs = ps.executeQuery();
//
//                while (rs.next()) {
//                    Cliente cli = new Cliente();
//                    cli.setIdCliente(rs.getString("idCliente"));
//                    cli.setApellidos(rs.getString("apellidos"));
//                    cli.setNombres(rs.getString("nombres"));
//                    cli.setDireccion(rs.getString("direccion"));
//                    cli.setDNI(rs.getString("DNI"));
//                    cli.setTelefono(rs.getString("telefono"));
//                    cli.setMovil(rs.getString("movil"));
//
//                    listaCliente.add(cli);
//
//                }
//                request.setAttribute("Lista", listaCliente);
//                request.getRequestDispatcher("listarClientes.jsp").forward(request, response);
//            } catch (SQLException ex) {
//                System.out.println("Error de SQL +" + ex.getMessage());
//            } finally {
//                conect.disconnect();
//            }
//
//            break;

            case "Loguin":
                String username = request.getParameter("username");
                String password = request.getParameter("password");

                if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                    request.setAttribute("errorLogin", "Todos los campos son obligatorios.");
                    request.getRequestDispatcher("Vistas/Login.jsp").forward(request, response);
                    return;
                }

                try {
                    HttpSession sesion = request.getSession();

                    // DECLARAMOS EL CONTADOR DE INTENTOS FALLIDOS
                    Integer intentosFallidos = (Integer) sesion.getAttribute("intentosFallidos_" + username);
                    if (intentosFallidos == null) {
                        intentosFallidos = 0;
                    }

                    // 1. Buscar usuario
                    String sql = "SELECT d.*, c.Estados, c.Num_Ingresos, c.Permisos FROM datosusuarios d "
                            + "JOIN catusuario c ON d.codUsuario = c.codUsuario "
                            + "WHERE d.usuario = ?";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, username);
                    rs = ps.executeQuery();

                    Usuario user = null;
                    String estado = null;

                    if (rs.next()) {
                        user = new Usuario();
                        user.setCodUsuario(rs.getString("codUsuario"));
                        user.setUsuario(rs.getString("usuario"));
                        user.setPassword(rs.getString("password"));
                        user.setNombres(rs.getString("nombres"));
                        user.setApellidos(rs.getString("apellidos"));
                        user.setEmail(rs.getString("email"));
                        user.setEstado(rs.getString("Estados"));
                        user.setPermisos(rs.getString("Permisos"));
                        user.setNumIngresos(rs.getInt("Num_Ingresos"));
                        estado = rs.getString("Estados");
                    }

                    if (user == null) {
                        request.setAttribute("errorLogin", "Usuario no existe.");
                        request.getRequestDispatcher("Vistas/Login.jsp").forward(request, response);
                        return;
                    }

                    // VERIFICA SI ESTA BLOQUEADO EL USUARIO
                    if ("Bloqueado".equalsIgnoreCase(estado)||"Eliminado".equalsIgnoreCase(estado)) {
                        request.setAttribute("errorLogin", "Tu cuenta está inactiva. Contacta al administrador.");
                        request.getRequestDispatcher("Vistas/Login.jsp").forward(request, response);
                        return;
                    }

                    // VALIDA LA PASSWORD
                    if (!password.equals(user.getPassword())) {
                        intentosFallidos++;
                        sesion.setAttribute("intentosFallidos_" + username, intentosFallidos);

                        if (intentosFallidos >= 3) {
                            // Actualizar en la BD a "Bloqueado"
                            String sqlBlock = "UPDATE catusuario SET Estados = 'Bloqueado' WHERE codUsuario = ?";
                            ps = con.prepareStatement(sqlBlock);
                            ps.setString(1, user.getCodUsuario());
                            ps.executeUpdate();

                            request.setAttribute("errorLogin", "Has superado los 3 intentos. Tu cuenta ha sido bloqueada.");
                        } else {
                            request.setAttribute("errorLogin", "Contraseña incorrecta. Intento " + intentosFallidos + " de 3.");
                        }

                        request.getRequestDispatcher("Vistas/Login.jsp").forward(request, response);
                        return;
                    }

                    // RESETEO DE INTENTOS
                    sesion.setAttribute("intentosFallidos_" + username, 0);

                    // ACTUALIZA EL EnLinea y NumIngresos
                    String sql2 = "UPDATE catusuario SET EnLinea = 1, Num_Ingresos = Num_Ingresos + 1 WHERE codUsuario = ?";
                    ps = con.prepareStatement(sql2);
                    ps.setString(1, user.getCodUsuario());
                    ps.executeUpdate();
                    
                    // ACTUALIZA LA HORA Y FECHA DE ULTIMA CONEXION
                    String sql3 = "UPDATE altusuario SET Fec_UltimoAcceso = ?, Hor_UltimoAcceso	 = ? WHERE codUsuario = ?";
                    ps = con.prepareStatement(sql3);
                    ps.setString(1, Herramientas.Persistencias.getFecha());
                    ps.setString(2, Herramientas.Persistencias.getHora());
                    ps.setString(3, user.getCodUsuario());
                    ps.executeUpdate();

                    Herramientas.Persistencias.setUser(user);
                    sesion.setAttribute("usuarioLogueado", user);
                    request.getRequestDispatcher("Vistas/Index.jsp").forward(request, response);

                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("errorLogin", "Error en la base de datos: " + e.getMessage());
                    request.getRequestDispatcher("Vistas/Login.jsp").forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("errorLogin", "Ocurrió un error inesperado: " + e.getMessage());
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }

                break;

            case "Consultar":
                break;

            case "Nuevo":
                break;

            case "Modificar":
                break;

            case "Eliminar":
                break;

            case "CerrarSesion":
                try {

                //ALTERA LOS DATOS DE EN LINEA A DESCONCETADO
                String CodUsuario = Herramientas.Persistencias.getUser().getCodUsuario();

                String sql = "UPDATE catusuario SET EnLinea = 0 WHERE codUsuario = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, CodUsuario);
                ps.executeUpdate();

                Herramientas.Persistencias.setUser(null);
                request.getRequestDispatcher("/Login.jsp").forward(request, response);

            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorLogout", "Error en la base de datos: " + e.getMessage());
                request.getRequestDispatcher("Vistas/Login.jsp").forward(request, response);

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorLogout", "Ocurrió un error inesperado: " + e.getMessage());
                request.getRequestDispatcher("Vistas/Login.jsp").forward(request, response);

            }

            break;
            default:
                System.out.println("MALA ");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
