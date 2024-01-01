import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import UserManagement.User;
import UserManagement.UserNotFoundException;
import UserManagement.UsernameAlreadyExistsException;
import UserManagement.UsersDatabase;
import UserManagement.Watchlist;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PagesGUI {
    static boolean isLoginSuccess = false;
    static void loginPage(){ 
        JFrame frame = new JFrame(); 
        JLabel l, l1, l2, l3; 
        JButton login, signup; 
        JTextField usernameField; 
        JPasswordField passwordField;  
        
        l1 = new JLabel("Welcome! Login"); 
        l1.setBounds(50,0, 100, 30); 

        l2 = new JLabel("Username"); 
        l2.setBounds(50,30, 100, 30); 
        usernameField = new JTextField(); 
        usernameField.setBounds(50,50, 220, 30); 

        l3 = new JLabel("Password"); 
        l3.setBounds(50,80,100,30); 
        passwordField = new JPasswordField(); 
        passwordField.setBounds(50,100, 220, 30); 

        login = new JButton("Login"); 
        login.setBounds(50,130, 100, 30); 
        signup = new JButton("Sign Up"); 
        signup.setBounds(170,130, 100, 30); 

        l = new JLabel(); 
        l.setBounds(50,160,300,30); 

        login.addActionListener((ActionListener) new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) { 
                String password  = new String(passwordField.getPassword());
                try {
                    User user = User.login(usernameField.getText(), password);
                    if (user == null) { 
                        l.setText("Login failed"); 
                    } else {
                        frame.dispose();
                        moviePage();
                    }
                } catch (UserNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame, "User not found: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        signup.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                try {
                    User newUser = User.register(usernameField.getText(), password);
                    UsersDatabase.updateFile(); 
                } catch (UsernameAlreadyExistsException ex) {
                    JOptionPane.showMessageDialog(frame, "Username already exists: " + ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(l); 
        frame.add(l1);
        frame.add(l2);
        frame.add(l3);
        frame.add(login);
        frame.add(signup);
        frame.add(usernameField);
        frame.add(passwordField);
        frame.setSize(400,400); 
        frame.setLayout(null);
        frame.setVisible(true); 
    }

    static void moviePage() {
        JFrame frame = new JFrame("Movie Page");

        String[] columns = {"Movie Name", "Director", "Release Year", "Running Time"};
        String[][] generalMoviesData = {
                {"Movie1", "Director1", "Year1", "Running Time1"},
                {"Movie2", "Director2", "Year2", "Running Time2"}
        };
        String[][] watchlistData = { 

        };

        JLabel generalMoviesLabel = new JLabel("General Movies");
        JLabel watchlistLabel = new JLabel("Watchlist");


        Font titleFont = new Font(generalMoviesLabel.getFont().getName(), Font.BOLD, 18);
        generalMoviesLabel.setFont(titleFont);
        watchlistLabel.setFont(titleFont);

        DefaultTableModel generalMoviesModel = new DefaultTableModel(generalMoviesData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        JTable generalMoviesTable = new JTable(generalMoviesModel);
        JScrollPane generalMoviesScrollPane = new JScrollPane(generalMoviesTable);

        DefaultTableModel watchlistModel = new DefaultTableModel(watchlistData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        JTable watchlistTable = new JTable(watchlistModel);
        JScrollPane watchlistScrollPane = new JScrollPane(watchlistTable);

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2));
        JPanel generalMoviesPanel = new JPanel(new BorderLayout());
        JPanel watchlistPanel = new JPanel(new BorderLayout());

        generalMoviesPanel.add(generalMoviesLabel, BorderLayout.NORTH);
        generalMoviesPanel.add(generalMoviesScrollPane, BorderLayout.CENTER);

        watchlistPanel.add(watchlistLabel, BorderLayout.NORTH);
        watchlistPanel.add(watchlistScrollPane, BorderLayout.CENTER);

        tablesPanel.add(generalMoviesPanel);
        tablesPanel.add(watchlistPanel);

        JPanel addMoviePanel = new JPanel();
        addMoviePanel.setLayout((LayoutManager) new BoxLayout(addMoviePanel, BoxLayout.Y_AXIS));
        addMoviePanel.setBorder(BorderFactory.createTitledBorder("Add New Movie"));

        JTextField title = new JTextField(20);
        JTextField director = new JTextField(20);
        JTextField year = new JTextField(20);
        JTextField runningTime = new JTextField(20);

        JButton addToFavorite = new JButton("Add to Favorite");
        addToFavorite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to add new movie to the database
                // Retrieve data from text fields (title, director, year, runningTime)
                // Create Movie object and add it to the database
            }
        });

        addMoviePanel.add(new JLabel("Title: "));
        addMoviePanel.add(title);
        addMoviePanel.add(new JLabel("Director: "));
        addMoviePanel.add(director);
        addMoviePanel.add(new JLabel("Release Year: "));
        addMoviePanel.add(year);
        addMoviePanel.add(new JLabel("Running Time: "));
        addMoviePanel.add(runningTime);
        addMoviePanel.add(addToFavorite);

        // Create main panel and add components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tablesPanel, BorderLayout.CENTER);
        mainPanel.add(addMoviePanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); 
    }
}
