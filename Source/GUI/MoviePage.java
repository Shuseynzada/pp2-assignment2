package Source.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import Source.Exceptions.InvalidReleaseYearException;
import Source.Exceptions.InvalidRunningTimeException;
import Source.MovieManagement.Movie;
import Source.MovieManagement.MovieDatabase;
import Source.UserManagement.User;

/**
 * Represents the main movie management page in the movie program GUI.
 * This class provides a graphical user interface for users to view, add, and manage movies.
 * 
 * @author Farhad Aliyev
 * @version 1.0
 * @since 02/01/2024
 */
public class MoviePage {
    private JFrame frame;
    private User user;
    public String userSelectedSortBy = "", userSelectedFilterBy = "";
    private MoviePanel generalMoviesPanel, watchlistMoviesPanel;
    private JTextField titleField, directorField, yearField, runningTimeField;

    /**
     * Initializes the MoviePage with the user's information.
     * 
     * @param user The user for whom the MoviePage is displayed.
     */
    public MoviePage(User user) {
        this.user = user;
        initializeUI();
    }

    /**
     * Retrieves the user associated with this MoviePage.
     * 
     * @return The user associated with this MoviePage.
     */
    public User getUser() {
        return this.user;
    }

    private void initializeUI() {
        frame = new JFrame("Movie Program");
        frame.setSize(1500, 1200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2));

        generalMoviesPanel = new MoviePanel("Movie Database",
                new String[] { "ID", "Movie Name", "Director", "Release Year", "Running Time", "Add Watchlist" }, true,
                this);
        watchlistMoviesPanel = new MoviePanel("Your movies",
                new String[] { "ID", "Movie Name", "Director", "Release Year", "Running Time", "Remove" },
                false, this);

        tablesPanel.add(generalMoviesPanel.getPanel());
        tablesPanel.add(watchlistMoviesPanel.getPanel());

        mainPanel.add(tablesPanel, BorderLayout.CENTER);
        mainPanel.add(createAddMoviePanel(), BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createAddMoviePanel() {
        JPanel addMoviePanel = new JPanel();
        addMoviePanel.setLayout(new BoxLayout(addMoviePanel, BoxLayout.Y_AXIS));
        addMoviePanel.setBorder(BorderFactory.createTitledBorder("Add New Movie"));

        titleField = new JTextField(20);
        directorField = new JTextField(20);
        yearField = new JTextField(20);
        runningTimeField = new JTextField(20);

        JButton addMovieButton = new JButton("Add Movie");
        addMovieButton.addActionListener(this::addMovie);

        addMoviePanel.add(new JLabel("Title: "));
        addMoviePanel.add(titleField);
        addMoviePanel.add(new JLabel("Director: "));
        addMoviePanel.add(directorField);
        addMoviePanel.add(new JLabel("Release Year: "));
        addMoviePanel.add(yearField);
        addMoviePanel.add(new JLabel("Running Time: "));
        addMoviePanel.add(runningTimeField);
        addMoviePanel.add(addMovieButton);

        return addMoviePanel;
    }

    private void addMovie(ActionEvent e) {
        try {
            Movie m = new Movie(titleField.getText(), directorField.getText(), Integer.parseInt(yearField.getText()),
                    Integer.parseInt(runningTimeField.getText()));
            MovieDatabase.addToFile(m);
            refreshTables();
        } catch (InvalidRunningTimeException | InvalidReleaseYearException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Error adding movie: " + ex.getMessage(), "Add Movie Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refreshes the movie panels to reflect the updated movie data.
     */
    public void refreshTables() {
        generalMoviesPanel.refreshPanel();
        watchlistMoviesPanel.refreshPanel();
    }
}
