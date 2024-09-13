import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class OrderItem {
    private Item items;
    private String ordItemName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    
    // for saving
    public OrderItem(Item items, int quantity, double unitPrice){
        this.items = items;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = getTotalPrice(unitPrice, quantity);
    }
    
    // for reading
    public OrderItem(String name, int quantity, double unitPrice, double totalPrice){
        this.ordItemName = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }
    
    public Item getItem(){
        return items;
    }
    
    public String getOrdItemName(){
        return ordItemName;
    }
    
    public int getQuantity(){
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    
    public double getTotalPrice(){
        return totalPrice;
    }
    
    public double getTotalPrice(double unitPrice, int quantity){
        return unitPrice * quantity;
    }

    public static void readItemIdAndNameForPurchaseOrder() {
        String fileName = "items.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String itemId = parts[0];
                    String itemName = parts[1];
                    System.out.println("Item ID: " + itemId + ", Item Name: " + itemName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
