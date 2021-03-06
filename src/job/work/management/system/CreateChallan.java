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
public class CreateChallan extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null, rs2 = null;
    PreparedStatement pst = null, pst2 = null;

    /**
     * Creates new form CreateChallan
     */
    public CreateChallan() {
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
    private static CreateChallan obj = null;

    public static CreateChallan getObj() {
        if (obj == null) {
            obj = new CreateChallan();
        }
        return obj;
    }
    //Function or method to get challan id
    int gpno = 0;

    public void getChallanID() {
        try {
            String sql = "SELECT * FROM gatepass";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            while (rs.next()) {
                gpno = rs.getInt( "gp_no" );
            }
            gpno++;
            txtGatePassNo.setText( String.valueOf( gpno ) );
            btnAdd.setEnabled( true );
            btnUpdate.setEnabled( false );
            btnDelete.setEnabled( false );
            btnPrint.setEnabled( false );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getChallanID() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to set current date to jdatechooser
    public void getDate() {
        dtGatePass.setDateFormatString( "yyyy-MM-dd" );
        dtInvoice.setDateFormatString( "yyyy-MM-dd" );
        java.util.Date date = new java.util.Date();
        dtGatePass.setDate( date );
        dtPODate.setDate( date );
    }

    //Function or method to clear Fields
    public void clearFields() {
        txtDriverName.setText( "" );
        txtMobile.setText( "" );
        cmbFirmName.setSelectedItem( "" );
        txtporgpno.setText( "" );
        txtInvoiceNo.setText( "" );
        txtVehicleNo.setText( "" );
        getDate();
        btnAdd.setEnabled( true );
        btnUpdate.setEnabled( false );
        btnDelete.setEnabled( false );
        btnPrint.setEnabled( false );
    }

    //Function or method to get data to table
    public void getDataToTable() {
        try {
            String sql = "SELECT company,productname,tstrt,w,od,tl,mdp,qty,remark FROM invoicemaster WHERE invoiceno='" + txtInvoiceNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblChallan.setModel( DbUtils.resultSetToTableModel( rs ) );
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

    //Function or method to get data to table
    public void getChallanData() {
        try {
            String sql = "SELECT * FROM gatepass WHERE gp_no='" + txtGatePassNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            if (rs.next()) {
                dtGatePass.setDate( rs.getDate( "gp_date" ) );
                dtInvoice.setDate( rs.getDate( "gp_invoicedate" ) );
                txtInvoiceNo.setText( rs.getString( "gp_invoiceno" ) );
                txtDriverName.setText( rs.getString( "gp_drivername" ) );
                txtMobile.setText( rs.getString( "gp_mobile" ) );
                txtVehicleNo.setText( rs.getString( "gp_vehicleno" ) );
                cmbFirmName.setSelectedItem( rs.getString( "gp_firmname" ) );
                txtporgpno.setText( rs.getString( "gp_porgpno" ) );
                dtPODate.setDate( rs.getDate( "gp_podate" ) );
                btnAdd.setEnabled( false );
                btnUpdate.setEnabled( true );
                btnDelete.setEnabled( true );
                btnPrint.setEnabled( true );
            } else {
                clearFields();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getChallanDataToTable() Exception", JOptionPane.ERROR_MESSAGE );
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
    public void getInvoiceDate() {
        try {
            String sql = "SELECT * FROM invoicemaster WHERE invoiceno='" + txtInvoiceNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            if (rs.next()) {
                dtInvoice.setDate( rs.getDate( "date" ) );
                cmbFirmName.setSelectedItem( rs.getString( "company" ) );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getInvoiceDate() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
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

    //Function or method to add challan
    public void addChallan() {
        try {
            String sql = "INSERT INTO `gatepass`(`gp_no`, `gp_porgpno`, `gp_date`, `gp_firmname`, `gp_invoiceno`, `gp_invoicedate`, `gp_drivername`, `gp_mobile`, `gp_vehicleno`,`gp_podate`) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, txtGatePassNo.getText() );
            pst.setString( 2, txtporgpno.getText() );
            pst.setString( 3, ((JTextField) dtGatePass.getDateEditor().getUiComponent()).getText() );
            pst.setString( 4, cmbFirmName.getSelectedItem().toString() );
            pst.setString( 5, txtInvoiceNo.getText() );
            pst.setString( 6, ((JTextField) dtInvoice.getDateEditor().getUiComponent()).getText() );
            pst.setString( 7, txtDriverName.getText() );
            pst.setString( 8, txtMobile.getText() );
            pst.setString( 9, txtVehicleNo.getText() );
            pst.setString( 10, ((JTextField) dtPODate.getDateEditor().getUiComponent()).getText() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Challan added to database", "CHALLAN ADDED", JOptionPane.PLAIN_MESSAGE );
            btnAdd.setEnabled( false );
            btnUpdate.setEnabled( true );
            btnDelete.setEnabled( true );
            btnPrint.setEnabled( true );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "addChallan() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to update challan
    public void updateChallan() {
        try {
            String sql = "UPDATE `gatepass` SET `gp_porgpno`=?, `gp_date`=?, `gp_firmname`=?, `gp_invoiceno`=?, `gp_invoicedate`=?, "
                    + "`gp_drivername`=?, `gp_mobile`=?, `gp_vehicleno`=?, 'gp_podate'=? WHERE 'gp_no'='" + txtGatePassNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, txtporgpno.getText() );
            pst.setString( 2, ((JTextField) dtGatePass.getDateEditor().getUiComponent()).getText() );
            pst.setString( 3, cmbFirmName.getSelectedItem().toString() );
            pst.setString( 4, txtInvoiceNo.getText() );
            pst.setString( 5, ((JTextField) dtInvoice.getDateEditor().getUiComponent()).getText() );
            pst.setString( 6, txtDriverName.getText() );
            pst.setString( 7, txtMobile.getText() );
            pst.setString( 8, txtVehicleNo.getText() );
            pst.setString( 9, ((JTextField) dtPODate.getDateEditor().getUiComponent()).getText() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Challan updated to database", "CHALLAN UPDATED", JOptionPane.PLAIN_MESSAGE );
            btnAdd.setEnabled( false );
            btnUpdate.setEnabled( true );
            btnDelete.setEnabled( true );
            btnPrint.setEnabled( true );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "updateChallan() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to delete challan
    public void deleteChallan() {
        try {
            String sql = "DELETE FROM gatepass WHERE gp_no='" + txtGatePassNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Challan deleted from database", "CHALLAN DELETED", JOptionPane.PLAIN_MESSAGE );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "deleteChallan() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Program to give printout options
    //final JDialog dialog=new JDialog();
    public void printOption() {
        try {
            try {
                String sql = "SELECT * FROM invoicemaster,companydetails,gatepass WHERE invoicemaster.invoiceno='" + txtInvoiceNo.getText() + "' AND gatepass.gp_no='" + txtGatePassNo.getText() + "' AND companydetails.c_uid=(SELECT MIN(companydetails.c_uid) FROM companydetails)";
                JasperDesign jd = JRXmlLoader.load( "src/reports/challan.jrxml" );
                JRDesignQuery qry = new JRDesignQuery();
                qry.setText( sql );
                jd.setQuery( qry );
                JasperReport jr = JasperCompileManager.compileReport( jd );
                JasperPrint jp = JasperFillManager.fillReport( jr, null, conn );
                JasperViewer.viewReport( jp, false );
            } catch (JRException e) {
                JOptionPane.showMessageDialog( null, e, "printOption() Exception", JOptionPane.ERROR_MESSAGE );
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog( null, e, "Print Challan Exception", JOptionPane.ERROR_MESSAGE );
        }

//        try {
//            String[] choice = {"PRINT 1", "PRINT 2"};
//            int x = JOptionPane.showOptionDialog( null, "Choose Print Method", "Invoice Print", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choice, choice[0] );
//            //System.out.println(x);
//            //JOptionPane.showMessageDialog(null, "You Selected: "+x);            
//            if (x == 0) {
//                try {
//                    String sql = "SELECT * FROM invoicemaster,companydetails,gatepass WHERE invoicemaster.invoiceno='" + txtInvoiceNo.getText() + "' AND gatepass.gp_no='" + txtGatePassNo.getText() + "' AND companydetails.c_uid=(SELECT MIN(companydetails.c_uid) FROM companydetails)";
//                    JasperDesign jd = JRXmlLoader.load( "src/reports/challan.jrxml" );
//                    JRDesignQuery qry = new JRDesignQuery();
//                    qry.setText( sql );
//                    jd.setQuery( qry );
//                    JasperReport jr = JasperCompileManager.compileReport( jd );
//                    JasperPrint jp = JasperFillManager.fillReport( jr, null, conn );
//                    JasperViewer.viewReport( jp, false );
//                } catch (JRException e) {
//                    JOptionPane.showMessageDialog( null, e, "printOption() Exception", JOptionPane.ERROR_MESSAGE );
//                } finally {
//                    try {
//                        rs.close();
//                        pst.close();
//                    } catch (SQLException e) {
//
//                    }
//                }
//            } else {
//                try {
//                    String sql = "SELECT * FROM invoicemaster,companydetails,gatepass WHERE invoicemaster.invoiceno='" + txtInvoiceNo.getText() + "' AND gatepass.gp_no='" + txtGatePassNo.getText() + "' AND companydetails.c_uid=(SELECT MAX(companydetails.c_uid) FROM companydetails)";
//                    JasperDesign jd = JRXmlLoader.load( "src/reports/challan.jrxml" );
//                    JRDesignQuery qry = new JRDesignQuery();
//                    qry.setText( sql );
//                    jd.setQuery( qry );
//                    JasperReport jr = JasperCompileManager.compileReport( jd );
//                    JasperPrint jp = JasperFillManager.fillReport( jr, null, conn );
//                    JasperViewer.viewReport( jp, false );
//                } catch (JRException e) {
//                    JOptionPane.showMessageDialog( null, e, "printOption() Exception", JOptionPane.ERROR_MESSAGE );
//                } finally {
//                    try {
//                        rs.close();
//                        pst.close();
//                    } catch (SQLException e) {
//
//                    }
//                }
//            }
//        } catch (HeadlessException e) {
//            JOptionPane.showMessageDialog( null, e, "Print Invoice Exception", JOptionPane.ERROR_MESSAGE );
//        }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChallan = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtDriverName = new javax.swing.JTextField();
        txtMobile = new javax.swing.JTextField();
        txtVehicleNo = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtGatePassNo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        dtGatePass = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        cmbFirmName = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtporgpno = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtInvoiceNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        dtInvoice = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        dtPODate = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Delivery Challan / Gate Pass - Job Work Management System");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        tblChallan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblChallan);

        jPanel3.setBackground(new java.awt.Color(204, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Vehicle Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Driver / Owner Name");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Mobile No.");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Vehicle No.");

        txtDriverName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtDriverName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDriverNameKeyPressed(evt);
            }
        });

        txtMobile.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtMobile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMobileKeyPressed(evt);
            }
        });

        txtVehicleNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtVehicleNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVehicleNoKeyPressed(evt);
            }
        });

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/add_16px.png"))); // NOI18N
        btnAdd.setText("ADD");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/update_16px.png"))); // NOI18N
        btnUpdate.setText("UPDATE");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/Delete_16px.png"))); // NOI18N
        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnPrint.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/print_16px.png"))); // NOI18N
        btnPrint.setText("PRINT");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDriverName, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVehicleNo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrint))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMobile)))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAdd, btnDelete, btnPrint, btnUpdate});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtDriverName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtMobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtVehicleNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnPrint))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAdd, btnDelete, btnPrint, btnUpdate, jLabel7, jLabel8, jLabel9, txtDriverName, txtMobile, txtVehicleNo});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Delivery Challan / Gate Pass Details", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Gate Pass No.");

        txtGatePassNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtGatePassNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGatePassNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGatePassNoKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Date");

        dtGatePass.setDateFormatString("yyyy-MM-dd");
        dtGatePass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dtGatePassKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Firm Name");

        cmbFirmName.setEditable(true);
        cmbFirmName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("PO/RGP No.");

        txtporgpno.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtporgpno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtporgpnoKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Invoice No.");

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

        dtInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dtInvoiceKeyPressed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setText("PO Date");

        dtPODate.setDateFormatString("yyyy-MM-dd");

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
                    .addComponent(dtInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbFirmName, dtGatePass, dtInvoice, jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, txtGatePassNo, txtInvoiceNo, txtporgpno});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        getChallanID();
        getDate();
        clearFields();
        getFirmData();
    }//GEN-LAST:event_formWindowOpened

    private void txtInvoiceNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceNoKeyReleased
        // TODO add your handling code here:
        if (!"".equals( txtInvoiceNo.getText() )) {
            getDataToTable();
            getInvoiceDate();
        }
    }//GEN-LAST:event_txtInvoiceNoKeyReleased

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        addChallan();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        updateChallan();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        deleteChallan();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        printOption();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void txtGatePassNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGatePassNoKeyReleased
        // TODO add your handling code here:
        if (!"".equals( txtGatePassNo.getText() )) {
            getChallanData();
            getDataToTable();
        } else {
            clearFields();
        }
    }//GEN-LAST:event_txtGatePassNoKeyReleased

    private void txtGatePassNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGatePassNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            dtGatePass.requestFocus();
        }
    }//GEN-LAST:event_txtGatePassNoKeyPressed

    private void dtGatePassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtGatePassKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtInvoiceNo.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_dtGatePassKeyPressed

    private void txtInvoiceNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            dtInvoice.requestFocus();
        }
    }//GEN-LAST:event_txtInvoiceNoKeyPressed

    private void dtInvoiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtInvoiceKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtporgpno.requestFocus();
        }
    }//GEN-LAST:event_dtInvoiceKeyPressed

    private void txtporgpnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtporgpnoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtDriverName.requestFocus();
        }
    }//GEN-LAST:event_txtporgpnoKeyPressed

    private void txtDriverNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDriverNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtMobile.requestFocus();
        }
    }//GEN-LAST:event_txtDriverNameKeyPressed

    private void txtMobileKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMobileKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtVehicleNo.requestFocus();
        }
    }//GEN-LAST:event_txtMobileKeyPressed

    private void txtVehicleNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVehicleNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            addChallan();
        }
    }//GEN-LAST:event_txtVehicleNoKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        obj = null;
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger( CreateChallan.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger( CreateChallan.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger( CreateChallan.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger( CreateChallan.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable() {
            public void run() {
                new CreateChallan().setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnUpdate;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblChallan;
    private javax.swing.JTextField txtDriverName;
    private javax.swing.JTextField txtGatePassNo;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtMobile;
    private javax.swing.JTextField txtVehicleNo;
    private javax.swing.JTextField txtporgpno;
    // End of variables declaration//GEN-END:variables
}
