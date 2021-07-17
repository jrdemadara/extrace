package frames;

import classes.DatabaseConnection;
import static classes.NumberFunction.getFormattedNumber;
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

public class FrameRevolvingFund extends javax.swing.JFrame {

    DatabaseConnection conn = new DatabaseConnection();
    DefaultTableModel tablemodel;
    Connection connection = conn.getConnection();
    DefaultTableModel dm;
    int id;
    double walletbalance;
    double refund;

    public FrameRevolvingFund() {
        initComponents();
        RetrieveChartofAccount();
        RetrieveCompany();
        Refresh();
        setIconImage();
    }

    private void setIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/icon-60px.png")));
    }

    private void Refresh() {
        RetrieveData();
        RetrieveWallet();
        CodeGenerator();
        btsave.setText("Save");
        txtdes.setText("");
        txtamount.setText("");
        id = 0;
        walletbalance = 0;
        refund = 0;
    }

    private void RetrieveCompany() {
        cbcompany.removeAllItems();
        cbcompany.addItem("Select Company");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblcompany");
            while (rs.next()) {
                String chart = rs.getString("CompanyName");
                cbcompany.addItem(chart);
            }
            stmt.close();
        } catch (SQLException e) {
        }
    }

    private void RetrieveChartofAccount() {
        cbfundsource.removeAllItems();
        cbfundsource.addItem("Select Bank");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblchartofaccount WHERE ChartName LIKE '%CASH IN BANK%' ");
            while (rs.next()) {
                String chart = rs.getString("ChartName");
                cbfundsource.addItem(chart);
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
        } catch (SQLException e) {
        }
    }

    public void RetrieveData() {
        DefaultTableModel TableModel = (DefaultTableModel) table.getModel();
        while (TableModel.getRowCount() > 0) {
            TableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblrevolvingfund");
            while (rs.next()) {
                String rfcode = rs.getString("RFCode");
                String fundsource = rs.getString("FundSource");
                String code = rs.getString("Code");
                String description = rs.getString("Description");
                String amount = rs.getString("Amount");
                String company = rs.getString("Company");
                String status = rs.getString("Status");
                String dateupdated = rs.getString("Date");
                TableModel.addRow(new Object[]{rfcode, fundsource, code, description, amount, company, status, dateupdated});
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameRevolvingFund.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void RetrieveWallet() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblrfwallet");
            if (rs.next()) {
                double balance = rs.getDouble("Balance");
                double amount = Double.parseDouble(txtamount.getText());
                walletbalance = balance + amount;
                lblwallet.setText("₱" + getFormattedNumber(Double.toString(balance)));
            }
            stmt.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameRevolvingFund.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void SaveRevolvingFund() {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tblrevolvingfund VALUES(?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, lblcode.getText());
            stmt.setString(3, cbfundsource.getSelectedItem().toString());
            stmt.setString(4, txtcode.getText().toUpperCase());
            stmt.setString(5, txtdes.getText().toUpperCase());
            stmt.setString(6, txtamount.getText());
            stmt.setString(7, cbcompany.getSelectedItem().toString());
            stmt.setString(8, "OPEN");
            stmt.setString(9, cbdate.getText());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(this, "Revolving Fund '" + lblcode.getText() + "' has been created!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(FrameRevolvingFund.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void SaveJournal() {
        //Save Journal Entry Debit
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbljournal VALUES(?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, cbdate.getText());
            stmt.setString(3, lblcode.getText());
            stmt.setString(4, txtcode.getText());
            stmt.setString(5, "REVOLVING FUNDS");
            stmt.setString(6, txtamount.getText());
            stmt.setString(7, "0");
            stmt.setString(8, "OPEN");
            stmt.setString(9, cbcompany.getSelectedItem().toString());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameRevolvingFund.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Save Journal Entry Credit
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbljournal VALUES(?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, cbdate.getText());
            stmt.setString(3, lblcode.getText());
            stmt.setString(4, txtcode.getText());
            stmt.setString(5, cbfundsource.getSelectedItem().toString());
            stmt.setString(6, "0");
            stmt.setString(7, txtamount.getText());
            stmt.setString(8, "OPEN");
            stmt.setString(9, cbcompany.getSelectedItem().toString());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameRevolvingFund.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void UpdateWallet() {
        try (Statement stmt0 = connection.createStatement()) {
            ResultSet rs = stmt0.executeQuery("SELECT * FROM tblrfwallet WHERE Status = 'OPEN'");
            if (rs.next()) {
                double balance = rs.getDouble("Balance");
                double totalamount = Double.parseDouble(txtamount.getText());
                double newbalance = balance + totalamount;
                //update revolving fund balance
                try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblrfwallet SET Balance = ? WHERE Status = 'OPEN'")) {
                    stmt.setDouble(1, newbalance);
                    stmt.execute();
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //if no revolving fund exist
                JOptionPane.showMessageDialog(this, "No revolving fund exist.", " System Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
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
                wallet = balance - refund;
                //refund wallet
                try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblrfwallet SET Balance = ? WHERE Status = 'OPEN'")) {
                    stmt.setDouble(1, wallet);
                    stmt.execute();
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void UpdateJournal() {
        //update debit
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbljournal SET Code = ?, Account = ?, Debit = ?, Company = ? WHERE Reference = ? AND Credit = '0.00'")) {
            stmt.setString(1, txtcode.getText());
            stmt.setString(2, "REVOLVING FUNDS");
            stmt.setString(3, txtamount.getText());
            stmt.setString(4, cbcompany.getSelectedItem().toString());
            stmt.setString(5, lblcode.getText());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FrameRevolvingFund.class.getName()).log(Level.SEVERE, null, ex);
        }

        //update credit
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbljournal SET Code = ?, Account = ?, Credit = ?, Company = ? WHERE Reference = ? AND Debit = '0.00'")) {
            stmt.setString(1, txtcode.getText());
            stmt.setString(2, cbfundsource.getSelectedItem().toString());
            stmt.setString(3, txtamount.getText());
            stmt.setString(4, cbcompany.getSelectedItem().toString());
            stmt.setString(5, lblcode.getText());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FrameRevolvingFund.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void UpdateRevolvingFund() {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblrevolvingfund SET FundSource = ?, Code = ?,  Description = ?, Amount = ?, Company = ? WHERE RFCode = ?")) {
            stmt.setString(1, cbfundsource.getSelectedItem().toString());
            stmt.setString(2, txtcode.getText());
            stmt.setString(3, txtdes.getText().toUpperCase());
            stmt.setString(4, txtamount.getText().toUpperCase());
            stmt.setString(5, cbcompany.getSelectedItem().toString());
            stmt.setString(6, lblcode.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Revolving Fund '" + lblcode.getText() + "' has been updated!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(FrameRevolvingFund.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void DeleteRF() {
        int row = table.getSelectedRow();
        if (row > -1) {
            if (JOptionPane.showConfirmDialog(this, "Warning: This action can't be rollback! \n Are you sure you want to delete " + lblcode.getText() + "?  ", " System Question", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                String sid = table.getValueAt(row, 0).toString();
                String sqlc = "DELETE t1, t2 FROM tblrevolvingfund AS t1 INNER JOIN tbljournal AS t2 ON t1.RFCode = t2.Reference WHERE t1.RFCode = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlc)) {
                    stmt.setString(1, sid);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Revolving Fund " + lblcode.getText() + " has been deleted!", " System Information", JOptionPane.INFORMATION_MESSAGE);
                    RefundWallet();
                    Refresh();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameChartofAccount.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data.", " System Information", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String CodeGenerator() {
        String code = null;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Max(ID) FROM tblrevolvingfund");
            if (rs.next()) {
                code = rs.getString("Max(ID)");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (code == null) {
                String date = classes.DateFunction.getFormattedYearMonth();
                String dateout = date.replaceAll("-", "");
                String newcode = "RFC" + dateout + "1";
                code = newcode;
                lblcode.setText(code);

            } else {
                int temp = Integer.parseInt(code);
                temp += 1;
                String output = Integer.toString(temp);
                String date = classes.DateFunction.getFormattedYearMonth();
                String dateout = date.replaceAll("-", "");
                String newcode = "RFC" + dateout + output;
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
        txtdes = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtamount = new javax.swing.JTextField();
        cbfundsource = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        cbdate = new datechooser.beans.DateChooserCombo();
        jLabel6 = new javax.swing.JLabel();
        txtcode = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbcompany = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        btsave = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btdelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Extrace");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 52, 66));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REVOLVING FUND");

        lblcode.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lblcode.setForeground(new java.awt.Color(255, 153, 0));
        lblcode.setText("000000");

        lblwallet.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lblwallet.setForeground(new java.awt.Color(255, 153, 0));
        lblwallet.setText("₱0");

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblcode)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblwallet)
                        .addComponent(jLabel12)))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));

        jLabel3.setText("Fund Source");

        jLabel4.setText("Description");

        txtamount.setText("0");

        cbfundsource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbfundsourceActionPerformed(evt);
            }
        });

        jLabel5.setText("Amount");

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

    jLabel6.setText("Date");

    txtcode.setEditable(false);
    txtcode.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txtcodeActionPerformed(evt);
        }
    });

    jLabel9.setText("Code");

    cbcompany.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbcompanyActionPerformed(evt);
        }
    });

    jLabel7.setText("Charge To");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(306, 306, 306)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtcode)
                        .addComponent(cbdate, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel5)
                    .addGap(264, 264, 264)
                    .addComponent(jLabel6))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtamount, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cbfundsource, javax.swing.GroupLayout.Alignment.LEADING, 0, 300, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel9)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(txtdes)
                .addComponent(jLabel4)
                .addComponent(jLabel7)
                .addComponent(cbcompany, 0, 300, Short.MAX_VALUE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel9))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cbfundsource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(txtcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel4)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtdes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel5)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtamount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cbdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cbcompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(7, Short.MAX_VALUE))
    );

    jButton2.setBackground(new java.awt.Color(45, 52, 66));
    jButton2.setForeground(new java.awt.Color(255, 255, 255));
    jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_close_window_16px.png"))); // NOI18N
    jButton2.setText("Close");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
        }
    });

    btsave.setBackground(new java.awt.Color(45, 52, 66));
    btsave.setForeground(new java.awt.Color(255, 255, 255));
    btsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_save_16px.png"))); // NOI18N
    btsave.setText("Save");
    btsave.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btsaveActionPerformed(evt);
        }
    });

    table.setBackground(new java.awt.Color(107, 115, 131));
    table.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
    table.setForeground(new java.awt.Color(255, 255, 255));
    table.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "RFCode", "Fund Source", "Code", "Description", "Amount", "Company", "Status", "Date"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    table.setGridColor(new java.awt.Color(204, 204, 204));
    table.setSelectionBackground(new java.awt.Color(45, 52, 66));
    table.setSelectionForeground(new java.awt.Color(235, 235, 236));
    table.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tableMouseClicked(evt);
        }
    });
    jScrollPane2.setViewportView(table);

    btdelete.setBackground(new java.awt.Color(45, 52, 66));
    btdelete.setForeground(new java.awt.Color(255, 255, 255));
    btdelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_waste_16px.png"))); // NOI18N
    btdelete.setText("Delete");
    btdelete.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btdeleteActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btsave, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btdelete, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(7, 7, 7)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addComponent(jScrollPane2))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton2)
                .addComponent(btsave)
                .addComponent(btdelete))
            .addContainerGap())
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsaveActionPerformed
        if ("Save".equals(btsave.getText())) {
            if (!"".equals(txtdes.getText())) {
                RetrieveWallet();
                SaveRevolvingFund();
                SaveJournal();
                UpdateWallet();
                Refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill the required fields.", " System Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            if (!"".equals(txtdes.getText())) {
                RefundWallet();
                UpdateRevolvingFund();
                UpdateJournal();
                UpdateWallet();
                Refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill the required fields.", " System Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btsaveActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cbfundsourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbfundsourceActionPerformed
        RetrieveCode();
    }//GEN-LAST:event_cbfundsourceActionPerformed

    private void cbcompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbcompanyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbcompanyActionPerformed

    private void txtcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcodeActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        btsave.setText("Update");
        int row = table.getSelectedRow();
        lblcode.setText(table.getValueAt(row, 0).toString());
        cbfundsource.setSelectedItem(table.getValueAt(row, 1).toString());
        txtcode.setText(table.getValueAt(row, 2).toString());
        txtdes.setText(table.getValueAt(row, 3).toString());
        txtamount.setText(table.getValueAt(row, 4).toString());
        cbdate.setText(table.getValueAt(row, 7).toString());
        cbcompany.setSelectedItem(table.getValueAt(row, 5).toString());
        refund = Double.parseDouble(table.getValueAt(row, 4).toString());
    }//GEN-LAST:event_tableMouseClicked

    private void btdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeleteActionPerformed
        String pwallet = lblwallet.getText().replace("₱", "");
        String cwallet = pwallet.replace(",", "");
        double wallet = Double.parseDouble(cwallet);
        double amount = Double.parseDouble(txtamount.getText());
        if (wallet >= amount) {
            DeleteRF();
        } else {
            JOptionPane.showMessageDialog(this, "Revolving Fund " + lblcode.getText() + " is already been used.", " System Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btdeleteActionPerformed

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
            java.util.logging.Logger.getLogger(FrameRevolvingFund.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameRevolvingFund.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameRevolvingFund.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameRevolvingFund.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new FrameRevolvingFund().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btdelete;
    private javax.swing.JButton btsave;
    private javax.swing.JComboBox cbcompany;
    private datechooser.beans.DateChooserCombo cbdate;
    private javax.swing.JComboBox cbfundsource;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JLabel lblcode;
    private static javax.swing.JLabel lblwallet;
    private static javax.swing.JTable table;
    private javax.swing.JTextField txtamount;
    private javax.swing.JTextField txtcode;
    private javax.swing.JTextField txtdes;
    // End of variables declaration//GEN-END:variables
}
