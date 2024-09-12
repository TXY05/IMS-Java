import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class GoodReceive{
    
    public static void displayGoodReceive() {
        int numCount = 0;
        ArrayList<PurchaseOrder> goodReceive = PurchaseOrder.readPOFromFile("PO.txt");
        Item.loadInventoryFromFile();
        
            System.out.println("Goods Receive\n");
            System.out.println("=============\n");
            System.out.println("Purchase Order\n");
            
        for(PurchaseOrder display : goodReceive){
            System.out.print(numCount+1 + " " + goodReceive.get(numCount).getOrderID() + "\t\t\n" + 
                    "\tItem List: ");
            
            for(int i = 0; i < goodReceive.get(numCount).getItemCount(); i++){
                for(String itemID : Item.getInventory().keySet()){
                    if(goodReceive.get(numCount).getOrderItems().get(i).getOrdItemID() == itemID){
                        System.out.print(Item.getInventory().get(itemID) + " ");
                    }
                }
            }
            System.out.println("\n");
        }
    }
    
    public static void GoodReceiveMenu(){
        int choice;
        ArrayList<PurchaseOrder> goodReceive = PurchaseOrder.readPOFromFile("PO.txt");
        GoodReceive.displayGoodReceive();
        System.out.print("\nSelect Order: ");
        Scanner sc = new Scanner(System.in);
        choice = sc.nextInt();
        
        showGoodReceive(goodReceive.get(choice - 1));
    }
    
    public static void showGoodReceive(PurchaseOrder goodReceive){
        
    }
}
