package panels;

import carwash_service.*;

import javax.swing.*;
import java.awt.*;

public class CarWashSystemGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private CarWashSystem_con system;

    private Customer loggedInCustomer;

    private AdminPanel adminPanel;
    private CustomerPanel customerPanel;
    private BookingPanel bookingPanel;

    public CarWashSystemGUI() {
        setTitle("Car Wash System");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        system = new CarWashSystem_con();

       
        adminPanel = new AdminPanel(system);
        customerPanel = new CustomerPanel(system);
        bookingPanel = new BookingPanel(system, this, adminPanel, customerPanel);

    
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton homeBtn = new JButton("Home");
        JButton bookBtn = new JButton("Book Appointment");
        JButton adminBtn = new JButton("Admin ");
        JButton customerBtn = new JButton("Customer ");

        navPanel.add(homeBtn);
        navPanel.add(bookBtn);
        navPanel.add(adminBtn);
        navPanel.add(customerBtn);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

      
        mainPanel.add(new WelcomePanel(system,
            (customer) -> {
                setLoggedInCustomer(customer);
                cardLayout.show(mainPanel, "Customer");
            },
            () -> cardLayout.show(mainPanel, "Register")
        ), "Welcome");

        mainPanel.add(new HomePanel(system), "Home");
        mainPanel.add(bookingPanel, "Book");
        mainPanel.add(adminPanel, "Admin");
        mainPanel.add(customerPanel, "Customer");
        mainPanel.add(new RegisterPanel(system), "Register");

       
        homeBtn.addActionListener(e -> {
            if (loggedInCustomer == null) {
                cardLayout.show(mainPanel, "Welcome");
            } else {
                cardLayout.show(mainPanel, "Home");
            }
        });

        bookBtn.addActionListener(e -> {
            if (loggedInCustomer == null) {
                JOptionPane.showMessageDialog(this, " Please log in to book appointment.");
                cardLayout.show(mainPanel, "Welcome");
            } else {
                cardLayout.show(mainPanel, "Book");
            }
        });

        adminBtn.addActionListener(e -> cardLayout.show(mainPanel, "Admin"));

        customerBtn.addActionListener(e -> {
            if (loggedInCustomer == null) {
                JOptionPane.showMessageDialog(this, "⚠ Please log in first.");
                cardLayout.show(mainPanel, "Welcome");
            } else {
                cardLayout.show(mainPanel, "Customer");
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(navPanel, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        
        cardLayout.show(mainPanel, "Welcome");
    }

    public Customer getLoggedInCustomer() {
        return loggedInCustomer;
    }

    public void setLoggedInCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        customerPanel.setCurrentCustomer(customer);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarWashSystemGUI().setVisible(true));
    }
}
