/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package job.work.management.system;

/**
 *
 * @author sunny
 */

import java.sql.*;
import javax.swing.JOptionPane;
public class javaconnect {
    Connection conn=null;
    public static Connection ConnectDB(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/si_db?autoReconnect=true&useSSL=false","si_root","si_root");
            //Connection conn=DriverManager.getConnection("jdbc:mysql://192.168.1.11:3306/hmdb?autoReconnect=true&useSSL=false","root","root");
            JOptionPane.showMessageDialog(null, "Connected to database server");
            return conn;
        }catch(ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, "Connection with database server failed.","Network Link Failure",JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
