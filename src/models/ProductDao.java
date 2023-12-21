package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductDao {

    //instanciar la conexion  
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //registrar producto
    public boolean registerProductQuery(Product product) {
        String query = "INSERT INTO product (code, name, description, unitPrice, dateCreate, dateUpdate, categoryId)"
                + "VALUES (?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1,product.getCode());
            pst.setString(2,product.getName());
            pst.setString(3, product.getDescription());
            pst.setDouble(4,product.getUnitPrice());
            pst.setTimestamp(5, datetime);
            pst.setTimestamp(6, datetime);
            pst.setInt(7, product.getCategoryId());
            
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el producto" + e.getMessage());
            return false;
        }

    }

    //listar productos
    public List listProductQuery(String value) {
        List<Product> listProducts = new ArrayList();
        String query = "SELECT pro.*, ca.name AS categoryName "
                + "FROM product pro, category ca "
                + "WHERE pro.categoryId = ca.id";
        String querySearchProduct = "SELECT pro.*, ca.name AS categoryName "
                + "FROM product pro INNER JOIN category ca ON pro.categoryId = ca.id "
                + "WHERE pro.name LIKE '%" + value + "%'";

        try {
            conn = cn.getConnection();
            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(querySearchProduct);
                rs = pst.executeQuery();
            }

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setCode(rs.getInt("code"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnitPrice(rs.getDouble("unitPrice"));
                product.setProductQuantity(rs.getInt("ProductQuantity"));
                product.setCategoryName(rs.getString("categoryName")); //almacena el nombre de la categoria obtenida en la consulta
                listProducts.add(product);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return listProducts;
    }

    //modificar producto
    public boolean updateProductQuery(Product product) {
        String query = "UPDATE  product SET code = ?, name =?, description =? , unitPrice =?, dateUpdate = ?, categoryId =? "
                + "WHERE id = ?";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, product.getCode());
            pst.setString(2, product.getName());
            pst.setString(3, product.getDescription());
            pst.setDouble(4, product.getUnitPrice());
            pst.setTimestamp(5, datetime);
            pst.setInt(6, product.getCategoryId());
            pst.setInt(7, product.getId());
            pst.execute();

            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los datos del producto");
            return false;
        }
    }

    //eliminar producto
    public boolean deleteProductQuery(int id) {
        String query = "DELETE FROM product WHERE id = " + id;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede eliminar un producto que tenga relación con otra tabla");
            return false;
        }
    }

    //buscar producto
    public Product searchProduct(int id) {
        String query = "SELECT pro.*, ca.name AS categoryName "
                + "FROM product pro INNER JOIN category ca ON pro.categoryId=ca.id "
                + "WHERE pro.id =?";

        Product product = new Product();

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                product.setId(rs.getInt("id"));
                product.setCode(rs.getInt("code"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setUnitPrice(rs.getDouble("unitPrice"));
                product.setCategoryId(rs.getInt("categoryId"));
                product.setCategoryName(rs.getString("categoryName"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }

    //Buscar producto por codigo
    public Product searchCode(int code) {
        String query = "SELECT pro.id, pro.name FROM product pro WHERE code = ?"; //ddevuelve el id y nombre del producto que corresponda con cierto codigo

        Product product = new Product();

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, code);
            rs = pst.executeQuery();

            if (rs.next()) {
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }

    //traer cantidad de productos
    public Product searchId(int id) {
        String query = "SELECT productQuantity FROM product WHERE id = ?";

        Product product = new Product();

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                product.setProductQuantity(rs.getInt("productQuantity"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }

    //actualizar stock
    public boolean updateStockQuery(int amount, int productId) {
        String query = "UPDATE product SET productQuantity =? WHERE id = ?";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, amount);
            pst.setInt(2, productId);
            pst.execute();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

            return false;
        }
    }
    
    //metodo para listar las compras realizadas
    
}
