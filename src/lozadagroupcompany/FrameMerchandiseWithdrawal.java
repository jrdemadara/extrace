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
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class FrameMerchandiseWithdrawal extends javax.swing.JFrame {

    DatabaseConnection conn = new DatabaseConnection();
    DefaultTableModel tablemodel;
    Connection connection = conn.getConnection();
    DefaultTableModel dm;
    String user;

    public FrameMerchandiseWithdrawal() {
        initComponents();
        RetrieveCompany();
        Refresh();
    }

    public void GetUser(String name) {
        user = name;
    }

    private void Refresh() {
        CodeGenerator();
        retrieveData();
        btsave.setText("Save");
        btclose.setText("Close");
        spquantity.setValue(0);
        txtunitcost.setText("0.00");
        txttotal.setText("0.00");
        txtpurpose.setText("");
        txtdestination.setText("");
        txtunitcost.setText("");
        txtunit.setText("");
        txtmrcode.setText("");
        txtreceive.setText("");
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
                String mrcode = rs.getString("MRCode");
                String no = rs.getString("ChargeTo");
                String purpose = rs.getString("Purpose");
                String destination = rs.getString("Destination");
                String quantity = rs.getString("Quantity");
                String unit = rs.getString("Unit");
                String unitcost = rs.getString("UnitCost");
                String totalamount = rs.getString("TotalAmount");
                String req = rs.getString("RequestedBy");
                String ver = rs.getString("VerifiedBy");
                String approved = rs.getString("ApprovedBy");
                String rec = rs.getString("ReceivedBy");
                String status = rs.getString("Status");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, mrcode, no, purpose, destination, quantity, unit, unitcost, totalamount, req, ver, approved, rec, status, date});
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

    public static void LoadMR(String mrcode, String unit, String unitcost, String quantity) {
        txtmrcode.setText(mrcode);
        txtunit.setText(unit);
        txtunitcost.setText(unitcost);

    }

    private void RetrieveCompany() {
        cbcompany.addItem("Select Company");
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblcompany");
            while (rs.next()) {
                String chart = rs.getString("CompanyName");
                cbcompany.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    private void Save() {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO tblmerchandisewithdrawal VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            stmt.setInt(1, 0);
            stmt.setString(2, lblcode.getText());
            stmt.setString(3, txtmrcode.getText());
            stmt.setString(4, cbcompany.getSelectedItem().toString());
            stmt.setString(5, txtpurpose.getText());
            stmt.setString(6, txtdestination.getText());
            stmt.setString(7, spquantity.getValue().toString());
            stmt.setString(8, txtunit.getText());
            stmt.setString(9, txtunitcost.getText());
            stmt.setString(10, txttotal.getText());
            stmt.setString(11, user);
            stmt.setString(12, "NOT-VERIFIED");
            stmt.setString(13, "NOT-APPROVED");
            stmt.setString(14, txtreceive.getText().toUpperCase());
            stmt.setString(15, "PENDING");
            stmt.setString(16, DateFunction.getFormattedDate());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(this, "Merchandise Withdrawal '" + lblcode.getText() + "' has been saved!", " System Information", JOptionPane.INFORMATION_MESSAGE);
            Refresh();
        } catch (SQLException ex) {
            Logger.getLogger(FrameMerchandiseWithdrawal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Update() {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblmerchandisewithdrawal SET MRCode = ?, ChargeTo = ?, Purpose = ?, Destination = ?, Quantity = ?, Unit = ?, UnitCost = ?, TotalAmount = ?, ReceivedBy = ? WHERE MerchWithdrawalCode = ?")) {
            stmt.setString(10, lblcode.getText());
            stmt.setString(1, txtmrcode.getText());
            stmt.setString(2, cbcompany.getSelectedItem().toString());
            stmt.setString(3, txtpurpose.getText());
            stmt.setString(4, txtdestination.getText());
            stmt.setInt(5, Integer.parseInt(spquantity.getValue().toString()));
            stmt.setString(6, txtunit.getText());
            stmt.setString(7, txtunitcost.getText());
            stmt.setString(8, txttotal.getText());
            stmt.setString(9, txtreceive.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Merchandise Withdrawal '" + lblcode.getText() + "' has been updated!", " System Information", JOptionPane.INFORMATION_MESSAGE);
            Refresh();
        } catch (SQLException ex) {
            Logger.getLogger(FrameMerchandiseWithdrawal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Delete() {
        int row = table.getSelectedRow();
        if (row > -1) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this data?", " System Information", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                String sid = table.getValueAt(row, 0).toString();
                String sqlc = "DELETE FROM tblmerchandisewithdrawal WHERE MerchWithdrawalCode = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlc)) {
                    stmt.setString(1, sid);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Merchandise Withdrawal '" + lblcode.getText() + "' has been deleted!", " System Information", JOptionPane.INFORMATION_MESSAGE);
                    Refresh();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameMerchandiseWithdrawal.class.getName()).log(Level.SEVERE, null, ex);
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
        spquantity = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        txtunit = new javax.swing.JTextField();
        txtmrcode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbcompany = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        txtreceive = new javax.swing.JTextField();
        btclose = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btsave = new javax.swing.JButton();
        btdelete = new javax.swing.JButton();
        txtsearch = new javax.swing.JTextField();
        btimport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);

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

        jLabel2.setText("Charge To");

        jLabel3.setText("Purpose");

        jLabel4.setText("Destination");

        jLabel7.setText("Unit");

        jLabel8.setText("Unit Cost");

        txtunitcost.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        txtunitcost.setForeground(new java.awt.Color(0, 204, 0));
        txtunitcost.setText("0.00");

        jLabel9.setText("Total Amount");

        txttotal.setEditable(false);
        txttotal.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        txttotal.setText("0.00");

        spquantity.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
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

        jLabel11.setText("Quantity");

        txtunit.setEditable(false);
        txtunit.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        txtunit.setForeground(new java.awt.Color(0, 204, 0));

        txtmrcode.setEditable(false);
        txtmrcode.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        txtmrcode.setForeground(new java.awt.Color(0, 204, 0));

        jLabel5.setText("MR Code");

        jLabel10.setText("Receive By");

        txtreceive.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        txtreceive.setForeground(new java.awt.Color(0, 204, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtdestination)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtmrcode))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7)
                            .addComponent(txtunit)
                            .addComponent(jLabel2)
                            .addComponent(cbcompany, 0, 247, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(spquantity, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel8))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtunitcost, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtpurpose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtreceive, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtpurpose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtunitcost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtreceive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(34, 34, 34))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtmrcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbcompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtunit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(34, 34, 34))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtdestination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spquantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        btclose.setBackground(new java.awt.Color(45, 52, 66));
        btclose.setForeground(new java.awt.Color(255, 255, 255));
        btclose.setText("Close");
        btclose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btcloseActionPerformed(evt);
            }
        });

        table.setBackground(new java.awt.Color(107, 115, 131));
        table.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        table.setForeground(new java.awt.Color(255, 255, 255));
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MW Code", "MR Code", "Charge To", "Purpose", "Destination", "Quantity", "Unit", "Unit Cost", "Total Amount", "Requested By", "Verified By", "Approved By", "Received By", "Status", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true, false, true, false, false
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
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table);

        btsave.setBackground(new java.awt.Color(45, 52, 66));
        btsave.setForeground(new java.awt.Color(255, 255, 255));
        btsave.setText("Save");
        btsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btsaveActionPerformed(evt);
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

        txtsearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsearch.setText("Search...");
        txtsearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsearchKeyReleased(evt);
            }
        });

        btimport.setBackground(new java.awt.Color(45, 52, 66));
        btimport.setForeground(new java.awt.Color(255, 255, 255));
        btimport.setText("Import MR");
        btimport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btimportActionPerformed(evt);
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
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btimport, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btsave, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btdelete, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btclose, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtsearch))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btclose)
                    .addComponent(btsave)
                    .addComponent(btdelete)
                    .addComponent(btimport))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void spquantityStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spquantityStateChanged
        double unitcost = Double.parseDouble(txtunitcost.getText());
        double quantity = Double.parseDouble(spquantity.getValue().toString());
        double total = unitcost * quantity;
        txttotal.setText(Double.toString(total));

    }//GEN-LAST:event_spquantityStateChanged

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
        txtmrcode.setText(table.getValueAt(row, 1).toString());
        cbcompany.setSelectedItem(table.getValueAt(row, 2).toString());
        txtpurpose.setText(table.getValueAt(row, 3).toString());
        txtdestination.setText(table.getValueAt(row, 4).toString());
        txtunit.setText(table.getValueAt(row, 6).toString());
        txtunitcost.setText(table.getValueAt(row, 7).toString());
        txttotal.setText(table.getValueAt(row, 8).toString());
        txtreceive.setText(table.getValueAt(row, 12).toString());
        if (!"".equals(txtmrcode.getText())) {
            spquantity.setValue(Integer.parseInt(table.getValueAt(row, 5).toString()));
        }
    }//GEN-LAST:event_tableMouseClicked

    private void btsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsaveActionPerformed
        if ("Save".equals(btsave.getText())) {
            if (!"".equals(txtpurpose.getText()) && !"".equals(txtdestination.getText()) && !"0.00".equals(txtunitcost.getText()) && !"0.00".equals(txttotal.getText()) && !"0.00".equals(txtunitcost.getText()) && !"Select Unit".equals(txtunit.getText())) {
                Save();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill the required fields.", " System Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            if (!"".equals(txtpurpose.getText()) && !"".equals(txtdestination.getText()) && !"0.00".equals(txtunitcost.getText()) && !"0.00".equals(txttotal.getText()) && !"0.00".equals(txtunitcost.getText()) && !"Select Unit".equals(txtunit.getText())) {
                Update();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill the required fields.", " System Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btsaveActionPerformed

    private void btdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeleteActionPerformed
        Delete();
    }//GEN-LAST:event_btdeleteActionPerformed

    private void txtsearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsearchKeyReleased
        Search(txtsearch.getText());
    }//GEN-LAST:event_txtsearchKeyReleased

    private void btimportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btimportActionPerformed
        FrameMRList frame = new FrameMRList();
        frame.setVisible(true);
    }//GEN-LAST:event_btimportActionPerformed

    private void spquantityInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_spquantityInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_spquantityInputMethodTextChanged

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
    private javax.swing.JButton btclose;
    private javax.swing.JButton btdelete;
    private javax.swing.JButton btimport;
    private javax.swing.JButton btsave;
    private javax.swing.JComboBox cbcompany;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblcode;
    private javax.swing.JSpinner spquantity;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtdestination;
    private static javax.swing.JTextField txtmrcode;
    private javax.swing.JTextField txtpurpose;
    private static javax.swing.JTextField txtreceive;
    private javax.swing.JTextField txtsearch;
    private javax.swing.JTextField txttotal;
    private static javax.swing.JTextField txtunit;
    private static javax.swing.JTextField txtunitcost;
    // End of variables declaration//GEN-END:variables
}
