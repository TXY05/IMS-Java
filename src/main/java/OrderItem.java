import java.io.*;
import java.util.*;

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
        double tot = unitPrice * quantity; 
        return tot;
    }
}
