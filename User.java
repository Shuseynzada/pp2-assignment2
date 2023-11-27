import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String username, password;
    private static String filepath = "users.csv";

    // Constructors
    public User(int id, String username, String password, boolean save) {
        this.id = id;
        this.username = username;
        this.password =  password;
        if(save) saveToFile();
    }
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.username;
    }

    //Setters
    public boolean setName(String newUsername, String password){
        if(this.password != password){
            System.out.println("Password doesn't match");
            return false;
        }
        this.username = newUsername;
        return true;
    }
    public boolean setPassword(String newPassword, String oldPassword){
        if(this.password != oldPassword){
            System.out.println("Password doesn't match");
            return false;
        }
        this.password = newPassword;
        return true;
    }

    //Database methods
    private void saveToFile(){
        saveToFile(new User(id, username, password));
    }
    private static void saveToFile(User u) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(u.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load users from the CSV file
    public static List<User> loadUsersFromFile() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(User.filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String hashedPassword = parts[2];
                    users.add(new User(id, username, hashedPassword));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public String toString() {
        return id + "," + username + "," + password;
    }

    public static void main(String[] args) {
        List<User> users = User.loadUsersFromFile();
        for (User user : users) {
            System.out.println(user.toString());
        }
    }
}