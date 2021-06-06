package dvmanage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrameDisbursementVoucher extends javax.swing.JFrame {

    DatabaseConnection conn = new DatabaseConnection();
    DefaultTableModel tablemodel;
    Connection connection = conn.getConnection();
    DefaultTableModel dm;
    int particulars;
    String user;

    public FrameDisbursementVoucher() {
        initComponents();
        Refresh();
    }

    public void GetUser(String name) {
        user = name;
    }

    private void Refresh() {
        CodeGenerator();
        RetrieveChart();
        RetrieveSupplier();
        RetrieveCompany();
        btsave.setText("Save");
        btclose.setText("Close");
        cbdec.setSelectedIndex(0);
        txtcheque.setText("");
        DefaultTableModel TableModel = (DefaultTableModel) table.getModel();
        while (TableModel.getRowCount() > 0) {
            TableModel.removeRow(0);
        }
    }

    private void RetrieveSupplier() {
        cbpayee.addItem("Select Payee");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblsupplier");
            while (rs.next()) {
                String chart = rs.getString("SupplierName");
                cbpayee.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveTin() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblsupplier WHERE SupplierName = '" + cbpayee.getSelectedItem() + "'");
            while (rs.next()) {
                String chart = rs.getString("TIN");
                txttin.setText(chart);
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveCode() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblchartofaccount WHERE ChartName = '" + cbfundsource.getSelectedItem() + "'");
            while (rs.next()) {
                String chart = rs.getString("ChartCode");
                txtcode.setText(chart);
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveCompany() {
        cbchargeto.addItem("Select Company");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblcompany");
            while (rs.next()) {
                String chart = rs.getString("CompanyName");
                cbchargeto.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveChart() {
        cbfundsource.addItem("Select Fund");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblchartofaccount");
            while (rs.next()) {
                String chart = rs.getString("ChartName");
                cbfundsource.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    public static void LoadParticular(Object[] dataRow) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(dataRow);
    }

    public static void LoadDV(String code, String chargeto, String payee, String tin, String fund, String description, String cheque, String amount) {
        lblcode.setText(code);
        cbchargeto.setSelectedItem(chargeto);
        cbpayee.setSelectedItem(payee);
        txttin.setText(tin);
        cbfundsource.setSelectedItem(fund);
        cbdec.setSelectedItem(description);
        txtcheque.setText(cheque);
        txttotal.setText(amount);
        btsave.setText("Update");
        btclose.setText("Cancel");
    }

    public static void LoadDVParticular(Object[] dataRow) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(dataRow);
    }

    public static void CalculateParticular() {
        double grandtotal = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            String amount = table.getValueAt(i, 2).toString();
            double namount = Double.parseDouble(amount);
            grandtotal += namount;

        }
        txttotal.setText(Double.toString(grandtotal));
    }

    private void SaveJournalToSetup() {
        try (Statement stmt0 = connection.createStatement()) {
            ResultSet rs = stmt0.executeQuery("SELECT * FROM tbljournal WHERE Account = 'REVOLVING FUNDS'");
            if (rs.next()) {
                double bal = Double.parseDouble(rs.getString("Balance"));
                double amount = Double.parseDouble(txttotal.getText());
                double newbal = amount + bal;
                String account = table.getValueAt(0, 1).toString();
                try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbljournal SET Debit = ?, Balance = ? WHERE Account = ?")) {
                    stmt.setDouble(1, newbal);
                    stmt.setDouble(2, newbal);
                    stmt.setString(3, account);
                    stmt.execute();
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //Save Journal Entry Debit

                for (int i = 0; i < table.getRowCount(); i++) {
                    String code = table.getValueAt(i, 0).toString();
                    String account = table.getValueAt(i, 1).toString();
                    String amount = table.getValueAt(i, 2).toString();
                    try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbljournal VALUES(?,?,?,?,?,?,?,?,?,?)")) {
                        stmt.setInt(1, 0);
                        stmt.setString(2, cbdate.getText());
                        stmt.setString(3, lblcode.getText());
                        stmt.setString(4, code);
                        stmt.setString(5, account);
                        stmt.setString(6, amount);
                        stmt.setString(7, "0");
                        stmt.setString(8, amount);
                        stmt.setString(9, "OPEN");
                        stmt.setString(10, cbchargeto.getSelectedItem().toString());
                        stmt.execute();
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (SQLException e) {
        }

        //Save Journal Entry Credit
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbljournal VALUES(?,?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, cbdate.getText());
            stmt.setString(3, lblcode.getText());
            stmt.setString(4, txtcode.getText());
            stmt.setString(5, cbfundsource.getSelectedItem().toString());
            stmt.setString(6, "0");
            stmt.setString(7, txttotal.getText());
            stmt.setString(8, "0");
            stmt.setString(9, "OPEN");
            stmt.setString(10, cbchargeto.getSelectedItem().toString());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void SaveJournalToReplenish() {
        //Save Journal Entry Debit
        for (int i = 0; i < table.getRowCount(); i++) {
            String code = table.getValueAt(i, 0).toString();
            String account = table.getValueAt(i, 1).toString();
            String amount = table.getValueAt(i, 2).toString();
            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbljournal VALUES(?,?,?,?,?,?,?,?,?,?)")) {
                stmt.setInt(1, 0);
                stmt.setString(2, cbdate.getText());
                stmt.setString(3, lblcode.getText());
                stmt.setString(4, code);
                stmt.setString(5, account);
                stmt.setString(6, amount);
                stmt.setString(7, "0");
                stmt.setString(8, "0");
                stmt.setString(9, "OPEN");
                stmt.setString(10, cbchargeto.getSelectedItem().toString());
                stmt.execute();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        //Save Journal Entry Credit
//        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbljournal VALUES(?,?,?,?,?,?,?,?,?)")) {
//            stmt.setInt(1, 0);
//            stmt.setString(2, cbdate.getText());
//            stmt.setString(3, lblcode.getText());
//            stmt.setString(4, txtcode.getText());
//            stmt.setString(5, cbfundsource.getSelectedItem().toString());
//            stmt.setString(6, "0");
//            stmt.setString(7, txttotal.getText());
//            stmt.setString(8, "OPEN");
//            stmt.setString(9, cbchargeto.getSelectedItem().toString());
//            stmt.execute();
//            stmt.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private void SaveJournalOther() {
        //Save Journal Entry Debit
        for (int i = 0; i < table.getRowCount(); i++) {
            String code = table.getValueAt(i, 0).toString();
            String account = table.getValueAt(i, 1).toString();
            String amount = table.getValueAt(i, 2).toString();
            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbljournal VALUES(?,?,?,?,?,?,?,?,?,?)")) {
                stmt.setInt(1, 0);
                stmt.setString(2, cbdate.getText());
                stmt.setString(3, lblcode.getText());
                stmt.setString(4, code);
                stmt.setString(5, account);
                stmt.setString(6, amount);
                stmt.setString(7, "0");
                stmt.setString(8, amount);
                stmt.setString(9, "OPEN");
                stmt.setString(10, cbchargeto.getSelectedItem().toString());
                stmt.execute();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Save Journal Entry Credit
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbljournal VALUES(?,?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, cbdate.getText());
            stmt.setString(3, lblcode.getText());
            stmt.setString(4, txtcode.getText());
            stmt.setString(5, cbfundsource.getSelectedItem().toString());
            stmt.setString(6, "0");
            stmt.setString(7, txttotal.getText());
            stmt.setString(8, "0");
            stmt.setString(9, "OPEN");
            stmt.setString(10, cbchargeto.getSelectedItem().toString());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void UpdateRevolvingCredit() {
        //Get Current Revolving Fund Balance
        double revbalance = 0;
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbljournal WHERE Account = 'REVOLVING FUNDS'");
            if (rs.next()) {
                double bal = Double.parseDouble(rs.getString("Balance"));
                double amount = Double.parseDouble(txttotal.getText());
                revbalance = bal - amount;
            }
        } catch (SQLException e) {
        }
        //Save Revolving Credit
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbljournal SET Credit = ?, Balance = ? WHERE Account = ?")) {
            stmt.setString(1, txttotal.getText());
            stmt.setDouble(2, revbalance);
            stmt.setString(3, cbfundsource.getSelectedItem().toString());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        private void UpdateOther() {
        //Get Current Revolving Fund Balance
        double revbalance = 0;
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbljournal WHERE Account = '"+cbfundsource.getSelectedItem().toString().toUpperCase()+"'");
            if (rs.next()) {
                double bal = Double.parseDouble(rs.getString("Balance"));
                double amount = Double.parseDouble(txttotal.getText());
                revbalance = bal - amount;
            }
        } catch (SQLException e) {
        }
        //Save Credit
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbljournal SET Credit = ?, Balance = ? WHERE Account = ?")) {
            stmt.setString(1, txttotal.getText());
            stmt.setDouble(2, revbalance);
            stmt.setString(3, cbfundsource.getSelectedItem().toString().toUpperCase());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void UpdateJournal() {
        //Update Journal Entry Debit
        for (int i = 0; i < table.getRowCount(); i++) {
            String code = table.getValueAt(i, 0).toString();
            String account = table.getValueAt(i, 1).toString();
            String amount = table.getValueAt(i, 2).toString();
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbljournal SET debit = ?, credit = ?, Company = ? WHERE Code = ? AND Reference = ?")) {
                stmt.setString(1, amount);
                stmt.setString(2, "0");
                stmt.setString(3, cbchargeto.getSelectedItem().toString());
                stmt.setString(4, code);
                stmt.setString(5, lblcode.getText());
                stmt.execute();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Update Journal Entry Credit
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbljournal SET debit = ?, credit = ?, Company = ? WHERE Code = ? AND Reference = ?")) {
            stmt.setString(1, "0");
            stmt.setString(2, txttotal.getText());
            stmt.setString(3, cbchargeto.getSelectedItem().toString());
            stmt.setString(4, txtcode.getText());
            stmt.setString(5, lblcode.getText());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Save() {

        //Save Item
        for (int i = 0; i < table.getRowCount(); i++) {
            String code = table.getValueAt(i, 0).toString();
            String account = table.getValueAt(i, 1).toString();
            String amount = table.getValueAt(i, 2).toString();
            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbldisbursementvoucherparticular VALUES(?,?,?,?,?)")) {
                stmt.setInt(1, 0);
                stmt.setString(2, lblcode.getText());
                stmt.setString(3, code);
                stmt.setString(4, account);
                stmt.setString(5, amount);
                stmt.execute();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbldisbursementvoucher VALUES(?,?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, lblcode.getText());
            stmt.setString(3, cbchargeto.getSelectedItem().toString());
            stmt.setString(4, cbpayee.getSelectedItem().toString());
            stmt.setString(5, txttin.getText().toUpperCase());
            stmt.setString(6, cbfundsource.getSelectedItem().toString());
            stmt.setString(7, cbdec.getSelectedItem().toString().toUpperCase());
            stmt.setString(8, txtcheque.getText());
            stmt.setString(9, txttotal.getText());
            stmt.setString(10, cbdate.getText());
            stmt.execute();
            stmt.close();
            Refresh();
            JOptionPane.showMessageDialog(this, "Disbursement Voucher '" + lblcode.getText() + "' has been saved!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Update() {
        //Update Journal
        UpdateJournal();
        //Update Disbursement
        for (int i = 0; i < table.getRowCount(); i++) {
            String code = table.getValueAt(i, 0).toString();
            String account = table.getValueAt(i, 1).toString();
            String amount = table.getValueAt(i, 2).toString();
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbldisbursementvoucherparticular SET Amount = ? WHERE Code = ? AND DisbursementCode = ?")) {
                stmt.setString(1, amount);
                stmt.setString(2, code);
                stmt.setString(3, lblcode.getText());
                stmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbldisbursementvoucher SET ChargeTo = ?, Payee = ?,  TINNumber = ?, FundSource = ?, Description = ?, CheckNo = ?, TotalAmount = ? WHERE DisbursementCode = ?")) {
            stmt.setString(1, cbchargeto.getSelectedItem().toString());
            stmt.setString(2, cbpayee.getSelectedItem().toString());
            stmt.setString(3, txttin.getText());
            stmt.setString(4, cbfundsource.getSelectedItem().toString());
            stmt.setString(5, cbdec.getSelectedItem().toString().toUpperCase());
            stmt.setString(6, txtcheque.getText());
            stmt.setString(7, txttotal.getText());
            stmt.setString(8, lblcode.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Disbursement Voucher '" + lblcode.getText() + "' has been updated!", " System Information", JOptionPane.INFORMATION_MESSAGE);
            Refresh();
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String CodeGenerator() {
        String code = null;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Max(ID) FROM tbldisbursementvoucher");
            if (rs.next()) {
                code = rs.getString("Max(ID)");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (code == null) {
                String date = dvmanage.DateFunction.getFormattedYearMonth();
                String dateout = date.replaceAll("-", "");
                String newcode = "DVC" + dateout + "1";
                code = newcode;
                lblcode.setText(code);

            } else {
                int temp = Integer.parseInt(code);
                temp += 1;
                String output = Integer.toString(temp);
                String date = dvmanage.DateFunction.getFormattedYearMonth();
                String dateout = date.replaceAll("-", "");
                String newcode = "DVC" + dateout + output;
                code = newcode;
                lblcode.setText(code);
            }
        }
        return code;
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
        lblcode = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtcheque = new javax.swing.JTextField();
        cbfundsource = new javax.swing.JComboBox();
        cbpayee = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cbchargeto = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        txttin = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        cbdate = new datechooser.beans.DateChooserCombo();
        jLabel8 = new javax.swing.JLabel();
        txtcode = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbdec = new javax.swing.JComboBox();
        txtspec = new javax.swing.JTextField();
        btsave = new javax.swing.JButton();
        btclose = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 52, 66));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DISBURSEMENT VOUCHER CODE:");

        lblcode.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lblcode.setForeground(new java.awt.Color(255, 153, 0));
        lblcode.setText("000000");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblcode)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblcode))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));

        jLabel2.setText("Payee");

        jLabel3.setText("Description");

        jLabel4.setText("Fund Source");

        jLabel5.setText("Cheque No.");

        cbfundsource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbfundsourceActionPerformed(evt);
            }
        });

        cbpayee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbpayeeActionPerformed(evt);
            }
        });

        jLabel6.setText("Charge To");

        cbchargeto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbchargetoActionPerformed(evt);
            }
        });

        jLabel10.setText("TIN Number");

        txttin.setEditable(false);
        txttin.setBackground(new java.awt.Color(238, 238, 238));
        txttin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txttinKeyReleased(evt);
            }
        });

        jLabel7.setText("Total Amount");

        txttotal.setEditable(false);
        txttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalActionPerformed(evt);
            }
        });

        cbdate.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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

    jLabel8.setText("Date");

    txtcode.setEditable(false);

    jLabel9.setText("Code");

    cbdec.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Description", "-------------------------", "To Setup Revolving Fund", "Revolving Fund Replenishment", "Other (Specify)" }));
    cbdec.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbdecActionPerformed(evt);
        }
    });

    txtspec.setText("Specify");
    txtspec.setEnabled(false);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtcheque, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbchargeto, javax.swing.GroupLayout.Alignment.LEADING, 0, 227, Short.MAX_VALUE)
                    .addComponent(cbfundsource, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addComponent(jLabel5))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(cbpayee, 0, 247, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel10)
                        .addComponent(txttin, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel8)
                        .addComponent(cbdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9)
                        .addComponent(txtcode, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(cbdec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtspec)))))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel6)
                    .addGap(38, 38, 38)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel3)
                        .addComponent(jLabel9))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbfundsource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbdec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtspec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(cbchargeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cbpayee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txttin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(56, 56, 56)))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel5)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtcheque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(cbdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(10, Short.MAX_VALUE))
    );

    btsave.setBackground(new java.awt.Color(45, 52, 66));
    btsave.setForeground(new java.awt.Color(255, 255, 255));
    btsave.setText("Save");
    btsave.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btsaveActionPerformed(evt);
        }
    });

    btclose.setBackground(new java.awt.Color(45, 52, 66));
    btclose.setForeground(new java.awt.Color(255, 255, 255));
    btclose.setText("Close");
    btclose.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btcloseActionPerformed(evt);
        }
    });

    jButton3.setBackground(new java.awt.Color(45, 52, 66));
    jButton3.setForeground(new java.awt.Color(255, 255, 255));
    jButton3.setText("Add Account");
    jButton3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton3ActionPerformed(evt);
        }
    });

    jButton4.setBackground(new java.awt.Color(107, 115, 131));
    jButton4.setForeground(new java.awt.Color(255, 255, 255));
    jButton4.setText("Remove Account");
    jButton4.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton4ActionPerformed(evt);
        }
    });

    jButton5.setBackground(new java.awt.Color(45, 52, 66));
    jButton5.setForeground(new java.awt.Color(255, 255, 255));
    jButton5.setText("View List");
    jButton5.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton5ActionPerformed(evt);
        }
    });

    table.setBackground(new java.awt.Color(107, 115, 131));
    table.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
    table.setForeground(new java.awt.Color(255, 255, 255));
    table.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "Code", "Account", "Amount"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, true
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    table.setGridColor(new java.awt.Color(204, 204, 204));
    table.setSelectionBackground(new java.awt.Color(45, 52, 66));
    table.setSelectionForeground(new java.awt.Color(235, 235, 236));
    jScrollPane2.setViewportView(table);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 8, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(btsave, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btclose, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)))))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton3)
                .addComponent(jButton4))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btclose)
                .addComponent(btsave)
                .addComponent(jButton5))
            .addContainerGap())
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsaveActionPerformed
        if ("Save".equals(btsave.getText())) {
            if (cbdec.getSelectedIndex() == 2) {
                //Normal Entry
                UpdateRevolvingCredit();
                SaveJournalToSetup();
            } else if (cbdec.getSelectedIndex() == 3) {
                //Update Revolving Fund
                UpdateRevolvingCredit();
                SaveJournalToReplenish();
            } else if (cbdec.getSelectedIndex() == 4) {
                //Update Revolving Fund
                UpdateOther();
                SaveJournalOther();
            }
            if (!"Select Payee".equals(cbpayee.getSelectedItem()) && !"Select Fund".equals(cbfundsource.getSelectedItem())) {
                Save();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill the required fields.", " System Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            if (!"Select Payee".equals(cbpayee.getSelectedItem()) && !"Select Fund".equals(cbfundsource.getSelectedItem())) {
                Update();

            } else {
                JOptionPane.showMessageDialog(this, "Please fill the required fields.", " System Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btsaveActionPerformed

    private void btcloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btcloseActionPerformed
        if ("Cancel".equals(btclose.getText())) {
            Refresh();
        } else {
            dispose();
        }
    }//GEN-LAST:event_btcloseActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        FrameChartofAccount frame = new FrameChartofAccount();
        frame.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int row = table.getSelectedRow();
        if (row > -1) {
            String item = table.getValueAt(row, 1).toString();
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove Item '" + item + "'?", " System Information", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                tableModel.removeRow(row);
                CalculateParticular();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No selected data!", " System Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cbpayeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbpayeeActionPerformed
        RetrieveTin();
    }//GEN-LAST:event_cbpayeeActionPerformed

    private void cbfundsourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbfundsourceActionPerformed
        RetrieveCode();
    }//GEN-LAST:event_cbfundsourceActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        FrameDVList frame = new FrameDVList();
        frame.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cbchargetoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbchargetoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbchargetoActionPerformed

    private void txttinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttinKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txttinKeyReleased

    private void txttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalActionPerformed
        CalculateParticular();
    }//GEN-LAST:event_txttotalActionPerformed

    private void cbdecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbdecActionPerformed
        if (cbdec.getSelectedIndex() == 4) {
            txtspec.setEnabled(true);
        }
    }//GEN-LAST:event_cbdecActionPerformed

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
            java.util.logging.Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new FrameDisbursementVoucher().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton btclose;
    private static javax.swing.JButton btsave;
    private static javax.swing.JComboBox cbchargeto;
    private datechooser.beans.DateChooserCombo cbdate;
    private static javax.swing.JComboBox cbdec;
    private static javax.swing.JComboBox cbfundsource;
    private static javax.swing.JComboBox cbpayee;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JLabel lblcode;
    private static javax.swing.JTable table;
    private static javax.swing.JTextField txtcheque;
    private javax.swing.JTextField txtcode;
    private javax.swing.JTextField txtspec;
    private static javax.swing.JTextField txttin;
    private static javax.swing.JTextField txttotal;
    // End of variables declaration//GEN-END:variables
}
