package UserManagement;

import java.util.ArrayList;
import java.util.List;

import MovieManagement.Movie;

public class Watchlist {
    private ArrayList<Movie> watchList;

    Watchlist() {
        this.watchList = new ArrayList<>();
    }

    void addToWatchList(Movie m) {
        watchList.add(m);
    }

    void addToWatchList(List<Movie> movies) {
        movies.stream().forEach(m -> watchList.add(m));
    }

    void removeByIndex(int index) {
        watchList.remove(index);
    }
}
