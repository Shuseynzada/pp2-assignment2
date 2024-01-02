package Source.UserManagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsersDatabase {
    private static String filepath = "Resources/users.csv";
    static List<User> users = loadUsersFromFile();

    // Method to change the file path for testing
    public static void setFilePath(String newPath) {
        filepath = newPath;
        users = loadUsersFromFile();
    }

    // Method to get the current file path
    public static String getFilePath() {
        return filepath;
    }

    public static void appendToFile(User u) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(u.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        users.add(u);
    }

    public static void updateFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, false))) {
            for (User user : users) {
                writer.write(user.toString());
                writer.newLine();
            }
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
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String hashedPassword = parts[2];

                    String[] movieStrings = parts[3].split(";");
                    Watchlist watchlist = new Watchlist();
                    watchlist.addToWatchList(movieStrings);
                    users.add(new User(id, username, hashedPassword, watchlist));
                } else if (parts.length >= 3) {
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

    public static void main(String[] args) {
        users.stream().forEach(u -> System.out.println(u));
    }
}