package Source.MovieManagement;
import Source.Exceptions.*;

/**
 * Represents a movie with associated properties.
 * This class provides methods for creating and managing movie objects.
 * 
 * @author Emil Hajiyev
 * @version 1.0
 * @since 02/01/2024
 */
public class Movie {
    private int id;
    private String title;
    private String director;
    private int releaseYear;
    private int runningTime;

    /**
     * Constructor for creating a movie with an ID, title, director, release year, and running time.
     * 
     * @param id           The unique identifier of the movie.
     * @param title        The title of the movie.
     * @param director     The director of the movie.
     * @param releaseYear  The release year of the movie.
     * @param runningTime  The running time of the movie in minutes.
     * @throws InvalidRunningTimeException If the running time is invalid (less than or equal to 0).
     * @throws InvalidReleaseYearException If the release year is invalid (not within the range of 1885-2023).
     */
    public Movie(int id, String title, String director, int releaseYear, int runningTime)
            throws InvalidRunningTimeException, InvalidReleaseYearException {
        this.id = id;
        this.title = title;
        this.director = director;
        if (releaseYear < 1885 || releaseYear > 2023) {
            throw new InvalidReleaseYearException("Invalid release year: " + releaseYear);
        } else {
            this.releaseYear = releaseYear;
        }

        if (runningTime <= 0) {
            throw new InvalidRunningTimeException("Invalid running time: " + runningTime);
        } else {
            this.runningTime = runningTime;
        }
    }

    /**
     * Constructor for creating a movie with a title, director, release year, and running time.
     * 
     * @param title        The title of the movie.
     * @param director     The director of the movie.
     * @param releaseYear  The release year of the movie.
     * @param runningTime  The running time of the movie in minutes.
     * @throws InvalidRunningTimeException If the running time is invalid (less than or equal to 0).
     * @throws InvalidReleaseYearException If the release year is invalid (not within the range of 1885-2023).
     */
    public Movie(String title, String director, int releaseYear, int runningTime)
            throws InvalidRunningTimeException, InvalidReleaseYearException {
        this.title = title;
        this.director = director;
        if (releaseYear < 1885 || releaseYear > 2023) {
            throw new InvalidReleaseYearException("Invalid release year: " + releaseYear);
        } else {
            this.releaseYear = releaseYear;
        }

        if (runningTime <= 0) {
            throw new InvalidRunningTimeException("Invalid running time: " + runningTime);
        } else {
            this.runningTime = runningTime;
        }
    }

    /**
     * Constructor for creating a movie with a title and release year.
     * 
     * @param title        The title of the movie.
     * @param releaseYear  The release year of the movie.
     * @throws InvalidReleaseYearException If the release year is invalid (not within the range of 1885-2024).
     */
    public Movie(String title, int releaseYear) throws InvalidReleaseYearException {
        this.title = title;
        if (releaseYear < 1885 || releaseYear > 2024) {
            throw new InvalidReleaseYearException("Invalid release year: " + releaseYear);
        } else {
            this.releaseYear = releaseYear;
        }
    }

    /**
     * Constructor for creating a movie with a title and director.
     * 
     * @param title    The title of the movie.
     * @param director The director of the movie.
     */
    public Movie(String title, String director) {
        this.title = title;
        this.director = director;
    }

    /**
     * Gets the unique identifier of the movie.
     * 
     * @return The movie's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the title of the movie.
     * 
     * @return The movie's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the director of the movie.
     * 
     * @return The movie's director.
     */
    public String getDirector() {
        return director;
    }

    /**
     * Gets the release year of the movie.
     * 
     * @return The movie's release year.
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Gets the running time of the movie in minutes.
     * 
     * @return The movie's running time.
     */
    public int getRunningTime() {
        return runningTime;
    }

    /**
     * Sets the unique identifier of the movie.
     * 
     * @param id The new ID to set for the movie.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the title of the movie.
     * 
     * @param title The new title to set for the movie.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the director of the movie.
     * 
     * @param director The new director to set for the movie.
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Sets the release year of the movie.
     * 
     * @param releaseYear The new release year to set for the movie.
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Sets the running time of the movie in minutes.
     * 
     * @param runningTime The new running time to set for the movie.
     */
    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    /**
     * Converts the movie object to a string representation.
     * 
     * @return A string representation of the movie object.
     */
    @Override
    public String toString() {
        return id + "," + title + "," + director + "," + releaseYear + "," + runningTime;
    }
}
