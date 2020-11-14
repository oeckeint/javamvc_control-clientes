package web;

import datos.clienteDaoJDBC;
import dominio.Cliente;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null && !accion.trim().equals("")) {
            switch (accion) {
                case "editar": {
                    try {
                        this.editarCliente(request, response);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case "eliminar": {
                    try {
                        this.eliminarCliente(request, response);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    private void accionDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Cliente> clientes = new clienteDaoJDBC().listar();
            //System.out.println("clientes = " + clientes);
            HttpSession sesion = request.getSession();
            sesion.setAttribute("clientes", clientes);
            sesion.setAttribute("totalClientes", clientes.size());
            sesion.setAttribute("saldoTotal", this.calcularSaldoTotal(clientes));
            //request.getRequestDispatcher("clientes.jsp").forward(request, response);
            response.sendRedirect("clientes.jsp");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null && !accion.trim().equals("")) {
            switch (accion) {
                case "insertar": {
                    try {
                        this.insertarCliente(request, response);
                    } catch (ClassNotFoundException ex) {

                    }
                    break;
                }
                case "modificar": {
                    try {
                        this.modificarCliente(request, response);
                    } catch (ClassNotFoundException ex) {

                    }
                    break;
                }

                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    private double calcularSaldoTotal(List<Cliente> clientes) {
        double saldoTotal = 0;
        for (Cliente cliente : clientes) {
            saldoTotal += cliente.getSaldo();
        }
        return saldoTotal;
    }

    private void insertarCliente(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, ServletException, IOException {
        //1 Recuperación de los valores del cliente
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        if (saldoString != null && !saldoString.trim().equals("")) {
            saldo = Double.parseDouble(saldoString);
        } else {
            saldo = 0;
        }

        //2 Creación del objeto cliente (modelo)
        Cliente cliente = new Cliente(nombre, apellido, email, telefono, saldo);

        //3 Insertarlo en la base de datos
        int registrosModificado = new clienteDaoJDBC().insertar(cliente);
        System.out.println("registrosModificado = " + registrosModificado);

        //4. Ejecutar la accion por default recargando toda la información
        this.accionDefault(request, response);
    }

    private void modificarCliente(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, ServletException, IOException {
        //1 Recuperación de los valores del cliente
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        double saldo = 0;
        String saldoString = request.getParameter("saldo");
        if (saldoString != null && !saldoString.trim().equals("")) {
            saldo = Double.parseDouble(saldoString);
        } else {
            saldo = 0;
        }

        System.out.println("Hola");

        //2 Creación del objeto cliente (modelo)
        Cliente cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);

        //3 Actualizar en la base de datos
        System.out.println("cliente = " + cliente);
        int registrosModificado = new clienteDaoJDBC().actualizar(cliente);
        System.out.println("registrosModificado = " + registrosModificado);

        //4. Ejecutar la accion por default recargando toda la información
        this.accionDefault(request, response);
    }

    private void editarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException {
        //Recuperación de los datos
        int idCliente = 0;
        String idClienteString = request.getParameter("idCliente");
        if (idClienteString != null && !idClienteString.trim().equals("")) {
            idCliente = Integer.parseInt(idClienteString);
        }
        //Creación del objeto Cliente
        Cliente cliente = new clienteDaoJDBC().encontrar(new Cliente(idCliente));
        //Definición del alcance de la variable
        request.setAttribute("cliente", cliente);
        //Envio al jsp que presenta la información
        String jspEditar = "/WEB-INF/paginas/cliente/editarCliente.jsp";
        request.getRequestDispatcher(jspEditar).forward(request, response);
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, ServletException, IOException {
        int idCliente = 0;
        String idClienteString = request.getParameter("idCliente");
        if (idClienteString != null && !idClienteString.trim().equals("")) {
            idCliente = Integer.parseInt(idClienteString);
        } else {
            idCliente = 0;
        }
        int datosEliminados = new clienteDaoJDBC().eliminar(new Cliente(idCliente));
        System.out.println("datosEliminados = " + datosEliminados);
        this.accionDefault(request, response);
    }
}
