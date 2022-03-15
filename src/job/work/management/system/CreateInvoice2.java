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
public class CreateInvoice2 extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null, rs2 = null;
    PreparedStatement pst = null, pst2 = null;

    /**
     * Creates new form CreateInvoice
     */
    public CreateInvoice2() {
        initComponents();
        try {
            conn = javaconnect.ConnectDB();
        } catch (UnknownHostException e) {
            System.out.println( e );
        }
        this.setIconImage( new ImageIcon( getClass().getResource( "LOGO.png" ) ).getImage() );
        AutoCompleteDecorator.decorate( cmbCompany );
        AutoCompleteDecorator.decorate( cmbAddress );

        AutoCompleteDecorator.decorate( cmbProductName );
        AutoCompleteDecorator.decorate( cmbProcess );
        AutoCompleteDecorator.decorate( cmbMaterial );
    }

    //Program to set single instance of ManageClients
    private static CreateInvoice2 obj = null;

    public static CreateInvoice2 getObj() {
        if (obj == null) {
            obj = new CreateInvoice2();
        }
        return obj;
    }

    //Function or method to get data from database
    int invno = 0;

    public void getInvoiceNo() {
        try {
            String sql = "SELECT * FROM invoice2";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            while (rs.next()) {
                invno = rs.getInt( "invoiceno" );
            }
            invno++;
            txtInvoiceNo.setText( String.valueOf( invno ) );
            btnSaveInvoice.setEnabled( true );
            btnUpdateInvoice.setEnabled( false );
            btnDeleteInvoice.setEnabled( false );
            btnPrintInvoice.setEnabled( false );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getInvoice() Exception", JOptionPane.ERROR_MESSAGE );
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
        dtInvoice.setDateFormatString( "yyyy-MM-dd" );
        java.util.Date date = new java.util.Date();
        dtInvoice.setDate( date );
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
        txtDisc.setText( "0" );
        txtRate.setText( "0" );
        txtQTY.setText( "0" );
        txtAmount.setText( "0" );
    }

    //Function or method to clear Invoice Fields
    public void clearInvoiceTotals() {
        txtGSTPer.setText( "0" );
        txtBasicPrice.setText( "0" );
        txtDiscount.setText( "0" );
        txtGSTAmount.setText( "0" );
        txtGrandTotal.setText( "0" );
    }

    //Function or method to save client
    public void saveClient() {
        try {
//            String sql = "INSERT INTO clients (clientname,clientcompanyname,address,email,mobile,landline,gstin) VALUES (?,?,?,?,?,?,?)";
            String sql = "INSERT INTO clients (clientcompanyname,address) VALUES (?,?)";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, cmbCompany.getEditor().getItem().toString() );
            pst.setString( 1, cmbAddress.getEditor().getItem().toString() );

//            pst.setString( 1, cmbClientName.getEditor().getItem().toString() );
//            pst.setString( 2, cmbCompany.getEditor().getItem().toString() );
//            pst.setString( 3, cmbAddress.getEditor().getItem().toString() );
//            pst.setString( 4, cmbEmail.getEditor().getItem().toString() );
//            pst.setString( 5, cmbMobile.getEditor().getItem().toString() );
//            pst.setString( 6, cmbLandline.getEditor().getItem().toString() );
//            pst.setString( 7, cmbGSTIN.getEditor().getItem().toString() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Client Company added to database", "CLIENT ADDED", JOptionPane.PLAIN_MESSAGE );
            btnSaveClient.setEnabled( false );
//            cmbClientName.setEnabled( false );
            cmbCompany.setEnabled( false );
            cmbAddress.setEnabled( false );
//            cmbEmail.setEnabled( false );
//            cmbMobile.setEnabled( false );
//            cmbLandline.setEnabled( false );
//            cmbGSTIN.setEnabled( false );
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
            String sql = "SELECT * FROM jobordermaster WHERE joborderno='" + txtJobOrderNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblInvoice.setModel( DbUtils.resultSetToTableModel( rs ) );
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
            String sql = "SELECT * FROM invoice2master WHERE invoiceno='" + txtInvoiceNo.getText() + "'";
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
            txtBasicPrice.setText( String.valueOf( Math.round( basicprice ) ) );
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
            cmbCompany.addItem( "" );
            cmbAddress.addItem( "" );
            String sql = "SELECT * FROM clients";
            pst = conn.prepareStatement( sql );
            rs2 = pst.executeQuery();
            while (rs2.next()) {
                cmbCompany.addItem( rs2.getString( "clientcompanyname" ) );
                cmbAddress.addItem( rs2.getString( "address" ) );
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

//Function or method to get saved invoice data calculation value from database
    public void getInvoiceDataCALC() {
        try {
            String sql = "SELECT * FROM invoice2 WHERE invoiceno='" + txtInvoiceNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs2 = pst.executeQuery();
            if (rs2.next()) {
                cmbCompany.setSelectedItem( rs2.getString( "company" ) );
                cmbAddress.setSelectedItem( rs2.getString( "address" ) );
                txtChallanNo.setText( rs2.getString( "challanno" ) );
                txtJobOrderNo.setText( rs2.getString( "joborderno" ) );
                txtGSTPer.setText( rs2.getString( "gstper" ) );
                txtBasicPrice.setText( rs2.getString( "basicprice" ) );
                txtDiscount.setText( rs2.getString( "discount" ) );
                txtGSTAmount.setText( rs2.getString( "gstamt" ) );
                txtGrandTotal.setText( rs2.getString( "totalamt" ) );
                txtNote.setText( rs2.getString( "note" ) );
                btnSaveInvoice.setEnabled( false );
                btnUpdateInvoice.setEnabled( true );
                btnDeleteInvoice.setEnabled( true );
                btnPrintInvoice.setEnabled( true );
            } else {
                cmbCompany.setSelectedItem( "" );
                cmbAddress.setSelectedItem( "" );
                txtGSTPer.setText( "0" );
                txtBasicPrice.setText( "0" );
                txtDiscount.setText( "0" );
                txtGSTAmount.setText( "0" );
                txtGrandTotal.setText( "0" );
                txtNote.setText( "" );
                btnSaveInvoice.setEnabled( true );
                btnUpdateInvoice.setEnabled( false );
                btnDeleteInvoice.setEnabled( false );
                btnPrintInvoice.setEnabled( false );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getInvoiceDataCALC() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs2.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e, "getInvoiceDataCALC() Exception", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    //Funtion or method to calculate current list
    public void calcLatest() {
        try {
            disc = 0;
            basicprice = 0;
            dbamt = 0;
            dbdisc = 0;
            dbrate = 0;
            dbqty = 0;
            String sql = "SELECT * FROM invoice2master WHERE invoiceno='" + txtInvoiceNo.getText() + "'";
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
            txtBasicPrice.setText( String.valueOf( Math.round( basicprice ) ) );
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

    //Function or method to get saved quotation data calculation
    public void getQuotationDataCALC() {
        try {
            String sql = "SELECT * FROM invoice2 WHERE challanno='" + txtChallanNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs2 = pst.executeQuery();
            if (rs2.next()) {
                cmbCompany.setSelectedItem( rs2.getString( "clientcompanyname" ) );
                cmbAddress.setSelectedItem( rs2.getString( "address" ) );
                txtChallanNo.setText( rs2.getString( "challanno" ) );
                txtJobOrderNo.setText( rs2.getString( "joborderno" ) );
                txtGSTPer.setText( rs2.getString( "gstper" ) );
                txtBasicPrice.setText( rs2.getString( "basicprice" ) );
                txtDiscount.setText( rs2.getString( "discount" ) );
                txtGSTAmount.setText( rs2.getString( "gstamt" ) );
                txtGrandTotal.setText( rs2.getString( "totalamt" ) );
            } else {
                cmbCompany.setSelectedItem( "" );
                txtChallanNo.setText("");
                cmbAddress.setSelectedItem( "" );
                txtGSTPer.setText( "0" );
                txtBasicPrice.setText( "0" );
                txtDiscount.setText( "0" );
                txtGSTAmount.setText( "0" );
                txtGrandTotal.setText( "0" );
                btnSaveInvoice.setEnabled( true );
                btnUpdateInvoice.setEnabled( false );
                btnDeleteInvoice.setEnabled( false );
                btnPrintInvoice.setEnabled( false );
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
        cmbCompany.setSelectedItem( "" );
        cmbAddress.setSelectedItem( "" );
    }

    //Function to populate combobox on client name input
    public void getClientDataFromJobOrder() {
        try {
            cmbCompany.setSelectedItem( "" );
            cmbAddress.setSelectedItem( "" );
            String sql = "SELECT * FROM jobordermaster WHERE joborderno='" + txtJobOrderNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs2 = pst.executeQuery();
            if (rs2.next()) {
                cmbCompany.setSelectedItem( rs2.getString( "company" ) );
                cmbAddress.setSelectedItem( rs2.getString( "clientaddress" ) );
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
    String clientname, company;

    public void setDataToFieldsOnNameInput() {
//        clientname = cmbClientName.getSelectedItem().toString();
        company = cmbCompany.getSelectedItem().toString();
        try {
            String sql = "SELECT * FROM clients WHERE company LIKE '" + company + "'";
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
    String count, tblClick, tblInvClick;

    public void getTableDataToField() {
        try {
            row = tblInvoice.getSelectedRow();
            tblClick = tblInvoice.getModel().getValueAt( row, 0 ).toString();
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
        if ("".equals( txtDisc.getText() )) {
            disc = 0;
        } else {
            disc = Double.parseDouble( txtDisc.getText() );
        }
        amount = rate * quantity;
        disc = (amount * disc) / 100;
        amount = amount - disc;
        txtAmount.setText( String.valueOf( amount ) );
    }

    //Funtion or method to calculate GRAND TOTAL
    double gstper = 0, discount = 0, subtotal = 0, gstamt = 0, grandTotal = 0;

    public void calcGT() {
        gstamt = 0;
        grandTotal = 0;
        if (!"".equals( txtGSTPer.getText() )) {
            gstper = Double.parseDouble( txtGSTPer.getText() );
        } else {
            gstper = 0;
        }

        gstamt = (Double.parseDouble( txtBasicPrice.getText() ) * gstper) / 100;
        txtGSTAmount.setText( String.valueOf( gstamt ) );
        grandTotal = Double.parseDouble( txtBasicPrice.getText() ) + gstamt;
        txtGrandTotal.setText( String.valueOf( Math.round( grandTotal ) ) );
    }

    //Function to save invoice
    public void saveInvoice() {
        try {
            String sql = "INSERT INTO invoice2 (challanno,date,company,address,basicprice,"
                    + "discount,gstper,gstamt,totalamt,joborderno,invoiceno,note) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, txtChallanNo.getText() );
            pst.setString( 2, ((JTextField) dtInvoice.getDateEditor().getUiComponent()).getText() );
            pst.setString( 3, cmbCompany.getSelectedItem().toString() );
            pst.setString( 4, cmbAddress.getSelectedItem().toString() );
            pst.setString( 5, txtBasicPrice.getText() );
            pst.setString( 6, txtDiscount.getText() );
            pst.setString( 7, txtGSTPer.getText() );
            pst.setString( 8, txtGSTAmount.getText() );
            pst.setString( 9, txtGrandTotal.getText() );
            pst.setString( 10, txtJobOrderNo.getText() );
            pst.setString( 11, txtInvoiceNo.getText() );
            pst.setString( 12, txtNote.getText() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Invoice saved to database", "Saved", JOptionPane.PLAIN_MESSAGE );
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "saveInvoice() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to update existing quotation
    public void updateInvoice() {
        try {
            String sql = "UPDATE invoice2 SET date=?,company=?,address=?,basicprice=?,discount=?,gstper=?,gstamt=?,totalamt=?,"
                    + "challanno=?,note=? WHERE invoiceno='" + txtInvoiceNo.getText() + "' ";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, ((JTextField) dtInvoice.getDateEditor().getUiComponent()).getText() );
            pst.setString( 2, cmbCompany.getSelectedItem().toString() );
            pst.setString( 3, cmbAddress.getSelectedItem().toString() );
            pst.setString( 4, txtBasicPrice.getText() );
            pst.setString( 5, txtDiscount.getText() );
            pst.setString( 6, txtGSTPer.getText() );
            pst.setString( 7, txtGSTAmount.getText() );
            pst.setString( 8, txtGrandTotal.getText() );
            pst.setString( 9, txtChallanNo.getText() );
            pst.setString( 10, txtNote.getText() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Invoice modified on database", "Updated", JOptionPane.PLAIN_MESSAGE );
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "updateInvoice() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to delete saved Quotation
    public void deleteInvoice() {
        try {
            String sql = "DELETE FROM invoice2 WHERE invoiceno='" + txtInvoiceNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            pst.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, "deleteInvoice() Exception", "Invoice Deleted", JOptionPane.ERROR_MESSAGE );
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
    public void printOptions() {
        try {
            String sql = "SELECT * FROM invoice2,invoice2master,companydetails WHERE invoice2.invoiceno='" + txtInvoiceNo.getText() + "' AND invoice2master.invoiceno='" + txtInvoiceNo.getText() + "' AND companydetails.c_uid=(SELECT MIN(companydetails.c_uid) FROM companydetails)";
            JasperDesign jd = JRXmlLoader.load( "src/reports/invoice2PT.jrxml" );
            JRDesignQuery qry = new JRDesignQuery();
            qry.setText( sql );
            jd.setQuery( qry );
            JasperReport jr = JasperCompileManager.compileReport( jd );
            JasperPrint jp = JasperFillManager.fillReport( jr, null, conn );
            JasperViewer.viewReport( jp, false );
        } catch (JRException e) {
            JOptionPane.showMessageDialog( null, e, "printOption() Exception", JOptionPane.ERROR_MESSAGE );
        }
    }

    //Function or method to import joborder to invoice list
    public void importJobOrder() {
        try {
            String sql = "INSERT INTO invoice2master (joborderno,date,productname,processname,materialname,tstrt,w,od,tl,mdp,discountper,qty,rate,"
                    + "amount,remark,company,clientaddress,discountamt,invoiceno) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement( sql );
            if (!"".equals( txtJobOrderNo.getText() )) {
                pst.setInt( 1, Integer.parseInt( txtJobOrderNo.getText() ) );
            } else {
                pst.setInt( 1, 0 );
            }
            pst.setString( 2, ((JTextField) dtInvoice.getDateEditor().getUiComponent()).getText() );
            pst.setString( 3, cmbProductName.getSelectedItem().toString() );
            pst.setString( 4, cmbProcess.getSelectedItem().toString() );
            pst.setString( 5, cmbMaterial.getSelectedItem().toString() );
            pst.setString( 6, txtTSTRT.getText() );
            pst.setString( 7, txtW.getText() );
            pst.setString( 8, txtOD.getText() );
            pst.setString( 9, txtTL.getText() );
            pst.setString( 10, txtMDP.getText() );
            pst.setString( 11, txtDisc.getText() );
            pst.setString( 12, txtQTY.getText() );
            pst.setString( 13, txtRate.getText() );
            pst.setString( 14, txtAmount.getText() );
            pst.setString( 15, txtRemarks.getText() );
            pst.setString( 16, cmbCompany.getSelectedItem().toString() );
            pst.setString( 17, cmbAddress.getSelectedItem().toString() );
            //Calculation of discountamt
            double q = 0, r = 0, d = 0, da = 0;
            q = Double.parseDouble( txtQTY.getText() );
            r = Double.parseDouble( txtRate.getText() );
            d = Double.parseDouble( txtDisc.getText() );
            da = (q * r * d) / 100;
            pst.setString( 18, String.valueOf( da ) );
            pst.setInt( 19, Integer.parseInt( txtInvoiceNo.getText() ) );
            pst.execute();
//            JOptionPane.showMessageDialog( null, "Product added to invoice database", "Saved", JOptionPane.PLAIN_MESSAGE );
            System.out.println( "Product added to invoice database." );
            clearProductField();
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "importJobOrder() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }
//Function or method to get Invoice Table Data To Field

    public void getInvoiceTableDataToField() {
        try {
            row = tblInvoice1.getSelectedRow();
            tblInvClick = tblInvoice1.getModel().getValueAt( row, 0 ).toString();
            String sql = "SELECT * FROM invoice2master WHERE inv_uid='" + tblInvClick + "'";
            pst2 = conn.prepareStatement( sql );
            rs2 = pst2.executeQuery();
            if (rs2.next()) {
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
            JOptionPane.showMessageDialog( null, e, "getInvoiceTableDataToField() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Remove imported Job Order selection from Invoice 
    public void removeImport() {
        try {
            String sql = "DELETE FROM invoice2master WHERE inv_uid='" + tblInvClick + "'";
            pst = conn.prepareStatement( sql );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Product deleted from Invoice database", "Import Deleted", JOptionPane.PLAIN_MESSAGE );
            System.out.println( "Product deleted from invoice database" );
            clearProductField();
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "removeImport() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function or method to get data to table
    public void getInvoiceDataToTable() {
        try {
            String sql = "SELECT inv_uid AS 'INV_UID',invoiceno AS 'INVOICE NO',joborderno AS 'JOB ORDER NO',productname AS 'PRODUCT NAME',processname AS 'PROCESS NAME',"
                    + "materialname AS 'MATERIAL NAME',tstrt AS 'T/START',w AS 'W',od AS 'OD',tl AS 'TL',mdp AS 'M/DP',rate AS 'RATE',qty AS 'QUANTITY',"
                    + "discountper AS 'DISC. %',discountamt AS 'DISC. AMT',amount AS 'AMOUNT' FROM invoice2master WHERE invoiceno='" + txtInvoiceNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblInvoice1.setModel( DbUtils.resultSetToTableModel( rs ) );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getInvoiceDataToTable() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    public void calcInvoiceTableData() {
        try {
            double d = 0, a = 0;
            String sql = "SELECT discountamt,amount  FROM invoice2master WHERE invoiceno='" + txtInvoiceNo.getText() + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            while (rs.next()) {
                d = d + rs.getDouble( "discountamt" );
                a = a + rs.getDouble( "amount" );
            }
            txtBasicPrice.setText( String.valueOf( Math.round( a ) ) );
            txtDiscount.setText( String.valueOf( Math.round( d ) ) );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "calcInvoiceTableData() Exception", JOptionPane.ERROR_MESSAGE );
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
        cmbCompany = new javax.swing.JComboBox<>();
        cmbAddress = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
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
        jLabel23 = new javax.swing.JLabel();
        txtInvoiceNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtChallanNo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        dtInvoice = new com.toedter.calendar.JDateChooser();
        jLabel30 = new javax.swing.JLabel();
        txtJobOrderNo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvoice = new javax.swing.JTable();
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
        btnSaveInvoice = new javax.swing.JButton();
        btnUpdateInvoice = new javax.swing.JButton();
        btnDeleteInvoice = new javax.swing.JButton();
        btnPrintInvoice = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblInvoice1 = new javax.swing.JTable();
        btnImport = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("INVOICE-2 BUILDER - JOB WORK MANAGEMENT SYSTEM");
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
        cmbCompany.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbCompanyKeyPressed(evt);
            }
        });

        cmbAddress.setEditable(true);
        cmbAddress.setRequestFocusEnabled(true);
        cmbAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbAddressKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Firm Name");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel24.setText("Address");

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
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCompany, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSaveClient)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel24)
                    .addComponent(cmbCompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSaveClient))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnSaveClient, cmbAddress, cmbCompany, jLabel2, jLabel24});

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cmbProductName.setEditable(true);
        cmbProductName.setRequestFocusEnabled(true);
        cmbProductName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbProductNameKeyPressed(evt);
            }
        });

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

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("M/DP");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setText("Process");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel28.setText("Material");

        txtRemarks.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRemarksFocusGained(evt);
            }
        });
        txtRemarks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRemarksKeyPressed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel29.setText("Remarks");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Disc.%");

        txtDisc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDisc.setText("0");
        txtDisc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiscFocusGained(evt);
            }
        });
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
                            .addComponent(cmbMaterial, 0, 384, Short.MAX_VALUE)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDisc)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAmount)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtMDP, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(jLabel11)
                                .addComponent(jLabel12)
                                .addComponent(jLabel9))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtOD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTSTRT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtQTY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDisc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtAmount))))
                .addGap(10, 10, 10))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbMaterial, cmbProcess, cmbProductName, jLabel27, jLabel28, jLabel29, jLabel5, txtRemarks});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtAmount, txtDisc, txtMDP, txtOD, txtQTY, txtRate, txtTL, txtTSTRT, txtW});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel10, jLabel11, jLabel12, jLabel13, jLabel14, jLabel22});

        jPanel4.setBackground(new java.awt.Color(255, 255, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel23.setText("INVOICE NO.");

        txtInvoiceNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInvoiceNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtInvoiceNoFocusGained(evt);
            }
        });
        txtInvoiceNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInvoiceNoActionPerformed(evt);
            }
        });
        txtInvoiceNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInvoiceNoKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("CHALLAN NO.");

        txtChallanNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtChallanNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChallanNoActionPerformed(evt);
            }
        });
        txtChallanNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtChallanNoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtChallanNoKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("DATE");

        dtInvoice.setDateFormatString("yyyy-MM-dd");
        dtInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dtInvoiceKeyPressed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel30.setText("JOB ORDER NO.");

        txtJobOrderNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtJobOrderNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJobOrderNoActionPerformed(evt);
            }
        });
        txtJobOrderNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJobOrderNoKeyPressed(evt);
            }
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
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtInvoiceNo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtChallanNo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtJobOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dtInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel23)
                .addComponent(txtInvoiceNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(txtChallanNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtJobOrderNo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(dtInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {dtInvoice, jLabel6, jLabel7, txtChallanNo});

        tblInvoice.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInvoiceMouseClicked(evt);
            }
        });
        tblInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblInvoiceKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblInvoice);

        jPanel5.setBackground(new java.awt.Color(204, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("GST %");

        txtGSTPer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGSTPer.setText("0");
        txtGSTPer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGSTPerKeyReleased(evt);
            }
        });

        txtBasicPrice.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBasicPrice.setText("0");
        txtBasicPrice.setFocusable(false);
        txtBasicPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBasicPriceActionPerformed(evt);
            }
        });

        txtDiscount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDiscount.setText("0");
        txtDiscount.setFocusable(false);
        txtDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscountKeyReleased(evt);
            }
        });

        txtGSTAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGSTAmount.setText("0");
        txtGSTAmount.setFocusable(false);

        txtGrandTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGrandTotal.setText("0");
        txtGrandTotal.setFocusable(false);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Basic Price");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Discount Amt.");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("GST Amount");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("GRAND TOTAL");

        btnSaveInvoice.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSaveInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/add_16px.png"))); // NOI18N
        btnSaveInvoice.setText("SAVE");
        btnSaveInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveInvoiceActionPerformed(evt);
            }
        });

        btnUpdateInvoice.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnUpdateInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/update_16px.png"))); // NOI18N
        btnUpdateInvoice.setText("UPDATE");
        btnUpdateInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateInvoiceActionPerformed(evt);
            }
        });

        btnDeleteInvoice.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnDeleteInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/Delete_16px.png"))); // NOI18N
        btnDeleteInvoice.setText("DELETE");
        btnDeleteInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteInvoiceActionPerformed(evt);
            }
        });

        btnPrintInvoice.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnPrintInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/print_16px.png"))); // NOI18N
        btnPrintInvoice.setText("PRINT");
        btnPrintInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintInvoiceActionPerformed(evt);
            }
        });

        txtNote.setColumns(20);
        txtNote.setRows(5);
        txtNote.setBorder(javax.swing.BorderFactory.createTitledBorder("Note"));
        jScrollPane3.setViewportView(txtNote);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
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
                            .addComponent(txtDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtGSTAmount)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtGrandTotal)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(btnSaveInvoice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdateInvoice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteInvoice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrintInvoice)))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDeleteInvoice, btnPrintInvoice, btnSaveInvoice, btnUpdateInvoice});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                            .addComponent(btnUpdateInvoice)
                            .addComponent(btnDeleteInvoice)
                            .addComponent(btnPrintInvoice)
                            .addComponent(btnSaveInvoice)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tblInvoice1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tblInvoice1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblInvoice1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInvoice1MouseClicked(evt);
            }
        });
        tblInvoice1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblInvoice1KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblInvoice1);

        btnImport.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/down_arrow_16px.png"))); // NOI18N
        btnImport.setText("IMPORT");
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        btnRemove.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/Remove_16px.png"))); // NOI18N
        btnRemove.setText("REMOVE");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jScrollPane2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnImport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemove)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnImport, btnRemove});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnImport)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

        setSize(new java.awt.Dimension(1176, 644));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        getInvoiceNo();
        getDate();
        getClientData();
        getProductData();
        getProcessData();
        getMaterialData();
        clearProductField();
        clearInvoiceTotals();
        //AFTER FORM IS LOADED
        setTotCalFieldsZero();
        getDataToTable();
        getData();
        getInvoiceDataToTable();
        calcAmt();
        calcGT();
    }//GEN-LAST:event_formWindowOpened

    private void btnSaveClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveClientActionPerformed
        // TODO add your handling code here:
        saveClient();
    }//GEN-LAST:event_btnSaveClientActionPerformed

    private void txtChallanNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChallanNoActionPerformed
        // TODO add your handling code here:
        getDataToTable();
    }//GEN-LAST:event_txtChallanNoActionPerformed

    private void cmbCompanyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCompanyKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbAddress.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            dtInvoice.requestFocus();
        }
    }//GEN-LAST:event_cmbCompanyKeyPressed

    private void cmbAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbAddressKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            cmbProductName.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            cmbCompany.requestFocus();
        }
    }//GEN-LAST:event_cmbAddressKeyPressed

    private void cmbProductNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbProductNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            cmbProcess.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            cmbAddress.requestFocus();
        }
    }//GEN-LAST:event_cmbProductNameKeyPressed

    private void cmbProcessKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbProcessKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            cmbMaterial.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            cmbProductName.requestFocus();
        }
    }//GEN-LAST:event_cmbProcessKeyPressed

    private void cmbMaterialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMaterialKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtRemarks.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            cmbProcess.requestFocus();
        }
    }//GEN-LAST:event_cmbMaterialKeyPressed

    private void txtQTYKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQTYKeyReleased
        // TODO add your handling code here:
        calcAmt();

    }//GEN-LAST:event_txtQTYKeyReleased

    private void txtQTYKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQTYKeyPressed
        // TODO add your handling code here:
        getData();
        getDataToTable();
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtDisc.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtRate.requestFocus();
        }
    }//GEN-LAST:event_txtQTYKeyPressed

    private void tblInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInvoiceMouseClicked
        // TODO add your handling code here:
        getTableDataToField();
    }//GEN-LAST:event_tblInvoiceMouseClicked

    private void tblInvoiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblInvoiceKeyPressed
        // TODO add your handling code here:
        getTableDataToField();
    }//GEN-LAST:event_tblInvoiceKeyPressed

    private void txtBasicPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBasicPriceActionPerformed
        // TODO add your handling code here:
        calcGT();
    }//GEN-LAST:event_txtBasicPriceActionPerformed

    private void txtGSTPerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTPerKeyReleased
        // TODO add your handling code here:
        if (!"".equals( txtGSTPer.getText() )) {
            calcGT();
        } else {
            txtGSTPer.setText( "0" );
            txtGSTPer.selectAll();
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

    private void btnSaveInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveInvoiceActionPerformed
        // TODO add your handling code here:
        saveInvoice();
        getInvoiceDataCALC();
    }//GEN-LAST:event_btnSaveInvoiceActionPerformed

    private void btnUpdateInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateInvoiceActionPerformed
        // TODO add your handling code here:
        updateInvoice();
    }//GEN-LAST:event_btnUpdateInvoiceActionPerformed

    private void btnDeleteInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteInvoiceActionPerformed
        // TODO add your handling code here:
        deleteInvoice();
    }//GEN-LAST:event_btnDeleteInvoiceActionPerformed

    private void txtChallanNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtChallanNoKeyReleased
        // TODO add your handling code here:
        if ("".equals( txtChallanNo.getText() )) {
            txtChallanNo.setText( "0" );
            txtChallanNo.selectAll();
        }
    }//GEN-LAST:event_txtChallanNoKeyReleased

    private void btnPrintInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintInvoiceActionPerformed
        // TODO add your handling code here:
        printOptions();
    }//GEN-LAST:event_btnPrintInvoiceActionPerformed

    private void txtDiscKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtAmount.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtQTY.requestFocus();
        }
    }//GEN-LAST:event_txtDiscKeyPressed

    private void txtDiscKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscKeyReleased
        // TODO add your handling code here:
        calcAmt();
    }//GEN-LAST:event_txtDiscKeyReleased

    private void txtJobOrderNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJobOrderNoKeyReleased
        // TODO add your handling code here:
        if ("".equals( txtJobOrderNo.getText() )) {
            txtJobOrderNo.setText( "0" );
            txtJobOrderNo.selectAll();
        }
        getData();
        getDataToTable();
        getClientDataFromJobOrder();
    }//GEN-LAST:event_txtJobOrderNoKeyReleased

    private void txtJobOrderNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJobOrderNoActionPerformed
        // TODO add your handling code here:
        getData();
        getDataToTable();
        getClientDataFromJobOrder();
    }//GEN-LAST:event_txtJobOrderNoActionPerformed

    private void txtInvoiceNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInvoiceNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInvoiceNoActionPerformed

    private void txtInvoiceNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceNoKeyReleased
        // TODO add your handling code here:
        getInvoiceDataToTable();
        getInvoiceDataCALC();
        calcLatest();
        calcGT();
    }//GEN-LAST:event_txtInvoiceNoKeyReleased

    private void tblInvoice1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInvoice1MouseClicked
        // TODO add your handling code here:
        getInvoiceTableDataToField();
    }//GEN-LAST:event_tblInvoice1MouseClicked

    private void tblInvoice1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblInvoice1KeyPressed
        // TODO add your handling code here:
        getInvoiceTableDataToField();
        if (evt.getKeyChar() == KeyEvent.VK_DELETE) {
            removeImport();
            getInvoiceDataToTable();
            calcInvoiceTableData();
            calcAmt();
            calcGT();
        }
    }//GEN-LAST:event_tblInvoice1KeyPressed

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        // TODO add your handling code here:
        importJobOrder();
        getInvoiceDataToTable();
        calcInvoiceTableData();
        calcAmt();
        calcGT();
    }//GEN-LAST:event_btnImportActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // TODO add your handling code here:
        removeImport();
        getInvoiceDataToTable();
        calcInvoiceTableData();
        calcAmt();
        calcGT();
    }//GEN-LAST:event_btnRemoveActionPerformed

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

    private void txtDiscFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscFocusGained
        // TODO add your handling code here:
        txtDisc.selectAll();
    }//GEN-LAST:event_txtDiscFocusGained

    private void txtAmountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusGained
        // TODO add your handling code here:
        txtAmount.selectAll();
    }//GEN-LAST:event_txtAmountFocusGained

    private void txtInvoiceNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            dtInvoice.requestFocus();
        }
    }//GEN-LAST:event_txtInvoiceNoKeyPressed

    private void txtChallanNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtChallanNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtJobOrderNo.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtInvoiceNo.requestFocus();
        }
    }//GEN-LAST:event_txtChallanNoKeyPressed

    private void txtJobOrderNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJobOrderNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            dtInvoice.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtChallanNo.requestFocus();
        }
    }//GEN-LAST:event_txtJobOrderNoKeyPressed

    private void dtInvoiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtInvoiceKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            cmbCompany.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtInvoiceNo.requestFocus();
        }
    }//GEN-LAST:event_dtInvoiceKeyPressed

    private void txtRemarksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRemarksKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtTSTRT.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            cmbMaterial.requestFocus();
        }
    }//GEN-LAST:event_txtRemarksKeyPressed

    private void txtTSTRTKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTSTRTKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtW.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtRemarks.requestFocus();
        }
    }//GEN-LAST:event_txtTSTRTKeyPressed

    private void txtWKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtOD.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtTSTRT.requestFocus();
        }
    }//GEN-LAST:event_txtWKeyPressed

    private void txtODKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtODKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtTL.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtW.requestFocus();
        }
    }//GEN-LAST:event_txtODKeyPressed

    private void txtTLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTLKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtMDP.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtOD.requestFocus();
        }
    }//GEN-LAST:event_txtTLKeyPressed

    private void txtMDPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMDPKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtRate.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtTL.requestFocus();
        }
    }//GEN-LAST:event_txtMDPKeyPressed

    private void txtRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRateKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtQTY.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtMDP.requestFocus();
        }
    }//GEN-LAST:event_txtRateKeyPressed

    private void txtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyPressed
        // TODO add your handling code here:
        importJobOrder();
        getInvoiceDataToTable();
        getInvoiceDataCALC();
        calcLatest();
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            cmbProductName.requestFocus();
        }
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            txtDisc.requestFocus();
        }
    }//GEN-LAST:event_txtAmountKeyPressed

    private void txtRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRemarksFocusGained
        // TODO add your handling code here:
        txtRemarks.selectAll();
    }//GEN-LAST:event_txtRemarksFocusGained

    private void txtInvoiceNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvoiceNoFocusGained
        // TODO add your handling code here:
        txtInvoiceNo.selectAll();
    }//GEN-LAST:event_txtInvoiceNoFocusGained

    private void txtRateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRateKeyReleased
        // TODO add your handling code here:
        calcAmt();
    }//GEN-LAST:event_txtRateKeyReleased

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
            java.util.logging.Logger.getLogger( CreateInvoice2.class
                    .getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger( CreateInvoice2.class
                    .getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger( CreateInvoice2.class
                    .getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger( CreateInvoice2.class
                    .getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable() {
            public void run() {
                new CreateInvoice2().setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeleteInvoice;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnPrintInvoice;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSaveClient;
    private javax.swing.JButton btnSaveInvoice;
    private javax.swing.JButton btnUpdateInvoice;
    private javax.swing.JComboBox<String> cmbAddress;
    private javax.swing.JComboBox<String> cmbCompany;
    private javax.swing.JComboBox<String> cmbMaterial;
    private javax.swing.JComboBox<String> cmbProcess;
    private javax.swing.JComboBox<String> cmbProductName;
    private com.toedter.calendar.JDateChooser dtInvoice;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblInvoice;
    private javax.swing.JTable tblInvoice1;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtBasicPrice;
    private javax.swing.JTextField txtChallanNo;
    private javax.swing.JTextField txtDisc;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtGSTAmount;
    private javax.swing.JTextField txtGSTPer;
    private javax.swing.JTextField txtGrandTotal;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtJobOrderNo;
    private javax.swing.JTextField txtMDP;
    private javax.swing.JTextArea txtNote;
    private javax.swing.JTextField txtOD;
    private javax.swing.JTextField txtQTY;
    private javax.swing.JTextField txtRate;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtTL;
    private javax.swing.JTextField txtTSTRT;
    private javax.swing.JTextField txtW;
    // End of variables declaration//GEN-END:variables
}
