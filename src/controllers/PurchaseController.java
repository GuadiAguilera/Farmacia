package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.DynamicComboBox;
import models.*;
import static models.EmployeeDao.idUser;
import static models.EmployeeDao.rolUser;
import views.*;

public class PurchaseController implements KeyListener, ActionListener, MouseListener {

    private Purchase purchase;
    private PurchaseDao purchaseDao;
    private SystemView view;
    private int getIdSupplier = 0;
    private int item = 0;

    String rol = rolUser;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;
    //instanciar el modelo de producto
    Product product = new Product();
    ProductDao productDao = new ProductDao();

    public PurchaseController(Purchase purchase, PurchaseDao purchaseDao, SystemView view) {
        this.purchase = purchase;
        this.purchaseDao = purchaseDao;
        this.view = view;

        this.view.txtPurchaseProductCode.addKeyListener(this);
        this.view.txtPurchasePrice.addKeyListener(this);
        //boton de agregar
        this.view.btnAddProductToBuy.addActionListener(this);
        //boton comprar
        this.view.btnConfirmPurchase.addActionListener(this);
        //boton eliminar compra
        this.view.btnRemovePurchase.addActionListener(this);
        this.view.btnNewPurchase.addActionListener(this);

        this.view.lblReports.addMouseListener(this);
        this.view.lblPurchases.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.lblPurchases) {
            if (rol.equals("Administrador")) {
                view.jTabbedPane1.setSelectedIndex(1);
                cleanTable();
            } else {
                view.jTabbedPane1.setEnabledAt(1, false);
                view.lblPurchases.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tiene privilegios de administrador para acceder a esta vista");
            }
        } else if (e.getSource() == view.lblReports) {
            view.jTabbedPane1.setSelectedIndex(6);
            cleanTable();
            listAllPurchases();
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
        if (e.getSource() == view.txtPurchaseProductCode) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) { // si la persona presiona enter
                if (view.txtPurchaseProductCode.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Ingresa el codigo del producto a comprar");
                } else {
                    // cuando se ingresa el docigo del producto se rellenan automaticamente el nombre del producto y el id 
                    int id = Integer.parseInt(view.txtPurchaseProductCode.getText());
                    product = productDao.searchCode(id);
                    view.txtPurchaseProductName.setText(product.getName());
                    view.txtPurchaseId.setText("" + product.getId());
                    //ubica el cursor en la caja de texto conatidad

                    view.txtPurchaseAmount.requestFocus();

                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // se verifica si se introdujo o no una cantidad
        if (e.getSource() == view.txtPurchasePrice) {
            int quantity;
            double price = 0;
            if (view.txtPurchaseAmount.getText().equals("")) {
                quantity = 1;
                view.txtPurchasePrice.setText("" + price);
            } else {
                quantity = Integer.parseInt(view.txtPurchaseAmount.getText());
                price = Double.parseDouble(view.txtPurchasePrice.getText());
                view.txtPurchaseSubtotal.setText("" + quantity * price);

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnAddProductToBuy) {
            //obtener el id del proveedor
            DynamicComboBox supplierCmb = (DynamicComboBox) view.cmbPurchaseSupplier.getSelectedItem();
            //se asigna a supplierId lo que tiene supplierCmb, es decir, el id del supplier
            int supplierId = supplierCmb.getId();

            if (getIdSupplier == 0) {
                getIdSupplier = supplierId;
            } else {
                if (getIdSupplier != supplierId) { //el usuario seleccionó a dos proveedores. No permite hacer una compra a distintos proveedores
                    JOptionPane.showMessageDialog(null, "No puede realizar una misma compra a diferentes proveedores");
                } else {
                    int amount = Integer.parseInt(view.txtPurchaseAmount.getText());
                    String productName = view.txtPurchaseProductName.getText();
                    Double price = Double.parseDouble(view.txtPurchasePrice.getText());
                    int purchaseId = Integer.parseInt(view.txtPurchaseId.getText());
                    String supplierName = view.cmbPurchaseSupplier.getSelectedItem().toString();

                    if (amount > 0) {
                        temp = (DefaultTableModel) view.purchasesTable.getModel();
                        for (int i = 0; i < view.purchasesTable.getRowCount(); i++) {
                            if (view.purchasesTable.getValueAt(i, 1).equals(view.txtPurchaseProductName.getText())) {
                                JOptionPane.showMessageDialog(null, "El producto ya está registrado en la tabla de compras");
                                return;
                            }
                        }
                        ArrayList list = new ArrayList();
                        item = 1;
                        list.add(item);
                        list.add(purchaseId);
                        list.add(productName);
                        list.add(amount);
                        list.add(price);
                        list.add(amount * price);
                        list.add(supplierName);

                        Object[] obj = new Object[6];
                        obj[0] = list.get(1);
                        obj[1] = list.get(2);
                        obj[2] = list.get(3);
                        obj[3] = list.get(4);
                        obj[4] = list.get(5);
                        obj[5] = list.get(6);
                        temp.addRow(obj);
                        view.purchasesTable.setModel(temp);
                        cleanFieldsPurchases();
                        view.cmbPurchaseSupplier.setEditable(false);
                        view.txtPurchaseProductCode.requestFocus();
                        calculatePurchase();

                    }
                }
            }
            //registrar compra
        } else if (e.getSource() == view.btnConfirmPurchase) {
            insertPurchase();
            //eliminar la compra
        } else if (e.getSource() == view.btnRemovePurchase) {
            model = (DefaultTableModel) view.purchasesTable.getModel();
            model.removeRow(view.purchasesTable.getSelectedRow());
            calculatePurchase();
            view.txtPurchaseProductCode.requestFocus();
        } else if (e.getSource() == view.btnNewPurchase) {
            cleanTableTemp();
            cleanFieldsPurchases();

        }
    }

    //calcular total a pagar
    public void calculatePurchase() {
        double total = 0.0;
        int numRow = view.purchasesTable.getRowCount();

        for (int i = 0; i < numRow; i++) {
            //pasar el indice de la columna que se sumara
            total = total + Double.parseDouble(String.valueOf(view.purchasesTable.getValueAt(i, 4)));
        }

        view.txtPurchaseTotal.setText("" + total);
    }

    private void insertPurchase() {
        double total = Double.parseDouble(view.txtPurchaseTotal.getText());
        int employeeId = idUser;

        if (purchaseDao.registerPurchaseQuery(getIdSupplier, employeeId, total)) {
            int purchaseId = purchaseDao.purchaseId();
            int largo = view.purchasesTable.getRowCount();
            for (int i = 0; i < largo; i++) {
                int productId = Integer.parseInt(view.purchasesTable.getValueAt(i, 0).toString());
                int purchaseAmount = Integer.parseInt(view.purchasesTable.getValueAt(i, 2).toString());
                double purchasePrice = Double.parseDouble(view.purchasesTable.getValueAt(i, 3).toString());
                Double purchaseSubtotal = purchasePrice * purchaseAmount;

                //Registrar detalle de la compra
                purchaseDao.registerPurchaseDetailsQuery(purchaseId, purchasePrice, purchaseAmount, purchaseSubtotal, productId);

                //traer la cantidad de productos
                product = productDao.searchId(productId);
                int amount = product.getProductQuantity() + purchaseAmount;

                productDao.updateStockQuery(amount, productId);
            }
            cleanTableTemp(); //limpia la tabla temporal
            cleanFieldsPurchases();
            JOptionPane.showMessageDialog(null, "Compra generada con éxito");
            Print print = new Print(purchaseId);
            print.setVisible(true);
        }
    }

    //limpiar tabla temporal
    public void cleanTableTemp() {
        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);
            i = i - 1;
        }
    }

    public void listAllPurchases() {
        if (rol.equals("Administrador") || rol.equals("Auxiliar")) {
            List<Purchase> list = purchaseDao.listAllPurchaseQuery();
            model = (DefaultTableModel) view.allPurchasesTable.getModel();

            Object[] row = new Object[4];

            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getSupplierNameProduct();
                row[2] = list.get(i).getTotal();
                row[3] = list.get(i).getDateCreate();
                model.addRow(row);
            }
            view.allPurchasesTable.setModel(model);
        }
    }
    
    //limpiar campos
    public void cleanFieldsPurchases() {
        view.txtPurchaseProductCode.setText("");
        view.txtPurchaseProductName.setText("");
        view.txtPurchasePrice.setText("");
        view.txtPurchaseSubtotal.setText("");
        view.txtPurchaseId.setText("");
        view.txtPurchaseTotal.setText("");
        view.txtPurchaseAmount.setText("");
        
        view.purchasesTable.clearSelection();
    }

    // limpiar tabla
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

}
