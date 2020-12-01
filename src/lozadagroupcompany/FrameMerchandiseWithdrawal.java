/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Administrator
 */
public class FrameMerchandiseWithdrawal extends javax.swing.JFrame {

    /**
     * Creates new form FrameUnit
     */
    DatabaseConnection conn = new DatabaseConnection();
    DefaultTableModel tablemodel;
    Connection connection = conn.getConnection();
    DefaultTableModel dm;
    String user;

    public FrameMerchandiseWithdrawal() {
        initComponents();
        Refresh();
    }

    private void Refresh() {
        CodeGenerator();
        RetrieveUnit();
        retrieveData();
        spquantity.setValue(0.00);
        txtunitcost.setText("0.00");
        txttotal.setText("0.00");
        txtno.setText("");
        txtpurpose.setText("");
        txtdestination.setText("");
        cbunit.setSelectedIndex(0);
    }

    void retrieveData() {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisewithdrawal");
            while (rs.next()) {
                String code = rs.getString("MerchWithdrawalCode");
                String no = rs.getString("BusinessNo");
                String purpose = rs.getString("Purpose");
                String destination = rs.getString("Destination");
                String quantity = rs.getString("Quantity");
                String unit = rs.getString("Unit");
                String unitcost = rs.getString("UnitCost");
                String totalamount = rs.getString("TotalAmount");
                String req = rs.getString("RequestedBy");
                String approved = rs.getString("ApprovedBy");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, no, purpose, destination, quantity, unit, unitcost, totalamount, req, approved, date});
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
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tblmerchandisewithdrawal VALUES(?,?,?,?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, lblcode.getText());
            stmt.setString(3, txtno.getText());
            stmt.setString(4, txtpurpose.getText());
            stmt.setString(5, txtdestination.getText());
            stmt.setString(6, spquantity.getValue().toString());
            stmt.setString(7, cbunit.getSelectedItem().toString());
            stmt.setString(8, txtunitcost.getText());
            stmt.setString(9, txttotal.getText());
            stmt.setString(10, user);
            stmt.setString(11, "");
            stmt.setString(12, DateFunction.getFormattedDate());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(this, "Merchandise Withdrawal '" + lblcode.getText() + "' has been saved!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(FrameMerchandiseWithdrawal.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private String CodeGenerator() {
        String code = null;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Max(ID) FROM tblmerchandisewithdrawal");
            if (rs.next()) {
                code = rs.getString("Max(ID)");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (code == null) {
                String date = lozadagroupcompany.DateFunction.getFormattedYearMonth();
                String dateout = date.replaceAll("-", "");
                String newcode = "MWC" + dateout + "1";
                code = newcode;
                lblcode.setText(code);

            } else {
                int temp = Integer.parseInt(code);
                temp += 1;
                String output = Integer.toString(temp);
                String date = lozadagroupcompany.DateFunction.getFormattedYearMonth();
                String dateout = date.replaceAll("-", "");
                String newcode = "MWC" + dateout + output;
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
        txtno = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtpurpose = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtunitcost = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        txtdestination = new javax.swing.JTextField();
        cbunit = new javax.swing.JComboBox();
        spquantity = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        jPanel1.setBackground(new java.awt.Color(45, 52, 66));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MERCHANDISE WITHDRAWAL CODE:");

        lblcode.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lblcode.setForeground(new java.awt.Color(255, 153, 0));
        lblcode.setText("MWC2020111");

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

        jLabel2.setText("Business No.");

        jLabel3.setText("Purpose");

        jLabel4.setText("Destination");

        jLabel7.setText("Unit");

        jLabel8.setText("Unit Cost");

        txtunitcost.setText("0.00");

        jLabel9.setText("Total Amount");

        txttotal.setEditable(false);

        spquantity.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spquantityStateChanged(evt);
            }
        });

        jLabel11.setText("Quantity");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(cbunit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtunitcost, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtno, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtpurpose, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8)
                                .addGap(202, 202, 202)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4)
                                .addComponent(txtdestination, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                                .addComponent(spquantity)))))
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
                    .addComponent(txtno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtpurpose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdestination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbunit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtunitcost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spquantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(34, 34, 34)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MerchWithdrawalCode", "BusinessNo", "Purpose", "Destination", "Quantity", "Unit", "Unit Cost", "Total Amount", "Requested By", "Approved By", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (!"Select Supplier".equals(txtno.getText()) && !"0.00".equals(txtunitcost.getText()) && !"Select Unit".equals(cbunit.getSelectedItem()) && !"".equals(txtpurpose.getText()) && !"".equals(txtdestination.getText())) {
            Save();
            Refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill the required fields.", " System Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void spquantityStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spquantityStateChanged
        double unitcost = Double.parseDouble(txtunitcost.getText());
        double quantity = Double.parseDouble(spquantity.getValue().toString());
        double total = unitcost * quantity;
        txttotal.setText(Double.toString(total));
    }//GEN-LAST:event_spquantityStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(FrameMerchandiseWithdrawal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameMerchandiseWithdrawal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameMerchandiseWithdrawal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameMerchandiseWithdrawal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
            new FrameMerchandiseWithdrawal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbunit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblcode;
    private javax.swing.JSpinner spquantity;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtdestination;
    private javax.swing.JTextField txtno;
    private javax.swing.JTextField txtpurpose;
    private javax.swing.JTextField txttotal;
    private javax.swing.JTextField txtunitcost;
    // End of variables declaration//GEN-END:variables
}
