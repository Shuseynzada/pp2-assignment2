package GUI;

import javax.swing.*;
import Exceptions.*;
import UserManagement.User;

public class LoginPage {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public LoginPage() {
        frame = new JFrame("Movie Program");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        statusLabel = new JLabel();
        setupUI();
    }

    private void setupUI() {
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel l1 = new JLabel("Welcome! Login");
        l1.setBounds(50, 0, 100, 30);

        JLabel l2 = new JLabel("Username");
        l2.setBounds(50, 30, 100, 30);
        usernameField.setBounds(50, 50, 220, 30);

        JLabel l3 = new JLabel("Password");
        l3.setBounds(50, 80, 100, 30);
        passwordField.setBounds(50, 100, 220, 30);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 130, 100, 30);
        loginButton.addActionListener(e -> login());

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(170, 130, 100, 30);
        signupButton.addActionListener(e -> signUp());

        statusLabel.setBounds(50, 160, 300, 30);

        frame.add(l1);
        frame.add(l2);
        frame.add(l3);
        frame.add(loginButton);
        frame.add(signupButton);
        frame.add(usernameField);
        frame.add(passwordField);
        frame.add(statusLabel);

        frame.setVisible(true);
    }

    private void login() {
        String password = new String(passwordField.getPassword());
        try {
            User user = User.login(usernameField.getText(), password);
            frame.dispose();
            new MoviePage(user);
        } catch (IncorrectPasswordException | UserNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signUp() {
        String password = new String(passwordField.getPassword());
        try {
            User newUser = User.register(usernameField.getText(), password);
            if (newUser != null) {
                frame.dispose();
                new MoviePage(newUser);
            } else {
                statusLabel.setText("Registration failed");
            }
        } catch (UsernameAlreadyExistsException | InvalidPasswordDigitException | InvalidPasswordLengthException
                | InvalidPasswordUppercaseException | InvalidUsernameException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
