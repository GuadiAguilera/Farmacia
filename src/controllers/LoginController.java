package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import models.Employee;
import models.EmployeeDao;
import views.LoginView;
import views.SystemView;

public class LoginController implements ActionListener , KeyListener{

    private Employee employee;
    private EmployeeDao employeeDao;
    private LoginView loginView;

    public LoginController(Employee employee, EmployeeDao employeeDao, LoginView loginView) {
        this.employee = employee;
        this.employeeDao = employeeDao;
        this.loginView = loginView;
        this.loginView.btnEnter.addActionListener(this);
        this.loginView.btnEnter.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //se obtienen los datos de las vistas
        String user = loginView.txtUsuario.getText().trim();
        String pass = String.valueOf(loginView.txtPassword.getPassword());
        
        if (e.getSource() == loginView.btnEnter && loginView.btnEnter.isFocusOwner()) {
            //validar que los campos no esten vacios
            if (!user.equals("") && !pass.equals("")) {
                //pasar los parametros al metodo login
                employee = employeeDao.loginQuery(user, pass);

                //verificar la existencia del usuario
                if (employee.getUsername() != null) {
                    //mostrar una u otra ventana dependiendo el rol
                    if (employee.getRol().equals("Administrador")) {
                        SystemView admin = new SystemView();
                        admin.setVisible(true);
                    } else {
                        SystemView aux = new SystemView();
                        aux.setVisible(true);
                    }
                    this.loginView.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecto");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Los campos estan vacios");
            }
        }   
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    
    @Override
    public void keyPressed(KeyEvent e) { 
        /**
        //se obtienen los datos de las vistas
        String user = loginView.txtUsuario.getText().trim();
        String pass = String.valueOf(loginView.txtPassword.getPassword());
        
        char PressKey = e.getKeyChar();
        if(PressKey == KeyEvent.VK_ENTER){
            loginView.btnEnter.doClick();
        }    
        **/
    }

    @Override
    public void keyReleased(KeyEvent e) {  
        
    }

}
