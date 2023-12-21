package models;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class SaleDao {

    //instanciar la conexion  
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //Registrar una venta
    public boolean registerSaleQuery(int customerId, int employeeId, double total) {

        String query = "INSERT INTO sales (customerId, employeeId,  total, saleDate) VALUES (?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, customerId);
            pst.setInt(2, employeeId);
            pst.setDouble(3, total);
            pst.setTimestamp(4, datetime);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }

    //Registrar detalles de la venta
    public boolean registerSaleDetailQuery(int productId, double saleId,
            int saleQuantity, double salePrice, double saleSubtotal) {

        String query = "INSERT INTO sale_details (productId, saleId, saleQuantity, salePrice, saleSubtotal) VALUES (?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, productId);
            pst.setDouble(2, saleId);
            pst.setInt(3, saleQuantity);
            pst.setDouble(4, salePrice);
            pst.setDouble(5, saleSubtotal);
            pst.execute();

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }
    }

    //Obtener id maximo de la venta
    public int saleId() {

        int id = 0;
        String query = "SELECT MAX(id) AS id FROM sales";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    //Listar ventas realizadas
    public List listAllSalesQuery() {

        List<Sale> listSales = new ArrayList();

        String query = "SELECT s.id AS invoice, c.fullName AS customer, e.fullName AS employee, s.total, s.saleDate "
                + "FROM sale s INNER JOIN customer c ON s.customerId = c.id INNER JOIN employee e ON s.employeeId = e.id "
                + "ORDER BY d.id ASC";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            //Recorrer 
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setId(rs.getInt("invoice"));
                sale.setCustomerName(rs.getString("customer"));
                sale.setEmployeeName(rs.getString("employee"));
                sale.setTotalToPay(rs.getDouble("total"));
                sale.setSaleDate(rs.getString("saleDate"));

                listSales.add(sale);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return listSales;
    }

}
