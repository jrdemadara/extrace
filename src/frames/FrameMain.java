package frames;

import classes.DatabaseConnection;
import classes.NumberFunction;
import static classes.NumberFunction.getFormattedNumber;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public final class FrameMain extends javax.swing.JFrame {

    DatabaseConnection conn = new DatabaseConnection();
    Connection connection = conn.getConnection();
    DefaultTableModel tablemodel;
    DefaultTableModel dm;
    //Company Details
    String company;
    String address;
    String tin;
    String email;
    String phone;

    double refund;

    public FrameMain() {
        initComponents();
        RetrieveVersion();
        SetIconImage();
        RetrieveCompanyDetails();
        Refresh();
    }

    private void SetIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/icon-60px.png")));
        lbllogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo-64.png")));
    }

    public void Refresh() {
        RetrieveDV();
        RetrieveJE();
        RetrieveRF();
        RetrieveCompanyDetails();
        RetrieveDVParticular();
        RetrieveRFWallet();
        refund = 0;
    }

    private void SearchRF(String query) {
        dm = (DefaultTableModel) tabledv.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);
        tablerf.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    private void SearchDV(String query) {
        dm = (DefaultTableModel) tabledv.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);
        tabledv.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    private void SearchJE(String query) {
        dm = (DefaultTableModel) tabledv.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);
        tablejournal.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    private void ClearDVparticular() {
        DefaultTableModel tableModel = (DefaultTableModel) tableparticular.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
    }

    private void RetrieveVersion() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblversion");
            if (rs.next()) {
                lblversion.setText(rs.getString("Version"));
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveRFWallet() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblrfwallet");
            if (rs.next()) {
                String balance = rs.getString("Balance");
                lblwallet.setText("₱" + getFormattedNumber(balance));
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameRevolvingFund.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void RetrieveCompanyDetails() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblcompanydetails");
            if (rs.next()) {
                lblcompany.setText(rs.getString("Company"));
                lbladdress.setText(rs.getString("Address"));
                company = rs.getString("Company");
                address = rs.getString("address");
                tin = rs.getString("tin");
                email = rs.getString("email");
                phone = rs.getString("phone");

            } else {
                lbldatatype.setText("Welcome!");
                lblcode.setText("Please setup the company details.");
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveRF() {
        DefaultTableModel tableModel = (DefaultTableModel) tablerf.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblrevolvingfund");
            while (rs.next()) {
                String rfcode = rs.getString("RFCode");
                String fundsource = rs.getString("FundSource");
                String code = rs.getString("Code");
                String description = rs.getString("Description");
                String amount = rs.getString("Amount");
                String icompany = rs.getString("Company");
                String status = rs.getString("Status");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{rfcode, fundsource, code, description, amount, icompany, status, date});
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveDV() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String charge = rs.getString("ChargeTo");
                String payee = rs.getString("Payee");
                String itin = rs.getString("TINNumber");
                String fund = rs.getString("FundSource");
                String des = rs.getString("Description");
                String check = rs.getString("CheckNo");
                String amount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, charge, payee, itin, fund, des, check, amount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveJE() {
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
                String deb = rs.getString("Debit");
                String cre = rs.getString("Credit");
                String stat = rs.getString("Status");
                String com = rs.getString("Company");
                tableModel.addRow(new Object[]{date, ref, code, acc, deb, cre, stat, com});
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveDVParticular() {
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            DefaultTableModel TableModel1 = (DefaultTableModel) tabledv.getModel();
            DefaultTableModel TableModel2 = (DefaultTableModel) tableparticular.getModel();
            lblcode.setText(TableModel1.getValueAt(row, 0).toString());
            refund = Double.parseDouble(TableModel1.getValueAt(row, 7).toString());
            lbldatatype.setText("DISBURSEMENT VOUCHER CODE");
            while (TableModel2.getRowCount() > 0) {
                ClearDVparticular();
            }
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucherparticular WHERE DisbursementCode = '" + lblcode.getText() + "' ");
                while (rs.next()) {
                    String dvcode = rs.getString("DisbursementCode");
                    String code = rs.getString("Code");
                    String account = rs.getString("Account");
                    String amount = rs.getString("Amount");
                    TableModel2.addRow(new Object[]{dvcode, code, account, amount});
                }
                stmt.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void PrintDV() {
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            String gross = tabledv.getValueAt(row, 7).toString();
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("dvcode", lblcode.getText());
            parameters.put("total", "₱ " + NumberFunction.getFormattedNumber(gross));
            parameters.put("company", company);
            parameters.put("address", address);
            parameters.put("tin", tin);
            parameters.put("email", email);
            parameters.put("phone", phone);
            try {
                InputStream i = getClass().getResourceAsStream("/report/DisbursementVoucher.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMain.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, ex, " System Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data from the table.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void DeleteJournal() {
        int row = tabledv.getSelectedRow();
        String sid = tabledv.getValueAt(row, 0).toString();
        String sqlc = "DELETE FROM tbljournal WHERE Reference=?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlc)) {
            stmt.setString(1, lblcode.getText());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void RefundWallet() {
        //retrieve wallet balance
        double wallet;
        try (Statement stmt0 = connection.createStatement()) {
            ResultSet rs = stmt0.executeQuery("SELECT * FROM tblrfwallet WHERE Status = 'OPEN'");
            if (rs.next()) {
                //get the current amount of revolving fund
                double balance = rs.getDouble("Balance");
                wallet = balance + refund;
                //refund wallet
                try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblrfwallet SET Balance = ? WHERE Status = 'OPEN'")) {
                    stmt.setDouble(1, wallet);
                    stmt.execute();
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void DeleteDV() {
        int row = tabledv.getSelectedRow();
        //Check if revolving fund is used
        String account = tabledv.getValueAt(row, 4).toString();
        if (row > -1) {
            if ("REVOLVING FUNDS".equals(account)) {
                if (JOptionPane.showConfirmDialog(this, "Warning: This action can't be rollback! \n Are you sure you want to delete " + lblcode.getText() + "?  ", " System Question", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    String sid = tabledv.getValueAt(row, 0).toString();
                    String sqlc = "DELETE tbldisbursementvoucher,tbldisbursementvoucherparticular FROM tbldisbursementvoucher INNER JOIN tbldisbursementvoucherparticular ON tbldisbursementvoucherparticular.DisbursementCode = tbldisbursementvoucher.DisbursementCode WHERE tbldisbursementvoucher.DisbursementCode = ?;";
                    try (PreparedStatement stmt = connection.prepareStatement(sqlc)) {
                        stmt.setString(1, lblcode.getText());
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Voucher " + lblcode.getText() + " has been deleted!", " System Information", JOptionPane.INFORMATION_MESSAGE);
                        RefundWallet();
                        DeleteJournal();
                        Refresh();
                        ClearDVparticular();
                    } catch (SQLException ex) {
                        Logger.getLogger(FrameMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else {
                if (JOptionPane.showConfirmDialog(this, "Warning: This action can't be rollback! \n Are you sure you want to delete " + lblcode.getText() + "?  ", " System Question", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    String sid = tabledv.getValueAt(row, 0).toString();
                    String sqlc = "DELETE tbldisbursementvoucher,tbldisbursementvoucherparticular FROM tbldisbursementvoucher INNER JOIN tbldisbursementvoucherparticular ON tbldisbursementvoucherparticular.DisbursementCode = tbldisbursementvoucher.DisbursementCode WHERE tbldisbursementvoucher.DisbursementCode = ?;";
                    try (PreparedStatement stmt = connection.prepareStatement(sqlc)) {
                        stmt.setString(1, lblcode.getText());
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Voucher " + lblcode.getText() + " has been deleted!", " System Information", JOptionPane.INFORMATION_MESSAGE);
                        DeleteJournal();
                        Refresh();
                        ClearDVparticular();
                    } catch (SQLException ex) {
                        Logger.getLogger(FrameMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data.", " System Information", JOptionPane.WARNING_MESSAGE);
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
        jLabel2 = new javax.swing.JLabel();
        lblversion = new javax.swing.JLabel();
        lblcompany = new javax.swing.JLabel();
        lbladdress = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbllogo = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblversion1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btrf = new javax.swing.JButton();
        btdv = new javax.swing.JButton();
        btreport = new javax.swing.JButton();
        lbldatatype = new javax.swing.JLabel();
        lblcode = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablerf = new javax.swing.JTable();
        txtrfs = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        btreportdv = new javax.swing.JButton();
        btdeletedv = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableparticular = new javax.swing.JTable();
        txtsdv = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabledv = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablejournal = new javax.swing.JTable();
        txtjes = new javax.swing.JTextField();
        btmonitoring1 = new javax.swing.JButton();
        btrefresh = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        lblwallet = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        MenuSetup = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        MenuTransaction = new javax.swing.JMenu();
        midv = new javax.swing.JMenuItem();
        midv1 = new javax.swing.JMenuItem();
        MenuSettings = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Extrace");
        setMaximumSize(new java.awt.Dimension(1366, 768));
        setMinimumSize(new java.awt.Dimension(1200, 678));
        setSize(new java.awt.Dimension(1200, 678));

        jPanel1.setBackground(new java.awt.Color(45, 52, 66));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Extrace");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 0));
        jLabel2.setText("Expenditure Tracker");

        lblversion.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblversion.setForeground(new java.awt.Color(255, 255, 255));
        lblversion.setText("0.0.0");

        lblcompany.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblcompany.setForeground(new java.awt.Color(255, 255, 255));
        lblcompany.setText("COMPANY NAME");

        lbladdress.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lbladdress.setForeground(new java.awt.Color(255, 153, 0));
        lbladdress.setText("Address");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icon-60px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblversion)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbllogo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblcompany, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(lbladdress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbllogo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGap(7, 7, 7)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(lblcompany, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(lbladdress, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(lblversion))
                                    .addGap(0, 0, 0)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(11, 11, 11)))))
        );

        jPanel3.setBackground(new java.awt.Color(45, 52, 66));

        lblversion1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblversion1.setForeground(new java.awt.Color(255, 255, 255));
        lblversion1.setText(" All Rights Reserved.");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Copyright © 2021 Slicksoft Coder");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblversion1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblversion1)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

        btrf.setBackground(new java.awt.Color(255, 153, 0));
        btrf.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btrf.setForeground(new java.awt.Color(255, 255, 255));
        btrf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_cost_32px.png"))); // NOI18N
        btrf.setText("REVOLVING FUND");
        btrf.setVerifyInputWhenFocusTarget(false);
        btrf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btrfActionPerformed(evt);
            }
        });

        btdv.setBackground(new java.awt.Color(255, 153, 0));
        btdv.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btdv.setForeground(new java.awt.Color(255, 255, 255));
        btdv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_tips_32px.png"))); // NOI18N
        btdv.setText("DISBURSEMENT");
        btdv.setVerifyInputWhenFocusTarget(false);
        btdv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btdvActionPerformed(evt);
            }
        });

        btreport.setBackground(new java.awt.Color(255, 153, 0));
        btreport.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btreport.setForeground(new java.awt.Color(255, 255, 255));
        btreport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_report_file_32px.png"))); // NOI18N
        btreport.setText("REPORT");
        btreport.setVerifyInputWhenFocusTarget(false);
        btreport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btreportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btrf, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btdv, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btreport, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btrf, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btdv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btreport, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbldatatype.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        lbldatatype.setForeground(new java.awt.Color(45, 52, 66));
        lbldatatype.setText("CODE");

        lblcode.setFont(new java.awt.Font("sansserif", 1, 32)); // NOI18N
        lblcode.setForeground(new java.awt.Color(255, 153, 0));
        lblcode.setText("000000");

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

        tablerf.setBackground(new java.awt.Color(107, 115, 131));
        tablerf.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tablerf.setForeground(new java.awt.Color(255, 255, 255));
        tablerf.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RFCode", "Fund Source", "Code", "Description", "Account", "Company", "Status", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablerf.setGridColor(new java.awt.Color(204, 204, 204));
        tablerf.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tablerf.setSelectionForeground(new java.awt.Color(235, 235, 236));
        tablerf.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablerf.setShowHorizontalLines(true);
        tablerf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablerfMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tablerfMouseEntered(evt);
            }
        });
        jScrollPane6.setViewportView(tablerf);

        txtrfs.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtrfs.setText("Search...");
        txtrfs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtrfsKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1174, Short.MAX_VALUE)
                    .addComponent(txtrfs))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtrfs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1188, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 465, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Revolving Fund", jPanel7);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

        btreportdv.setBackground(new java.awt.Color(45, 52, 66));
        btreportdv.setForeground(new java.awt.Color(255, 255, 255));
        btreportdv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_print_16px.png"))); // NOI18N
        btreportdv.setText("Print");
        btreportdv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btreportdvActionPerformed(evt);
            }
        });

        btdeletedv.setBackground(new java.awt.Color(45, 52, 66));
        btdeletedv.setForeground(new java.awt.Color(255, 255, 255));
        btdeletedv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_waste_16px.png"))); // NOI18N
        btdeletedv.setText("Delete");
        btdeletedv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btdeletedvActionPerformed(evt);
            }
        });

        tableparticular.setBackground(new java.awt.Color(107, 115, 131));
        tableparticular.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        tableparticular.setForeground(new java.awt.Color(255, 255, 255));
        tableparticular.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DV Code", "Code", "Account", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableparticular.setGridColor(new java.awt.Color(204, 204, 204));
        tableparticular.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tableparticular.setSelectionForeground(new java.awt.Color(235, 235, 236));
        tableparticular.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tableparticular);

        txtsdv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsdv.setText("Search...");
        txtsdv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsdvKeyReleased(evt);
            }
        });

        tabledv.setBackground(new java.awt.Color(107, 115, 131));
        tabledv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabledv.setForeground(new java.awt.Color(255, 255, 255));
        tabledv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DV Code", "Charge To", "Payee", "TIN", "Fund Source", "Description", "Cheque No.", "Total Amount", "Date"
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
        tabledv.setShowHorizontalLines(true);
        tabledv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabledvMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tabledv);

        jButton3.setBackground(new java.awt.Color(45, 52, 66));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_update_file_16px.png"))); // NOI18N
        jButton3.setText("Update");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btreportdv, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btdeletedv, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtsdv)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1174, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btreportdv)
                    .addComponent(btdeletedv)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Disbursement Voucher", jPanel5);

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

        tablejournal.setBackground(new java.awt.Color(107, 115, 131));
        tablejournal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tablejournal.setForeground(new java.awt.Color(255, 255, 255));
        tablejournal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Reference", "Code", "Account", "Debit", "Credit", "Status", "Company"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablejournal.setGridColor(new java.awt.Color(204, 204, 204));
        tablejournal.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tablejournal.setSelectionForeground(new java.awt.Color(235, 235, 236));
        tablejournal.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablejournal.setShowHorizontalLines(true);
        tablejournal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablejournalMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tablejournalMouseEntered(evt);
            }
        });
        jScrollPane3.setViewportView(tablejournal);

        txtjes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtjes.setText("Search...");
        txtjes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtjesKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1174, Short.MAX_VALUE)
                    .addComponent(txtjes))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtjes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Journal Entry", jPanel11);

        btmonitoring1.setBackground(new java.awt.Color(255, 153, 0));
        btmonitoring1.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btmonitoring1.setForeground(new java.awt.Color(255, 255, 255));
        btmonitoring1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_logout_rounded_down_16px.png"))); // NOI18N
        btmonitoring1.setToolTipText("Logout");
        btmonitoring1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btmonitoring1.setVerifyInputWhenFocusTarget(false);
        btmonitoring1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmonitoring1ActionPerformed(evt);
            }
        });

        btrefresh.setBackground(new java.awt.Color(255, 153, 0));
        btrefresh.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btrefresh.setForeground(new java.awt.Color(255, 255, 255));
        btrefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_refresh_16px.png"))); // NOI18N
        btrefresh.setToolTipText("Refresh");
        btrefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btrefresh.setVerifyInputWhenFocusTarget(false);
        btrefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btrefreshActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(45, 52, 66));
        jLabel12.setText("RF WALLET:");

        lblwallet.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lblwallet.setForeground(new java.awt.Color(255, 153, 0));
        lblwallet.setText("₱0");

        MenuSetup.setText("Setup");

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_chart_16px.png"))); // NOI18N
        jMenuItem8.setText("Chart of Account");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        MenuSetup.add(jMenuItem8);

        jMenuItem16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_supplier_16px.png"))); // NOI18N
        jMenuItem16.setText("Supplier");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        MenuSetup.add(jMenuItem16);

        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_unit_16px.png"))); // NOI18N
        jMenuItem10.setText("Unit");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        MenuSetup.add(jMenuItem10);

        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_department_16px.png"))); // NOI18N
        jMenuItem13.setText("Company");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        MenuSetup.add(jMenuItem13);

        jMenuBar1.add(MenuSetup);

        MenuTransaction.setText("Transaction");

        midv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_tips_16px.png"))); // NOI18N
        midv.setText("Cash Disbursement Voucher");
        midv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                midvActionPerformed(evt);
            }
        });
        MenuTransaction.add(midv);

        midv1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_cost_16px_1.png"))); // NOI18N
        midv1.setText("Revolving Fund");
        midv1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                midv1ActionPerformed(evt);
            }
        });
        MenuTransaction.add(midv1);

        jMenuBar1.add(MenuTransaction);

        MenuSettings.setText("Settings");

        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_user_16px.png"))); // NOI18N
        jMenuItem11.setText("User Account");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        MenuSettings.add(jMenuItem11);

        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_new_company_16px.png"))); // NOI18N
        jMenuItem14.setText("Company Details");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        MenuSettings.add(jMenuItem14);

        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_database_administrator_16px.png"))); // NOI18N
        jMenuItem12.setText("Database Settings");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        MenuSettings.add(jMenuItem12);

        jMenuBar1.add(MenuSettings);

        jMenu5.setText("Help");

        jMenuItem19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_help_16px_4.png"))); // NOI18N
        jMenuItem19.setText("Help Contents");
        jMenu5.add(jMenuItem19);

        jMenuItem20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_bug_16px_2.png"))); // NOI18N
        jMenuItem20.setText("Bug Report");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem20);

        jMenuItem21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_info_16px_4.png"))); // NOI18N
        jMenuItem21.setText("About");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem21);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbldatatype, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblcode))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btrefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btmonitoring1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblwallet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbldatatype, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lblcode, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btmonitoring1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(btrefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblwallet)
                            .addComponent(jLabel12))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        FrameChartofAccount frame = new FrameChartofAccount();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        FrameUnit frame = new FrameUnit();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void midvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_midvActionPerformed
        FrameDisbursementVoucher frame = new FrameDisbursementVoucher();
        frame.setVisible(true);
    }//GEN-LAST:event_midvActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        FrameUser frame = new FrameUser();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void btmonitoring1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmonitoring1ActionPerformed
        dispose();
        FrameLogin frame = new FrameLogin();
        frame.setVisible(true);
    }//GEN-LAST:event_btmonitoring1ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        FrameSupplier frame = new FrameSupplier();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void tabledvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledvMouseClicked
        RetrieveDVParticular();
    }//GEN-LAST:event_tabledvMouseClicked

    private void txtsdvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsdvKeyReleased
        SearchDV(txtsdv.getText().toUpperCase());
    }//GEN-LAST:event_txtsdvKeyReleased

    private void btrefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btrefreshActionPerformed
        Refresh();
    }//GEN-LAST:event_btrefreshActionPerformed

    private void btreportdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btreportdvActionPerformed
        PrintDV();
    }//GEN-LAST:event_btreportdvActionPerformed

    private void btdeletedvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeletedvActionPerformed
        DeleteDV();
    }//GEN-LAST:event_btdeletedvActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        FrameCompany frame = new FrameCompany();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void midv1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_midv1ActionPerformed
        FrameRevolvingFund frame = new FrameRevolvingFund();
        frame.setVisible(true);
    }//GEN-LAST:event_midv1ActionPerformed

    private void btrfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btrfActionPerformed
        FrameRevolvingFund frame = new FrameRevolvingFund();
        frame.setVisible(true);
    }//GEN-LAST:event_btrfActionPerformed

    private void txtjesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtjesKeyReleased
        SearchJE(txtjes.getText().toUpperCase());
    }//GEN-LAST:event_txtjesKeyReleased

    private void tablejournalMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablejournalMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tablejournalMouseEntered

    private void tablejournalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablejournalMouseClicked
        int row = tablejournal.getSelectedRow();
        DefaultTableModel TableModel = (DefaultTableModel) tablejournal.getModel();
        if (row > -1) {
            lblcode.setText(TableModel.getValueAt(row, 1).toString());
            lbldatatype.setText("JOURNAL REFERENCE");
        } else {
            //Do nothing!
        }
    }//GEN-LAST:event_tablejournalMouseClicked

    private void txtrfsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtrfsKeyReleased
        SearchRF(txtrfs.getText().toUpperCase());
    }//GEN-LAST:event_txtrfsKeyReleased

    private void tablerfMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablerfMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tablerfMouseEntered

    private void tablerfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablerfMouseClicked
        int row = tablerf.getSelectedRow();
        if (row > -1) {
            DefaultTableModel TableModel = (DefaultTableModel) tablerf.getModel();
            lblcode.setText(TableModel.getValueAt(row, 0).toString());
            lbldatatype.setText("REVOLVING FUND CODE");
        }
    }//GEN-LAST:event_tablerfMouseClicked

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        FrameCompanyDetails frame = new FrameCompanyDetails();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void btdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdvActionPerformed
        FrameDisbursementVoucher frame = new FrameDisbursementVoucher();
        frame.setVisible(true);
    }//GEN-LAST:event_btdvActionPerformed

    private void btreportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btreportActionPerformed
        FrameReport frame = new FrameReport();
        frame.setVisible(true);
    }//GEN-LAST:event_btreportActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        FrameDisbursementVoucher frame = new FrameDisbursementVoucher();
        frame.setVisible(true);
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            String code = tabledv.getValueAt(row, 0).toString();
            String charge = tabledv.getValueAt(row, 1).toString();
            String payee = tabledv.getValueAt(row, 2).toString();
            String itin = tabledv.getValueAt(row, 3).toString();
            String fund = tabledv.getValueAt(row, 4).toString();
            String des = tabledv.getValueAt(row, 5).toString();
            String cheque = tabledv.getValueAt(row, 6).toString();
            String amount = tabledv.getValueAt(row, 7).toString();
            FrameDisbursementVoucher.LoadDV(code, charge, payee, itin, fund, des, cheque, amount);
            for (int i = 0; i < tableparticular.getRowCount(); i++) {
                String pcode = tableparticular.getValueAt(i, 1).toString();
                String pacount = tableparticular.getValueAt(i, 2).toString();
                String pamount = tableparticular.getValueAt(i, 3).toString();
                FrameDisbursementVoucher.LoadDVParticular(new Object[]{
                    pcode,
                    pacount,
                    pamount,});
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item.", " System Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        FrameAbout frame = new FrameAbout();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        FrameBugReport frame = new FrameBugReport();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem20ActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new FrameMain().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu MenuSettings;
    private javax.swing.JMenu MenuSetup;
    private javax.swing.JMenu MenuTransaction;
    private javax.swing.JButton btdeletedv;
    private javax.swing.JButton btdv;
    private javax.swing.JButton btmonitoring1;
    private javax.swing.JButton btrefresh;
    private javax.swing.JButton btreport;
    private javax.swing.JButton btreportdv;
    private javax.swing.JButton btrf;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbladdress;
    private javax.swing.JLabel lblcode;
    private javax.swing.JLabel lblcompany;
    private javax.swing.JLabel lbldatatype;
    private javax.swing.JLabel lbllogo;
    private javax.swing.JLabel lblversion;
    private javax.swing.JLabel lblversion1;
    private static javax.swing.JLabel lblwallet;
    private javax.swing.JMenuItem midv;
    private javax.swing.JMenuItem midv1;
    private javax.swing.JTable tabledv;
    private javax.swing.JTable tablejournal;
    private static javax.swing.JTable tableparticular;
    private javax.swing.JTable tablerf;
    private javax.swing.JTextField txtjes;
    private javax.swing.JTextField txtrfs;
    private javax.swing.JTextField txtsdv;
    // End of variables declaration//GEN-END:variables
}
