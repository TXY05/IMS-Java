import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class GoodReceive{
    
    public static void displayGoodReceive(ArrayList<PurchaseOrder> goodReceive) {
        
            System.out.println("Goods Receive");
            System.out.println("=============");
            System.out.println("Purchase Order");
            
        for(int i = 0; i < goodReceive.size(); i++){
            int itemCount = goodReceive.get(i).getItemCount();
            
            System.out.print(i+1 + " " + goodReceive.get(i).getOrderID() + "\t\t\n" + 
                    "\tItem List: ");
            
            for(int j = 0; j < itemCount; j++){
                String itemName = PurchaseOrder.findItemName(goodReceive.get(i), j);
                System.out.print(itemName + " ");
            }
            System.out.println("\n");
        }
    }
    
    public static void GoodsMenu(){
        int choice;
        ArrayList<PurchaseOrder> goodReceive = PurchaseOrder.readPOFromFile("PO.txt");
        GoodReceive.displayGoodReceive(goodReceive);
        System.out.print("\nSelect Order: ");
        Scanner sc = new Scanner(System.in);
        choice = sc.nextInt();
        
        showGoodReceive(goodReceive.get(choice - 1));
    }
    
    public static void showGoodReceive(PurchaseOrder goodReceive){
        int itemNum = goodReceive.getItemCount();
        String supplierName = goodReceive.getSupp().getName();
        System.out.println(goodReceive.getOrderID() + "\t" + supplierName);
        for(int i = 0; i < itemNum; i++){
            String itemID = goodReceive.getOrderItems().get(i).getItem().getItemId();
            String itemName = goodReceive.getOrderItems().get(i).getItem().getItemName();
            int itemQuantity = goodReceive.getOrderItems().get(i).getQuantity();
            double itemCost = goodReceive.getOrderItems().get(i).getTotalPrice();
            
            System.out.print(i+1 + ". " + itemID + "\t\t" + itemName + "\t\t" + itemQuantity + "\t\t" + itemCost + "\n");
        }
    }
    
//    public static void goodsOption(){
//        int choice = 0;
//        
//        switch(choice){
//            case 1:
//                goodReceiveMenu();
//                break;
//            case 2:
//                goodReturnMenu();
//                break;
//            default:
//                System.out.println("Invalid options!");
//                System.out.println("Please choose the option within the range.");
//                break;
//        }
//    }
}
