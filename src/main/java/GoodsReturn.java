import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GoodsReturn {
    
    public static void goodsReturnMenu(PurchaseOrder goodsReturn){
        Scanner sc = new Scanner(System.in);
        boolean leave = false;
        boolean invalid;
        int index = 0;
        int returnQty[] = new int[goodsReturn.getItemCount()];
        Arrays.fill(returnQty, 0);
        int leftQty[] = new int[goodsReturn.getItemCount()];
        double leftCost[] = new double[goodsReturn.getItemCount()];
        
        while(!leave){
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
            do{
                System.out.println("-1 to Exit");
                System.out.print("Select Item to Return: ");
            
                try{
                    index = sc.nextInt();
                    invalid = false;
                    if(index == -1){
                        leave = true;
                        invalid = false;
                    }
                    else{
                        System.out.print("Enter Quantity to Return: ");
                        returnQty[index - 1] = sc.nextInt();
                    }
                }catch (InputMismatchException e){
                        System.out.println("Please Enter Integer/N to Exit!");
                        sc.nextLine();
                        invalid = true;
                    }
            }while(invalid);
        }
    }
}
