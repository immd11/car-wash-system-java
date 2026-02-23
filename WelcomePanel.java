package panels;

import carwash_service.*;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class WelcomePanel extends JPanel {
    private JTextField emailField;
    private JButton loginButton, registerButton;
    private CarWashSystem_con system;

    public WelcomePanel(CarWashSystem_con system, Consumer<Customer> onLoginSuccess, Runnable onRegisterClick) {
        this.system = system;
        setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/welcome_bg.png"));
        JLabel backgroundLabel = new JLabel(icon);
        backgroundLabel.setLayout(new GridBagLayout()); 
        add(backgroundLabel, BorderLayout.CENTER);

        JPanel overlayPanel = new JPanel();
        overlayPanel.setOpaque(false);
        overlayPanel.setLayout(new BoxLayout(overlayPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(" Welcome to the Car Wash Service ");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(250, 30));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        styleButton(loginButton);
        styleButton(registerButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        overlayPanel.add(title);
        overlayPanel.add(Box.createVerticalStrut(20));
        overlayPanel.add(emailLabel);
        overlayPanel.add(emailField);
        overlayPanel.add(Box.createVerticalStrut(15));
        overlayPanel.add(buttonPanel);

        backgroundLabel.add(overlayPanel);

        
        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            Customer customer = system.findCustomerByEmail(email);
            if (customer != null) {
                onLoginSuccess.accept(customer);
                JOptionPane.showMessageDialog(this, " Logged in as: " + customer.getName());
            } else {
                JOptionPane.showMessageDialog(this, " Customer not found. Try registering.");
            }
        });

        registerButton.addActionListener(e -> onRegisterClick.run());
    }

    private void styleButton(JButton button) {
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(110, 35));
    }
}
