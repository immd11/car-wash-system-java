package panels;

import carwash_service.*;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private JTextField nameField, emailField;
    private JButton registerButton, backButton;
    private CarWashSystem_con system;

    public RegisterPanel(CarWashSystem_con system) {
        this.system = system;
        setLayout(new BorderLayout());

       
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/welcome_bg.png"));
        JLabel backgroundLabel = new JLabel(icon);
        backgroundLabel.setLayout(new GridBagLayout());
        add(backgroundLabel, BorderLayout.CENTER);

       
        JPanel overlay = new JPanel();
        overlay.setOpaque(false);
        overlay.setLayout(new BoxLayout(overlay, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(" Register New Customer");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField(20);
        nameField.setMaximumSize(new Dimension(250, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(250, 30));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton = new JButton("Register");
        backButton = new JButton("Back");
        styleButton(registerButton);
        styleButton(backButton);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        overlay.add(title);
        overlay.add(Box.createVerticalStrut(20));
        overlay.add(nameLabel);
        overlay.add(nameField);
        overlay.add(Box.createVerticalStrut(10));
        overlay.add(emailLabel);
        overlay.add(emailField);
        overlay.add(Box.createVerticalStrut(15));
        overlay.add(buttonPanel);

        backgroundLabel.add(overlay);

     
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, " Please fill all fields.");
                return;
            }

            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(this, " Invalid email format.");
                return;
            }

            if (system.findCustomerByEmail(email) != null) {
                JOptionPane.showMessageDialog(this, " Email already registered.");
                return;
            }

            String id = "CUST" + System.currentTimeMillis();
            Customer customer = new Customer(id, name, email);
            system.addCustomer(customer);
            JOptionPane.showMessageDialog(this, " Registered successfully!");
        });

    
        backButton.addActionListener(e -> {
            Container parent = this.getParent();
            if (parent instanceof JPanel panel && panel.getLayout() instanceof CardLayout layout) {
                layout.show(panel, "Welcome");
            }
        });
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
