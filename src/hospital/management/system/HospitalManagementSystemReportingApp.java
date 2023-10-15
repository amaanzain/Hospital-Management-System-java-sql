/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital.management.system;

/**
 *
 * @author zaina
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HospitalManagementSystemReportingApp extends javax.swing.JFrame{
    private JFrame frame;
    private JTextArea reportTextArea;
    private JButton patientDemographicsButton, appointmentHistoryButton, inventoryStatusButton;
    private Connection connection;

    public HospitalManagementSystemReportingApp() {
        initialize();
        connectToDatabase();
    }

    private void initialize() {
        frame = new JFrame("Hospital Management System Reporting");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        patientDemographicsButton = new JButton("Patient Demographics");
        appointmentHistoryButton = new JButton("Appointment History");
        inventoryStatusButton = new JButton("Inventory Status");
        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        panel.add(patientDemographicsButton);
        panel.add(appointmentHistoryButton);
        panel.add(inventoryStatusButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(reportTextArea), BorderLayout.CENTER);

        patientDemographicsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generatePatientDemographicsReport();
            }
        });

        appointmentHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateAppointmentHistoryReport();
            }
        });

        inventoryStatusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateInventoryStatusReport();
            }
        });

        frame.setVisible(true);
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/hospitalmanagementsystem", "root", "1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generatePatientDemographicsReport() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM patientreport");

            StringBuilder report = new StringBuilder("Patient Demographics Report:\n");

            while (resultSet.next()) {
                report.append(resultSet.getString("patientID")).append(" - ").append(resultSet.getString("diagnosis")).append("\n");
            }

            reportTextArea.setText(report.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateAppointmentHistoryReport() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM appointments");

            StringBuilder report = new StringBuilder("Appointment History Report:\n");

            while (resultSet.next()) {
                report.append(resultSet.getString("appointment_id")).append(" - ").append(resultSet.getString("appointment_date")).append("\n");
            }

            reportTextArea.setText(report.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateInventoryStatusReport() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM inventory");

            StringBuilder report = new StringBuilder("Inventory Status Report:\n");

            while (resultSet.next()) {
                report.append(resultSet.getString("name")).append(" - ").append(resultSet.getString("quantity")).append("\n");
            }

            reportTextArea.setText(report.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HospitalManagementSystemReportingApp();
        });
    }
}

