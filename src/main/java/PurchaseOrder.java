import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PurchaseOrder {
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private String orderID;
    private ArrayList<OrderItem> orderItems;
    private String suppID;
    private LocalDate orderDate;
    private String status;
    private double totalPOprice;
    private int itemCount;

    public PurchaseOrder(ArrayList<OrderItem> orderItems, Supplier supp) {
        this.orderID = generatePOId();
        this.orderItems = orderItems;
        this.suppID = supp.getId();
        this.orderDate = LocalDate.now();
        this.status = "Pending";
        this.totalPOprice = calTotalPOprice(orderItems);
        this.itemCount = orderItems.size();
    }
    
    public PurchaseOrder(String orderID, ArrayList<OrderItem> orderItems, String suppID, 
            LocalDate date, String status, double totalPrice, int itemCount){
        this.orderID = orderID;
        this.orderItems = orderItems;
        this.suppID = suppID;
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
    
    public String getSuppID(){
        return suppID;
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
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public String generatePOId() {
        ArrayList<PurchaseOrder> poList = readPOFromFile("PO.txt");
        
        Set<String> existingIDs = new HashSet<>();
        for (PurchaseOrder po : poList) {
            existingIDs.add(po.getOrderID());
        }
        
        String poID;
        do {
            poID = String.format("PO%04d", idCounter.incrementAndGet());
        } while (existingIDs.contains(poID)); // Keep generating until a unique ID is found
        return poID;
    }
    
    public double calTotalPOprice(ArrayList<OrderItem> orderItems){
        double total = 0;
        for (int i = 0; i < (orderItems.size()); i++){
            total += (orderItems.get(i).getTotalPrice());
        }
        return total;
    }
    
    public static ArrayList<Item> readNameAndIDFromItem(String filename){
        ArrayList<Item> ListItems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length >= 2){
                    String itemId = parts[0];
                    String itemName = parts[1];
                    Item items = new Item(itemId, itemName);
                    ListItems.add(items);
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to Open File !!!");
        }
        return ListItems;
    }
    
    public static void poMenu(){
        Scanner sc = new Scanner(System.in);
        boolean leave = true, error;
        int choice;
        
        do{
            System.out.println("=======================================================================================================");
            System.out.println("  Purchase Order Menu");
            System.out.println("=======================================================================================================");
            System.out.println("  1. Create Purchase Order\n  2. View PO History\n  3. Return Main Menu");
            
            do{
                error = true;
                System.out.print("\n  Select: ");
                try{
                    choice = sc.nextInt();
                    switch(choice){
                        case 1:{
                            error = false;
                            createPOMenu();
                            break;
                        }
                        case 2:{
                            error = false;
                            poHistory();
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
    
    public static void createPOMenu(){
        Supplier selSupp;
        ArrayList<OrderItem> ordList = new ArrayList<>();
        
        Scanner sc = new Scanner(System.in);
        boolean leave = true, error;
        int choice;
        
        selSupp = selectedSupp(ordList);
        addOrdItems(ordList, selSupp);
                
        do{ 
            listOrdItems(ordList);
            System.out.println("  Options: ");
            System.out.println("  1. Continue Add Item\n  2. Reduce Item Quantity\n  3. Edit Unit Price\n  4. Make Order\n  5. Return");
            do{
                error = true;
                System.out.printf("\n  Select: ");
                try{
                    choice = sc.nextInt();
                    switch(choice){
                        case 1:{
                            error = false;
                            addOrdItems(ordList, selSupp);
                            break;
                        }
                        case 2:{
                            error = false;
                            reduceOrdItemsQty(ordList);
                            break;
                        }
                        case 3:{
                            error = false;
                            editUnitPrice(ordList);
                            break;
                        }
                        case 4:{
                            error = false;
                            leave = false;
                            createPO(ordList, selSupp);
                            break;
                        }
                        case 5:{
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
    
    public static Supplier selectedSupp(ArrayList<OrderItem> orderList){
        ArrayList<Supplier> ListSupplier = Supplier.getSupplier();
        ArrayList<Item> itemList = readNameAndIDFromItem("items.txt");
        
        Scanner sc = new Scanner(System.in);
        boolean leave = true, error;
        int selectedIndex = 0;
        char choice;

        do{ 
            System.out.println("=======================================================================================================");
            System.out.println("  Supplier List: ");
            System.out.println("=======================================================================================================");
            for (int i = 0; i < ListSupplier.size(); i++) {
                System.out.printf("  %-2d. %s\n", (i+1), ListSupplier.get(i).getName());
            }
            
            do {
                error = true;
                System.out.print("\n  Select: ");
                try {
                    selectedIndex = sc.nextInt()-1;
                    
                    if (selectedIndex < 0 || selectedIndex >= ListSupplier.size()) {
                        System.out.println("  Error: Selection out of range. Please select a number between 1 and " + ListSupplier.size());
                    }else{
                        error = false;
                    }
                    
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!");
                }
                sc.nextLine();
            }while(error);
            
            System.out.println("=======================================================================================================");
            System.out.printf("  Supplier Selected: %s\n", ListSupplier.get(selectedIndex).getName());
            System.out.println("=======================================================================================================");
            for(int i = 0; i < ListSupplier.get(selectedIndex).getItemList().size(); i++){
                String itemID = ListSupplier.get(selectedIndex).getItemList().get(i);
                for(int j = 0; j < itemList.size(); j++){
                    if(itemID.equals(itemList.get(j).getItemId())){
                        System.out.printf("  %2d. %s\n", (i+1), itemList.get(j).getItemName());
                    }
                  }
            }
            
            do{
                error = true;
                System.out.print("\n  Confirm Selection ? (Y/N): ");
                try {
                    choice = Character.toUpperCase(sc.next(".").charAt(0));
                    switch (choice){
                        case 'Y':{
                            error = false;
                            leave = false;
                            for(int i = 0; i < ListSupplier.get(selectedIndex).getItemList().size(); i++){
                                String itemID = ListSupplier.get(selectedIndex).getItemList().get(i);
                                for(int j = 0; j < itemList.size(); j++){
                                    if(itemID.equals(itemList.get(j).getItemId())){
                                        orderList.add(new OrderItem(itemList.get(j)));
                                    }
                                  }
                            }
                            break;
                        }
                        case 'N':{
                            error = false;
                            break;
                        }
                        default:{
                            System.out.println("  Input Error, Please Try Again!!!");
                        }
                    }
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!");
                }
                sc.nextLine();
            }while(error);
        }while(leave);
        
        Supplier selSupp = ListSupplier.get(selectedIndex);
        System.out.println("\n");
        
        return selSupp;
    }
    
    public static void addOrdItems(ArrayList<OrderItem> orderList, Supplier selSupp){
        Scanner sc = new Scanner(System.in);
        boolean leave = true, error;
        int selectedIndex = 0, quantity = 0;
        double unitPrice = 0;
        char choice;
        
        do{
            System.out.println("=======================================================================================================");
            System.out.println("  Available Items:");
            System.out.println("=======================================================================================================");
            for(int i = 0; i < orderList.size(); i++){
                System.out.printf("  %2d. %s\n", (i+1), orderList.get(i).getOrdItemName());
            }
            
            listOrdItems(orderList);
            do{
                error = true;
                System.out.print("  Select Item to add: ");
                try{
                    selectedIndex = sc.nextInt()-1;
                    if(selectedIndex >= 0 && selectedIndex < (orderList.size())){
                        error = false;
                    }else{
                        System.out.println("  Input Error, Please Try Again!!!\n");
                    }   
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!\n");
                }
                sc.nextLine();
            }while(error);

            do{
                error = true;
                System.out.println("=======================================================================================================");
                System.out.print("  Quantity: ");
                try{
                    quantity = sc.nextInt();
                    if(quantity < 1){
                        System.out.println("  Cannot Be Less Than 1 !!!");
                    }else{
                        error = false;
                        orderList.get(selectedIndex).setQuantity(quantity);
                    }
                    
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!");
                }
                sc.nextLine();
            }while(error);
            
            if(orderList.get(selectedIndex).getUnitPrice() == 0.0){
                do{
                    error = true;
                    System.out.println("=======================================================================================================");
                    System.out.print("  Unit Price: ");
                    try{
                        unitPrice = sc.nextDouble();
                        if(unitPrice < 1){
                            System.out.println("  Cannot Be Less Than 1 !!!");
                        }else{
                            error = false;
                            orderList.get(selectedIndex).setUnitPrice(unitPrice);
                        }

                    }catch(InputMismatchException e){
                        System.out.println("  Input Error, Please Try Again!!!");
                    }
                    sc.nextLine();
                }while(error);
            }
            
            do{
                error = true;
                System.out.println("=======================================================================================================");
                System.out.print("  Continue Adding Item ? (Y/N): ");
                try{
                    choice = Character.toUpperCase(sc.next(".").charAt(0));
                    switch (choice){
                        case 'Y':{
                            error = false;
                            break;
                        }
                        case 'N':{
                            error = false;
                            leave = false;
                            break;
                        }
                        default:{
                            System.out.println("  Input Error, Please Try Again!!!");
                        }
                    }
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!");
                }
                sc.nextLine();
            }while(error); 
            
        }while(leave);
    }
    
    public static void listOrdItems(ArrayList<OrderItem> orderList){
        boolean display = true;
        int noCount = 1;
        
        Collections.sort(orderList, Comparator.comparing(OrderItem::getQuantity).reversed());
        System.out.println("=======================================================================================================");
        System.out.println("  Items Selected: ");
        System.out.println("=======================================================================================================");
        System.out.println("  No. \t Items \t\t\t Quantity \t   Unity Price \t\t Total Price");
        System.out.println("=======================================================================================================");
        
        for (int i = 0; i < orderList.size(); i++){
            if(orderList.get(i).getQuantity() != 0){
                System.out.printf("  %2d. \t %-20s \t %d \t\t   %-8.2f \t\t %-8.2f\n" , noCount, orderList.get(i).getOrdItemName(), orderList.get(i).getQuantity(),
                    orderList.get(i).getUnitPrice(), orderList.get(i).getTotalPrice());
                display = false;
                noCount++;
            } 
        }
        
        if(display){
            System.out.println("  [EMPTY]");
            System.out.println("=======================================================================================================");
        }else{
            System.out.println("=======================================================================================================");
        }
    }    
    
    public static void reduceOrdItemsQty(ArrayList<OrderItem> orderList){ 
        Scanner sc = new Scanner(System.in);
        boolean leave = true, error;
        int selectedIndex = 0, choice, quantity;
        
        do{
            listOrdItems(orderList);
            do{
                error = true;
                System.out.print("  Select Item you want to reduce: ");
                try{
                    selectedIndex = sc.nextInt()-1;
                    if(selectedIndex >= 0 && selectedIndex < (orderList.size())){
                        error = false;
                    }else{
                        System.out.println("  Input Error, Please Try Again!!!\n");
                    }   
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!\n");
                }
                sc.nextLine();
            }while(error);
            
            do{
                error = true;
                System.out.println("=======================================================================================================");
                System.out.print("  Quantity: ");
                try{
                    quantity = sc.nextInt();
                    if(quantity < 1){
                        System.out.println("  Cannot Be Less Than 1 !!!");
                    }else{
                        error = false;
                        orderList.get(selectedIndex).reduceQuantity(quantity);
                    }
                    
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!");
                }
                sc.nextLine();
            }while(error);
            
            do {
                System.out.println("=======================================================================================================");
                System.out.print("  Continue Reducing Item ? (Y/N): ");
                try{
                    choice = Character.toUpperCase(sc.next(".").charAt(0));
                        switch (choice){
                            case 'Y':{
                                error = false;
                                break;
                            }
                            case 'N':{
                                error = false;
                                leave = false;
                                break;
                            }
                            default:{
                                System.out.println("  Input Error, Please Try Again!!!");
                            }
                        }
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!");
                }
                sc.nextLine();
            }while(error);
        }while(leave);
        
    }
    
    public static void editUnitPrice(ArrayList<OrderItem> orderList){
        Scanner sc = new Scanner(System.in);
        boolean leave = true, error;
        int selectedIndex = 0, choice;
        double unitPrice;
        
        do{
            listOrdItems(orderList);
            do{
                error = true;
                System.out.print("  Select Item you want to edit Price: ");
                try{
                    selectedIndex = sc.nextInt()-1;
                    if(selectedIndex >= 0 && selectedIndex < (orderList.size())){
                        error = false;
                    }else{
                        System.out.println("  Input Error, Please Try Again!!!\n");
                    }   
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!\n");
                }
                sc.nextLine();
            }while(error);
            
            do{
                error = true;
                System.out.println("=======================================================================================================");
                System.out.print("  Unit Price: ");
                try{
                    unitPrice = sc.nextDouble();
                    if(unitPrice < 1){
                        System.out.println("  Cannot Be Less Than 1 !!!");
                    }else{
                        error = false;
                        orderList.get(selectedIndex).setUnitPrice(unitPrice);
                    }

                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!");
                }
                sc.nextLine();
            }while(error);
            
            do {
                System.out.println("=======================================================================================================");
                System.out.print("  Continue Editing Unit Price ? (Y/N): ");
                try{
                    choice = Character.toUpperCase(sc.next(".").charAt(0));
                        switch (choice){
                            case 'Y':{
                                error = false;
                                break;
                            }
                            case 'N':{
                                error = false;
                                leave = false;
                                break;
                            }
                            default:{
                                System.out.println("  Input Error, Please Try Again!!!");
                            }
                        }
                }catch(InputMismatchException e){
                    System.out.println("  Input Error, Please Try Again!!!");
                }
                sc.nextLine();
            }while(error);
        }while(leave);
    }
    
    public static void createPO(ArrayList<OrderItem> orderList, Supplier supp){
        Scanner sc = new Scanner(System.in);
        boolean error;
        char choice;
       
        for(int i = orderList.size() - 1; i >= 0; i--){
            if(orderList.get(i).getQuantity() == 0){
                orderList.remove(i);
            }
        }
        
        PurchaseOrder po = new PurchaseOrder(orderList, supp);
        showPO(po, supp);
        
        do{
            error = true;
            System.out.print("  Confirm Create Purchase Order (Y/N): ");
            try{
                choice = Character.toUpperCase(sc.next(".").charAt(0));
                switch (choice){
                    case 'Y':{
                        error = false;
                        savePOToFile(po);
                        sc.nextLine();
                        break;
                    }
                    case 'N':{
                        error = false;
                        break;
                    }
                    default:{
                        System.out.println("  Input Error, Please Try Again!!!\n");
                    }
                }
            }catch(InputMismatchException e){
                System.out.println("  Input Error, Please Try Again!!!\n");
            }
            sc.nextLine();
        }while(error);
    }
    
    public static void savePOToFile(PurchaseOrder po) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("PO.txt", true))) {
            bw.write(po.getOrderID() + "|" +  po.getSuppID() + "|" + po.getOrderDate() + "|" + 
                    po.getStatus() + "|" + po.getTotalPOprice() + "|" + po.getItemCount());
            for (int i = 0; i < po.getItemCount(); i++){
                bw.newLine();
                bw.write(po.getOrderItems().get(i).getOrdItemID() + "|" + po.getOrderItems().get(i).getQuantity() + "|" +
                        po.getOrderItems().get(i).getUnitPrice());
            }
            bw.newLine();
            System.out.println("  Order Successful!!");
            
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("  Something Went Wrong !!!");
            System.exit(1);
        }
    }
     
    public static ArrayList<PurchaseOrder> readPOFromFile(String filename){
        ArrayList<PurchaseOrder> poList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<OrderItem> orderList = new ArrayList<>();
                String[] parts = line.split("\\|");
                if (parts.length == 6){
                    String poID = parts[0];
                    String suppID = parts[1];
                    LocalDate date = LocalDate.parse(parts[2]);
                    String status = parts[3];
                    double totalPrice = Double.parseDouble(parts[4]);
                    int itemCount = Integer.parseInt(parts[5]);
                
                    for(int i = 0; i < itemCount; i++){
                        line = br.readLine();
                        String[] part = line.split("\\|");
                        if (part.length == 3){
                            String itemID = part[0];
                            int quantity = Integer.parseInt(part[1]);
                            double unitPrice = Double.parseDouble(part[2]);
                            OrderItem ordItem = new OrderItem(itemID, quantity, unitPrice);
                            orderList.add(ordItem);
                        }
                    }
                    PurchaseOrder po = new PurchaseOrder(poID, orderList, suppID, date, status, totalPrice, itemCount);
                    poList.add(po);
                }
            }
    
        } catch (FileNotFoundException e){
            System.out.println("  File Not Found !!!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("  Something Went Wrong !!!");
            System.exit(1);
        }
        return poList;
    }
    
    public static void poHistory(){
        Scanner sc = new Scanner(System.in);
        ArrayList<PurchaseOrder> poList = readPOFromFile("PO.txt");
        ArrayList<Supplier> ListSupplier = Supplier.getSupplier();
        boolean leave = true, error;
        int choice;
        String tempSuppName = "";
        
        do{
            System.out.println("\n  Purchase Order History: ");
            System.out.println("=======================================================================================================");
            System.out.println("       Order No." + "\tSupplier Name\t\t       " + "Order Date\t" + "Order Price\t" + "Status \t");
            System.out.println("=======================================================================================================");
            
            for (int i = 0; i < poList.size(); i++){
                for(int j = 0; j < ListSupplier.size(); j++){
                    if(poList.get(i).getSuppID().equals(ListSupplier.get(j).getId())){
                        tempSuppName = ListSupplier.get(j).getName();
                    }
                }
                System.out.printf(" %3d.  %s\t\t%-30s %s \t%-8.2f \t%-10s\n", (i+1), poList.get(i).getOrderID(), tempSuppName, 
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
            System.out.println("  1. PurchaseOrder ID(Asc)\n  2. PurchaseOrder ID(Dec)\n  3. Date(Asc)");
            System.out.print("  4. Date(Dec)\n  5. Price(Asc)\n  6. Price(Dec)\n  7. Status(Asc)\n  Please Select: ");
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
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getOrderDate));
                        break;
                    }
                    case 4:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getOrderDate).reversed());
                        break;
                    }
                    case 5:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getTotalPOprice));
                        break;
                    }
                    case 6:{
                        error = false;
                        Collections.sort(poList, Comparator.comparing(PurchaseOrder::getTotalPOprice).reversed());
                        break;
                    }
                    case 7:{
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
                choice = (sc.nextInt()-1);
                if(choice >= 0 && choice < poList.size()){
                    error = false;
                }else{
                    System.out.println("  Input Error, Please Try Again!!!");
                }
            }catch(InputMismatchException e){
                System.out.println("  Input Error, Please Try Again!!!");
            }
            sc.nextLine();
        }while(error); 
        
        Supplier selSupp = findSupplier(poList.get(choice).getSuppID());
        showPO(poList.get(choice), selSupp);
    }
    
    public static void showPO(PurchaseOrder po, Supplier supp){
        Scanner sc = new Scanner(System.in);
        
        System.out.println("\n=======================================================================================================");
        System.out.println("\t\t\t\t\t\tPurchase Order");
        System.out.println("\t\t\t\t\t    ======================");
        System.out.println("\n  Vendors : \t\t\t\t\t\t\t\t\t Purchase Order ID: ");
        System.out.printf("  %-30s \t\t\t\t\t\t #%s", supp.getName(), po.getOrderID());
        System.out.printf("\n  %-20s", supp.getAddress().getAddress());
        System.out.printf("\n  %-20s          \t\t\t\t\t\t Date: ", supp.getAddress().getCity());
        System.out.printf("\n  %-6d      \t\t\t\t\t\t\t\t\t %s: ", supp.getAddress().getPostalCode(),po.getOrderDate());
        System.out.println("\n\n  ItemID \t Items \t\t\t Quantity \t   Unity Price \t\t Total Price");
        System.out.println("=======================================================================================================");
        
        for(int i = 0; i < po.getItemCount(); i++){
            String tempName = findItemName(po, i);
            System.out.printf("  #%s \t %-20s \t %-3d \t\t   %-7.2f \t\t %-7.2f\n", po.getOrderItems().get(i).getOrdItemID(), tempName, 
                po.getOrderItems().get(i).getQuantity(),  po.getOrderItems().get(i).getUnitPrice(),po.getOrderItems().get(i).getTotalPrice());
        }
        
        System.out.println("=======================================================================================================");
        System.out.printf("          \t\t\t\t\t\t   Total(RM) : \t\t %-7.2f\n", po.getTotalPOprice());
        System.out.println("=======================================================================================================");
        
        if(po.getStatus().equals("Return")){
            System.out.printf("\n  Status : %s\n", po.getStatus());
            System.out.println("  ====================");
            System.out.println("  Return Items : ");
            System.out.println("\n  ItemID \t Items \t\t\t Quantity \t   Unity Price \t\t Total Price");
            System.out.println("=======================================================================================================");
            
//            for(int i = 0; i < po.getItemCount(); i++){
//                System.out.printf("  #%s \t %-20s \t %-3d \t\t   %-7.2f \t\t %-7.2f\n", po.getOrderItems().get(i).getOrdItemID(), tempName, 
//                po.getOrderItems().get(i).getQuantity(),  po.getOrderItems().get(i).getUnitPrice(),po.getOrderItems().get(i).getTotalPrice());
//            }
            
            System.out.println("=======================================================================================================");
            System.out.printf("          \t\t\t\t\t    Return Total(RM) : \t\t %-7.2f\n", po.getTotalPOprice());
            
        }else if(po.getStatus().equals("Receive")){
            System.out.printf("\n  Status : %s\n", po.getStatus());
            System.out.println("  ====================");
            System.out.println("  Return Items : ");
            System.out.println("\n  [None]\n");
            
        }else{
            System.out.printf("  Status : %s\n", po.getStatus());
        }
        System.out.println("=======================================================================================================");
        System.out.println("\n  Press Enter to continue...");
        sc.nextLine();
    }
    
    public static Supplier findSupplier(String idIndex){
        ArrayList<Supplier> supplierList = Supplier.getSupplier();
        int supplierIndex = 0;
        
        for(int i = 0; i < supplierList.size(); i++){
            if(idIndex.equals(supplierList.get(i).getId())){
                supplierIndex = i;
            }
        }
        return supplierList.get(supplierIndex);
    }
    
    public static String findItemName(PurchaseOrder po, int index ){
        ArrayList<Item> itemList = readNameAndIDFromItem("items.txt");
        int itemIndex = 0;
        
        for(int i = 0; i < itemList.size(); i++){
            if(po.getOrderItems().get(index).getOrdItemID().equals(itemList.get(i).getItemId())){
                itemIndex = i;
            }
        }
        return itemList.get(itemIndex).getItemName();
    }
}
