import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class WelcomePage {
    public static void main(String[] args){
        JFrame frame = new JFrame(); 
        JLabel l1, l2, l3; 
        JButton login, signup; 
        JTextField usernameField; 
        JPasswordField passwordField; 
        
        l1 = new JLabel("Welcome! Login"); 
        l1.setBounds(50,0, 100, 30); 

        l2 = new JLabel("Username"); 
        l2.setBounds(50,30, 100, 30); 
        usernameField = new JTextField(); 
        usernameField.setBounds(50,50, 100, 30); 

        l3 = new JLabel("Password"); 
        l3.setBounds(50,80,100,30); 
        passwordField = new JPasswordField(); 
        passwordField.setBounds(50,100, 100, 30); 

        login = new JButton("Login"); 
        login.setBounds(50,130, 100, 30); 
        signup = new JButton("Sign Up"); 
        signup.setBounds(170,130, 100, 30); 

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
}
