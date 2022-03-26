/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package job.work.management.system;

import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author sunny
 */
public class SearchInvoice1 extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null, rs2 = null;
    PreparedStatement pst = null, pst2 = null;

    /**
     * Creates new form SearchInvoice1
     */
    public SearchInvoice1() {
        initComponents();
        try {
            conn = javaconnect.ConnectDB();
        } catch (UnknownHostException e) {
            System.out.println( e );
        }
        this.setIconImage( new ImageIcon( getClass().getResource( "LOGO.png" ) ).getImage() );
        AutoCompleteDecorator.decorate( cmbCompany );
        AutoCompleteDecorator.decorate( cmbProductName );
    }

//Program to set single instance of ManageClients
    private static SearchInvoice1 obj = null;

    public static SearchInvoice1 getObj() {
        if (obj == null) {
            obj = new SearchInvoice1();
        }
        return obj;
    }

    public void getData1() {
        try {
            String sql = "SELECT invoiceno AS 'INVOICE NO.',joborderno AS 'JOB ORDER NO.',date AS 'DATE',company AS 'COMPANY', productname AS 'PRODUCT NAME',processname AS 'PROCESS',materialname As 'MATERIAL',"
                    + "tstrt AS 'T/START',w AS 'W',od AS 'OD',tl AS 'TL',mdp AS 'MDP',qty AS 'QTY',rate AS 'RATE',discountper AS 'DISC%',discountamt AS 'DISC.AMT',"
                    + "amount As 'AMOUNT',remark AS 'REMARK' FROM invoicemaster WHERE company LIKE '%" + cmbCompany.getSelectedItem() + "%' ORDER BY inv_uid DESC";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblSearchInvoice1.setModel( DbUtils.resultSetToTableModel( rs ) );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getData1() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    public void getData2() {
        try {
            String sql = "SELECT invoiceno AS 'INVOICE NO.',joborderno AS 'JOB ORDER NO.',date AS 'DATE',company AS 'COMPANY', productname AS 'PRODUCT NAME',processname AS 'PROCESS',materialname As 'MATERIAL',"
                    + "tstrt AS 'T/START',w AS 'W',od AS 'OD',tl AS 'TL',mdp AS 'MDP',qty AS 'QTY',rate AS 'RATE',discountper AS 'DISC%',discountamt AS 'DISC.AMT',"
                    + "amount As 'AMOUNT',remark As 'REMARK'FROM invoicemaster WHERE company LIKE '%" + cmbCompany.getSelectedItem() + "%' AND "
                    + "productname LIKE '%" + cmbProductName.getSelectedItem() + "%' ORDER BY inv_uid DESC";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblSearchInvoice1.setModel( DbUtils.resultSetToTableModel( rs ) );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getData2() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    public void getData3() {
        try {
            String sql = "SELECT invoiceno AS 'INVOICE NO.',joborderno AS 'JOB ORDER NO.',date AS 'DATE',company AS 'COMPANY', productname AS 'PRODUCT NAME',processname AS 'PROCESS',materialname As 'MATERIAL',"
                    + "tstrt AS 'T/START',w AS 'W',od AS 'OD',tl AS 'TL',mdp AS 'MDP',qty AS 'QTY',rate AS 'RATE',discountper AS 'DISC%',discountamt AS 'DISC.AMT',"
                    + "amount As 'AMOUNT',remark As 'REMARK'FROM invoicemaster WHERE company LIKE '%" + cmbCompany.getSelectedItem() + "%' AND "
                    + "productname LIKE '%" + cmbProductName.getSelectedItem() + "%' AND tstrt='" + txtTSTRT.getText() + "' ORDER BY inv_uid DESC";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblSearchInvoice1.setModel( DbUtils.resultSetToTableModel( rs ) );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getData3() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    public void getData4() {
        try {
            String sql = "SELECT invoiceno AS 'INVOICE NO.',joborderno AS 'JOB ORDER NO.',date AS 'DATE',company AS 'COMPANY', productname AS 'PRODUCT NAME',processname AS 'PROCESS',materialname As 'MATERIAL',"
                    + "tstrt AS 'T/START',w AS 'W',od AS 'OD',tl AS 'TL',mdp AS 'MDP',qty AS 'QTY',rate AS 'RATE',discountper AS 'DISC%',discountamt AS 'DISC.AMT',"
                    + "amount As 'AMOUNT',remark As 'REMARK'FROM invoicemaster WHERE company LIKE '%" + cmbCompany.getSelectedItem() + "%' AND "
                    + "productname LIKE '%" + cmbProductName.getSelectedItem() + "%' AND "
                    + "tsrt='" + txtTSTRT.getText() + "' AND w='" + txtW.getText() + "' ORDER BY inv_uid DESC";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblSearchInvoice1.setModel( DbUtils.resultSetToTableModel( rs ) );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getDataToTable() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    public void getClientData() {
        try {
            cmbCompany.addItem( "" );
            String sql = "SELECT * FROM clients";
            pst = conn.prepareStatement( sql );
            rs2 = pst.executeQuery();
            while (rs2.next()) {
                cmbCompany.addItem( rs2.getString( "clientcompanyname" ) );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getClientData() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs2.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    public void getProductData() {
        try {
            cmbProductName.removeAllItems();
            cmbProductName.addItem( "" );
            String sql = "SELECT * FROM products";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            if (rs.next() == false) {
                System.out.println( "Resultset is empty at getProductData()" );
            } else {
                do {
                    cmbProductName.addItem( rs.getString( "productname" ) );
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getProductData() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbCompany = new javax.swing.JComboBox<>();
        cmbProductName = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtTSTRT = new javax.swing.JTextField();
        txtW = new javax.swing.JTextField();
        txtOD = new javax.swing.JTextField();
        txtTL = new javax.swing.JTextField();
        txtMDP = new javax.swing.JTextField();
        txtRate = new javax.swing.JTextField();
        txtQty = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSearchInvoice1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Search Invoice 1");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Firm Name");

        cmbCompany.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCompanyItemStateChanged(evt);
            }
        });

        cmbProductName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProductNameItemStateChanged(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Product Name");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("T/START");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("W");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("OD");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("TL");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("M/DP");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("RATE");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("QTY");

        txtTSTRT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTSTRTKeyReleased(evt);
            }
        });

        txtW.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtWKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(cmbCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbProductName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 423, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTSTRT)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtW)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtOD)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTL)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMDP)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRate)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtQty))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9, txtMDP, txtOD, txtQty, txtRate, txtTL, txtTSTRT, txtW});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTSTRT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblSearchInvoice1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblSearchInvoice1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1055, 647));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbCompanyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCompanyItemStateChanged
        // TODO add your handling code here:
        getData1();
    }//GEN-LAST:event_cmbCompanyItemStateChanged

    private void txtTSTRTKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTSTRTKeyReleased
        // TODO add your handling code here:
        if (!"".equals( txtTSTRT.getText() )) {
            getData3();
        }
    }//GEN-LAST:event_txtTSTRTKeyReleased

    private void txtWKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWKeyReleased
        // TODO add your handling code here:
        if (!"".equals( txtW.getText() )) {
            getData4();
        }
    }//GEN-LAST:event_txtWKeyReleased

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        getClientData();
        getProductData();
    }//GEN-LAST:event_formWindowOpened

    private void cmbProductNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProductNameItemStateChanged
        // TODO add your handling code here:
        getData2();
    }//GEN-LAST:event_cmbProductNameItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals( info.getName() )) {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger( SearchInvoice1.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger( SearchInvoice1.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger( SearchInvoice1.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger( SearchInvoice1.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable() {
            public void run() {
                new SearchInvoice1().setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbCompany;
    private javax.swing.JComboBox<String> cmbProductName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblSearchInvoice1;
    private javax.swing.JTextField txtMDP;
    private javax.swing.JTextField txtOD;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtRate;
    private javax.swing.JTextField txtTL;
    private javax.swing.JTextField txtTSTRT;
    private javax.swing.JTextField txtW;
    // End of variables declaration//GEN-END:variables
}
