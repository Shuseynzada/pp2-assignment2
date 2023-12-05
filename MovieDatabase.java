import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MovieDatabase {
    private List<Movie> movies;
    private static String filepath = "Resources/movies.csv";

    static void addToFile(Movie m) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(m.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }