import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class OrderItem {
    private Item items;
    private String ordItemID;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    
    // for saving
    public OrderItem(Item items){
        this.items = items;
        this.quantity = 0;
        this.unitPrice = 0.0;
        this.totalPrice = 0.0;
    }
    
    // for reading
    public OrderItem(String ordItemID, int quantity, double unitPrice){
        this.ordItemID = ordItemID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = getTotalPrice(unitPrice, quantity);
    }
    
    public Item getItem(){
        return items;
    }
    
    public String getOrdItemID(){
        return ordItemID;
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
    
    public void setQuantity(int quantity){
        this.quantity += quantity;
        this.totalPrice = getTotalPrice(this.unitPrice, this.quantity);
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.totalPrice = getTotalPrice(this.unitPrice, this.quantity);
    }
    
    public void reduceQuantity(int quantity){
        if((this.quantity - quantity) < 0){
            this.quantity = 0;
        }else{
            this.quantity -= quantity;
        }
        this.totalPrice = getTotalPrice(this.unitPrice, this.quantity);
    }
}
