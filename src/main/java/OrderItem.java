import java.io.*;
import java.util.*;

public class OrderItem {
    private Item items;
    private String ordItemName;
    private int quantity;
    private double totalPrice;
    
    // for saving
    public OrderItem(Item items, int quantity){
        this.items = items;
        this.quantity = quantity;
        this.totalPrice = getTotalPrice(items, quantity);
    }
    
    // for reading
    public OrderItem(String name, int quantity, double totalPrice){
        this.ordItemName = name;
        this.quantity = quantity;
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
    
    public double getTotalPrice(){
        return totalPrice;
    }
    
    public double getTotalPrice(Item items, int quantity){
        double tot = items.getBuyPrice() * quantity; 
        return tot;
    }
}
