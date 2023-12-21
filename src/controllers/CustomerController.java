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
import models.*;
import views.SystemView;

public class CustomerController implements ActionListener, MouseListener, KeyListener {

    private Customer customer;
    private CustomerDao customerDao;
    private SystemView view;

    DefaultTableModel model = new DefaultTableModel();

    public CustomerController(Customer customer, CustomerDao customerDao, SystemView view) {
        this.customer = customer;
        this.customerDao = customerDao;
        this.view = view;
        //boton de registrar cliente
        this.view.btnRegisterCustomer.addActionListener(this);
        // boton modificar cliente
        this.view.btnUpdateCustomer.addActionListener(this);
        //boton eliminar cliente
        this.view.btnDeleteCustomer.addActionListener(this);
        //boton de cancelar
        this.view.btnCancelCustomer.addActionListener(this);
        //coloca la tabla en escucha
        this.view.customersTable.addMouseListener(this);
        //pone en escucha al buscador
        this.view.txtSearchCustomers.addKeyListener(this);
        //poner en escucha al label del menu 
        this.view.lblCustomers.addMouseListener(this);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Registrar
        if (e.getSource() == view.btnRegisterCustomer) {
            //verificar si los campos estan vacios
            if (view.txtCustomerId.getText().equals("")
                    || view.txtCustomerFullName.getText().equals("")
                    || view.txtCustomerAddress.getText().equals("")
                    || view.txtCustomerEmail.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                customer.setId(Integer.parseInt(view.txtCustomerId.getText().trim()));
                customer.setFullName(view.txtCustomerFullName.getText().trim());
                customer.setAddress(view.txtCustomerAddress.getText().trim());
                customer.setTelephone(view.txtCustomerTelephone.getText().trim());
                customer.setEmail(view.txtCustomerEmail.getText().trim());

                if (customerDao.registerCustomerQuery(customer)) {
                    cleanTable();
                    listAllCustomers();
                    JOptionPane.showMessageDialog(null, "Cliente registrado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar al cliente");
                }
            }
            // modificar cliente    
        } else if (e.getSource() == view.btnUpdateCustomer) {
            //verificar si los campos estan vacions
            if (view.txtCustomerId.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            } else {
                if (view.txtCustomerId.getText().equals("") || view.txtCustomerFullName.getText().equals("")
                        || view.txtCustomerAddress.getText().equals("") || view.txtCustomerTelephone.getText().equals("")
                        || view.txtCustomerEmail.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    customer.setId(Integer.parseInt(view.txtCustomerId.getText().trim()));
                    customer.setFullName(view.txtCustomerFullName.getText().trim());
                    customer.setAddress(view.txtCustomerAddress.getText().trim());
                    customer.setTelephone(view.txtCustomerTelephone.getText().trim());
                    customer.setEmail(view.txtCustomerEmail.getText().trim());
                    
                    if (customerDao.updateCustomerQuery(customer)) {
                        cleanTable();
                        cleanFields();
                        listAllCustomers();
                        view.btnRegisterCustomer.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del cliente modificados con éxito");
                    } else{
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar los datos del cliente");
                    }
                }
            }
        // eliminar cliente    
        }else if (e.getSource() == view.btnDeleteCustomer) {
            int row = view.customersTable.getSelectedRow();
            
            if (row == -1) { // si la persona no seleccionó nada
            JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente para eliminar");
            }else{
                int id = Integer.parseInt(view.customersTable.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar a este Cliente?");
                
                if (question == 0 && customerDao.deleteCustomerQuery(id) != false) {
                    cleanFields();
                    cleanTable();
                    view.btnRegisterCustomer.setEnabled(true);
                    listAllCustomers();
                    JOptionPane.showMessageDialog(null, "Cliente eliminado con éxito");
                }    
            }
        // cancelar empleado    
        }else if (e.getSource() == view.btnCancelCustomer) {
            view.btnRegisterCustomer.setEnabled(true);
            cleanFields();
            
        }
      
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.customersTable) {
            int row = view.customersTable.rowAtPoint(e.getPoint()); //para saber donde se hizo click

            //Asignar la info de cada columna y cada fila a las cajas de texto
            view.txtCustomerId.setText(view.customersTable.getValueAt(row, 0).toString()); //lo que va adentro de getValueAt es la posicion de la fila y la posicion de la columna
            view.txtCustomerFullName.setText(view.customersTable.getValueAt(row, 1).toString());
            view.txtCustomerAddress.setText(view.customersTable.getValueAt(row, 2).toString());
            view.txtCustomerTelephone.setText(view.customersTable.getValueAt(row, 3).toString());
            view.txtCustomerEmail.setText(view.customersTable.getValueAt(row, 4).toString());

            //deshabilitar botones
            view.btnRegisterCustomer.setEnabled(false);
            view.txtCustomerId.setEditable(false);
        //acceder a la ventana por el menu del costado
        }else if (e.getSource() == view.lblCustomers) {
            view.jTabbedPane1.setSelectedIndex(2);
            cleanFields();
            cleanTable();
            listAllCustomers();
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
        if (e.getSource() == view.txtSearchCustomers) {
            //limpiarTabla
            cleanTable();
            //listar clientes
            listAllCustomers();

        }
    }
    
        //listar clientes
    public void listAllCustomers() {
        List<Customer> list = customerDao.listCustomerQuery(view.txtSearchCustomers.getText());
        model = (DefaultTableModel) view.customersTable.getModel(); //se definen la tabla donde estan los registros de la tabla clientes

        Object[] row = new Object[5]; // 5 es la cantidad de columnas de la tabla en la vista
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getFullName();
            row[2] = list.get(i).getAddress();
            row[3] = list.get(i).getTelephone();
            row[4] = list.get(i).getEmail();
            model.addRow(row);  // se agregan todas las filas  
        }
        view.customersTable.setModel(model); //Se pasan todos los datos a la tabla
    }

    //limpiar la tabla
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }
    
    // limpiar los campos de texto
    public void cleanFields(){
        view.txtCustomerId.setText("");
        view.txtEmployeeId.setEditable(true);
        view.txtCustomerFullName.setText("");
        view.txtCustomerAddress.setText("");
        view.txtCustomerTelephone.setText("");
        view.txtCustomerEmail.setText("");
        
        view.customersTable.clearSelection();
    }
}
