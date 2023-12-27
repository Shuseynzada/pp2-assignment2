import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import MovieManagement.Movie;
import MovieManagement.MovieDatabase;
import UserManagement.User;
import UserManagement.UsersDatabase;

public class Pages {
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

         /* login.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword()); 
                User us1 = User.login(usernameField.getText(), password);
                UsersDatabase.updateFile(); 
            }
        }); */

        login.addActionListener((ActionListener) new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) { 
                String password  = new String(passwordField.getPassword());
                if (User.login(usernameField.getText(), password) == null){ 
                    l.setText("No such User found"); 
                } 
                else {
                    frame.dispose();
                    moviePage();
                }
            }
        });


        signup.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                if(User.register(usernameField.getText(), password).getName() == "false"){ 
                    l.setText("Username already exist");
                } 
                if(User.register(usernameField.getText(), password).getName() == "true"){
                    l.setText("Password is not valid");
                }
                else UsersDatabase.updateFile(); 
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

    static void moviePage(){ 
        JFrame frame = new JFrame(); 
        JLabel l1, l2, l3, l4,l5; 
        JButton AddtoFavorite; 
        JTextField title, director,year, time; 
        
        l1 = new JLabel("Welcome! Movie Page"); 
        l1.setBounds(50,0, 300, 30); 

        l2 = new JLabel("Title"); 
        l2.setBounds(50,30, 100, 30); 
        title = new JTextField(); 
        title.setBounds(50,50, 200, 30); 

        l3 = new JLabel("Director"); 
        l3.setBounds(50,80,100,30); 
        director = new JTextField(); 
        director.setBounds(50,100, 200, 30); 

        l4 = new JLabel("Release Year"); 
        l4.setBounds(50,130,100,30); 
        year = new JTextField(); 
        year.setBounds(50,150, 200, 30); 

        l5 = new JLabel("Running Time"); 
        l5.setBounds(50,180,100,30); 
        time = new JTextField(); 
        time.setBounds(50,200, 200, 30); 

        AddtoFavorite = new JButton("Add to Favorite"); 
        AddtoFavorite.setBounds(50,240, 200, 30); 

        AddtoFavorite.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) { 
                Movie m = new Movie(title.getText(), director.getText(),Integer.parseInt(year.getText()), Integer.parseInt(time.getText())); 
                MovieDatabase.addToFile(m);
            }
        }); 
  
        frame.add(AddtoFavorite);
        frame.add(l1);
        frame.add(l2);
        frame.add(l3);
        frame.add(l4); 
        frame.add(l5);
        frame.add(title);
        frame.add(year);
        frame.add(director); 
        frame.add(time);
        frame.setSize(400,400); 
        frame.setLayout(null);
        frame.setVisible(true);  
    }
}
