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
        tablesPanel.add(createGeneralMoviesPanel());
        tablesPanel.add(createWatchlistPanel());
    
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
    
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(filterByComboBox);
        searchPanel.add(startField);
        searchPanel.add(new JLabel(" to "));
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
    
    

    private JPanel createGeneralMoviesPanel() {

        JPanel generalMoviesPanel = new JPanel(new BorderLayout());

        String[] columns = { "ID", "Movie Name", "Director", "Release Year", "Running Time", "Add Watchlist" };

        generalMoviesModel = new DefaultTableModel(new String[][] {}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        MovieDatabase.getMovies().forEach(movie -> generalMoviesModel.addRow(new Object[] {
                movie.getId(), movie.getTitle(), movie.getDirector(), movie.getReleaseYear(),
                movie.getRunningTime() }));

        generalMoviesTable = new JTable(generalMoviesModel);
        generalMoviesTable.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                // Check if the current column index is not the button column
                if (generalMoviesTable.getSelectedColumn() != 5) {
                    super.setSelectionInterval(index0, index1);
                }
            }
        });
        generalMoviesTable.setRowHeight(25);
        setColumnWidths(generalMoviesTable, new int[] { 50, 150, 150, 100, 100, 80 });
        generalMoviesTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonCellRenderer("+"));
        generalMoviesTable.getColumnModel().getColumn(5).setCellEditor(new ButtonCellEditor(this, 0));
        generalMoviesPanel.add(new JScrollPane(generalMoviesTable), BorderLayout.CENTER);
        generalMoviesPanel.add(
                createSortPanel("Sort General Movies By: ",
                        generalMoviesSortBy = new JComboBox<>(
                                new String[] { "Name", "Director", "Release Year", "Running Time" })),
                BorderLayout.SOUTH);

        return generalMoviesPanel;
    }

    private JPanel createWatchlistPanel() {
        JPanel watchlistPanel = new JPanel(new BorderLayout());
        String[] columns = { "ID", "Movie Name", "Director", "Release Year", "Running Time", "Remove" };

        watchlistModel = new DefaultTableModel(new String[][] {}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        MovieDatabase.getMoviesByIndex(user.getWatchList().getSet())
                .forEach(movie -> watchlistModel.addRow(new Object[] {
                        movie.getId(), movie.getTitle(), movie.getDirector(), movie.getReleaseYear(),
                        movie.getRunningTime() }));

        watchlistTable = new JTable(watchlistModel);
        watchlistTable.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                // Check if the current column index is not the button column
                if (watchlistTable.getSelectedColumn() != 5) {
                    super.setSelectionInterval(index0, index1);
                }
            }
        });
        watchlistTable.setRowHeight(25);
        setColumnWidths(watchlistTable, new int[] { 50, 150, 150, 100, 100, 80 });
        watchlistTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonCellRenderer("-"));
        watchlistTable.getColumnModel().getColumn(5).setCellEditor(new ButtonCellEditor(this, 1));
        watchlistPanel.add(new JScrollPane(watchlistTable), BorderLayout.CENTER);
        watchlistPanel.add(
                createSortPanel("Sort Watchlist By: ",
                        watchlistSortBy = new JComboBox<>(
                                new String[] { "Name", "Director", "Release Year", "Running Time" })),
                BorderLayout.SOUTH);

        return watchlistPanel;
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
        generalMoviesModel.setRowCount(0);
        watchlistModel.setRowCount(0);

        MovieDatabase.getMovies().forEach(movie -> generalMoviesModel.addRow(new Object[] {
                movie.getId(), movie.getTitle(), movie.getDirector(), movie.getReleaseYear(), movie.getRunningTime()
        }));

        MovieDatabase.getMoviesByIndex(user.getWatchList().getSet())
                .forEach(movie -> watchlistModel.addRow(new Object[] {
                        movie.getId(), movie.getTitle(), movie.getDirector(), movie.getReleaseYear(),
                        movie.getRunningTime()
                }));

    }

    private JPanel createSortPanel(String labelText, JComboBox<String> comboBox) {
        JPanel sortPanel = new JPanel();
        sortPanel.add(new JLabel(labelText));
        sortPanel.add(comboBox);
        comboBox.addActionListener(this::sortMovies);
        return sortPanel;
    }

private void sortMovies(ActionEvent e) {
    JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
    String selectedItem = (String) comboBox.getSelectedItem();

    // Sort General Movies table
    if (comboBox == generalMoviesSortBy) {
        int columnToSortBy = -1; // Initialize with an invalid value
        switch (selectedItem) {
            case "Name":
                columnToSortBy = 1; // Movie Name column
                break;
            case "Director":
                columnToSortBy = 2; // Director column
                break;
            case "Release Year":
                columnToSortBy = 3; // Release Year column
                break;
            case "Running Time":
                columnToSortBy = 4; // Running Time column
                break;
            default:
                // Handle default case or no selection
                break;
        }
        if (columnToSortBy != -1) {
            sortTable(generalMoviesTable, columnToSortBy);
        }
    }

    // Sort Watchlist table
    if (comboBox == watchlistSortBy) {
        int columnToSortBy = -1; // Initialize with an invalid value
        switch (selectedItem) {
            case "Name":
                columnToSortBy = 1; // Movie Name column
                break;
            case "Director":
                columnToSortBy = 2; // Director column
                break;
            case "Release Year":
                columnToSortBy = 3; // Release Year column
                break;
            case "Running Time":
                columnToSortBy = 4; // Running Time column
                break;
            default:
                // Handle default case or no selection
                break;
        }
        if (columnToSortBy != -1) {
            sortTable(watchlistTable, columnToSortBy);
        }
    }
}

private void sortTable(JTable table, int columnToSortBy) {
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
    table.setRowSorter(sorter);
    sorter.setSortsOnUpdates(true);
    List<RowSorter.SortKey> sortKeys = new ArrayList<>();

    switch (columnToSortBy) {
        case 1: // Sort by Movie Name
            sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
            break;
        case 2: // Sort by Director
            sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
            break;
        case 3: // Sort by Release Year
            sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
            break;
        case 4: // Sort by Running Time
            sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));
            break;
        default:
            break;
    }

    sorter.setSortKeys(sortKeys);
    sorter.sort();
}


    private void setColumnWidths(JTable table, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    class ButtonCellRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        private String text;

        public ButtonCellRenderer(String s) {
            this.text = s;
            setOpaque(true);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setText(this.text);
            return this;
        }
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
