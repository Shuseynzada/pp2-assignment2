package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import Exceptions.InvalidReleaseYearException;
import Exceptions.InvalidRunningTimeException;
import MovieManagement.Movie;
import MovieManagement.MovieDatabase;
import UserManagement.User;

public class MoviePage {
    private JFrame frame;
    private User user;
    private JTable generalMoviesTable;
    private JTable watchlistTable;
    private DefaultTableModel generalMoviesModel;
    private DefaultTableModel watchlistModel;
    public String userSelectedSortBy = "", userSelectedFilterBy = "";
    private MoviePanel generalMoviesPanel, watchlistMoviesPanel;
    private JTextField titleField, directorField, yearField, runningTimeField, searchField;
    private JComboBox<String> generalMoviesSortBy, watchlistSortBy;


    public MoviePage(User user) {
        this.user = user;
        initializeUI();
    }

    public User getUser() {
        return this.user;
    }

    private void initializeUI() {
        frame = new JFrame("Movie Page");
        frame.setSize(1500, 1200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        JPanel mainPanel = new JPanel(new BorderLayout()); 
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMovies());
        topPanel.add(searchField);
        topPanel.add(searchButton);
    
        mainPanel.add(topPanel, BorderLayout.NORTH);
    
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2));

        generalMoviesPanel = new MoviePanel("General Movies",
                new String[] { "ID", "Movie Name", "Director", "Release Year", "Running Time", "Add Watchlist" }, true,
                this);
        watchlistMoviesPanel = new MoviePanel("General Movies",
                new String[] { "ID", "Movie Name", "Director", "Release Year", "Running Time", "Remove" },
                false, this);

        tablesPanel.add(generalMoviesPanel.getPanel());
        tablesPanel.add(watchlistMoviesPanel.getPanel());

        mainPanel.add(tablesPanel, BorderLayout.CENTER);
        mainPanel.add(createAddMoviePanel(), BorderLayout.SOUTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton.addActionListener(e -> searchMovies());
    
        JComboBox<String> filterByComboBox = new JComboBox<>(new String[]{"Running Time", "Release Year"});
        JTextField startField = new JTextField(5);
        JTextField endField = new JTextField(5);
    
        JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(e -> {
            String searchText = searchField.getText().toLowerCase();
            String startText = startField.getText();
            String endText = endField.getText();
            String selectedFilter = (String) filterByComboBox.getSelectedItem();
    
            if (!startText.isEmpty() && !endText.isEmpty()) {
                try {
                    int start = Integer.parseInt(startText);
                    int end = Integer.parseInt(endText);
    
                    if (start <= 0 || (start < 1885 && end > 2024)) {
                        JOptionPane.showMessageDialog(frame, "Please give appropriate numbers.", "Filter Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
    
                    DefaultTableModel generalModel = generalMoviesModel;
    
                    filterMovies(generalModel, searchText, start, end, selectedFilter);
                    filterMovies(watchlistModel, searchText, start, end, selectedFilter);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Filter Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        filterByComboBox.setBorder(BorderFactory.createEmptyBorder(0,100, 0, 0));
    
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(filterByComboBox);
        searchPanel.add(startField);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(endField);
        searchPanel.add(filterButton);
    
        mainPanel.add(searchPanel, BorderLayout.NORTH);
    
    
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(filterByComboBox);
        searchPanel.add(startField);
        searchPanel.add(new JLabel(" to "));
        searchPanel.add(endField);
        searchPanel.add(filterButton);
    
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        
    
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

    public void refreshTables() {
        generalMoviesPanel.refreshPanel();
        watchlistMoviesPanel.refreshPanel();
    }
    private void searchMovies() {
        String searchText = searchField.getText().toLowerCase();
        DefaultTableModel generalModel = (DefaultTableModel) generalMoviesTable.getModel();
        DefaultTableModel watchlistModel = (DefaultTableModel) watchlistTable.getModel();
    
        filterMovies(generalModel, searchText);
        filterMovies(watchlistModel, searchText);
    }
    
    private void filterMovies(DefaultTableModel model, String searchText) {
        JTable table = (model == generalMoviesModel) ? generalMoviesTable : watchlistTable;
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    
        RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
            public boolean include(Entry entry) {
                for (int i = 0; i < entry.getValueCount(); i++) {
                    if (entry.getStringValue(i).toLowerCase().contains(searchText)) {
                        return true;
                    }
                }
                return false;
            }
        };
    
        sorter.setRowFilter(filter);
        table.setRowSorter(sorter);
    }    
    private void filterMovies(DefaultTableModel model, String searchText, int start, int end, String selectedFilter) {
        JTable table = (model == generalMoviesModel) ? generalMoviesTable : watchlistTable;
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    
        RowFilter<Object, Object> rowFilter = new RowFilter<Object, Object>() {
            public boolean include(Entry entry) {
                int value;
                try {
                    value = Integer.parseInt(entry.getStringValue(selectedFilter.equals("Running Time") ? 4 : 3));
                    return value >= start && value <= end && entry.getStringValue(1).toLowerCase().contains(searchText);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        };
    
        sorter.setRowFilter(rowFilter);
        table.setRowSorter(sorter);
    }
}
