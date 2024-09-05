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
        boolean login, signup;
        int choice = 0;

        User user = new User();

        do {
            header();
            System.out.println("main menu");
            System.out.println("1.Login");
            System.out.println("2.Signup");
            System.out.print("Enter your choice > ");
            try {
                choice = scanner.nextInt();

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();  // Clear the invalid input from the scanner buffer
            }
            switch (choice) {
                case 1:
                    login = user.login();
                    if (login) {
                        System.out.println("Login success");
                    } else {
                        System.out.println("Login fail");
                    }
                    break;
                case 2:
                    signup = user.signup();
                    if (signup) {
                        System.out.println("Signup success");
                    } else {
                        System.out.println("Signup fail");
                    }
                    break;
                default:
                    System.out.println("Invalid input.");
            }
            clearConsole();
        } while (choice < 1 || choice > 2);

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

}
