package GUI;

import java.awt.BorderLayout;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import MovieManagement.Movie;
import MovieManagement.MovieDatabase;

public class MoviePanel {
    private JPanel moviesPanel;
    private MoviePage moviePage;
    private String[] columns;
    private DefaultTableModel moviesModel;
    private JTable moviesTable;
    private boolean isButtonAdd;
    private String panelTitle;
    private String currentSortModifier = "";
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
                        new JComboBox<>(new String[] { "Name", "Director", "Release Year", "Running Time" })),
                BorderLayout.SOUTH);
        refreshPanel();
    }

    private JPanel createSortPanel(String labelText, JComboBox<String> comboBox) {
        JPanel sortPanel = new JPanel();
        sortPanel.add(new JLabel(labelText));
        sortPanel.add(comboBox);
        comboBox.addActionListener(e -> {
            JComboBox<String> cb = (JComboBox<String>) e.getSource();
            String selectedSortBy = (String) cb.getSelectedItem();
            this.currentSortModifier = selectedSortBy;
            refreshPanel();
        });
        return sortPanel;
    }

    public void refreshPanel() {

        moviesModel.setRowCount(0);

        Comparator<Movie> sortLogic = getSortLogic(currentSortModifier);

        if (isButtonAdd)
            renderedMovies = MovieDatabase.getMovies();
        else
            renderedMovies = MovieDatabase.getMoviesByIndex(moviePage.getUser().getWatchList().getSet());

        renderedMovies.stream().sorted(sortLogic)
                .forEach(movie -> moviesModel.addRow(new Object[] {
                        movie.getId(), movie.getTitle(), movie.getDirector(), movie.getReleaseYear(),
                        movie.getRunningTime() }));
    }

    private static void setColumnWidths(JTable table, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    public Predicate<Movie> getFilterLogic(String director) {
        return movie -> true;
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
