package dvmanage;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

public final class FrameMain extends javax.swing.JFrame {

    DatabaseConnection conn = new DatabaseConnection();
    DefaultTableModel tablemodel;
    Connection connection = conn.getConnection();
    DefaultTableModel dm;

    public FrameMain() {
        initComponents();
        Refresh();

    }

    public void Refresh() {
        retrieveDV();
        retrieveJE();
    }

    private void SearchDV(String query) {
        dm = (DefaultTableModel) tabledv.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);
        tabledv.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    private void clearDVparticular() {
        DefaultTableModel tableModel = (DefaultTableModel) tableparticular.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
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
                String fund = rs.getString("FundSource");
                String des = rs.getString("Description");
                String check = rs.getString("CheckNo");
                String amount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, charge, payee, tin, fund, des, check, amount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveJE() {
        DefaultTableModel tableModel = (DefaultTableModel) tablejournal.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbljournal");
            while (rs.next()) {
                String date = rs.getString("Date");
                String ref = rs.getString("Reference");
                String code = rs.getString("Code");
                String acc = rs.getString("Account");
                String deb = rs.getString("Debit");
                String cre = rs.getString("Credit");
                String bal = rs.getString("Balance");
                String stat = rs.getString("Status");
                String com = rs.getString("Company");
                tableModel.addRow(new Object[]{date, ref, code, acc, deb, cre, bal, stat, com});
            }
        } catch (SQLException e) {
        }
    }

    private void PrintDV() {
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            String gross = tabledv.getValueAt(row, 7).toString();
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("dvcode", lblcode.getText());
            parameters.put("total", "₱ " + NumberFunction.getFormattedNumber(gross));
            try {
                InputStream i = getClass().getResourceAsStream("/report/DisbursementVoucher.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMain.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, ex, " System Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data from the table.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void UpdateJournal() {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbljournal SET Status = ? WHERE Reference = ?")) {
            stmt.setString(1, "CLOSE");
            stmt.setString(2, lblcode.getText());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrameDVList.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btmonitoring1 = new javax.swing.JButton();
        btrefresh = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btreport = new javax.swing.JButton();
        btdv = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabledv = new javax.swing.JTable();
        txtsdv = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableparticular = new javax.swing.JTable();
        btreportdv = new javax.swing.JButton();
        btdeletedv = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablejournal = new javax.swing.JTable();
        txtsmw = new javax.swing.JTextField();
        lbldatatype = new javax.swing.JLabel();
        lblcode = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        MenuSetup = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        MenuTransaction = new javax.swing.JMenu();
        midv = new javax.swing.JMenuItem();
        midv1 = new javax.swing.JMenuItem();
        MenuSettings = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DVManage 1.0.0");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 52, 66));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DVManage");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 0));
        jLabel2.setText("Slicksoft Automation");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("1.0.0");

        btmonitoring1.setBackground(new java.awt.Color(255, 153, 0));
        btmonitoring1.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btmonitoring1.setForeground(new java.awt.Color(255, 255, 255));
        btmonitoring1.setText("Logout");
        btmonitoring1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btmonitoring1.setVerifyInputWhenFocusTarget(false);
        btmonitoring1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmonitoring1ActionPerformed(evt);
            }
        });

        btrefresh.setBackground(new java.awt.Color(255, 153, 0));
        btrefresh.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btrefresh.setForeground(new java.awt.Color(255, 255, 255));
        btrefresh.setText("Refresh");
        btrefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btrefresh.setVerifyInputWhenFocusTarget(false);
        btrefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btrefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btrefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmonitoring1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btmonitoring1)
                        .addComponent(btrefresh))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(4, 4, 4)
                        .addComponent(jLabel2)))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(137, 145, 162)));

        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

        btreport.setBackground(new java.awt.Color(255, 153, 0));
        btreport.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btreport.setForeground(new java.awt.Color(255, 255, 255));
        btreport.setText("REPORT");
        btreport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btreport.setVerifyInputWhenFocusTarget(false);
        btreport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btreportActionPerformed(evt);
            }
        });

        btdv.setBackground(new java.awt.Color(255, 153, 0));
        btdv.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btdv.setForeground(new java.awt.Color(255, 255, 255));
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
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btdv, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btreport, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btdv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btreport, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

        tabledv.setBackground(new java.awt.Color(107, 115, 131));
        tabledv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabledv.setForeground(new java.awt.Color(255, 255, 255));
        tabledv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DV Code", "Charge To", "Payee", "TIN", "Fund Source", "Description", "Cheque No.", "Total Amount", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtsdv)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1338, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Voucher", jPanel6);

        tableparticular.setBackground(new java.awt.Color(107, 115, 131));
        tableparticular.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        tableparticular.setForeground(new java.awt.Color(255, 255, 255));
        tableparticular.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DV Code", "Code", "Account", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableparticular.setGridColor(new java.awt.Color(204, 204, 204));
        tableparticular.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tableparticular.setSelectionForeground(new java.awt.Color(235, 235, 236));
        tableparticular.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tableparticular);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1338, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Particular", jPanel10);

        btreportdv.setBackground(new java.awt.Color(45, 52, 66));
        btreportdv.setForeground(new java.awt.Color(255, 255, 255));
        btreportdv.setText("Print");
        btreportdv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btreportdvActionPerformed(evt);
            }
        });

        btdeletedv.setBackground(new java.awt.Color(45, 52, 66));
        btdeletedv.setForeground(new java.awt.Color(255, 255, 255));
        btdeletedv.setText("Delete");
        btdeletedv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btdeletedvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btreportdv, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btdeletedv, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btreportdv)
                    .addComponent(btdeletedv))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Disbursement Voucher", jPanel5);

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

        tablejournal.setBackground(new java.awt.Color(107, 115, 131));
        tablejournal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tablejournal.setForeground(new java.awt.Color(255, 255, 255));
        tablejournal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Reference", "Code", "Account", "Debit", "Credit", "Balance", "Status", "Company"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablejournal.setGridColor(new java.awt.Color(204, 204, 204));
        tablejournal.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tablejournal.setSelectionForeground(new java.awt.Color(235, 235, 236));
        tablejournal.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablejournal.setShowHorizontalLines(true);
        tablejournal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablejournalMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tablejournalMouseEntered(evt);
            }
        });
        jScrollPane3.setViewportView(tablejournal);

        txtsmw.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsmw.setText("Search...");
        txtsmw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtsmwKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtsmw, javax.swing.GroupLayout.DEFAULT_SIZE, 1338, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsmw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Journal Entry", jPanel11);

        lbldatatype.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        lbldatatype.setForeground(new java.awt.Color(45, 52, 66));
        lbldatatype.setText("CODE");

        lblcode.setFont(new java.awt.Font("sansserif", 1, 32)); // NOI18N
        lblcode.setForeground(new java.awt.Color(255, 153, 0));
        lblcode.setText("000000");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbldatatype, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblcode))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lbldatatype, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblcode, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(45, 52, 66));

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 153, 0));
        jLabel5.setText("Slicksoft Coder");

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

        MenuSetup.setText("Setup");

        jMenuItem8.setText("Chart of Account");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        MenuSetup.add(jMenuItem8);

        jMenuItem16.setText("Supplier");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        MenuSetup.add(jMenuItem16);

        jMenuItem10.setText("Unit");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        MenuSetup.add(jMenuItem10);

        jMenuItem13.setText("Company");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        MenuSetup.add(jMenuItem13);

        jMenuBar1.add(MenuSetup);

        MenuTransaction.setText("Transaction");

        midv.setText("Cash Disbursement Voucher");
        midv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                midvActionPerformed(evt);
            }
        });
        MenuTransaction.add(midv);

        midv1.setText("Revolving Fund");
        midv1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                midv1ActionPerformed(evt);
            }
        });
        MenuTransaction.add(midv1);

        jMenuBar1.add(MenuTransaction);

        MenuSettings.setText("Settings");

        jMenuItem11.setText("User Account");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        MenuSettings.add(jMenuItem11);

        jMenuItem12.setText("Database");
        jMenuItem12.setEnabled(false);
        MenuSettings.add(jMenuItem12);

        jMenuBar1.add(MenuSettings);

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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        FrameChartofAccount frame = new FrameChartofAccount();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        FrameUnit frame = new FrameUnit();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void midvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_midvActionPerformed
        FrameDisbursementVoucher frame = new FrameDisbursementVoucher();
        frame.setVisible(true);
    }//GEN-LAST:event_midvActionPerformed

    private void btreportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btreportActionPerformed
        FrameReport frame = new FrameReport();
        frame.setVisible(true);
    }//GEN-LAST:event_btreportActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        FrameUser frame = new FrameUser();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void btmonitoring1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmonitoring1ActionPerformed
        dispose();
        FrameLogin frame = new FrameLogin();
        frame.setVisible(true);
    }//GEN-LAST:event_btmonitoring1ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        FrameSupplier frame = new FrameSupplier();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void tabledvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledvMouseClicked
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            DefaultTableModel TableModel1 = (DefaultTableModel) tabledv.getModel();
            DefaultTableModel TableModel2 = (DefaultTableModel) tableparticular.getModel();
            lblcode.setText(TableModel1.getValueAt(row, 0).toString());
            lbldatatype.setText("DISBURSEMENT VOUCHER CODE");
            while (TableModel2.getRowCount() > 0) {
                TableModel2.removeRow(0);
            }
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucherparticular WHERE DisbursementCode = '" + lblcode.getText() + "' ");
                while (rs.next()) {
                    String dvcode = rs.getString("DisbursementCode");
                    String code = rs.getString("Code");
                    String account = rs.getString("Account");
                    String amount = rs.getString("Amount");
                    TableModel2.addRow(new Object[]{dvcode, code, account, amount});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_tabledvMouseClicked

    private void txtsdvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsdvKeyReleased
        SearchDV(txtsdv.getText().toUpperCase());
    }//GEN-LAST:event_txtsdvKeyReleased

    private void tablejournalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablejournalMouseClicked
        int row = tablejournal.getSelectedRow();
        DefaultTableModel TableModel = (DefaultTableModel) tablejournal.getModel();
        if (row > -1) {
            lblcode.setText(TableModel.getValueAt(row, 1).toString());
            lbldatatype.setText("JOURNAL REFERENCE");
        } else {
            //Do nothing!
        }
    }//GEN-LAST:event_tablejournalMouseClicked

    private void txtsmwKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsmwKeyReleased
        // SearchMW(txtsmw.getText().toUpperCase());
    }//GEN-LAST:event_txtsmwKeyReleased

    private void btrefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btrefreshActionPerformed
        Refresh();
    }//GEN-LAST:event_btrefreshActionPerformed

    private void btreportdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btreportdvActionPerformed
        PrintDV();
    }//GEN-LAST:event_btreportdvActionPerformed


    private void btdeletedvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeletedvActionPerformed
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this voucher?", " System Information", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                String sid = tabledv.getValueAt(row, 0).toString();
                String sqlc = "DELETE tbldisbursementvoucher,tbldisbursementvoucherparticular FROM tbldisbursementvoucher INNER JOIN tbldisbursementvoucherparticular ON tbldisbursementvoucherparticular.DisbursementCode = tbldisbursementvoucher.DisbursementCode WHERE tbldisbursementvoucher.DisbursementCode = ?;";
                try (PreparedStatement stmt = connection.prepareStatement(sqlc)) {
                    stmt.setString(1, sid);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Voucher '" + lblcode.getText() + "' has been deleted!", " System Information", JOptionPane.INFORMATION_MESSAGE);
                    UpdateJournal();
                    Refresh();
                    clearDVparticular();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameChartofAccount.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data.", " System Information", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btdeletedvActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        FrameCompany frame = new FrameCompany();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void midv1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_midv1ActionPerformed
        FrameRevolvingFund frame = new FrameRevolvingFund();
        frame.setVisible(true);
    }//GEN-LAST:event_midv1ActionPerformed

    private void tablejournalMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablejournalMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tablejournalMouseEntered

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
    private javax.swing.JMenu MenuSettings;
    private javax.swing.JMenu MenuSetup;
    private javax.swing.JMenu MenuTransaction;
    private javax.swing.JButton btdeletedv;
    private javax.swing.JButton btdv;
    private javax.swing.JButton btmonitoring1;
    private javax.swing.JButton btrefresh;
    private javax.swing.JButton btreport;
    private javax.swing.JButton btreportdv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel lblcode;
    private javax.swing.JLabel lbldatatype;
    private javax.swing.JMenuItem midv;
    private javax.swing.JMenuItem midv1;
    private javax.swing.JTable tabledv;
    private javax.swing.JTable tablejournal;
    private static javax.swing.JTable tableparticular;
    private javax.swing.JTextField txtsdv;
    private javax.swing.JTextField txtsmw;
    // End of variables declaration//GEN-END:variables
}
