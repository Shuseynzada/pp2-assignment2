package MovieManagement;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MovieDatabase {
    static List<Movie> movies = loadMoviesFromFile();
    private static String filepath = "Resources/movies.csv";

    static void addToFile(Movie m) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(m.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void updateFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, false))) {
            for (Movie movie : movies) {
                writer.write(movie.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<Movie> loadMoviesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Movie movie = new Movie(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                movies.add(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public void listAllMovies() {
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}
