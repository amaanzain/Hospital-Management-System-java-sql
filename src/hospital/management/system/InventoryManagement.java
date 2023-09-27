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

public class InventoryManagement extends javax.swing.JFrame {
    private JFrame frame;
    private JTextField itemNameField, itemQuantityField, itemPriceField;
    private JButton addButton, viewButton;
    private JTextArea inventoryTextArea;
    private Connection connection;

    public InventoryManagement() {
        // Initialize the database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/hospitalmanagementsystem", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create the main frame
        frame = new JFrame("Inventory Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Create GUI components
        itemNameField = new JTextField(20);
        itemQuantityField = new JTextField(5);
        itemPriceField = new JTextField(5);
        addButton = new JButton("Add Item");
        viewButton = new JButton("View Inventory");
        inventoryTextArea = new JTextArea(10, 30);
        inventoryTextArea.setEditable(false);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemToInventory();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewInventory();
            }
        });

        // Create a panel for input components
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Item Name:"));
        inputPanel.add(itemNameField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(itemQuantityField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(itemPriceField);
        inputPanel.add(addButton);
        inputPanel.add(viewButton);

        // Create a panel for displaying inventory
        JScrollPane scrollPane = new JScrollPane(inventoryTextArea);

        // Add components to the frame
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void addItemToInventory() {
        String name = itemNameField.getText();
        int quantity = Integer.parseInt(itemQuantityField.getText());
        double price = Double.parseDouble(itemPriceField.getText());

        String sql = "INSERT INTO inventory (name, quantity, price) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, quantity);
            statement.setDouble(3, price);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Item added to inventory.");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewInventory() {
        StringBuilder inventoryText = new StringBuilder();
        String sql = "SELECT * FROM inventory";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                inventoryText.append("ID: ").append(id).append(", Name: ").append(name)
                        .append(", Quantity: ").append(quantity).append(", Price: ").append(price).append("\n");
            }
            inventoryTextArea.setText(inventoryText.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        itemNameField.setText("");
        itemQuantityField.setText("");
        itemPriceField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventoryManagement();
            }
        });
    }
}
