package classes;

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
    private String Host = "localhost";
    private String Port = "3306";
    private String Database = "schema_extrace";
    private String ServerConfig = "serverTimezone=UTC";
    private String User = "root";
    private String Password = "Slasher15";

    public DatabaseConnection() {
        try {
            Connection connect;
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://" + Host + ":" + Port + "/" + Database + "?" + ServerConfig + "", "" + User + "", "" + Password + "");
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
