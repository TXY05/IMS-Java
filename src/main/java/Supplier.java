/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author TXY
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Supplier extends Person {
    private static String filename = "Supplier.txt";
    
    private Address address;
    private String id;
    private ArrayList<String> itemList;

    public Supplier(String id, String name, String email, Address address, ArrayList<String> itemList) {
        super(name, email);
        this.address = address;
        this.id = id;
        this.itemList = itemList;
    }    

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }
    
    public ArrayList<String> getItemList(){
        return itemList;
    }
    
    public static ArrayList<Supplier> getSupplier(){
        ArrayList<Supplier> supplier = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<String> itemList = new ArrayList<>();
                String[] row = line.split("[|]");
                String[] item = row[6].split(",");
                for(String i:item){
                    itemList.add(i);
                }
                supplier.add(new Supplier(row[0],row[1],row[2],new Address(row[3],row[4],Integer.parseInt(row[5])),itemList));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to Open File !!!");
        }
        return supplier;
    }
    
    public void encryption(){
        
    }
    
    public void decryption(){
        
    }
}
