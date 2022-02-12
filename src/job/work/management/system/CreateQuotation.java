/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package job.work.management.system;

import com.sun.glass.events.KeyEvent;
import java.awt.HeadlessException;
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
        try{
            conn=javaconnect.ConnectDB();
        }catch(UnknownHostException e){
            System.out.println(e);
        }
        this.setIconImage(new ImageIcon(getClass().getResource("LOGO.png")).getImage());
        AutoCompleteDecorator.decorate(cmbClientName);
        AutoCompleteDecorator.decorate(cmbCompany);
        AutoCompleteDecorator.decorate(cmbAddress);
        AutoCompleteDecorator.decorate(cmbEmail);
        AutoCompleteDecorator.decorate(cmbMobile);
        AutoCompleteDecorator.decorate(cmbLandline);
        AutoCompleteDecorator.decorate(cmbGSTIN);

        AutoCompleteDecorator.decorate(cmbProductName);
        AutoCompleteDecorator.decorate(cmbProcess);
        AutoCompleteDecorator.decorate(cmbMaterial);
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
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                qno = rs.getInt("quotationno");
            }
            qno++;
            txtQuotationNo.setText(String.valueOf(qno));
            btnSaveQuotation.setEnabled(true);
            btnUpdateQuotation.setEnabled(false);
            btnDeleteQuotation.setEnabled(false);
            btnPrintQuotation.setEnabled(false);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "getData() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Function or method to set current date to jdatechooser
    public void getDate() {
        jDateChooser1.setDateFormatString("YYYY-MM-DD");
        java.util.Date date = new java.util.Date();
        jDateChooser1.setDate(date);
    }

    //Function or method to clear product field
    public void clearProductField() {
        cmbProductName.setSelectedIndex(0);
        cmbProcess.setSelectedIndex(0);
        cmbMaterial.setSelectedIndex(0);
        txtRemarks.setText("");
        txtT.setText("");
        txtW.setText("");
        txtOD.setText("");
        txtTL.setText("");
        txtM.setText("");
        txtDP.setText("");
        txtSTD.setText("");
        txtRate.setText("");
        txtQTY.setText("");
        txtAmount.setText("");
    }

    //Function or method to clear Quotation Fields
    public void clearQuotationTotals() {
        txtGSTPer.setText("");
        txtBasicPrice.setText("");
        txtDiscount.setText("");
        txtSubTotal.setText("");
        txtGSTAmount.setText("");
        txtGrandTotal.setText("");
    }

    //Function or method to save client
    public void saveClient() {
        try {
            String sql = "INSERT INTO clients (clientname,clientcompanyname,address,email,mobile,landline,gstin) VALUES (?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, cmbClientName.getEditor().getItem().toString());
            pst.setString(2, cmbCompany.getEditor().getItem().toString());
            pst.setString(3, cmbAddress.getEditor().getItem().toString());
            pst.setString(4, cmbEmail.getEditor().getItem().toString());
            pst.setString(5, cmbMobile.getEditor().getItem().toString());
            pst.setString(6, cmbLandline.getEditor().getItem().toString());
            pst.setString(7, cmbGSTIN.getEditor().getItem().toString());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Client added to database", "CLIENT ADDED", JOptionPane.PLAIN_MESSAGE);
            btnSaveClient.setEnabled(false);
            cmbClientName.setEnabled(false);
            cmbCompany.setEnabled(false);
            cmbAddress.setEnabled(false);
            cmbEmail.setEnabled(false);
            cmbMobile.setEnabled(false);
            cmbLandline.setEnabled(false);
            cmbGSTIN.setEnabled(false);
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "saveClient() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Function or method to get data to table
    public void getDataToTable() {
        try {
            String sql = "SELECT * FROM joborder WHERE quotationno='" + txtQuotationNo.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            tblQuotation.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "getDataToTable() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    //Function or method to set totcalcfields zero
    public void setTotCalFieldsZero(){
        txtGSTPer.setText("0");
        txtBasicPrice.setText("0");
        txtDiscount.setText("0");
        txtSubTotal.setText("0");
        txtGSTAmount.setText("0");
        txtGrandTotal.setText("0");
    }
    //Function to getData
    double basicprice = 0, dbamt = 0;
    String cn;
    public void getData() {
        try {
            String sql = "SELECT * FROM joborder WHERE quotationno='" + txtQuotationNo.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                cn = rs.getString("clientname");
                dbamt = rs.getDouble("amount");
                basicprice = basicprice + dbamt;
            }
//            cmbClientName.setSelectedItem(String.valueOf(cn));
            txtBasicPrice.setText(String.valueOf(basicprice));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "getData() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Function or method to get saved quotation data calculation
    public void getQuotationDataCALC() {
        try {
            String sql = "SELECT * FROM quotation WHERE quotationno='" + txtQuotationNo.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs2 = pst.executeQuery();
            if (rs2.next()) {
                cmbClientName.setSelectedItem(rs2.getString("clientname"));
                cmbCompany.setSelectedItem(rs2.getString("company"));
                cmbAddress.setSelectedItem(rs2.getString("address"));
                cmbEmail.setSelectedItem(rs2.getString("email"));
                cmbMobile.setSelectedItem(rs2.getString("mobile"));
                cmbLandline.setSelectedItem(rs2.getString("landline"));
                cmbGSTIN.setSelectedItem(rs2.getString("gstin"));
                txtGSTPer.setText(rs2.getString("gstper"));
                txtBasicPrice.setText(rs2.getString("basicprice"));
                txtDiscount.setText(rs2.getString("discount"));
                txtSubTotal.setText(rs2.getString("subtotal"));
                txtGSTAmount.setText(rs2.getString("gstamt"));
                txtGrandTotal.setText(rs2.getString("totalamt"));
                btnSaveQuotation.setEnabled(false);
                btnUpdateQuotation.setEnabled(true);
                btnDeleteQuotation.setEnabled(true);
                btnPrintQuotation.setEnabled(true);
            } else {
                cmbClientName.setSelectedItem("");
                cmbCompany.setSelectedItem("");
                cmbAddress.setSelectedItem("");
                cmbEmail.setSelectedItem("");
                cmbMobile.setSelectedItem("");
                cmbLandline.setSelectedItem("");
                cmbGSTIN.setSelectedItem("");
                txtGSTPer.setText("0");
                txtBasicPrice.setText("0");
                txtDiscount.setText("0");
                txtSubTotal.setText("0");
                txtGSTAmount.setText("0");
                txtGrandTotal.setText("0");
                btnSaveQuotation.setEnabled(true);
                btnUpdateQuotation.setEnabled(false);
                btnDeleteQuotation.setEnabled(false);
                btnPrintQuotation.setEnabled(false);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "getQuotationDataCALC() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                rs2.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e, "getQuotationDataCALC() Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Function or method to clear client data field
    public void clearClientData() {
        cmbClientName.setSelectedItem("");
        cmbCompany.setSelectedItem("");
        cmbAddress.setSelectedItem("");
        cmbEmail.setSelectedItem("");
        cmbMobile.setSelectedItem("");
        cmbLandline.setSelectedItem("");
        cmbGSTIN.setSelectedItem("");
    }

    //Function to populate combobox on client name input
    public void getClientData() {
        try {
            cmbClientName.setSelectedItem("");
            cmbCompany.setSelectedItem("");
            cmbAddress.setSelectedItem("");
            cmbEmail.setSelectedItem("");
            cmbMobile.setSelectedItem("");
            cmbLandline.setSelectedItem("");
            cmbGSTIN.setSelectedItem("");
            String sql = "SELECT * FROM clients";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == false) {
                System.out.println("Resultset is empty at getClientData()");
            } else {
                do {
                    cmbClientName.addItem(rs.getString("clientname"));
                    cmbCompany.addItem(rs.getString("clientcompanyname"));
                    cmbAddress.addItem(rs.getString("address"));
                    cmbEmail.addItem(rs.getString("email"));
                    cmbMobile.addItem(rs.getString("mobile"));
                    cmbLandline.addItem(rs.getString("landline"));
                    cmbGSTIN.addItem(rs.getString("gstin"));
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "getClientData() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    //Function to populate other client data comboboxes on Enter key press on client name
    boolean clientexist = false;
    int clientID = 0;
    String clientname;
    public void setDataToFieldsOnNameInput() {
        clientname=cmbClientName.getSelectedItem().toString();
        try {
            String sql = "SELECT * FROM clients WHERE clientname LIKE '" + clientname + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()==false) {
                System.out.println("Resultset is empty setDataToFieldOnNameInput()");
                btnSaveClient.setEnabled(true);
            } else {
                do {
                    clientID = rs.getInt("uid");
                    System.out.println(String.valueOf(clientID));
                    cmbCompany.setSelectedItem(rs.getString("clientcompanyname"));
                    cmbAddress.setSelectedItem(rs.getString("address"));
                    cmbEmail.setSelectedItem(rs.getString("email"));
                    cmbMobile.setSelectedItem(rs.getString("mobile"));
                    cmbLandline.setSelectedItem(rs.getString("landline"));
                    cmbGSTIN.setSelectedItem(rs.getString("gstin"));
                } while (rs.next());
                btnSaveClient.setEnabled(false);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "setDataToFieldsOnNameInput() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }

    //Function or method to get product data
    public void getProductData() {
        try {
            cmbProductName.removeAllItems();
            cmbProductName.addItem("");
            String sql = "SELECT * FROM products";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == false) {
                System.out.println("Resultset is empty at getProductData()");
            } else {
                do {
                    cmbProductName.addItem(rs.getString("productname"));
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "getProductData() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Function or method to get process data
    public void getProcessData() {
        try {
            cmbProcess.removeAllItems();
            cmbProcess.addItem("");
            String sql = "SELECT * FROM process";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == false) {
                System.out.println("Resultset is empty");
            } else {
                do {
                    cmbProcess.addItem(rs.getString("processname"));
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "getProcessData() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Function or method to get material data
    public void getMaterialData() {
        try {
            cmbMaterial.removeAllItems();
            cmbMaterial.addItem("");
            String sql = "SELECT * FROM material";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == false) {
                System.out.println("Resultset is empty");
            } else {
                do {
                    cmbMaterial.addItem(rs.getString("materialname"));
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "getMaterialData() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Function or method to add product to job list
    public void addProduct() {
        try {
            String sql = "INSERT INTO joborder (quotationno,date,clientname,productname,processname,materialname,t,w,od,tl,m,dp,std,qty,rate,"
                    + "amount,remark,clientid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtQuotationNo.getText());
            pst.setString(2, ((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText());
            pst.setString(3, cmbClientName.getEditor().getItem().toString());
            pst.setString(4, cmbProductName.getSelectedItem().toString());
            pst.setString(5, cmbProcess.getSelectedItem().toString());
            pst.setString(6, cmbMaterial.getSelectedItem().toString());
            pst.setString(7, txtT.getText());
            pst.setString(8, txtW.getText());
            pst.setString(9, txtOD.getText());
            pst.setString(10, txtTL.getText());
            pst.setString(11, txtM.getText());
            pst.setString(12, txtDP.getText());
            pst.setString(13, txtSTD.getText());
            pst.setString(14, txtQTY.getText());
            pst.setString(15, txtRate.getText());
            pst.setString(16, txtAmount.getText());
            pst.setString(17, txtRemarks.getText());
            pst.setInt(18, clientID);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Poduct added to database", "Saved", JOptionPane.PLAIN_MESSAGE);
//            getData();
            clearProductField();
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "addProduct() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Function or method to get selected data from table to field
    int row;
    String count, tblClick;

    public void getTableDataToField() {
        try {
            row = tblQuotation.getSelectedRow();
            tblClick = tblQuotation.getModel().getValueAt(row, 0).toString();
            String sql = "SELECT * FROM joborder WHERE uid='" + tblClick + "'";
            pst2 = conn.prepareStatement(sql);
            rs2 = pst2.executeQuery();
            if (rs2.next()) {
                cmbClientName.setSelectedItem(rs2.getString("clientname"));
                cmbProductName.setSelectedItem(rs2.getString("productname"));
                cmbProcess.setSelectedItem(rs2.getString("processname"));
                cmbMaterial.setSelectedItem(rs2.getString("materialname"));
                txtRemarks.setText(rs2.getString("remark"));
                txtT.setText(rs2.getString("t"));
                txtW.setText(rs2.getString("w"));
                txtOD.setText(rs2.getString("od"));
                txtTL.setText(rs2.getString("tl"));
                txtM.setText(rs2.getString("m"));
                txtDP.setText(rs2.getString("dp"));
                txtSTD.setText(rs2.getString("std"));
                txtRate.setText(rs2.getString("rate"));
                txtQTY.setText(rs2.getString("qty"));
                txtAmount.setText(rs2.getString("amount"));
            } else {
                JOptionPane.showMessageDialog(null, "No data found. Please check your database connection.", "No record found", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "getTableDataToField() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Function or method to delete product from job order
    public void deleteProduct() {
        try {
            String sql = "DELETE FROM joborder WHERE uid='" + tblClick + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Poduct deleted to database", "Deleted", JOptionPane.PLAIN_MESSAGE);
//            getData();
            clearProductField();
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "deleteProduct() Exception", "Deleted", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    //Function or method to calculate Rate X Quantity = AMOUNT
    double rate = 0, quantity = 0, amount = 0;

    public void calcAmt() {
        rate = Double.parseDouble(txtRate.getText());
        quantity = Double.parseDouble(txtQTY.getText());
        if (rate < 0) {
            rate = 0;
        }
        if (quantity < 0) {
            quantity = 0;
        }
        amount = rate * quantity;
        txtAmount.setText(String.valueOf(amount));
    }

    //Funtion or method to calculate GRAND TOTAL
    double gstper = 0, discount = 0, subtotal = 0, gstamt = 0, grandTotal = 0;

    public void calcGT() {
        if (txtGSTPer.getText() != null || !"".equals(txtGSTPer.getText())) {
            gstper = Double.parseDouble(txtGSTPer.getText());
        }else{
            gstper=0;
        }
        if (txtDiscount.getText() != null || !"".equals(txtDiscount.getText())) {
            discount = Double.parseDouble(txtDiscount.getText());
        }else{
            discount=0;
        }
        subtotal = basicprice - discount;
        txtSubTotal.setText(String.valueOf(subtotal));
        gstamt = (subtotal * gstper) / 100;
        txtGSTAmount.setText(String.valueOf(gstamt));
        grandTotal = subtotal + gstamt;
        txtGrandTotal.setText(String.valueOf(grandTotal));
    }
    
    //Function to save quotation
    public void saveQuotation(){
        try {
            String sql = "INSERT INTO quotation (quotationno,date,clientname,company,address,email,mobile,landline,gstin,basicprice,"
                    + "discount,subtotal,gstper,gstamt,totalamt) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtQuotationNo.getText());
            pst.setString(2, ((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText());
            pst.setString(3, cmbClientName.getEditor().getItem().toString());
            pst.setString(4, cmbCompany.getSelectedItem().toString());
            pst.setString(5, cmbAddress.getSelectedItem().toString());
            pst.setString(6, cmbEmail.getSelectedItem().toString());
            pst.setString(7, cmbMobile.getSelectedItem().toString());
            pst.setString(8, cmbLandline.getSelectedItem().toString());
            pst.setString(9, cmbGSTIN.getSelectedItem().toString());
            pst.setString(10, txtBasicPrice.getText());
            pst.setString(11, txtDiscount.getText());
            pst.setString(12, txtSubTotal.getText());
            pst.setString(13, txtGSTPer.getText());
            pst.setString(14, txtGSTAmount.getText());
            pst.setString(15, txtGrandTotal.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Quotation saved to database", "Saved", JOptionPane.PLAIN_MESSAGE);
//            getData();
//            clearProductField();
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "saveQuotation() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    //Function or method to update existing quotation
    public void updateQuotation(){
        try {
            String sql = "UPDATE quotation SET date=?,clientname=?,company=?,address=?,email=?,mobile=?,"
                    + "landline=?,gstin=?,basicprice=?,discount=?,subtotal=?,gstper=?,gstamt=?,totalamt=?"
                    + "WHERE quotationno='"+txtQuotationNo.getText()+"'";
            pst = conn.prepareStatement(sql);            
            pst.setString(1, ((JTextField) jDateChooser1.getDateEditor().getUiComponent()).getText());
            pst.setString(2, cmbClientName.getEditor().getItem().toString());
            pst.setString(3, cmbCompany.getSelectedItem().toString());
            pst.setString(4, cmbAddress.getSelectedItem().toString());
            pst.setString(5, cmbEmail.getSelectedItem().toString());
            pst.setString(6, cmbMobile.getSelectedItem().toString());
            pst.setString(7, cmbLandline.getSelectedItem().toString());
            pst.setString(8, cmbGSTIN.getSelectedItem().toString());
            pst.setString(9, txtBasicPrice.getText());
            pst.setString(10, txtDiscount.getText());
            pst.setString(11, txtSubTotal.getText());
            pst.setString(12, txtGSTPer.getText());
            pst.setString(13, txtGSTAmount.getText());
            pst.setString(14, txtGrandTotal.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Quotation modified on database", "Updated", JOptionPane.PLAIN_MESSAGE);
            cmbProductName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "updateQuotation() Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //Function or method to delete saved Quotation
    public void deleteQuotation(){
        try{
            String sql="DELETE FROM quotation WHERE quotationno='"+txtQuotationNo.getText()+"'";
            pst=conn.prepareStatement(sql);
            pst.execute();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "deleteQuotation() Exception", "Quotation Deleted", JOptionPane.ERROR_MESSAGE);
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    //Function or method to add product name to database
    public void addProductName(){
        try{
            String sql="INSERT INTO products (productname) VALUES (?)";
            pst=conn.prepareStatement(sql);
            pst.setString(1, cmbProductName.getEditor().getItem().toString());
            pst.execute();
            JOptionPane.showMessageDialog(null, "New Product Name added to database","Saved",JOptionPane.PLAIN_MESSAGE);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e, "addProductName() Exception",JOptionPane.ERROR_MESSAGE);
        }finally{
            try{
                pst.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    //Function or method to add process name to database
    public void addProcessName(){
        try{
            String sql="INSERT INTO process (processname) VALUES (?)";
            pst=conn.prepareStatement(sql);
            pst.setString(1, cmbProductName.getEditor().getItem().toString());
            pst.execute();
            JOptionPane.showMessageDialog(null, "New Process Name added to database","Saved",JOptionPane.PLAIN_MESSAGE);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e, "addProcessName() Exception",JOptionPane.ERROR_MESSAGE);
        }finally{
            try{
                pst.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    //Function or method to add material name to database
    public void addMaterialName(){
        try{
            String sql="INSERT INTO material (materialname) VALUES (?)";
            pst=conn.prepareStatement(sql);
            pst.setString(1, cmbMaterial.getEditor().getItem().toString());
            pst.execute();
            JOptionPane.showMessageDialog(null, "New Material Name added to database","Saved",JOptionPane.PLAIN_MESSAGE);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e, "addMaterialName() Exception",JOptionPane.ERROR_MESSAGE);
        }finally{
            try{
                pst.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    //Program to give printout options
    //final JDialog dialog=new JDialog();
    public void printOptions(){
        try{
            String qno=txtQuotationNo.getText();
            //String[] choice={"CGST+SSGT Invoice","IGST Invoice"};
            String[] choice={"Landscape","Portrait"};
            //Integer[] options = {1, 3, 5, 7, 9, 11};
            //Double[] options = {3.141, 1.618};
            //Character[] options = {'a', 'b', 'c', 'd'};
            
            int x = JOptionPane.showOptionDialog(null, "Choose Print Method","Invoice Print",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choice, choice[0]);
            //System.out.println(x);
            //JOptionPane.showMessageDialog(null, "You Selected: "+x);            
            if(x == 0){
                try{
                    String sql="Select * from quotation,joborder,companydetails where quotation.quotationno='"+qno+"' "
                            + "AND joborder.quotationno='"+qno+"'";
                    JasperDesign jd= JRXmlLoader.load("src/reports/quotationLS.jrxml");
                    JRDesignQuery qry=new JRDesignQuery();
                    qry.setText(sql);
                    jd.setQuery(qry);
                    JasperReport jr= JasperCompileManager.compileReport(jd);
                    JasperPrint jp=JasperFillManager.fillReport(jr, null,conn);
                    JasperViewer.viewReport(jp,false);
                }catch(JRException e){
                    JOptionPane.showMessageDialog(null, e,"printOption() Exception",JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                try{
                    String sql="Select * from quotation,joborder,companydetails where quotation.quotationno='"+qno+"' "
                            + "AND joborder.quotationno='"+qno+"'";
                    JasperDesign jd= JRXmlLoader.load("src/reports/quotationPT.jrxml");
                    JRDesignQuery qry=new JRDesignQuery();
                    qry.setText(sql);
                    jd.setQuery(qry);
                    JasperReport jr= JasperCompileManager.compileReport(jd);
                    JasperPrint jp=JasperFillManager.fillReport(jr, null,conn);
                    JasperViewer.viewReport(jp,false);
                }catch(JRException e){
                    JOptionPane.showMessageDialog(null, e,"printOption() Exception",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        catch(HeadlessException e){
            JOptionPane.showMessageDialog(null, e,"Print Invoice Exception",JOptionPane.ERROR_MESSAGE);
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
        txtT = new javax.swing.JTextField();
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
        txtM = new javax.swing.JTextField();
        txtDP = new javax.swing.JTextField();
        txtSTD = new javax.swing.JTextField();
        txtRate = new javax.swing.JTextField();
        txtQTY = new javax.swing.JTextField();
        txtAmount = new javax.swing.JTextField();
        btnAddProduct = new javax.swing.JButton();
        btnDeleteProduct = new javax.swing.JButton();
        btnClearProduct = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        btnAddProductName = new javax.swing.JButton();
        btnAddProcessName = new javax.swing.JButton();
        btnAddMaterialName = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtQuotationNo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblQuotation = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txtGSTPer = new javax.swing.JTextField();
        txtBasicPrice = new javax.swing.JTextField();
        txtDiscount = new javax.swing.JTextField();
        txtSubTotal = new javax.swing.JTextField();
        txtGSTAmount = new javax.swing.JTextField();
        txtGrandTotal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        btnSaveQuotation = new javax.swing.JButton();
        btnUpdateQuotation = new javax.swing.JButton();
        btnDeleteQuotation = new javax.swing.JButton();
        btnPrintQuotation = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("QUOTATION BUILDER - JOB WORK MANAGEMENT SYSTEM");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 204, 102));

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Client Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Client Name");

        cmbClientName.setEditable(true);
        cmbClientName.setRequestFocusEnabled(true);
        cmbClientName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbClientNameActionPerformed(evt);
            }
        });
        cmbClientName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cmbClientNameKeyReleased(evt);
            }
        });

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

        cmbEmail.setEditable(true);
        cmbEmail.setRequestFocusEnabled(true);
        cmbEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbEmailKeyPressed(evt);
            }
        });

        cmbMobile.setEditable(true);
        cmbMobile.setRequestFocusEnabled(true);
        cmbMobile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMobileKeyPressed(evt);
            }
        });

        cmbLandline.setEditable(true);
        cmbLandline.setRequestFocusEnabled(true);
        cmbLandline.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbLandlineKeyPressed(evt);
            }
        });

        cmbGSTIN.setEditable(true);
        cmbGSTIN.setRequestFocusEnabled(true);
        cmbGSTIN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbGSTINKeyPressed(evt);
            }
        });

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
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

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Job Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        cmbProductName.setEditable(true);
        cmbProductName.setNextFocusableComponent(cmbProcess);
        cmbProductName.setRequestFocusEnabled(true);
        cmbProductName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbProductNameKeyPressed(evt);
            }
        });

        cmbProcess.setEditable(true);
        cmbProcess.setNextFocusableComponent(cmbMaterial);
        cmbProcess.setRequestFocusEnabled(true);
        cmbProcess.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbProcessKeyPressed(evt);
            }
        });

        cmbMaterial.setEditable(true);
        cmbMaterial.setNextFocusableComponent(txtT);
        cmbMaterial.setRequestFocusEnabled(true);
        cmbMaterial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMaterialKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Product Name");

        txtT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtT.setText("0");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("AMOUNT");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("T");

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

        txtM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtM.setText("0");

        txtDP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDP.setText("0");

        txtSTD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSTD.setText("0");

        txtRate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRate.setText("0");

        txtQTY.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtQTY.setText("0");
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

        btnAddProduct.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/add_16px.png"))); // NOI18N
        btnAddProduct.setText("ADD");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });

        btnDeleteProduct.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnDeleteProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/job/work/images/Delete_16px.png"))); // NOI18N
        btnDeleteProduct.setText("DELETE");
        btnDeleteProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProductActionPerformed(evt);
            }
        });

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
        jLabel21.setText("M");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("DP");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("STD");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setText("Process");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel28.setText("Material");

        txtRemarks.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel29.setText("Remarks");

        btnAddProductName.setText("+");
        btnAddProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductNameActionPerformed(evt);
            }
        });

        btnAddProcessName.setText("+");
        btnAddProcessName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProcessNameActionPerformed(evt);
            }
        });

        btnAddMaterialName.setText("+");
        btnAddMaterialName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMaterialNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbMaterial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbProductName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnAddProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnAddMaterialName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel29)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cmbProcess, 0, 497, Short.MAX_VALUE)
                                .addGap(3, 3, 3)
                                .addComponent(btnAddProcessName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtRemarks)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(btnAddProduct)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDeleteProduct)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnClearProduct))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtT, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                    .addComponent(txtM)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtDP)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSTD)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                    .addComponent(txtAmount)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddProduct, btnClearProduct, btnDeleteProduct});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddProcessName, btnAddProductName});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAddProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(cmbProductName)
                        .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbProcess)
                        .addComponent(btnAddProcessName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnAddMaterialName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel28))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
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
                                .addComponent(txtT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtQTY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSTD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAddProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(btnDeleteProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClearProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAddProcessName, btnAddProductName});

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
                .addComponent(jLabel7))
            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

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
        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

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
        txtBasicPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBasicPriceActionPerformed(evt);
            }
        });

        txtDiscount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDiscount.setText("0");
        txtDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscountKeyReleased(evt);
            }
        });

        txtSubTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSubTotal.setText("0");

        txtGSTAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGSTAmount.setText("0");

        txtGrandTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGrandTotal.setText("0");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Basic Price");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Discount");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Sub Total");

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(640, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtGSTPer)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBasicPrice)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDiscount)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSubTotal)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
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
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBasicPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGSTAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGSTPer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateQuotation)
                    .addComponent(btnDeleteQuotation)
                    .addComponent(btnPrintQuotation)
                    .addComponent(btnSaveQuotation))
                .addGap(0, 37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        setSize(new java.awt.Dimension(1295, 828));
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

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        // TODO add your handling code here:
        addProduct();
        getDataToTable();
    }//GEN-LAST:event_btnAddProductActionPerformed

    private void txtQuotationNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuotationNoActionPerformed
        // TODO add your handling code here:
        getDataToTable();
    }//GEN-LAST:event_txtQuotationNoActionPerformed

    private void cmbCompanyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCompanyKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbAddress.requestFocus();
        }
    }//GEN-LAST:event_cmbCompanyKeyPressed

    private void cmbAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbAddressKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbEmail.requestFocus();
        }
    }//GEN-LAST:event_cmbAddressKeyPressed

    private void cmbEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbEmailKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbMobile.requestFocus();
        }
    }//GEN-LAST:event_cmbEmailKeyPressed

    private void cmbMobileKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMobileKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbLandline.requestFocus();
        }
    }//GEN-LAST:event_cmbMobileKeyPressed

    private void cmbLandlineKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbLandlineKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbGSTIN.requestFocus();
        }
    }//GEN-LAST:event_cmbLandlineKeyPressed

    private void cmbGSTINKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbGSTINKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (clientexist == false) {
                saveClient();
            }
        }
    }//GEN-LAST:event_cmbGSTINKeyPressed

    private void cmbProductNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbProductNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbProcess.requestFocus();
        }
    }//GEN-LAST:event_cmbProductNameKeyPressed

    private void cmbProcessKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbProcessKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbMaterial.requestFocus();
        }
    }//GEN-LAST:event_cmbProcessKeyPressed

    private void cmbMaterialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMaterialKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtT.requestFocus();
        }
    }//GEN-LAST:event_cmbMaterialKeyPressed

    private void cmbClientNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbClientNameKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cmbCompany.requestFocus();
        }
    }//GEN-LAST:event_cmbClientNameKeyReleased

    private void txtQTYKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQTYKeyReleased
        // TODO add your handling code here:
        calcAmt();
    }//GEN-LAST:event_txtQTYKeyReleased

    private void btnDeleteProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProductActionPerformed
        // TODO add your handling code here:
        deleteProduct();
        getData();
        getDataToTable();
    }//GEN-LAST:event_btnDeleteProductActionPerformed

    private void txtQTYKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQTYKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addProduct();
            getData();
            getDataToTable();
        }
    }//GEN-LAST:event_txtQTYKeyPressed

    private void tblQuotationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQuotationMouseClicked
        // TODO add your handling code here:
        getTableDataToField();
    }//GEN-LAST:event_tblQuotationMouseClicked

    private void btnClearProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearProductActionPerformed
        // TODO add your handling code here:
        clearProductField();
    }//GEN-LAST:event_btnClearProductActionPerformed

    private void tblQuotationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblQuotationKeyPressed
        // TODO add your handling code here:
        getTableDataToField();
    }//GEN-LAST:event_tblQuotationKeyPressed

    private void txtBasicPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBasicPriceActionPerformed
        // TODO add your handling code here:
        calcGT();
    }//GEN-LAST:event_txtBasicPriceActionPerformed

    private void txtGSTPerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTPerKeyReleased
        // TODO add your handling code here:
        if(!"".equals(txtGSTPer.getText())){
            calcGT();
        }else{
            txtGSTPer.setText("0");
            calcGT();
        }
    }//GEN-LAST:event_txtGSTPerKeyReleased

    private void txtDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyReleased
        // TODO add your handling code here:
        if(!"".equals(txtDiscount.getText())){
            calcGT();
        }else{
            txtDiscount.setText("0");
            calcGT();
        }
    }//GEN-LAST:event_txtDiscountKeyReleased

    private void btnSaveQuotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveQuotationActionPerformed
        // TODO add your handling code here:
        saveQuotation();
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
        getDataToTable();
        getQuotationDataCALC();
    }//GEN-LAST:event_txtQuotationNoKeyReleased

    private void cmbClientNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClientNameActionPerformed
        // TODO add your handling code here:
        getData();
    }//GEN-LAST:event_cmbClientNameActionPerformed

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

    private void btnPrintQuotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintQuotationActionPerformed
        // TODO add your handling code here:
        printOptions();
    }//GEN-LAST:event_btnPrintQuotationActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CreateQuotation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateQuotation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateQuotation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateQuotation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreateQuotation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddMaterialName;
    private javax.swing.JButton btnAddProcessName;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnAddProductName;
    private javax.swing.JButton btnClearProduct;
    private javax.swing.JButton btnDeleteProduct;
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
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblQuotation;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtBasicPrice;
    private javax.swing.JTextField txtDP;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtGSTAmount;
    private javax.swing.JTextField txtGSTPer;
    private javax.swing.JTextField txtGrandTotal;
    private javax.swing.JTextField txtM;
    private javax.swing.JTextField txtOD;
    private javax.swing.JTextField txtQTY;
    private javax.swing.JTextField txtQuotationNo;
    private javax.swing.JTextField txtRate;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtSTD;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtT;
    private javax.swing.JTextField txtTL;
    private javax.swing.JTextField txtW;
    // End of variables declaration//GEN-END:variables
}
