package controllers;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static models.EmployeeDao.addressUser;
import static models.EmployeeDao.emailUser;
import static models.EmployeeDao.fullNameUser;
import static models.EmployeeDao.idUser;
import static models.EmployeeDao.telephoneUser;
import views.SystemView;

public class SettingsController implements MouseListener{
    
    private SystemView view;

    public SettingsController(SystemView view) {
        this.view = view;
        this.view.lblCategories.addMouseListener(this);
        this.view.lblCustomers.addMouseListener(this);
        this.view.lblEmployees.addMouseListener(this);
        this.view.lblPurchases.addMouseListener(this);
        this.view.lblReports.addMouseListener(this);
        this.view.lblSettings.addMouseListener(this);
        this.view.lblSuppliers.addMouseListener(this);
        this.view.lblProducts.addMouseListener(this);
        profile();

    }
    
    //Asignar el perfil del usuario
    public void profile(){
        //Se carga en las cajas de texto del panel perfil los datos del usuario autenticado
        this.view.txtProfileId.setText(""+idUser);
        this.view.txtProfileName.setText(""+fullNameUser);
        this.view.txtProfileAddress.setText(""+addressUser);
        this.view.txtProfilePhone.setText(""+telephoneUser);
        this.view.txtProfileEmail.setText(""+emailUser);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
     }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == view.lblProducts) {
            view.product.setBackground(new Color(152,202,63));
        } else if(e.getSource() == view.lblPurchases){
            view.purchases.setBackground(new Color(152,202,63));
        } else if (e.getSource() == view.lblCategories) {
            view.categories.setBackground(new Color(152,202,63));
        }else if (e.getSource() == view.lblCustomers) {
            view.customers.setBackground(new Color(152,202,63));
        }else if (e.getSource() == view.lblEmployees) {
            view.employees.setBackground(new Color(152,202,63));
        }else if (e.getSource() == view.lblReports) {
            view.reports.setBackground(new Color(152,202,63));
        }else if (e.getSource() == view.lblSettings) {
            view.settings.setBackground(new Color(152,202,63));
        }else if (e.getSource() == view.lblSuppliers) {
            view.suppliers.setBackground(new Color(152,202,63));
        }

     }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == view.lblProducts) {
            view.product.setBackground(new Color(18,45,61));
        } else if(e.getSource() == view.lblPurchases){
            view.purchases.setBackground(new Color(18,45,61));
        } else if (e.getSource() == view.lblCategories) {
            view.categories.setBackground(new Color(18,45,61));
        }else if (e.getSource() == view.lblCustomers) {
            view.customers.setBackground(new Color(18,45,61));
        }else if (e.getSource() == view.lblEmployees) {
            view.employees.setBackground(new Color(18,45,61));
        }else if (e.getSource() == view.lblReports) {
            view.reports.setBackground(new Color(18,45,61));
        }else if (e.getSource() == view.lblSettings) {
            view.settings.setBackground(new Color(18,45,61));
        }else if (e.getSource() == view.lblSuppliers) {
            view.suppliers.setBackground(new Color(18,45,61));
        }

    }

    
    
}

