package lozadagroupcompany;

import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static lozadagroupcompany.NumberFunction.getFormattedNumber;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FrameReport extends javax.swing.JFrame {

    DatabaseConnection conn = new DatabaseConnection();
    DefaultTableModel tablemodel;
    Connection connection = conn.getConnection();
    DefaultTableModel dm;
    String importtype;
    String datefrom;
    String dateto;
    String overalltotal;
    String charge;

    public FrameReport() {
        initComponents();
        retrieveMR();
        retrieveMW();
        RetrieveCompanyMW();
        retrieveDV();
        RetrieveCompanyDV();
        setIconImage();
    }

    private void setIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("ico.png")));
    }

    public void SetImport(String type) {
        importtype = type;
    }

    //********************MR CODE*************************//
    private void retrieveMR() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemr.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisereceipt");
            while (rs.next()) {
                String code = rs.getString("MerchReceiptCode");
                String quantity = rs.getString("Quantity");
                String unitcost = rs.getString("UnitCost");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, quantity, unitcost, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveMRByDate() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemr.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisereceipt WHERE Status = 'APPROVED' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("MerchReceiptCode");
                String quantity = rs.getString("Quantity");
                String unitcost = rs.getString("UnitCost");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, quantity, unitcost, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void CountCalculateMR() {
        double totalamount = 0;
        for (int i = 0; i < tablemr.getRowCount(); i++) {
            double total = Double.parseDouble(tablemr.getValueAt(i, 3).toString());
            totalamount += total;

        }
        overalltotal = Double.toString(totalamount);
    }

    private void PrintMR() {
        int row = tablemr.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overalltotal", "Php " + getFormattedNumber(overalltotal));
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportMR.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    //********************MW CODE*************************//
    private void retrieveMW() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemw.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisewithdrawal");
            while (rs.next()) {
                String code = rs.getString("MerchWithdrawalCode");
                String quantity = rs.getString("Quantity");
                String unitcost = rs.getString("UnitCost");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, quantity, unitcost, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveMWByDate() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemw.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisewithdrawal WHERE Status = 'APPROVED' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("MerchWithdrawalCode");
                String quantity = rs.getString("Quantity");
                String unitcost = rs.getString("UnitCost");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, quantity, unitcost, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveMWByDateCharge() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisewithdrawal WHERE Status = 'APPROVED' AND ChargeTo = '" + cbchargemw.getSelectedItem() + "' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("MerchWithdrawalCode");
                String icharge = rs.getString("ChargeTo");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void CountCalculateMW() {
        double totalamount = 0;
        for (int i = 0; i < tablemw.getRowCount(); i++) {
            double total = Double.parseDouble(tablemw.getValueAt(i, 3).toString());
            totalamount += total;

        }
        overalltotal = Double.toString(totalamount);
    }

    private void PrintMW() {
        int row = tablemw.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportMW.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintMWByCompany() {
        int row = tablemw.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            parameters.put("chargeto", charge);
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportMWChargeTo.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void RetrieveCompanyMW() {
        cbchargemw.addItem("All");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblcompany");
            while (rs.next()) {
                String chart = rs.getString("CompanyName");
                cbchargemw.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    //********************DV CODE*************************//
    private void retrieveDV() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String icharge = rs.getString("ChargeTo");
                String totalamount = rs.getString("GrossAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveDVByDateCharge() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher WHERE Status = 'APPROVED' AND ChargeTo = '" + cbchargedv.getSelectedItem() + "' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String icharge = rs.getString("ChargeTo");
                String totalamount = rs.getString("GrossAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveDVByDate() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher WHERE Status = 'APPROVED' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String icharge = rs.getString("ChargeTo");
                String totalamount = rs.getString("GrossAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void CountCalculateDV() {
        double totalamount = 0;
        for (int i = 0; i < tabledv.getRowCount(); i++) {
            double total = Double.parseDouble(tabledv.getValueAt(i, 2).toString());
            totalamount += total;

        }
        overalltotal = Double.toString(totalamount);
    }

    private void PrintDV() {
        int row = tabledv.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportDV.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintDVByCompany() {
        int row = tabledv.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            parameters.put("chargeto", charge);
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportDVChargeTo.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void RetrieveCompanyDV() {
        cbchargedv.addItem("All");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblcompany");
            while (rs.next()) {
                String chart = rs.getString("CompanyName");
                cbchargedv.addItem(chart);
            }
        } catch (SQLException e) {
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
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        dtfrom3 = new datechooser.beans.DateChooserCombo();
        dtto3 = new datechooser.beans.DateChooserCombo();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablemr3 = new javax.swing.JTable();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        dtfromdv = new datechooser.beans.DateChooserCombo();
        dttodv = new datechooser.beans.DateChooserCombo();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabledv = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        cbchargedv = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        dtfrom = new datechooser.beans.DateChooserCombo();
        dtto = new datechooser.beans.DateChooserCombo();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablemr = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        dtfrommw = new datechooser.beans.DateChooserCombo();
        dttomw = new datechooser.beans.DateChooserCombo();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablemw = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        cbchargemw = new javax.swing.JComboBox();
        jPanel12 = new javax.swing.JPanel();
        dtfrommw1 = new datechooser.beans.DateChooserCombo();
        dttomw1 = new datechooser.beans.DateChooserCombo();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablemw1 = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        cbchargemw1 = new javax.swing.JComboBox();
        cbchargemw2 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 52, 66));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DATA REPORT");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

        dtfrom3.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));

    dtto3.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));

jLabel10.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
jLabel10.setText("From :");

jLabel11.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
jLabel11.setText("To :");

jButton11.setBackground(new java.awt.Color(45, 52, 66));
jButton11.setForeground(new java.awt.Color(255, 255, 255));
jButton11.setText("Load");
jButton11.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton11ActionPerformed(evt);
    }
    });

    tablemr3.setBackground(new java.awt.Color(107, 115, 131));
    tablemr3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
    tablemr3.setForeground(new java.awt.Color(255, 255, 255));
    tablemr3.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "MR Code", "Quantity", "Unit Cost", "Total Amount", "Date"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    tablemr3.setGridColor(new java.awt.Color(204, 204, 204));
    tablemr3.setSelectionBackground(new java.awt.Color(45, 52, 66));
    tablemr3.setSelectionForeground(new java.awt.Color(235, 235, 236));
    tablemr3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    tablemr3.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tablemr3MouseClicked(evt);
        }
    });
    jScrollPane4.setViewportView(tablemr3);
    if (tablemr3.getColumnModel().getColumnCount() > 0) {
        tablemr3.getColumnModel().getColumn(2).setHeaderValue("Unit Cost");
    }

    jButton12.setBackground(new java.awt.Color(45, 52, 66));
    jButton12.setForeground(new java.awt.Color(255, 255, 255));
    jButton12.setText("Print");
    jButton12.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton12ActionPerformed(evt);
        }
    });

    jButton13.setBackground(new java.awt.Color(45, 52, 66));
    jButton13.setForeground(new java.awt.Color(255, 255, 255));
    jButton13.setText("Close");
    jButton13.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton13ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
    jPanel11.setLayout(jPanel11Layout);
    jPanel11Layout.setHorizontalGroup(
        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addComponent(jLabel10)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtfrom3, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel11)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtto3, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane4))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );
    jPanel11Layout.setVerticalGroup(
        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dtto3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dtfrom3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton11)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton12)
                .addComponent(jButton13))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
    jPanel10.setLayout(jPanel10Layout);
    jPanel10Layout.setHorizontalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel10Layout.createSequentialGroup()
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
    );
    jPanel10Layout.setVerticalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("RECEIPT VOUCHER", jPanel10);

    jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    dtfromdv.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));

dttodv.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
    new datechooser.view.appearance.ViewAppearance("custom",
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.ButtonPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(0, 0, 255),
            true,
            true,
            new datechooser.view.appearance.swing.ButtonPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 255),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.ButtonPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(128, 128, 128),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.LabelPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.LabelPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(255, 0, 0),
            false,
            false,
            new datechooser.view.appearance.swing.ButtonPainter()),
        (datechooser.view.BackRenderer)null,
        false,
        true)));

jLabel8.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
jLabel8.setText("From :");

jLabel9.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
jLabel9.setText("To :");

tabledv.setBackground(new java.awt.Color(107, 115, 131));
tabledv.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
tabledv.setForeground(new java.awt.Color(255, 255, 255));
tabledv.setModel(new javax.swing.table.DefaultTableModel(
new Object [][] {

    },
    new String [] {
        "DV Code", "Charge To", "Total Amount", "Date"
    }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    tabledv.setGridColor(new java.awt.Color(204, 204, 204));
    tabledv.setSelectionBackground(new java.awt.Color(45, 52, 66));
    tabledv.setSelectionForeground(new java.awt.Color(235, 235, 236));
    tabledv.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    tabledv.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tabledvMouseClicked(evt);
        }
    });
    jScrollPane3.setViewportView(tabledv);

    jButton9.setBackground(new java.awt.Color(45, 52, 66));
    jButton9.setForeground(new java.awt.Color(255, 255, 255));
    jButton9.setText("Print");
    jButton9.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton9ActionPerformed(evt);
        }
    });

    jButton10.setBackground(new java.awt.Color(45, 52, 66));
    jButton10.setForeground(new java.awt.Color(255, 255, 255));
    jButton10.setText("Close");
    jButton10.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton10ActionPerformed(evt);
        }
    });

    cbchargedv.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbchargedvActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addComponent(jLabel8)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtfromdv, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel9)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dttodv, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cbchargedv, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dttodv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dtfromdv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbchargedv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton9)
                .addComponent(jButton10))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("DISBURSEMENT VOUCHER", jPanel3);

    jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    dtfrom.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dtfrom.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
    public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
        dtfromOnSelectionChange(evt);
    }
    });

    dtto.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dtto.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
    public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
        dttoOnSelectionChange(evt);
    }
    });

    jLabel4.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel4.setText("From :");

    jLabel5.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel5.setText("To :");

    tablemr.setBackground(new java.awt.Color(107, 115, 131));
    tablemr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
    tablemr.setForeground(new java.awt.Color(255, 255, 255));
    tablemr.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "MR Code", "Quantity", "Unit Cost", "Total Amount", "Date"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    tablemr.setGridColor(new java.awt.Color(204, 204, 204));
    tablemr.setSelectionBackground(new java.awt.Color(45, 52, 66));
    tablemr.setSelectionForeground(new java.awt.Color(235, 235, 236));
    tablemr.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    tablemr.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tablemrMouseClicked(evt);
        }
    });
    jScrollPane1.setViewportView(tablemr);
    if (tablemr.getColumnModel().getColumnCount() > 0) {
        tablemr.getColumnModel().getColumn(2).setHeaderValue("Unit Cost");
    }

    jButton1.setBackground(new java.awt.Color(45, 52, 66));
    jButton1.setForeground(new java.awt.Color(255, 255, 255));
    jButton1.setText("Print");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jButton2.setBackground(new java.awt.Color(45, 52, 66));
    jButton2.setForeground(new java.awt.Color(255, 255, 255));
    jButton2.setText("Close");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addComponent(jLabel4)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtfrom, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel5)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dtto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dtfrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton1)
                .addComponent(jButton2))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("MERCHANDISE RECEIPT", jPanel5);

    jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    dtfrommw.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));

dttomw.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
    new datechooser.view.appearance.ViewAppearance("custom",
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.ButtonPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(0, 0, 255),
            true,
            true,
            new datechooser.view.appearance.swing.ButtonPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 255),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.ButtonPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(128, 128, 128),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.LabelPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.LabelPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(255, 0, 0),
            false,
            false,
            new datechooser.view.appearance.swing.ButtonPainter()),
        (datechooser.view.BackRenderer)null,
        false,
        true)));
dttomw.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
    dttomwOnSelectionChange(evt);
    }
    });

    jLabel6.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel6.setText("From :");

    jLabel7.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel7.setText("To :");

    tablemw.setBackground(new java.awt.Color(107, 115, 131));
    tablemw.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
    tablemw.setForeground(new java.awt.Color(255, 255, 255));
    tablemw.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "MW Code", "Quantity", "Unit Cost", "Total Amount", "Date"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    tablemw.setGridColor(new java.awt.Color(204, 204, 204));
    tablemw.setSelectionBackground(new java.awt.Color(45, 52, 66));
    tablemw.setSelectionForeground(new java.awt.Color(235, 235, 236));
    tablemw.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    tablemw.setShowVerticalLines(true);
    tablemw.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tablemwMouseClicked(evt);
        }
    });
    jScrollPane2.setViewportView(tablemw);

    jButton6.setBackground(new java.awt.Color(45, 52, 66));
    jButton6.setForeground(new java.awt.Color(255, 255, 255));
    jButton6.setText("Print");
    jButton6.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton6ActionPerformed(evt);
        }
    });

    jButton7.setBackground(new java.awt.Color(45, 52, 66));
    jButton7.setForeground(new java.awt.Color(255, 255, 255));
    jButton7.setText("Close");
    jButton7.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton7ActionPerformed(evt);
        }
    });

    cbchargemw.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbchargemwActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtfrommw, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dttomw, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbchargemw, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addContainerGap())
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dttomw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dtfrommw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbchargemw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton6)
                .addComponent(jButton7))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("MERCHADISE WITHDRAWAL", jPanel4);

    jPanel12.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    dtfrommw1.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));

dttomw1.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
    new datechooser.view.appearance.ViewAppearance("custom",
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.ButtonPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(0, 0, 255),
            true,
            true,
            new datechooser.view.appearance.swing.ButtonPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 255),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.ButtonPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(128, 128, 128),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.LabelPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(0, 0, 255),
            false,
            true,
            new datechooser.view.appearance.swing.LabelPainter()),
        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12),
            new java.awt.Color(0, 0, 0),
            new java.awt.Color(255, 0, 0),
            false,
            false,
            new datechooser.view.appearance.swing.ButtonPainter()),
        (datechooser.view.BackRenderer)null,
        false,
        true)));
dttomw1.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
    dttomw1OnSelectionChange(evt);
    }
    });

    jLabel12.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel12.setText("From :");

    jLabel13.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel13.setText("To :");

    tablemw1.setBackground(new java.awt.Color(107, 115, 131));
    tablemw1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
    tablemw1.setForeground(new java.awt.Color(255, 255, 255));
    tablemw1.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "MW Code", "Quantity", "Unit Cost", "Total Amount", "Date"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    tablemw1.setGridColor(new java.awt.Color(204, 204, 204));
    tablemw1.setSelectionBackground(new java.awt.Color(45, 52, 66));
    tablemw1.setSelectionForeground(new java.awt.Color(235, 235, 236));
    tablemw1.setSelectionMode();
    tablemw1.setShowVerticalLines(true);
    tablemw1.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tablemw1MouseClicked(evt);
        }
    });
    jScrollPane5.setViewportView(tablemw1);

    jButton8.setBackground(new java.awt.Color(45, 52, 66));
    jButton8.setForeground(new java.awt.Color(255, 255, 255));
    jButton8.setText("Print");
    jButton8.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton8ActionPerformed(evt);
        }
    });

    jButton14.setBackground(new java.awt.Color(45, 52, 66));
    jButton14.setForeground(new java.awt.Color(255, 255, 255));
    jButton14.setText("Close");
    jButton14.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton14ActionPerformed(evt);
        }
    });

    cbchargemw1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbchargemw1ActionPerformed(evt);
        }
    });

    cbchargemw2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbchargemw2ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
    jPanel12.setLayout(jPanel12Layout);
    jPanel12Layout.setHorizontalGroup(
        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel12Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addComponent(jLabel12)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtfrommw1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel13)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dttomw1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cbchargemw1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cbchargemw2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel12Layout.setVerticalGroup(
        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel12Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dttomw1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dtfrommw1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbchargemw1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbchargemw2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton8)
                .addComponent(jButton14))
            .addContainerGap())
    );

    jTabbedPane1.addTab("OTHER REPORT", jPanel12);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTabbedPane1))
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        PrintMR();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tablemrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemrMouseClicked
        int row = tablemr.getSelectedRow();
        DefaultTableModel TableModel = (DefaultTableModel) tablemr.getModel();
    }//GEN-LAST:event_tablemrMouseClicked

    private void tablemwMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemwMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablemwMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if ("All".equals(charge)) {
            PrintMW();
        } else {
            PrintMWByCompany();
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tabledvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledvMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabledvMouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if ("All".equals(charge)) {
            PrintDV();
        } else {
            PrintDVByCompany();
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void tablemr3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemr3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablemr3MouseClicked

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void dttomwOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dttomwOnSelectionChange
        // TODO add your handling code here:
    }//GEN-LAST:event_dttomwOnSelectionChange

    private void cbchargedvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbchargedvActionPerformed
        if ("All".equals(cbchargedv.getSelectedItem())) {
            charge = cbchargedv.getSelectedItem().toString();
            datefrom = dtfromdv.getText().replace("/", "-");
            dateto = dttodv.getText().replace("/", "-");
            retrieveDVByDate();
            CountCalculateDV();
            charge = "All";
        } else {
            charge = cbchargedv.getSelectedItem().toString();
            datefrom = dtfromdv.getText().replace("/", "-");
            dateto = dttodv.getText().replace("/", "-");
            retrieveDVByDateCharge();
            CountCalculateDV();
        }
    }//GEN-LAST:event_cbchargedvActionPerformed

    private void cbchargemwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbchargemwActionPerformed
        if ("All".equals(cbchargemw.getSelectedItem())) {
            charge = cbchargemw.getSelectedItem().toString();
            datefrom = dtfrommw.getText().replace("/", "-");
            dateto = dttomw.getText().replace("/", "-");
            retrieveMWByDate();
            CountCalculateMW();
            charge = "All";
        } else {
            charge = cbchargemw.getSelectedItem().toString();
            datefrom = dtfrommw.getText().replace("/", "-");
            dateto = dttomw.getText().replace("/", "-");
            retrieveMWByDateCharge();
            CountCalculateMW();
        }
    }//GEN-LAST:event_cbchargemwActionPerformed

    private void dttoOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dttoOnSelectionChange
        datefrom = dtfrom.getText().replace("/", "-");
        dateto = dtto.getText().replace("/", "-");
        retrieveMRByDate();
        CountCalculateMR();
    }//GEN-LAST:event_dttoOnSelectionChange

    private void dtfromOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dtfromOnSelectionChange
        datefrom = dtfrom.getText().replace("/", "-");
        dateto = dtto.getText().replace("/", "-");
        retrieveMRByDate();
        CountCalculateMR();
    }//GEN-LAST:event_dtfromOnSelectionChange

    private void dttomw1OnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dttomw1OnSelectionChange
        // TODO add your handling code here:
    }//GEN-LAST:event_dttomw1OnSelectionChange

    private void tablemw1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemw1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablemw1MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void cbchargemw1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbchargemw1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbchargemw1ActionPerformed

    private void cbchargemw2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbchargemw2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbchargemw2ActionPerformed

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
            java.util.logging.Logger.getLogger(FrameReport.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameReport.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameReport.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameReport.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new FrameReport().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbchargedv;
    private javax.swing.JComboBox cbchargemw;
    private javax.swing.JComboBox cbchargemw1;
    private javax.swing.JComboBox cbchargemw2;
    private datechooser.beans.DateChooserCombo dtfrom;
    private datechooser.beans.DateChooserCombo dtfrom3;
    private datechooser.beans.DateChooserCombo dtfromdv;
    private datechooser.beans.DateChooserCombo dtfrommw;
    private datechooser.beans.DateChooserCombo dtfrommw1;
    private datechooser.beans.DateChooserCombo dtto;
    private datechooser.beans.DateChooserCombo dtto3;
    private datechooser.beans.DateChooserCombo dttodv;
    private datechooser.beans.DateChooserCombo dttomw;
    private datechooser.beans.DateChooserCombo dttomw1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tabledv;
    private javax.swing.JTable tablemr;
    private javax.swing.JTable tablemr3;
    private javax.swing.JTable tablemw;
    private javax.swing.JTable tablemw1;
    // End of variables declaration//GEN-END:variables
}
