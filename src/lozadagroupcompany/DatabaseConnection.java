/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lozadagroupcompany;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Johnny Roger
 */
public class DatabaseConnection {
    private Connection conn;
    
    public DatabaseConnection(){
        try {
            Connection connect;
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/schema_lgcmerch", "root", "Slasher15");
            connect.setAutoCommit(true);
            setConnection(connect);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setConnection(Connection conn){
        this.conn = conn;
    }
    
    public Connection getConnection(){
        return conn;
    }
}
