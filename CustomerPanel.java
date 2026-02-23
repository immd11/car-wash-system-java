
package panels;

import carwash_service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {
    private CarWashSystem_con system;
    private JTextField emailField, cancelField;
    private JTable bookingTable;
    private DefaultTableModel tableModel;

    private Customer currentCustomer;

    public CustomerPanel(CarWashSystem_con system) {
        this.system = system;
        setLayout(new BorderLayout());

       
        JPanel loginPanel = new JPanel(new FlowLayout());
        emailField = new JTextField(20);
        JButton loginBtn = new JButton("Login");

        loginPanel.add(new JLabel("Enter Email:"));
        loginPanel.add(emailField);
        loginPanel.add(loginBtn);

        add(loginPanel, BorderLayout.NORTH);

        
        String[] columns = { "ID", "Package", "Date/Time", "Status" };
        tableModel = new DefaultTableModel(columns, 0);
        bookingTable = new JTable(tableModel);
        add(new JScrollPane(bookingTable), BorderLayout.CENTER);

 
        JPanel cancelPanel = new JPanel(new FlowLayout());
        cancelField = new JTextField(5);
        JButton cancelBtn = new JButton("Cancel Booking");

        cancelPanel.add(new JLabel("Appointment ID:"));
        cancelPanel.add(cancelField);
        cancelPanel.add(cancelBtn);

        add(cancelPanel, BorderLayout.SOUTH);

    
        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            currentCustomer = system.findCustomerByEmail(email);
            if (currentCustomer == null) {
                JOptionPane.showMessageDialog(this, " Customer not found. Please register.");
                return;
            }
            loadCustomerAppointments();
        });

     
   cancelBtn.addActionListener(e -> {
    if (currentCustomer == null) {
        JOptionPane.showMessageDialog(this, "Please log in first.");
        return;
    }

    try {
        int id = Integer.parseInt(cancelField.getText().trim());
        boolean success = system.cancelAppointment(id, currentCustomer);
        if (success) {
            JOptionPane.showMessageDialog(this, " Appointment cancelled.");
            loadCustomerAppointments();
        } else {
            JOptionPane.showMessageDialog(this, " Appointment not found or not yours.");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, " Error: " + ex.getMessage());
    }
});

    }

   public void loadCustomerAppointments() {
    if (currentCustomer == null) return;

    tableModel.setRowCount(0);
    List<Appointment> appointments = system.getAppointmentsByCustomer(currentCustomer);
    for (Appointment a : appointments) {
        tableModel.addRow(new Object[]{
            a.getAppointmentId(),
            a.getCarWashPackage().getName(),
            a.getDateTime().toString(),
            a.getStatus().trim()
        });
    }
}
public void setCurrentCustomer(Customer customer) {
    this.currentCustomer = customer;
    loadCustomerAppointments();
}

}
