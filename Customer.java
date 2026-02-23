
package carwash_service;


public class Customer extends User {
    
    public Customer(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public void showMenu() {
        System.out.println("Welcome, " + name + "! Please choose an option:");
        System.out.println("1. Book Appointment");
        System.out.println("2. View My Bookings");
        System.out.println("3. Cancel Booking");
        System.out.println("4. Rate the Program");
        System.out.println("5. Logout");
    }
}
