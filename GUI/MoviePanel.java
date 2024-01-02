package GUI;

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

import MovieManagement.Movie;
import MovieManagement.MovieDatabase;

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

    MoviePanel(String title, String[] columns, boolean isButtonAdd, MoviePage moviePage) {
        this.panelTitle = title;
        this.columns = columns;
        this.isButtonAdd = isButtonAdd;
        this.moviePage = moviePage;
        initializeUI();
    }

    public JPanel getPanel() {
        return this.moviesPanel;
    }

    private void initializeUI() {
        moviesPanel = new JPanel(new BorderLayout());

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
        moviesPanel.add(new JLabel(panelTitle), BorderLayout.NORTH);
        moviesPanel.add(new JScrollPane(moviesTable), BorderLayout.CENTER);
        moviesPanel.add(
                createSortPanel("Sort Watchlist By: ",
                        new JComboBox<>(
                                new String[] { "Default", "Name", "Director", "Release Year", "Running Time" })),
                BorderLayout.SOUTH);

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

        moviesPanel.add(toggleFilterButton, BorderLayout.PAGE_START);
        moviesPanel.add(filterPanel, BorderLayout.LINE_START);

        refreshPanel();
    }

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

    private Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

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

    private static void setColumnWidths(JTable table, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    public Predicate<Movie> getFilterLogic(String title, String director, Integer minReleaseYear,
            Integer maxReleaseYear, Integer minRunningTime, Integer maxRunningTime) {
        return movie -> {

            boolean matchesTitle = (title == null || title.isEmpty()) || movie.getTitle().contains(title);
            boolean matchesDirector = (director == null || director.isEmpty())
                    || movie.getDirector().contains(director);
            boolean matchesReleaseYear = (minReleaseYear == null || minReleaseYear == 0 || movie.getReleaseYear() >= minReleaseYear) &&
                    (maxReleaseYear == null || maxReleaseYear == 0 || movie.getReleaseYear() <= maxReleaseYear);
            boolean matchesRunningTime = (minRunningTime == null || minRunningTime == 0 || movie.getRunningTime() >= minRunningTime) &&
                    (maxRunningTime == null || maxRunningTime == 0 || movie.getRunningTime() <= maxRunningTime);

            return matchesTitle && matchesDirector && matchesReleaseYear && matchesRunningTime;
        };
    }

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
