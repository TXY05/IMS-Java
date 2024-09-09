import java.io.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

public class Item {
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private String itemName;
    private int itemQuantity;
    private ItemGroups itemGroup;  // Association with ItemGroups
    private final String itemId;
    private static final Map<String, Item> inventory = new TreeMap<>();

    private double buyPrice;
    private int maxInvLV;
    private int minInvLV;

    public static void main(String[] args) {
        manageInventory();
    }

    // Constructors
    public Item(String itemName, int itemQuantity, ItemGroups itemGroup) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemGroup = itemGroup;
        this.itemId = generateItemId();  // Automatically generate ID
        addItemToInventory(this);
    }

    public Item(String name, int quantity, ItemGroups groupName, double buyPrice, int maxInvLV, int minInvLV) {
        this(name, quantity, groupName);
        this.buyPrice = buyPrice;
        this.maxInvLV = maxInvLV;
        this.minInvLV = minInvLV;
    }

    // Getters and Setters
    public String getItemName() {
        return itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public String getItemId() {
        return itemId;
    }

    public ItemGroups getItemGroup() {
        return itemGroup;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getMaxInvLV() {
        return maxInvLV;
    }

    public int getMinInvLV() {
        return minInvLV;
    }

    public boolean isInStock() {
        return getItemQuantity() > 0;
    }

    // Methods
    public void addItemQuantity(int quantityToAdd) {
        if (getInventory().containsKey(this.itemId)) {
            Item item = getInventory().get(this.itemId);
            item.itemQuantity += quantityToAdd;
        } else {
            this.itemQuantity += quantityToAdd;
            getInventory().put(this.itemId, this);
        }
    }

    public void removeItemQuantity(int quantityToRemove) {
        if (getInventory().containsKey(this.itemId)) {
            Item item = getInventory().get(this.itemId);
            if (item.itemQuantity >= quantityToRemove) {
                item.itemQuantity -= quantityToRemove;
            } else {
                System.out.println("Not enough quantity to remove.");
            }
        } else {
            System.out.println("Item ID not found in inventory.");
        }
    }

    public void editItemDetails(String newItemName, int newItemQuantity, ItemGroups newItemGroup) {
        this.itemName = newItemName;
        this.itemQuantity = newItemQuantity;
        this.itemGroup = newItemGroup;
        saveInventoryToFile();  // Save changes to file
    }

    // Static Methods
    public static Map<String, Item> getInventory() {
        return inventory;
    }

    public static void saveInventoryToFile() {
        String fileName = "items.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Item item : getInventory().values()) {
                writer.write(item.getItemId() + "," + item.getItemName() + "," + item.getItemQuantity() + "," + item.getItemGroup().getGroupName());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadInventoryFromFile() {
        String fileName = "items.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String itemId = parts[0];
                    String itemName = parts[1];
                    int itemQuantity = Integer.parseInt(parts[2]);
                    String groupName = parts[3];
                    ItemGroups itemGroup = new ItemGroups(groupName);
                    Item item = new Item(itemName, itemQuantity, itemGroup);
                    getInventory().put(itemId, item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void promptUserToEditItemQuantity() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the item ID: ");
        String itemId = scanner.nextLine();

        if (getInventory().containsKey(itemId)) {
            Item item = getInventory().get(itemId);
            System.out.print("Enter the new quantity for item " + item.getItemName() + ": ");
            item.itemQuantity = scanner.nextInt();
            System.out.println("New quantity of " + item.getItemName() + ": " + item.getItemQuantity());
            saveInventoryToFile();  // Save changes to file
        } else {
            System.out.println("Item ID not found in inventory.");
        }
    }

    public static void promptUserToEditItemDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the item ID to edit: ");
        String itemId = scanner.nextLine();

        if (getInventory().containsKey(itemId)) {
            Item item = getInventory().get(itemId);
            System.out.print("Enter the new name for item " + item.getItemName() + ": ");
            String newItemName = scanner.nextLine();
            System.out.print("Enter the new quantity for item " + item.getItemName() + ": ");
            int newItemQuantity = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            System.out.print("Enter the new group name for item " + item.getItemName() + ": ");
            String newItemGroupName = scanner.nextLine();
            ItemGroups newItemGroup = new ItemGroups(newItemGroupName);
            item.editItemDetails(newItemName, newItemQuantity, newItemGroup);
            System.out.println("Item details updated.");
        } else {
            System.out.println("Item ID not found in inventory.");
        }
    }

    public static void promptUserToAddNewItem() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter the item name: ");
        String itemName = scanner.nextLine();

        System.out.print("Enter the item quantity: ");
        int itemQuantity = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        // Display existing item groups
        System.out.println("Existing item groups:");
        List<ItemGroups> existingGroups = getExistingItemGroups();
        for (int i = 0; i < existingGroups.size(); i++) {
            System.out.println((i + 1) + ". " + existingGroups.get(i).getGroupName());
        }
        System.out.println((existingGroups.size() + 1) + ". Create new group");

        System.out.print("Choose an option: ");
        int groupChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        ItemGroups itemGroup;
        if (groupChoice > 0 && groupChoice <= existingGroups.size()) {
            itemGroup = existingGroups.get(groupChoice - 1);
        } else {
            System.out.print("Enter the new group name: ");
            String groupName = scanner.nextLine();
            itemGroup = new ItemGroups(groupName);
            existingGroups.add(itemGroup);
        }

        // Create a new Item object
        Item newItem = new Item(itemName, itemQuantity, itemGroup);

        // Add the new item to the inventory
        getInventory().put(newItem.getItemId(), newItem);

        // Save the updated inventory to the file
        saveInventoryToFile();

        System.out.println("New item added successfully.");
    }

    public static List<ItemGroups> getExistingItemGroups() {
        // Collect all unique item groups from the inventory
        Set<String> groupNamesSet = new HashSet<>();
        List<ItemGroups> uniqueGroups = new ArrayList<>();
        for (Item item : getInventory().values()) {
            if (groupNamesSet.add(item.getItemGroup().getGroupName())) {
                uniqueGroups.add(item.getItemGroup());
            }
        }
        return uniqueGroups;
    }

    public static void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Item item : getInventory().values()) {
            System.out.println("ID: " + item.getItemId() + ", Name: " + item.getItemName() + ", Quantity: " + item.getItemQuantity() + ", Category: " + item.getItemGroup().getGroupName());
        }
    }

    public static void manageInventory() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        // Load inventory from file at the start
        loadInventoryFromFile();

        while (!exit) {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add New Item");
            System.out.println("2. Edit Item Quantity");
            System.out.println("3. Edit Item Details");
            System.out.println("4. Display Inventory");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    promptUserToAddNewItem();
                    break;
                case 2:
                    promptUserToEditItemQuantity();
                    break;
                case 3:
                    promptUserToEditItemDetails();
                    break;
                case 4:
                    displayInventory();
                    break;
                case 5:
                    saveInventoryToFile(); // Save inventory to file before exiting
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    public String generateItemId() {
        return String.format("P%04d", idCounter.incrementAndGet());
    }

    private void addItemToInventory(Item item) {
        getInventory().put(item.itemId, item);
    }
}