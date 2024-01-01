import Exceptions.IncorrectPasswordException;
import Exceptions.UserNotFoundException;
import GUI.MoviePage;
import UserManagement.User;

public class Main {
    public static void main(String[] args) {
        try {
            User samxal = User.login("samxal", "Samxal123");
            new MoviePage(samxal);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        } catch (IncorrectPasswordException e) {
            e.printStackTrace();
        }
    }
}