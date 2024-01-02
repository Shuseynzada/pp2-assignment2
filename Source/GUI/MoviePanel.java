package Source.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Source.MovieManagement.Movie;
import Source.MovieManagement.MovieDatabase;

/**
 * The {@code MoviePanel} class represents a graphical user interface panel for displaying and interacting with a list of movies.
 * It provides functionality for sorting and filtering movies, as well as adding or removing movies from a list.
 * 
 * @author Farhad Aliyev
 * @author Shamkhal Huseynzade
 * @date 02/01/2024
 */
public class MoviePanel {
    private JPanel moviesPanel;
    private JPanel filterPanel;
    private MoviePage moviePage;
    private String[] columns;
    private DefaultTableModel moviesModel;
    private JTable moviesTable;
    private boolean isButtonAdd;
    private String panelTitle;
    private String currentSortModifier = "";
    private String filterTitle, filterDirector;
    private int filterMaxYear, filterMinYear, filterMaxRunningTime, filterMinRunningTime;
    private List<Movie> renderedMovies;

    /**
     * Constructs a new MoviePanel with the specified parameters.
     *
     * @param title     The title of the panel.
     * @param columns   The column names for the movie table.
     * @param isButtonAdd   Indicates whether the panel should have an "Add" button.
     * @param moviePage The parent MoviePage associated with this panel.
     */
    public MoviePanel(String title, String[] columns, boolean isButtonAdd, MoviePage moviePage) {
        this.panelTitle = title;
        this.columns = columns;
        this.isButtonAdd = isButtonAdd;
        this.moviePage = moviePage;
        initializeUI();
    }

    /**
     * Gets the JPanel representing the MoviePanel.
     *
     * @return The JPanel representing the MoviePanel.
     */
    public JPanel getPanel() {
        return this.moviesPanel;
    }

    /**
     * Initializes the user interface components and layout of the MoviePanel.
     */
    private void initializeUI() {
        moviesPanel = new JPanel(new BorderLayout());
        moviesPanel.add(new JLabel(this.panelTitle), BorderLayout.NORTH);

        moviesModel = new DefaultTableModel(new String[][] {}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        moviesTable = new JTable(moviesModel);
        moviesTable.setRowHeight(25);
        setColumnWidths(moviesTable, new int[] { 50, 150, 150, 100, 100, 80 });
        moviesTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonCellRenderer((isButtonAdd) ? "+" : "-"));
        moviesTable.getColumnModel().getColumn(5)
                .setCellEditor(new ButtonCellEditor(moviePage, (isButtonAdd) ? '+' : '-'));
        moviesPanel.add(new JScrollPane(moviesTable), BorderLayout.CENTER);
        filterPanel = createFilterPanel();
        filterPanel.setVisible(false);

        JButton toggleFilterButton = new JButton("Show Filters");
        toggleFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isVisible = filterPanel.isVisible();
                filterPanel.setVisible(!isVisible);
                toggleFilterButton.setText(isVisible ? "Show Filters" : "Hide Filters");
                moviesPanel.revalidate(); // Update the panel layout
            }
        });
        JPanel southPanel = new JPanel();
        southPanel.add(toggleFilterButton);
        southPanel.add(createSortPanel("Sort By: ",
                new JComboBox<>(
                        new String[] { "Default", "Name", "Director", "Release Year", "Running Time" })),
                BorderLayout.SOUTH);

        moviesPanel.add(southPanel, BorderLayout.SOUTH);
        moviesPanel.add(filterPanel, BorderLayout.LINE_START);

        refreshPanel();
    }

    /**
     * Creates a JPanel containing sorting options.
     *
     * @param labelText The label text for the sorting options.
     * @param comboBox  The JComboBox for selecting sorting options.
     * @return The JPanel containing sorting options.
     */
    private JPanel createSortPanel(String labelText, JComboBox<String> comboBox) {
        JPanel sortPanel = new JPanel();
        sortPanel.add(new JLabel(labelText));
        sortPanel.add(comboBox);
        comboBox.addActionListener(e -> {
            Object source = e.getSource();
            if (source instanceof JComboBox) {
                JComboBox<?> cb = (JComboBox<?>) source;
                String selectedSortBy = Objects.requireNonNull((String) cb.getSelectedItem());
                this.currentSortModifier = selectedSortBy;
                refreshPanel();
            }
        });
        return sortPanel;
    }

    /**
     * Creates a JPanel containing filtering options.
     *
     * @return The JPanel containing filtering options.
     */
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));

        // Text fields for filter criteria
        JTextField titleField = new JTextField(10);
        JTextField directorField = new JTextField(10);
        JTextField minReleaseYearField = new JTextField(5);
        JTextField maxReleaseYearField = new JTextField(5);
        JTextField minRunningTimeField = new JTextField(5);
        JTextField maxRunningTimeField = new JTextField(5);

        // Filter button
        JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(e -> {
            filterTitle = titleField.getText();
            filterDirector = directorField.getText();
            filterMinYear = tryParseInt(minReleaseYearField.getText());
            filterMaxYear = tryParseInt(maxReleaseYearField.getText());
            filterMinRunningTime = tryParseInt(minRunningTimeField.getText());
            filterMaxRunningTime = tryParseInt(maxRunningTimeField.getText());

            refreshPanel();
        });

        // Adding components to the panel
        filterPanel.add(new JLabel("Title:"));
        filterPanel.add(titleField);
        filterPanel.add(new JLabel("Director:"));
        filterPanel.add(directorField);
        filterPanel.add(new JLabel("Min Release Year:"));
        filterPanel.add(minReleaseYearField);
        filterPanel.add(new JLabel("Max Release Year:"));
        filterPanel.add(maxReleaseYearField);
        filterPanel.add(new JLabel("Min Running Time:"));
        filterPanel.add(minRunningTimeField);
        filterPanel.add(new JLabel("Max Running Time:"));
        filterPanel.add(maxRunningTimeField);
        filterPanel.add(filterButton);

        JTextField[] filterFields = { titleField, directorField, minReleaseYearField, maxReleaseYearField,
                minRunningTimeField, maxRunningTimeField };
        for (JTextField field : filterFields) {
            field.addActionListener(e -> {
                filterTitle = titleField.getText();
                filterDirector = directorField.getText();
                filterMinYear = tryParseInt(minReleaseYearField.getText());
                filterMaxYear = tryParseInt(maxReleaseYearField.getText());
                filterMinRunningTime = tryParseInt(minRunningTimeField.getText());
                filterMaxRunningTime = tryParseInt(maxRunningTimeField.getText());
                System.out.println(filterTitle);

                refreshPanel();
            });
        }

        return filterPanel;
    }

    /**
     * Attempts to parse an integer from a string value.
     *
     * @param value The string value to parse.
     * @return The parsed integer value or 0 if parsing fails.
     */
    private Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Refreshes the panel by updating the displayed movies based on sorting and filtering criteria.
     */
    public void refreshPanel() {

        moviesModel.setRowCount(0);

        Comparator<Movie> sortLogic = getSortLogic(currentSortModifier);
        Predicate<Movie> filterLogic = getFilterLogic(filterTitle, filterDirector, filterMinYear, filterMaxYear,
                filterMinRunningTime, filterMaxRunningTime);

        if (isButtonAdd)
            renderedMovies = MovieDatabase.getMovies();
        else
            renderedMovies = MovieDatabase.getMoviesByIndex(moviePage.getUser().getWatchList().getSet());

        renderedMovies.stream()
                .filter(filterLogic)
                .sorted(sortLogic)
                .forEach(movie -> moviesModel.addRow(new Object[] {
                        movie.getId(), movie.getTitle(), movie.getDirector(), movie.getReleaseYear(),
                        movie.getRunningTime() }));
    }

    /**
     * Sets the preferred column widths for the movie table.
     *
     * @param table   The JTable for which to set column widths.
     * @param widths  An array of integers representing the preferred widths for each column.
     */
    private static void setColumnWidths(JTable table, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    /**
     * Constructs a Predicate for filtering movies based on specified criteria.
     *
     * @param title           The movie title criteria for filtering.
     * @param director        The movie director criteria for filtering.
     * @param minReleaseYear  The minimum release year criteria for filtering.
     * @param maxReleaseYear  The maximum release year criteria for filtering.
     * @param minRunningTime  The minimum running time criteria for filtering.
     * @param maxRunningTime  The maximum running time criteria for filtering.
     * @return A Predicate for filtering movies based on the specified criteria.
     */
    public Predicate<Movie> getFilterLogic(String title, String director, Integer minReleaseYear,
            Integer maxReleaseYear, Integer minRunningTime, Integer maxRunningTime) {
        return movie -> {

            boolean matchesTitle = (title == null || title.isEmpty()) || movie.getTitle().contains(title);
            boolean matchesDirector = (director == null || director.isEmpty())
                    || movie.getDirector().contains(director);
            boolean matchesReleaseYear = (minReleaseYear == null || minReleaseYear == 0
                    || movie.getReleaseYear() >= minReleaseYear) &&
                    (maxReleaseYear == null || maxReleaseYear == 0 || movie.getReleaseYear() <= maxReleaseYear);
            boolean matchesRunningTime = (minRunningTime == null || minRunningTime == 0
                    || movie.getRunningTime() >= minRunningTime) &&
                    (maxRunningTime == null || maxRunningTime == 0 || movie.getRunningTime() <= maxRunningTime);

            return matchesTitle && matchesDirector && matchesReleaseYear && matchesRunningTime;
        };
    }

    /**
     * Constructs a Comparator for sorting movies based on the specified criteria.
     *
     * @param sortBy The sorting criteria for movies.
     * @return A Comparator for sorting movies based on the specified criteria.
     */
    public Comparator<Movie> getSortLogic(String sortBy) {
        switch (sortBy) {
            case "Name":
                return Comparator.comparing(Movie::getTitle);
            case "Director":
                return Comparator.comparing(Movie::getDirector);
            case "Release Year":
                return Comparator.comparing(Movie::getReleaseYear);
            case "Running Time":
                return Comparator.comparing(Movie::getRunningTime);
            default:
                return (movie1, movie2) -> 0;
        }
    }
}
