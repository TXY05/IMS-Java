import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class IMS {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner scanner = new Scanner(System.in);
        boolean endProgram=false;
        int choice = 0;
                
        User user = new User();

        do {
            header();
            System.out.println("Main Menu");
            System.out.println("1.Login");
            System.out.println("2.Signup");
            System.out.println("3.Exit Program");
            System.out.print("Enter your choice > ");
            try {
                choice = scanner.nextInt();

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();  // Clear the invalid input from the scanner buffer
            }
            
            switch (choice) {
                case 1:
                    endProgram = user.login();
                    if (!endProgram) {
                        System.out.println("\nLogin Failed! Please try again later!");
                        systemPause();
                    } else{
                        System.out.println("\nLogin Success!");
                        systemPause();  
                    }
                    break;
                case 2:
                    endProgram = user.signup();
                    if (!endProgram) {
                        System.out.println("\nRegister Failed! Please try again later!");
                        System.out.println("Press Enter to Continue...");
                        scanner.nextLine();
                        scanner.nextLine();
                    } else{
                        System.out.println("\nRegister Success!");
                        systemPause();
                    }
                    break;
                case 3:
                    System.out.println("Program exitting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid input.");
            }
            clearConsole();
        } while (choice !=3);

    }
    
    public static void dashboard(){
        header();
        //lowLevelStock
        //viewItem
        //viewPO
        //goodreturn
    }
    

    public static void line() {
        System.out.println("==========================================================================================================");
    }

    public static void header() {
        try {
            FileReader reader = new FileReader("art.txt");
            int data = reader.read();
            while (data != -1) {
                System.out.print((char) data);

                data = reader.read();
            }

            System.out.println("");
            reader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        line();
        System.out.println("================================= Welcome To Inventory Management System =================================");
        line();

        String todayDay = LocalDate.now().getDayOfWeek().toString().toLowerCase();
        String todayDate = LocalDate.now().toString();
        String todayTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        System.out.println("Day : " + todayDay.substring(0, 1).toUpperCase() + todayDay.substring(1) + "\t\t\t\tDate : " + todayDate + "\t\t\t\tTime : " + todayTime);
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void systemPause(){
        Scanner sc = new Scanner(System.in);
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }
}
