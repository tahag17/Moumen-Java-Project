package login;

import database.PostgreSQLConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginComponent {
    private boolean authenticated;
    // Method for authentication
    public static boolean authenticate(String username, String password) {
        String query = "SELECT password FROM users WHERE username = ?";

        try (Connection connection = new PostgreSQLConnection().connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return storedPassword.equals(password);
            } else {
                return false; // User not found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Connection error
        }
    }

    // Method for account creation
    public static boolean createAccount(String username, String password,String firstname, String lastname, String birthDate) {
        String insertQuery = "INSERT INTO users (username, password,firstname,lastname,birthdate) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = new PostgreSQLConnection().connect();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstname);
            preparedStatement.setString(4, lastname);
            preparedStatement.setString(5, birthDate);

            preparedStatement.executeUpdate();
            return true; // Account created successfully

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred
        }
    }

    // Login dialog with both login and account creation
    public static void showLoginDialog() {
        JDialog loginDialog = new JDialog();
        loginDialog.setSize(300, 200);
        loginDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);

        loginDialog.add(panel, BorderLayout.CENTER);
        loginDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Login button action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticate(username, password)) {
                FrameM frame = new FrameM();
                JOptionPane.showMessageDialog(loginDialog, "Login successful!");
                loginDialog.dispose();
                frame.setMainFrame();
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Create Account button action
        createAccountButton.addActionListener(e -> {

            showCreateAccountDialog(); // Show the account creation dialog
            loginDialog.dispose();
        });

        loginDialog.setLocationRelativeTo(null);
        loginDialog.setVisible(true);
    }

    // Dialog for account creation
    public static void showCreateAccountDialog() {
        JDialog createAccountDialog = new JDialog();
        createAccountDialog.setSize(400, 300);
        createAccountDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        JLabel firstnameLabel = new JLabel("First name:");
        JTextField firstnameField = new JTextField();
        JLabel lastnameLabel = new JLabel("Last name:");
        JTextField lastnameField = new JTextField();
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);

        // Set the editor to display date in a user-friendly format
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        // Add the spinner to the frame

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        panel.add(firstnameLabel);
        panel.add(firstnameField);
        panel.add(lastnameLabel);
        panel.add(lastnameField);
        panel.add(new JLabel("Select your birth date:"));
        panel.add(dateSpinner);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        JButton createButton = new JButton("Create");
        JButton cancel = new JButton("Authenticate");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton);
        buttonPanel.add(cancel);

        createAccountDialog.add(panel, BorderLayout.CENTER);
        createAccountDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Create button action
        createButton.addActionListener(e -> {
            String firstname = firstnameField.getText();
            String lastname = lastnameField.getText();
            String date = String.valueOf(dateSpinner.getValue());
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (createAccount(username, password,firstname,lastname,date)) {
                JOptionPane.showMessageDialog(createAccountDialog, "Account created successfully!");
                showLoginDialog();
                createAccountDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(createAccountDialog, "Error creating account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel button action
        cancel.addActionListener(e -> showLoginDialog());

        createAccountDialog.setLocationRelativeTo(null);
        createAccountDialog.setVisible(true);
    }

}
