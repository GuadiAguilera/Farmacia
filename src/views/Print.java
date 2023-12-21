/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import models.Purchase;
import models.PurchaseDao;

/**
 *
 * @author Us
 */
public class Print extends javax.swing.JFrame {

    Purchase purchase = new Purchase();
    PurchaseDao purchaseDao = new PurchaseDao();
    DefaultTableModel model = new DefaultTableModel();
            
    public Print(int id) {
        initComponents();
        setLocationRelativeTo(null); //para colocarlo en el medio
        setResizable(false); // para que el usuario no modificque el tamaño
        setTitle("Factura de compra");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); //Configura la ventana para que, cuando el usuario solicite cerrarla, finalice el programa.
        //DISPOSE ON CLOSE -> mantiene la ventana anterior abierta cuando se abre esta
        txtInvoice.setText("" + id);
        listAllPurchaseDetails(id);
        calculatePurchase();
    }

    public void listAllPurchaseDetails(int id){
        List<Purchase> list = purchaseDao.listPurchaseDetailQuery(id);
        model = (DefaultTableModel) purchaseDetailsTable.getModel();
        Object[] row = new Object[7];
        
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getProductName();
            row[1] = list.get(i).getPurchaseAmount();
            row[2] = list.get(i).getPurchasePrice();
            row[3] = list.get(i).getPurchaseSubtotal();
            row[4] = list.get(i).getSupplierNameProduct();
            row[5] = list.get(i).getPurchase(); //el comprador
            row[6] = list.get(i).getDateCreate();
            model.addRow(row);
        }
        purchaseDetailsTable.setModel(model);
    }
    
    //calcular el total
    public void calculatePurchase() {
        double total = 0.0;
        int numRow = purchaseDetailsTable.getRowCount();

        for (int i = 0; i < numRow; i++) {
            //pasar el indice de la columna que se sumara
            total = total + Double.parseDouble(String.valueOf(purchaseDetailsTable.getValueAt(i, 3)));
        }

        txtTotal.setText("" + total);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formPrint = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtInvoice = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        purchaseDetailsTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnPrintPurchase = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(580, 590));
        setName(""); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        formPrint.setBackground(new java.awt.Color(152, 202, 193));
        formPrint.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Iconos/farmacia.png"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, 70));

        formPrint.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, 70));

        jPanel1.setBackground(new java.awt.Color(18, 45, 61));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("VIDA NATURAL");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, -1, -1));

        txtInvoice.setEditable(false);
        jPanel1.add(txtInvoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, 110, -1));

        formPrint.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 70));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Detalles de la compra");
        formPrint.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        purchaseDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Precio", "Subtotal", "Proveedor", "Comprado por", "Fecha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(purchaseDetailsTable);
        if (purchaseDetailsTable.getColumnModel().getColumnCount() > 0) {
            purchaseDetailsTable.getColumnModel().getColumn(0).setMinWidth(100);
            purchaseDetailsTable.getColumnModel().getColumn(5).setMinWidth(110);
            purchaseDetailsTable.getColumnModel().getColumn(6).setMinWidth(80);
        }

        formPrint.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 590, 250));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total:");
        formPrint.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 450, -1, -1));

        txtTotal.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        formPrint.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 450, 170, -1));

        getContentPane().add(formPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 520));

        btnPrintPurchase.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnPrintPurchase.setText("IMPRIMIR");
        btnPrintPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintPurchaseActionPerformed(evt);
            }
        });
        getContentPane().add(btnPrintPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 560, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintPurchaseActionPerformed
        Toolkit tk = formPrint.getToolkit();
        PrintJob pj = tk.getPrintJob(this, null, null);
        Graphics graphics = pj.getGraphics();
        formPrint.print(graphics);
        txtInvoice.print(graphics);
        txtTotal.print(graphics);
        graphics.dispose();
        pj.end();
    }//GEN-LAST:event_btnPrintPurchaseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPrintPurchase;
    public javax.swing.JPanel formPrint;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable purchaseDetailsTable;
    public javax.swing.JTextField txtInvoice;
    public javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
