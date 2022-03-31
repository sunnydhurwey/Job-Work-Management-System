/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class ManageChallan extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null, rs2 = null;
    PreparedStatement pst = null, pst2 = null;

    /**
     * Creates new form ManageChallan
     */
    public ManageChallan() {
        initComponents();
        try {
            conn = javaconnect.ConnectDB();
        } catch (UnknownHostException e) {
            System.out.println( e );
        }
        this.setIconImage( new ImageIcon( getClass().getResource( "LOGO.png" ) ).getImage() );
        AutoCompleteDecorator.decorate( cmbFirmName );
    }

    //Program to set single instance of ManageClients
    private static ManageChallan obj = null;

    public static ManageChallan getObj() {
        if (obj == null) {
            obj = new ManageChallan();
        }
        return obj;
    }

//Function or method to populate firm list
    public void getFirmData() {
        try {
            cmbFirmName.addItem( "" );
            String sql = "SELECT * FROM clients ";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            while (rs.next()) {
                cmbFirmName.addItem( rs.getString( "clientcompanyname" ) );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getFirmData() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }
//Function or method to get data to table

    public void getDataToTable() {
        try {
            String sql = "SELECT * FROM gatepass ORDER BY gp_uid DESC";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblChallanList.setModel( DbUtils.resultSetToTableModel( rs ) );
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

    public void getDataToTable2() {
        try {
            String sql = "SELECT * FROM gatepass WHERE gp_no='" + txtGatePassNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblChallanList.setModel( DbUtils.resultSetToTableModel( rs ) );
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

    public void getDataToTable3() {
        try {
            String sql = "SELECT * FROM gatepass WHERE gp_date='" + dtGatePass.getDate() + "' ORDER BY gp_uid DESC";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblChallanList.setModel( DbUtils.resultSetToTableModel( rs ) );
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

    public void getDataToTable4() {
        try {
            String sql = "SELECT * FROM gatepass WHERE gp_invoiceno='" + txtInvoiceNo.getText() + "' ORDER BY gp_uid DESC";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblChallanList.setModel( DbUtils.resultSetToTableModel( rs ) );
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

    public void getDataToTable5() {
        try {
            String sql = "SELECT * FROM gatepass WHERE gp_invoicedate='" + dtInvoice.getDate() + "' ORDER BY gp_uid DESC";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblChallanList.setModel( DbUtils.resultSetToTableModel( rs ) );
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

    public void getDataToTable6() {
        try {
            String sql = "SELECT * FROM gatepass WHERE gp_firmname='" + cmbFirmName.getSelectedItem() + "' ORDER BY gp_uid DESC";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblChallanList.setModel( DbUtils.resultSetToTableModel( rs ) );
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

    public void getDataToTable7() {
        try {
            String sql = "SELECT * FROM gatepass WHERE gp_porgpno='" + txtporgpno.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblChallanList.setModel( DbUtils.resultSetToTableModel( rs ) );
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

    public void getDataToTable8() {
        try {
            String sql = "SELECT * FROM gatepass WHERE gp_podate='" + dtPODate.getDate() + "' ORDER BY gp_uid DESC";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblChallanList.setModel( DbUtils.resultSetToTableModel( rs ) );
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
        txtInvoiceNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        dtInvoice = new com.toedter.calendar.JDateChooser();
        txtGatePassNo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dtPODate = new com.toedter.calendar.JDateChooser();
        dtGatePass = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        cmbFirmName = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtporgpno = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChallanList = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Job Work Management System - Manage Challan");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "CHALLAN RECORD"));

        txtInvoiceNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtInvoiceNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInvoiceNoKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Invoice Dated");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Gate Pass No.");

        dtInvoice.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                dtInvoiceInputMethodTextChanged(evt);
            }
        });
        dtInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dtInvoiceKeyPressed(evt);
            }
        });

        txtGatePassNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtGatePassNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGatePassNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGatePassNoKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setText("PO Date");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Date");

        dtPODate.setDateFormatString("yyyy-MM-dd");
        dtPODate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dtPODateKeyReleased(evt);
            }
        });

        dtGatePass.setDateFormatString("yyyy-MM-dd");
        dtGatePass.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                dtGatePassInputMethodTextChanged(evt);
            }
        });
        dtGatePass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dtGatePassKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Firm Name");

        cmbFirmName.setEditable(true);
        cmbFirmName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cmbFirmName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFirmNameItemStateChanged(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("PO/RGP No.");

        txtporgpno.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtporgpno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtporgpnoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtporgpnoKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Invoice No.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(21, 21, 21)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtGatePassNo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtGatePass, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbFirmName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtporgpno)
                    .addComponent(txtInvoiceNo, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dtInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addComponent(dtPODate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtGatePassNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel5)
                        .addComponent(txtInvoiceNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(dtGatePass, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(dtInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbFirmName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtporgpno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dtPODate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblChallanList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblChallanList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE))
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

        setSize(new java.awt.Dimension(916, 630));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtInvoiceNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            dtInvoice.requestFocus();
        }
    }//GEN-LAST:event_txtInvoiceNoKeyPressed

    private void txtInvoiceNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceNoKeyReleased
        // TODO add your handling code here:
        if (!"".equals( txtInvoiceNo.getText() )) {
            getDataToTable4();
        }
    }//GEN-LAST:event_txtInvoiceNoKeyReleased

    private void dtInvoiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtInvoiceKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtporgpno.requestFocus();
        }
    }//GEN-LAST:event_dtInvoiceKeyPressed

    private void txtGatePassNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGatePassNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            dtGatePass.requestFocus();
        }
    }//GEN-LAST:event_txtGatePassNoKeyPressed

    private void txtGatePassNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGatePassNoKeyReleased
        // TODO add your handling code here:
        if (!"".equals( txtGatePassNo.getText() )) {
            getDataToTable2();
        }
    }//GEN-LAST:event_txtGatePassNoKeyReleased

    private void dtGatePassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtGatePassKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtInvoiceNo.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_dtGatePassKeyPressed

    private void txtporgpnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtporgpnoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {

        }

    }//GEN-LAST:event_txtporgpnoKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        getFirmData();
        getDataToTable();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        obj = null;
    }//GEN-LAST:event_formWindowClosing

    private void dtGatePassInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_dtGatePassInputMethodTextChanged
        // TODO add your handling code here:
        getDataToTable3();
    }//GEN-LAST:event_dtGatePassInputMethodTextChanged

    private void dtInvoiceInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_dtInvoiceInputMethodTextChanged
        // TODO add your handling code here:
        getDataToTable5();
    }//GEN-LAST:event_dtInvoiceInputMethodTextChanged

    private void cmbFirmNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFirmNameItemStateChanged
        // TODO add your handling code here:
        getDataToTable6();
    }//GEN-LAST:event_cmbFirmNameItemStateChanged

    private void txtporgpnoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtporgpnoKeyReleased
        // TODO add your handling code here:
        getDataToTable7();
    }//GEN-LAST:event_txtporgpnoKeyReleased

    private void dtPODateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtPODateKeyReleased
        // TODO add your handling code here:
        getDataToTable8();
    }//GEN-LAST:event_dtPODateKeyReleased

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
            java.util.logging.Logger.getLogger( ManageChallan.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger( ManageChallan.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger( ManageChallan.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger( ManageChallan.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable() {
            public void run() {
                new ManageChallan().setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbFirmName;
    private com.toedter.calendar.JDateChooser dtGatePass;
    private com.toedter.calendar.JDateChooser dtInvoice;
    private com.toedter.calendar.JDateChooser dtPODate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblChallanList;
    private javax.swing.JTextField txtGatePassNo;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtporgpno;
    // End of variables declaration//GEN-END:variables
}
