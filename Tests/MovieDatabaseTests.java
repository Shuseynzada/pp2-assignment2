package Tests;

import Resources.*;
import Source.Exceptions.*;
import Source.MovieManagement.*;
import Source.UserManagement.*;

import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;

public class MovieDatabaseTests {

    private static final String TEST_DATABASE_PATH = "Resources/test_movies.csv";
    private static final String ORIGINAL_DATABASE_PATH = MovieDatabase.getFilepath();

    @BeforeClass
    public static void setupClass() {
       // Redirect the MovieDatabase to use the test file
       MovieDatabase.setFilepath(TEST_DATABASE_PATH);
    }

    @Before
    public void setUp() {
        clearFile(TEST_DATABASE_PATH);
    }

    @Test
    public void testAddMovieToDatabase() throws InvalidRunningTimeException, InvalidReleaseYearException {
        Movie movie = new Movie("Inception", "Christopher Nolan", 2010, 148);
        MovieDatabase.addToFile(movie);

        List<Movie> movies = MovieDatabase.getMovies();
        assertTrue("Movie should be added to the database", movies.contains(movie));
    }

    @Test(expected = InvalidReleaseYearException.class)
    public void testAddMovieInvalidYear() throws InvalidRunningTimeException, InvalidReleaseYearException {
        new Movie("Back to the Future", "Robert Zemeckis", 1884, 116);
    }

    @Test(expected = InvalidRunningTimeException.class)
    public void testAddMovieInvalidRunningTime() throws InvalidRunningTimeException, InvalidReleaseYearException {
        new Movie("The Godfather", "Francis Ford Coppola", 1972, -175);
    }

   
    @Test
    public void testRetrieveMovieDetails() throws InvalidRunningTimeException, InvalidReleaseYearException {
        Movie movie = new Movie("Forrest Gump", "Robert Zemeckis", 1994, 142);
        MovieDatabase.addToFile(movie);

        List<Movie> movies = MovieDatabase.getMovies();
        assertTrue("Movie details should be retrievable", movies.stream()
                .anyMatch(m -> m.getTitle().equals("Forrest Gump") && m.getDirector().equals("Robert Zemeckis")));
    }

    @After
    public static void tearDownClass() {
        MovieDatabase.setFilepath(ORIGINAL_DATABASE_PATH);
    }

    private static void clearFile(String path) {
        try {
            new File(path).delete();
            Files.createFile(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
