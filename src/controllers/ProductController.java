package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.CategoryDao;
import models.DynamicComboBox;
import static models.EmployeeDao.rolUser;
import models.Product;
import models.ProductDao;
import views.SystemView;

public class ProductController implements ActionListener, MouseListener, KeyListener {

    private Product product;
    private ProductDao productDao;
    private SystemView view;
    private CategoryController categoryController;

    String rol = rolUser;
    DefaultTableModel model = new DefaultTableModel();

    public ProductController(Product product, ProductDao procuctDao, SystemView view) {
        this.product = product;
        this.productDao = procuctDao;
        this.view = view;

        this.view.btnRegisterProduct.addActionListener(this);
        this.view.btnUpdateProduct.addActionListener(this);
        this.view.btnDeleteProduct.addActionListener(this);
        this.view.btnCancelProduct.addActionListener(this);

        this.view.productTable.addMouseListener(this);
        this.view.txtSearchProduct.addKeyListener(this);
        this.view.lblProducts.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        categoryController.getCategoryName();
        //registrar
        if (e.getSource() == view.btnRegisterProduct) {
            if (view.txtProductCode.getText().equals("")
                    || view.txtProductName.getText().equals("")
                    || view.txtProductDescription.getText().equals("")
                    || view.txtProductUnitPrice.getText().equals("")
                    || view.cmbProductCategory.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                product.setCode(Integer.parseInt(view.txtProductCode.getText()));
                product.setName(view.txtProductName.getText().trim());
                product.setDescription(view.txtProductDescription.getText().trim());
                product.setUnitPrice(Double.parseDouble(view.txtProductUnitPrice.getText()));
                //registrar el id del nombre del comboBox
                DynamicComboBox categoryId = (DynamicComboBox) view.cmbProductCategory.getSelectedItem();
                product.setCategoryId(categoryId.getId());

                if (productDao.registerProductQuery(product)) {
                    cleanTable();
                    cleanFields();
                    listAllProducts();
                    JOptionPane.showMessageDialog(null, "Producto registrado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el producto");
                }
            }
        } else if (e.getSource() == view.btnUpdateProduct) {
            if (view.txtProductCode.getText().equals("")
                    || view.txtProductName.getText().equals("")
                    || view.txtProductDescription.getText().equals("")
                    || view.txtProductUnitPrice.getText().equals("")
                    || view.cmbProductCategory.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                product.setCode(Integer.parseInt(view.txtProductCode.getText()));
                product.setName(view.txtProductName.getText().trim());
                product.setDescription(view.txtProductDescription.getText().trim());
                product.setUnitPrice(Double.parseDouble(view.txtProductUnitPrice.getText()));
                //registrar el id del nombre del comboBox de la categoria
                DynamicComboBox categoryId = (DynamicComboBox) view.cmbProductCategory.getSelectedItem();
                product.setCategoryId(categoryId.getId());

                //pasar id al metodo
                product.setId(Integer.parseInt(view.txtProductId.getText()));

                if (productDao.updateProductQuery(product)) {
                    cleanTable();
                    cleanFields();
                    listAllProducts();
                    JOptionPane.showMessageDialog(null, "Datos del producto modificados con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar los datos del producto");
                }
            }
        } else if (e.getSource() == view.btnDeleteProduct) {
            int row = view.productTable.getSelectedRow();

            if (row == -1) { // si la persona no seleccionó nada
                JOptionPane.showMessageDialog(null, "Debes seleccionar un producto para eliminar");
            } else {
                int id = Integer.parseInt(view.productTable.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar a este producto?");

                if (question == 0 && productDao.deleteProductQuery(id) != false) {
                    cleanFields();
                    cleanTable();
                    view.btnRegisterProduct.setEnabled(true);
                    listAllProducts();
                    JOptionPane.showMessageDialog(null, "Producto eliminado con éxito");
                }
            }
        } else if (e.getSource() == view.btnCancelProduct) {
            cleanFields();
            view.btnRegisterProduct.setEnabled(true); 
        }
    }

    //listar productos
    public void listAllProducts() {
        if (rol.equals("Administrador") || rol.equals("Auxiliar")) {
            List<Product> list = productDao.listProductQuery(view.txtSearchProduct.getText());
            model = (DefaultTableModel) view.productTable.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getCode();
                row[2] = list.get(i).getName();
                row[3] = list.get(i).getDescription();
                row[4] = list.get(i).getUnitPrice();
                row[5] = list.get(i).getProductQuantity();
                row[6] = list.get(i).getCategoryName();
                model.addRow(row);
            }
            view.productTable.setModel(model);

            if (rol.equals("Auxiliar")) {
                view.btnRegisterProduct.setEnabled(false);
                view.btnUpdateProduct.setEnabled(false);
                view.btnDeleteProduct.setEnabled(false);
                view.btnCancelProduct.setEnabled(false);

                view.txtProductCode.setEnabled(false);
                view.txtProductDescription.setEnabled(false);
                view.txtProductId.setEditable(false);
                view.txtProductName.setEditable(false);
                view.txtProductUnitPrice.setEditable(false);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        categoryController.getCategoryName();
        if (e.getSource() == view.productTable) {
            int row = view.productTable.rowAtPoint(e.getPoint());
            view.txtProductId.setText(view.productTable.getValueAt(row, 0).toString());

            //buscar el nombre de la categoria
            product = productDao.searchProduct(Integer.parseInt(view.txtProductId.getText()));
            // se obtiene desde el modelo donde se define la relacion entre productos y categoria
            view.txtProductCode.setText("" + product.getCode());
            view.txtProductName.setText(product.getName());
            view.txtProductDescription.setText(product.getDescription());
            view.txtProductUnitPrice.setText("" + product.getUnitPrice());
            view.cmbProductCategory.setSelectedItem(new DynamicComboBox(product.getCategoryId(), product.getCategoryName()));

            // Deshabilitar
            view.btnRegisterProduct.setEnabled(false);
        }else if (e.getSource() == view.lblProducts) {
            view.jTabbedPane1.setSelectedIndex(0);
            cleanFields();
            cleanTable();
            listAllProducts();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == view.txtSearchProduct) {
            cleanTable();
            listAllProducts();
        }
    }

    //limpiar la tabla
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    public void cleanFields() {
        view.txtProductId.setText("");
        view.txtProductCode.setText("");
        view.txtProductDescription.setText("");
        view.txtProductName.setText("");
        view.txtProductUnitPrice.setText("");
        
        view.productTable.clearSelection();
    }
}
