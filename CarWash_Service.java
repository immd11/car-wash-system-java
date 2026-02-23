package carwash_service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class CarWash_Service {
    public static void main(String[] args) {
        CarWashSystem_con system = new CarWashSystem_con(); 
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println(" Welcome to the Car Wash System 🚿");

        while (running) {
            System.out.println("\n1. Login as Customer");
            System.out.println("2. Login as Admin");
            System.out.println("3. Register as Customer");
            System.out.println("4. Exit");

            try {
                System.out.print("Choose an option: ");
                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1 -> loginCustomer(system, scanner);
                    case 2 -> loginAdmin(system, scanner);
                    case 3 -> registerCustomer(system, scanner);
                    case 4 -> {
                        running = false;
                        System.out.println("Goodbye!");
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Please enter a valid number.");
            }
        }

        scanner.close();
    }

    private static void loginCustomer(CarWashSystem_con system, Scanner scanner) {
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        Customer customer = system.findCustomerByEmail(email);

        if (customer != null) {
            boolean loggedIn = true;
            while (loggedIn) {
                customer.showMenu();

                try {
                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {
                        case 1 -> {
                            List<CarWashPackage> packages = system.getPackages();
                            System.out.println("Available Packages:");
                            for (CarWashPackage pkg : packages) {
                                pkg.displayPackage();
                            }

                            try {
                                System.out.print("Choose package ID: ");
                                int pkgId = Integer.parseInt(scanner.nextLine());

                                CarWashPackage selected = packages.stream()
                                        .filter(p -> p.getId() == pkgId)
                                        .findFirst().orElse(null);

                                if (selected != null) {
                                    System.out.print("Enter date (yyyy-MM-dd): ");
                                    String dateInput = scanner.nextLine();

                                    try {
                                        LocalDate date = LocalDate.parse(dateInput);

                                        String[] availableTimes = { "09:00", "11:00", "13:00", "15:00", "17:00" };
                                        System.out.println("Available Times:");
                                        for (int i = 0; i < availableTimes.length; i++) {
                                            System.out.println((i + 1) + ". " + availableTimes[i]);
                                        }

                                        System.out.print("Choose a time option (1-" + availableTimes.length + "): ");
                                        int timeOption = Integer.parseInt(scanner.nextLine());

                                        if (timeOption >= 1 && timeOption <= availableTimes.length) {
                                            String time = availableTimes[timeOption - 1];
                                            LocalTime localTime = LocalTime.parse(time);
                                            LocalDateTime appointmentDateTime = LocalDateTime.of(date, localTime);

                                            if (appointmentDateTime.isBefore(LocalDateTime.now())) {
                                                System.out.println("⚠️ Cannot book an appointment in the past.");
                                            } else {
                                                system.bookAppointment(new Appointment(customer, selected, appointmentDateTime));
                                            }
                                        } else {
                                            System.out.println("⚠️ Invalid time selection.");
                                        }

                                    } catch (DateTimeParseException e) {
                                        System.out.println("⚠️ Invalid date format. Please use: yyyy-MM-dd");
                                    } catch (NumberFormatException e) {
                                        System.out.println("⚠️ Please enter a valid number for time selection.");
                                    }

                                } else {
                                    System.out.println("⚠️ Invalid package ID.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("⚠️ Please enter a valid number for package ID.");
                            }
                        }

                        case 2 -> {
                            List<Appointment> myAppointments = system.getAppointmentsByCustomer(customer);
                            if (myAppointments.isEmpty()) {
                                System.out.println("No appointments found.");
                            } else {
                                for (Appointment a : myAppointments) a.displayAppointment();
                            }
                        }

                        case 3 -> {
                            try {
                                System.out.print("Enter Appointment ID to cancel: ");
                                int apptId = Integer.parseInt(scanner.nextLine());

                                if (system.cancelAppointment(apptId, customer)) {
                                    System.out.println(" Appointment cancelled.");
                                } else {
                                    System.out.println("⚠️ Appointment not found or cannot be cancelled.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("⚠️ Please enter a valid numeric Appointment ID.");
                            }
                        }

                        case 4 -> {
                            System.out.print("Rate the program (1–5): ");
                            try {
                                int rating = Integer.parseInt(scanner.nextLine());
                                if (rating >= 1 && rating <= 5) {
                                    system.addRating(rating);
                                    System.out.println(" Thank you for rating us " + rating + " star(s)!");
                                } else {
                                    System.out.println("⚠️ Rating must be between 1 and 5.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("⚠️ Please enter a number between 1 and 5.");
                            }
                        }

                        case 5 -> loggedIn = false;
                        default -> System.out.println("Invalid choice.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Please enter a valid menu option.");
                }
            }
        } else {
            System.out.println("⚠️ Customer not found. Please check your email or register.");
        }
    }

    private static void loginAdmin(CarWashSystem_con system, Scanner scanner) {
        System.out.print("Enter Admin Email: ");
        String email = scanner.nextLine();
        Admin admin = system.findAdminByEmail(email);

        if (admin != null) {
            boolean adminLoggedIn = true;
            while (adminLoggedIn) {
                admin.showMenu();

                try {
                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {
                        case 1 -> {
                            List<Appointment> all = system.getAllAppointments();
                            if (all.isEmpty()) {
                                System.out.println("No appointments found.");
                            } else {
                                for (Appointment a : all) a.displayAppointment();
                            }
                        }

                        case 2 -> {
                            try {
                                System.out.print("Enter Appointment ID to reschedule: ");
                                int apptId = Integer.parseInt(scanner.nextLine());
                                System.out.print("Enter new date/time (yyyy-MM-ddTHH:mm): ");
                                LocalDateTime newDate = LocalDateTime.parse(scanner.nextLine());

                                if (newDate.isBefore(LocalDateTime.now())) {
                                    System.out.println(" Cannot reschedule to a past date/time.");
                                } else if (system.rescheduleAppointment(apptId, newDate)) {
                                    System.out.println(" Appointment rescheduled.");
                                } else {
                                    System.out.println(" Appointment not found.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(" Invalid ID. Must be a number.");
                            } catch (Exception e) {
                                System.out.println(" Invalid date format. Use: yyyy-MM-ddTHH:mm");
                            }
                        }

                        case 3 -> System.out.println(" Manage Customer Info ");
                        case 4 -> {
                            double avg = system.getAverageRating();
                            if (avg == 0) {
                                System.out.println(" No ratings submitted yet.");
                            } else {
                                System.out.printf(" Average Customer Rating: %.2f / 5\n", avg);
                            }
                        }

                        case 5 -> adminLoggedIn = false;
                        default -> System.out.println("Invalid option.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Please enter a valid number.");
                }
            }
        } else {
            System.out.println("⚠️ Admin not found.");
        }
    }

    private static void registerCustomer(CarWashSystem_con system, Scanner scanner) {
        String id = "CUST" + System.currentTimeMillis();

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        String email;
        while (true) {
            System.out.print("Enter Email: ");
            email = scanner.nextLine();

            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                System.out.println("⚠️ Invalid email format. Please enter a valid email (e.g., name@example.com).");
            } else if (system.findCustomerByEmail(email) != null) {
                System.out.println("⚠️ This email is already registered. Please use a different one.");
            } else {
                break;
            }
        }

        Customer customer = new Customer(id, name, email);
        system.addCustomer(customer);
        System.out.println(" Registered successfully!");
        System.out.println(" You can now log in using your email: " + email);
    }
}
