/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author TXY
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Supplier extends Person {

    private static int supplierCount = 1;
    private static String supplierFile = "Supplier.txt";

    private static BufferedReader reader = null;
    private Address address;
    private String id;
    private ArrayList<String> itemList;

    public Supplier() {

    }

    public Supplier(String id, String name, String email, Address address) {
        super(name, email);
        this.address = address;
        this.id = id;
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

    public ArrayList<String> getItemList() {
        return itemList;
    }

    public static ArrayList<Supplier> getSupplier() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Supplier supplier = new Supplier();
        ArrayList<Supplier> suppliers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(supplierFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                supplierCount++;
                String decryptedText = supplier.decryption(line);

                ArrayList<String> itemList = new ArrayList<>();

                String[] row = supplier.decryption(line).split("[|]");
                                
                String[] item = row[6].split(",");
                for (String i : item) {
                    itemList.add(i);
                }
                suppliers.add(new Supplier(row[0], row[1], row[2], new Address(row[3], row[4], Integer.parseInt(row[5])), itemList));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to Open File !!!");
        }
        return suppliers;
    }

    public static boolean addSupplier() {
        Scanner scanner = new Scanner(System.in);
        boolean invalid = false;
        String name = null;
        String email = null;
        String address = null;
        String city = null;
        String postalCodeInput = null;
        int postalCode = 0;
        String line = null;
        boolean addDone = false;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";
        int itemSelection = 0;

        ArrayList<String> itemIDList = new ArrayList<>();

        ArrayList<String> itemList = new ArrayList<>();
        Item item = new Item();
        Map<String, Item> inventory = item.getInventory();
        int i = 1;
        boolean finish = false;
        try {
            Supplier.getSupplier();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("\n=====================");
        System.out.println("     Add Supplier");
        System.out.println("  Enter \"E\" To Exit");
        System.out.println("=====================");
        do {
            invalid = false;
            System.out.print("Enter Name > ");
            name = scanner.nextLine();

            if (name.equalsIgnoreCase("e")) {
                return false;
            } else if (name == null || name.isEmpty()) {
                invalid = true;
                System.out.println("Name invalid! Please try again!");
            }
        } while (invalid);

        do {
            invalid = false;
            System.out.print("Enter Email > ");
            email = scanner.nextLine();
            if (email.equalsIgnoreCase("e")) {
                return false;
            } else if (email == null || !Pattern.compile(emailRegex).matcher(email).matches() || email.isEmpty()) {
                invalid = true;
                System.out.println("Email invalid! Please try again!");
            }
        } while (invalid);

        do {
            invalid = false;
            System.out.print("Enter Address > ");
            address = scanner.nextLine();
            if (address.equalsIgnoreCase("e")) {
                return false;
            } else if (address == null || address.isEmpty()) {
                invalid = true;
                System.out.println("Address is invalid! Please try again!");
            }
        } while (invalid);

        do {
            invalid = false;
            System.out.print("Enter City > ");
            city = scanner.nextLine();
            if (city.equalsIgnoreCase("e")) {
                return false;
            } else if (city == null || city.isEmpty()) {
                invalid = true;
                System.out.println("City is invalid! Please try again!");
            }
        } while (invalid);

        do {
            invalid = false;
            System.out.print("Enter Postal Code > ");
            postalCodeInput = scanner.nextLine();
            if (postalCodeInput.equalsIgnoreCase("e")) {
                return false;
            } else if (postalCodeInput == null || postalCodeInput.isEmpty()) {
                invalid = true;
                System.out.println("Address is invalid! Please try again!");
            }
        } while (invalid);
        postalCode = Integer.parseInt(postalCodeInput);

        Supplier supplier = new Supplier();

        try {
            reader = new BufferedReader(new FileReader(supplierFile));
            while ((line = reader.readLine()) != null) {
                String decodedText = supplier.decryption(line);
                String[] row = decodedText.split("[|]");
                if (email.compareTo(row[2]) == 0) {
                    System.out.println("Email repeated! Please try again!");
                    return false;
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Supplier newSupplier = new Supplier(Supplier.generateSupplierId(), name, email, new Address(address, city, postalCode));
        do {
            i = 1;

            System.out.println("\n\n============================================================================================");
            System.out.printf("ID: %-34s Address: %s", newSupplier.getId(), newSupplier.address.getAddress());
            System.out.printf("\nName: %-41s %s", newSupplier.getName(), newSupplier.address.getCity());
            System.out.printf("\nEmail: %-40s %s", newSupplier.getEmail(), newSupplier.address.getPostalCode());
            System.out.println("\n============================================================================================");

            item.loadInventoryFromFile();
            int maxItem = item.getNumberOfItemsAvailable();

            for (Item items : inventory.values()) {
                itemIDList.add(items.getItemId());
            }
            do {
                invalid = false;
                item.displayAllItems();

                System.out.println("\nEnter \"-1\" to Exit or \"-2\" to Finish");
                try {
                    System.out.print("Please select the item(s) sold by this suppliers: ");
                    itemSelection = scanner.nextInt();
                    scanner.nextLine();
                    invalid = false;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
                if (itemSelection == -1) {
                    System.out.println("Cancelled...");
                    return false;

                } else if (itemSelection == -2) {
                    addDone = true;
                    break;
                } else if (itemSelection < 1 || itemSelection > maxItem) {
                    System.out.println("Please choose an option within the range.\n");
                    scanner.nextLine();
                } else {
                    do {
                        invalid = false;
                        System.out.printf("\n%s selected\n", itemIDList.get(itemSelection - 1));
                        System.out.print("Press Y to add or N to remove > ");
                        String confirm = scanner.nextLine();
                        if (confirm.equalsIgnoreCase("y")) {
                            if (itemList.contains(itemIDList.get(itemSelection - 1))) {
                                System.out.println("Item already exists in list");
                                break;
                            } else {
                                itemList.add(itemIDList.get(itemSelection - 1));
                                System.out.println("Item added");
                                break;
                            }

                        } else if (confirm.equalsIgnoreCase("n")) {
                            itemList.remove(itemIDList.get(itemSelection - 1));
                            System.out.println("Item removed");
                            invalid = false;
                        } else {
                            System.out.println("Please press Y or N only!");
                            invalid = true;
                        }
                    } while (invalid);

                    do {
                        addDone = false;
                        invalid = false;
                        System.out.print("Continue add item? (Y/N) > ");
                        String confirm = scanner.nextLine();

                        if (confirm.equalsIgnoreCase("y")) {
                            break;
                        } else if (confirm.equalsIgnoreCase("n")) {
                            addDone = true;
                            invalid = false;
                        } else {
                            System.out.println("Please press Y or N only!");
                            invalid = true;
                        }
                    } while (invalid);

                }

            } while (itemSelection != -1 && !addDone);

            System.out.println("Item sold by supplier: ");
            if (!itemList.isEmpty()) {
                for (String itemlists : itemList) {
                    System.out.println(i + ". " + itemlists);
                    i++;
                }
            }
            do {
                invalid = false;
                System.out.print("Confirm changes? (Y/N) > ");
                String confirm = scanner.nextLine();
                if (confirm.equalsIgnoreCase("y")) {
                    finish = true;

                    break;
                } else if (confirm.equalsIgnoreCase("n")) {
                    finish = false;
                    invalid = false;
                } else {
                    System.out.println("Please press Y or N only!");
                    invalid = true;
                }
            } while (invalid);
        } while (!finish);
        String plainText = new String(Supplier.generateSupplierId() + "|" + name + "|" + email + "|" + address + "|" + city + "|" + postalCode+"|");

        for (String items : itemList) {
            plainText = plainText + items + ",";
        }
        plainText = plainText.substring(0, plainText.length() - 1);


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(supplierFile, true))) {
            String encryptedText = supplier.encryption(plainText);
            writer.append(encryptedText);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }


    public static String generateSupplierId() {
        String supplierID = String.format("S%04d", supplierCount);

        return supplierID;
    }

    public String encryption(String line) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
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

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

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

    public String decryption(String line) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
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

        cipher.init(Cipher.DECRYPT_MODE, secretKey);

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
