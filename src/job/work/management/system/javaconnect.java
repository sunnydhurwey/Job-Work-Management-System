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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import javax.swing.JOptionPane;
public class javaconnect {
    Connection conn=null;
    public static Connection ConnectDB() throws UnknownHostException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
//InetAddress host = InetAddress.getByName("SATYAM2");
//InetAddress host = InetAddress.getByName("desktop-asus");
InetAddress host = InetAddress.getByName("DESKTOP-RAJSE97");
Connection conn=DriverManager.getConnection("jdbc:mysql://"+host.getHostAddress()+":3306/si_db?autoReconnect=true&useSSL=false","si_root","si_root");

//            Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/si_db?autoReconnect=true&useSSL=false","si_root","si_root");
            //Connection conn=DriverManager.getConnection("jdbc:mysql://192.168.1.11:3306/hmdb?autoReconnect=true&useSSL=false","root","root");
//            JOptionPane.showMessageDialog(null, "Connected to database server");
            return conn;
        }catch(ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, "Connection with database server failed.","Network Link Failure",JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
