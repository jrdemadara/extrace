package lozadagroupcompany;

import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
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
        setIconImage();

    }

    public void Refresh() {
        retrieveRV();
        retrieveDV();
        retrieveMR();
        retrieveMW();
    }

    public void GetUser(String type, String name) {
        lbltype.setText(type);
        lbluser.setText(name);
        SetRole();
    }

    private void SetRole() {
        if (null != lbltype.getText()) {
            switch (lbltype.getText()) {
                case "ADMINISTRATOR":

                    break;
                case "REQUESTER":
                    MenuSetup.setEnabled(false);
                    MenuSettings.setEnabled(false);
                    btdeleterv.setEnabled(false);
                    btdeletedv.setEnabled(false);
                    btdeletemw.setEnabled(false);
                    btdeletemr.setEnabled(false);
                    MenuVerifyRV.setEnabled(false);
                    MenuVerifyDV.setEnabled(false);
                    MenuVerifyMW.setEnabled(false);
                    MenuVerifyMR.setEnabled(false);
                    MenuVerifyRV.setText("No Actions Available");
                    MenuVerifyDV.setText("No Actions Available");
                    MenuVerifyMW.setText("No Actions Available");
                    MenuVerifyMR.setText("No Actions Available");
                    break;
                case "VERIFIER":
                    btdv.setEnabled(false);
                    btrv.setEnabled(false);
                    btmw.setEnabled(false);
                    btmr.setEnabled(false);
                    btreport.setEnabled(false);
                    MenuSetup.setEnabled(false);
                    MenuSettings.setEnabled(false);
                    MenuTransaction.setEnabled(false);
                    btdeleterv.setEnabled(false);
                    btdeletedv.setEnabled(false);
                    btdeletemw.setEnabled(false);
                    btdeletemr.setEnabled(false);
                    MenuVerifyRV.setText("Verify Request");
                    MenuVerifyDV.setText("Verify Request");
                    MenuVerifyMW.setText("Verify Request");
                    MenuVerifyMR.setText("Verify Request");
                    break;
                case "APPROVER":
                    btdv.setEnabled(false);
                    btrv.setEnabled(false);
                    btmw.setEnabled(false);
                    btmr.setEnabled(false);
                    btreport.setEnabled(false);
                    MenuSetup.setEnabled(false);
                    MenuSettings.setEnabled(false);
                    MenuTransaction.setEnabled(false);
                    MenuSetup.setEnabled(false);
                    MenuSettings.setEnabled(false);
                    btdeleterv.setEnabled(false);
                    btdeletedv.setEnabled(false);
                    btdeletemw.setEnabled(false);
                    btdeletemr.setEnabled(false);
                    MenuVerifyRV.setText("Approve Request");
                    MenuVerifyDV.setText("Approve Request");
                    MenuVerifyMW.setText("Approve Request");
                    MenuVerifyMR.setText("Approve Request");
                    break;
            }
        }
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
        dm = (DefaultTableModel) tablerv.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dm);
        tabledv.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    private void retrieveRV() {
        DefaultTableModel tableModel = (DefaultTableModel) tablerv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblreceiptvoucher");
            while (rs.next()) {
                String code = rs.getString("ReceiptCode");
                String payor = rs.getString("Payor");
                String tin = rs.getString("TINNumber");
                String deposited = rs.getString("DepositedTo");
                String description = rs.getString("Description");
                String acc = rs.getString("Account");
                String particulars = rs.getString("Particulars");
                String gross = rs.getString("GrossAmount");
                String vat = rs.getString("VAT");
                String netvat = rs.getString("NetVAT");
                String prepared = rs.getString("PreparedBy");
                String verified = rs.getString("VerifiedBy");
                String approved = rs.getString("ApprovedBy");
                String received = rs.getString("ReceivedBy");
                String status = rs.getString("Status");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, payor, tin, deposited, description, acc, particulars, gross, vat, netvat, prepared, verified, approved, received, status, date});
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
                String acc = rs.getString("Account");
                String supplier = rs.getString("Supplier");
                String quantity = rs.getString("Quantity");
                String unit = rs.getString("Unit");
                String unitcost = rs.getString("UnitCost");
                String totalamount = rs.getString("TotalAmount");
                String reqby = rs.getString("RequestedBy");
                String ver = rs.getString("VerifiedBy");
                String approvedby = rs.getString("ApprovedBy");
                String status = rs.getString("Status");
                String rec = rs.getString("ReceivedBy");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, acc, supplier, quantity, unit, unitcost, totalamount, reqby, ver, approvedby, rec, status, date});
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
                String charge = rs.getString("ChargeTo");
                String purpose = rs.getString("Purpose");
                String destination = rs.getString("Destination");
                String acc = rs.getString("Account");
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
                tableModel.addRow(new Object[]{code, mrcode, charge, purpose, destination, acc, quantity, unit, unitcost, totalamount, req, ver, approved, rec, status, date});
            }
        } catch (SQLException e) {
        }
    }

    private void PrintRV() {
        int row = tablerv.getSelectedRow();
        String code = tablerv.getValueAt(row, 0).toString();
        String status = tablerv.getValueAt(row, 13).toString();
        if ("APPROVED".equals(status)) {
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
        } else {
            JOptionPane.showMessageDialog(this, "Receipt Voucher " + code + " is not yet approved.", " System Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void PrintDV() {
        int row = tabledv.getSelectedRow();
        String code = tabledv.getValueAt(row, 0).toString();
        String status = tabledv.getValueAt(row, 13).toString();
        if ("APPROVED".equals(status)) {
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
        } else {
            JOptionPane.showMessageDialog(this, "Disbursement Voucher " + code + " is not yet approved.", " System Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void PrintMR() {
        int row = tablemr.getSelectedRow();
        String code = tablemr.getValueAt(row, 0).toString();
        String status = tablemr.getValueAt(row, 10).toString();
        if ("APPROVED".equals(status)) {
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
        } else {
            JOptionPane.showMessageDialog(this, "Merchandise Receipt " + code + " is not yet approved.", " System Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void PrintMW() {
        int row = tablemw.getSelectedRow();
        String code = tablemw.getValueAt(row, 0).toString();
        String status = tablemw.getValueAt(row, 13).toString();
        if ("APPROVED".equals(status)) {
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
        } else {
            JOptionPane.showMessageDialog(this, "Merchandise Withdrawal " + code + " is not yet approved.", " System Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void DeleteMR() {
        int row = tablemr.getSelectedRow();
        if (row > -1) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this data?", " System Information", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                String sid = tablemr.getValueAt(row, 0).toString();
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

    private void DeleteMW() {
        int row = tablemw.getSelectedRow();
        if (row > -1) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this data?", " System Information", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                String sid = tablemw.getValueAt(row, 0).toString();
                String sqlc = "DELETE FROM tblmerchandisewithdrawal WHERE MerchWithdrawalCode = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlc)) {
                    stmt.setString(1, sid);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Merchandise Withdrawal '" + lblcode.getText() + "' has been deleted!", " System Information", JOptionPane.INFORMATION_MESSAGE);
                    Refresh();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameMerchandiseReceipt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data.", " System Information", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void DeleteDV() {
        int row = tabledv.getSelectedRow();
        if (row > -1) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this data?", " System Information", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                String sid = tabledv.getValueAt(row, 0).toString();
                String sqlc = "DELETE FROM tbldisbursementvoucher WHERE DisbursementCode = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlc)) {
                    stmt.setString(1, sid);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Disbursement Voucher '" + lblcode.getText() + "' has been deleted!", " System Information", JOptionPane.INFORMATION_MESSAGE);
                    Refresh();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameMerchandiseReceipt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data.", " System Information", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void DeleteRV() {
        int row = tablerv.getSelectedRow();
        if (row > -1) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this data?", " System Information", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                String sid = tablerv.getValueAt(row, 0).toString();
                String sqlc = "DELETE FROM tblreceiptvoucher WHERE ReceiptCode = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlc)) {
                    stmt.setString(1, sid);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Receipt Voucher '" + lblcode.getText() + "' has been deleted!", " System Information", JOptionPane.INFORMATION_MESSAGE);
                    Refresh();
                } catch (SQLException ex) {
                    Logger.getLogger(FrameMerchandiseReceipt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select data.", " System Information", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void RVVerifyRequest() {
        DefaultTableModel TableModel = (DefaultTableModel) tablerv.getModel();
        int row = tablerv.getSelectedRow();
        String rvcode = TableModel.getValueAt(row, 0).toString();
        if (row > -1) {
            if (null != TableModel.getValueAt(row, 13).toString()) {
                switch (TableModel.getValueAt(row, 13).toString()) {
                    case "PENDING":
                        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblreceiptvoucher SET VerifiedBy = ?, Status = ? WHERE ReceiptCode = ?")) {
                            stmt.setString(1, lbluser.getText());
                            stmt.setString(2, "VERIFIED");
                            stmt.setString(3, rvcode);
                            stmt.executeUpdate();
                            retrieveRV();
                            JOptionPane.showMessageDialog(this, "Receipt Voucher " + rvcode + " has been verified!.", " System Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "VERIFIED":
                        JOptionPane.showMessageDialog(this, "Receipt Voucher " + rvcode + " is already verified!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "APPROVED":
                        JOptionPane.showMessageDialog(this, "Receipt Voucher " + rvcode + " is already approved!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No data selected!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void RVApproveRequest() {
        DefaultTableModel TableModel = (DefaultTableModel) tablerv.getModel();
        int row = tablerv.getSelectedRow();
        String rvcode = TableModel.getValueAt(row, 0).toString();
        if (row > -1) {
            if (null != TableModel.getValueAt(row, 13).toString()) {
                switch (TableModel.getValueAt(row, 13).toString()) {
                    case "VERIFIED":
                        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblreceiptvoucher SET ApprovedBy = ?, Status = ? WHERE ReceiptCode = ?")) {
                            stmt.setString(1, lbluser.getText());
                            stmt.setString(2, "APPROVED");
                            stmt.setString(3, rvcode);
                            stmt.executeUpdate();
                            retrieveRV();
                            JOptionPane.showMessageDialog(this, "Receipt Voucher " + rvcode + " has been approved!.", " System Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "APPROVED":
                        JOptionPane.showMessageDialog(this, "Receipt Voucher " + rvcode + " is already approved!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "PENDING":
                        JOptionPane.showMessageDialog(this, "Receipt Voucher " + rvcode + " is not yet verified!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No data selected!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void DVVerifyRequest() {
        DefaultTableModel TableModel = (DefaultTableModel) tabledv.getModel();
        int row = tabledv.getSelectedRow();
        String rvcode = TableModel.getValueAt(row, 0).toString();
        if (row > -1) {
            if (null != TableModel.getValueAt(row, 13).toString()) {
                switch (TableModel.getValueAt(row, 13).toString()) {
                    case "PENDING":
                        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbldisbursementvoucher SET VerifiedBy = ?, Status = ? WHERE DisbursementCode = ?")) {
                            stmt.setString(1, lbluser.getText());
                            stmt.setString(2, "VERIFIED");
                            stmt.setString(3, rvcode);
                            stmt.executeUpdate();
                            retrieveRV();
                            JOptionPane.showMessageDialog(this, "Disbursement Voucher " + rvcode + " has been verified!.", " System Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "VERIFIED":
                        JOptionPane.showMessageDialog(this, "Disbursement Voucher " + rvcode + " is already verified!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "APPROVED":
                        JOptionPane.showMessageDialog(this, "Disbursement Voucher " + rvcode + " is already approved!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No data selected!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void DVApproveRequest() {
        DefaultTableModel TableModel = (DefaultTableModel) tabledv.getModel();
        int row = tabledv.getSelectedRow();
        String rvcode = TableModel.getValueAt(row, 0).toString();
        if (row > -1) {
            if (null != TableModel.getValueAt(row, 13).toString()) {
                switch (TableModel.getValueAt(row, 13).toString()) {
                    case "VERIFIED":
                        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tbldisbursementvoucher SET ApprovedBy = ?, Status = ? WHERE DisbursementCode = ?")) {
                            stmt.setString(1, lbluser.getText());
                            stmt.setString(2, "APPROVED");
                            stmt.setString(3, rvcode);
                            stmt.executeUpdate();
                            retrieveRV();
                            JOptionPane.showMessageDialog(this, "Disbursement Voucher " + rvcode + " has been approved!.", " System Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "APPROVED":
                        JOptionPane.showMessageDialog(this, "Disbursement Voucher " + rvcode + " is already approved!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "PENDING":
                        JOptionPane.showMessageDialog(this, "Disbursement Voucher " + rvcode + " is not yet verified!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No data selected!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void MWVerifyRequest() {
        DefaultTableModel TableModel = (DefaultTableModel) tablemw.getModel();
        int row = tablemw.getSelectedRow();
        String rvcode = TableModel.getValueAt(row, 0).toString();
        if (row > -1) {
            if (null != TableModel.getValueAt(row, 14).toString()) {
                switch (TableModel.getValueAt(row, 14).toString()) {
                    case "PENDING":
                        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblmerchandisewithdrawal SET VerifiedBy = ?, Status = ? WHERE MerchWithdrawalCode = ?")) {
                            stmt.setString(1, lbluser.getText());
                            stmt.setString(2, "VERIFIED");
                            stmt.setString(3, rvcode);
                            stmt.executeUpdate();
                            retrieveRV();
                            JOptionPane.showMessageDialog(this, "Merchandise Withdrawal " + rvcode + " has been verified!.", " System Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "VERIFIED":
                        JOptionPane.showMessageDialog(this, "Merchandise Withdrawal " + rvcode + " is already verified!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "APPROVED":
                        JOptionPane.showMessageDialog(this, "Merchandise Withdrawal " + rvcode + " is already approved!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No data selected!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void MWApproveRequest() {
        DefaultTableModel TableModel = (DefaultTableModel) tablemw.getModel();
        int row = tablemw.getSelectedRow();
        String rvcode = TableModel.getValueAt(row, 0).toString();
        if (row > -1) {
            if (null != TableModel.getValueAt(row, 14).toString()) {
                switch (TableModel.getValueAt(row, 14).toString()) {
                    case "VERIFIED":
                        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblmerchandisewithdrawal SET ApprovedBy = ?, Status = ? WHERE MerchWithdrawalCode = ?")) {
                            stmt.setString(1, lbluser.getText());
                            stmt.setString(2, "APPROVED");
                            stmt.setString(3, rvcode);
                            stmt.executeUpdate();
                            retrieveRV();
                            JOptionPane.showMessageDialog(this, "Merchandise Withdrawal " + rvcode + " has been approved!.", " System Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "APPROVED":
                        JOptionPane.showMessageDialog(this, "Merchandise Withdrawal " + rvcode + " is already approved!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "PENDING":
                        JOptionPane.showMessageDialog(this, "Merchandise Withdrawal " + rvcode + " is not yet verified!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No data selected!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void MRVerifyRequest() {
        DefaultTableModel TableModel = (DefaultTableModel) tablemr.getModel();
        int row = tablemr.getSelectedRow();
        String rvcode = TableModel.getValueAt(row, 0).toString();
        if (row > -1) {
            if (null != TableModel.getValueAt(row, 11).toString()) {
                switch (TableModel.getValueAt(row, 11).toString()) {
                    case "PENDING":
                        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblmerchandisereceipt SET VerifiedBy = ?, Status = ? WHERE MerchReceiptCode = ?")) {
                            stmt.setString(1, lbluser.getText());
                            stmt.setString(2, "VERIFIED");
                            stmt.setString(3, rvcode);
                            stmt.executeUpdate();
                            retrieveRV();
                            JOptionPane.showMessageDialog(this, "Merchandise Receipt " + rvcode + " has been verified!.", " System Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "VERIFIED":
                        JOptionPane.showMessageDialog(this, "Merchandise Receipt " + rvcode + " is already verified!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "APPROVED":
                        JOptionPane.showMessageDialog(this, "Merchandise Receipt " + rvcode + " is already approved!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No data selected!", " System Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void MRApproveRequest() {
        DefaultTableModel TableModel = (DefaultTableModel) tablemr.getModel();
        int row = tablemr.getSelectedRow();
        String rvcode = TableModel.getValueAt(row, 0).toString();
        if (row > -1) {
            if (null != TableModel.getValueAt(row, 11).toString()) {
                switch (TableModel.getValueAt(row, 11).toString()) {
                    case "VERIFIED":
                        try (PreparedStatement stmt = connection.prepareStatement("UPDATE tblmerchandisereceipt SET ApprovedBy = ?, Status = ? WHERE MerchReceiptCode = ?")) {
                            stmt.setString(1, lbluser.getText());
                            stmt.setString(2, "APPROVED");
                            stmt.setString(3, rvcode);
                            stmt.executeUpdate();
                            retrieveRV();
                            JOptionPane.showMessageDialog(this, "Merchandise Receipt " + rvcode + " has been approved!.", " System Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, "Can't read record in system table!\nSystem detects changes in table entities!\nPlease contact the backend developer.", "ERROR 1012", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    case "APPROVED":
                        JOptionPane.showMessageDialog(this, "Merchandise Receipt " + rvcode + " is already approved!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "PENDING":
                        JOptionPane.showMessageDialog(this, "Merchandise Receipt " + rvcode + " is not yet verified!.", " System Warning", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No data selected!", " System Information", JOptionPane.INFORMATION_MESSAGE);
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

        PopupMenuRV = new javax.swing.JPopupMenu();
        MenuVerifyRV = new javax.swing.JMenuItem();
        PopupMenuDV = new javax.swing.JPopupMenu();
        MenuVerifyDV = new javax.swing.JMenuItem();
        PopupMenuMW = new javax.swing.JPopupMenu();
        MenuVerifyMW = new javax.swing.JMenuItem();
        PopupMenuMR = new javax.swing.JPopupMenu();
        MenuVerifyMR = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btmonitoring1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbluser = new javax.swing.JLabel();
        lbltype = new javax.swing.JLabel();
        btrefresh = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btreport = new javax.swing.JButton();
        btmr = new javax.swing.JButton();
        btmw = new javax.swing.JButton();
        btdv = new javax.swing.JButton();
        btrv = new javax.swing.JButton();
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
        btreportrv = new javax.swing.JButton();
        btdeleterv = new javax.swing.JButton();
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
        tablemw = new javax.swing.JTable();
        txtsmw = new javax.swing.JTextField();
        btreportmw = new javax.swing.JButton();
        btdeletemw = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        txtsmr = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablemr = new javax.swing.JTable();
        btreportmr = new javax.swing.JButton();
        btdeletemr = new javax.swing.JButton();
        lbldatatype = new javax.swing.JLabel();
        lblcode = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        MenuSetup = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        MenuTransaction = new javax.swing.JMenu();
        mirv = new javax.swing.JMenuItem();
        midv = new javax.swing.JMenuItem();
        mimw = new javax.swing.JMenuItem();
        mimr = new javax.swing.JMenuItem();
        MenuSettings = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();

        PopupMenuRV.setInvoker(tablerv);

        MenuVerifyRV.setText("Approve Request");
        MenuVerifyRV.setActionCommand("Verify Request");
        MenuVerifyRV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuVerifyRVActionPerformed(evt);
            }
        });
        PopupMenuRV.add(MenuVerifyRV);

        PopupMenuDV.setInvoker(tabledv);

        MenuVerifyDV.setText("Approve Request");
        MenuVerifyDV.setActionCommand("Verify Request");
        PopupMenuDV.add(MenuVerifyDV);

        PopupMenuMW.setInvoker(tablemw);

        MenuVerifyMW.setText("Approve Request");
        MenuVerifyMW.setActionCommand("Verify Request");
        PopupMenuMW.add(MenuVerifyMW);

        PopupMenuMR.setInvoker(tablemr);

        MenuVerifyMR.setText("Approve Request");
        MenuVerifyMR.setActionCommand("Verify Request");
        PopupMenuMR.add(MenuVerifyMR);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LGCMerch 1.0.0");
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
        btmonitoring1.setForeground(new java.awt.Color(255, 255, 255));
        btmonitoring1.setText("Logout");
        btmonitoring1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btmonitoring1.setVerifyInputWhenFocusTarget(false);
        btmonitoring1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmonitoring1ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("USER TYPE:");

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("USER NAME:");

        lbluser.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lbluser.setForeground(new java.awt.Color(255, 153, 0));
        lbluser.setText("xxxx");

        lbltype.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lbltype.setForeground(new java.awt.Color(255, 153, 0));
        lbltype.setText("xxxx");

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
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbltype)
                .addGap(10, 10, 10)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbluser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                    .addComponent(jLabel7)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btmonitoring1)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(lbluser)
                            .addComponent(lbltype)
                            .addComponent(btrefresh))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel3))
                            .addGap(4, 4, 4)
                            .addComponent(jLabel2))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        btmr.setBackground(new java.awt.Color(255, 153, 0));
        btmr.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btmr.setForeground(new java.awt.Color(255, 255, 255));
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
        btmw.setForeground(new java.awt.Color(255, 255, 255));
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
        btdv.setForeground(new java.awt.Color(255, 255, 255));
        btdv.setText("DISBURSEMENT VOUCHER");
        btdv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btdv.setVerifyInputWhenFocusTarget(false);
        btdv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btdvActionPerformed(evt);
            }
        });

        btrv.setBackground(new java.awt.Color(255, 153, 0));
        btrv.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        btrv.setForeground(new java.awt.Color(255, 255, 255));
        btrv.setText("RECEIPT VOUCHER");
        btrv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btrv.setVerifyInputWhenFocusTarget(false);
        btrv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btrvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btrv, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btdv, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmw)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btmr, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btreport, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btrv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btdv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btmw, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btmr, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btreport, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

        tablerv.setBackground(new java.awt.Color(107, 115, 131));
        tablerv.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tablerv.setForeground(new java.awt.Color(255, 255, 255));
        tablerv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RV Code", "Payor", "TIN", "Deposited To", "Description", "Account", "Particulars", "Gross Amount", "VAT", "NetVAT", "Prepared By", "Verified By", "Approved By", "Received By", "Status", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablerv.setComponentPopupMenu(PopupMenuRV);
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
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1338, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsrv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
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
            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1338, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Particular", jPanel9);

        btreportrv.setBackground(new java.awt.Color(45, 52, 66));
        btreportrv.setForeground(new java.awt.Color(255, 255, 255));
        btreportrv.setText("Print");
        btreportrv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btreportrvActionPerformed(evt);
            }
        });

        btdeleterv.setBackground(new java.awt.Color(45, 52, 66));
        btdeleterv.setForeground(new java.awt.Color(255, 255, 255));
        btdeleterv.setText("Delete");
        btdeleterv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btdeletervActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane3)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btreportrv, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btdeleterv, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btreportrv)
                    .addComponent(btdeleterv))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Receipt Voucher", jPanel7);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

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
        tabledv.setComponentPopupMenu(PopupMenuDV);
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

        tablemw.setBackground(new java.awt.Color(107, 115, 131));
        tablemw.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tablemw.setForeground(new java.awt.Color(255, 255, 255));
        tablemw.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MW Code", "MR Code", "Charge To", "Purpose", "Destination", "Account", "Quantity", "Unit", "Unit Cost", "Total Amount", "Requested By", "Verified By", "Approved By", "Received By", "Status", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablemw.setComponentPopupMenu(PopupMenuMW);
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

        btreportmw.setBackground(new java.awt.Color(45, 52, 66));
        btreportmw.setForeground(new java.awt.Color(255, 255, 255));
        btreportmw.setText("Print");
        btreportmw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btreportmwActionPerformed(evt);
            }
        });

        btdeletemw.setBackground(new java.awt.Color(45, 52, 66));
        btdeletemw.setForeground(new java.awt.Color(255, 255, 255));
        btdeletemw.setText("Delete");
        btdeletemw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btdeletemwActionPerformed(evt);
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
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(btreportmw, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btdeletemw, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsmw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btreportmw)
                    .addComponent(btdeletemw))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Merchandise Withdrawal", jPanel11);

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(146, 152, 163)));

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
        tablemr.setComponentPopupMenu(PopupMenuMR);
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

        btreportmr.setBackground(new java.awt.Color(45, 52, 66));
        btreportmr.setForeground(new java.awt.Color(255, 255, 255));
        btreportmr.setText("Print");
        btreportmr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btreportmrActionPerformed(evt);
            }
        });

        btdeletemr.setBackground(new java.awt.Color(45, 52, 66));
        btdeletemr.setForeground(new java.awt.Color(255, 255, 255));
        btdeletemr.setText("Delete");
        btdeletemr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btdeletemrActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtsmr, javax.swing.GroupLayout.DEFAULT_SIZE, 1338, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(btreportmr, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btdeletemr, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsmr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btreportmr)
                    .addComponent(btdeletemr))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Merchandise Receipt", jPanel12);

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
                            .addComponent(lblcode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jMenuItem9.setText("Items");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        MenuSetup.add(jMenuItem9);

        jMenuItem13.setText("Personnel");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        MenuSetup.add(jMenuItem13);

        jMenuBar1.add(MenuSetup);

        MenuTransaction.setText("Transaction");

        mirv.setText("Cash Receipt Voucher");
        mirv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mirvActionPerformed(evt);
            }
        });
        MenuTransaction.add(mirv);

        midv.setText("Cash Disbursement Voucher");
        midv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                midvActionPerformed(evt);
            }
        });
        MenuTransaction.add(midv);

        mimw.setText("Merchandise Withdrawal");
        mimw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mimwActionPerformed(evt);
            }
        });
        MenuTransaction.add(mimw);

        mimr.setText("Merchandise Receipt");
        mimr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mimrActionPerformed(evt);
            }
        });
        MenuTransaction.add(mimr);

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
        frame.GetUser(lbluser.getText());
    }//GEN-LAST:event_btdvActionPerformed

    private void btmwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmwActionPerformed
        FrameMerchandiseWithdrawal frame = new FrameMerchandiseWithdrawal();
        frame.setVisible(true);
        frame.GetUser(lbluser.getText());
    }//GEN-LAST:event_btmwActionPerformed

    private void btmrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmrActionPerformed
        FrameMerchandiseReceipt frame = new FrameMerchandiseReceipt();
        frame.setVisible(true);
        frame.GetUser(lbluser.getText());
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

    private void midvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_midvActionPerformed
        FrameDisbursementVoucher frame = new FrameDisbursementVoucher();
        frame.setVisible(true);
    }//GEN-LAST:event_midvActionPerformed

    private void mimwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mimwActionPerformed
        FrameMerchandiseWithdrawal frame = new FrameMerchandiseWithdrawal();
        frame.setVisible(true);
    }//GEN-LAST:event_mimwActionPerformed

    private void mimrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mimrActionPerformed
        FrameMerchandiseReceipt frame = new FrameMerchandiseReceipt();
        frame.setVisible(true);
    }//GEN-LAST:event_mimrActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        FramePersonnel frame = new FramePersonnel();
        frame.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

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

    private void btrvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btrvActionPerformed
        FrameReceiptVoucher frame = new FrameReceiptVoucher();
        frame.setVisible(true);
        frame.GetUser(lbluser.getText());
    }//GEN-LAST:event_btrvActionPerformed

    private void mirvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mirvActionPerformed
        FrameReceiptVoucher frame = new FrameReceiptVoucher();
        frame.setVisible(true);
    }//GEN-LAST:event_mirvActionPerformed

    private void tablervMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablervMouseClicked
        int row = tablerv.getSelectedRow();
        if (row > -1) {
            DefaultTableModel TableModel1 = (DefaultTableModel) tablerv.getModel();
            DefaultTableModel TableModel2 = (DefaultTableModel) tablervparticular.getModel();
            lblcode.setText(TableModel1.getValueAt(row, 0).toString());
            lbldatatype.setText("RECEIPT VOUCHER CODE");
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

    private void txtsdvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsdvKeyReleased
        SearchDV(txtsdv.getText().toUpperCase());
    }//GEN-LAST:event_txtsdvKeyReleased

    private void tablemwMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemwMouseClicked
        int row = tablemw.getSelectedRow();
        DefaultTableModel TableModel = (DefaultTableModel) tablemw.getModel();
        if (row > -1) {
            lblcode.setText(TableModel.getValueAt(row, 0).toString());
            lbldatatype.setText("MERCHANDISE WITHDRAWAL CODE");
        } else {
            //Do nothing!
        }
    }//GEN-LAST:event_tablemwMouseClicked

    private void txtsmwKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsmwKeyReleased
        SearchMW(txtsmw.getText().toUpperCase());
    }//GEN-LAST:event_txtsmwKeyReleased

    private void txtsmrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsmrKeyReleased
        SearchMR(txtsmr.getText().toUpperCase());
    }//GEN-LAST:event_txtsmrKeyReleased

    private void tablemrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemrMouseClicked
        int row = tablemr.getSelectedRow();
        DefaultTableModel TableModel = (DefaultTableModel) tablemr.getModel();
        if (row > -1) {
            lblcode.setText(TableModel.getValueAt(row, 0).toString());
            lbldatatype.setText("MERCHANDISE RECEIPT CODE");
        } else {
            //Do nothing!
        }
    }//GEN-LAST:event_tablemrMouseClicked

    private void btrefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btrefreshActionPerformed
        Refresh();
    }//GEN-LAST:event_btrefreshActionPerformed

    private void btreportrvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btreportrvActionPerformed
        PrintRV();
    }//GEN-LAST:event_btreportrvActionPerformed

    private void btreportdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btreportdvActionPerformed
        PrintDV();
    }//GEN-LAST:event_btreportdvActionPerformed

    private void btreportmwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btreportmwActionPerformed
        PrintMW();
    }//GEN-LAST:event_btreportmwActionPerformed

    private void btreportmrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btreportmrActionPerformed
        PrintMR();
    }//GEN-LAST:event_btreportmrActionPerformed

    private void btdeletervActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeletervActionPerformed
        DeleteRV();
    }//GEN-LAST:event_btdeletervActionPerformed

    private void btdeletedvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeletedvActionPerformed
        DeleteDV();
    }//GEN-LAST:event_btdeletedvActionPerformed

    private void btdeletemwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeletemwActionPerformed
        DeleteMW();
    }//GEN-LAST:event_btdeletemwActionPerformed

    private void btdeletemrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btdeletemrActionPerformed
        DeleteMR();
    }//GEN-LAST:event_btdeletemrActionPerformed

    private void MenuVerifyRVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuVerifyRVActionPerformed
        if ("Verify Request".equals(MenuVerifyRV.getText())) {
            RVVerifyRequest();
        } else {
            RVApproveRequest();
        }

    }//GEN-LAST:event_MenuVerifyRVActionPerformed

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
    private javax.swing.JMenuItem MenuVerifyDV;
    private javax.swing.JMenuItem MenuVerifyMR;
    private javax.swing.JMenuItem MenuVerifyMW;
    private javax.swing.JMenuItem MenuVerifyRV;
    private javax.swing.JPopupMenu PopupMenuDV;
    private javax.swing.JPopupMenu PopupMenuMR;
    private javax.swing.JPopupMenu PopupMenuMW;
    private javax.swing.JPopupMenu PopupMenuRV;
    private javax.swing.JButton btdeletedv;
    private javax.swing.JButton btdeletemr;
    private javax.swing.JButton btdeletemw;
    private javax.swing.JButton btdeleterv;
    private javax.swing.JButton btdv;
    private javax.swing.JButton btmonitoring1;
    private javax.swing.JButton btmr;
    private javax.swing.JButton btmw;
    private javax.swing.JButton btrefresh;
    private javax.swing.JButton btreport;
    private javax.swing.JButton btreportdv;
    private javax.swing.JButton btreportmr;
    private javax.swing.JButton btreportmw;
    private javax.swing.JButton btreportrv;
    private javax.swing.JButton btrv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
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
    private javax.swing.JLabel lbldatatype;
    private javax.swing.JLabel lbltype;
    private javax.swing.JLabel lbluser;
    private javax.swing.JMenuItem midv;
    private javax.swing.JMenuItem mimr;
    private javax.swing.JMenuItem mimw;
    private javax.swing.JMenuItem mirv;
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
