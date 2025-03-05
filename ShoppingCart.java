/*
    Samarth Patel and shp5246.
    I declare that what has been written in this work has been written by me and that no
    part has been copied from scientific publications, the Internet or from other online
    sources or was already presented in the academic field by me or by other students.
*/
/*
Initialize the Cart
Create an empty list to store the items in the cart.
display a menu that offers options until the user decides to exit.
User Input
Use a scanner to read the user's input.
Process User Choice
Based on the user’s choice, execute the corresponding action:
Prompt for fruit details (name, type, quantity). do the same for the rest of it.
Display Cart:
Display each item in the cart and its details.
Calculate and display the total cost of the items in the cart.
Save Cart:
Prompt for a filename.
Save the current state of the cart to the specified file.
Load Cart:
Prompt for a filename.
Load the cart from the specified file, replacing the current cart contents.
Exit :Terminate the program.
*/
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Classes and Interfaces for various item types
interface ItemInfo extends Serializable {
    double cost();
    String toString();
}

class Fruits implements ItemInfo {
    private String name;
    private String type;
    private int noOfItems;

    public Fruits(String name, String type, int noOfItems) {
        this.name = name;
        this.type = type;
        this.noOfItems = noOfItems;
    }

    @Override
    public double cost() {
        switch (type) {
            case "local":
                return 0.5 * noOfItems;
            case "tropical":
                return 3 * noOfItems;
            case "imported":
                return 5 * noOfItems;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return "Fruit: " + name + ", Type: " + type + ", Quantity: " + noOfItems;
    }
}

class Vegetables implements ItemInfo {
    private String name;
    private double weight;
    private String type;

    public Vegetables(String name, double weight, String type) {
        this.name = name;
        this.weight = weight;
        this.type = type;
    }

    @Override
    public double cost() {
        switch (type) {
            case "leafy green":
                return 0.3 * weight;
            case "cruciferous":
                return 0.1 * weight;
            case "root":
                return 0.5 * weight;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return "Vegetable: " + name + ", Type: " + type + ", Weight: " + weight;
    }
}

class CannedItem implements ItemInfo {
    private String name;
    private int noOfCans;

    public CannedItem(String name, int noOfCans) {
        this.name = name;
        this.noOfCans = noOfCans;
    }

    @Override
    public double cost() {
        return 1.25 * noOfCans;
    }

    @Override
    public String toString() {
        return "Canned Item: " + name + ", Number of Cans: " + noOfCans;
    }
}

// Cart class for managing a list of items
class Cart implements Serializable {
    private List<ItemInfo> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public void addItem(ItemInfo item) {
        items.add(item);
    }

    public double totalCost() {
        return items.stream().mapToDouble(ItemInfo::cost).sum();
    }

    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            items.forEach(System.out::println);
        }
    }

    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(items);
            System.out.println("Cart has been saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving cart: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            items = (List<ItemInfo>) ois.readObject();
            System.out.println("Cart has been loaded from " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading cart: " + e.getMessage());
        }
    }
}

// Main application
public class ShoppingCart {
    public static void main(String[] args) {
        Cart cart = new Cart();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Fruit \n2. Add Vegetable \n3. Add Canned Item \n4. Display Cart \n5. Save Cart \n6. Load Cart \n7. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Fruit name, type, and no. of items (comma separated): ");
                    String[] fData = scanner.nextLine().split(",");
                    cart.addItem(new Fruits(fData[0].trim(), fData[1].trim(), Integer.parseInt(fData[2].trim())));
                    break;
                case 2:
                    System.out.print("Enter Vegetable name, weight, and type (comma separated): ");
                    String[] vData = scanner.nextLine().split(",");
                    cart.addItem(new Vegetables(vData[0].trim(), Double.parseDouble(vData[1].trim()), vData[2].trim()));
                    break;
                case 3:
                    System.out.print("Enter Canned Item name and number of cans: ");
                    String[] cData = scanner.nextLine().split(",");
                    cart.addItem(new CannedItem(cData[0].trim(), Integer.parseInt(cData[1].trim())));
                    break;
                case 4:
                    System.out.println("Displaying cart items:");
                    cart.displayItems();
                    System.out.println("Total Cost: $" + cart.totalCost());
                    break;
                case 5:
                    System.out.print("Enter file name to save cart: ");
                    String fileNameSave = scanner.nextLine().trim();
                    cart.saveToFile(fileNameSave);
                    break;
                case 6:
                    System.out.print("Enter file name to load cart: ");
                    String fileNameLoad = scanner.nextLine().trim();
                    cart.loadFromFile(fileNameLoad);
                    break;
                case 7:
                    System.out.println("Exiting the application.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

