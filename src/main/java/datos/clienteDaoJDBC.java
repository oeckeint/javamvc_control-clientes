package datos;

import dominio.Cliente;
import java.sql.*;
import java.util.*;

public class clienteDaoJDBC {

    private static final String SQL_SELECT = "SELECT id_cliente, nombre, apellido, email, telefono, saldo FROM cliente";
    private static final String SQL_SELECT_BY_ID = "SELECT id_cliente, nombre, apellido, email, telefono, saldo FROM cliente WHERE id_cliente = ?";
    private static final String SQL_DELETE = "DELETE FROM cliente WHERE id_cliente = ?";
    private static final String SQL_UPDATE = "UPDATE cliente SET nombre=?, apellido=?, email=?, telefono = ?, saldo=? WHERE id_cliente=?";
    private static final String SQL_INSERT = "INSERT INTO cliente(nombre, apellido, email, telefono, saldo) VALUES(?, ?, ? , ?, ?)";

    public List<Cliente> listar() throws ClassNotFoundException {
        //Inicializacion de variables de conexion
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //Inicializacion de un objeto y lista de objetos
        Cliente cliente = null;
        List<Cliente> clientes = new ArrayList<>();

        try {
            //Iniciar conexion
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            //Leer las consultas hasta que quede vacía
            while (rs.next()) {
                //Recuperar una consulta
                int idCliente = rs.getInt("id_cliente");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                double saldo = rs.getDouble("saldo");
                //Crear un objeto de tipo cliente
                cliente = new Cliente(idCliente, nombre, apellido, email, telefono, saldo);
                //Agregar el objeto a la lista de clientes
                clientes.add(cliente);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return clientes;
    }

    public Cliente encontrar(Cliente cliente) throws ClassNotFoundException {
        //Inicializacion de variables de conexion
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //Inicializacion de un objeto y lista de objetos

        try {
            //Iniciar conexion
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            //Se proporciona el entero que reemplaza la consulta
            stmt.setInt(1, cliente.getIdCliente());
            rs = stmt.executeQuery();
            //Nos posicionamos en un solo registro
            rs.absolute(1);
            //Recuperar una consulta
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            String email = rs.getString("email");
            String telefono = rs.getString("telefono");
            double saldo = rs.getDouble("saldo");
            //Agregar datos al objeto de tipo cliente ya creado
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setEmail(email);
            cliente.setTelefono(telefono);
            cliente.setSaldo(saldo);

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return cliente;
    }

    public int insertar(Cliente cliente) throws ClassNotFoundException {
        //Inicializacion de variables de conexion
        Connection conn = null;
        PreparedStatement stmt = null;
        //Inicializacion de la variable que indicara los registros afectados
        int rows = 0;
        try {
            //Iniciar conexion
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);

            //Se proporciona los parametros a insertar
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefono());
            stmt.setDouble(5, cliente.getSaldo());

            //Ejecución del query y asignación de los recursos afectados
            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }
    
    public int actualizar(Cliente cliente) throws ClassNotFoundException{
        //Inicializacion de variables de conexion
        Connection conn = null;
        PreparedStatement stmt = null;
        //Inicializacion de la variable que indicara los registros afectados
        int rows = 0;
        try {
            //Iniciar conexion
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);

            //Se proporciona los parametros a insertar
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefono());
            stmt.setDouble(5, cliente.getSaldo());
            stmt.setInt(6, cliente.getIdCliente());

            //Ejecución del query y asignación de los recursos afectados
            rows = stmt.executeUpdate();
            System.out.println("Se ejecuto una actuazlización");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }
    
    public int eliminar(Cliente cliente) throws ClassNotFoundException{
        //Inicializacion de variables de conexion
        Connection conn = null;
        PreparedStatement stmt = null;
        //Inicializacion de la variable que indicara los registros afectados
        int rows = 0;
        try {
            //Iniciar conexion
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);

            //Se proporciona los parametros a insertar
            stmt.setInt(1, cliente.getIdCliente());

            //Ejecución del query y asignación de los recursos afectados
            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }
}
