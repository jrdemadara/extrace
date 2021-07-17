package frames;

import classes.DatabaseConnection;
import static classes.NumberFunction.getFormattedNumber;
import java.awt.Color;
import java.awt.Toolkit;
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
    static double refund;

    public FrameDisbursementVoucher() {
        initComponents();
        Refresh();
        setIconImage();
    }

    private void setIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/icon-60px.png")));
    }

    private void Refresh() {
        CodeGenerator();
        RetrieveChart();
        RetrieveSupplier();
        RetrieveCompany();
        RetrieveRFWallet();
        btsave.setText("Save");
        btclose.setText("Close");
        btadd.setEnabled(true);
        btremove.setEnabled(true);
        txtdescription.setText("");
        txtcheque.setText("");
        lblinfo.setVisible(false);
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
            stmt.close();
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
            stmt.close();
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
            stmt.close();
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
            stmt.close();
        } catch (SQLException e) {
        }
    }

    private void RetrieveChart() {
        cbfundsource.removeAllItems();
        cbfundsource.addItem("Select Fund Source");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblchartofaccount WHERE ChartName LIKE '%BANK%' OR ChartName LIKE '%REVOLVING%'");
            while (rs.next()) {
                String chart = rs.getString("ChartName");
                cbfundsource.addItem(chart);
            }
            stmt.close();
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
        txtdescription.setText(description);
        txtcheque.setText(cheque);
        txttotal.setText(amount);
        btsave.setText("Update");
        btclose.setText("Cancel");
        btadd.setEnabled(false);
        btremove.setEnabled(false);
        lblinfo.setVisible(true);
        lblinfo.setText("INFO: On the table section, only the amount can be edited.");
        refund = Double.parseDouble(amount);
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

    private void SaveEntry() {
        //save debit
        for (int i = 0; i < table.getRowCount(); i++) {
            String code = table.getValueAt(i, 0).toString();
            String account = table.getValueAt(i, 1).toString();
            String amount = table.getValueAt(i, 2).toString();
            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbljournal VALUES(?,?,?,?,?,?,?,?,?)")) {
                stmt.setInt(1, 0);
                stmt.setString(2, cbdate.getText());
                stmt.setString(3, lblcode.getText());
                stmt.setString(4, code);
                stmt.setString(5, account);
                stmt.setString(6, amount);
                stmt.setString(7, "0");
                stmt.setString(8, "OPEN");
                stmt.setString(9, cbchargeto.getSelectedItem().toString());
                stmt.execute();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //save credit
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbljournal VALUES(?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, cbdate.getText());
            stmt.setString(3, lblcode.getText());
            stmt.setString(4, txtcode.getText());
            stmt.setString(5, cbfundsource.getSelectedItem().toString());
            stmt.setString(6, "0");
            stmt.setString(7, txttotal.getText());
            stmt.setString(8, "OPEN");
            stmt.setString(9, cbchargeto.getSelectedItem().toString());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void SaveDisbursement() {
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
            stmt.setString(7, txtdescription.getText().toUpperCase());
            stmt.setString(8, txtcheque.getText());
            stmt.setString(9, txttotal.getText());
            stmt.setString(10, cbdate.getText());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(this, "Disbursement Voucher '" + lblcode.getText() + "' has been saved!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Save() {
        //Check if revolving fund is used
        String fundsource = cbfundsource.getSelectedItem().toString();
        if ("REVOLVING FUNDS".equals(fundsource)) {
            //Check if their is revolving fund exist
            try (Statement stmt0 = connection.createStatement()) {
                ResultSet rs = stmt0.executeQuery("SELECT * FROM tblrfwallet WHERE Status = 'OPEN'");
                if (rs.next()) {
                    //if revolving fund exist
                    //get the current amount of revolving fund
                    double balance = rs.getDouble("Balance");
                    double totalamount = Double.parseDouble(txttotal.getText());
                    //check if fund is sufficient
                    if (balance >= totalamount) {
                        //deduct amount in balance
                        double newbalance = balance - totalamount;
                        //Save entry
                        SaveEntry();
                        //save disbursement voucher
                        SaveDisbursement();
                        //update revolving fund balance
                        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblrfwallet SET Balance = ? WHERE Status = 'OPEN'")) {
                            stmt.setDouble(1, newbalance);
                            stmt.execute();
                            stmt.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Refresh();
                    } else {
                        //if insufficient balance
                        JOptionPane.showMessageDialog(this, "Insufficient fund.\n You only have " + lblwallet.getText() + " in your revolving fund.", " System Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    //if no revolving fund exist
                    JOptionPane.showMessageDialog(this, "No revolving fund exist.", " System Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Save entry
            SaveEntry();
            //save disbursement voucher
            SaveDisbursement();
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
                    RetrieveRFWallet();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void UpdateEntry() {
        //update debit
        for (int i = 0; i < table.getRowCount(); i++) {
            String code = table.getValueAt(i, 0).toString();
            String amount = table.getValueAt(i, 2).toString();
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbljournal SET Debit = ? WHERE Reference = ? AND Credit = '0.00' AND Code = ?")) {
                stmt.setString(1, amount);
                stmt.setString(2, lblcode.getText());
                stmt.setString(3, code);
                stmt.executeUpdate();
                stmt.close();

            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        //update credit
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbljournal SET Code = ?, Account = ?, Credit = ?, Company = ? WHERE Reference = ? AND Debit = '0.00'")) {
            stmt.setString(1, txtcode.getText());
            stmt.setString(2, cbfundsource.getSelectedItem().toString());
            stmt.setString(3, txttotal.getText());
            stmt.setString(4, cbchargeto.getSelectedItem().toString());
            stmt.setString(5, lblcode.getText());
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void UpdateWallet() {
        try (Statement stmt0 = connection.createStatement()) {
            ResultSet rs = stmt0.executeQuery("SELECT * FROM tblrfwallet WHERE Status = 'OPEN'");
            if (rs.next()) {
                double balance = rs.getDouble("Balance");
                double totalamount = Double.parseDouble(txttotal.getText());
                double newbalance = balance - totalamount;
                //update revolving fund balance
                try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblrfwallet SET Balance = ? WHERE Status = 'OPEN'")) {
                    stmt.setDouble(1, newbalance);
                    stmt.execute();
                    stmt.close();

                } catch (SQLException ex) {
                    Logger.getLogger(FrameDisbursementVoucher.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //if no revolving fund exist
                JOptionPane.showMessageDialog(this, "No revolving fund exist.", " System Error", JOptionPane.ERROR_MESSAGE);

            }
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void UpdateDisbursement() {
        //Update Disbursement
        for (int i = 0; i < table.getRowCount(); i++) {
            String code = table.getValueAt(i, 0).toString();
            String account = table.getValueAt(i, 1).toString();
            String amount = table.getValueAt(i, 2).toString();
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbldisbursementvoucherparticular SET Amount = ? WHERE Account = ? AND DisbursementCode = ?")) {
                stmt.setString(1, amount);
                stmt.setString(2, account);
                stmt.setString(3, lblcode.getText());
                stmt.executeUpdate();
                stmt.close();

            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbldisbursementvoucher SET ChargeTo = ?, Payee = ?,  TINNumber = ?, FundSource = ?, Description = ?, CheckNo = ?, TotalAmount = ? WHERE DisbursementCode = ?")) {
            stmt.setString(1, cbchargeto.getSelectedItem().toString());
            stmt.setString(2, cbpayee.getSelectedItem().toString());
            stmt.setString(3, txttin.getText());
            stmt.setString(4, cbfundsource.getSelectedItem().toString());
            stmt.setString(5, txtdescription.getText().toUpperCase());
            stmt.setString(6, txtcheque.getText());
            stmt.setString(7, txttotal.getText());
            stmt.setString(8, lblcode.getText());
            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(this, "Disbursement Voucher '" + lblcode.getText() + "' has been updated!", " System Information", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Update() {
        //Check if revolving fund is used
        String fundsource = cbfundsource.getSelectedItem().toString();
        if ("REVOLVING FUNDS".equals(fundsource)) {
            //Refund Wallet
            RefundWallet();
            //Check if their is revolving fund exist
            try (Statement stmt0 = connection.createStatement()) {
                ResultSet rs = stmt0.executeQuery("SELECT * FROM tblrfwallet WHERE Status = 'OPEN'");
                if (rs.next()) {
                    //if revolving fund exist
                    //get the current amount of revolving fund
                    double balance = rs.getDouble("Balance");
                    double totalamount = Double.parseDouble(txttotal.getText());
                    //check if fund is sufficient
                    if (balance >= totalamount) {
                        //Update entry
                        UpdateEntry();
                        //Update disbursement voucher
                        UpdateDisbursement();
                        //Update Wallet
                        UpdateWallet();
                        Refresh();
                    } else {
                        //if insufficient balance
                        JOptionPane.showMessageDialog(this, "Insufficient fund.\n You only have " + lblwallet.getText() + " in your revolving fund.", " System Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    //if no revolving fund exist
                    JOptionPane.showMessageDialog(this, "No revolving fund exist.", " System Error", JOptionPane.ERROR_MESSAGE);

                }
            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Update entry
            UpdateEntry();
            //Update disbursement voucher
            UpdateDisbursement();
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
                String date = classes.DateFunction.getFormattedYearMonth();
                String dateout = date.replaceAll("-", "");
                String newcode = "DVC" + dateout + "1";
                code = newcode;
                lblcode.setText(code);

            } else {
                int temp = Integer.parseInt(code);
                temp += 1;
                String output = Integer.toString(temp);
                String date = classes.DateFunction.getFormattedYearMonth();
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
        lblwallet = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbfundsource = new javax.swing.JComboBox();
        cbpayee = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cbchargeto = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        txttin = new javax.swing.JTextField();
        txtcode = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtdescription = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtcheque = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cbdate = new datechooser.beans.DateChooserCombo();
        jLabel11 = new javax.swing.JLabel();
        btsave = new javax.swing.JButton();
        btclose = new javax.swing.JButton();
        btadd = new javax.swing.JButton();
        btremove = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        lblinfo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Extrace");
        setAlwaysOnTop(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 52, 66));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DISBURSEMENT VOUCHER CODE:");

        lblcode.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblcode.setForeground(new java.awt.Color(255, 153, 0));
        lblcode.setText("000000");

        lblwallet.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblwallet.setForeground(new java.awt.Color(255, 153, 0));
        lblwallet.setText("₱0");

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("REVOLVING FUND WALLET:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblcode)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblwallet, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(132, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblcode)
                    .addComponent(lblwallet)
                    .addComponent(jLabel12))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("Payee");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Description");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("Fund Source");

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

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("Charge To");

        cbchargeto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbchargetoActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel10.setText("TIN Number");

        txttin.setEditable(false);
        txttin.setBackground(new java.awt.Color(238, 238, 238));
        txttin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txttinKeyReleased(evt);
            }
        });

        txtcode.setEditable(false);
        txtcode.setBackground(new java.awt.Color(238, 238, 238));

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel9.setText("Code");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText("Cheque No.");

        txtcheque.setText("N/A");

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel7.setText("Total Amount");

        txttotal.setEditable(false);
        txttotal.setText("0");
        txttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel8.setText("Date");

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

    jLabel11.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
    jLabel11.setText("(optional)");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cbchargeto, javax.swing.GroupLayout.Alignment.LEADING, 0, 227, Short.MAX_VALUE)
                        .addComponent(cbfundsource, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(cbpayee, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addComponent(txttin, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9)
                                .addComponent(txtcode, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtdescription)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(0, 304, Short.MAX_VALUE)))))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel11))
                        .addComponent(txtcheque, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addGap(0, 154, Short.MAX_VALUE))
                        .addComponent(txttotal))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel8)
                        .addComponent(cbdate, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cbchargeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cbpayee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel10)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txttin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(jLabel3)
                .addComponent(jLabel9))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbfundsource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtdescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel11))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtcheque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel8)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cbdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    btsave.setBackground(new java.awt.Color(45, 52, 66));
    btsave.setForeground(new java.awt.Color(255, 255, 255));
    btsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_save_16px.png"))); // NOI18N
    btsave.setText("Save");
    btsave.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btsaveActionPerformed(evt);
        }
    });

    btclose.setBackground(new java.awt.Color(45, 52, 66));
    btclose.setForeground(new java.awt.Color(255, 255, 255));
    btclose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_close_window_16px.png"))); // NOI18N
    btclose.setText("Close");
    btclose.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btcloseActionPerformed(evt);
        }
    });

    btadd.setBackground(new java.awt.Color(45, 52, 66));
    btadd.setForeground(new java.awt.Color(255, 255, 255));
    btadd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_add_16px.png"))); // NOI18N
    btadd.setText("Add Account");
    btadd.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btaddActionPerformed(evt);
        }
    });

    btremove.setBackground(new java.awt.Color(107, 115, 131));
    btremove.setForeground(new java.awt.Color(255, 255, 255));
    btremove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_cancel_16px.png"))); // NOI18N
    btremove.setText("Remove Account");
    btremove.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btremoveActionPerformed(evt);
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
    table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    table.setShowHorizontalLines(true);
    jScrollPane2.setViewportView(table);

    lblinfo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    lblinfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_info_16px_4.png"))); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btsave, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btclose, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(btadd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btremove, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblinfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(lblinfo)
            .addGap(5, 5, 5)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btadd)
                .addComponent(btremove))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btclose)
                .addComponent(btsave))
            .addContainerGap())
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsaveActionPerformed
        CalculateParticular();
        double amount = Double.parseDouble(txttotal.getText());
        if (amount == 0) {
            JOptionPane.showMessageDialog(this, "Please enter amount.", " System Warning", JOptionPane.ERROR_MESSAGE);
            txttotal.setForeground(Color.red);
        } else {
            if (!"Select Payee".equals(cbpayee.getSelectedItem()) && !"Select Fund".equals(cbfundsource.getSelectedItem()) && !"".equals(txtdescription.getText())) {
                if ("Save".equals(btsave.getText())) {
                    Save();
                } else {
                    Update();
                }
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

    private void btaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btaddActionPerformed
        FrameChartofAccount frame = new FrameChartofAccount();
        frame.setVisible(true);
    }//GEN-LAST:event_btaddActionPerformed

    private void btremoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btremoveActionPerformed
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
    }//GEN-LAST:event_btremoveActionPerformed

    private void cbpayeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbpayeeActionPerformed
        RetrieveTin();
    }//GEN-LAST:event_cbpayeeActionPerformed

    private void cbfundsourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbfundsourceActionPerformed
        RetrieveCode();
    }//GEN-LAST:event_cbfundsourceActionPerformed

    private void cbchargetoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbchargetoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbchargetoActionPerformed

    private void txttinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttinKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txttinKeyReleased

    private void txttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalActionPerformed
        CalculateParticular();
        txttotal.setForeground(Color.black);
    }//GEN-LAST:event_txttotalActionPerformed

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
            java.util.logging.Logger.getLogger(FrameDisbursementVoucher.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameDisbursementVoucher.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameDisbursementVoucher.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameDisbursementVoucher.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private static javax.swing.JButton btadd;
    private static javax.swing.JButton btclose;
    private static javax.swing.JButton btremove;
    private static javax.swing.JButton btsave;
    private static javax.swing.JComboBox cbchargeto;
    private datechooser.beans.DateChooserCombo cbdate;
    private static javax.swing.JComboBox cbfundsource;
    private static javax.swing.JComboBox cbpayee;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private static javax.swing.JLabel lblinfo;
    private static javax.swing.JLabel lblwallet;
    private static javax.swing.JTable table;
    private static javax.swing.JTextField txtcheque;
    private javax.swing.JTextField txtcode;
    private static javax.swing.JTextField txtdescription;
    private static javax.swing.JTextField txttin;
    private static javax.swing.JTextField txttotal;
    // End of variables declaration//GEN-END:variables
}
