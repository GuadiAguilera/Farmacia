package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Category;
import models.DynamicComboBox;
import static models.EmployeeDao.rolUser;
import models.Supplier;
import models.SupplierDao;
import views.SystemView;

public class SupplierController implements ActionListener, MouseListener, KeyListener {

    private Supplier supplier;
    private SupplierDao supplierDao;
    private SystemView view;

    String rol = rolUser;

    DefaultTableModel model = new DefaultTableModel();

    public SupplierController(Supplier supplier, SupplierDao supplierDao, SystemView view) {
        this.supplier = supplier;
        this.supplierDao = supplierDao;
        this.view = view;
        // boton registar proveedor
        this.view.btnRegisterSupplier.addActionListener(this);
        // boton modificar proveedor
        this.view.btnUpdateSupplier.addActionListener(this);
        //boton eliminar proveedor
        this.view.btnDeleteSupplier.addActionListener(this);
        // Boton cancelar proveedor
        this.view.btnCancelSupplier.addActionListener(this);
        //colocar la tabla en escucha
        this.view.supplierTable.addMouseListener(this);
        //colocar el buscador en escucha
        this.view.txtSearchSupplier.addKeyListener(this);
        // pone a la escucha al label del menu
        this.view.lblSuppliers.addMouseListener(this);
        
        getSupplierName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //registrar
        if (e.getSource() == view.btnRegisterSupplier) {
            if (view.txtSupplierName.getText().equals("")
                    || view.txtSupplierDescription.getText().equals("")
                    || view.txtSupplierAddress.getText().equals("")
                    || view.txtSupplierTelephone.getText().equals("")
                    || view.txtSupplierEmail.getText().equals("")
                    || view.cmbSupplierCity.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                //realizar inserccion
                supplier.setName(view.txtSupplierName.getText().trim());
                supplier.setDescription(view.txtSupplierDescription.getText().trim());
                supplier.setAddress(view.txtSupplierAddress.getText().trim());
                supplier.setTelephone(view.txtSupplierTelephone.getText().trim());
                supplier.setEmail(view.txtSupplierEmail.getText().trim());
                supplier.setCity(view.cmbSupplierCity.getSelectedItem().toString());

                if (supplierDao.registerSupplierQuery(supplier)) {
                    cleanTable();
                    cleanFields();
                    listAllSupplier();
                    JOptionPane.showMessageDialog(null, "Proveedor registrado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar al proveedor");
                }
            }
            // modificar    
        } else if (e.getSource() == view.btnUpdateSupplier) {
            if (view.txtSupplierId.equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            } else {
                if (view.txtSupplierName.getText().equals("")
                        || view.txtSupplierAddress.getText().equals("")
                        || view.txtSupplierTelephone.getText().equals("")
                        || view.txtSupplierEmail.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    //realizar inserccion
                    supplier.setName(view.txtSupplierName.getText().trim());
                    supplier.setDescription(view.txtSupplierDescription.getText().trim());
                    supplier.setAddress(view.txtSupplierAddress.getText().trim());
                    supplier.setTelephone(view.txtSupplierTelephone.getText().trim());
                    supplier.setEmail(view.txtSupplierEmail.getText().trim());
                    supplier.setCity(view.cmbSupplierCity.getSelectedItem().toString());
                    supplier.setId(Integer.parseInt(view.txtSupplierId.getText()));
                    if (supplierDao.updateSupplierQuery(supplier)) {
                        //limpiarTabla
                        cleanTable();
                        //limpiar campos
                        cleanFields();
                        //listar proveedores
                        listAllSupplier();
                        view.btnRegisterSupplier.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del proveedor modificados con éxito");
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar los datos del proveedor");
                    }
                }
            }
            //eliminar
        } else if (e.getSource() == view.btnDeleteSupplier) {
            int row = view.supplierTable.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un proveedor para eliminar");
            } else {
                int id = Integer.parseInt(view.supplierTable.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar este proveedor?");
                if (question == 0 && supplierDao.deleteSupplierQuery(id) != false) {
                    //limpiar tabla
                    cleanTable();
                    //limpiar campos
                    cleanFields();
                    //listar proveedores
                    listAllSupplier();
                    JOptionPane.showMessageDialog(null, "Proveedor eliminado con exito");
                }
            }
            // cancelar
        } else if (e.getSource() == view.btnCancelSupplier) {
            cleanFields();
            view.btnRegisterSupplier.setEnabled(true);
        }
    }

    //listar proveedores
    public void listAllSupplier() {
        if (rol.equals("Administrador")) {
            List<Supplier> list = supplierDao.listSupplierQuery(view.txtSearchSupplier.getText());
            model = (DefaultTableModel) view.supplierTable.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();
                row[2] = list.get(i).getDescription();
                row[3] = list.get(i).getAddress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getCity();
                model.addRow(row);
            }
            view.supplierTable.setModel(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.supplierTable) {
            int row = view.supplierTable.rowAtPoint(e.getPoint());
            view.txtSupplierId.setText(view.supplierTable.getValueAt(row, 0).toString());
            view.txtSupplierName.setText(view.supplierTable.getValueAt(row, 1).toString());
            view.txtSupplierDescription.setText(view.supplierTable.getValueAt(row, 2).toString());
            view.txtSupplierAddress.setText(view.supplierTable.getValueAt(row, 3).toString());
            view.txtSupplierTelephone.setText(view.supplierTable.getValueAt(row, 4).toString());
            view.txtSupplierEmail.setText(view.supplierTable.getValueAt(row, 5).toString());
            view.cmbSupplierCity.setSelectedItem(view.supplierTable.getValueAt(row, 6).toString());

            //deshabilitar botones
            view.btnRegisterSupplier.setEnabled(false);
            view.txtSupplierId.setEditable(false);

        } else if (e.getSource() == view.lblSuppliers) {
            if (rol.equals("Administrador")) {
                view.jTabbedPane1.setSelectedIndex(4);
                //limpiar tabla
                cleanTable();
                cleanFields();
                listAllSupplier();
            } else {
                view.jTabbedPane1.setEnabledAt(4, false);
                view.lblSuppliers.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tienes privilegios de Administrador para acceder a esta vista");
            }
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
        if (e.getSource() == view.txtSearchSupplier) {
            //limpiar tabla
            cleanTable();
            //listar proveedores
            listAllSupplier();
        }
    }

    //limpiar tabla
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    public void cleanFields() {
        view.txtSupplierId.setText("");
        view.txtSupplierId.setEditable(true);
        view.txtSupplierName.setText("");
        view.txtSupplierDescription.setText("");
        view.txtSupplierAddress.setText("");
        view.txtSupplierTelephone.setText("");
        view.txtSupplierEmail.setText("");
        view.cmbSupplierCity.setSelectedIndex(0);
        
        view.supplierTable.clearSelection();
    }

    // metodo para mostrar el nombre del Proveedor
    public void getSupplierName() {
        List<Supplier> list = supplierDao.listSupplierQuery(view.txtSearchSupplier.getText());

        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            view.cmbPurchaseSupplier.addItem(new DynamicComboBox(id, name));
        }
    }

}
