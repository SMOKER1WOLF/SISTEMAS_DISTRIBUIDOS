package Controladores;

import Entidades.Cliente;
import Modelos.ClienteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ClienteControlador", urlPatterns = {"/ClienteControlador"})
public class ClienteControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String Op = request.getParameter("Op");
        ClienteDAO clienteDAO = new ClienteDAO();

        try {
            switch (Op) {
                case "Listar":
                    listarClientes(request, response, clienteDAO);
                    break;

                case "Consultar":
                    consultarCliente(request, response, clienteDAO);
                    break;

                case "Nuevo":
                    // Solo mostrar formulario, no necesita lógica especial
                    request.getRequestDispatcher("nuevoCliente.jsp").forward(request, response);
                    break;

                case "Modificar":
                    modificarCliente(request, response, clienteDAO);
                    break;

                case "Eliminar":
                    eliminarCliente(request, response, clienteDAO);
                    break;

                default:
                    request.setAttribute("error", "Opción no válida");
                    request.getRequestDispatcher("listarClientes.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el sistema: " + e.getMessage());
            request.getRequestDispatcher("listarClientes.jsp").forward(request, response);
        } finally {
            clienteDAO.cerrarConexion();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String Op = request.getParameter("Op");
        ClienteDAO clienteDAO = new ClienteDAO();

        try {
            if ("Nuevo".equals(Op)) {
                insertarCliente(request, response, clienteDAO);
            } else {
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el sistema: " + e.getMessage());
            request.getRequestDispatcher("nuevoCliente.jsp").forward(request, response);
        } finally {
            clienteDAO.cerrarConexion();
        }
    }

    private void listarClientes(HttpServletRequest request, HttpServletResponse response, ClienteDAO clienteDAO) 
            throws ServletException, IOException {
        try {
            List<Cliente> listaCliente = clienteDAO.listarClientes();
            request.setAttribute("Lista", listaCliente);
            request.getRequestDispatcher("/Vistas/listarClientes.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al listar clientes: " + e.getMessage());
            request.getRequestDispatcher("/Vistas/listarClientes.jsp").forward(request, response);
        }
    }

    private void consultarCliente(HttpServletRequest request, HttpServletResponse response, ClienteDAO clienteDAO) 
            throws ServletException, IOException {
        try {
            String id = request.getParameter("idCliente");
            String jsp = request.getParameter("pagina");
            
            Cliente cliente = clienteDAO.consultarCliente(id);
            if (cliente != null) {
                request.setAttribute("cliente", cliente);
            } else {
                request.setAttribute("error", "Cliente no encontrado");
            }
            
            request.getRequestDispatcher(jsp).forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al consultar cliente: " + e.getMessage());
            request.getRequestDispatcher("/Vistas/listarClientes.jsp").forward(request, response);
        }
    }

    private void insertarCliente(HttpServletRequest request, HttpServletResponse response, ClienteDAO clienteDAO) 
            throws ServletException, IOException {
        try {
            // Generar nuevo ID automático
            String nuevoId = clienteDAO.generarNuevoId();
            
            // Crear objeto Cliente con los datos del formulario
            Cliente cliente = new Cliente();
            cliente.setIdCliente(nuevoId);
            cliente.setApellidos(request.getParameter("apellidos"));
            cliente.setNombres(request.getParameter("nombres"));
            cliente.setDireccion(request.getParameter("direccion"));
            cliente.setDNI(request.getParameter("DNI"));
            cliente.setTelefono(request.getParameter("telefono"));
            cliente.setMovil(request.getParameter("movil"));
            
            // Insertar en la base de datos
            boolean exito = clienteDAO.insertarCliente(cliente);
            
            if (exito) {
                request.setAttribute("mensaje", "✅ Cliente registrado correctamente con ID: " + nuevoId);
            } else {
                request.setAttribute("error", "❌ No se pudo registrar el cliente.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "⚠️ Error al registrar cliente: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/Vistas/nuevoCliente.jsp").forward(request, response);
    }

    private void modificarCliente(HttpServletRequest request, HttpServletResponse response, ClienteDAO clienteDAO) 
            throws ServletException, IOException {
        try {
            // Crear objeto Cliente con los datos del formulario
            Cliente cliente = new Cliente();
            cliente.setIdCliente(request.getParameter("idCliente"));
            cliente.setApellidos(request.getParameter("apellidos"));
            cliente.setNombres(request.getParameter("nombres"));
            cliente.setDireccion(request.getParameter("direccion"));
            cliente.setDNI(request.getParameter("DNI"));
            cliente.setTelefono(request.getParameter("telefono"));
            cliente.setMovil(request.getParameter("movil"));
            
            // Actualizar en la base de datos
            boolean exito = clienteDAO.actualizarCliente(cliente);
            
            if (exito) {
                // Consultar datos actualizados
                Cliente clienteActualizado = clienteDAO.consultarCliente(cliente.getIdCliente());
                request.setAttribute("cliente", clienteActualizado);
                request.setAttribute("mensaje", "✅ Cliente modificado correctamente.");
            } else {
                request.setAttribute("error", "❌ No se pudo modificar el cliente. Verifique los datos.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "⚠️ Error al modificar cliente: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/Vistas/modificarCliente.jsp").forward(request, response);
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response, ClienteDAO clienteDAO) 
            throws ServletException, IOException {
        try {
            String idCliente = request.getParameter("idCliente");
            
            boolean exito = clienteDAO.eliminarCliente(idCliente);
            
            if (exito) {
                request.setAttribute("mensaje", "✅ Cliente eliminado correctamente.");
            } else {
                request.setAttribute("error", "❌ No se encontró el cliente a eliminar.");
            }
            
            // Volver a listar los clientes actualizados
            List<Cliente> listaCliente = clienteDAO.listarClientes();
            request.setAttribute("Lista", listaCliente);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "⚠️ Error al eliminar cliente: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/Vistas/listarClientes.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador de clientes";
    }
}