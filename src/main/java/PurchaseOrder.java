/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment;

/**
 *
 * @author User
 */
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PurchaseOrder {
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private String orderID;
    private ArrayList<OrderItem> orderItems;
    private String supplierName;
    private LocalDate orderDate;
    private String status;
    private double totalPOprice;
    private int itemCount;

    public PurchaseOrder(ArrayList<OrderItem> orderItems, Supplier supplier) {
        this.orderID = generatePOId();
        this.orderItems = orderItems;
        this.supplierName = supplier.getName();
        this.orderDate = LocalDate.now();
        this.status = "Pending";
        this.totalPOprice = calTotalPOprice(orderItems);
        this.itemCount = orderItems.size();
    }
    
    public PurchaseOrder(String orderID, ArrayList<OrderItem> orderItems, String suppName, 
            LocalDate date, String status, double totalPrice, int itemCount){
        this.orderID = orderID;
        this.orderItems = orderItems;
        this.supplierName = suppName;
        this.orderDate = date;
        this.status = status;
        this.totalPOprice = totalPrice;
        this.itemCount = itemCount;
    }

    public String getOrderID() {
        return orderID;
    }
    
    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }
    
    public double getTotalPOprice(){
        return totalPOprice;
    }
    
    public int getItemCount(){
        return itemCount;
    }
    
    public String generatePOId() {
        return String.format("PO%04d", idCounter.incrementAndGet());
    }
    
    public double calTotalPOprice(ArrayList<OrderItem> orderItems){
        double total = 0;
        for (int i = 0; i < (orderItems.size()); i++){
            total += (orderItems.get(i).getTotalPrice());
        }
        return total;
    }

    public static ArrayList<Supplier> listSupplierFromFile(String filename){
        ArrayList<Supplier> ListSupplier = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length == 5){
                    String name = parts[0];
                    String email = parts[1];
                    String address = parts[2];
                    String city = parts[3];
                    int postalCode = Integer.parseInt(parts[4]);
                    Address newAddress = new Address(address, city, postalCode);
                    Supplier supp = new Supplier(newAddress, name, email);
                    ListSupplier.add(supp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to Open File !!!");
        }
        return ListSupplier;
    
    }
    
    public static Supplier selectedSupp(){
        Scanner sc = new Scanner(System.in);
        ArrayList<Supplier> ListSupplier = listSupplierFromFile("Supplier.txt");
        Supplier selSupp;
        int selectedIndex = 0;
        char done = 'Y';
        
        do{
            System.out.println("Choose Supplier: ");
            for (int i = 0; i < ListSupplier.size(); i++) {
                System.out.println((i+1) + ". " + ListSupplier.get(i).getName());
            }
            
            do {
                try {
                    System.out.print("Select: ");
                    selectedIndex = sc.nextInt()-1;
                    
                    if (selectedIndex < 0 || selectedIndex >= ListSupplier.size()) {
                        System.out.println("Error: Selection out of range. Please select a number between 1 and " + ListSupplier.size());
                    }
                    
                }catch(InputMismatchException e){
                    System.out.println("Input Error, Please Try Again!!");
                }
            }while(selectedIndex < 0 || selectedIndex > (ListSupplier.size()-1));

            do{
                try {
                    System.out.print("Confirm Selection ? (Y/N): ");
                    done = Character.toUpperCase(sc.next(".").charAt(0));
                }catch(Exception e){
                    System.out.println("Input Error, Please Try Again!!");
                }
            }while(done != 'Y' && done != 'N');
            
            selSupp = ListSupplier.get(selectedIndex);
            System.out.println();System.out.println();
            
        }while(done == 'N');
        
        return selSupp;
    }
    
    public static ArrayList<Item> listItemFromFile(String filename){
        ArrayList<Item> ListItems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length == 5){
                    String itemId = parts[0];
                    String itemName = parts[1];
                    int itemQuantity = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);
                    String groupName = parts[4];
                    ItemGroups itemGroup = new ItemGroups(groupName);
                    Item items = new Item(itemId, itemName, itemQuantity, price, itemGroup);
                    ListItems.add(items);
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to Open File !!!");
        }
        return ListItems;
    
    }
    
    public static ArrayList<OrderItem> addOrdItems(){
        Scanner sc = new Scanner(System.in);
        ArrayList<Item> ListItems = listItemFromFile("Item.txt");
        ArrayList<OrderItem> ordList = new ArrayList<>();
        char done = 'Y';
        
        do{
            int selectedIndex;
            int quantity;
            
            System.out.println("Available Items:");
            for (int i = 0; i < ListItems.size(); i++) {
                System.out.println((i+1) + ". " + ListItems.get(i).getItemName());
            }
            System.out.println();
            System.out.print("Select Item to add: ");
            selectedIndex = sc.nextInt();
            
            System.out.print("Quantity: ");
            quantity = sc.nextInt();
            
            
            Item items = ListItems.get(selectedIndex-1);
            System.out.println(items.getItemName());
            OrderItem ordItem = new OrderItem(items, quantity);
            ordList.add(ordItem);
            
            do{
                System.out.print("Continue Adding Item ? (Y/N): ");
                done = Character.toUpperCase(sc.next(".").charAt(0));
                System.out.println();
               
                
            }while(done != 'Y' && done != 'N');
            System.out.println();
            
        }while(done == 'Y');
       
        return ordList;
    }
    
    public static void listOrdItems(ArrayList<OrderItem> orderItems){
        if(orderItems != null){
            int itemCount = orderItems.size();
            System.out.println("Items Selected: ");
            for (int i = 0; i < itemCount; i++){
                System.out.println((i+1) + ". " + orderItems.get(i).getItem().getItemName() + "\t" + 
                    orderItems.get(i).getQuantity() + "\t" + orderItems.get(i).getTotalPrice());
            }   
        }else {
            System.out.println("Items Selected: ");
        }
    }
    
    public static void deleteOrdItems(ArrayList<OrderItem> orderItems){ 
        if(orderItems != null){
            Scanner sc = new Scanner(System.in);
            int choice;
            char choice2;

            System.out.println("Enter the Item You Want To Delete: ");
            choice = sc.nextInt();
            choice--;
            do {
                System.out.println();
                System.out.println("Are You Sure You Want to Delete This Item !! (Y/N)");
                choice2 = Character.toUpperCase(sc.next(".").charAt(0));

            }while(choice2 != 'Y' && choice2 != 'N');

            if(choice2 == 'Y'){
                orderItems.remove(choice);
            }
        }else {
            System.out.println("Item List Empty!!!");
        }
    }
    
    public static void savePOToFile(String orderID, ArrayList<OrderItem> orderItems,String supplierName, 
            LocalDate orderDate, String status, double totalPOprice, int itemCount) {
        String fileName = "PO.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(orderID + "|" +  supplierName + "|" + orderDate + "|" + status + "|" + totalPOprice + "|" + itemCount);
            for (int i = 0; i < itemCount; i++){
                bw.newLine();
                bw.write(orderItems.get(i).getItem().getItemName() + "|" + orderItems.get(i).getQuantity() + "|" +
                        orderItems.get(i).getTotalPrice());
            }
            bw.newLine();
            System.out.println("Order Successful!!");
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to Open File !!!");
        }
    }
     
    public static ArrayList<PurchaseOrder> readPOFromFile(String filename){
        ArrayList<PurchaseOrder> poList = new ArrayList<>();
        ArrayList<OrderItem> orderList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                String poID = parts[0];
                String suppName = parts[1];
                LocalDate date = LocalDate.parse(parts[2]);
                String status = parts[3];
                double totalPrice = Double.parseDouble(parts[4]);
                int itemCount = Integer.parseInt(parts[5]);
                
                for(int i = 0; i < itemCount; i++){
                    line = br.readLine();
                    String[] part = line.split("\\|");
                    String itemName = part[0];
                    int quantity = Integer.parseInt(part[1]);
                    double totalItemPrice = Double.parseDouble(part[2]);
                    OrderItem ordItem = new OrderItem(itemName, quantity, totalItemPrice);
                    orderList.add(ordItem);
                }
            PurchaseOrder po = new PurchaseOrder(poID, orderList, suppName, date, status, totalPrice, itemCount);
            poList.add(po);
            }
            } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to Open File !!!");
            System.exit(1);
        }
//        } catch (FileNotFoundException e){
//            
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Unable to Open File !!!");
//            System.exit(1);
//        }
        return poList;
    }
    
    public static void poHistory(){
        Scanner sc = new Scanner(System.in);
        ArrayList<PurchaseOrder> poList = readPOFromFile("PO.txt");
        boolean leave = true, error;
        int choice;
        
        do{
            System.out.println("  Purchase Order History: ");
            System.out.println("=======================================================================================================");
            System.out.println("       Order No." + "\tSupplier Name\t\t       " + "Order Date\t" + "Order Price\t" + "Status \t");
            System.out.println("=======================================================================================================");
            
            for (int i = 0; i < poList.size(); i++){
                System.out.printf(" %3d.  %s\t\t%-30s %s \t%-8.2f \t%-10s%n", (i+1), poList.get(i).getOrderID(),poList.get(i).getSupplierName(), 
                        poList.get(i).getOrderDate(), poList.get(i).getTotalPOprice(), poList.get(i).getStatus());
                System.out.println("=======================================================================================================");
            }

            do{
                error = true;
                System.out.println("\n  Options:");
                System.out.print("  1. Change Sorting Type\n  2. View Purchase Order Detail\n  3. Return\n  Please Select: ");
                try{
                    choice = sc.nextInt();
                    switch(choice){
                        case 1:{
                            error = false;
                            sortPOHistory(poList);
                            break;
                        }
                        case 2:{
                            error = false;
                            selectPO(poList);
                            break;
                        }
                        case 3:{
                            error = false;
                            leave = false;
                            break;
                        }
                        default:{
                            System.out.println("  Input Error, Please Try Again!!!");
                            break;
                        }
                    }
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!");
                }
                sc.nextLine();
            }while(error);
        }while(leave);
    }
    
    public static void sortPOHistory(ArrayList<PurchaseOrder> poList){
        Scanner sc = new Scanner(System.in);
        boolean error;
        int choice;
        
        do{
            error = true;
            System.out.println("\n  Sorting Options:");
            System.out.println("  1. PurchaseOrder ID(Asc)\n  2. PurchaseOrder ID(Dec)\n  3. Supplier Name(Asc)\n  4. Supplier Name(Dec)");
            System.out.print("  5. Date(Asc)\n  6. Date(Dec)\n  7. Price(Asc)\n  8. Price(Dec)\n  9. Status(Asc)\n  Please Select: ");
            try{
                choice = sc.nextInt();
                switch(choice){
                    case 1:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getOrderID));
                        break;
                    }
                    case 2:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getOrderID).reversed());
                        break;
                    }
                    case 3:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getSupplierName));
                        break;
                    }
                    case 4:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getSupplierName).reversed());
                        break;
                    }
                    case 5:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getOrderDate));
                        break;
                    }
                    case 6:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getOrderDate).reversed());
                        break;
                    }
                    case 7:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getTotalPOprice));
                        break;
                    }
                    case 8:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getTotalPOprice).reversed());
                        break;
                    }
                    case 9:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getStatus));
                        break;
                    }
                    default:{
                        System.out.println("  Input Error, Please Try Again!!!");
                        break;
                    }
                }
            }catch(InputMismatchException e){
                System.out.println("  Input Error, Please Try Again!!!");
            }
            sc.nextLine();
        }while(error);
    }
    
    public static void selectPO(ArrayList<PurchaseOrder> poList){
        Scanner sc = new Scanner(System.in);
        boolean error;
        int choice = 0;
        
        do{
                error = true;
                System.out.print("\n  No. Purchase Order to View: ");
                try{
                    choice = sc.nextInt();
                    if(choice >= 1 && choice <= (poList.size())){
                        error = false;
                    }else{
                        System.out.println("  Input Error, Please Try Again!!!");
                    }
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!");
                }
                sc.nextLine();
            }while(error); 
        
        showPO(poList.get(choice-1));
    }
    
    public static void showPO(PurchaseOrder po){
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=======================================================================================================");
        System.out.println("\t\t\t\t\tPurchase Order");
        System.out.println("\t\t\t\t    ======================");
        System.out.println("\n  Bill To \t\t\t\t\t\t\t\t Purchase Order ID: ");
        System.out.printf("  %-30s \t\t\t\t\t #%s", po.getSupplierName(), po.getOrderID());
        System.out.println("\n\n          \t\t\t\t\t\t\t\t Date: ");
        System.out.printf("          \t\t\t\t\t\t\t\t %s", po.getOrderDate());
        System.out.println("\n\n  No. \t Items \t\t\t\t\t Quantity \t\t Price");
        System.out.println("=======================================================================================================");
        for(int i = 0; i < po.getItemCount(); i++){
            System.out.printf("  %d. \t %-20s \t\t\t %-3d \t\t\t %-7.2f\n", (i+1), po.getOrderItems().get(i).getOrdItemName(), 
                    po.getOrderItems().get(i).getQuantity(),  po.getOrderItems().get(i).getTotalPrice());
        }
        System.out.println("=======================================================================================================");
        System.out.printf("          \t\t\t\t\t Total : \t\t %-7.2f\n", po.getTotalPOprice());
        System.out.println("=======================================================================================================");
        
        System.out.println("\n  Press Enter to continue...");
        sc.nextLine();
    }
    
    public static void aa(){
        Scanner sc = new Scanner(System.in);
        boolean leave = true, error;
        int choice;
        
        do{
            do{
                error = true;
                try{
                    choice = sc.nextInt();
                    switch(choice){
                        case 1:{
                            error = false;
                            break;
                        }
                        case 2:{
                            error = false;
                            leave = false;
                            break;
                        }
                        default:{
                            System.out.println("  Input Error, Please Try Again!!!");
                            break;
                        }
                    }
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!");
                }
                sc.nextLine();
            }while(error); 
        }while(leave);
    }
    
//    public static void clearConsole() {
//     try {
//            if (System.getProperty("os.name").contains("Windows")) {
//                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//            } else {
//                System.out.print("\033[H\033[2J");
//                System.out.flush();
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }  
//    }
}
