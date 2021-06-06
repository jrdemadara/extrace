package dvmanage;

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
import static dvmanage.NumberFunction.getFormattedNumber;
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
    String account;
    String printtype = "bydate";
    String credit;
    String debit;

    public FrameReport() {
        initComponents();
        RetrieveAccountDV();
        RetrieveCompany();
        retrieveDV();
        retrieveJournal();

    }

    public void SetImport(String type) {
        importtype = type;
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
                String chargeto = rs.getString("ChargeTo");
                String payee = rs.getString("Payee");
                String tin = rs.getString("TINNumber");
                String fund = rs.getString("FundSource");
                String des = rs.getString("Description");
                String check = rs.getString("CheckNo");
                String amount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, chargeto, payee, tin, fund, des, check, amount, date});
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher WHERE Date BETWEEN '" + dtfromdv.getText() + "' AND '" + dttodv.getText() + "' ");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String chargeto = rs.getString("ChargeTo");
                String payee = rs.getString("Payee");
                String tin = rs.getString("TINNumber");
                String fund = rs.getString("FundSource");
                String des = rs.getString("Description");
                String check = rs.getString("CheckNo");
                String amount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, chargeto, payee, tin, fund, des, check, amount, date});
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher WHERE ChargeTo = '" + cbchargedv.getSelectedItem() + "' AND Date BETWEEN '" + dtfromdv.getText() + "' AND '" + dttodv.getText() + "' ");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String chargeto = rs.getString("ChargeTo");
                String payee = rs.getString("Payee");
                String tin = rs.getString("TINNumber");
                String fund = rs.getString("FundSource");
                String des = rs.getString("Description");
                String check = rs.getString("CheckNo");
                String amount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, chargeto, payee, tin, fund, des, check, amount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveDVByDateAccount() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher WHERE FundSource = '" + cbaccountdv.getSelectedItem() + "' AND Date BETWEEN '" + dtfromdv.getText() + "' AND '" + dttodv.getText() + "' ");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String chargeto = rs.getString("ChargeTo");
                String payee = rs.getString("Payee");
                String tin = rs.getString("TINNumber");
                String fund = rs.getString("FundSource");
                String des = rs.getString("Description");
                String check = rs.getString("CheckNo");
                String amount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, chargeto, payee, tin, fund, des, check, amount, date});
            }
        } catch (SQLException e) {
        }
    }

    //********************JOURNAL ENTRY*************************//
    private void retrieveJournal() {
        DefaultTableModel tableModel = (DefaultTableModel) tablejournal.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbljournal");
            while (rs.next()) {
                String date = rs.getString("Date");
                String ref = rs.getString("Reference");
                String code = rs.getString("Code");
                String acc = rs.getString("Account");
                String idebit = rs.getString("Debit");
                String icredit = rs.getString("Credit");
                String bal = rs.getString("Balance");
                String stat = rs.getString("Status");
                String company = rs.getString("Company");
                tableModel.addRow(new Object[]{date, ref, code, acc, idebit, icredit, bal, stat, company});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveJournalByDate() {
        DefaultTableModel tableModel = (DefaultTableModel) tablejournal.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbljournal WHERE Status = 'OPEN' AND Date BETWEEN '" + dtfromjournal.getText() + "' AND '" + dttojournal.getText() + "' ");
            while (rs.next()) {
                String date = rs.getString("Date");
                String ref = rs.getString("Reference");
                String code = rs.getString("Code");
                String acc = rs.getString("Account");
                String idebit = rs.getString("Debit");
                String icredit = rs.getString("Credit");
                String bal = rs.getString("Balance");
                String stat = rs.getString("Status");
                String company = rs.getString("Company");
                tableModel.addRow(new Object[]{date, ref, code, acc, idebit, icredit, bal, stat, company});
            }

        } catch (SQLException e) {
        }
    }

    private void retrieveJournalByDateCharge() {
        DefaultTableModel tableModel = (DefaultTableModel) tablejournal.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbljournal WHERE Status = 'OPEN' AND Company = '" + cbchargejournal.getSelectedItem() + "' AND Date BETWEEN '" + dtfromjournal.getText() + "' AND '" + dttojournal.getText() + "' ");
            while (rs.next()) {
                String date = rs.getString("Date");
                String ref = rs.getString("Reference");
                String code = rs.getString("Code");
                String acc = rs.getString("Account");
                String idebit = rs.getString("Debit");
                String icredit = rs.getString("Credit");
                String bal = rs.getString("Balance");
                String stat = rs.getString("Status");
                String company = rs.getString("Company");
                tableModel.addRow(new Object[]{date, ref, code, acc, idebit, icredit, bal, stat, company
                });
            }
        } catch (SQLException e) {
        }
    }

    private void CountCalculateDV() {
        double totalamount = 0;
        for (int i = 0; i < tabledv.getRowCount(); i++) {
            double total = Double.parseDouble(tabledv.getValueAt(i, 7).toString());
            totalamount += total;
        }
        overalltotal = Double.toString(totalamount);
    }

    private void CountCalculateJournal() {
        credit = "0";
        debit = "0";
        double totaldebit = 0;
        double totalcredit = 0;
        for (int i = 0; i < tablejournal.getRowCount(); i++) {
            double idebit = Double.parseDouble(tablejournal.getValueAt(i, 4).toString());
            double icredit = Double.parseDouble(tablejournal.getValueAt(i, 5).toString());
            totaldebit += idebit;
            totalcredit += icredit;
        }
        debit = Double.toString(totaldebit);
        credit = Double.toString(totalcredit);
    }

    private void PrintDV() {
        int row = tabledv.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", dtfromdv.getText());
            parameters.put("dateto", dttodv.getText());
            parameters.put("total", "Php " + getFormattedNumber(overalltotal));
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportDV.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                JOptionPane.showMessageDialog(this, "Something wrong.", " System Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintDVByCompany() {
        int row = tabledv.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", dtfromdv.getText());
            parameters.put("dateto", dttodv.getText());
            parameters.put("total", "Php " + getFormattedNumber(overalltotal));
            parameters.put("chargeto", charge);
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportDVChargeTo.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);

            } catch (JRException ex) {
                Logger.getLogger(FrameReport.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintDVByAccount() {
        int row = tabledv.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", dtfromdv.getText());
            parameters.put("dateto", dttodv.getText());
            parameters.put("total", "Php " + getFormattedNumber(overalltotal));
            parameters.put("account", account);
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportDVAccount.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);

            } catch (JRException ex) {
                Logger.getLogger(FrameReport.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintJournal() {
        int row = tablejournal.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", dtfromjournal.getText());
            parameters.put("dateto", dttojournal.getText());
            parameters.put("totaldebit", "Php " + getFormattedNumber(debit));
            parameters.put("totalcredit", "Php " + getFormattedNumber(credit));
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportJournalEntry.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);

            } catch (JRException ex) {
                Logger.getLogger(FrameReport.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintJournalByCompany() {
        int row = tablejournal.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("company", cbchargejournal.getSelectedItem());
            parameters.put("datefrom", dtfromjournal.getText());
            parameters.put("dateto", dttojournal.getText());
            parameters.put("totaldebit", "Php " + getFormattedNumber(debit));
            parameters.put("totalcredit", "Php " + getFormattedNumber(credit));
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportJournalEntryCompany.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);

            } catch (JRException ex) {
                Logger.getLogger(FrameReport.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void RetrieveCompany() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblcompany");
            while (rs.next()) {
                String chart = rs.getString("CompanyName");
                cbchargedv.addItem(chart);
                cbchargejournal.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveAccountDV() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblchartofaccount");
            while (rs.next()) {
                String chart = rs.getString("ChartName");
                cbaccountdv.addItem(chart);
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
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabledv = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        dtfromdv = new datechooser.beans.DateChooserCombo();
        jLabel11 = new javax.swing.JLabel();
        dttodv = new datechooser.beans.DateChooserCombo();
        jRadioButton4 = new javax.swing.JRadioButton();
        jPanel14 = new javax.swing.JPanel();
        cbchargedv = new javax.swing.JComboBox();
        cbaccountdv = new javax.swing.JComboBox();
        checkboxaccountdv = new javax.swing.JCheckBox();
        checkboxcompanydv = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablejournal = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        dtfromjournal = new datechooser.beans.DateChooserCombo();
        jLabel7 = new javax.swing.JLabel();
        dttojournal = new datechooser.beans.DateChooserCombo();
        jRadioButton3 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        cbchargejournal = new javax.swing.JComboBox();
        checkboxcompanyjournal = new javax.swing.JCheckBox();

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

        jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

        tabledv.setBackground(new java.awt.Color(107, 115, 131));
        tabledv.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        tabledv.setForeground(new java.awt.Color(255, 255, 255));
        tabledv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DV Code", "Charge To", "Payee", "TIN", "Fund Source", "Description", "Cheque", "Total Amount", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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

        jPanel13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

        jLabel10.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel10.setText("From :");

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

    jLabel11.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel11.setText("To :");

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
dttodv.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
    public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
        dttodvOnSelectionChange(evt);
    }
    });
    dttodv.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            dttodvOnCommit(evt);
        }
    });

    jRadioButton4.setSelected(true);
    jRadioButton4.setText("Filter By Date");
    jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jRadioButton4ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
    jPanel13.setLayout(jPanel13Layout);
    jPanel13Layout.setHorizontalGroup(
        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel13Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addComponent(jLabel10)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtfromdv, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel11)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dttodv, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addComponent(jRadioButton4)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel13Layout.setVerticalGroup(
        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jRadioButton4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dttodv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dtfromdv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel14.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    cbchargedv.setEnabled(false);
    cbchargedv.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbchargedvActionPerformed(evt);
        }
    });

    cbaccountdv.setEnabled(false);
    cbaccountdv.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbaccountdvActionPerformed(evt);
        }
    });

    checkboxaccountdv.setText("Filter By Account");
    checkboxaccountdv.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            checkboxaccountdvActionPerformed(evt);
        }
    });

    checkboxcompanydv.setText("Filter By Company");
    checkboxcompanydv.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            checkboxcompanydvActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
    jPanel14.setLayout(jPanel14Layout);
    jPanel14Layout.setHorizontalGroup(
        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel14Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cbaccountdv, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(checkboxaccountdv))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cbchargedv, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(checkboxcompanydv))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel14Layout.setVerticalGroup(
        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel14Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(checkboxaccountdv)
                .addComponent(checkboxcompanydv))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbchargedv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cbaccountdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
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
        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    jTabbedPane1.addTab("DISBURSEMENT VOUCHER", jPanel3);

    jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    tablejournal.setBackground(new java.awt.Color(107, 115, 131));
    tablejournal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
    tablejournal.setForeground(new java.awt.Color(255, 255, 255));
    tablejournal.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "Date", "Ref", "Code", "Account", "Debit", "Credit", "Balance", "Status", "Company"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    tablejournal.setGridColor(new java.awt.Color(204, 204, 204));
    tablejournal.setSelectionBackground(new java.awt.Color(45, 52, 66));
    tablejournal.setSelectionForeground(new java.awt.Color(235, 235, 236));
    tablejournal.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    tablejournal.setShowVerticalLines(true);
    tablejournal.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tablejournalMouseClicked(evt);
        }
    });
    jScrollPane2.setViewportView(tablejournal);

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

    jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    jLabel6.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel6.setText("From :");

    dtfromjournal.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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
dtfromjournal.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);

jLabel7.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
jLabel7.setText("To :");

dttojournal.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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
dttojournal.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
dttojournal.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
    dttojournalOnSelectionChange(evt);
    }
    });
    dttojournal.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            dttojournalOnCommit(evt);
        }
    });

    jRadioButton3.setSelected(true);
    jRadioButton3.setText("Filter By Date");
    jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jRadioButton3ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtfromjournal, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dttojournal, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jRadioButton3)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jRadioButton3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dttojournal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dtfromjournal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    cbchargejournal.setEnabled(false);
    cbchargejournal.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbchargejournalActionPerformed(evt);
        }
    });

    checkboxcompanyjournal.setText("Filter By Company");
    checkboxcompanyjournal.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            checkboxcompanyjournalActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cbchargejournal, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(checkboxcompanyjournal))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(checkboxcompanyjournal)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cbchargejournal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(246, 246, 246)))
            .addContainerGap())
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
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
        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("Journal Entry", jPanel4);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jTabbedPane1)
            .addContainerGap())
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

    private void tablejournalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablejournalMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablejournalMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (null != printtype) {
            switch (printtype) {
                case "bydate":
                    PrintJournal();
                    break;
                case "bycompany":
                    PrintJournalByCompany();
                    break;
            }
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tabledvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledvMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabledvMouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (null != printtype) {
            switch (printtype) {
                case "bydate":
                    PrintDV();
                    break;
                case "byaccount":
                    PrintDVByAccount();
                    break;
                case "bycompany":
                    PrintDVByCompany();
                    break;
            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void dttojournalOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dttojournalOnSelectionChange


    }//GEN-LAST:event_dttojournalOnSelectionChange

    private void cbchargejournalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbchargejournalActionPerformed
        charge = cbchargejournal.getSelectedItem().toString();
        retrieveJournalByDateCharge();
        CountCalculateJournal();
    }//GEN-LAST:event_cbchargejournalActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void dttojournalOnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dttojournalOnCommit
        printtype = "bydate";
        retrieveJournalByDate();
        CountCalculateJournal();
    }//GEN-LAST:event_dttojournalOnCommit

    private void checkboxcompanyjournalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxcompanyjournalActionPerformed
        if (checkboxcompanyjournal.isSelected()) {
            charge = cbchargejournal.getSelectedItem().toString();
            cbchargejournal.setEnabled(true);
            printtype = "bycompany";
            retrieveJournalByDateCharge();
            CountCalculateJournal();
        } else {
            charge = "";
            printtype = "bydate";
            cbchargejournal.setEnabled(false);
            retrieveJournalByDate();
            CountCalculateJournal();
        }
    }//GEN-LAST:event_checkboxcompanyjournalActionPerformed

    private void dttodvOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dttodvOnSelectionChange
        // TODO add your handling code here:
    }//GEN-LAST:event_dttodvOnSelectionChange

    private void dttodvOnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dttodvOnCommit
        printtype = "bydate";
        retrieveDVByDate();
        CountCalculateDV();

    }//GEN-LAST:event_dttodvOnCommit

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void cbchargedvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbchargedvActionPerformed
        charge = cbchargedv.getSelectedItem().toString();
        retrieveDVByDateCharge();
        CountCalculateDV();
    }//GEN-LAST:event_cbchargedvActionPerformed

    private void cbaccountdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbaccountdvActionPerformed
        account = cbaccountdv.getSelectedItem().toString();
        retrieveDVByDateAccount();
        CountCalculateDV();
    }//GEN-LAST:event_cbaccountdvActionPerformed

    private void checkboxaccountdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxaccountdvActionPerformed
        if (checkboxaccountdv.isSelected()) {
            checkboxcompanydv.setSelected(false);
            cbchargedv.setEnabled(false);
            account = cbaccountdv.getSelectedItem().toString();
            cbaccountdv.setEnabled(true);
            printtype = "byaccount";
            retrieveDVByDateAccount();
            CountCalculateDV();
        } else {
            account = "";
            printtype = "bydate";
            cbaccountdv.setEnabled(false);
            retrieveDVByDate();
            CountCalculateDV();

        }
    }//GEN-LAST:event_checkboxaccountdvActionPerformed

    private void checkboxcompanydvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxcompanydvActionPerformed
        if (checkboxcompanydv.isSelected()) {
            checkboxaccountdv.setSelected(false);
            cbaccountdv.setEnabled(false);
            charge = cbchargedv.getSelectedItem().toString();
            cbchargedv.setEnabled(true);
            printtype = "bycompany";
            retrieveDVByDateCharge();
            CountCalculateDV();
        } else {
            charge = "";
            printtype = "bydate";
            cbchargedv.setEnabled(false);
            retrieveDVByDate();
            CountCalculateDV();
        }
    }//GEN-LAST:event_checkboxcompanydvActionPerformed

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
    private javax.swing.JComboBox cbaccountdv;
    private javax.swing.JComboBox cbchargedv;
    private javax.swing.JComboBox cbchargejournal;
    private javax.swing.JCheckBox checkboxaccountdv;
    private javax.swing.JCheckBox checkboxcompanydv;
    private javax.swing.JCheckBox checkboxcompanyjournal;
    private datechooser.beans.DateChooserCombo dtfromdv;
    private datechooser.beans.DateChooserCombo dtfromjournal;
    private datechooser.beans.DateChooserCombo dttodv;
    private datechooser.beans.DateChooserCombo dttojournal;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tabledv;
    private javax.swing.JTable tablejournal;
    // End of variables declaration//GEN-END:variables
}
