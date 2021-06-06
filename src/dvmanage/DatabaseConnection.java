package dvmanage;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Johnny Roger
 */
public class DatabaseConnection {

    private Connection conn;

    public DatabaseConnection() {
        try {
            Connection connect;
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/dvmanage?serverTimezone=UTC", "root", "tsiafytwiafm7677");
            connect.setAutoCommit(true);
            setConnection(connect);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            try {
                dispose();
            } catch (Exception ex1) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void setConnection(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() {
        return conn;
    }
}
