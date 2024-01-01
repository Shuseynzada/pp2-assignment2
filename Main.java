import MovieManagement.MovieDatabase;
import UserManagement.User;
import UserManagement.UserNotFoundException;
import UserManagement.UsersDatabase;

public class Main {
    public static void main(String[] args) {
        PagesGUI.moviePage();
        User samxal;
        try {
            samxal = User.login("samxal", "Samxal123");
            samxal.getWatchList().addToWatchList("1;2;3;4".split(";"));
            samxal.getWatchList().addToWatchList("1;2;3;4".split(";"));
            samxal.getWatchList().addToWatchList("1;2;3;4".split(";"));
            System.out.println(samxal);
            UsersDatabase.updateFile();
            MovieDatabase.getMoviesByIndex(samxal.getWatchList().getSet()).stream().forEach(m->System.out.println(m));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        } 

    }
}