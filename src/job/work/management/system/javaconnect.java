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
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class javaconnect {
//    Connection conn=null;
    public static Connection ConnectDB(){
        Connection conn=null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
                InetAddress host=InetAddress.getByName("DESKTOP-ASUS");
                conn=DriverManager.getConnection("jdbc:mysql://"+host.getHostAddress()+"/si_db","si_root","si_root");
                System.out.println("CONNECTION ESTABLISHED AT : "+host.getHostAddress());    
//            Connection conn=DriverManager.getConnection("jdbc:mysql://192.168.225.54/si_db","si_root","si_root");
//            Connection conn=DriverManager.getConnection("jdbc:mysql://localhost/si_db","si_root","si_root");
            return conn;
        }catch(ClassNotFoundException | UnknownHostException | SQLException e){
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
}
