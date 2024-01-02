package Source.MovieManagement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Source.Exceptions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Manages movie data, including reading from and writing to a CSV file.
 * This class provides methods for managing the movie database.
 * 
 * @author Emil Hajiyev
 * @version 1.0
 * @since 02/01/2024
 */
public class MovieDatabase {
    private static String filepath = "Resources/movies.csv";
    static List<Movie> movies = loadMoviesFromFile();

    /**
     * Adds a new movie to the movie database file.
     * 
     * @param m The movie to add to the database.
     */
    public static void addToFile(Movie m) {
        m.setId(movies.size());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(m.toString());
            writer.newLine();
            movies.add(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the movie database file with the current list of movies.
     */
    public static void updateFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, false))) {
            for (Movie movie : movies) {
                writer.write(movie.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads movie data from the movie database file.
     * 
     * @return A list of Movie objects loaded from the file.
     */
    static List<Movie> loadMoviesFromFile() {
        List<Movie> resultMovies = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    Movie movie = new Movie(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]),
                            Integer.parseInt(data[4]));
                    resultMovies.add(movie);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading movies: " + e.getMessage());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (InvalidRunningTimeException e) {
            e.printStackTrace();
        } catch (InvalidReleaseYearException e) {
            e.printStackTrace();
        }
        return resultMovies;
    }

    /**
     * Retrieves a list of movies by their indices.
     * 
     * @param movieIDs The set of movie indices to retrieve.
     * @return A list of Movie objects corresponding to the given indices.
     */
    public static List<Movie> getMoviesByIndex(Set<Integer> movieIDs) {
        List<Movie> resMovies = new ArrayList<>();
        movieIDs.stream().filter(i -> i >= 0 && i < movies.size()).forEach(i -> resMovies.add(movies.get(i)));
        return resMovies;
    }

    /**
     * Lists all movies in the movie database.
     */
    public static void listAllMovies() {
        movies.stream().forEach(m -> System.out.println(m));
    }

    /**
     * Gets the list of all movies in the movie database.
     * 
     * @return A list of all Movie objects in the movie database.
     */
    public static List<Movie> getMovies() {
        return movies;
    }
}
