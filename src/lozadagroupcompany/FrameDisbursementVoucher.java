package lozadagroupcompany;

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

    public FrameDisbursementVoucher() {
        initComponents();
        Refresh();
        setIconImage();
    }

    public void GetUser(String name) {
        user = name;
    }

    private void setIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("ico.png")));
    }

    private void Refresh() {
        CodeGenerator();
        RetrieveChart();
        RetrieveSupplier();
        RetrieveCompany();
        btsave.setText("Save");
        btclose.setText("Close");
        txtdescription.setText("");
        txtreceive.setText("");
        txtparticular.setText("");
        txtgrossamount.setText("0.00");
        txtvat.setText("0.00");
        txtnetvat.setText("0.00");
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
        CountParticular();
    }

    public static void LoadDV(String code, String payee, String description, String fundsource, String particular, String grossamount, String vat, String netvat, String received) {
        lblcode.setText(code);
        cbpayee.setSelectedItem(payee);
        txtdescription.setText(description);
        cbfundsource.setSelectedItem(fundsource);
        txtparticular.setText(particular);
        txtgrossamount.setText(grossamount);
        txtvat.setText(vat);
        txtnetvat.setText(netvat);
        txtreceive.setText(received);
        btsave.setText("Update");
        btclose.setText("Cancel");
    }

    public static void LoadDVParticular(Object[] dataRow) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(dataRow);
    }

    public static void ModifyQuantity(String unitcost, String quantity) {
        int row = table.getSelectedRow();
        table.setValueAt(unitcost, row, 4);
        table.setValueAt(quantity, row, 2);
    }

    public static void ModifyVATType(String vattype) {
        int row = table.getSelectedRow();
        table.setValueAt(vattype, row, 6);
        if ("Non-VAT".equals(vattype)) {
            table.setValueAt("0", row, 7);
            table.setValueAt("0", row, 8);
            CalculateParticular();
        }
    }

    private static void CountParticular() {
        int count = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            count++;
        }
        txtparticular.setText(Integer.toString(count));
    }

    public static void CalculateParticular() {
        double grandtotal = 0;
        double totalvat = 0;
        double totalnetvat = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            String vattype = table.getValueAt(i, 6).toString();
            if ("VAT".equals(vattype)) {
                double quantity = Double.parseDouble(table.getValueAt(i, 2).toString());
                double unitcost = Double.parseDouble(table.getValueAt(i, 4).toString());
                double totalamount = (unitcost * quantity);
                double vat = ((totalamount / 1.12) * 0.12);
                double netvat = (totalamount / 1.12);
                table.setValueAt(totalamount, i, 5);
                table.setValueAt(vat, i, 7);
                table.setValueAt(netvat, i, 8);
                grandtotal += totalamount;
                totalvat += vat;
                totalnetvat += netvat;
            } else {
                double quantity = Double.parseDouble(table.getValueAt(i, 2).toString());
                double unitcost = Double.parseDouble(table.getValueAt(i, 4).toString());
                double totalamount = (unitcost * quantity);
                table.setValueAt(totalamount, i, 5);
                table.setValueAt("0", i, 7);
                table.setValueAt("0", i, 8);
                grandtotal += totalamount;
            }

        }
        txtgrossamount.setText(Double.toString(grandtotal));
        txtvat.setText(Double.toString(totalvat));
        txtnetvat.setText(Double.toString(totalnetvat));
        CountParticular();
    }

    private void Save() {
        for (int i = 0; i < table.getRowCount(); i++) {
            String code = table.getValueAt(i, 0).toString();
            String particular = table.getValueAt(i, 1).toString();
            String quantity = table.getValueAt(i, 2).toString();
            String unit = table.getValueAt(i, 3).toString();
            String unitcost = table.getValueAt(i, 4).toString();
            String gross = table.getValueAt(i, 5).toString();
            String pvattype = table.getValueAt(i, 6).toString();
            String vat = table.getValueAt(i, 7).toString();
            String netvat = table.getValueAt(i, 8).toString();
            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbldisbursementvoucherparticular VALUES(?,?,?,?,?,?,?,?,?,?,?)")) {
                stmt.setInt(1, 0);
                stmt.setString(2, lblcode.getText());
                stmt.setString(3, code);
                stmt.setString(4, particular);
                stmt.setString(5, quantity);
                stmt.setString(6, unit);
                stmt.setString(7, unitcost);
                stmt.setString(8, gross);
                stmt.setString(9, pvattype);
                stmt.setString(10, vat);
                stmt.setString(11, netvat);
                stmt.execute();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbldisbursementvoucher VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, lblcode.getText());
            stmt.setString(3, cbchargeto.getSelectedItem().toString());
            stmt.setString(4, cbpayee.getSelectedItem().toString());
            stmt.setString(5, txttin.getText().toUpperCase());
            stmt.setString(6, txtdescription.getText().toUpperCase());
            stmt.setString(7, txtparticular.getText());
            stmt.setString(8, txtgrossamount.getText());
            stmt.setString(9, txtvat.getText());
            stmt.setString(10, txtnetvat.getText());
            stmt.setString(11, cbfundsource.getSelectedItem().toString());
            stmt.setString(12, user);
            stmt.setString(13, "NOT-VERIFIED");
            stmt.setString(14, "NOT-APPROVED");
            stmt.setString(15, txtreceive.getText());
            stmt.setString(16, "PENDING");
            stmt.setString(17, DateFunction.getFormattedDate());
            stmt.execute();
            stmt.close();
            Refresh();
            JOptionPane.showMessageDialog(this, "Disbursement Voucher '" + lblcode.getText() + "' has been saved!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Update() {
        for (int i = 0; i < table.getRowCount(); i++) {
            String code = table.getValueAt(i, 0).toString();
            String particular = table.getValueAt(i, 1).toString();
            String quantity = table.getValueAt(i, 2).toString();
            String unit = table.getValueAt(i, 3).toString();
            String unitcost = table.getValueAt(i, 4).toString();
            String gross = table.getValueAt(i, 5).toString();
            String vat = table.getValueAt(i, 7).toString();
            String netvat = table.getValueAt(i, 8).toString();
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbldisbursementvoucherparticular SET Code = ?, Particular = ?, Quantity = ?, Unit = ?, UnitCost = ?, PGrossAmount = ?, PVAT = ?, PNetVAT = ? WHERE DisbursementCode = ? AND Particular = ?")) {
                stmt.setString(1, code);
                stmt.setString(2, particular);
                stmt.setString(3, quantity);
                stmt.setString(4, unit);
                stmt.setString(5, unitcost);
                stmt.setString(6, gross);
                stmt.setString(7, vat);
                stmt.setString(8, netvat);
                stmt.setString(9, lblcode.getText());
                stmt.setString(10, particular);
                stmt.executeUpdate();
                Refresh();
            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbldisbursementvoucher SET Payee = ?, Description = ?, Particulars = ?, GrossAmount = ?, VAT = ?, NetVAT = ?, FundSource = ?, ReceivedBy = ? WHERE DisbursementCode = ?")) {
            stmt.setString(1, cbpayee.getSelectedItem().toString());
            stmt.setString(2, txtdescription.getText().toUpperCase());
            stmt.setString(3, txtparticular.getText());
            stmt.setString(4, txtgrossamount.getText());
            stmt.setString(5, txtvat.getText());
            stmt.setString(6, txtnetvat.getText());
            stmt.setString(7, cbfundsource.getSelectedItem().toString());
            stmt.setString(8, txtreceive.getText());
            stmt.setString(9, lblcode.getText());
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
                String date = lozadagroupcompany.DateFunction.getFormattedYearMonth();
                String dateout = date.replaceAll("-", "");
                String newcode = "DVC" + dateout + "1";
                code = newcode;
                lblcode.setText(code);

            } else {
                int temp = Integer.parseInt(code);
                temp += 1;
                String output = Integer.toString(temp);
                String date = lozadagroupcompany.DateFunction.getFormattedYearMonth();
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        modifyquantity = new javax.swing.JMenuItem();
        vattype = new javax.swing.JMenuItem();
        remove = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblcode = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtdescription = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtparticular = new javax.swing.JTextField();
        cbfundsource = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        txtgrossamount = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtvat = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtnetvat = new javax.swing.JTextField();
        cbpayee = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cbchargeto = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        txttin = new javax.swing.JTextField();
        txtreceive = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btsave = new javax.swing.JButton();
        btclose = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        jPopupMenu1.setInvoker(table);

        modifyquantity.setText("Modify Quantity");
        modifyquantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyquantityActionPerformed(evt);
            }
        });
        jPopupMenu1.add(modifyquantity);

        vattype.setText("Modify VAT Type");
        vattype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vattypeActionPerformed(evt);
            }
        });
        jPopupMenu1.add(vattype);

        remove.setText("Remove Item");
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });
        jPopupMenu1.add(remove);

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

        txtdescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdescriptionActionPerformed(evt);
            }
        });

        jLabel3.setText("Description");

        jLabel4.setText("Fund Source");

        jLabel5.setText("Particular");

        txtparticular.setEditable(false);
        txtparticular.setBackground(new java.awt.Color(238, 238, 238));

        cbfundsource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbfundsourceActionPerformed(evt);
            }
        });

        jLabel7.setText("Gross Amount");

        txtgrossamount.setEditable(false);
        txtgrossamount.setBackground(new java.awt.Color(238, 238, 238));
        txtgrossamount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtgrossamountKeyReleased(evt);
            }
        });

        jLabel8.setText("VAT");

        txtvat.setEditable(false);
        txtvat.setBackground(new java.awt.Color(238, 238, 238));

        jLabel9.setText("Net VAT");

        txtnetvat.setEditable(false);
        txtnetvat.setBackground(new java.awt.Color(238, 238, 238));

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

        txtreceive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtreceiveActionPerformed(evt);
            }
        });

        jLabel11.setText("Receive By");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 109, Short.MAX_VALUE))
                            .addComponent(txtparticular))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtgrossamount, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtvat, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnetvat, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(cbchargeto, 0, 227, Short.MAX_VALUE)
                            .addComponent(cbfundsource, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtdescription, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cbpayee, javax.swing.GroupLayout.Alignment.LEADING, 0, 247, Short.MAX_VALUE))
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(txttin, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(txtreceive))
                                .addContainerGap())))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(38, 38, 38)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbfundsource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtdescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtreceive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtparticular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))
                        .addGap(34, 34, 34))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtnetvat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtvat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtgrossamount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jButton3.setText("Add Item");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(107, 115, 131));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Remove Item");
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
                "Code", "Item", "Quantity", "Unit", "Unit Cost", "Total Amount", "VAT Type", "VAT", "Net VAT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setComponentPopupMenu(jPopupMenu1);
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btsave, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btclose, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
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
            if (!"Select Payee".equals(cbpayee.getSelectedItem()) && !"".equals(txtdescription.getText()) && !"Select Fund".equals(cbfundsource.getSelectedItem())) {
                Save();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill the required fields.", " System Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            if (!"Select Payee".equals(cbpayee.getSelectedItem()) && !"".equals(txtdescription.getText()) && !"Select Fund".equals(cbfundsource.getSelectedItem())) {
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

    private void txtgrossamountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtgrossamountKeyReleased
        double x;
        try {
            x = Double.parseDouble(txtgrossamount.getText());
        } catch (NumberFormatException nfe) {
            txtgrossamount.setText("0.00");
            txtgrossamount.selectAll();
        }
    }//GEN-LAST:event_txtgrossamountKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        FrameItemList frame = new FrameItemList();
        frame.setVisible(true);
        frame.SetImport("dv");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void modifyquantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyquantityActionPerformed
        int row = table.getSelectedRow();
        String unitcost = table.getValueAt(row, 4).toString();
        String quantity = table.getValueAt(row, 2).toString();
        FrameModifyParticular frame = new FrameModifyParticular();
        frame.setVisible(true);
        frame.SetDetails(unitcost, quantity);
        frame.SetType("DV");
    }//GEN-LAST:event_modifyquantityActionPerformed

    private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
        int row = table.getSelectedRow();
        String item = table.getValueAt(row, 1).toString();
        if (row > -1) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove Item '" + item + "'?", " System Information", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                tableModel.removeRow(row);
                CalculateParticular();
            }
        }
    }//GEN-LAST:event_removeActionPerformed

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

    }//GEN-LAST:event_cbfundsourceActionPerformed

    private void txtdescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdescriptionActionPerformed

    }//GEN-LAST:event_txtdescriptionActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        FrameDVList frame = new FrameDVList();
        frame.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void vattypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vattypeActionPerformed
        FrameModifyVATType frame = new FrameModifyVATType();
        frame.setVisible(true);
        frame.SetType("DV");
    }//GEN-LAST:event_vattypeActionPerformed

    private void cbchargetoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbchargetoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbchargetoActionPerformed

    private void txttinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttinKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txttinKeyReleased

    private void txtreceiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtreceiveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtreceiveActionPerformed

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
    private static javax.swing.JComboBox cbfundsource;
    private static javax.swing.JComboBox cbpayee;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JLabel lblcode;
    private javax.swing.JMenuItem modifyquantity;
    private javax.swing.JMenuItem remove;
    private static javax.swing.JTable table;
    private static javax.swing.JTextField txtdescription;
    private static javax.swing.JTextField txtgrossamount;
    private static javax.swing.JTextField txtnetvat;
    private static javax.swing.JTextField txtparticular;
    private static javax.swing.JTextField txtreceive;
    private static javax.swing.JTextField txttin;
    private static javax.swing.JTextField txtvat;
    private javax.swing.JMenuItem vattype;
    // End of variables declaration//GEN-END:variables
}
