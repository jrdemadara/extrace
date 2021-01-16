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
import javax.swing.table.DefaultTableModel;
import static lozadagroupcompany.NumberFunction.getFormattedNumber;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class FrameReport extends javax.swing.JFrame {

    DatabaseConnection conn = new DatabaseConnection();
    DefaultTableModel tablemodel;
    Connection connection = conn.getConnection();
    DefaultTableModel dm;
    String importtype;
    String datefrom;
    String dateto;
    String overalltotal;
    String vat;
    String netvat;
    String charge;
    String account;
    String printtype = "bydate";

    public FrameReport() {
        initComponents();
        RetrieveAccountMW();
        RetrieveCompanyMW();
        RetrieveAccountDV();
        RetrieveCompanyDV();
        RetrieveAccountRV();
        RetrieveAccountMR();
        retrieveMR();
        retrieveMW();
        retrieveDV();
        retrieveRV();

        setIconImage();
    }

    private void setIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("ico.png")));
    }

    public void SetImport(String type) {
        importtype = type;
    }

    //********************MR CODE*************************//
    private void retrieveMR() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemr.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisereceipt");
            while (rs.next()) {
                String code = rs.getString("MerchReceiptCode");
                String quantity = rs.getString("Account");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, quantity, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveMRByDate() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemr.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisereceipt WHERE Status = 'APPROVED' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("MerchReceiptCode");
                String quantity = rs.getString("Account");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, quantity, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveMRByDateAccount() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemr.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisereceipt WHERE Status = 'APPROVED' AND Account = '" + cbaccountmr.getSelectedItem() + "' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("MerchReceiptCode");
                String quantity = rs.getString("Account");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, quantity, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void CountCalculateMR() {
        double totalamount = 0;
        for (int i = 0; i < tablemr.getRowCount(); i++) {
            double total = Double.parseDouble(tablemr.getValueAt(i, 2).toString());
            totalamount += total;

        }
        overalltotal = Double.toString(totalamount);
    }

    private void PrintMR() {
        int row = tablemr.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overalltotal", "Php " + getFormattedNumber(overalltotal));
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportMR.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void PrintMRByAccount() {
        int row = tablemr.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            parameters.put("account", account);
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportMRAccount.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void RetrieveAccountMR() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblchartofaccount");
            while (rs.next()) {
                String chart = rs.getString("ChartName");
                cbaccountmr.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    //********************MW CODE*************************//
    private void retrieveMW() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemw.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisewithdrawal");
            while (rs.next()) {
                String code = rs.getString("MerchWithdrawalCode");
                String icharge = rs.getString("ChargeTo");
                String iaccount = rs.getString("Account");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, iaccount, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveMWByDate() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemw.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisewithdrawal WHERE Status = 'APPROVED' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("MerchWithdrawalCode");
                String icharge = rs.getString("ChargeTo");
                String iaccount = rs.getString("Account");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, iaccount, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveMWByDateCharge() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemw.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisewithdrawal WHERE Status = 'APPROVED' AND ChargeTo = '" + cbchargemw.getSelectedItem() + "' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("MerchWithdrawalCode");
                String icharge = rs.getString("ChargeTo");
                String iaccount = rs.getString("Account");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, iaccount, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveMWByDateAccount() {
        DefaultTableModel tableModel = (DefaultTableModel) tablemw.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblmerchandisewithdrawal WHERE Status = 'APPROVED' AND Account = '" + cbaccountmw.getSelectedItem() + "' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("MerchWithdrawalCode");
                String icharge = rs.getString("ChargeTo");
                String iaccount = rs.getString("Account");
                String totalamount = rs.getString("TotalAmount");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, iaccount, totalamount, date});
            }
        } catch (SQLException e) {
        }
    }

    private void CountCalculateMW() {
        double totalamount = 0;
        for (int i = 0; i < tablemw.getRowCount(); i++) {
            double total = Double.parseDouble(tablemw.getValueAt(i, 3).toString());
            totalamount += total;

        }
        overalltotal = Double.toString(totalamount);
    }

    private void PrintMW() {
        int row = tablemw.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportMW.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintMWByCompany() {
        int row = tablemw.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            parameters.put("chargeto", charge);
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportMWChargeTo.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintMWByAccount() {
        int row = tablemw.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            parameters.put("account", account);
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportMWAccount.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void RetrieveCompanyMW() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblcompany");
            while (rs.next()) {
                String chart = rs.getString("CompanyName");
                cbchargemw.addItem(chart);

            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveAccountMW() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblchartofaccount");
            while (rs.next()) {
                String chart = rs.getString("ChartName");
                cbaccountmw.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    //********************DV CODE*************************//
    private void retrieveDV() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String icharge = rs.getString("ChargeTo");
                String iaccount = rs.getString("FundSource");
                String totalamount = rs.getString("GrossAmount");
                String ivat = rs.getString("VAT");
                String inetvat = rs.getString("NetVAT");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, iaccount, totalamount, ivat, inetvat, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveDVByDate() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher WHERE Status = 'APPROVED' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String icharge = rs.getString("ChargeTo");
                String iaccount = rs.getString("FundSource");
                String totalamount = rs.getString("GrossAmount");
                String ivat = rs.getString("VAT");
                String inetvat = rs.getString("NetVAT");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, iaccount, totalamount, ivat, inetvat, date});
            }

        } catch (SQLException e) {
        }
    }

    private void retrieveDVByDateCharge() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher WHERE Status = 'APPROVED' AND ChargeTo = '" + cbchargedv.getSelectedItem() + "' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String icharge = rs.getString("ChargeTo");
                String iaccount = rs.getString("FundSource");
                String totalamount = rs.getString("GrossAmount");
                String ivat = rs.getString("VAT");
                String inetvat = rs.getString("NetVAT");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, iaccount, totalamount, ivat, inetvat, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveDVByDateAccount() {
        DefaultTableModel tableModel = (DefaultTableModel) tabledv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbldisbursementvoucher WHERE Status = 'APPROVED' AND FundSource = '" + cbaccountdv.getSelectedItem() + "' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("DisbursementCode");
                String icharge = rs.getString("ChargeTo");
                String iaccount = rs.getString("FundSource");
                String totalamount = rs.getString("GrossAmount");
                String ivat = rs.getString("VAT");
                String inetvat = rs.getString("NetVAT");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, icharge, iaccount, totalamount, ivat, inetvat, date});
            }
        } catch (SQLException e) {
        }
    }

    private void CountCalculateDV() {
        double totalamount = 0;
        double vatamount = 0;
        double vatinput = 0;
        for (int i = 0; i < tabledv.getRowCount(); i++) {
            double total = Double.parseDouble(tabledv.getValueAt(i, 3).toString());
            double ivat = Double.parseDouble(tabledv.getValueAt(i, 4).toString());
            double inetvat = Double.parseDouble(tabledv.getValueAt(i, 5).toString());
            totalamount += total;
            vatamount += ivat;
            vatinput += inetvat;

        }
        overalltotal = Double.toString(totalamount);
        vat = Double.toString(vatamount);
        netvat = Double.toString(vatinput);
    }

    private void PrintDV() {
        int row = tabledv.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            parameters.put("vat", "Php " + getFormattedNumber(vat));
            parameters.put("netvat", "Php " + getFormattedNumber(netvat));
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportDV.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintDVByCompany() {
        int row = tabledv.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            parameters.put("vat", "Php " + getFormattedNumber(vat));
            parameters.put("netvat", "Php " + getFormattedNumber(netvat));
            parameters.put("chargeto", charge);
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportDVChargeTo.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintDVByAccount() {
        int row = tabledv.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            parameters.put("vat", "Php " + getFormattedNumber(vat));
            parameters.put("netvat", "Php " + getFormattedNumber(netvat));
            parameters.put("account", account);
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportDVAccount.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void RetrieveCompanyDV() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblcompany");
            while (rs.next()) {
                String chart = rs.getString("CompanyName");
                cbchargedv.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    private void RetrieveAccountDV() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblchartofaccount");
            while (rs.next()) {
                String chart = rs.getString("ChartName");
                cbaccountdv.addItem(chart);
            }
        } catch (SQLException e) {
        }
    }

    //********************RV CODE*************************//
    private void retrieveRV() {
        DefaultTableModel tableModel = (DefaultTableModel) tablerv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblreceiptvoucher");
            while (rs.next()) {
                String code = rs.getString("ReceiptCode");
                String iaccount = rs.getString("Account");
                String totalamount = rs.getString("GrossAmount");
                String ivat = rs.getString("VAT");
                String inetvat = rs.getString("NetVAT");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, iaccount, totalamount, ivat, inetvat, date});
            }
        } catch (SQLException e) {
        }
    }

    private void retrieveRVByDate() {
        DefaultTableModel tableModel = (DefaultTableModel) tablerv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblreceiptvoucher WHERE Status = 'APPROVED' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("ReceiptCode");
                String iaccount = rs.getString("Account");
                String totalamount = rs.getString("GrossAmount");
                String ivat = rs.getString("VAT");
                String inetvat = rs.getString("NetVAT");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, iaccount, totalamount, ivat, inetvat, date});
            }

        } catch (SQLException e) {
        }
    }

    private void retrieveRVByDateAccount() {
        DefaultTableModel tableModel = (DefaultTableModel) tablerv.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblreceiptvoucher WHERE Status = 'APPROVED' AND Account = '" + cbaccountrv.getSelectedItem() + "' AND Date BETWEEN '" + datefrom + "' AND '" + dateto + "' ");
            while (rs.next()) {
                String code = rs.getString("ReceiptCode");
                String iaccount = rs.getString("Account");
                String totalamount = rs.getString("GrossAmount");
                String ivat = rs.getString("VAT");
                String inetvat = rs.getString("NetVAT");
                String date = rs.getString("Date");
                tableModel.addRow(new Object[]{code, iaccount, totalamount, ivat, inetvat, date});
            }
        } catch (SQLException e) {
        }
    }

    private void CountCalculateRV() {
        double totalamount = 0;
        double vatamount = 0;
        double vatinput = 0;
        for (int i = 0; i < tablerv.getRowCount(); i++) {
            double total = Double.parseDouble(tablerv.getValueAt(i, 2).toString());
            double ivat = Double.parseDouble(tablerv.getValueAt(i, 3).toString());
            double inetvat = Double.parseDouble(tablerv.getValueAt(i, 4).toString());
            totalamount += total;
            vatamount += ivat;
            vatinput += inetvat;

        }
        overalltotal = Double.toString(totalamount);
        vat = Double.toString(vatamount);
        netvat = Double.toString(vatinput);
    }

    private void PrintRV() {
        int row = tablerv.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            parameters.put("vat", "Php " + getFormattedNumber(vat));
            parameters.put("netvat", "Php " + getFormattedNumber(netvat));
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportRV.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintRVByAccount() {
        int row = tablerv.getRowCount();
        if (row > -1) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("datefrom", datefrom);
            parameters.put("dateto", dateto);
            parameters.put("overallamount", "Php " + getFormattedNumber(overalltotal));
            parameters.put("vat", "Php " + getFormattedNumber(vat));
            parameters.put("netvat", "Php " + getFormattedNumber(netvat));
            parameters.put("account", account);
            try {
                InputStream i = getClass().getResourceAsStream("/report/ReportRVAccount.jrxml");
                JasperDesign jasdi = JRXmlLoader.load(i);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, parameters, connection);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                Logger.getLogger(FrameMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "There are no data between that date.", " System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void RetrieveAccountRV() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblchartofaccount");
            while (rs.next()) {
                String chart = rs.getString("ChartName");
                cbaccountrv.addItem(chart);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablerv = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        dtfromrv = new datechooser.beans.DateChooserCombo();
        jLabel13 = new javax.swing.JLabel();
        dttorv = new datechooser.beans.DateChooserCombo();
        jRadioButton5 = new javax.swing.JRadioButton();
        jPanel16 = new javax.swing.JPanel();
        cbaccountrv = new javax.swing.JComboBox();
        checkboxaccountrv = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabledv = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        dtfromdv = new datechooser.beans.DateChooserCombo();
        jLabel11 = new javax.swing.JLabel();
        dttodv = new datechooser.beans.DateChooserCombo();
        jRadioButton4 = new javax.swing.JRadioButton();
        jPanel14 = new javax.swing.JPanel();
        cbchargedv = new javax.swing.JComboBox();
        cbaccountdv = new javax.swing.JComboBox();
        checkboxaccountdv = new javax.swing.JCheckBox();
        checkboxcompanydv = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablemr = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        dtfrommr = new datechooser.beans.DateChooserCombo();
        jLabel15 = new javax.swing.JLabel();
        dttomr = new datechooser.beans.DateChooserCombo();
        jRadioButton6 = new javax.swing.JRadioButton();
        jPanel18 = new javax.swing.JPanel();
        cbaccountmr = new javax.swing.JComboBox();
        checkboxaccountmr = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablemw = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        dtfrommw = new datechooser.beans.DateChooserCombo();
        jLabel7 = new javax.swing.JLabel();
        dttomw = new datechooser.beans.DateChooserCombo();
        jRadioButton3 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        cbchargemw = new javax.swing.JComboBox();
        cbaccountmw = new javax.swing.JComboBox();
        checkboxaccountmw = new javax.swing.JCheckBox();
        checkboxcompanymw = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 52, 66));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DATA REPORT");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

        jButton12.setBackground(new java.awt.Color(45, 52, 66));
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Print");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(45, 52, 66));
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Close");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        tablerv.setBackground(new java.awt.Color(107, 115, 131));
        tablerv.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
        tablerv.setForeground(new java.awt.Color(255, 255, 255));
        tablerv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RV Code", "Account", "Total Amount", "VAT", "Net VAT", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablerv.setGridColor(new java.awt.Color(204, 204, 204));
        tablerv.setSelectionBackground(new java.awt.Color(45, 52, 66));
        tablerv.setSelectionForeground(new java.awt.Color(235, 235, 236));
        tablerv.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablerv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablervMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tablerv);

        jPanel15.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

        jLabel12.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel12.setText("From :");

        dtfromrv.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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

    jLabel13.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel13.setText("To :");

    dttorv.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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
dttorv.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
    public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
        dttorvOnSelectionChange(evt);
    }
    });
    dttorv.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            dttorvOnCommit(evt);
        }
    });

    jRadioButton5.setSelected(true);
    jRadioButton5.setText("Filter By Date");
    jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jRadioButton5ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
    jPanel15.setLayout(jPanel15Layout);
    jPanel15Layout.setHorizontalGroup(
        jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel15Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addComponent(jLabel12)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtfromrv, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel13)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dttorv, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addComponent(jRadioButton5)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel15Layout.setVerticalGroup(
        jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jRadioButton5)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dttorv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dtfromrv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel16.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    cbaccountrv.setEnabled(false);
    cbaccountrv.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbaccountrvActionPerformed(evt);
        }
    });

    checkboxaccountrv.setText("Filter By Account");
    checkboxaccountrv.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            checkboxaccountrvActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
    jPanel16.setLayout(jPanel16Layout);
    jPanel16Layout.setHorizontalGroup(
        jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(checkboxaccountrv)
                .addComponent(cbaccountrv, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );
    jPanel16Layout.setVerticalGroup(
        jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(checkboxaccountrv)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cbaccountrv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
    jPanel11.setLayout(jPanel11Layout);
    jPanel11Layout.setHorizontalGroup(
        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane6)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    jPanel11Layout.setVerticalGroup(
        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton12)
                .addComponent(jButton13))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
    jPanel10.setLayout(jPanel10Layout);
    jPanel10Layout.setHorizontalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel10Layout.setVerticalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("RECEIPT VOUCHER", jPanel10);

    jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    tabledv.setBackground(new java.awt.Color(107, 115, 131));
    tabledv.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
    tabledv.setForeground(new java.awt.Color(255, 255, 255));
    tabledv.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "DV Code", "Charge To", "Account", "Total Amount", "VAT", "NetVAT", "Date"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    tabledv.setGridColor(new java.awt.Color(204, 204, 204));
    tabledv.setSelectionBackground(new java.awt.Color(45, 52, 66));
    tabledv.setSelectionForeground(new java.awt.Color(235, 235, 236));
    tabledv.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    tabledv.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tabledvMouseClicked(evt);
        }
    });
    jScrollPane3.setViewportView(tabledv);

    jButton9.setBackground(new java.awt.Color(45, 52, 66));
    jButton9.setForeground(new java.awt.Color(255, 255, 255));
    jButton9.setText("Print");
    jButton9.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton9ActionPerformed(evt);
        }
    });

    jButton10.setBackground(new java.awt.Color(45, 52, 66));
    jButton10.setForeground(new java.awt.Color(255, 255, 255));
    jButton10.setText("Close");
    jButton10.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton10ActionPerformed(evt);
        }
    });

    jPanel13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    jLabel10.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel10.setText("From :");

    dtfromdv.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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

jLabel11.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
jLabel11.setText("To :");

dttodv.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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
dttodv.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
    dttodvOnSelectionChange(evt);
    }
    });
    dttodv.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            dttodvOnCommit(evt);
        }
    });

    jRadioButton4.setSelected(true);
    jRadioButton4.setText("Filter By Date");
    jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jRadioButton4ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
    jPanel13.setLayout(jPanel13Layout);
    jPanel13Layout.setHorizontalGroup(
        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel13Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addComponent(jLabel10)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtfromdv, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel11)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dttodv, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jRadioButton4))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel13Layout.setVerticalGroup(
        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jRadioButton4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dttodv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dtfromdv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel14.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    cbchargedv.setEnabled(false);
    cbchargedv.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbchargedvActionPerformed(evt);
        }
    });

    cbaccountdv.setEnabled(false);
    cbaccountdv.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbaccountdvActionPerformed(evt);
        }
    });

    checkboxaccountdv.setText("Filter By Account");
    checkboxaccountdv.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            checkboxaccountdvActionPerformed(evt);
        }
    });

    checkboxcompanydv.setText("Filter By Company");
    checkboxcompanydv.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            checkboxcompanydvActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
    jPanel14.setLayout(jPanel14Layout);
    jPanel14Layout.setHorizontalGroup(
        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel14Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cbaccountdv, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(checkboxaccountdv))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cbchargedv, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(checkboxcompanydv))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel14Layout.setVerticalGroup(
        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel14Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(checkboxaccountdv)
                .addComponent(checkboxcompanydv))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbchargedv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cbaccountdv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton9)
                .addComponent(jButton10))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("DISBURSEMENT VOUCHER", jPanel3);

    jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    tablemr.setBackground(new java.awt.Color(107, 115, 131));
    tablemr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
    tablemr.setForeground(new java.awt.Color(255, 255, 255));
    tablemr.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "MR Code", "Account", "Total Amount", "Date"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false
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

    jButton1.setBackground(new java.awt.Color(45, 52, 66));
    jButton1.setForeground(new java.awt.Color(255, 255, 255));
    jButton1.setText("Print");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jButton2.setBackground(new java.awt.Color(45, 52, 66));
    jButton2.setForeground(new java.awt.Color(255, 255, 255));
    jButton2.setText("Close");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
        }
    });

    jPanel17.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    jLabel14.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel14.setText("From :");

    dtfrommr.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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

jLabel15.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
jLabel15.setText("To :");

dttomr.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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
dttomr.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
    dttomrOnSelectionChange(evt);
    }
    });
    dttomr.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            dttomrOnCommit(evt);
        }
    });

    jRadioButton6.setSelected(true);
    jRadioButton6.setText("Filter By Date");
    jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jRadioButton6ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
    jPanel17.setLayout(jPanel17Layout);
    jPanel17Layout.setHorizontalGroup(
        jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel17Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addComponent(jLabel14)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtfrommr, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel15)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dttomr, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addComponent(jRadioButton6)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel17Layout.setVerticalGroup(
        jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jRadioButton6)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dttomr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dtfrommr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel18.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    cbaccountmr.setEnabled(false);
    cbaccountmr.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbaccountmrActionPerformed(evt);
        }
    });

    checkboxaccountmr.setText("Filter By Account");
    checkboxaccountmr.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            checkboxaccountmrActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
    jPanel18.setLayout(jPanel18Layout);
    jPanel18Layout.setHorizontalGroup(
        jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(checkboxaccountmr)
                .addComponent(cbaccountmr, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );
    jPanel18Layout.setVerticalGroup(
        jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(checkboxaccountmr)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cbaccountmr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton1)
                .addComponent(jButton2))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("MERCHANDISE RECEIPT", jPanel5);

    jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    tablemw.setBackground(new java.awt.Color(107, 115, 131));
    tablemw.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(45, 52, 66)));
    tablemw.setForeground(new java.awt.Color(255, 255, 255));
    tablemw.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "MW Code", "Charge To", "Account", "Total Amount", "Date"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    tablemw.setGridColor(new java.awt.Color(204, 204, 204));
    tablemw.setSelectionBackground(new java.awt.Color(45, 52, 66));
    tablemw.setSelectionForeground(new java.awt.Color(235, 235, 236));
    tablemw.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    tablemw.setShowVerticalLines(true);
    tablemw.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tablemwMouseClicked(evt);
        }
    });
    jScrollPane2.setViewportView(tablemw);

    jButton6.setBackground(new java.awt.Color(45, 52, 66));
    jButton6.setForeground(new java.awt.Color(255, 255, 255));
    jButton6.setText("Print");
    jButton6.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton6ActionPerformed(evt);
        }
    });

    jButton7.setBackground(new java.awt.Color(45, 52, 66));
    jButton7.setForeground(new java.awt.Color(255, 255, 255));
    jButton7.setText("Close");
    jButton7.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton7ActionPerformed(evt);
        }
    });

    jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    jLabel6.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
    jLabel6.setText("From :");

    dtfrommw.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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

jLabel7.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
jLabel7.setText("To :");

dttomw.setCurrentView(new datechooser.view.appearance.AppearancesList("Grey",
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
dttomw.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
    dttomwOnSelectionChange(evt);
    }
    });
    dttomw.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            dttomwOnCommit(evt);
        }
    });

    jRadioButton3.setSelected(true);
    jRadioButton3.setText("Filter By Date");
    jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jRadioButton3ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dtfrommw, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dttomw, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jRadioButton3))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jRadioButton3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(dttomw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dtfrommw, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 52, 66), 1, true));

    cbchargemw.setEnabled(false);
    cbchargemw.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbchargemwActionPerformed(evt);
        }
    });

    cbaccountmw.setEnabled(false);
    cbaccountmw.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbaccountmwActionPerformed(evt);
        }
    });

    checkboxaccountmw.setText("Filter By Account");
    checkboxaccountmw.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            checkboxaccountmwActionPerformed(evt);
        }
    });

    checkboxcompanymw.setText("Filter By Company");
    checkboxcompanymw.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            checkboxcompanymwActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cbaccountmw, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(checkboxaccountmw))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cbchargemw, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(checkboxcompanymw))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(checkboxaccountmw)
                .addComponent(checkboxcompanymw))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbchargemw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cbaccountmw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))))
            .addContainerGap())
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton6)
                .addComponent(jButton7))
            .addContainerGap())
    );

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("MERCHADISE WITHDRAWAL", jPanel4);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jTabbedPane1)
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTabbedPane1))
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (null != printtype) {
            switch (printtype) {
                case "bydate":
                    PrintMR();
                    break;
                case "byaccount":
                    PrintMRByAccount();
                    break;
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tablemrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemrMouseClicked

    }//GEN-LAST:event_tablemrMouseClicked

    private void tablemwMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablemwMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablemwMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (null != printtype) {
            switch (printtype) {
                case "bydate":
                    PrintMW();
                    break;
                case "byaccount":
                    PrintMWByAccount();
                    break;
                default:
                    PrintMWByCompany();
                    break;
            }
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tabledvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabledvMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabledvMouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (null != printtype) {
            switch (printtype) {
                case "bydate":
                    PrintDV();
                    break;
                case "byaccount":
                    PrintDVByAccount();
                    break;
                default:
                    PrintDVByCompany();
                    break;
            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if (null != printtype) {
            switch (printtype) {
                case "bydate":
                    PrintRV();
                    break;
                case "byaccount":
                    PrintRVByAccount();
                    break;
            }
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void dttomwOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dttomwOnSelectionChange


    }//GEN-LAST:event_dttomwOnSelectionChange

    private void cbchargemwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbchargemwActionPerformed
        charge = cbchargemw.getSelectedItem().toString();
        datefrom = dtfrommw.getText().replace("/", "-");
        dateto = dttomw.getText().replace("/", "-");
        retrieveMWByDateCharge();
        CountCalculateMW();
    }//GEN-LAST:event_cbchargemwActionPerformed

    private void tablervMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablervMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablervMouseClicked

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void cbaccountmwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbaccountmwActionPerformed
        account = cbaccountmw.getSelectedItem().toString();
        datefrom = dtfrommw.getText().replace("/", "-");
        dateto = dttomw.getText().replace("/", "-");
        retrieveMWByDateAccount();
        CountCalculateMW();

    }//GEN-LAST:event_cbaccountmwActionPerformed

    private void dttomwOnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dttomwOnCommit
        printtype = "bydate";
        datefrom = dtfrommw.getText().replace("/", "-");
        dateto = dttomw.getText().replace("/", "-");
        retrieveMWByDate();
        CountCalculateMW();
    }//GEN-LAST:event_dttomwOnCommit

    private void checkboxaccountmwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxaccountmwActionPerformed
        if (checkboxaccountmw.isSelected()) {
            checkboxcompanymw.setSelected(false);
            cbchargemw.setEnabled(false);
            account = cbaccountmw.getSelectedItem().toString();
            datefrom = dtfrommw.getText().replace("/", "-");
            dateto = dttomw.getText().replace("/", "-");
            cbaccountmw.setEnabled(true);
            printtype = "byaccount";
            retrieveMWByDateAccount();
            CountCalculateMW();
        } else {
            account = "";
            printtype = "bydate";
            datefrom = dtfrommw.getText().replace("/", "-");
            dateto = dttomw.getText().replace("/", "-");
            cbaccountmw.setEnabled(false);
            retrieveMWByDate();
            CountCalculateMW();

        }

    }//GEN-LAST:event_checkboxaccountmwActionPerformed

    private void checkboxcompanymwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxcompanymwActionPerformed
        if (checkboxcompanymw.isSelected()) {
            checkboxaccountmw.setSelected(false);
            cbaccountmw.setEnabled(false);
            charge = cbchargemw.getSelectedItem().toString();
            datefrom = dtfrommw.getText().replace("/", "-");
            dateto = dttomw.getText().replace("/", "-");
            cbchargemw.setEnabled(true);
            printtype = "bycompany";
            retrieveMWByDateCharge();
            CountCalculateMW();
        } else {
            charge = "";
            printtype = "bydate";
            datefrom = dtfrommw.getText().replace("/", "-");
            dateto = dttomw.getText().replace("/", "-");
            cbchargemw.setEnabled(false);
            retrieveMWByDate();
            CountCalculateMW();
        }
    }//GEN-LAST:event_checkboxcompanymwActionPerformed

    private void dttodvOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dttodvOnSelectionChange
        // TODO add your handling code here:
    }//GEN-LAST:event_dttodvOnSelectionChange

    private void dttodvOnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dttodvOnCommit
        printtype = "bydate";
        datefrom = dtfromdv.getText().replace("/", "-");
        dateto = dttodv.getText().replace("/", "-");
        retrieveDVByDate();
        CountCalculateDV();

    }//GEN-LAST:event_dttodvOnCommit

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void cbchargedvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbchargedvActionPerformed
        charge = cbchargedv.getSelectedItem().toString();
        datefrom = dtfromdv.getText().replace("/", "-");
        dateto = dttodv.getText().replace("/", "-");
        retrieveDVByDateCharge();
        CountCalculateDV();
    }//GEN-LAST:event_cbchargedvActionPerformed

    private void cbaccountdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbaccountdvActionPerformed
        account = cbaccountdv.getSelectedItem().toString();
        datefrom = dtfromdv.getText().replace("/", "-");
        dateto = dttodv.getText().replace("/", "-");
        retrieveDVByDateAccount();
        CountCalculateDV();
    }//GEN-LAST:event_cbaccountdvActionPerformed

    private void checkboxaccountdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxaccountdvActionPerformed
        if (checkboxaccountdv.isSelected()) {
            checkboxcompanydv.setSelected(false);
            cbchargedv.setEnabled(false);
            account = cbaccountdv.getSelectedItem().toString();
            datefrom = dtfromdv.getText().replace("/", "-");
            dateto = dttodv.getText().replace("/", "-");
            cbaccountdv.setEnabled(true);
            printtype = "byaccount";
            retrieveDVByDateAccount();
            CountCalculateDV();
        } else {
            account = "";
            printtype = "bydate";
            datefrom = dtfromdv.getText().replace("/", "-");
            dateto = dttodv.getText().replace("/", "-");
            cbaccountdv.setEnabled(false);
            retrieveDVByDate();
            CountCalculateDV();

        }
    }//GEN-LAST:event_checkboxaccountdvActionPerformed

    private void checkboxcompanydvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxcompanydvActionPerformed
        if (checkboxcompanydv.isSelected()) {
            checkboxaccountdv.setSelected(false);
            cbaccountdv.setEnabled(false);
            charge = cbchargedv.getSelectedItem().toString();
            datefrom = dtfromdv.getText().replace("/", "-");
            dateto = dttodv.getText().replace("/", "-");
            cbchargedv.setEnabled(true);
            printtype = "bycompany";
            retrieveDVByDateCharge();
            CountCalculateDV();
        } else {
            charge = "";
            printtype = "bydate";
            datefrom = dtfromdv.getText().replace("/", "-");
            dateto = dttodv.getText().replace("/", "-");
            cbchargedv.setEnabled(false);
            retrieveDVByDate();
            CountCalculateDV();
        }
    }//GEN-LAST:event_checkboxcompanydvActionPerformed

    private void dttorvOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dttorvOnSelectionChange
        // TODO add your handling code here:
    }//GEN-LAST:event_dttorvOnSelectionChange

    private void dttorvOnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dttorvOnCommit
        printtype = "bydate";
        datefrom = dtfromrv.getText().replace("/", "-");
        dateto = dttorv.getText().replace("/", "-");
        retrieveRVByDate();
        CountCalculateRV();
    }//GEN-LAST:event_dttorvOnCommit

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void cbaccountrvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbaccountrvActionPerformed
        account = cbaccountrv.getSelectedItem().toString();
        datefrom = dtfromrv.getText().replace("/", "-");
        dateto = dttorv.getText().replace("/", "-");
        retrieveRVByDateAccount();
        CountCalculateRV();
    }//GEN-LAST:event_cbaccountrvActionPerformed

    private void checkboxaccountrvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxaccountrvActionPerformed
        if (checkboxaccountrv.isSelected()) {
            account = cbaccountrv.getSelectedItem().toString();
            datefrom = dtfromrv.getText().replace("/", "-");
            dateto = dttorv.getText().replace("/", "-");
            cbaccountrv.setEnabled(true);
            printtype = "byaccount";
            retrieveRVByDateAccount();
            CountCalculateRV();
        } else {
            account = "";
            printtype = "bydate";
            datefrom = dtfromrv.getText().replace("/", "-");
            dateto = dttorv.getText().replace("/", "-");
            cbaccountrv.setEnabled(false);
            retrieveRVByDate();
            CountCalculateRV();

        }
    }//GEN-LAST:event_checkboxaccountrvActionPerformed

    private void dttomrOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dttomrOnSelectionChange
        // TODO add your handling code here:
    }//GEN-LAST:event_dttomrOnSelectionChange

    private void dttomrOnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_dttomrOnCommit
        printtype = "bydate";
        datefrom = dtfrommr.getText().replace("/", "-");
        dateto = dttomr.getText().replace("/", "-");
        retrieveMRByDate();
        CountCalculateMR();
    }//GEN-LAST:event_dttomrOnCommit

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void cbaccountmrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbaccountmrActionPerformed
        account = cbaccountmr.getSelectedItem().toString();
        datefrom = dtfrommr.getText().replace("/", "-");
        dateto = dttomr.getText().replace("/", "-");
        retrieveMRByDateAccount();
        CountCalculateMR();
    }//GEN-LAST:event_cbaccountmrActionPerformed

    private void checkboxaccountmrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxaccountmrActionPerformed
        if (checkboxaccountmr.isSelected()) {
            account = cbaccountmr.getSelectedItem().toString();
            datefrom = dtfrommr.getText().replace("/", "-");
            dateto = dttomr.getText().replace("/", "-");
            cbaccountmr.setEnabled(true);
            printtype = "byaccount";
            retrieveMRByDateAccount();
            CountCalculateMR();
        } else {
            account = "";
            printtype = "bydate";
            datefrom = dtfrommr.getText().replace("/", "-");
            dateto = dttomr.getText().replace("/", "-");
            cbaccountmr.setEnabled(false);
            retrieveMRByDate();
            CountCalculateMR();

        }
    }//GEN-LAST:event_checkboxaccountmrActionPerformed

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
            java.util.logging.Logger.getLogger(FrameReport.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameReport.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameReport.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameReport.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new FrameReport().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbaccountdv;
    private javax.swing.JComboBox cbaccountmr;
    private javax.swing.JComboBox cbaccountmw;
    private javax.swing.JComboBox cbaccountrv;
    private javax.swing.JComboBox cbchargedv;
    private javax.swing.JComboBox cbchargemw;
    private javax.swing.JCheckBox checkboxaccountdv;
    private javax.swing.JCheckBox checkboxaccountmr;
    private javax.swing.JCheckBox checkboxaccountmw;
    private javax.swing.JCheckBox checkboxaccountrv;
    private javax.swing.JCheckBox checkboxcompanydv;
    private javax.swing.JCheckBox checkboxcompanymw;
    private datechooser.beans.DateChooserCombo dtfromdv;
    private datechooser.beans.DateChooserCombo dtfrommr;
    private datechooser.beans.DateChooserCombo dtfrommw;
    private datechooser.beans.DateChooserCombo dtfromrv;
    private datechooser.beans.DateChooserCombo dttodv;
    private datechooser.beans.DateChooserCombo dttomr;
    private datechooser.beans.DateChooserCombo dttomw;
    private datechooser.beans.DateChooserCombo dttorv;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tabledv;
    private javax.swing.JTable tablemr;
    private javax.swing.JTable tablemw;
    private javax.swing.JTable tablerv;
    // End of variables declaration//GEN-END:variables
}
