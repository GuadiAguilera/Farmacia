//todos los métodos que permiten a java interactuar con mySQL
package models;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeDao {

//instanciar la conexion  
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //Variables para enviar datos entre interfaces
    public static int idUser = 0;
    public static String fullNameUser = "";
    public static String usernameUser = "";
    public static String addressUser = "";
    public static String telephoneUser = "";
    public static String emailUser = "";
    public static String rolUser = "";

    //método de login
    public Employee loginQuery(String user, String password) {
        String query = "SELECT * FROM employee WHERE username = ? AND password = ?";
        Employee employee = new Employee();
        try {
            conn = cn.getConnection(); //llama a la conexion
            pst = conn.prepareStatement(query);

            //enviar parámetros 
            pst.setString(1, user);
            pst.setString(2, password);

            rs = pst.executeQuery();

            //compara que los datos ingresados por el usuario coincidan
            if (rs.next()) {
                employee.setId(rs.getInt("id"));
                idUser = employee.getId();
                employee.setFullName(rs.getString("fullName"));
                fullNameUser = employee.getFullName();
                employee.setUsername(rs.getString("username"));
                usernameUser = employee.getUsername();
                employee.setAddress(rs.getString("address"));
                addressUser = employee.getAddress();
                employee.setTelephone(rs.getString("telephone"));
                telephoneUser = employee.getTelephone();
                employee.setEmail(rs.getString("email"));
                emailUser = employee.getEmail();
                employee.setRol(rs.getString("rol"));
                rolUser = employee.getRol();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener al empleado" + e);
        }
        return employee;
    }

    //registrar empleados
    public boolean registerEmployeeQuery(Employee employee) {
        String query = "INSERT INTO employee(id, fullName, username, address,telephone, email, password, rol, dateCreate, dateUpdate)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?)"; // Cada ? hace referencia a un campo de la base de datos, con datos que va a ingresar el usuario 
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            //accede a los métodos setter del empleado para enviar los datos que se van a guardar en la base de datos
            //para create y update se envia la variable datetime
            pst.setInt(1, employee.getId());
            pst.setString(2, employee.getFullName());
            pst.setString(3, employee.getUsername());
            pst.setString(4, employee.getAddress());
            pst.setString(5, employee.getTelephone());
            pst.setString(6, employee.getEmail());
            pst.setString(7, employee.getPassword());
            pst.setString(8, employee.getRol());
            pst.setTimestamp(9, datetime);
            pst.setTimestamp(10, datetime);

            pst.execute();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el empleado" + e);
            return false;
        }
    }

    // método para listar empleados
    public List listEmployeesQuery(String value) {
        List<Employee> listEmployee = new ArrayList();
        String query = "SELECT * FROM employee ORDER BY rol ASC";
        String querySearchEmployee = "SELECT * FROM employee WHERE id LIKE '%" + value + "%'"; //Busca el empleado con la identificacion pasada en la caja de texto

        try {
            conn = cn.getConnection();
            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(querySearchEmployee);
                rs = pst.executeQuery();
            }

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getInt("id"));
                employee.setFullName(rs.getString("fullName"));
                employee.setUsername(rs.getString("username"));
                employee.setAddress(rs.getString("address"));
                employee.setTelephone(rs.getString("telephone"));
                employee.setEmail(rs.getString("email"));
                employee.setRol(rs.getString("rol"));
                listEmployee.add(employee);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return listEmployee;

    }

    //modificar empleado
    public boolean updateEmployeeQuery(Employee employee) {
        String query = "UPDATE employee SET fullName = ?, username = ?, address = ?,telephone = ?, email = ?, rol = ?, dateUpdate = ?"
                + "WHERE id = ?";

        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setString(1, employee.getFullName());
            pst.setString(2, employee.getUsername());
            pst.setString(3, employee.getAddress());
            pst.setString(4, employee.getTelephone());
            pst.setString(5, employee.getEmail());
            pst.setString(6, employee.getRol());
            pst.setTimestamp(7, datetime);
            pst.setInt(8, employee.getId());

            pst.execute();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los datos del empleado" + e);
            return false;
        }
    }

    //eliminar empleado
    public boolean deleteEmployeeQuery(int id) {
        String query = "DELETE FROM employee WHERE id = " + id;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede eliminar un empleado que tenga relación con otra tabla");
            return false;
        }
    }

    public boolean updateEmployePassword(Employee employee) {
        String query = "UPDATE employee SET password = ? WHERE username = '" + usernameUser + "'";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, employee.getPassword());
            pst.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar la contraseña" + e);
            return false;
        }
    }

}
