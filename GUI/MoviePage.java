package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Comparator;
import java.util.function.Predicate;

import Exceptions.InvalidReleaseYearException;
import Exceptions.InvalidRunningTimeException;
import GUI.MoviePage.ButtonCellRenderer;
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
    private JTextField titleField, directorField, yearField, runningTimeField;
    public String userSelectedSortBy = "", userSelectedFilterBy = "";

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
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2));
        tablesPanel.add(createGeneralMoviesPanel());
        tablesPanel.add(createWatchlistPanel());

        mainPanel.add(tablesPanel, BorderLayout.CENTER);
        mainPanel.add(createAddMoviePanel(), BorderLayout.SOUTH);

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

        MovieDatabase.getMovies().stream()
                .forEach(movie -> generalMoviesModel.addRow(new Object[] {
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
                        new JComboBox<>(new String[] { "Name", "Director", "Release Year", "Running Time" })),
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
                        new JComboBox<>(new String[] { "Name", "Director", "Release Year", "Running Time" })),
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

        System.out.println(userSelectedSortBy);
        Comparator<Movie> sortLogic = getSortLogic(userSelectedSortBy);
        Predicate<Movie> filterLogic = getFilterLogic(userSelectedFilterBy);

        MovieDatabase.getMovies().stream().filter(filterLogic).sorted(sortLogic)
                .forEach(movie -> generalMoviesModel.addRow(new Object[] {
                        movie.getId(), movie.getTitle(), movie.getDirector(), movie.getReleaseYear(),
                        movie.getRunningTime() }));

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
        comboBox.addActionListener(e -> {
            JComboBox<String> cb = (JComboBox<String>) e.getSource();
            String selectedSortBy = (String) cb.getSelectedItem();

            switch (selectedSortBy) {
                case "Name":
                    this.userSelectedSortBy = "Title";
                    break;
                case "Director":
                    this.userSelectedSortBy = "Director";
                    break;
                case "Release Year":
                    this.userSelectedSortBy = "Release Year";
                    break;
                case "Running Time":
                    this.userSelectedSortBy = "Running Time";
                    break;
                default:
                    this.userSelectedSortBy = ""; // No sorting
            }

            refreshTables();
        });
        return sortPanel;
    }

    public Predicate<Movie> getFilterLogic(String director) {
        return movie -> true;
    }

    public Comparator<Movie> getSortLogic(String sortBy) {
        switch (sortBy) {
            case "Title":
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
}
