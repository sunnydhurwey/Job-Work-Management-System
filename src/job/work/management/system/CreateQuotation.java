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
public class CreateQuotation extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null, rs2 = null;
    PreparedStatement pst = null, pst2 = null;

    /**
     * Creates new form CreateQuotation
     */
    public CreateQuotation() {
        initComponents();
        try {
            conn = javaconnect.ConnectDB();
        } catch (UnknownHostException e) {
            System.out.println( e );
        }
        this.setIconImage( new ImageIcon( getClass().getResource( "LOGO.png" ) ).getImage() );
        AutoCompleteDecorator.decorate( cmbClientName );
        AutoCompleteDecorator.decorate( cmbCompany );
        AutoCompleteDecorator.decorate( cmbAddress );
        AutoCompleteDecorator.decorate( cmbEmail );
        AutoCompleteDecorator.decorate( cmbMobile );
        AutoCompleteDecorator.decorate( cmbLandline );
        AutoCompleteDecorator.decorate( cmbGSTIN );

        AutoCompleteDecorator.decorate( cmbProductName );
        AutoCompleteDecorator.decorate( cmbProcess );
        AutoCompleteDecorator.decorate( cmbMaterial );
    }

    //Program to set single instance of ManageClients
    private static CreateQuotation obj = null;

    public static CreateQuotation getObj() {
        if (obj == null) {
            obj = new CreateQuotation();
        }
        return obj;
    }

    //Function or method to get data from database
    int qno = 0;

    public void getQuotationID() {
        try {
            String sql = "SELECT * FROM quotation";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            while (rs.next()) {
                qno = rs.getInt( "quotationno" );
            }
            qno++;
            txtQuotationNo.setText( String.valueOf( qno ) );
            btnSaveQuotation.setEnabled( true );
            btnUpdateQuotation.setEnabled( false );
            btnDeleteQuotation.setEnabled( false );
            btnPrintQuotation.setEnabled( false );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getQuotation() Exception", JOptionPane.ERROR_MESSAGE );
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
        txtJobOrderNo.setText( "" );
        cmbProductName.setSelectedIndex( 0 );
        cmbProcess.setSelectedIndex( 0 );
        cmbMaterial.setSelectedIndex( 0 );
        txtRemarks.setText( "" );
        txtTSTRT.setText( "0" );
        txtW.setText( "0" );
        txtOD.setText( "0" );
        txtTL.setText( "0" );
        txtMDP.setText( "0" );
        txtDisc.setText( "0" );
        txtRate.setText( "0" );
        txtQTY.setText( "0" );
        txtAmount.setText( "0" );
    }

    //Function or method to clear Quotation Fields
    public void clearQuotationTotals() {
        txtGSTPer.setText( "0" );
        txtBasicPrice.setText( "0" );
        txtDiscount.setText( "0" );
        txtGSTAmount.setText( "0" );
        txtGrandTotal.setText( "0" );
    }

    //Function or method to save client
    public void saveClient() {
        try {
            String sql = "INSERT INTO clients (clientname,clientcompanyname,address,email,mobile,landline,gstin) VALUES (?,?,?,?,?,?,?)";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, cmbClientName.getEditor().getItem().toString() );
            pst.setString( 2, cmbCompany.getEditor().getItem().toString() );
            pst.setString( 3, cmbAddress.getEditor().getItem().toString() );
            pst.setString( 4, cmbEmail.getEditor().getItem().toString() );
            pst.setString( 5, cmbMobile.getEditor().getItem().toString() );
            pst.setString( 6, cmbLandline.getEditor().getItem().toString() );
            pst.setString( 7, cmbGSTIN.getEditor().getItem().toString() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Client added to database", "CLIENT ADDED", JOptionPane.PLAIN_MESSAGE );
            btnSaveClient.setEnabled( false );
            cmbClientName.setEnabled( false );
            cmbCompany.setEnabled( false );
            cmbAddress.setEnabled( false );
            cmbEmail.setEnabled( false );
            cmbMobile.setEnabled( false );
            cmbLandline.setEnabled( false );
            cmbGSTIN.setEnabled( false );
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
            String sql = "SELECT uid AS 'UID',joborderno AS 'JOB ORDER NO',productname AS 'PRODUCT NAME',processname AS 'PROCESS NAME',"
                    + "materialname AS 'MATERIAL NAME',tstrt AS 'T/START',w AS 'W',od AS 'OD',tl AS 'TL',mdp AS 'M/DP',rate AS 'RATE',qty AS 'QUANTITY',"
                    + "discountper AS 'DISC. %',discountamt AS 'DISC. AMT',amount AS 'AMOUNT' FROM jobordermaster WHERE joborderno='" + txtJobOrderNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblQuotation.setModel( DbUtils.resultSetToTableModel( rs ) );
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

    //Function or method to set totcalcfields zero
    public void setTotCalFieldsZero() {
        txtGSTPer.setText( "0" );
        txtBasicPrice.setText( "0" );
        txtDiscount.setText( "0" );
        txtGSTAmount.setText( "0" );
        txtGrandTotal.setText( "0" );
    }
    //Function to getData
    double disc = 0, basicprice = 0, dbamt = 0, dbdisc = 0, dbrate = 0, dbqty = 0;
    String cn;

    public void getData() {
        try {
            disc = 0;
            basicprice = 0;
            dbamt = 0;
            dbdisc = 0;
            dbrate = 0;
            dbqty = 0;
            String sql = "SELECT * FROM jobordermaster WHERE joborderno='" + txtJobOrderNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            while (rs.next()) {
                cn = rs.getString( "clientname" );
                dbamt = rs.getDouble( "amount" );
                dbdisc = rs.getDouble( "discountper" );
                dbrate = rs.getDouble( "rate" );
                dbqty = rs.getDouble( "qty" );
                disc = disc + ((dbrate * dbqty * dbdisc) / 100);
                basicprice = basicprice + dbamt;
            }
            txtBasicPrice.setText( String.valueOf( basicprice ) );
            txtDiscount.setText( String.valueOf( disc ) );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getData() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //function or method to get client data
    public void getClientData() {
        try {
            cmbClientName.addItem( "" );
            cmbCompany.addItem( "" );
            cmbAddress.addItem( "" );
            cmbEmail.addItem( "" );
            cmbMobile.addItem( "" );
            cmbLandline.addItem( "" );
            cmbGSTIN.addItem( "" );
            String sql = "SELECT * FROM clients";
            pst = conn.prepareStatement( sql );
            rs2 = pst.executeQuery();
            while (rs2.next()) {
                cmbClientName.addItem( rs2.getString( "clientname" ) );
                cmbCompany.addItem( rs2.getString( "clientcompanyname" ) );
                cmbAddress.addItem( rs2.getString( "address" ) );
                cmbEmail.addItem( rs2.getString( "email" ) );
                cmbMobile.addItem( rs2.getString( "mobile" ) );
                cmbLandline.addItem( rs2.getString( "landline" ) );
                cmbGSTIN.addItem( rs2.getString( "gstin" ) );
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

    //Function or method to get saved quotation data calculation
    public void getQuotationDataCALC() {
        try {
            String sql = "SELECT * FROM quotation WHERE quotationno='" + txtQuotationNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs2 = pst.executeQuery();
            if (rs2.next()) {
                cmbClientName.setSelectedItem( rs2.getString( "clientname" ) );
                cmbCompany.setSelectedItem( rs2.getString( "company" ) );
                cmbAddress.setSelectedItem( rs2.getString( "address" ) );
                cmbEmail.setSelectedItem( rs2.getString( "email" ) );
                cmbMobile.setSelectedItem( rs2.getString( "mobile" ) );
                cmbLandline.setSelectedItem( rs2.getString( "landline" ) );
                cmbGSTIN.setSelectedItem( rs2.getString( "gstin" ) );
                txtJobOrderNo.setText( rs2.getString( "joborderno" ) );
                txtGSTPer.setText( rs2.getString( "gstper" ) );
                txtBasicPrice.setText( rs2.getString( "basicprice" ) );
                txtDiscount.setText( rs2.getString( "discount" ) );
                txtGSTAmount.setText( rs2.getString( "gstamt" ) );
                txtGrandTotal.setText( rs2.getString( "totalamt" ) );
                btnSaveQuotation.setEnabled( false );
                btnUpdateQuotation.setEnabled( true );
                btnDeleteQuotation.setEnabled( true );
                btnPrintQuotation.setEnabled( true );
            } else {
                cmbClientName.setSelectedItem( "" );
                cmbCompany.setSelectedItem( "" );
                cmbAddress.setSelectedItem( "" );
                cmbEmail.setSelectedItem( "" );
                cmbMobile.setSelectedItem( "" );
                cmbLandline.setSelectedItem( "" );
                cmbGSTIN.setSelectedItem( "" );
                txtGSTPer.setText( "0" );
                txtBasicPrice.setText( "0" );
                txtDiscount.setText( "0" );
                txtGSTAmount.setText( "0" );
                txtGrandTotal.setText( "0" );
                btnSaveQuotation.setEnabled( true );
                btnUpdateQuotation.setEnabled( false );
                btnDeleteQuotation.setEnabled( false );
                btnPrintQuotation.setEnabled( false );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getQuotationDataCALC() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs2.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e, "getQuotationDataCALC() Exception", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    //Function or method to clear client data field
    public void clearClientData() {
        cmbClientName.setSelectedItem( "" );
        cmbCompany.setSelectedItem( "" );
        cmbAddress.setSelectedItem( "" );
        cmbEmail.setSelectedItem( "" );
        cmbMobile.setSelectedItem( "" );
        cmbLandline.setSelectedItem( "" );
        cmbGSTIN.setSelectedItem( "" );
    }

    //Function to populate combobox on client name input
    public void getClientDataFromJobOrder() {
        try {
            cmbClientName.setSelectedItem( "" );
            cmbCompany.setSelectedItem( "" );
            cmbAddress.setSelectedItem( "" );
            cmbEmail.setSelectedItem( "" );
            cmbMobile.setSelectedItem( "" );
            cmbLandline.setSelectedItem( "" );
            cmbGSTIN.setSelectedItem( "" );
            String sql = "SELECT * FROM jobordermaster WHERE joborderno='" + txtJobOrderNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs2 = pst.executeQuery();
            if (rs2.next()) {
                cmbClientName.setSelectedItem( rs2.getString( "clientname" ) );
                cmbCompany.setSelectedItem( rs2.getString( "company" ) );
                cmbAddress.setSelectedItem( rs2.getString( "clientaddress" ) );
                cmbEmail.setSelectedItem( rs2.getString( "clientemail" ) );
                cmbMobile.setSelectedItem( rs2.getString( "clientmobile" ) );
                cmbLandline.setSelectedItem( rs2.getString( "clientlandline" ) );
                cmbGSTIN.setSelectedItem( rs2.getString( "clientgstin" ) );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getClientDataFromJobOrder() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs2.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }
    //Function to populate other client data comboboxes on Enter key press on client name
    boolean clientexist = false;
    int clientID = 0;
    String clientname;

    public void setDataToFieldsOnNameInput() {
        clientname = cmbClientName.getSelectedItem().toString();
        try {
            String sql = "SELECT * FROM clients WHERE clientname LIKE '" + clientname + "'";
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
                    cmbAddress.setSelectedItem( rs.getString( "address" ) );
                    cmbEmail.setSelectedItem( rs.getString( "email" ) );
                    cmbMobile.setSelectedItem( rs.getString( "mobile" ) );
                    cmbLandline.setSelectedItem( rs.getString( "landline" ) );
                    cmbGSTIN.setSelectedItem( rs.getString( "gstin" ) );
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

    //Function or method to get selected data from table to field
    int row;
    String count, tblClick;

    public void getTableDataToField() {
        try {
            row = tblQuotation.getSelectedRow();
            tblClick = tblQuotation.getModel().getValueAt( row, 0 ).toString();
            String sql = "SELECT * FROM jobordermaster WHERE uid='" + tblClick + "'";
            pst2 = conn.prepareStatement( sql );
            rs2 = pst2.executeQuery();
            if (rs2.next()) {
                cmbClientName.setSelectedItem( rs2.getString( "clientname" ) );
                cmbProductName.setSelectedItem( rs2.getString( "productname" ) );
                cmbProcess.setSelectedItem( rs2.getString( "processname" ) );
                cmbMaterial.setSelectedItem( rs2.getString( "materialname" ) );
                txtRemarks.setText( rs2.getString( "remark" ) );
                txtTSTRT.setText( rs2.getString( "tstrt" ) );
                txtW.setText( rs2.getString( "w" ) );
                txtOD.setText( rs2.getString( "od" ) );
                txtTL.setText( rs2.getString( "tl" ) );
                txtMDP.setText( rs2.getString( "mdp" ) );
                txtDisc.setText( rs2.getString( "discountper" ) );
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

    //Function or method to calculate Rate X Quantity = AMOUNT
    double rate = 0, quantity = 0, amount = 0;

    public void calcAmt() {
        rate = 0;
        quantity = 0;
        disc = 0;
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
            disc = 0;
        } else {
            disc = Double.parseDouble( txtDiscount.getText() );
        }
        amount = rate * quantity;
        disc = (amount * disc) / 100;
        amount = amount - disc;
        txtAmount.setText( String.valueOf( amount ) );
    }

    //Funtion or method to calculate GRAND TOTAL
    double gstper = 0, discount = 0, subtotal = 0, gstamt = 0, grandTotal = 0;

    public void calcGT() {
        if (txtGSTPer.getText() != null || !"".equals( txtGSTPer.getText() )) {
            gstper = Double.parseDouble( txtGSTPer.getText() );
        } else {
            gstper = 0;
        }

        gstamt = (basicprice * gstper) / 100;
        txtGSTAmount.setText( String.valueOf( gstamt ) );
        grandTotal = basicprice + gstamt;
        txtGrandTotal.setText( String.valueOf( grandTotal ) );
    }

    //Function to save quotation
    public void saveQuotation() {
        try {
            String sql = "INSERT INTO quotation (quotationno,date,clientname,company,address,email,mobile,landline,gstin,basicprice,"
                    + "discount,gstper,gstamt,totalamt,joborderno,note) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, txtQuotationNo.getText() );
            pst.setString( 2, ((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText() );
            pst.setString( 3, cmbClientName.getEditor().getItem().toString() );
            pst.setString( 4, cmbCompany.getSelectedItem().toString() );
            pst.setString( 5, cmbAddress.getSelectedItem().toString() );
            pst.setString( 6, cmbEmail.getSelectedItem().toString() );
            pst.setString( 7, cmbMobile.getSelectedItem().toString() );
            pst.setString( 8, cmbLandline.getSelectedItem().toString() );
            pst.setString( 9, cmbGSTIN.getSelectedItem().toString() );
            pst.setString( 10, txtBasicPrice.getText() );
            pst.setString( 11, txtDiscount.getText() );
            pst.setString( 12, txtGSTPer.getText() );
            pst.setString( 13, txtGSTAmount.getText() );
            pst.setString( 14, txtGrandTotal.getText() );
            pst.setString( 15, txtJobOrderNo.getText() );
            pst.setString( 16, txtNote.getText() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Quotation saved to database", "Saved", JOptionPane.PLAIN_MESSAGE );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "saveQuotation() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to update existing quotation
    public void updateQuotation() {
        try {
            String sql = "UPDATE quotation SET date=?,clientname=?,company=?,address=?,email=?,mobile=?,"
                    + "landline=?,gstin=?,basicprice=?,discount=?,gstper=?,gstamt=?,totalamt=?,note=?"
                    + "WHERE quotationno='" + txtQuotationNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, ((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText() );
            pst.setString( 2, cmbClientName.getEditor().getItem().toString() );
            pst.setString( 3, cmbCompany.getSelectedItem().toString() );
            pst.setString( 4, cmbAddress.getSelectedItem().toString() );
            pst.setString( 5, cmbEmail.getSelectedItem().toString() );
            pst.setString( 6, cmbMobile.getSelectedItem().toString() );
            pst.setString( 7, cmbLandline.getSelectedItem().toString() );
            pst.setString( 8, cmbGSTIN.getSelectedItem().toString() );
            pst.setString( 9, txtBasicPrice.getText() );
            pst.setString( 10, txtDiscount.getText() );
            pst.setString( 11, txtGSTPer.getText() );
            pst.setString( 12, txtGSTAmount.getText() );
            pst.setString( 13, txtGrandTotal.getText() );
            pst.setString( 14, txtNote.getText() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Quotation modified on database", "Updated", JOptionPane.PLAIN_MESSAGE );
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "updateQuotation() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to delete saved Quotation
    public void deleteQuotation() {
        try {
            String sql = "DELETE FROM quotation WHERE quotationno='" + txtQuotationNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            pst.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, "deleteQuotation() Exception", "Quotation Deleted", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
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
            String qno = txtQuotationNo.getText();
            String jno = txtJobOrderNo.getText();
            try {
                String sql = "Select * from quotation,jobordermaster,companydetails where quotation.quotationno='" + qno + "' "
                        + "AND jobordermaster.joborderno='" + jno + "' AND companydetails.c_uid=(SELECT MIN(companydetails.c_uid) FROM companydetails)";
                JasperDesign jd = JRXmlLoader.load( "src/reports/quotationPT.jrxml" );
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
        jLabel1 = new javax.swing.JLabel();
        cmbClientName = new javax.swing.JComboBox<>();
        cmbCompany = new javax.swing.JComboBox<>();
        cmbAddress = new javax.swing.JComboBox<>();
        cmbEmail = new javax.swing.JComboBox<>();
        cmbMobile = new javax.swing.JComboBox<>();
        cmbLandline = new javax.swing.JComboBox<>();
        cmbGSTIN = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
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
        jLabel21 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtDisc = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtQuotationNo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel30 = new javax.swing.JLabel();
        txtJobOrderNo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblQuotation = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txtGSTPer = new javax.swing.JTextField();
        txtBasicPrice = new javax.swing.JTextField();
        txtDiscount = new javax.swing.JTextField();
        txtGSTAmount = new javax.swing.JTextField();
        txtGrandTotal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        btnSaveQuotation = new javax.swing.JButton();
        btnUpdateQuotation = new javax.swing.JButton();
        btnDeleteQuotation = new javax.swing.JButton();
        btnPrintQuotation = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("QUOTATION BUILDER - JOB WORK MANAGEMENT SYSTEM");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Client Name");

        cmbClientName.setEditable(true);
        cmbClientName.setRequestFocusEnabled(true);

        cmbCompany.setEditable(true);
        cmbCompany.setRequestFocusEnabled(true);

        cmbAddress.setEditable(true);
        cmbAddress.setRequestFocusEnabled(true);

        cmbEmail.setEditable(true);
        cmbEmail.setRequestFocusEnabled(true);

        cmbMobile.setEditable(true);
        cmbMobile.setRequestFocusEnabled(true);

        cmbLandline.setEditable(true);
        cmbLandline.setRequestFocusEnabled(true);

        cmbGSTIN.setEditable(true);
        cmbGSTIN.setRequestFocusEnabled(true);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Clients Company Name");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Email");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Mobile");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel24.setText("Address");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel25.setText("LandLine");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel26.setText("GSTIN");

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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbClientName, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbAddress, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMobile, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbLandline, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbGSTIN, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(btnSaveClient)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel24)
                    .addComponent(cmbClientName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(btnSaveClient)
                    .addComponent(cmbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbGSTIN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbLandline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnSaveClient, cmbAddress, cmbClientName, cmbCompany, cmbEmail, cmbGSTIN, cmbLandline, cmbMobile, jLabel1, jLabel2, jLabel24, jLabel25, jLabel26, jLabel3, jLabel4});

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cmbProductName.setEditable(true);
        cmbProductName.setRequestFocusEnabled(true);

        cmbProcess.setEditable(true);
        cmbProcess.setRequestFocusEnabled(true);

        cmbMaterial.setEditable(true);
        cmbMaterial.setRequestFocusEnabled(true);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Product Name");

        txtTSTRT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTSTRT.setText("0");

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

        txtOD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtOD.setText("0");

        txtTL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTL.setText("0");

        txtMDP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMDP.setText("0");

        txtRate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRate.setText("0");

        txtQTY.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtQTY.setText("0");
        txtQTY.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQTYKeyReleased(evt);
            }
        });

        txtAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAmount.setText("0");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("M/DP");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setText("Process");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel28.setText("Material");

        txtRemarks.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel29.setText("Remarks");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Disc.%");

        txtDisc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDisc.setText("0");
        txtDisc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiscKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscKeyReleased(evt);
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
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtQTY)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDisc, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAmount)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
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
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtQTY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDisc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel21)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtMDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel9)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(jLabel11)
                                .addComponent(jLabel12))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtOD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTSTRT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbMaterial, cmbProcess, cmbProductName, jLabel27, jLabel28, jLabel29, jLabel5, txtRemarks});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtAmount, txtMDP, txtOD, txtQTY, txtRate, txtTL, txtTSTRT, txtW});

        jPanel4.setBackground(new java.awt.Color(255, 255, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("QUOTATION NO.");

        txtQuotationNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtQuotationNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuotationNoActionPerformed(evt);
            }
        });
        txtQuotationNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQuotationNoKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("DATE");

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel30.setText("JOB ORDER NO.");

        txtJobOrderNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtJobOrderNo.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtJobOrderNoInputMethodTextChanged(evt);
            }
        });
        txtJobOrderNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJobOrderNoActionPerformed(evt);
            }
        });
        txtJobOrderNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJobOrderNoKeyReleased(evt);
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
                .addComponent(txtQuotationNo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtJobOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(txtQuotationNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtJobOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jDateChooser1, jLabel6, jLabel7, txtQuotationNo});

        tblQuotation.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblQuotation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblQuotation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQuotationMouseClicked(evt);
            }
        });
        tblQuotation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblQuotationKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblQuotation);

        jPanel5.setBackground(new java.awt.Color(204, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("GST %");

        txtGSTPer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGSTPer.setText("0");
        txtGSTPer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtGSTPerFocusGained(evt);
            }
        });
        txtGSTPer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGSTPerKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGSTPerKeyReleased(evt);
            }
        });

        txtBasicPrice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBasicPrice.setText("0");
        txtBasicPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBasicPriceKeyPressed(evt);
            }
        });

        txtDiscount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDiscount.setText("0");
        txtDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiscountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscountKeyReleased(evt);
            }
        });

        txtGSTAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGSTAmount.setText("0");
        txtGSTAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGSTAmountKeyPressed(evt);
            }
        });

        txtGrandTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGrandTotal.setText("0");
        txtGrandTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGrandTotalKeyPressed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Basic Price");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Discount");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("GST Amount");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("GRAND TOTAL");

        btnSaveQuotation.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSaveQuotation.setText("SAVE");
        btnSaveQuotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveQuotationActionPerformed(evt);
            }
        });

        btnUpdateQuotation.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnUpdateQuotation.setText("UPDATE");
        btnUpdateQuotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateQuotationActionPerformed(evt);
            }
        });

        btnDeleteQuotation.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnDeleteQuotation.setText("DELETE");
        btnDeleteQuotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteQuotationActionPerformed(evt);
            }
        });

        btnPrintQuotation.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnPrintQuotation.setText("PRINT");
        btnPrintQuotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintQuotationActionPerformed(evt);
            }
        });

        txtNote.setColumns(20);
        txtNote.setRows(5);
        txtNote.setBorder(javax.swing.BorderFactory.createTitledBorder("Note"));
        jScrollPane2.setViewportView(txtNote);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtGSTPer)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBasicPrice)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDiscount)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtGSTAmount)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtGrandTotal)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(btnSaveQuotation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdateQuotation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteQuotation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrintQuotation)))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDeleteQuotation, btnPrintQuotation, btnSaveQuotation, btnUpdateQuotation});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBasicPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGSTAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGSTPer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdateQuotation)
                            .addComponent(btnDeleteQuotation)
                            .addComponent(btnPrintQuotation)
                            .addComponent(btnSaveQuotation))))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        setSize(new java.awt.Dimension(1192, 607));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        getQuotationID();
        getDate();
        getClientData();
        getProductData();
        getProcessData();
        getMaterialData();
        clearProductField();
        clearQuotationTotals();
        //AFTER FORM IS LOADED
        setTotCalFieldsZero();
        getData();
        getDataToTable();
    }//GEN-LAST:event_formWindowOpened

    private void btnSaveClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveClientActionPerformed
        // TODO add your handling code here:
        saveClient();
    }//GEN-LAST:event_btnSaveClientActionPerformed

    private void txtQuotationNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuotationNoActionPerformed
        // TODO add your handling code here:
        getDataToTable();
    }//GEN-LAST:event_txtQuotationNoActionPerformed

    private void txtQTYKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQTYKeyReleased
        // TODO add your handling code here:
        calcAmt();
    }//GEN-LAST:event_txtQTYKeyReleased

    private void tblQuotationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQuotationMouseClicked
        // TODO add your handling code here:
        getTableDataToField();
    }//GEN-LAST:event_tblQuotationMouseClicked

    private void tblQuotationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblQuotationKeyPressed
        // TODO add your handling code here:
        getTableDataToField();
    }//GEN-LAST:event_tblQuotationKeyPressed

    private void txtGSTPerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTPerKeyReleased
        // TODO add your handling code here:
        if (!"".equals( txtGSTPer.getText() )) {
            calcGT();
        } else {
            txtGSTPer.setText( "0" );
            calcGT();
        }
    }//GEN-LAST:event_txtGSTPerKeyReleased

    private void txtDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyReleased
        // TODO add your handling code here:
        if (!"".equals( txtDiscount.getText() )) {
            calcGT();
        } else {
            txtDiscount.setText( "0" );
            calcGT();
        }
    }//GEN-LAST:event_txtDiscountKeyReleased

    private void btnSaveQuotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveQuotationActionPerformed
        // TODO add your handling code here:
        saveQuotation();
        getQuotationDataCALC();
    }//GEN-LAST:event_btnSaveQuotationActionPerformed

    private void btnUpdateQuotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateQuotationActionPerformed
        // TODO add your handling code here:
        updateQuotation();
    }//GEN-LAST:event_btnUpdateQuotationActionPerformed

    private void btnDeleteQuotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteQuotationActionPerformed
        // TODO add your handling code here:
        deleteQuotation();
    }//GEN-LAST:event_btnDeleteQuotationActionPerformed

    private void txtQuotationNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuotationNoKeyReleased
        // TODO add your handling code here:
        getProductData();
        getProcessData();
        getMaterialData();
        clearProductField();
        clearQuotationTotals();

        //AFTER FROM IS LOADED
        setTotCalFieldsZero();
        getData();
        getClientDataFromJobOrder();
        getQuotationDataCALC();
        getDataToTable();
    }//GEN-LAST:event_txtQuotationNoKeyReleased

    private void btnPrintQuotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintQuotationActionPerformed
        // TODO add your handling code here:
        printOptions();
    }//GEN-LAST:event_btnPrintQuotationActionPerformed

    private void txtDiscKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscKeyPressed

    private void txtDiscKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscKeyReleased
        // TODO add your handling code here:
        calcAmt();
    }//GEN-LAST:event_txtDiscKeyReleased

    private void txtJobOrderNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJobOrderNoKeyReleased
        // TODO add your handling code here:
        getData();
        getDataToTable();
        getClientDataFromJobOrder();
    }//GEN-LAST:event_txtJobOrderNoKeyReleased

    private void txtJobOrderNoInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtJobOrderNoInputMethodTextChanged
        // TODO add your handling code here:
        getData();
        getDataToTable();
        getClientDataFromJobOrder();
    }//GEN-LAST:event_txtJobOrderNoInputMethodTextChanged

    private void txtJobOrderNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJobOrderNoActionPerformed
        // TODO add your handling code here:
        getData();
        getDataToTable();
        getClientDataFromJobOrder();
    }//GEN-LAST:event_txtJobOrderNoActionPerformed

    private void txtGSTPerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGSTPerFocusGained
        // TODO add your handling code here:
        txtGSTPer.selectAll();
    }//GEN-LAST:event_txtGSTPerFocusGained

    private void txtGSTPerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTPerKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtBasicPrice.requestFocus();
            txtBasicPrice.selectAll();
        }
    }//GEN-LAST:event_txtGSTPerKeyPressed

    private void txtBasicPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBasicPriceKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtDiscount.requestFocus();
            txtDiscount.selectAll();
        }
    }//GEN-LAST:event_txtBasicPriceKeyPressed

    private void txtDiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtGSTAmount.requestFocus();
            txtGSTAmount.selectAll();
        }
    }//GEN-LAST:event_txtDiscountKeyPressed

    private void txtGSTAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTAmountKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtGrandTotal.requestFocus();
            txtGrandTotal.selectAll();
        }
    }//GEN-LAST:event_txtGSTAmountKeyPressed

    private void txtGrandTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGrandTotalKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            saveQuotation();
            txtQuotationNo.requestFocus();
            txtQuotationNo.selectAll();
        }
    }//GEN-LAST:event_txtGrandTotalKeyPressed

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
            java.util.logging.Logger.getLogger( CreateQuotation.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger( CreateQuotation.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger( CreateQuotation.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger( CreateQuotation.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable() {
            public void run() {
                new CreateQuotation().setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeleteQuotation;
    private javax.swing.JButton btnPrintQuotation;
    private javax.swing.JButton btnSaveClient;
    private javax.swing.JButton btnSaveQuotation;
    private javax.swing.JButton btnUpdateQuotation;
    private javax.swing.JComboBox<String> cmbAddress;
    private javax.swing.JComboBox<String> cmbClientName;
    private javax.swing.JComboBox<String> cmbCompany;
    private javax.swing.JComboBox<String> cmbEmail;
    private javax.swing.JComboBox<String> cmbGSTIN;
    private javax.swing.JComboBox<String> cmbLandline;
    private javax.swing.JComboBox<String> cmbMaterial;
    private javax.swing.JComboBox<String> cmbMobile;
    private javax.swing.JComboBox<String> cmbProcess;
    private javax.swing.JComboBox<String> cmbProductName;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblQuotation;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtBasicPrice;
    private javax.swing.JTextField txtDisc;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtGSTAmount;
    private javax.swing.JTextField txtGSTPer;
    private javax.swing.JTextField txtGrandTotal;
    private javax.swing.JTextField txtJobOrderNo;
    private javax.swing.JTextField txtMDP;
    private javax.swing.JTextArea txtNote;
    private javax.swing.JTextField txtOD;
    private javax.swing.JTextField txtQTY;
    private javax.swing.JTextField txtQuotationNo;
    private javax.swing.JTextField txtRate;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtTL;
    private javax.swing.JTextField txtTSTRT;
    private javax.swing.JTextField txtW;
    // End of variables declaration//GEN-END:variables
}
