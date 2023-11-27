import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsersDatabase {
    private static String filepath = "users.csv"; 
    static List<User> users = loadUsersFromFile();
    
    static void saveToFile(User u) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(u.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<User> loadUsersFromFile() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
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
}