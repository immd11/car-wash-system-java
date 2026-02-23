
package carwash_service;
public class Admin extends User {

    public Admin(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public void showMenu() {
        System.out.println("Admin Panel - Welcome " + name + ":");
        System.out.println("1. View All Bookings");
        System.out.println("2. Reschedule Booking");
        System.out.println("3. Manage Customer Information");
        System.out.println("4. View Customer Ratings");
        System.out.println("5. Logout");
    }
}
