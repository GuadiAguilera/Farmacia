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
import models.Employee;
import models.EmployeeDao;
import static models.EmployeeDao.idUser;
import static models.EmployeeDao.rolUser;
import views.SystemView;

public class EmployeeController implements ActionListener,MouseListener, KeyListener {
  private Employee employee;
    private EmployeeDao employeeDao;
    private SystemView view;
    
    //rol
    String rol = rolUser;
    DefaultTableModel model = new DefaultTableModel(); //para interactuar con la tabla
            
    public EmployeeController(Employee employee, EmployeeDao employeeDao, SystemView view) {
        this.employee = employee;
        this.employeeDao = employeeDao;
        this.view = view;
        // boton de registrar empleado
        this.view.btnRegisterEmployee.addActionListener(this);
        this.view.employeeTable.addMouseListener(this);
        this.view.txtSearchEmployee.addKeyListener(this);
        //boton de modificar empleados
        this.view.btnUpdateEmployee.addActionListener(this);
        //boton de elimiar empleado
        this.view.btnDeleteEmployee.addActionListener(this);
        //boton cancelar
        this.view.btnCancelEmployee.addActionListener(this);
        //boton cambiar contraseña
        this.view.btnModifyData.addActionListener(this);
        //colocar label en escucha
        this.view.lblEmployees.addMouseListener(this);  
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //registro del empleado
        if (e.getSource() == view.btnRegisterEmployee) {
            //verificar si los campos estan vacios
            if (view.txtEmployeeId.getText().equals("") 
                    || view.txtEmployeeFullName.getText().equals("")
                    || view.txtEmployeeUsername.getText().equals("")
                    || view.txtEmployeeAddress.getText().equals("")
                    || view.txtEmployeeTelephone.getText().equals("")
                    || view.txtEmployeeEmail.getText().equals("")
                    || view.cmbRol.getSelectedItem().toString().equals("")
                    || String.valueOf(view.txtEmployeePassword.getPassword()).equals(""))
            {
               JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios"); 
            }else{
                // realizar la inserccion
                employee.setId(Integer.parseInt(view.txtEmployeeId.getText().trim()));
                employee.setFullName(view.txtEmployeeFullName.getText().trim());
                employee.setUsername(view.txtEmployeeUsername.getText().trim());
                employee.setAddress(view.txtEmployeeAddress.getText().trim());
                employee.setTelephone(view.txtEmployeeTelephone.getText().trim());
                employee.setEmail(view.txtEmployeeEmail.getText().trim());
                employee.setPassword(String.valueOf(view.txtEmployeePassword.getPassword()));
                employee.setRol(view.cmbRol.getSelectedItem().toString());
                
                if (employeeDao.registerEmployeeQuery(employee)) {
                    cleanTable();
                    cleanFields();
                    listAllEmployees();
                    JOptionPane.showMessageDialog(null, "Empleado registrado con exito");
                }else{
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el empleado");
                }
            }  
        //Modificacion del empleado
        } else if (e.getSource() == view.btnUpdateEmployee) {
            if (view.txtEmployeeId.equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            }else{
               //verificar si los campos estan vacios
                if (view.txtEmployeeId.getText().equals("") 
                        || view.txtEmployeeFullName.getText().equals("") 
                    || view.cmbRol.getSelectedItem().toString().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                }else{
                    employee.setId(Integer.parseInt(view.txtEmployeeId.getText().trim()));
                    employee.setFullName(view.txtEmployeeFullName.getText().trim());
                    employee.setUsername(view.txtEmployeeUsername.getText().trim());
                    employee.setAddress(view.txtEmployeeAddress.getText().trim());
                    employee.setTelephone(view.txtEmployeeTelephone.getText().trim());
                    employee.setEmail(view.txtEmployeeEmail.getText().trim());
                    employee.setPassword(String.valueOf(view.txtEmployeePassword.getPassword()));
                    employee.setRol(view.cmbRol.getSelectedItem().toString());
                    
                    if (employeeDao.updateEmployeeQuery(employee)) {
                        cleanTable();
                        cleanFields();
                        listAllEmployees();
                        view.btnRegisterEmployee.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del empleado modificados con exito");
                    }else{
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar al empleado");
                    }
                }
            }
        // eliminar empleado
        } else if (e.getSource() == view.btnDeleteEmployee) {
            int row = view.employeeTable.getSelectedRow();
            
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar un empleado para eliminar");
            }else if (view.employeeTable.getValueAt(row,0).equals(idUser)) {
                JOptionPane.showMessageDialog(null, "No puede eliminar al usuario autenticado");
            }else{
                int id = Integer.parseInt(view.employeeTable.getValueAt(row,0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar a este empleado?");
                
                if (question == 0 && employeeDao.deleteEmployeeQuery(id) != false) {
                    cleanFields();
                    cleanTable();
                    view.btnRegisterEmployee.setEnabled(true);
                    view.txtEmployeePassword.setEnabled(true);
                    listAllEmployees();
                    JOptionPane.showMessageDialog(null, "Empleado eliminado con éxito");
                }
            }
        
        // cancelar empleado
        }else if (e.getSource() == view.btnCancelEmployee) {
            cleanFields();
            //view.btnDeleteEmploye.setEnabled(false);
            view.btnRegisterEmployee.setEnabled(true);
            view.txtEmployeePassword.setEnabled(true);
            view.txtEmployeeId.setVisible(true);
            
        // modificar empleado    
        }else if (e.getSource() == view.btnModifyData) {
            //recolectar informacion de las cajas password
            String password = String.valueOf(view.txtProfilePassword.getPassword());
            String confirmPassword = String.valueOf(view.txtProfilePasswordModify.getPassword());
            
            //verificar que las cajas de textos estan vacias
            if (!password.equals("") && !confirmPassword.equals("")) {
              //verificar que las contraseñas sean iguales  
                if (password.equals(confirmPassword)) {
                    employee.setPassword(String.valueOf(view.txtProfilePasswordModify.getPassword()));
                    
                    if (employeeDao.updateEmployePassword(employee) != false) {
                        JOptionPane.showMessageDialog(null, "Contraseña modificada con exito");
                    }else{
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar la contraseña");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
                }
            }else{
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            }
        }       
    }
    
    //listar todos los empleados
    public void listAllEmployees(){
        if (rol.equals("Administrador")) {
            view.btnDeleteEmployee.setVisible(true);
            List<Employee> list = employeeDao.listEmployeesQuery(view.txtSearchEmployee.getText());
            model = (DefaultTableModel) view.employeeTable.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getFullName();
                row[2] = list.get(i).getUsername();
                row[3] = list.get(i).getAddress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getRol();
                model.addRow(row);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
        if (e.getSource() == view.employeeTable) {
            int row = view.employeeTable.rowAtPoint(e.getPoint()); // para saber en que fila se hizo click
            
            //Para llenar automaticamente los campos
            view.txtEmployeeId.setText(view.employeeTable.getValueAt(row,0).toString());
            view.txtEmployeeFullName.setText(view.employeeTable.getValueAt(row,1).toString());
            view.txtEmployeeUsername.setText(view.employeeTable.getValueAt(row,2).toString());
            view.txtEmployeeAddress.setText(view.employeeTable.getValueAt(row,3).toString());
            view.txtEmployeeTelephone.setText(view.employeeTable.getValueAt(row,4).toString());
            view.txtEmployeeEmail.setText(view.employeeTable.getValueAt(row,5).toString());
            view.cmbRol.setSelectedItem(view.employeeTable.getValueAt(row,6).toString());
            
            //Deshabilitar
            view.txtEmployeeId.setEditable(false);
            view.txtEmployeePassword.setEnabled(false);
            view.btnRegisterEmployee.setEnabled(false);  
        }else if (e.getSource() == view.lblEmployees) {
            if (rol.equals("Administrador")) {
                view.jTabbedPane1.setSelectedIndex(3);
                //limpiar la tabla
                cleanTable();
                //limpiar campos
                cleanFields();
                //listar empleados
                listAllEmployees();
            }else{
               view.jTabbedPane1.setEnabledAt(3, false);
               view.lblEmployees.setEnabled(false);
               JOptionPane.showMessageDialog(null, "No tiene privilegios de administrador para acceder a esta vista");
            }
            
        }
        //view.btnDeleteEmploye.setEnabled(true);
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
        if (e.getSource() == view.txtSearchEmployee) {
            cleanTable();
            listAllEmployees();   
        }
    }
    
    //Limpiar campos
    public void cleanFields(){
        view.txtEmployeeId.setText("");
        view.txtEmployeeId.setEditable(true);
        view.txtEmployeeFullName.setText("");
        view.txtEmployeeUsername.setText("");
        view.txtEmployeeAddress.setText("");
        view.txtEmployeeTelephone.setText("");
        view.txtEmployeeEmail.setText("");
        view.txtEmployeePassword.setText("");
        view.cmbRol.setSelectedIndex(0);
        
        view.employeeTable.clearSelection();
        
        //--> deberia setear la fila seleccionada a -1 (para indicar que no hay seleccionado nada)
    }

    //limpiar la tabla
    public void cleanTable(){
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i-1;
        }
    }
}
