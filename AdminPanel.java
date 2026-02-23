package panels;

import carwash_service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class AdminPanel extends JPanel {
    private CarWashSystem_con system;
    private Admin loggedInAdmin;

    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    
    private JTextField emailField;
    private JButton loginButton;

    
    private JTextField rescheduleIdField, rescheduleDateField;
    private JTextField cancelIdField;
    private JButton rescheduleBtn, cancelBtn;

    private JPanel loginPanel, adminUI;

    public AdminPanel(CarWashSystem_con system) {
        this.system = system;
        setLayout(new BorderLayout());

        initLoginPanel();
        initAdminPanel();

        add(loginPanel, BorderLayout.NORTH);
    }

    private void initLoginPanel() {
        loginPanel = new JPanel(new FlowLayout());
        emailField = new JTextField(20);
        loginButton = new JButton("Login");

        loginPanel.add(new JLabel("Admin Email:"));
        loginPanel.add(emailField);
        loginPanel.add(loginButton);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            Admin admin = system.findAdminByEmail(email);

            if (admin != null) {
                loggedInAdmin = admin;
                remove(loginPanel);
                add(adminUI, BorderLayout.CENTER);
                revalidate();
                repaint();
                loadAppointments();
                JOptionPane.showMessageDialog(this, " Welcome, " + admin.getName() + "!");
            } else {
                JOptionPane.showMessageDialog(this, " Invalid admin email.");
            }
        });
    }

    private void initAdminPanel() {
        adminUI = new JPanel(new BorderLayout());

 
        String[] columns = { "ID", "Customer", "Package", "Date/Time", "Status" };
        tableModel = new DefaultTableModel(columns, 0);
        appointmentTable = new JTable(tableModel);
        adminUI.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

    
        JPanel controlPanel = new JPanel(new GridLayout(2, 1));


        JPanel reschedulePanel = new JPanel(new FlowLayout());
        rescheduleIdField = new JTextField(5);
        rescheduleDateField = new JTextField(12);
        rescheduleBtn = new JButton("Reschedule");

        reschedulePanel.add(new JLabel("Appointment ID:"));
        reschedulePanel.add(rescheduleIdField);
        reschedulePanel.add(new JLabel("New Date/Time (yyyy-MM-ddTHH:mm):"));
        reschedulePanel.add(rescheduleDateField);
        reschedulePanel.add(rescheduleBtn);

    
        JPanel cancelPanel = new JPanel(new FlowLayout());
        cancelIdField = new JTextField(5);
        cancelBtn = new JButton("Cancel Appointment");

        cancelPanel.add(new JLabel("Cancel Appointment ID:"));
        cancelPanel.add(cancelIdField);
        cancelPanel.add(cancelBtn);

        controlPanel.add(reschedulePanel);
        controlPanel.add(cancelPanel);

        adminUI.add(controlPanel, BorderLayout.SOUTH);

      
        rescheduleBtn.addActionListener(e -> {
            try {
                int apptId = Integer.parseInt(rescheduleIdField.getText().trim());
                LocalDateTime newDateTime = LocalDateTime.parse(rescheduleDateField.getText().trim());

                if (newDateTime.isBefore(LocalDateTime.now())) {
                    JOptionPane.showMessageDialog(this, "Cannot reschedule to a past time.");
                    return;
                }

                boolean success = system.rescheduleAppointment(apptId, newDateTime);
                if (success) {
                    JOptionPane.showMessageDialog(this, " Appointment rescheduled.");
                    loadAppointments();
                } else {
                    JOptionPane.showMessageDialog(this, " Appointment not found.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error: " + ex.getMessage());
            }
        });

      
        cancelBtn.addActionListener(e -> {
            try {
                int apptId = Integer.parseInt(cancelIdField.getText().trim());
                boolean success = system.cancelAppointmentByAdmin(apptId);
                if (success) {
                    JOptionPane.showMessageDialog(this, " Appointment cancelled.");
                    loadAppointments();
                } else {
                    JOptionPane.showMessageDialog(this, " Appointment not found.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error: " + ex.getMessage());
            }
        });
    }

    public void loadAppointments() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = system.getAllAppointments();
        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{
                a.getAppointmentId(),
                a.getCustomer().getName(),
                a.getCarWashPackage().getName(),
                a.getDateTime().toString(),
                a.getStatus().trim()
            });
        }
    }
}
