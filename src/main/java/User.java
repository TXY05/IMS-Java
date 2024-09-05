import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User extends Person {

    private static String userFile = "User.txt";
    private String password;
    private BufferedReader reader = null;
    private String line = "";
    private Scanner scanner = new Scanner(System.in);

    public User() {

    }

    public User(String name, String email, String password) {
        super(name, email);
        this.password = password;
    }

    public User(String name, String email) {
        super(name, email);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login() {

        System.out.print("Enter Email > ");
        String email = scanner.nextLine();

        System.out.print("Enter Password > ");
        this.password = scanner.nextLine();
        try {
            reader = new BufferedReader(new FileReader(userFile));
            while ((line = reader.readLine()) != null) {

                String[] row = line.split("[|]");

                if (email.compareTo(row[1]) == 0 && password.compareTo(row[2]) == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean signup() {
        boolean emailRepeat = false;

        System.out.print("Enter Name > ");
        String name = scanner.nextLine();

        System.out.print("Enter Email > ");
        String email = scanner.nextLine();

        System.out.print("Enter Password > ");
        String password = scanner.nextLine();

        User newUser = new User(name, email, password);

        try {
            reader = new BufferedReader(new FileReader(userFile));
            while ((line = reader.readLine()) != null) {

                String[] row = line.split("[|]");

                if (email.compareTo(row[1]) == 0) {
                    System.out.println("Email repeated!");
                    emailRepeat = true;
                    return false;
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true))) {
            writer.append(newUser.getName() + "|" + newUser.getEmail() + "|" + newUser.getPassword());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
