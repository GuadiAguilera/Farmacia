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
import models.CategoryDao;
import models.DynamicComboBox;
import static models.EmployeeDao.rolUser;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import views.SystemView;

public class CategoryController implements ActionListener, MouseListener, KeyListener {
    
    private Category category;
    private CategoryDao categoryDao;
    private SystemView view;
    
    String rol = rolUser;
    
    DefaultTableModel model = new DefaultTableModel();
    
    public CategoryController(Category category, CategoryDao categoryDao, SystemView view) {
        this.category = category;
        this.categoryDao = categoryDao;
        this.view = view;

        //boton registrar categoria
        this.view.btnRegisterCategory.addActionListener(this);
        //boton modificar
        this.view.btnUpdateCategory.addActionListener(this);
        // boton eliminar categoria
        this.view.btnDeleteCategory.addActionListener(this);
        //boton cancelar
        this.view.btnCancelCategory.addActionListener(this);
        //pone en escucha la tabla
        this.view.categoriesTable.addMouseListener(this);
        // colocar a la escucha al buscador
        this.view.txtSearchCategory.addKeyListener(this);
        //pone a la escucha al menu
        this.view.lblCategories.addMouseListener(this);
        
        //listar categorias en el combobox
        getCategoryName();
        
        AutoCompleteDecorator.decorate(view.cmbProductCategory);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //registrar
        if (e.getSource() == view.btnRegisterCategory) {
            if (view.txtCategoryName.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                category.setName(view.txtCategoryName.getText().trim());
                
                if (categoryDao.registerCategoryQuery(category)) {
                    cleanTable();
                    cleanFields();
                    listAllCategory();
                    JOptionPane.showMessageDialog(null, "Categoria registrada con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar la categoria");
                }
            }
            //Modificar
        } else if (e.getSource() == view.btnUpdateCategory) {
            if (view.txtCategoryId.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            } else {
                if (view.txtCategoryId.getText().equals("")
                        || view.txtCategoryName.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    category.setId(Integer.parseInt(view.txtCategoryId.getText()));
                    category.setName(view.txtCategoryName.getText().trim());
                    if (categoryDao.updateCategoryQuery(category)) {
                        cleanTable();
                        cleanFields();
                        listAllCategory();
                        
                        view.btnRegisterCategory.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Categoria modificada con exito");
                    }
                }
            }
            // eliminar
        } else if (e.getSource() == view.btnDeleteCategory) {
            int row = view.categoriesTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una categoria para eliminar");
            } else {
                int id = Integer.parseInt(view.categoriesTable.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar esta categoria?");
                
                if (question == 0 && categoryDao.deleteCategoryQuery(id) != false) {
                    cleanFields();
                    cleanTable();
                    view.btnRegisterCategory.setEnabled(true);
                    listAllCategory();
                    JOptionPane.showMessageDialog(null, "Categoria eliminada con éxito");
                }
            }
            // cancelar
        } else if (e.getSource() == view.btnCancelCategory) {
            cleanFields();
            view.btnRegisterCategory.setEnabled(true);
            
        }
    }

    //listar categorias
    public void listAllCategory() {
        if (rol.equals("Administrador")) {
            List<Category> list = categoryDao.listCategoryQuery(view.txtSearchCategory.getText());
            
            model = (DefaultTableModel) view.categoriesTable.getModel();
            
            Object[] row = new Object[2];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();
                
                model.addRow(row);
            }
            view.categoriesTable.setModel(model);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.categoriesTable) {
            int row = view.categoriesTable.rowAtPoint(e.getPoint());
            
            view.txtCategoryId.setText(view.categoriesTable.getValueAt(row, 0).toString());
            view.txtCategoryName.setText(view.categoriesTable.getValueAt(row, 1).toString());
            
            view.btnRegisterCategory.setEnabled(false);
            
        } else if (e.getSource() == view.lblCategories) {
            if (rol.equals("Administrador")) {
                view.jTabbedPane1.setSelectedIndex(5);
                cleanTable();
                cleanFields();
                listAllCategory();
            } else {
                view.jTabbedPane1.setEnabledAt(5, false);
                view.lblCategories.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tiene permiso de administrador para acceder a esta vista");
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
        // 
        if (e.getSource() == view.txtSearchCategory) {
            //limpiar tabla
            cleanTable();
            //listar categorias
            listAllCategory();
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
        view.txtCategoryId.setText("");
        view.txtCategoryName.setText("");
        
        view.categoriesTable.clearSelection();
    }

    // metodo para mostrar el nombre de las categorias
    public void getCategoryName() {
        List<Category> list = categoryDao.listCategoryQuery(view.txtSearchCategory.getText());
        
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            view.cmbProductCategory.addItem(new DynamicComboBox(id, name));
        }
    }
}
