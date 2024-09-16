import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class GoodsReceive{
    
    public static void GoodsMenu(){
        Scanner sc = new Scanner(System.in);
        ArrayList<PurchaseOrder> goodsReceive = PurchaseOrder.readPOFromFile("PO.txt");
        int choice = 0;
        int max = goodsReceive.size();
        boolean invalid = false;
        
        displayGoodReceive(goodsReceive);
        
        do{
            System.out.print("Select Order: ");
            try{
                choice = sc.nextInt();
                invalid = choice < 1 || choice > max;
                if(invalid){
                    System.out.println("Please choose the option within the range.");
                }
            }catch(InputMismatchException e){
            System.out.println("Invalid Input, Please try again!");
            sc.next();
            invalid = true;
            }
        }while(invalid);
        showGoodReceive(goodsReceive.get(choice - 1));
    }
    
    private static void displayGoodReceive(ArrayList<PurchaseOrder> goodsReceive) {
        
            System.out.println("Goods Receive");
            System.out.println("=============");
            System.out.println("Purchase Order");
            
        for(int i = 0; i < goodsReceive.size(); i++){
            int itemCount = goodsReceive.get(i).getItemCount();
            
            System.out.print(i+1 + " " + goodsReceive.get(i).getOrderID() + "\t\t\n" + 
                    "\tItem List: ");
            
            for(int j = 0; j < itemCount; j++){
                String itemName = PurchaseOrder.findItemName(goodsReceive.get(i), j);
                System.out.print(itemName + " ");
            }
            System.out.println("\n");
        }
    }
    
    private static void showGoodReceive(PurchaseOrder goodsReceive){
        int itemNum = goodsReceive.getItemCount();
        Supplier supplier = PurchaseOrder.findSupplier(goodsReceive.getSuppID());
        System.out.println(goodsReceive.getOrderID() + "\t" + supplier.getName());
        
        for(int i = 0; i < itemNum; i++){
            String itemID = goodsReceive.getOrderItems().get(i).getOrdItemID();
            String itemName = PurchaseOrder.findItemName(goodsReceive, i);
            int itemQuantity = goodsReceive.getOrderItems().get(i).getQuantity();
            double itemCost = goodsReceive.getOrderItems().get(i).getTotalPrice();
            
            System.out.println(i+1 + ". " + itemID + "\t\t" + itemName + "\t\t" + itemQuantity + "\t\t" + itemCost);
        }
        System.out.println("\n");
        goodsOption(goodsReceive);
    }
    
    private static void goodsOption(PurchaseOrder goodsReceive){
        Scanner sc = new Scanner(System.in);
        int choice;
        
        System.out.println("1. Confirm Receive");
        System.out.println("2. Goods Return");
        System.out.print("Select Option: ");
        choice = sc.nextInt();
        
        switch(choice){
            case 1 -> {
                confirmReceive(goodsReceive);
                return;
            }
            case 2 -> GoodsReturn.goodsReturnMenu(goodsReceive);
            default -> {
                System.out.println("Invalid options!");
                System.out.println("Please choose the option within the range.");
            }
        }
    }
    
    public static void saveStatusTOPO(PurchaseOrder goodsReceive){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("PO.txt", true))) {
            bw.write(goodsReceive.getOrderID() + "|" +  goodsReceive.getSuppID() + "|" + goodsReceive.getOrderDate() + "|" + 
                    goodsReceive.getStatus() + "|" + goodsReceive.getTotalPOprice() + "|" + goodsReceive.getItemCount());
            for (int i = 0; i < goodsReceive.getItemCount(); i++){
                bw.newLine();
                bw.write(goodsReceive.getOrderItems().get(i).getOrdItemID() + "|" + goodsReceive.getOrderItems().get(i).getQuantity() + "|" +
                        goodsReceive.getOrderItems().get(i).getUnitPrice());
            }
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Writing PO file failed!");
            System.exit(1);
        }
    }

    private static void confirmReceive(PurchaseOrder goodsReceive) {
        Scanner sc = new Scanner(System.in);
        ArrayList<PurchaseOrder> purchaseOrder = PurchaseOrder.readPOFromFile("PO.txt");
        boolean invalid = false;
        char input;
        
        System.out.println("Are you sure to confirm order?");
        System.out.print("Y/N: ");
        input = Character.toUpperCase(sc.next(".").charAt(0));
        
        do{
            switch (input) {
                case 'Y' -> {
                    try{
                        new FileWriter("PO.txt", false).close();
                    }catch (IOException e){
                        System.out.println("Empty file failed!");
                    }       for(PurchaseOrder purchaseOrder1 : purchaseOrder) {
                        if(goodsReceive.getOrderID().equals(purchaseOrder1.getOrderID())){
                            purchaseOrder1.setStatus("Receive");
                            saveStatusTOPO(purchaseOrder1);
                        }
                        else{
                            saveStatusTOPO(purchaseOrder1);
                        }
                        
                    }       System.out.println("Successfully Received.");
                }
                case 'N' -> System.out.println("Order not receive");
                default -> {
                    System.out.println("Please enter Y or N");
                    invalid = true;
                }
            }
        }while(invalid);
    }
}
