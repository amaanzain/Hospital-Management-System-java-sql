/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hospital.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HospitalManagementApp extends javax.swing.JFrame{
    private JFrame frame;
    private JTextField doctorNameField;
    private JTextField patientNameField;
    private JTextField appointmentDateField;
    private JButton requestAppointmentButton;
    private JTextArea appointmentListArea;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost/hospitalmanagementsystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public HospitalManagementApp() {
        frame = new JFrame("Hospital Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 700);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel doctorLabel = new JLabel("Doctor Name:");
        doctorNameField = new JTextField(20);

        JLabel patientLabel = new JLabel("Patient Name:");
        patientNameField = new JTextField(20);

        JLabel appointmentDateLabel = new JLabel("Appointment Date (yyyy-MM-dd HH:mm):");
        appointmentDateField = new JTextField(20);

        requestAppointmentButton = new JButton("Request Appointment");

        appointmentListArea = new JTextArea(10, 30);
        appointmentListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(appointmentListArea);

        panel.add(doctorLabel);
        panel.add(doctorNameField);
        panel.add(patientLabel);
        panel.add(patientNameField);
        panel.add(appointmentDateLabel);
        panel.add(appointmentDateField);
        panel.add(requestAppointmentButton);
        panel.add(scrollPane);

        requestAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                requestAppointment();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void requestAppointment() {
        String doctorName = doctorNameField.getText();
        String patientName = patientNameField.getText();
        String appointmentDateStr = appointmentDateField.getText();

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Check if the doctor exists in the database
            int doctorId = getDoctorIdByName(doctorName, connection);

            // Check if the patient exists in the database
            int patientId = getPatientIdByName(patientName, connection);

            // Parse the appointment date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date appointmentDate = dateFormat.parse(appointmentDateStr);

            // Check for scheduling conflicts
            if (isDoctorAvailable(doctorId, appointmentDate, connection)) {
                // Insert the appointment into the database
                insertAppointment(doctorId, patientId, appointmentDate, connection);
                JOptionPane.showMessageDialog(frame, "Appointment requested successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Doctor is not available at that time.");
            }

            // Clear input fields
            doctorNameField.setText("");
            patientNameField.setText("");
            appointmentDateField.setText("");

            // Refresh the appointment list
            displayAppointments(connection);

            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error requesting appointment: " + ex.getMessage());
        }
    }

    private int getDoctorIdByName(String doctorName, Connection connection) throws SQLException {
        // Implement a method to retrieve the doctor's ID by name from the database
        String sql = "SELECT doctor_id FROM doctors WHERE doctor_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, doctorName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("doctor_id");
        } else {
            throw new SQLException("Doctor not found: " + doctorName);
        }
    }

    private int getPatientIdByName(String patientName, Connection connection) throws SQLException {
        // Implement a method to retrieve the patient's ID by name from the database
        String sql = "SELECT patient_id FROM patients WHERE patient_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, patientName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("patient_id");
        } else {
            throw new SQLException("Patient not found: " + patientName);
        }
    }

    private boolean isDoctorAvailable(int doctorId, Date appointmentDate, Connection connection) throws SQLException {
        // Implement a method to check if the doctor is available at the given date and time
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, doctorId);
        preparedStatement.setTimestamp(2, new java.sql.Timestamp(appointmentDate.getTime()));
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count == 0; // Doctor is available if no appointments exist at that time
        }
        return false;
    }

    private void insertAppointment(int doctorId, int patientId, Date appointmentDate, Connection connection) throws SQLException {
        // Implement a method to insert the appointment into the database
        String sql = "INSERT INTO appointments (doctor_id, patient_id, appointment_date) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, doctorId);
        preparedStatement.setInt(2, patientId);
        preparedStatement.setTimestamp(3, new java.sql.Timestamp(appointmentDate.getTime()));
        preparedStatement.executeUpdate();
    }

    private void displayAppointments(Connection connection) throws SQLException {
        // Implement a method to display existing appointments in the JTextArea
        String sql = "SELECT d.doctor_name, p.patient_name, a.appointment_date " +
                     "FROM appointments a " +
                     "INNER JOIN doctors d ON a.doctor_id = d.doctor_id " +
                     "INNER JOIN patients p ON a.patient_id = p.patient_id " +
                     "ORDER BY a.appointment_date";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        StringBuilder appointmentsText = new StringBuilder("Appointments:\n");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        while (resultSet.next()) {
            String doctorName = resultSet.getString("doctor_name");
            String patientName = resultSet.getString("patient_name");
            String appointmentDate = dateFormat.format(resultSet.getTimestamp("appointment_date"));
            appointmentsText.append(doctorName).append(" - ").append(patientName).append(" - ").append(appointmentDate).append("\n");
        }
        appointmentListArea.setText(appointmentsText.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HospitalManagementApp();
            }
        });
    }
}
