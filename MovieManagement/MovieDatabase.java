package MovieManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MovieDatabase  {
    private static String filepath = "Resources/movies.csv";
    private static int nextMovieIndex = 1;
    static List<Movie> movies = loadMoviesFromFile();

    public static void addToFile(Movie m) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(m.toString());
            writer.newLine();
            nextMovieIndex++; // Increment the index for the next movie
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    static List<Movie> loadMoviesFromFile() {
        List<Movie> resultMovies = new ArrayList<>();
        int maxIndex = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int index = Integer.parseInt(data[0]);
                maxIndex = Math.max(maxIndex, index);
                if (data.length >= 5) {
                    Movie movie = new Movie(data[1], data[3], Integer.parseInt(data[2]), Integer.parseInt(data[4]));
                    resultMovies.add(movie);
                }
            }
            nextMovieIndex = maxIndex + 1; // Set the next index
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

    public static List<Movie> getMoviesByIndex(Set<Integer> movieIDs) {
        List<Movie> resMovies = new ArrayList<>();
        movieIDs.stream().filter(i->i > 0 && i < movies.size()).forEach(i->resMovies.add(movies.get(i)));
        return resMovies;
    }

    public static void listAllMovies() {
        movies.stream().forEach(m -> System.out.println(m));
    }
}
