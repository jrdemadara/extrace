package lozadagroupcompany;

import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public final class FrameMain extends javax.swing.JFrame {

    static String user;
    static String utype;
    DatabaseConnection conn = new DatabaseConnection();
    DefaultTableModel tablemodel;
    Connection connection = conn.getConnection();
    DefaultTableModel dm;

    public FrameMain() {
        initComponents();
        retrieveMR();
        retrieveMW();
        retrieveDV();
        setIconImage();

    }

    private void setIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("ico.png")));
    }

    public static void userDetails(String username, String usertype) {
        user = username;
        utype = usertype;
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

    void retrieveMR() {
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
                String requestedby = rs.getString("RequestedBy");
                String approvedby = rs.getString("ApprovedBy");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, supplier, quantity, unit, unitcost, totalamount, requestedby, approvedby, date});
            }
        } catch (SQLException e) {
        }
    }

    void retrieveMW() {
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

    void retrieveDV() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String payee = rs.getString("Payee");
                String des = rs.getString("Description");
                String part = rs.getString("Particulars");
                String gross = rs.getString("GrossAmount");
                String vat = rs.getString("VAT");
                String netvat = rs.getString("NetVAT");
                String fund = rs.getString("FundSource");
                String pre = rs.getString("PreparedBy");
                String approved = rs.getString("ApprovedBy");
                String receive = rs.getString("ReceivedBy");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, payee, des, part, gross, vat, netvat, fund, pre, approved, receive, date});
            }
        } catch (SQLException e) {
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

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btmonitoring1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablemr = new javax.swing.JTable();
        txtsmr = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        txtsmw = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablemw = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabledv = new javax.swing.JTable();
        txtsdv = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabledvpart = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btmonitoring = new javax.swing.JButton();
        btmr = new javax.swing.JButton();
        btmw = new javax.swing.JButton();
        btdv = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        jMenuItem14.setText("jMenuItem14");

        jMenuItem15.setText("jMenuItem15");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 52, 66));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("LGCMerch");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 0));
        jLabel2.setText("Slicksoft Automation");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("1.0.0");

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lozadagroupcompany/logo-64.png"))); // NOI18N

        btmonitoring1.setBackground(new java.awt.Color(255, 153, 0));
        btmonitoring1.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btmonitoring1.setText("Logout");
        btmonitoring1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btmonitoring1.setVerifyInputWhenFocusTarget(false);
        btmonitoring1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmonitoring1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btmonitoring1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btmonitoring1)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel3))
                            .addGap(4, 4, 4)
                            .addComponent(jLabel2))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(137, 145, 162)));

        jTabbedPane1.setBackground(new java.awt.Color(45, 52, 66));
        jTabbedPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        tablemr.setBackground(new java.awt.Color(107, 115, 131));
        tablemr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        tablemr.setForeground(new java.awt.Color(255, 255, 255));
        tablemr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MR Code", "Supplier", "Quantity", "Unit", "Unit Cost", "Total Amount", "Requested By", "Approved By", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
        jScrollPane2.setViewportView(tablemr);

        txtsmr.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsmr.setText("Search...");
        txtsmr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsmrKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 916, Short.MAX_VALUE)
            .addComponent(txtsmr)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsmr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Merchandise Receipt", jPanel5);

        txtsmw.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsmw.setText("Search...");
        txtsmw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsmwKeyReleased(evt);
            }
        });

        tablemw.setBackground(new java.awt.Color(107, 115, 131));
        tablemw.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tablemw.setForeground(new java.awt.Color(255, 255, 255));
        tablemw.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MW Code", "MR Code", "Business No", "Purpose", "Destination", "Quantity", "Unit", "Unit Cost", "Total Amount", "Requested By", "Approved By", "Date"
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
        jScrollPane4.setViewportView(tablemw);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtsmw, javax.swing.GroupLayout.DEFAULT_SIZE, 916, Short.MAX_VALUE)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsmw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Merchandise Withdrawal", jPanel6);

        tabledv.setBackground(new java.awt.Color(107, 115, 131));
        tabledv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabledv.setForeground(new java.awt.Color(255, 255, 255));
        tabledv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DV Code", "Payee", "Description", "Particulars", "Gross Amount", "VAT", "NetVAT", "Fund Source", "Prepared By", "Approved By", "Received By", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
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
        jScrollPane5.setViewportView(tabledv);

        txtsdv.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsdv.setText("Search...");
        txtsdv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsdvKeyReleased(evt);
            }
        });

        tabledvpart.setBackground(new java.awt.Color(107, 115, 131));
        tabledvpart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        tabledvpart.setForeground(new java.awt.Color(255, 255, 255));
        tabledvpart.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DV Code", "Code", "Item", "Quantity", "Unit", "Unit Cost", "Total Amount", "VAT", "Net VAT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabledvpart.setGridColor(new java.awt.Color(204, 204, 204));
        tabledvpart.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tabledvpart.setSelectionForeground(new java.awt.Color(235, 235, 236));
        jScrollPane3.setViewportView(tabledvpart);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 916, Short.MAX_VALUE)
            .addComponent(txtsdv, javax.swing.GroupLayout.DEFAULT_SIZE, 916, Short.MAX_VALUE)
            .addComponent(jScrollPane3)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Disbursement Voucher", jPanel7);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lozadagroupcompany/logo-120.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Lato Black", 1, 24)); // NOI18N
        jLabel6.setText("Welcome to LGCMerch");

        jLabel8.setFont(new java.awt.Font("Lato", 0, 12)); // NOI18N
        jLabel8.setText("A  data management software for LGC Merchandise.");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "USER TASK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lato", 0, 12))); // NOI18N

        btmonitoring.setBackground(new java.awt.Color(255, 153, 0));
        btmonitoring.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btmonitoring.setText("MONITORING");
        btmonitoring.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btmonitoring.setVerifyInputWhenFocusTarget(false);
        btmonitoring.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmonitoringActionPerformed(evt);
            }
        });

        btmr.setBackground(new java.awt.Color(255, 153, 0));
        btmr.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btmr.setText("MERCHANDISE RECEIPT");
        btmr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btmr.setVerifyInputWhenFocusTarget(false);
        btmr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmrActionPerformed(evt);
            }
        });

        btmw.setBackground(new java.awt.Color(255, 153, 0));
        btmw.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btmw.setText("MERCHANDISE WITHDRAWAL");
        btmw.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btmw.setVerifyInputWhenFocusTarget(false);
        btmw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmwActionPerformed(evt);
            }
        });

        btdv.setBackground(new java.awt.Color(255, 153, 0));
        btdv.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btdv.setText("DISBURSEMENT VOUCHER");
        btdv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btdv.setVerifyInputWhenFocusTarget(false);
        btdv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btdvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btdv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btmw, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
            .addComponent(btmr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btmonitoring, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(btdv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmw, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmr, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmonitoring, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 918, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(159, 159, 159))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel8))))
                        .addContainerGap(82, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addGap(43, 43, 43)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1)))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(45, 52, 66));

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 153, 0));
        jLabel5.setText("Slicksoft Automation");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Setup");

        jMenuItem8.setText("Chart of Account");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem10.setText("Unit");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem9.setText("Items");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem13.setText("Personnel");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem13);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Transaction");

        jMenuItem3.setText("Cash Disbursement Voucher");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem4.setText("Merchandise Withdrawal");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem5.setText("Merchandise Receipt");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Settings");

        jMenuItem11.setText("User Account");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem11);

        jMenuItem12.setText("Database");
        jMenuItem12.setEnabled(false);
        jMenu4.add(jMenuItem12);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Help");

        jMenuItem19.setText("Help Content");
        jMenuItem19.setEnabled(false);
        jMenu5.add(jMenuItem19);

        jMenuItem20.setText("Developer");
        jMenuItem20.setEnabled(false);
        jMenu5.add(jMenuItem20);

        jMenuItem21.setText("About");
        jMenuItem21.setEnabled(false);
        jMenu5.add(jMenuItem21);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdvActionPerformed
        FrameDisbursementVoucher frame = new FrameDisbursementVoucher();
        frame.setVisible(true);
    }//GEN-LAST:event_btdvActionPerformed

    private void btmwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmwActionPerformed
        FrameMerchandiseWithdrawal frame = new FrameMerchandiseWithdrawal();
        frame.setVisible(true);
    }//GEN-LAST:event_btmwActionPerformed

    private void btmrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmrActionPerformed
        FrameMerchandiseReceipt frame = new FrameMerchandiseReceipt();
        frame.setVisible(true);
    }//GEN-LAST:event_btmrActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        FrameChartofAccount frame = new FrameChartofAccount();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        FrameItem frame = new FrameItem();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        FrameUnit frame = new FrameUnit();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        FrameDisbursementVoucher frame = new FrameDisbursementVoucher();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        FrameMerchandiseWithdrawal frame = new FrameMerchandiseWithdrawal();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        FrameMerchandiseReceipt frame = new FrameMerchandiseReceipt();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        FramePersonnel frame = new FramePersonnel();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void btmonitoringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmonitoringActionPerformed
        FrameMonitoring frame = new FrameMonitoring();
        frame.setVisible(true);
    }//GEN-LAST:event_btmonitoringActionPerformed

    private void tablemrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemrMouseClicked

    }//GEN-LAST:event_tablemrMouseClicked

    private void tablemwMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemwMouseClicked


    }//GEN-LAST:event_tablemwMouseClicked

    private void tabledvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledvMouseClicked
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            DefaultTableModel TableModel1 = (DefaultTableModel) tabledv.getModel();
            DefaultTableModel TableModel2 = (DefaultTableModel) tabledvpart.getModel();
            String scode = TableModel1.getValueAt(row, 0).toString();
            while (TableModel2.getRowCount() > 0) {
                TableModel2.removeRow(0);
            }
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucherparticular WHERE DisbursementCode = '" + scode + "' ");
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
        } else {
            //Do nothing!
        }
    }//GEN-LAST:event_tabledvMouseClicked

    private void txtsmrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsmrKeyReleased
        SearchMR(txtsmr.getText().toUpperCase());
    }//GEN-LAST:event_txtsmrKeyReleased

    private void txtsmwKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsmwKeyReleased
        SearchMW(txtsmw.getText().toUpperCase());
    }//GEN-LAST:event_txtsmwKeyReleased

    private void txtsdvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsdvKeyReleased
        SearchDV(txtsdv.getText().toUpperCase());
    }//GEN-LAST:event_txtsdvKeyReleased

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        FrameUser frame = new FrameUser();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void btmonitoring1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmonitoring1ActionPerformed
        dispose();
        FrameLogin frame = new FrameLogin();
        frame.setVisible(true);
    }//GEN-LAST:event_btmonitoring1ActionPerformed

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
    private javax.swing.JButton btdv;
    private javax.swing.JButton btmonitoring;
    private javax.swing.JButton btmonitoring1;
    private javax.swing.JButton btmr;
    private javax.swing.JButton btmw;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tabledv;
    private static javax.swing.JTable tabledvpart;
    private javax.swing.JTable tablemr;
    private javax.swing.JTable tablemw;
    private javax.swing.JTextField txtsdv;
    private javax.swing.JTextField txtsmr;
    private javax.swing.JTextField txtsmw;
    // End of variables declaration//GEN-END:variables
}
