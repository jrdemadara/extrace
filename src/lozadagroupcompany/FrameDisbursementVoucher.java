package lozadagroupcompany;

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
    String user = "";

    public FrameDisbursementVoucher() {
        initComponents();
        Refresh();
    }

    private void Refresh() {
        CodeGenerator();
        RetrieveChart();
        RetrieveSupplier();
        txtdescription.setText("");
        txtparticular.setText("");
        txtgrossamount.setText("0.00");
        txtvat.setText("0.00");
        txtnetvat.setText("0.00");
        cbpayee.setSelectedIndex(0);
        cbfundsource.setSelectedIndex(0);
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

    public static void ModifyQuantity(String quantity) {
        int row = table.getSelectedRow();
        table.setValueAt(quantity, row, 2);
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
            double quantity = Double.parseDouble(table.getValueAt(i, 2).toString());
            double unitcost = Double.parseDouble(table.getValueAt(i, 4).toString());
            double totalamount = Math.ceil(unitcost * quantity);
            double vat = Math.ceil((totalamount / 1.12) * 0.12);
            double netvat = Math.ceil(totalamount / 1.12);
            table.setValueAt(totalamount, i, 5);
            table.setValueAt(vat, i, 6);
            table.setValueAt(netvat, i, 7);
            grandtotal += totalamount;
            totalvat += vat;
            totalnetvat += netvat;
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
            String gross = table.getValueAt(i, 4).toString();
            String vat = table.getValueAt(i, 5).toString();
            String netvat = table.getValueAt(i, 6).toString();
            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbldisbursementvoucherparticular VALUES(?,?,?,?,?,?,?,?,?)")) {
                stmt.setInt(1, 0);
                stmt.setString(2, lblcode.getText());
                stmt.setString(3, code);
                stmt.setString(4, particular);
                stmt.setString(5, quantity);
                stmt.setString(6, unit);
                stmt.setString(7, gross);
                stmt.setString(8, vat);
                stmt.setString(9, netvat);
                stmt.execute();
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(FrameDisbursementVoucher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tbldisbursementvoucher VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, lblcode.getText());
            stmt.setString(3, cbpayee.getSelectedItem().toString());
            stmt.setString(4, txtdescription.getText());
            stmt.setString(5, txtparticular.getText());
            stmt.setString(6, txtgrossamount.getText());
            stmt.setString(7, txtvat.getText());
            stmt.setString(8, txtnetvat.getText());
            stmt.setString(9, cbfundsource.getSelectedItem().toString());
            stmt.setString(10, user);
            stmt.setString(11, "");
            stmt.setString(12, "");
            stmt.setString(13, "REQUESTED");
            stmt.setString(14, DateFunction.getFormattedDate());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(this, "Disbursement Voucher '" + lblcode.getText() + "' has been saved!", " System Information", JOptionPane.INFORMATION_MESSAGE);
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
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jPopupMenu1.setInvoker(table);

        jMenuItem1.setText("Modify Quantity");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Remove Item");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txtnetvat, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(199, 199, 199))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(cbpayee, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtdescription, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5)
                                    .addComponent(txtparticular, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(txtgrossamount, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8)
                            .addComponent(txtvat)
                            .addComponent(cbfundsource, 0, 206, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtdescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbfundsource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbpayee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtparticular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtgrossamount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtvat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtnetvat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Particular", "Quantity", "Unit", "Unit Cost", "Total Amount", "VAT", "Net VAT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(table);

        jButton1.setBackground(new java.awt.Color(45, 52, 66));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(107, 115, 131));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 153, 0));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Add Item");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 0, 0));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Remove Item");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(107, 115, 131));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("View List");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
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
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (!"Select Payee".equals(cbpayee.getSelectedItem()) && !"".equals(txtdescription.getText()) && !"Select Fund".equals(cbfundsource.getSelectedItem())) {
            Save();
            Refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill the required fields.", " System Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

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
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        FrameDVSettle frame = new FrameDVSettle();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
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
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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

    }//GEN-LAST:event_cbpayeeActionPerformed

    private void cbfundsourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbfundsourceActionPerformed

    }//GEN-LAST:event_cbfundsourceActionPerformed

    private void txtdescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdescriptionActionPerformed

    }//GEN-LAST:event_txtdescriptionActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

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
    private javax.swing.JComboBox cbfundsource;
    private javax.swing.JComboBox cbpayee;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblcode;
    private static javax.swing.JTable table;
    private javax.swing.JTextField txtdescription;
    private static javax.swing.JTextField txtgrossamount;
    private static javax.swing.JTextField txtnetvat;
    private static javax.swing.JTextField txtparticular;
    private static javax.swing.JTextField txtvat;
    // End of variables declaration//GEN-END:variables
}
