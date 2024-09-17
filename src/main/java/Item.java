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
    private double unitPrice;
    private int minInvLV;

    public static void main(String[] args) {
        manageInventory();
    }

    // Constructors
    public Item(String itemName, int itemQuantity, ItemGroups itemGroup, int minInvLV) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemGroup = itemGroup;
        this.minInvLV = minInvLV;
        this.itemId = generateItemId();  // Automatically generate ID
        getInventory().put(this.itemId, this);  // Add item to inventory
        itemGroup.addItem(this);  // Add item to group
    }

    public Item(String itemId, String itemName){
        this.itemId = itemId;
        this.itemName = itemName;
    }
    
    //For GoodsReceive Item ArrayList
    public Item(String itemId, String itemName, int itemQuantity, ItemGroups itemGroup, int minInvLV) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemGroup = itemGroup;
        this.minInvLV = minInvLV;
    }

    // Getters and Setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemId() {
        return itemId;
    }

    public ItemGroups getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroups itemGroup) {
        this.itemGroup = itemGroup;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getMinInvLV() {
        return minInvLV;
    }

    public void setMinInvLV(int minInvLV) {
        this.minInvLV = minInvLV;
    }

    // Static Methods
    public static Map<String, Item> getInventory() {
        return inventory;
    }

    public static void saveInventoryToFile() {
        String fileName = "items.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Item item : getInventory().values()) {
                writer.write(item.getItemId() + "," + item.getItemName() + "," + item.getItemQuantity() + "," + item.getItemGroup().getGroupName() + "," + item.getMinInvLV() + "," + item.getUnitPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Can't Save File");
        }
    }

    public static void loadInventoryFromFile() {
        String fileName = "items.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            Map<String, ItemGroups> groupMap = new TreeMap<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {  // Adjusted to 6 to include unitPrice
                    String itemId = parts[0];
                    String itemName = parts[1];
                    int itemQuantity = Integer.parseInt(parts[2]);
                    String groupName = parts[3];
                    int minInvLV = Integer.parseInt(parts[4]);
                    double unitPrice = Double.parseDouble(parts[5]);
                    ItemGroups itemGroup = groupMap.computeIfAbsent(groupName, ItemGroups::new);
                    Item item = new Item(itemName, itemQuantity, itemGroup, minInvLV);
                    item.setUnitPrice(unitPrice);
                    getInventory().put(itemId, item);
                }
            }
        } catch (IOException e) {
            System.out.println("Can't Read File");
        }
    }

    public static void promptUserToEditItemQuantity() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nPress 'e' to exit at any time.");
        System.out.print("\nEnter the item ID: ");
        String itemId = scanner.nextLine();
        if (itemId.equalsIgnoreCase("e")) return;

        if (getInventory().containsKey(itemId)) {
            Item item = getInventory().get(itemId);
            System.out.print("Enter the new quantity for item " + item.getItemName() + ":" );
            String quantityInput = scanner.nextLine();
            if (quantityInput.equalsIgnoreCase("e")) return;
            item.itemQuantity = Integer.parseInt(quantityInput);
            System.out.println("New quantity of " + item.getItemName() + ": " + item.getItemQuantity());
            saveInventoryToFile();  // Save changes to file
        } else {
            System.out.println("Item ID not found in inventory.");
        }
    }

    public static void promptUserToEditItemDetails() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nPress 'e' to exit at any time.");
        System.out.print("Enter the item ID to edit: ");
        String itemId = scanner.nextLine();
        if (itemId.equalsIgnoreCase("e")) return;

        Item item = getInventory().get(itemId);
        if (item != null) {
            System.out.println("\nItem Details:");
            System.out.printf("%-10s | %-20s | %-10s | %-15s%n", "ID", "Name", "Quantity", "Category");
            System.out.println("-------------------------------------------------------------");
            System.out.printf("%-10s | %-20s | %-10d | %-15s%n", item.getItemId(), item.getItemName(), item.getItemQuantity(), item.getItemGroup().getGroupName());

            boolean editing = true;
            while (editing) {
                System.out.println("\nEditing item: " + item.getItemName());
                System.out.println("1. Edit Name");
                System.out.println("2. Edit Quantity");
                System.out.println("3. Edit Group");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter new name: ");
                        String newName = scanner.nextLine();
                        item.setItemName(newName);
                        break;
                    case 2:
                        System.out.print("Enter new quantity: ");
                        int newQuantity = scanner.nextInt();
                        scanner.nextLine();  // Consume newline
                        item.setItemQuantity(newQuantity);
                        break;
                    case 3:
                        System.out.println("Existing item groups:");
                        List<ItemGroups> existingGroups = getExistingItemGroups();
                        for (int i = 0; i < existingGroups.size(); i++) {
                            System.out.println((i + 1) + ". " + existingGroups.get(i).getGroupName());
                        }
                        System.out.print("Choose a new group: ");
                        int groupChoice = scanner.nextInt();
                        scanner.nextLine();  // Consume newline
                        if (groupChoice > 0 && groupChoice <= existingGroups.size()) {
                            ItemGroups newGroup = existingGroups.get(groupChoice - 1);
                            item.setItemGroup(newGroup);
                        } else {
                            System.out.println("Invalid group choice.");
                        }
                        break;
                    case 4:
                        editing = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            saveInventoryToFile();  // Save changes to file
            removeEmptyGroups();  // Remove empty groups
        } else {
            System.out.println("Item ID not found in inventory.");
        }
    }

    private static void removeEmptyGroups() {
        List<ItemGroups> existingGroups = getExistingItemGroups();
        existingGroups.removeIf(group -> group.getItems().isEmpty());
    }

    public static void promptUserToAddNewItem() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nPress 'e' to exit at any time.");
        System.out.print("\nEnter the item name : ");
        String itemName = scanner.nextLine();
        if (itemName.equalsIgnoreCase("e")) return;
        if (itemName.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return;
        }

        System.out.print("Enter the item quantity : ");
        String quantityInput = scanner.nextLine();
        if (quantityInput.equalsIgnoreCase("e")) return;
        int itemQuantity = Integer.parseInt(quantityInput);
        if (itemQuantity < 0) {
            System.out.println("Quantity cannot be negative.");
            return;
        }

        System.out.print("Enter the minimum stock level: ");
        String minStockInput = scanner.nextLine();
        if (minStockInput.equalsIgnoreCase("e")) return;
        int minInvLV = Integer.parseInt(minStockInput);
        if (minInvLV < 0) {
            System.out.println("Minimum stock level cannot be negative.");
            return;
        }

        System.out.print("Enter the unit price: ");
        String priceInput = scanner.nextLine();
        if (priceInput.equalsIgnoreCase("e")) return;
        double unitPrice = Double.parseDouble(priceInput);
        if (unitPrice < 0) {
            System.out.println("Unit price cannot be negative.");
            return;
        }

        System.out.println("Existing item groups:");
        List<ItemGroups> existingGroups = getExistingItemGroups();
        for (int i = 0; i < existingGroups.size(); i++) {
            System.out.println((i + 1) + ". " + existingGroups.get(i).getGroupName());
        }
        System.out.println((existingGroups.size() + 1) + ". Create new group");

        System.out.print("Choose an option: ");
        String groupChoiceInput = scanner.nextLine();
        if (groupChoiceInput.equalsIgnoreCase("e")) return;
        int groupChoice = Integer.parseInt(groupChoiceInput);

        ItemGroups itemGroup;
        if (groupChoice > 0 && groupChoice <= existingGroups.size()) {
            itemGroup = existingGroups.get(groupChoice - 1);
        } else {
            System.out.print("Enter the new group name: ");
            String groupName = scanner.nextLine();
            if (groupName.equalsIgnoreCase("e")) return;
            if (groupName.isEmpty()) {
                System.out.println("Group name cannot be empty.");
                return;
            }
            itemGroup = new ItemGroups(groupName);
            existingGroups.add(itemGroup);
        }

        Item newItem = new Item(itemName, itemQuantity, itemGroup, minInvLV);
        newItem.setUnitPrice(unitPrice);
        getInventory().put(newItem.getItemId(), newItem);
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
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nDisplay Inventory Options:");
        System.out.println("1. Display All Items");
        System.out.println("2. Display Items by Group");
        System.out.println("3. Display Item by ID");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                displayAllItems();
                break;
            case 2:
                displayItemsByGroup(scanner);
                break;
            case 3:
                displayItemById(scanner);
                break;
            case 4:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void displayAllItems() {
        System.out.println("\nCurrent Inventory:");
        System.out.printf("%-10s | %-20s | %-10s | %-15s | %-10s | %-10s%n", "ID", "Name", "Quantity", "Category", "Min Stock", "Unit Price");
        System.out.println("------------------------------------------------------------------------------------------");
        for (Item item : getInventory().values()) {
            System.out.printf("%-10s | %-20s | %-10d | %-15s | %-10d | %-10.2f%n", item.getItemId(), item.getItemName(), item.getItemQuantity(), item.getItemGroup().getGroupName(), item.getMinInvLV(), item.getUnitPrice());
        }
    }

    private static void displayItemsByGroup(Scanner scanner) {
        System.out.println("\nExisting item groups:");
        List<ItemGroups> existingGroups = getExistingItemGroups();
        for (int i = 0; i < existingGroups.size(); i++) {
            System.out.println((i + 1) + ". " + existingGroups.get(i).getGroupName());
        }
        System.out.print("Choose a group to display: ");
        int groupChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (groupChoice > 0 && groupChoice <= existingGroups.size()) {
            ItemGroups selectedGroup = existingGroups.get(groupChoice - 1);
            System.out.println("\nItems in group: " + selectedGroup.getGroupName());
            System.out.printf("%-10s | %-20s | %-10s | %-15s | %-10s | %-10s%n", "ID", "Name", "Quantity", "Category", "Min Stock", "Unit Price");
            System.out.println("------------------------------------------------------------------------------------------");
            for (Item item : selectedGroup.getItems()) {
                System.out.printf("%-10s | %-20s | %-10d | %-15s | %-10d | %-10.2f%n", item.getItemId(), item.getItemName(), item.getItemQuantity(), item.getItemGroup().getGroupName(), item.getMinInvLV(), item.getUnitPrice());
            }
        } else {
            System.out.println("Invalid group choice. Please try again.");
        }
    }

    private static void displayItemById(Scanner scanner) {
        System.out.print("\nEnter the item ID: ");
        String itemId = scanner.nextLine();

        Item item = getInventory().get(itemId);
        if (item != null) {
            System.out.println("\nItem Details:");
            System.out.printf("%-10s | %-20s | %-10s | %-15s | %-10s | %-10s%n", "ID", "Name", "Quantity", "Category", "Min Stock", "Unit Price");
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.printf("%-10s | %-20s | %-10d | %-15s | %-10d | %-10.2f%n", item.getItemId(), item.getItemName(), item.getItemQuantity(), item.getItemGroup().getGroupName(), item.getMinInvLV(), item.getUnitPrice());

            System.out.print("\nDo you want to change the item details? (y/n): ");
            String response = scanner.nextLine();

            if (response.equalsIgnoreCase("y")) {
                promptUserToEditItemDetails(item);
            }
        } else {
            System.out.println("Item ID not found in inventory.");
        }
    }

    public static void promptUserToEditItemDetails(Item item) {
        Scanner scanner = new Scanner(System.in);

        boolean editing = true;
        while (editing) {
            System.out.print("\nPress 'e' to exit at any time.");
            System.out.println("\nEditing item: " + item.getItemName());
            System.out.println("1. Edit Name");
            System.out.println("2. Edit Quantity");
            System.out.println("3. Edit Group");
            System.out.println("4. Edit Min Stock Level");
            System.out.println("5. Edit Unit Price");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            String choiceInput = scanner.nextLine();
            if (choiceInput.equalsIgnoreCase("e")) return;
            int choice = Integer.parseInt(choiceInput);

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    if (newName.equalsIgnoreCase("e")) return;
                    if (newName.isEmpty()) {
                        System.out.println("Item name cannot be empty.");
                    } else {
                        item.setItemName(newName);
                    }
                    break;
                case 2:
                    System.out.print("Enter new quantity: ");
                    String quantityInput = scanner.nextLine();
                    if (quantityInput.equalsIgnoreCase("e")) return;
                    int newQuantity = Integer.parseInt(quantityInput);
                    if (newQuantity < 0) {
                        System.out.println("Quantity cannot be negative.");
                    } else {
                        item.setItemQuantity(newQuantity);
                    }
                    break;
                case 3:
                    System.out.println("Existing item groups:");
                    List<ItemGroups> existingGroups = getExistingItemGroups();
                    for (int i = 0; i < existingGroups.size(); i++) {
                        System.out.println((i + 1) + ". " + existingGroups.get(i).getGroupName());
                    }
                    System.out.print("Choose a new group: ");
                    String groupChoiceInput = scanner.nextLine();
                    if (groupChoiceInput.equalsIgnoreCase("e")) return;
                    int groupChoice = Integer.parseInt(groupChoiceInput);
                    if (groupChoice > 0 && groupChoice <= existingGroups.size()) {
                        item.setItemGroup(existingGroups.get(groupChoice - 1));
                    } else {
                        System.out.print("Enter the new group name: ");
                        String groupName = scanner.nextLine();
                        if (groupName.equalsIgnoreCase("e")) return;
                        if (groupName.isEmpty()) {
                            System.out.println("Group name cannot be empty.");
                        } else {
                            ItemGroups newGroup = new ItemGroups(groupName);
                            existingGroups.add(newGroup);
                            item.setItemGroup(newGroup);
                        }
                    }
                    break;
                case 4:
                    System.out.print("Enter new minimum stock level: ");
                    String minStockInput = scanner.nextLine();
                    if (minStockInput.equalsIgnoreCase("e")) return;
                    int newMinInvLV = Integer.parseInt(minStockInput);
                    if (newMinInvLV < 0) {
                        System.out.println("Minimum stock level cannot be negative.");
                    } else {
                        item.setMinInvLV(newMinInvLV);
                    }
                    break;
                case 5:
                    System.out.print("Enter new unit price: ");
                    String priceInput = scanner.nextLine();
                    if (priceInput.equalsIgnoreCase("e")) return;
                    double newUnitPrice = Double.parseDouble(priceInput);
                    if (newUnitPrice < 0) {
                        System.out.println("Unit price cannot be negative.");
                    } else {
                        item.setUnitPrice(newUnitPrice);
                    }
                    break;
                case 6:
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            saveInventoryToFile();  // Save changes to file
            removeEmptyGroups();  // Remove empty groups
        }
    }


    public static void promptUserToEditMinStockLevel() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nPress 'e' to exit at any time.");
        System.out.print("\nEnter the item ID: ");
        String itemId = scanner.nextLine();
        if (itemId.equalsIgnoreCase("e")) return;

        Item item = getInventory().get(itemId);
        if (item != null) {
            System.out.println("Current minimum stock level for " + item.getItemName() + ": " + item.getMinInvLV());
            System.out.print("Enter the new minimum stock level: ");
            String minStockInput = scanner.nextLine();
            if (minStockInput.equalsIgnoreCase("e")) return;
            int newMinInvLV = Integer.parseInt(minStockInput);
            item.setMinInvLV(newMinInvLV);
            System.out.println("New minimum stock level for " + item.getItemName() + ": " + item.getMinInvLV());
            saveInventoryToFile();  // Save changes to file
        } else {
            System.out.println("Item ID not found in inventory.");
        }
    }

    public static void promptUserToDeleteItem() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nPress 'e' to exit at any time.");
        System.out.print("\nEnter the item ID to delete: ");
        String itemId = scanner.nextLine();
        if (itemId.equalsIgnoreCase("e")) return;

        if (itemId.isEmpty()) {
            System.out.println("Item ID cannot be empty.");
            return;
        }

        Item item = getInventory().get(itemId);
        if (item != null) {
            item.getItemGroup().getItems().remove(item);
            getInventory().remove(itemId);
            saveInventoryToFile();
            System.out.println("Item " + item.getItemName() + " deleted successfully.");
            removeEmptyGroups();
        } else {
            System.out.println("Item ID not found in inventory.");
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
            System.out.println("4. Edit Min Stock Level");
            System.out.println("5. Display Inventory");
            System.out.println("6. Delete Item");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    promptUserToAddNewItem(); // Add New Item
                    break;
                case 2:
                    promptUserToEditItemQuantity(); // Edit Item Quantity
                    break;
                case 3:
                    promptUserToEditItemDetails(); // Edit Item Details
                    break;
                case 4:
                    promptUserToEditMinStockLevel(); // Edit Min Stock Level
                    break;
                case 5:
                    displayInventory(); // Display inventory
                    break;
                case 6:
                    promptUserToDeleteItem(); // Delete Item
                    break;
                case 7:
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
        return String.format("I%04d", idCounter.incrementAndGet());
    }
    
    //Add quantity after receving order
    public static void addItemQty(String restockID, int restockQty){
        ArrayList<Item> tempItem = GoodsReceive.readAndSaveItem("items.txt");
        int index = 0;
        for (Item item : tempItem){
            if (restockID.equals(item.getItemId())) {
                tempItem.get(index).setItemQuantity(item.getItemQuantity() + restockQty);
            }
            index++;
        }
        try {
            new FileWriter("items.txt", false).close();
        } catch (IOException e) {
            System.out.println("Empty file failed!");
        }
        GoodsReceive.saveItemArrayToFile(tempItem);
    }
}