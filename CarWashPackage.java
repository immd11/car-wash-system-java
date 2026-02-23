
package carwash_service;
public class CarWashPackage {
    private int id;
    private String name;
    private String description;
    private double price;

    public CarWashPackage(int id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void displayPackage() {
        System.out.println("[" + id + "] " + name + " - " + description + " ($" + price + ")");
    }
}
