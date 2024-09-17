/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author TXY
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Supplier extends Person {
    private static String filename = "Supplier.txt";
    
    private Address address;
    private String id;
    private ArrayList<String> itemList;
    
    public Supplier(){
        
    }

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
    
    public static ArrayList<Supplier> getSupplier() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        Supplier supplier = new Supplier();
        ArrayList<Supplier> suppliers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<String> itemList = new ArrayList<>();
                String[] row = supplier.decryption(line).split("[|]");
                String[] item = row[6].split(",");
                for(String i:item){
                    itemList.add(i);
                }
                suppliers.add(new Supplier(row[0],row[1],row[2],new Address(row[3],row[4],Integer.parseInt(row[5])),itemList));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to Open File !!!");
        }
        return suppliers;
    }
    
    
    public String encryption (String line)throws NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException{
        String encodedKey;
        SecretKey secretKey = null;
        byte[] encryptedBytes = null;
        
        try (BufferedReader br = new BufferedReader(new FileReader("secretKey.txt"))) {
            String row = br.readLine();
            byte[] decodedKey = Base64.getDecoder().decode(row);
            
         secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to Open File !!!");
        }
        
        Cipher cipher = Cipher.getInstance("AES");
            
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        
        try {
            encryptedBytes = cipher.doFinal(line.getBytes());
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
        
        return encryptedText;
    }
    
    public String decryption(String line)throws NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException{
        String encodedKey;
        SecretKey secretKey = null;
        byte[] decryptedBytes = null;
        
        try (BufferedReader br = new BufferedReader(new FileReader("secretKey.txt"))) {
            String row = br.readLine();
            byte[] decodedKey = Base64.getDecoder().decode(row);
            
         secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to Open File !!!");
        }
        
           
        Cipher cipher = Cipher.getInstance("AES");
            
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
         
        try {
            decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(line));
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String plainText = new String(decryptedBytes);
        
        return plainText;
    }
   
}
