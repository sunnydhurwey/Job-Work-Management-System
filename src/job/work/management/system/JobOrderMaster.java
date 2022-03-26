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
public class JobOrderMaster extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null, rs2 = null;
    PreparedStatement pst = null, pst2 = null;

    /**
     * Creates new form CreateQuotation
     */
    public JobOrderMaster() {
        initComponents();
        try {
            conn = javaconnect.ConnectDB();
        } catch (UnknownHostException e) {
            System.out.println( e );
        }
        this.setIconImage( new ImageIcon( getClass().getResource( "LOGO.png" ) ).getImage() );
        AutoCompleteDecorator.decorate( cmbCompany );
        AutoCompleteDecorator.decorate( cmbProductName );
        AutoCompleteDecorator.decorate( cmbProcess );
        AutoCompleteDecorator.decorate( cmbMaterial );
    }

    //Program to set single instance of ManageClients
    private static JobOrderMaster obj = null;

    public static JobOrderMaster getObj() {
        if (obj == null) {
            obj = new JobOrderMaster();
        }
        return obj;
    }

    //Function or method to get data from database
    int jno = 0;

    public void getJobNo() {
        try {
            String sql = "SELECT joborderno FROM jobordermaster ORDER BY joborderno DESC";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            if (rs.next()) {
                jno = rs.getInt( "joborderno" );
            }
            jno++;
            txtJobOrderNo.setText( String.valueOf( jno ) );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getJobNo() Exception", JOptionPane.ERROR_MESSAGE );
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
        jDateChooser1.setDateFormatString( "yyyy-MM-dd" );
        java.util.Date date = new java.util.Date();
        jDateChooser1.setDate( date );
    }

    //Function or method to clear product field
    public void clearProductField() {
        cmbProductName.setSelectedIndex( 0 );
        cmbProcess.setSelectedIndex( 0 );
        cmbMaterial.setSelectedIndex( 0 );
        txtRemarks.setText( "" );
        txtTSTRT.setText( "0" );
        txtW.setText( "0" );
        txtOD.setText( "0" );
        txtTL.setText( "0" );
        txtMDP.setText( "0" );
        txtDiscount.setText( "0" );
        txtRate.setText( "0" );
        txtQTY.setText( "0" );
        txtAmount.setText( "0" );
    }

    //Function or method to save client
    public void saveClient() {
        try {
            String sql = "INSERT INTO clients (clientcompanyname) VALUES (?)";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, cmbCompany.getEditor().getItem().toString() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Client added to database", "CLIENT ADDED", JOptionPane.PLAIN_MESSAGE );
            btnSaveClient.setEnabled( false );
//            cmbCompany.setEnabled( false );
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "saveClient() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to get data to table
    public void getDataToTable() {
        try {
            String sql = "SELECT uid AS 'UID',joborderno AS 'JOB ORDER NO',pageno AS 'PAGE NO',productname AS 'PRODUCT NAME',processname AS 'PROCESS NAME',"
                    + "materialname AS 'MATERIAL NAME',tstrt AS 'T/START',w AS 'W',od AS 'OD',tl AS 'TL',mdp AS 'M/DP',rate AS 'RATE',qty AS 'QUANTITY',"
                    + "discountper AS 'DISC. %',discountamt AS 'DISC. AMT',amount AS 'AMOUNT' FROM jobordermaster WHERE joborderno='" + txtJobOrderNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblJobOrder.setModel( DbUtils.resultSetToTableModel( rs ) );
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

    //Function or method to get saved job order data calculation
    public void getJobOrderDataCALC() {
        try {
            String sql = "SELECT * FROM jobordermaster WHERE joborderno='" + txtJobOrderNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs2 = pst.executeQuery();
            if (rs2.next()) {
                cmbCompany.setSelectedItem( rs2.getString( "company" ) );
                txtPageNo.setText( rs2.getString( "pageno" ) );
            } else {
                cmbCompany.setSelectedItem( "" );
                txtPageNo.setText( "" );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getJobOrderDataCALC() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs2.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e, "getJobOrderDataCALC() Exception", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    //Function or method to clear client data field
    public void clearClientData() {
        cmbCompany.setSelectedItem( "" );
    }

    //Function to populate combobox on client name input
    public void getClientData() {
        try {
            cmbCompany.setSelectedItem( "" );
            String sql = "SELECT * FROM clients";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            if (rs.next() == false) {
                System.out.println( "Resultset is empty at getClientData()" );
            } else {
                do {
                    cmbCompany.addItem( rs.getString( "clientcompanyname" ) );
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getClientData() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }
    //Function to populate other client data comboboxes on Enter key press on client name
    boolean clientexist = false;
    int clientID = 0;
    String clientcompany;

    public void setDataToFieldsOnNameInput() {
        clientcompany = cmbCompany.getSelectedItem().toString();
        try {
            String sql = "SELECT * FROM clients WHERE clientcompanyname LIKE '" + clientcompany + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            if (rs.next() == false) {
                System.out.println( "Resultset is empty setDataToFieldOnNameInput()" );
                btnSaveClient.setEnabled( true );
            } else {
                do {
                    clientID = rs.getInt( "uid" );
                    System.out.println( String.valueOf( clientID ) );
                    cmbCompany.setSelectedItem( rs.getString( "clientcompanyname" ) );
                } while (rs.next());
                btnSaveClient.setEnabled( false );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "setDataToFieldsOnNameInput() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }

    }

    //Function or method to get product data
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

    //Function or method to get process data
    public void getProcessData() {
        try {
            cmbProcess.removeAllItems();
            cmbProcess.addItem( "" );
            String sql = "SELECT * FROM process";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            if (rs.next() == false) {
                System.out.println( "Resultset is empty" );
            } else {
                do {
                    cmbProcess.addItem( rs.getString( "processname" ) );
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getProcessData() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to get material data
    public void getMaterialData() {
        try {
            cmbMaterial.removeAllItems();
            cmbMaterial.addItem( "" );
            String sql = "SELECT * FROM material";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            if (rs.next() == false) {
                System.out.println( "Resultset is empty" );
            } else {
                do {
                    cmbMaterial.addItem( rs.getString( "materialname" ) );
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getMaterialData() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to add product to job list
    public void addProduct() {
        try {
            String sql = "INSERT INTO jobordermaster (joborderno,date,productname,processname,materialname,tstrt,w,od,tl,mdp,discountper,qty,rate,"
                    + "amount,remark,clientid,company,discountamt,pageno) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, txtJobOrderNo.getText() );
            pst.setString( 2, ((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText() );
            pst.setString( 3, cmbProductName.getSelectedItem().toString() );
            pst.setString( 4, cmbProcess.getSelectedItem().toString() );
            pst.setString( 5, cmbMaterial.getSelectedItem().toString() );
            pst.setString( 6, txtTSTRT.getText() );
            pst.setString( 7, txtW.getText() );
            pst.setString( 8, txtOD.getText() );
            pst.setString( 9, txtTL.getText() );
            pst.setString( 10, txtMDP.getText() );
            pst.setString( 11, txtDiscount.getText() );
            pst.setString( 12, txtQTY.getText() );
            pst.setString( 13, txtRate.getText() );
            pst.setString( 14, txtAmount.getText() );
            pst.setString( 15, txtRemarks.getText() );
            pst.setInt( 16, clientID );
            pst.setString( 17, cmbCompany.getSelectedItem().toString() );
            pst.setString( 18, String.valueOf( discount ) );
            pst.setString( 19, String.valueOf( txtPageNo.getText() ) );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Poduct added to database", "Saved", JOptionPane.PLAIN_MESSAGE );
//            getData();
            clearProductField();
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "addProduct() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to get selected data from table to field
    int row;
    String count, tblClick;

    public void getTableDataToField() {
        try {
            row = tblJobOrder.getSelectedRow();
            tblClick = tblJobOrder.getModel().getValueAt( row, 0 ).toString();
            String sql = "SELECT * FROM jobordermaster WHERE uid='" + tblClick + "'";
            pst2 = conn.prepareStatement( sql );
            rs2 = pst2.executeQuery();
            if (rs2.next()) {
                cmbCompany.setSelectedItem( rs2.getString( "company" ) );
                cmbProductName.setSelectedItem( rs2.getString( "productname" ) );
                cmbProcess.setSelectedItem( rs2.getString( "processname" ) );
                cmbMaterial.setSelectedItem( rs2.getString( "materialname" ) );
                txtRemarks.setText( rs2.getString( "remark" ) );
                txtTSTRT.setText( rs2.getString( "tstrt" ) );
                txtW.setText( rs2.getString( "w" ) );
                txtOD.setText( rs2.getString( "od" ) );
                txtTL.setText( rs2.getString( "tl" ) );
                txtMDP.setText( rs2.getString( "mdp" ) );
                txtDiscount.setText( rs2.getString( "discountper" ) );
                txtRate.setText( rs2.getString( "rate" ) );
                txtQTY.setText( rs2.getString( "qty" ) );
                txtAmount.setText( rs2.getString( "amount" ) );
            } else {
                JOptionPane.showMessageDialog( null, "No data found. Please check your database connection.", "No record found", JOptionPane.ERROR_MESSAGE );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getTableDataToField() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to delete product from job order
    public void deleteProduct() {
        try {
            String sql = "DELETE FROM jobordermaster WHERE uid='" + tblClick + "'";
            pst = conn.prepareStatement( sql );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Poduct deleted to database", "Deleted", JOptionPane.PLAIN_MESSAGE );
//            getData();
            clearProductField();
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, "deleteProduct() Exception", "Deleted", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }
    //Function or method to calculate Rate X Quantity = AMOUNT
    double discount = 0, rate = 0, quantity = 0, amount = 0;

    public void calcAmt() {
        discount = 0;
        rate = 0;
        quantity = 0;
        amount = 0;

        if ("".equals( txtRate.getText() )) {
            rate = 0;
        } else {
            rate = Double.parseDouble( txtRate.getText() );
        }
        if ("".equals( txtQTY.getText() )) {
            quantity = 0;
        } else {
            quantity = Double.parseDouble( txtQTY.getText() );
        }
        if ("".equals( txtDiscount.getText() )) {
            discount = 0;
        } else {
            discount = Double.parseDouble( txtDiscount.getText() );
        }
        amount = rate * quantity;
        System.out.println( String.valueOf( rate ) + "*" + String.valueOf( quantity ) );
        discount = (amount * discount) / 100;
        System.out.println( String.valueOf( discount ) );
        amount = amount - discount;
        System.out.println( String.valueOf( amount ) );
        txtAmount.setText( String.valueOf( amount ) );
    }

    //Function or method to add product name to database
    public void addProductName() {
        try {
            String sql = "INSERT INTO products (productname) VALUES (?)";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, cmbProductName.getEditor().getItem().toString() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "New Product Name added to database", "Saved", JOptionPane.PLAIN_MESSAGE );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "addProductName() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to add process name to database
    public void addProcessName() {
        try {
            String sql = "INSERT INTO process (processname) VALUES (?)";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, cmbProductName.getEditor().getItem().toString() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "New Process Name added to database", "Saved", JOptionPane.PLAIN_MESSAGE );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "addProcessName() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to add material name to database
    public void addMaterialName() {
        try {
            String sql = "INSERT INTO material (materialname) VALUES (?)";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, cmbMaterial.getEditor().getItem().toString() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "New Material Name added to database", "Saved", JOptionPane.PLAIN_MESSAGE );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "addMaterialName() Exception", JOptionPane.ERROR_MESSAGE );
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
    public void printOptions() {
        try {
            String jno = txtJobOrderNo.getText();
            try {
                String sql = "SELECT * FROM jobordermaster,companydetails WHERE jobordermaster.joborderno='" + jno + "' AND companydetails.c_uid=(SELECT MIN(companydetails.c_uid) FROM companydetails)";
                JasperDesign jd = JRXmlLoader.load( "src/reports/joborderPT.jrxml" );
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
            JOptionPane.showMessageDialog( null, e, "Print Invoice Exception", JOptionPane.ERROR_MESSAGE );
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
        cmbCompany = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        btnSaveClient = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cmbProductName = new javax.swing.JComboBox<>();
        cmbProcess = new javax.swing.JComboBox<>();
        cmbMaterial = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtTSTRT = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtW = new javax.swing.JTextField();
        txtOD = new javax.swing.JTextField();
        txtTL = new javax.swing.JTextField();
        txtMDP = new javax.swing.JTextField();
        txtRate = new javax.swing.JTextField();
        txtQTY = new javax.swing.JTextField();
        txtAmount = new javax.swing.JTextField();
        btnAddProduct = new javax.swing.JButton();
        btnDeleteProduct = new javax.swing.JButton();
        btnClearProduct = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        btnAddProductName = new javax.swing.JButton();
        btnAddProcessName = new javax.swing.JButton();
        btnAddMaterialName = new javax.swing.JButton();
        btnPrintJobOrder = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtDiscount = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtJobOrderNo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        txtPageNo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblJobOrder = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("JOB ORDER MASTER - JOB WORK MANAGEMENT SYSTEM");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cmbCompany.setEditable(true);
        cmbCompany.setRequestFocusEnabled(true);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Firm Name");

        btnSaveClient.setBackground(new java.awt.Color(255, 255, 255));
        btnSaveClient.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSaveClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/add_administrator_16px.png"))); // NOI18N
        btnSaveClient.setText("ADD CLIENT");
        btnSaveClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveClientActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSaveClient)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbCompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSaveClient))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnSaveClient, cmbCompany, jLabel2});

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cmbProductName.setEditable(true);
        cmbProductName.setRequestFocusEnabled(true);

        cmbProcess.setEditable(true);
        cmbProcess.setRequestFocusEnabled(true);
        cmbProcess.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbProcessKeyPressed(evt);
            }
        });

        cmbMaterial.setEditable(true);
        cmbMaterial.setRequestFocusEnabled(true);
        cmbMaterial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMaterialKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Product Name");

        txtTSTRT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTSTRT.setText("0");
        txtTSTRT.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTSTRTFocusGained(evt);
            }
        });
        txtTSTRT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTSTRTKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("AMOUNT");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("T/START");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("W");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("OD");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("TL");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("RATE");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("QTY");

        txtW.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtW.setText("0");
        txtW.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtWFocusGained(evt);
            }
        });
        txtW.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtWKeyPressed(evt);
            }
        });

        txtOD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtOD.setText("0");
        txtOD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtODFocusGained(evt);
            }
        });
        txtOD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtODKeyPressed(evt);
            }
        });

        txtTL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTL.setText("0");
        txtTL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTLFocusGained(evt);
            }
        });
        txtTL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTLKeyPressed(evt);
            }
        });

        txtMDP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMDP.setText("0");
        txtMDP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMDPFocusGained(evt);
            }
        });
        txtMDP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMDPKeyPressed(evt);
            }
        });

        txtRate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRate.setText("0");
        txtRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRateFocusGained(evt);
            }
        });
        txtRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRateKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRateKeyReleased(evt);
            }
        });

        txtQTY.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtQTY.setText("0");
        txtQTY.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQTYFocusGained(evt);
            }
        });
        txtQTY.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQTYKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQTYKeyReleased(evt);
            }
        });

        txtAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAmount.setText("0");
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAmountFocusGained(evt);
            }
        });
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAmountKeyPressed(evt);
            }
        });

        btnAddProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnAddProduct.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/add_16px.png"))); // NOI18N
        btnAddProduct.setText("ADD");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });

        btnDeleteProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnDeleteProduct.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnDeleteProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/Delete_16px.png"))); // NOI18N
        btnDeleteProduct.setText("DELETE");
        btnDeleteProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProductActionPerformed(evt);
            }
        });

        btnClearProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnClearProduct.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnClearProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/erase_16px.png"))); // NOI18N
        btnClearProduct.setText("CLEAR");
        btnClearProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearProductActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("M/DP");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setText("Process");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel28.setText("Material");

        txtRemarks.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRemarks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRemarksKeyPressed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel29.setText("Remarks");

        btnAddProductName.setBackground(new java.awt.Color(255, 255, 255));
        btnAddProductName.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAddProductName.setText("+ PRODUCT");
        btnAddProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductNameActionPerformed(evt);
            }
        });

        btnAddProcessName.setBackground(new java.awt.Color(255, 255, 255));
        btnAddProcessName.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAddProcessName.setText("+ PROCESS");
        btnAddProcessName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProcessNameActionPerformed(evt);
            }
        });

        btnAddMaterialName.setBackground(new java.awt.Color(255, 255, 255));
        btnAddMaterialName.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAddMaterialName.setText("+ MATERIAL");
        btnAddMaterialName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMaterialNameActionPerformed(evt);
            }
        });

        btnPrintJobOrder.setBackground(new java.awt.Color(255, 255, 255));
        btnPrintJobOrder.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnPrintJobOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/print_16px.png"))); // NOI18N
        btnPrintJobOrder.setText("PRINT");
        btnPrintJobOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintJobOrderActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("DISC %");

        txtDiscount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDiscount.setText("0");
        txtDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiscountFocusGained(evt);
            }
        });
        txtDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiscountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscountKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbMaterial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbProductName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRemarks, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                            .addComponent(cmbProcess, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnAddProductName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddProcessName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddMaterialName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTSTRT, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtW)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtOD)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTL)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMDP)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRate)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtQTY, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDiscount)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAmount)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAddProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClearProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrintJobOrder)))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddProduct, btnClearProduct, btnDeleteProduct});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbProductName)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbProcess))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel28))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAddProcessName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAddMaterialName, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtQTY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel9)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel12)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtMDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtOD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTSTRT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClearProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPrintJobOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAddMaterialName, btnAddProcessName, btnAddProductName, cmbMaterial, cmbProcess, cmbProductName, jLabel27, jLabel28, jLabel29, jLabel5, txtRemarks});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtAmount, txtMDP, txtOD, txtQTY, txtRate, txtTL, txtTSTRT, txtW});

        jPanel4.setBackground(new java.awt.Color(255, 255, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("JOB ORDER NO.");

        txtJobOrderNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtJobOrderNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJobOrderNoKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("DATE");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setText("Page No.");

        txtPageNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPageNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPageNoKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtJobOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPageNo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel16)
                .addComponent(txtPageNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(txtJobOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7))
            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jDateChooser1, jLabel6, jLabel7, txtJobOrderNo});

        tblJobOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblJobOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblJobOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblJobOrderMouseClicked(evt);
            }
        });
        tblJobOrder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblJobOrderKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblJobOrder);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        setSize(new java.awt.Dimension(1099, 597));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        getJobNo();
        getDate();
        getClientData();
        getProductData();
        getProcessData();
        getMaterialData();
        clearProductField();
        getDataToTable();
    }//GEN-LAST:event_formWindowOpened

    private void btnSaveClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveClientActionPerformed
        // TODO add your handling code here:
        saveClient();
    }//GEN-LAST:event_btnSaveClientActionPerformed

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        // TODO add your handling code here:
        addProduct();
        getDataToTable();
        getJobOrderDataCALC();
    }//GEN-LAST:event_btnAddProductActionPerformed

    private void txtQTYKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQTYKeyReleased
        // TODO add your handling code here:
        calcAmt();
    }//GEN-LAST:event_txtQTYKeyReleased

    private void btnDeleteProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProductActionPerformed
        // TODO add your handling code here:
        deleteProduct();
        getDataToTable();
        getJobOrderDataCALC();
    }//GEN-LAST:event_btnDeleteProductActionPerformed

    private void txtQTYKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQTYKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtDiscount.requestFocus();
            txtDiscount.selectAll();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtRate.requestFocus();
        }
    }//GEN-LAST:event_txtQTYKeyPressed

    private void tblJobOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblJobOrderMouseClicked
        // TODO add your handling code here:
        getTableDataToField();
    }//GEN-LAST:event_tblJobOrderMouseClicked

    private void btnClearProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearProductActionPerformed
        // TODO add your handling code here:
        clearProductField();
    }//GEN-LAST:event_btnClearProductActionPerformed

    private void tblJobOrderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblJobOrderKeyPressed
        // TODO add your handling code here:
        getTableDataToField();
        if (evt.getKeyChar() == KeyEvent.VK_DELETE) {
            deleteProduct();
        }
    }//GEN-LAST:event_tblJobOrderKeyPressed

    private void txtJobOrderNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJobOrderNoKeyReleased
        // TODO add your handling code here:
        getProductData();
        getProcessData();
        getMaterialData();
        clearProductField();
        getDataToTable();
        getJobOrderDataCALC();
    }//GEN-LAST:event_txtJobOrderNoKeyReleased

    private void btnAddProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductNameActionPerformed
        // TODO add your handling code here:
        addProductName();
        getProductData();
    }//GEN-LAST:event_btnAddProductNameActionPerformed

    private void btnAddProcessNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProcessNameActionPerformed
        // TODO add your handling code here:
        addProcessName();
        getProcessData();
    }//GEN-LAST:event_btnAddProcessNameActionPerformed

    private void btnAddMaterialNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddMaterialNameActionPerformed
        // TODO add your handling code here:
        addMaterialName();
        getMaterialData();
    }//GEN-LAST:event_btnAddMaterialNameActionPerformed

    private void btnPrintJobOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintJobOrderActionPerformed
        // TODO add your handling code here:
        printOptions();
    }//GEN-LAST:event_btnPrintJobOrderActionPerformed

    private void txtDiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtAmount.requestFocus();
            txtAmount.selectAll();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtQTY.requestFocus();
        }
    }//GEN-LAST:event_txtDiscountKeyPressed

    private void txtDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyReleased
        // TODO add your handling code here:
        calcAmt();
    }//GEN-LAST:event_txtDiscountKeyReleased

    private void txtTSTRTKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTSTRTKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtW.requestFocus();
            txtW.selectAll();
        }
if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtRemarks.requestFocus();
        }
    }//GEN-LAST:event_txtTSTRTKeyPressed

    private void txtWKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtOD.requestFocus();
            txtOD.selectAll();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtTSTRT.requestFocus();
        }
    }//GEN-LAST:event_txtWKeyPressed

    private void txtODKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtODKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtTL.requestFocus();
            txtTL.selectAll();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtW.requestFocus();
        }
    }//GEN-LAST:event_txtODKeyPressed

    private void txtTLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTLKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtMDP.requestFocus();
            txtMDP.selectAll();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtOD.requestFocus();
        }
    }//GEN-LAST:event_txtTLKeyPressed

    private void txtMDPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMDPKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtRate.requestFocus();
            txtRate.selectAll();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtTL.requestFocus();
        }
    }//GEN-LAST:event_txtMDPKeyPressed

    private void txtRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtQTY.requestFocus();
            txtQTY.selectAll();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtMDP.requestFocus();
        }
    }//GEN-LAST:event_txtRateKeyPressed

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            addProduct();
            getDataToTable();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtDiscount.requestFocus();
        }
    }//GEN-LAST:event_txtAmountKeyPressed

    private void txtRemarksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRemarksKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtTSTRT.requestFocus();
            txtTSTRT.selectAll();
        }
if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            cmbMaterial.requestFocus();
        }
    }//GEN-LAST:event_txtRemarksKeyPressed

    private void txtRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRateKeyReleased
        // TODO add your handling code here:
        calcAmt();
    }//GEN-LAST:event_txtRateKeyReleased

    private void txtPageNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPageNoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPageNoKeyReleased

    private void txtTSTRTFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTSTRTFocusGained
        // TODO add your handling code here:
        txtTSTRT.selectAll();
    }//GEN-LAST:event_txtTSTRTFocusGained

    private void txtWFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWFocusGained
        // TODO add your handling code here:
        txtW.selectAll();
    }//GEN-LAST:event_txtWFocusGained

    private void txtODFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtODFocusGained
        // TODO add your handling code here:
        txtOD.selectAll();
    }//GEN-LAST:event_txtODFocusGained

    private void txtTLFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTLFocusGained
        // TODO add your handling code here:
        txtTL.selectAll();
    }//GEN-LAST:event_txtTLFocusGained

    private void txtMDPFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMDPFocusGained
        // TODO add your handling code here:
        txtMDP.selectAll();
    }//GEN-LAST:event_txtMDPFocusGained

    private void txtRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRateFocusGained
        // TODO add your handling code here:
        txtRate.selectAll();
    }//GEN-LAST:event_txtRateFocusGained

    private void txtQTYFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQTYFocusGained
        // TODO add your handling code here:
        txtQTY.selectAll();
    }//GEN-LAST:event_txtQTYFocusGained

    private void txtDiscountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountFocusGained
        // TODO add your handling code here:
        txtDiscount.selectAll();
    }//GEN-LAST:event_txtDiscountFocusGained

    private void txtAmountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusGained
        // TODO add your handling code here:
        txtAmount.selectAll();
    }//GEN-LAST:event_txtAmountFocusGained

    private void cmbMaterialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMaterialKeyPressed
        // TODO add your handling code here:
if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            cmbProcess.requestFocus();
        }
    }//GEN-LAST:event_cmbMaterialKeyPressed

    private void cmbProcessKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbProcessKeyPressed
        // TODO add your handling code here:
if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            cmbProductName.requestFocus();
        }
    }//GEN-LAST:event_cmbProcessKeyPressed

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
            java.util.logging.Logger.getLogger( JobOrderMaster.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger( JobOrderMaster.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger( JobOrderMaster.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger( JobOrderMaster.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable() {
            public void run() {
                new JobOrderMaster().setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddMaterialName;
    private javax.swing.JButton btnAddProcessName;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnAddProductName;
    private javax.swing.JButton btnClearProduct;
    private javax.swing.JButton btnDeleteProduct;
    private javax.swing.JButton btnPrintJobOrder;
    private javax.swing.JButton btnSaveClient;
    private javax.swing.JComboBox<String> cmbCompany;
    private javax.swing.JComboBox<String> cmbMaterial;
    private javax.swing.JComboBox<String> cmbProcess;
    private javax.swing.JComboBox<String> cmbProductName;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblJobOrder;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtJobOrderNo;
    private javax.swing.JTextField txtMDP;
    private javax.swing.JTextField txtOD;
    private javax.swing.JTextField txtPageNo;
    private javax.swing.JTextField txtQTY;
    private javax.swing.JTextField txtRate;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtTL;
    private javax.swing.JTextField txtTSTRT;
    private javax.swing.JTextField txtW;
    // End of variables declaration//GEN-END:variables
}
