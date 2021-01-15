package lozadagroupcompany;

import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.Connection;
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

public final class FrameMonitoring extends javax.swing.JFrame {

    DatabaseConnection conn = new DatabaseConnection();
    DefaultTableModel tablemodel;
    Connection connection = conn.getConnection();
    DefaultTableModel dm;
    String user;
    String reporttype;

    public FrameMonitoring() {
        initComponents();
        retrieveRV();
        retrieveDV();
        retrieveMR();
        retrieveMW();
        setIconImage();
    }

    private void setIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("ico.png")));
    }

    private void SearchMR(String query) {
        dm = (DefaultTableModel) tablemr.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);
        tablemr.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    private void SearchMW(String query) {
        dm = (DefaultTableModel) tablemw.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);
        tablemw.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    private void SearchDV(String query) {
        dm = (DefaultTableModel) tabledv.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);
        tabledv.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    private void SearchRV(String query) {
        dm = (DefaultTableModel) tabledv.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);
        tabledv.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    private void retrieveRV() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblreceiptvoucher");
            while (rs.next()) {
                String code = rs.getString("ReceiptCode");
                String charge = rs.getString("ChargeTo");
                String payor = rs.getString("Payor");
                String tin = rs.getString("TINNumber");
                String deposited = rs.getString("DepositedTo");
                String description = rs.getString("Description");
                String particulars = rs.getString("Particulars");
                String gross = rs.getString("GrossAmount");
                String vat = rs.getString("VAT");
                String netvat = rs.getString("NetVAT");
                String prepared = rs.getString("PreparedBy");
                String approved = rs.getString("ApprovedBy");
                String received = rs.getString("ReceivedBy");
                String status = rs.getString("Status");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, charge, payor, tin, deposited, description, particulars, gross, vat, netvat, prepared, approved, received, status, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveDV() {
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
                String tin = rs.getString("TINNumber");
                String des = rs.getString("Description");
                String part = rs.getString("Particulars");
                String gross = rs.getString("GrossAmount");
                String vat = rs.getString("VAT");
                String netvat = rs.getString("NetVAT");
                String fund = rs.getString("FundSource");
                String prepared = rs.getString("PreparedBy");
                String approved = rs.getString("ApprovedBy");
                String received = rs.getString("ReceivedBy");
                String status = rs.getString("Status");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, charge, payee, tin, des, part, gross, vat, netvat, fund, prepared, approved, received, status, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveMR() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemr.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisereceipt");
            while (rs.next()) {
                String code = rs.getString("MerchReceiptCode");
                String supplier = rs.getString("Supplier");
                String quantity = rs.getString("Quantity");
                String unit = rs.getString("Unit");
                String unitcost = rs.getString("UnitCost");
                String totalamount = rs.getString("TotalAmount");
                String reqby = rs.getString("RequestedBy");
                String approvedby = rs.getString("ApprovedBy");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, supplier, quantity, unit, unitcost, totalamount, reqby, approvedby, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveMW() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemw.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisewithdrawal");
            while (rs.next()) {
                String code = rs.getString("MerchWithdrawalCode");
                String mrcode = rs.getString("MRCode");
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
                tableModel.addRow(new Object[]{code, mrcode, no, purpose, destination, quantity, unit, unitcost, totalamount, req, approved, date});
            }
        } catch (SQLException e) {
        }
    }

    private void PrintRV() {
        int row = tablerv.getSelectedRow();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("rvcode", lblcode.getText());
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReceiptVoucher.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data from the table.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintDV() {
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("dvcode", lblcode.getText());
            try {
                InputStream i = getClass().getResourceAsStream("/report/DisbursementVoucher.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data from the table.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintMR() {
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("mrcode", lblcode.getText());
            try {
                InputStream i = getClass().getResourceAsStream("/report/MerchandiseReceipt.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data from the table.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintMW() {
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("mwcode", lblcode.getText());
            try {
                InputStream i = getClass().getResourceAsStream("/report/MerchandiseWithdrawal.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data from the table.", " System Error", JOptionPane.ERROR_MESSAGE);
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
        lblcode = new javax.swing.JLabel();
        lbltype = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablerv = new javax.swing.JTable();
        txtsrv = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablervparticular = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabledv = new javax.swing.JTable();
        txtsdv = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableparticular = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablemw = new javax.swing.JTable();
        txtsmw = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txtsmr = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablemr = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 52, 66));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MONITORING");

        lblcode.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lblcode.setForeground(new java.awt.Color(255, 153, 0));
        lblcode.setText("000000");

        lbltype.setForeground(new java.awt.Color(255, 255, 255));
        lbltype.setText("CODE");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbltype)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblcode)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblcode)
                    .addComponent(lbltype))
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

        tablerv.setBackground(new java.awt.Color(107, 115, 131));
        tablerv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tablerv.setForeground(new java.awt.Color(255, 255, 255));
        tablerv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RV Code", "Charge To", "Payor", "TIN", "Deposited To", "Description", "Particulars", "Gross Amount", "VAT", "NetVAT", "Prepared By", "Approved By", "Received By", "Status", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablerv.setGridColor(new java.awt.Color(204, 204, 204));
        tablerv.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tablerv.setSelectionForeground(new java.awt.Color(235, 235, 236));
        tablerv.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablerv.setShowHorizontalLines(true);
        tablerv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablervMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tablerv);

        txtsrv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsrv.setText("Search...");
        txtsrv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsrvKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtsrv)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsrv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Voucher", jPanel8);

        tablervparticular.setBackground(new java.awt.Color(107, 115, 131));
        tablervparticular.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        tablervparticular.setForeground(new java.awt.Color(255, 255, 255));
        tablervparticular.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RV Code", "Code", "Particular", "Quantity", "Unit", "Unit Cost", "Total Amount", "VAT Type", "VAT", "Net VAT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablervparticular.setGridColor(new java.awt.Color(204, 204, 204));
        tablervparticular.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tablervparticular.setSelectionForeground(new java.awt.Color(235, 235, 236));
        tablervparticular.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane7.setViewportView(tablervparticular);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Particular", jPanel9);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Receipt Voucher", jPanel7);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

        tabledv.setBackground(new java.awt.Color(107, 115, 131));
        tabledv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabledv.setForeground(new java.awt.Color(255, 255, 255));
        tabledv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DV Code", "Charge To", "Payee", "TIN", "Description", "Particulars", "Gross Amount", "VAT", "NetVAT", "Fund Source", "Prepared By", "Approved By", "Received By", "Status", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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

        txtsdv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsdv.setText("Search...");
        txtsdv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsdvKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtsdv)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Voucher", jPanel5);

        tableparticular.setBackground(new java.awt.Color(107, 115, 131));
        tableparticular.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        tableparticular.setForeground(new java.awt.Color(255, 255, 255));
        tableparticular.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DV Code", "Code", "Particular", "Quantity", "Unit", "Unit Cost", "Total Amount", "VAT Type", "VAT", "Net VAT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableparticular.setGridColor(new java.awt.Color(204, 204, 204));
        tableparticular.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tableparticular.setSelectionForeground(new java.awt.Color(235, 235, 236));
        jScrollPane2.setViewportView(tableparticular);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Particular", jPanel6);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Disbursement Voucher", jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

        tablemw.setBackground(new java.awt.Color(107, 115, 131));
        tablemw.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tablemw.setForeground(new java.awt.Color(255, 255, 255));
        tablemw.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MW Code", "Charge To", "Purpose", "Destination", "Quantity", "Unit", "Unit Cost", "Total Amount", "Requested By", "Approved By", "Status", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablemw.setGridColor(new java.awt.Color(204, 204, 204));
        tablemw.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tablemw.setSelectionForeground(new java.awt.Color(235, 235, 236));
        tablemw.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablemw.setShowHorizontalLines(true);
        tablemw.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablemwMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablemw);

        txtsmw.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsmw.setText("Search...");
        txtsmw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsmwKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtsmw, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsmw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Merchandise Withdrawal", jPanel3);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

        txtsmr.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsmr.setText("Search...");
        txtsmr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsmrKeyReleased(evt);
            }
        });

        tablemr.setBackground(new java.awt.Color(107, 115, 131));
        tablemr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        tablemr.setForeground(new java.awt.Color(255, 255, 255));
        tablemr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MR Code", "Supplier", "Quantity", "Unit", "Unit Cost", "Total Amount", "Requested By", "Approved By", "Status", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablemr.setGridColor(new java.awt.Color(204, 204, 204));
        tablemr.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tablemr.setSelectionForeground(new java.awt.Color(235, 235, 236));
        tablemr.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablemr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablemrMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablemr);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtsmr, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsmr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Merchandise Receipt", jPanel4);

        jButton2.setBackground(new java.awt.Color(45, 52, 66));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Close");

        jButton9.setBackground(new java.awt.Color(45, 52, 66));
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Export Exel");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(45, 52, 66));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton9)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tablemwMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemwMouseClicked
        int row = tablemw.getSelectedRow();
        DefaultTableModel TableModel = (DefaultTableModel) tablemw.getModel();
        if (row > -1) {
            lblcode.setText(TableModel.getValueAt(row, 0).toString());
            lbltype.setText("MERCHANDISE WITHDRAWAL CODE:");
        } else {
            //Do nothing!
        }
    }//GEN-LAST:event_tablemwMouseClicked

    private void tabledvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledvMouseClicked
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            DefaultTableModel TableModel1 = (DefaultTableModel) tabledv.getModel();
            DefaultTableModel TableModel2 = (DefaultTableModel) tableparticular.getModel();
            lblcode.setText(TableModel1.getValueAt(row, 0).toString());
            lbltype.setText("DISBURSEMENT VOUCHER CODE:");
            while (TableModel2.getRowCount() > 0) {
                TableModel2.removeRow(0);
            }
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucherparticular WHERE DisbursementCode = '" + lblcode.getText() + "' ");
                while (rs.next()) {
                    String dvcode = rs.getString("DisbursementCode");
                    String code = rs.getString("Code");
                    String particular = rs.getString("Particular");
                    String quantity = rs.getString("Quantity");
                    String unit = rs.getString("Unit");
                    String unitcost = rs.getString("UnitCost");
                    String gross = rs.getString("PGrossAmount");
                    String vat = rs.getString("PVAT");
                    String netvat = rs.getString("PNetVAT");
                    TableModel2.addRow(new Object[]{dvcode, code, particular, quantity, unit, unitcost, gross, vat, netvat});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_tabledvMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (null != reporttype) {
            switch (reporttype) {
                case "RV":
                    PrintRV();
                    break;
                case "DV":
                    PrintDV();
                    break;
                case "MW":
                    PrintMW();
                    break;
                case "MR":
                    PrintMR();
                    break;
            }
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void tablemrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemrMouseClicked
        int row = tablemr.getSelectedRow();
        DefaultTableModel TableModel = (DefaultTableModel) tablemr.getModel();
        if (row > -1) {
            lblcode.setText(TableModel.getValueAt(row, 0).toString());
            lbltype.setText("MERCHANDISE RECEIPT CODE:");
        } else {
            //Do nothing!
        }
    }//GEN-LAST:event_tablemrMouseClicked

    private void txtsdvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsdvKeyReleased
        SearchDV(txtsdv.getText().toUpperCase());
    }//GEN-LAST:event_txtsdvKeyReleased

    private void txtsmwKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsmwKeyReleased
        SearchMW(txtsmw.getText().toUpperCase());
    }//GEN-LAST:event_txtsmwKeyReleased

    private void txtsmrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsmrKeyReleased
        SearchMR(txtsmr.getText().toUpperCase());
    }//GEN-LAST:event_txtsmrKeyReleased

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void tablervMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablervMouseClicked
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            DefaultTableModel TableModel1 = (DefaultTableModel) tablerv.getModel();
            DefaultTableModel TableModel2 = (DefaultTableModel) tablervparticular.getModel();
            lblcode.setText(TableModel1.getValueAt(row, 0).toString());
            lbltype.setText("RECEIPT VOUCHER CODE:");
            while (TableModel2.getRowCount() > 0) {
                TableModel2.removeRow(0);
            }
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM tblreceiptvoucherparticular WHERE ReceiptCode = '" + lblcode.getText() + "' ");
                while (rs.next()) {
                    String rvcode = rs.getString("ReceiptCode");
                    String code = rs.getString("Code");
                    String particular = rs.getString("Particular");
                    String quantity = rs.getString("Quantity");
                    String unit = rs.getString("Unit");
                    String unitcost = rs.getString("UnitCost");
                    String gross = rs.getString("PGrossAmount");
                    String vat = rs.getString("PVAT");
                    String netvat = rs.getString("PNetVAT");
                    TableModel2.addRow(new Object[]{rvcode, code, particular, quantity, unit, unitcost, gross, vat, netvat});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_tablervMouseClicked

    private void txtsrvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsrvKeyReleased
        SearchRV(txtsdv.getText().toUpperCase());
    }//GEN-LAST:event_txtsrvKeyReleased

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
            java.util.logging.Logger.getLogger(FrameMonitoring.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameMonitoring.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameMonitoring.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameMonitoring.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new FrameMonitoring().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JLabel lblcode;
    private javax.swing.JLabel lbltype;
    private javax.swing.JTable tabledv;
    private javax.swing.JTable tablemr;
    private javax.swing.JTable tablemw;
    private static javax.swing.JTable tableparticular;
    private javax.swing.JTable tablerv;
    private static javax.swing.JTable tablervparticular;
    private javax.swing.JTextField txtsdv;
    private javax.swing.JTextField txtsmr;
    private javax.swing.JTextField txtsmw;
    private javax.swing.JTextField txtsrv;
    // End of variables declaration//GEN-END:variables
}
