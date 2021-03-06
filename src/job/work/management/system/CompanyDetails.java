/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package job.work.management.system;

import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;
import java.sql.*;
import java.sql.Connection;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author sunny
 */
public class CompanyDetails extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    /**
     * Creates new form CompanyDetails
     */
    public CompanyDetails() {
        initComponents();
        try {
            conn = javaconnect.ConnectDB();
        } catch (UnknownHostException e) {
            System.out.println( e );
        }

        this.setIconImage( new ImageIcon( getClass().getResource( "LOGO.png" ) ).getImage() );
    }
    //Program to set single instance of this form
    private static CompanyDetails obj = null;

    public static CompanyDetails getObj() {
        if (obj == null) {
            obj = new CompanyDetails();
        }
        return obj;
    }

    //Functions
    //Function to fetch data to table
    public void getCompanyData() {
        try {
            String sql = "SELECT * FROM companydetails";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            tblCompanyDetails.setModel( DbUtils.resultSetToTableModel( rs ) );
            txtCompanyName.requestFocus();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "getCompanyData() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }
    //Function to save comapny details
    String companyname, gstin, offaddress, email, bankname, accountno, ifsc, mobile, landline;

    public void saveData() {
        try {
            String sql = "INSERT INTO companydetails (c_name,c_gstin,c_officeaddress,c_email,c_mobile,c_landline,c_bankname,"
                    + "c_accountno,c_ifsc) VALUES (?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement( sql );
            pst.setString( 1, txtCompanyName.getText() );
            pst.setString( 2, txtGSTIN.getText() );
            pst.setString( 3, txtOfficeaddress.getText() );
            pst.setString( 4, txtEmail.getText() );
            pst.setString( 5, txtMobile.getText() );
            pst.setString( 6, txtLandline.getText() );
            pst.setString( 7, txtBankname.getText() );
            pst.setString( 8, txtAccount.getText() );
            pst.setString( 9, txtIFSC.getText() );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Company Details added to database", "Saved", JOptionPane.PLAIN_MESSAGE );
            getCompanyData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog( null, e, "saveData() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function to getTableDataToField
    int row;
    String count, tblClick;

    public void getTableDataToField() {
        try {
            row = tblCompanyDetails.getSelectedRow();
            tblClick = tblCompanyDetails.getModel().getValueAt( row, 0 ).toString();
            String sql = "Select * from companydetails where c_uid='" + tblClick + "'";
            pst = conn.prepareStatement( sql );
            rs = pst.executeQuery();
            if (rs.next()) {
                txtCompanyName.setText( rs.getString( "c_name" ) );
                txtGSTIN.setText( rs.getString( "c_gstin" ) );
                txtOfficeaddress.setText( rs.getString( "c_officeaddress" ) );
                txtEmail.setText( rs.getString( "c_email" ) );
                txtMobile.setText( rs.getString( "c_mobile" ) );
                txtLandline.setText( rs.getString( "c_landline" ) );
                txtBankname.setText( rs.getString( "c_bankname" ) );
                txtAccount.setText( rs.getString( "c_accountno" ) );
                txtIFSC.setText( rs.getString( "c_ifsc" ) );
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

    //Function to delete record
    public void deleteData() {
        try {
            String sql = "DELETE FROM companydetails WHERE c_uid='" + tblClick + "'";
            pst = conn.prepareStatement( sql );
            pst.execute();
            JOptionPane.showMessageDialog( null, "Data deleted from database", "Deleted", JOptionPane.PLAIN_MESSAGE );
            getCompanyData();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog( null, e, "deleteData() Exception", JOptionPane.ERROR_MESSAGE );
        } finally {
            try {
                pst.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog( null, e );
            }
        }
    }

    //Function to clear field
    public void clearField() {
        txtCompanyName.setText( "" );
        txtGSTIN.setText( "" );
        txtOfficeaddress.setText( "" );
        txtEmail.setText( "" );
        txtMobile.setText( "" );
        txtLandline.setText( "" );
        txtBankname.setText( "" );
        txtAccount.setText( "" );
        txtIFSC.setText( "" );
        txtCompanyName.requestFocus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        pnlCompanyDetails = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCompanyName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtGSTIN = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtOfficeaddress = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMobile = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtLandline = new javax.swing.JTextField();
        pnlBankDetails = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtBankname = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtAccount = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtIFSC = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblCompanyDetails = new javax.swing.JTable();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Company Details - Job Work Management System");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        pnlCompanyDetails.setBackground(new java.awt.Color(255, 255, 204));
        pnlCompanyDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Name");

        txtCompanyName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCompanyNameKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("GSTIN");

        txtGSTIN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGSTINKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Office Address");

        txtOfficeaddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOfficeaddressKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Email Address");

        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmailKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Mobile Number");

        txtMobile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMobileKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Landline");

        txtLandline.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLandlineKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlCompanyDetailsLayout = new javax.swing.GroupLayout(pnlCompanyDetails);
        pnlCompanyDetails.setLayout(pnlCompanyDetailsLayout);
        pnlCompanyDetailsLayout.setHorizontalGroup(
            pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCompanyDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCompanyDetailsLayout.createSequentialGroup()
                        .addGroup(pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlCompanyDetailsLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(164, 164, 164))
                            .addComponent(txtMobile))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txtLandline)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCompanyDetailsLayout.createSequentialGroup()
                        .addGroup(pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCompanyName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
                            .addComponent(txtGSTIN, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(pnlCompanyDetailsLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addComponent(txtOfficeaddress, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlCompanyDetailsLayout.setVerticalGroup(
            pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCompanyDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCompanyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtGSTIN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtOfficeaddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlCompanyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlCompanyDetailsLayout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCompanyDetailsLayout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtLandline, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlCompanyDetailsLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCompanyDetailsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, txtCompanyName, txtEmail, txtGSTIN, txtLandline, txtMobile, txtOfficeaddress});

        pnlBankDetails.setBackground(new java.awt.Color(255, 255, 204));
        pnlBankDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Bank Name");

        txtBankname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBanknameKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("A/C No.");

        txtAccount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAccountKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("IFSC");

        txtIFSC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIFSCKeyPressed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnClear.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnClear.setText("CLEAR");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBankDetailsLayout = new javax.swing.GroupLayout(pnlBankDetails);
        pnlBankDetails.setLayout(pnlBankDetailsLayout);
        pnlBankDetailsLayout.setHorizontalGroup(
            pnlBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBankDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBankDetailsLayout.createSequentialGroup()
                        .addGroup(pnlBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBankname)
                            .addGroup(pnlBankDetailsLayout.createSequentialGroup()
                                .addComponent(txtAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIFSC, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBankDetailsLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)))
                .addContainerGap())
        );

        pnlBankDetailsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnClear, btnDelete, btnSave});

        pnlBankDetailsLayout.setVerticalGroup(
            pnlBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBankDetailsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtBankname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(txtIFSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnClear)
                    .addComponent(btnDelete))
                .addGap(158, 158, 158))
        );

        pnlBankDetailsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtAccount, txtBankname, txtIFSC});

        pnlBankDetailsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnClear, btnDelete, btnSave});

        tblCompanyDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblCompanyDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCompanyDetailsMouseClicked(evt);
            }
        });
        tblCompanyDetails.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblCompanyDetailsKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblCompanyDetails);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCompanyDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlBankDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pnlCompanyDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBankDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearField();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        saveData();
        clearField();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        getCompanyData();
        clearField();
    }//GEN-LAST:event_formWindowOpened

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        deleteData();
        clearField();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblCompanyDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCompanyDetailsMouseClicked
        // TODO add your handling code here:
        getTableDataToField();
    }//GEN-LAST:event_tblCompanyDetailsMouseClicked

    private void tblCompanyDetailsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCompanyDetailsKeyPressed
        // TODO add your handling code here:
        getTableDataToField();
    }//GEN-LAST:event_tblCompanyDetailsKeyPressed

    private void txtCompanyNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCompanyNameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtGSTIN.requestFocus();
        }
    }//GEN-LAST:event_txtCompanyNameKeyPressed

    private void txtGSTINKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGSTINKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtOfficeaddress.requestFocus();
        }
    }//GEN-LAST:event_txtGSTINKeyPressed

    private void txtOfficeaddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOfficeaddressKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtEmail.requestFocus();
        }
    }//GEN-LAST:event_txtOfficeaddressKeyPressed

    private void txtEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtMobile.requestFocus();
        }
    }//GEN-LAST:event_txtEmailKeyPressed

    private void txtMobileKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMobileKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtLandline.requestFocus();
        }
    }//GEN-LAST:event_txtMobileKeyPressed

    private void txtLandlineKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLandlineKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtBankname.requestFocus();
        }
    }//GEN-LAST:event_txtLandlineKeyPressed

    private void txtBanknameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBanknameKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtAccount.requestFocus();
        }
    }//GEN-LAST:event_txtBanknameKeyPressed

    private void txtAccountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAccountKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            txtIFSC.requestFocus();
        }
    }//GEN-LAST:event_txtAccountKeyPressed

    private void txtIFSCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIFSCKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            saveData();
        }
    }//GEN-LAST:event_txtIFSCKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
obj=null;
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
            java.util.logging.Logger.getLogger( CompanyDetails.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger( CompanyDetails.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger( CompanyDetails.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger( CompanyDetails.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater( new Runnable() {
            public void run() {
                new CompanyDetails().setVisible( true );
            }
        } );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSave;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    private javax.swing.JPanel pnlBankDetails;
    private javax.swing.JPanel pnlCompanyDetails;
    private javax.swing.JTable tblCompanyDetails;
    private javax.swing.JTextField txtAccount;
    private javax.swing.JTextField txtBankname;
    private javax.swing.JTextField txtCompanyName;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtGSTIN;
    private javax.swing.JTextField txtIFSC;
    private javax.swing.JTextField txtLandline;
    private javax.swing.JTextField txtMobile;
    private javax.swing.JTextField txtOfficeaddress;
    // End of variables declaration//GEN-END:variables
}
