package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class PurchaseDao {

    //instanciar la conexion  
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //registrar compra
    public boolean registerPurchaseQuery(int supplierId, int employeeId, double total) {
        String query = "INSERT INTO purchase (idSupplier, idEmployee, total, dateCreate) VALUES(?,?,?,?) ";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, supplierId);
            pst.setInt(2, employeeId);
            pst.setDouble(3, total);
            pst.setTimestamp(4, datetime);
            pst.execute();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la compra");
            return false;
        }
    }

    //registrar detalles de la compra
    public boolean registerPurchaseDetailsQuery(int purchaseId, double purchasePrice, int purchaseAmount, double purchaseSubtotal, int productId) {
        String query = "INSERT INTO purchaseDetails (idPurchase, purchasePrice, purchaseAmount, purchaseSubtotal, productId) VALUES (?,?,?,?,?)";

        Timestamp datetime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, purchaseId);
            pst.setDouble(2, purchasePrice);
            pst.setInt(3, purchaseAmount);
            pst.setDouble(4, purchaseSubtotal);
            pst.setInt(5, productId);
            pst.execute();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar los detalles de la compra " + e.getMessage());
            return false;
        }

    }

    //obtener id de la compra
    public int purchaseId() {
        int id = 0;
        String query = "SELECT MAX(id) AS id FROM purchase";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return id;
    }

    //Listar todas las compras realizadas
    public List listAllPurchaseQuery() {
        List<Purchase> listPurchase = new ArrayList();
        String query = "SELECT pu.*, su.name AS supplierName "
                + "FROM purchase pu, supplier su "
                + "WHERE pu.idSupplier = su.id "
                + "ORDER BY pu.id ASC";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                Purchase purchase = new Purchase();
                purchase.setId(rs.getInt("id"));
                purchase.setSupplierNameProduct(rs.getString("supplierName"));
                purchase.setTotal(rs.getDouble("total"));
                purchase.setDateCreate(rs.getString("dateCreate"));
                listPurchase.add(purchase);
            }
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, e.getMessage());
        }
        return listPurchase;
    }
    
    //listar compras para imprimir en factura
    public List listPurchaseDetailQuery(int id) {
        List<Purchase> listPurchases = new ArrayList();
        String query = "SELECT pu.dateCreate, pude.purchasePrice, pude.purchaseAmount, pude.purchaseSubtotal, su.name As supplierName, pro.name AS productName, em.fullName FROM purchase pu INNER JOIN purchasedetails pude on pu.id = pude.idPurchase INNER JOIN product pro ON pude.productId = pro.id INNER JOIN supplier su ON su.id = pu.idSupplier INNER JOIN employee em ON pu.idEmployee = em.id WHERE pu.id = ?";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            while (rs.next()) {
                Purchase purchase = new Purchase();
                purchase.setProductName(rs.getString("productName"));
                purchase.setPurchaseAmount(rs.getInt("purchaseAmount"));
                purchase.setPurchasePrice(rs.getDouble("purchasePrice"));
                purchase.setPurchaseSubtotal(rs.getDouble("purchaseSubtotal"));
                purchase.setSupplierNameProduct(rs.getString("SupplierName"));
                purchase.setDateCreate(rs.getString("dateCreate"));
                purchase.setPurchase(rs.getString("fullName"));
                listPurchases.add(purchase);
            }
        } catch (SQLException e) {
            JOptionPane.showInternalMessageDialog(null, e.getMessage());
        }
        return listPurchases;
    }
}
