import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GoodsReturn {
    
    public static void goodsReturnMenu(PurchaseOrder goodsReturn){
        Scanner sc = new Scanner(System.in);
        boolean leave = false;
        boolean invalid;
        int max = goodsReturn.getOrderItems().size();
        int index = 0;
        int returnQty[] = new int[goodsReturn.getItemCount()];
        Arrays.fill(returnQty, 0);
        int leftQty[] = new int[goodsReturn.getItemCount()];
        double leftCost[] = new double[goodsReturn.getItemCount()];
        
        while(!leave){
            showGoodsReturn(goodsReturn, returnQty, leftQty, leftCost);
            
            do{
                System.out.println("-1 to Exit");
                System.out.println("Press Y to Complete Return");
                System.out.print("Select Item to Return: ");
            
                try{
                    index = sc.nextInt();
                    if(index == -1){
                        leave = true;
                        invalid = false;
                    }
                    else if(index < 1 || index > max){
                        invalid = true;
                        System.out.println("Please choose an option within the range.");
                    }
                    else{
                        System.out.print("Enter Quantity to Return: ");
                        returnQty[index - 1] = sc.nextInt();
                        invalid = false;
                    }
                }catch (InputMismatchException e){
                    String input = sc.next().toUpperCase();
                    if(input.equals("Y")){
                        ArrayList<PurchaseOrder> purchaseOrder = PurchaseOrder.readPOFromFile("PO.txt");
                        invalid = false;
                        leave = true;
                        try {
                            new FileWriter("PO.txt", false).close();
                        } catch (IOException j) {
                            System.out.println("Empty file failed!");
                        }
                        //Save Status to Purchase Order File
                        for (PurchaseOrder purchaseOrder1 : purchaseOrder) {
                        if (goodsReturn.getOrderID().equals(purchaseOrder1.getOrderID())) {
                            purchaseOrder1.setStatus("Return");
                            GoodsReceive.saveStatusTOPO(purchaseOrder1);
                        } else {
                            GoodsReceive.saveStatusTOPO(purchaseOrder1);
                        }
                    }
                    for(int i = 0; i < max; i++){
                        Item.addItemQty(goodsReturn.getOrderItems().get(i).getOrdItemID(), leftQty[i]);
                    }
                    saveGoodsReturnToFile();
                    System.out.println("Successfully Returned.");
                    }
                    else{
                        System.out.println("Please Enter Integer/-1 to Exit!");
                        sc.nextLine();
                        invalid = true;
                    }
                }
            }while(invalid);
        }
    }

    private static void showGoodsReturn(PurchaseOrder goodsReturn, int returnQty[], int leftQty[], double leftCost[]){
            System.out.println("Goods Return");
            System.out.println("============");
            for(int i = 0; i < goodsReturn.getItemCount(); i++){
                String itemID = goodsReturn.getOrderItems().get(i).getOrdItemID();
                String itemName = PurchaseOrder.findItemName(goodsReturn, i);
                int itemQuantity = goodsReturn.getOrderItems().get(i).getQuantity();
                double itemCost = goodsReturn.getOrderItems().get(i).getTotalPrice();
                double unitPrice = goodsReturn.getOrderItems().get(i).getUnitPrice();
                leftQty[i] = itemQuantity - returnQty[i];
                leftCost[i] = itemCost - (returnQty[i]* unitPrice);
            
                System.out.println(i+1 + ". " + itemID + "\t\t" + itemName + "\t\t" + itemQuantity + "\t\t" + itemCost);
                System.out.println("Amount of Return: " + returnQty[i]);
                System.out.println("Quantity Left: " + leftQty[i]);
                System.out.println("Cost Left: " + leftCost[i]);
        }
    }
    
    public static void saveGoodsReturnToFile(){
        
    }
}