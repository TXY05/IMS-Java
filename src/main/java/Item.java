import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.Map;

public class Item {
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private String itemName;
    private int itemQuantity;
    private ItemGroups itemGroup;  // Association with ItemGroups
    private final String itemId;
    private static final Map<String, Item> inventory = new HashMap<>();

    public Item(String itemName, int itemQuantity, ItemGroups itemGroup) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemGroup = itemGroup;
        this.itemId = generateItemId();  // Automatically generate ID
        addItemToInventory(this);
    }

    public String generateItemId() {
        return String.format("P%04d", idCounter.incrementAndGet());
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void addItemQuantity(int quantityToAdd) {
        if (inventory.containsKey(this.itemId)) {
            Item item = inventory.get(this.itemId);
            item.itemQuantity += quantityToAdd;
        } else {
            this.itemQuantity += quantityToAdd;
            inventory.put(this.itemId, this);
        }
    }

    public void removeItemQuantity(int quantityToRemove) {
        if (inventory.containsKey(this.itemId)) {
            Item item = inventory.get(this.itemId);
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
    }

    private void addItemToInventory(Item item) {
        inventory.put(item.itemId, item);
    }

    public static Map<String, Item> getInventory() {
        return inventory;
    }

    public static void promptUserToAddItemQuantity() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the item ID to add quantity:");
        String itemId = scanner.nextLine();
        if (inventory.containsKey(itemId)) {
            Item item = inventory.get(itemId);
            System.out.println("Enter the quantity to add for item " + item.getItemName() + ":");
            int quantityToAdd = scanner.nextInt();
            item.addItemQuantity(quantityToAdd);
            System.out.println("New quantity of " + item.getItemName() + ": " + item.getItemQuantity());
        } else {
            System.out.println("Item ID not found in inventory.");
        }
    }

    public static void promptUserToRemoveItemQuantity() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the item ID to remove quantity:");
        String itemId = scanner.nextLine();
        if (inventory.containsKey(itemId)) {
            Item item = inventory.get(itemId);
            System.out.println("Enter the quantity to remove for item " + item.getItemName() + ":");
            int quantityToRemove = scanner.nextInt();
            item.removeItemQuantity(quantityToRemove);
            System.out.println("New quantity of " + item.getItemName() + ": " + item.getItemQuantity());
        } else {
            System.out.println("Item ID not found in inventory.");
        }
    }

    public static void promptUserToEditItemDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the item ID to edit:");
        String itemId = scanner.nextLine();

        if (inventory.containsKey(itemId)) {
            Item item = inventory.get(itemId);
            System.out.println("Enter the new name for item " + item.getItemName() + ":");
            String newItemName = scanner.nextLine();
            System.out.println("Enter the new quantity for item " + item.getItemName() + ":");
            int newItemQuantity = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            System.out.println("Enter the new group name for item " + item.getItemName() + ":");
            String newItemGroupName = scanner.nextLine();
            ItemGroups newItemGroup = new ItemGroups(newItemGroupName);
            item.editItemDetails(newItemName, newItemQuantity, newItemGroup);
            System.out.println("Item details updated.");
        } else {
            System.out.println("Item ID not found in inventory.");
        }
    }

    public static void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Item item : inventory.values()) {
            System.out.println("ID: " + item.generateItemId() + ", Name: " + item.getItemName() + ", Quantity: " + item.getItemQuantity());
        }
    }

    public static void manageInventory() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        // Create an item group
        ItemGroups electronicsGroup = new ItemGroups("Electronics");

        // Create an item
        Item laptop = new Item("Laptop", 10, electronicsGroup);

        // Add the item to the group
        electronicsGroup.addItemGroup(laptop);

        while (!exit) {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add Item Quantity");
            System.out.println("2. Remove Item Quantity");
            System.out.println("3. Edit Item Details");
            System.out.println("4. Display Inventory");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    promptUserToAddItemQuantity();
                    break;
                case 2:
                    promptUserToRemoveItemQuantity();
                    break;
                case 3:
                    promptUserToEditItemDetails();
                    break;
                case 4:
                    displayInventory();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}