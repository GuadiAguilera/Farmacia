package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {
    private String databaseName = "pharmacy_database";
    private String user = "root";
    private String password = "root";
    private String url = "jdbc:mysql://localhost:3306/" + databaseName; //se realiza la conexion
    Connection conn = null;
    
    public Connection getConnection(){
        try {
            //Obtener valor del driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,user, password);
        } catch (ClassNotFoundException e) {
            System.err.println("Ha ocurrido un ClassNotFoundException " + e.getMessage());
        } catch(SQLException e){
            System.err.println("Ha ocurrido un SQLExcption" + e.getMessage());
        }
             
        return conn;
    }
}

