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
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class FrameMerchandiseReceipt extends javax.swing.JFrame {

    DatabaseConnection conn = new DatabaseConnection();
    DefaultTableModel tablemodel;
    Connection connection = conn.getConnection();
    DefaultTableModel dm;
    String user;

    public FrameMerchandiseReceipt() {
        initComponents();
        RetrieveUnit();
        RetrieveSupplier();
        RetrieveChart();
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
        RetrieveData();
        btsave.setText("Save");
        btclose.setText("Close");
        spquantity.setValue(0);
        txtunitcost.setText("0.00");
        txttotal.setText("0.00");
        txtreceive.setText("");

    }

    void RetrieveData() {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisereceipt");
            while (rs.next()) {
                String code = rs.getString("MerchReceiptCode");
                String acc = rs.getString("Account");
                String supplier = rs.getString("Supplier");
                String quantity = rs.getString("Quantity");
                String unit = rs.getString("Unit");
                String unitcost = rs.getString("UnitCost");
                String totalamount = rs.getString("TotalAmount");
                String requestedby = rs.getString("RequestedBy");
                String verifiedby = rs.getString("VerifiedBy");
                String approvedby = rs.getString("ApprovedBy");
                String receivedby = rs.getString("ReceivedBy");
                String status = rs.getString("Status");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, acc, supplier, quantity, unit, unitcost, totalamount, requestedby, verifiedby, approvedby, receivedby, status, date});
            }
        } catch (SQLException e) {
        }
    }

    private void Search(String query) {
        dm = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);
        table.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    private void RetrieveSupplier() {
        cbsupplier.addItem("Select Supplier");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblsupplier");
            while (rs.next()) {
                String chart = rs.getString("SupplierName");
                cbsupplier.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveChart() {
        cbchart.addItem("Select Account");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblchartofaccount");
            while (rs.next()) {
                String chart = rs.getString("ChartName");
                cbchart.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveUnit() {
        cbunit.addItem("Select Unit");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblunit");
            while (rs.next()) {
                String chart = rs.getString("Unit");
                cbunit.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    private void Save() {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tblmerchandisereceipt VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, lblcode.getText());
            stmt.setString(3, cbchart.getSelectedItem().toString());
            stmt.setString(4, cbsupplier.getSelectedItem().toString());
            stmt.setString(5, spquantity.getValue().toString());
            stmt.setString(6, cbunit.getSelectedItem().toString());
            stmt.setString(7, txtunitcost.getText());
            stmt.setString(8, txttotal.getText());
            stmt.setString(9, user);
            stmt.setString(10, "NOT-VERIFIED");
            stmt.setString(11, "NOT-APPROVED");
            stmt.setString(12, txtreceive.getText().toUpperCase());
            stmt.setString(13, "PENDING");
            stmt.setString(14, DateFunction.getFormattedDate());
            stmt.execute();
            stmt.close();
            Refresh();
            JOptionPane.showMessageDialog(this, "Merchandise Receipt '" + lblcode.getText() + "' has been saved!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(FrameMerchandiseReceipt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Update() {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblmerchandisereceipt SET Account = ?, Supplier = ?, Quantity = ?, Unit = ?, UnitCost = ?, TotalAmount = ?, ReceivedBy = ? WHERE MerchReceiptCode = ?")) {
            stmt.setString(8, lblcode.getText());
            stmt.setString(1, cbchart.getSelectedItem().toString());
            stmt.setString(2, cbsupplier.getSelectedItem().toString());
            stmt.setInt(3, Integer.parseInt(spquantity.getValue().toString()));
            stmt.setString(4, cbunit.getSelectedItem().toString());
            stmt.setString(5, txtunitcost.getText());
            stmt.setString(6, txttotal.getText());
            stmt.setString(7, txtreceive.getText());
            stmt.executeUpdate();
            Refresh();
            JOptionPane.showMessageDialog(this, "Merchandise Receipt '" + lblcode.getText() + "' has been updated!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(FrameMerchandiseReceipt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Delete() {
        int row = table.getSelectedRow();
        if (row > -1) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this data?", " System Information", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                String sid = table.getValueAt(row, 0).toString();
                String sqlc = "DELETE FROM tblmerchandisereceipt WHERE MerchReceiptCode = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlc)) {
                    stmt.setString(1, sid);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Merchandise Receipt '" + lblcode.getText() + "' has been deleted!", " System Information", JOptionPane.INFORMATION_MESSAGE);
                    Refresh();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameMerchandiseReceipt.class.getName()).log(Level.SEVERE, null, ex);
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
            ResultSet rs = stmt.executeQuery("SELECT Max(ID) FROM tblmerchandisereceipt");
            if (rs.next()) {
                code = rs.getString("Max(ID)");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (code == null) {
                String date = lozadagroupcompany.DateFunction.getFormattedYearMonth();
                String dateout = date.replaceAll("-", "");
                String newcode = "MRC" + dateout + "1";
                code = newcode;
                lblcode.setText(code);

            } else {
                int temp = Integer.parseInt(code);
                temp += 1;
                String output = Integer.toString(temp);
                String date = lozadagroupcompany.DateFunction.getFormattedYearMonth();
                String dateout = date.replaceAll("-", "");
                String newcode = "MRC" + dateout + output;
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
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtunitcost = new javax.swing.JTextField();
        cbsupplier = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        cbunit = new javax.swing.JComboBox();
        spquantity = new javax.swing.JSpinner();
        txtreceive = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cbchart = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btsave = new javax.swing.JButton();
        btclose = new javax.swing.JButton();
        txtsearch = new javax.swing.JTextField();
        btdelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 52, 66));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MERCHANDISE RECEIPT CODE:");

        lblcode.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lblcode.setForeground(new java.awt.Color(255, 153, 0));
        lblcode.setText("000000");

        jLabel6.setForeground(new java.awt.Color(255, 153, 0));
        jLabel6.setText("FUEL");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblcode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblcode)
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));

        jLabel2.setText("Supplier");

        jLabel3.setText("Quantity");

        jLabel4.setText("Unit");

        jLabel5.setText("Unit Cost");

        txtunitcost.setText("0.00");

        jLabel7.setText("Total Amount");

        spquantity.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spquantityStateChanged(evt);
            }
        });
        spquantity.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                spquantityInputMethodTextChanged(evt);
            }
        });

        jLabel8.setText("Receive By");

        jLabel12.setText("Account");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(cbsupplier, 0, 227, Short.MAX_VALUE)
                    .addComponent(jLabel12)
                    .addComponent(cbchart, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbunit, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(spquantity))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtunitcost, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 6, Short.MAX_VALUE))
                            .addComponent(txtreceive))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbunit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtunitcost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtreceive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(spquantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbchart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.setBackground(new java.awt.Color(107, 115, 131));
        table.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        table.setForeground(new java.awt.Color(255, 255, 255));
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MR Code", "Account", "Supplier", "Quantity", "Unit", "Unit Cost", "Total Amount", "Requested By", "Verified By", "Approved By", "Received By", "Status", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table.setGridColor(new java.awt.Color(204, 204, 204));
        table.setSelectionBackground(new java.awt.Color(45, 52, 66));
        table.setSelectionForeground(new java.awt.Color(235, 235, 236));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(200);
            table.getColumnModel().getColumn(5).setPreferredWidth(100);
            table.getColumnModel().getColumn(6).setPreferredWidth(150);
            table.getColumnModel().getColumn(7).setPreferredWidth(200);
            table.getColumnModel().getColumn(8).setPreferredWidth(200);
            table.getColumnModel().getColumn(9).setPreferredWidth(200);
            table.getColumnModel().getColumn(10).setPreferredWidth(200);
            table.getColumnModel().getColumn(11).setPreferredWidth(100);
        }

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

        txtsearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsearch.setText("Search...");
        txtsearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsearchKeyReleased(evt);
            }
        });

        btdelete.setBackground(new java.awt.Color(45, 52, 66));
        btdelete.setForeground(new java.awt.Color(255, 255, 255));
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btclose, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addComponent(txtsearch)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btclose)
                    .addComponent(btsave)
                    .addComponent(btdelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsaveActionPerformed
        if ("Save".equals(btsave.getText())) {
            if (!"Select Supplier".equals(cbsupplier.getSelectedItem()) && !"0.00".equals(txtunitcost.getText()) && !"Select Unit".equals(cbunit.getSelectedItem())) {
                Save();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill the required fields.", " System Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            if (!"Select Supplier".equals(cbsupplier.getSelectedItem()) && !"0.00".equals(txtunitcost.getText()) && !"Select Unit".equals(cbunit.getSelectedItem())) {
                Update();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill the required fields.", " System Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btsaveActionPerformed

    private void spquantityInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_spquantityInputMethodTextChanged

    }//GEN-LAST:event_spquantityInputMethodTextChanged

    private void spquantityStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spquantityStateChanged
        double unitcost = Double.parseDouble(txtunitcost.getText());
        double quantity = Double.parseDouble(spquantity.getValue().toString());
        double total = unitcost * quantity;
        txttotal.setText(Double.toString(total));
    }//GEN-LAST:event_spquantityStateChanged

    private void btdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeleteActionPerformed
        Delete();
    }//GEN-LAST:event_btdeleteActionPerformed

    private void txtsearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsearchKeyReleased
        Search(txtsearch.getText());
    }//GEN-LAST:event_txtsearchKeyReleased

    private void btcloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btcloseActionPerformed
        if ("Cancel".equals(btclose.getText())) {
            Refresh();
        } else {
            dispose();
        }
    }//GEN-LAST:event_btcloseActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        btsave.setText("Update");
        btclose.setText("Cancel");
        int row = table.getSelectedRow();
        lblcode.setText(table.getValueAt(row, 0).toString());
        cbchart.setSelectedItem(table.getValueAt(row, 1).toString());
        cbsupplier.setSelectedItem(table.getValueAt(row, 2).toString());
        spquantity.setValue(Integer.parseInt(table.getValueAt(row, 3).toString()));
        cbunit.setSelectedItem(table.getValueAt(row, 4).toString());
        txtunitcost.setText(table.getValueAt(row, 5).toString());
        txttotal.setText(table.getValueAt(row, 6).toString());
        txtreceive.setText(table.getValueAt(row, 10).toString());
    }//GEN-LAST:event_tableMouseClicked

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
            java.util.logging.Logger.getLogger(FrameMerchandiseReceipt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameMerchandiseReceipt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameMerchandiseReceipt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameMerchandiseReceipt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
            new FrameMerchandiseReceipt().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btclose;
    private javax.swing.JButton btdelete;
    private javax.swing.JButton btsave;
    private javax.swing.JComboBox cbchart;
    private javax.swing.JComboBox cbsupplier;
    private javax.swing.JComboBox cbunit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblcode;
    private javax.swing.JSpinner spquantity;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtreceive;
    private javax.swing.JTextField txtsearch;
    private javax.swing.JTextField txttotal;
    private javax.swing.JTextField txtunitcost;
    // End of variables declaration//GEN-END:variables
}
