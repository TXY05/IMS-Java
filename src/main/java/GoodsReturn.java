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
        int returnQty[] = new int[goodsReturn.getItemCount()];
        Arrays.fill(returnQty, 0); //Initialize all returnQty with 0
        int leftQty[] = new int[goodsReturn.getItemCount()];
        double leftCost[] = new double[goodsReturn.getItemCount()];
        int index = 0;
        
        while(!leave){
            //Display Goods Return
            showGoodsReturn(goodsReturn, returnQty, leftQty, leftCost);
            
            do{
                System.out.println("(Enter -1 to Exit)");
                System.out.println("Press Y to Complete Return");
                System.out.print("Select Item to Return: ");
            
                try{
                    index = sc.nextInt();
                    if(index == -1){ //If input = -1, Leave
                        leave = true;
                        invalid = false;
                    }
                    else if(index < 1 || index > max){ //Detect Invalid Input
                        invalid = true;
                        System.out.println("Please choose an option within the range.");
                    }
                    else{ //If valid
                        System.out.print("Enter Quantity to Return: ");
                        returnQty[index - 1] = sc.nextInt();
                        invalid = false;
                    }
                }catch (InputMismatchException e){
                    String input = sc.next().toUpperCase(); //Get To Upper Case
                    if(input.equals("Y")){ //If Y to Complete Return
                        ArrayList<PurchaseOrder> purchaseOrder = PurchaseOrder.readPOFromFile("PO.txt");
                        invalid = false;
                        leave = true;
                        //Empty Purchase Order File for Overwritting
                        try {
                            new FileWriter("PO.txt", false).close();
                        } catch (IOException j) {
                            System.out.println("Empty file failed!");
                        }
                        //Save all back into Purchase Order File with Updated Status
                        for (PurchaseOrder purchaseOrder1 : purchaseOrder) {
                        if (goodsReturn.getOrderID().equals(purchaseOrder1.getOrderID())) {
                            purchaseOrder1.setStatus("Return");
                            GoodsReceive.saveStatusTOPO(purchaseOrder1);
                        } else {
                            GoodsReceive.saveStatusTOPO(purchaseOrder1);
                        }
                    }
                    //Add Quantity into Item File
                    for(int i = 0; i < max; i++){
                        Item.addItemQty(goodsReturn.getOrderItems().get(i).getOrdItemID(), leftQty[i]);
                    }
                    //Save Goods Return To Goods Return File
                    saveGoodsReturnToFile(goodsReturn, returnQty);
                    System.out.println("Successfully Returned.");
                    }
                    else{ //Invalid Option
                        System.out.println("Please Enter Integer/-1 to Exit!");
                        sc.nextLine();
                        invalid = true;
                    }
                }
            }while(invalid);
        }
    }

    //Display Goods Return
    private static void showGoodsReturn(PurchaseOrder goodsReturn, int returnQty[], int leftQty[], double leftCost[]){
        System.out.println("\n==============================");
        System.out.printf("%20s\n", "Goods Return");
        System.out.println("==============================");
        System.out.printf("%s %-10s %-20s %-5s %s\n", "No", "Item ID", "Item Name", "Qty", "Total Cost");
        
            for(int i = 0; i < goodsReturn.getItemCount(); i++){
                String itemID = goodsReturn.getOrderItems().get(i).getOrdItemID();
                String itemName = PurchaseOrder.findItemName(goodsReturn, i);
                int itemQuantity = goodsReturn.getOrderItems().get(i).getQuantity();
                double itemCost = goodsReturn.getOrderItems().get(i).getTotalPrice();
                double unitPrice = goodsReturn.getOrderItems().get(i).getUnitPrice();
                leftQty[i] = itemQuantity - returnQty[i];
                leftCost[i] = itemCost - (returnQty[i]* unitPrice);

                System.out.printf("%d. %-10s %-20s %-5d %.2f\n",i + 1, itemID, itemName, itemQuantity, itemCost);
                System.out.printf("%s %-10d\n","Amount of Return:",returnQty[i]);
                System.out.printf("%s %-10d\n","Quantity Left:",leftQty[i]);
                System.out.printf("%s %-10f\n","Cost Left:",leftCost[i]);
                System.out.println("------------------------------");
        }
    }
    
    //Save Goods Return Into Goods Return File
    public static void saveGoodsReturnToFile(PurchaseOrder goodsReturn, int returnQty[]){
        String orderID = goodsReturn.getOrderID();
        int itemCount = goodsReturn.getItemCount();
        int itemLoop = 0;
        
        for (int j = 0; j < itemCount; j++){
            if(returnQty[j] > 0){
                itemLoop++;
            }
        }
        try(FileWriter writeGoods = new FileWriter("GoodsReturn.txt", true)){
            writeGoods.write(orderID + "|" + itemLoop);
            
            for(int i = 0; i < itemCount; i++){
                if(returnQty[i] > 0){
                    writeGoods.write("\n");
                    writeGoods.write(goodsReturn.getOrderItems().get(i).getOrdItemID() + "|" + returnQty[i] + 
                        "|" + goodsReturn.getOrderItems().get(i).getUnitPrice());
                }
            }
            writeGoods.write("\n");
            writeGoods.close();
        } catch (IOException e){
            System.out.println("Write GoodsReturn File Failed!");
            System.exit(1);
        }
    }
}