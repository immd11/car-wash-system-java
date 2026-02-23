package carwash_service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CarWashSystem_con {

    public void addCustomer(Customer customer) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (user_id, name, email, role, password) VALUES (?, ?, ?, 'customer', '')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customer.getId());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getEmail());
            stmt.executeUpdate();
            System.out.println(" Customer inserted into DB: " + customer.getEmail());
        } catch (SQLException e) {
            System.out.println(" Error adding customer: " + e.getMessage());
        }
    }

    public Customer findCustomerByEmail(String email) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ? AND role = 'customer'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(rs.getString("user_id"), rs.getString("name"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error finding customer: " + e.getMessage());
        }
        return null;
    }

    public Admin findAdminByEmail(String email) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ? AND role = 'admin'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin(rs.getString("user_id"), rs.getString("name"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error finding admin: " + e.getMessage());
        }
        return null;
    }
public boolean cancelAppointmentByAdmin(int appointmentId) {
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, appointmentId);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("⚠️ Error cancelling appointment: " + e.getMessage());
        return false;
    }
}

    public List<CarWashPackage> getPackages() {
        List<CarWashPackage> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM car_wash_packages";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new CarWashPackage(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error loading packages: " + e.getMessage());
        }
        return list;
    }

    public void bookAppointment(Appointment appt) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO appointments (customer_id, package_id, date_time) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, appt.getCustomer().getId());
            stmt.setInt(2, appt.getCarWashPackage().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(appt.getDateTime()));
            stmt.executeUpdate();
            System.out.println(" Appointment booked successfully!");
        } catch (SQLException e) {
            System.out.println("⚠ Error booking appointment: " + e.getMessage());
        }
    }

    public boolean cancelAppointment(int apptId, Customer customer) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM appointments WHERE appointment_id = ? AND customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, apptId);
            stmt.setString(2, customer.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("⚠️ Error canceling appointment: " + e.getMessage());
            return false;
        }
    }

    public List<Appointment> getAppointmentsByCustomer(Customer customer) {
        List<Appointment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT a.*, p.name, p.price FROM appointments a JOIN car_wash_packages p ON a.package_id = p.id WHERE a.customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customer.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CarWashPackage carWashPackage = new CarWashPackage(
                        rs.getInt("package_id"),
                        rs.getString("name"),
                        "",
                        rs.getDouble("price")
                );
                LocalDateTime date = rs.getTimestamp("date_time").toLocalDateTime();
                list.add(new Appointment(rs.getInt("appointment_id"), customer, carWashPackage, date));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error loading appointments: " + e.getMessage());
        }
        return list;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT a.*, u.name AS customer_name, u.email, p.name AS pkg_name, p.price FROM appointments a JOIN users u ON a.customer_id = u.user_id JOIN car_wash_packages p ON a.package_id = p.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Customer c = new Customer(
                        rs.getString("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("email")
                );
                CarWashPackage carWashPackage = new CarWashPackage(
                        rs.getInt("package_id"),
                        rs.getString("pkg_name"),
                        "",
                        rs.getDouble("price")
                );
                LocalDateTime date = rs.getTimestamp("date_time").toLocalDateTime();
                list.add(new Appointment(rs.getInt("appointment_id"), c, carWashPackage, date));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error loading all appointments: " + e.getMessage());
        }
        return list;
    }

    public boolean rescheduleAppointment(int apptId, LocalDateTime newDateTime) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE appointments SET date_time = ? WHERE appointment_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, Timestamp.valueOf(newDateTime));
            stmt.setInt(2, apptId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("⚠️ Error rescheduling: " + e.getMessage());
            return false;
        }
    }

    public void addRating(int rating) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO ratings (customer_id, rating) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "CUST-ANON");
            stmt.setInt(2, rating);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("⚠️ Error adding rating: " + e.getMessage());
        }
    }

    public double getAverageRating() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT AVG(rating) AS avg_rating FROM ratings";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error calculating average rating: " + e.getMessage());
        }
        return 0;
    }
}
