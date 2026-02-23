package panels;

import carwash_service.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class BookingPanel extends JPanel {
    private CarWashSystem_con system;
    private CarWashSystemGUI gui;
    private AdminPanel adminPanel;
    private CustomerPanel customerPanel;

    private JComboBox<String> packageComboBox;
    private JTextField dateField;
    private JComboBox<String> timeComboBox;

    public BookingPanel(CarWashSystem_con system, CarWashSystemGUI gui, AdminPanel adminPanel, CustomerPanel customerPanel) {
        this.system = system;
        this.gui = gui;
        this.adminPanel = adminPanel;
        this.customerPanel = customerPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        List<CarWashPackage> packages = system.getPackages();

        packageComboBox = new JComboBox<>();
        for (CarWashPackage p : packages) {
            packageComboBox.addItem(p.getId() + " - " + p.getName() + " ($" + p.getPrice() + ")");
        }

        dateField = new JTextField(10);
        timeComboBox = new JComboBox<>(new String[] { "09:00","10:00", "11:00", "13:00", "14:00","15:00", "16:00","17:00","18:00","19:00","20:00" });
        JButton bookBtn = new JButton("Book Appointment");

        int row = 0;
        addField("Package:", packageComboBox, gbc, row++);
        addField("Date (yyyy-MM-dd):", dateField, gbc, row++);
        addField("Time:", timeComboBox, gbc, row++);
        gbc.gridx = 1;
        gbc.gridy = row;
        add(bookBtn, gbc);

        bookBtn.addActionListener(e -> {
            Customer customer = gui.getLoggedInCustomer();
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Please log in before booking.");
                return;
            }

            try {
                String dateStr = dateField.getText().trim();
                String timeStr = (String) timeComboBox.getSelectedItem();
                int pkgId = Integer.parseInt(((String) packageComboBox.getSelectedItem()).split(" - ")[0]);

                LocalDate date = LocalDate.parse(dateStr);
                LocalTime time = LocalTime.parse(timeStr);
                LocalDateTime dateTime = LocalDateTime.of(date, time);

                if (dateTime.isBefore(LocalDateTime.now())) {
                    JOptionPane.showMessageDialog(this, "Cannot book in the past.");
                    return;
                }

                CarWashPackage selected = packages.stream()
                        .filter(p -> p.getId() == pkgId)
                        .findFirst().orElse(null);

                if (selected != null) {
                    Appointment appointment = new Appointment(customer, selected, dateTime);
                    system.bookAppointment(appointment);
                    JOptionPane.showMessageDialog(this, "Appointment booked successfully!");

                    
                    adminPanel.loadAppointments();
                    customerPanel.setCurrentCustomer(customer);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    private void addField(String label, Component comp, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(comp, gbc);
    }
}
