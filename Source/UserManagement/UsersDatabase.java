package Source.UserManagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages user data, including reading from and writing to a CSV file.
 * This class provides methods for managing the user database.
 * 
 * @author Shamkhal Huseynzade
 * @version 1.0
 * @since 02/01/2024
 */
public class UsersDatabase {
    private static String filepath = "Resources/users.csv";
    static List<User> users = loadUsersFromFile();

    /**
     * Sets a new file path for the user database file.
     * 
     * @param newPath The new file path to set.
     */
    public static void setFilePath(String newPath) {
        filepath = newPath;
        users = loadUsersFromFile();
    }

    /**
     * Gets the current file path for the user database file.
     * 
     * @return The current file path.
     */
    public static String getFilePath() {
        return filepath;
    }

    /**
     * Appends a user's information to the user database file.
     * 
     * @param u The user to add to the database.
     */
    public static void appendToFile(User u) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(u.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        users.add(u);
    }

    /**
     * Updates the user database file with the current list of users.
     */
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

    /**
     * Loads user data from the user database file.
     * 
     * @return A list of User objects loaded from the file.
     */
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

    /**
     * Main method to display user information.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        users.forEach(u -> System.out.println(u));
    }
}
