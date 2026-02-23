package carwash_service;
import java.time.LocalDateTime;

public class Appointment {
    private static int nextId = 1;
    private int appointmentId;
    private Customer customer;
    private CarWashPackage carWashPackage;
    private LocalDateTime dateTime;
    private String status;

    
    public Appointment(int appointmentId, Customer customer, CarWashPackage carWashPackage, LocalDateTime dateTime) {
        this.appointmentId = appointmentId;
        this.customer = customer;
        this.carWashPackage = carWashPackage;
        this.dateTime = dateTime;
        this.status = "Scheduled";
    }

   
    public Appointment(Customer customer, CarWashPackage carWashPackage, LocalDateTime dateTime) {
        this.appointmentId = nextId++;
        this.customer = customer;
        this.carWashPackage = carWashPackage;
        this.dateTime = dateTime;
        this.status = "Scheduled";
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public CarWashPackage getCarWashPackage() {
        return carWashPackage;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void cancel() {
        this.status = "Cancelled";
    }

    public void reschedule(LocalDateTime newDateTime) {
        this.dateTime = newDateTime;
    }

    public void displayAppointment() {
        System.out.println("Appointment #" + appointmentId);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Package: " + carWashPackage.getName());
        System.out.println("Date/Time: " + dateTime);
        System.out.println("Status: " + status);
        System.out.println("---------------------------");
    }
}
