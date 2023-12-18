import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import UserManagement.User;
import UserManagement.UsersDatabase;

public class Movie {
    private String title;
    private String director; 
    private int releaseYear;
    private int runningTime; 

    public Movie (String title, String director, int releaseYear, int runningTime){
        this.title = title; 
        this.director = director; 
        if(releaseYear >= 1885){
            this.releaseYear = releaseYear; 
        } 
        else {
            System.out.println("Please Enter Appropiate Release Year (between 1885 and 2023)!"); 
        } 
        if(runningTime != 0){
            this.runningTime = runningTime; 
        } 
        else {
            System.out.println("Please Enter Appropiate Running Time!");
        }
    }  

    public Movie (String title, int releaseYear){
        this.title = title; 
        if(releaseYear >= 1885){
            this.releaseYear = releaseYear; 
        } 
        else {
            System.out.println("Please Enter Appropiate Release Year (between 1885 and 2023)!"); 
        } 
    } 

    public Movie (String title, String director){
        this.title = title; 
        this.director = director; 
    } 

    public String getTitle() {
        return title;
    } 
    public String getDirector() {
        return director;
    } 
    public int getReleaseYear() {
        return releaseYear;
    }
    public int getRunningTime() {
        return runningTime;
    } 

    public void setTitle(String title) {
        this.title = title;
    }
    public void setDirector(String director) {
        this.director = director;
    } 

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    } 

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    } 

    public String toString() {
        return title + "," + releaseYear + "," + director + "," + runningTime; 
    } 

    public static void main(String[] args) {
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
