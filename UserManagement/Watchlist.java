package UserManagement;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Watchlist {
    private Set<Integer> watchList;

    Watchlist() {
        this.watchList = new HashSet<>();
    }

    Watchlist(Set<Integer> watchList) {
        this.watchList = watchList;
    }

    // Getters
    public Set<Integer> getSet(){
        return this.watchList;
    }

    // Setters
    public void setWatchList(Set<Integer> idSet) {
        this.watchList = idSet;
    }

    public void addToWatchList(int i) {
        watchList.add(i);
    }

    public void addToWatchList(Set<Integer> idSet) {
        watchList.addAll(idSet);
    }

    public void addToWatchList(String[] idList) {
        for (String movieString : idList) {
            try {
                int movieId = Integer.parseInt(movieString.trim());
                watchList.add(movieId);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing '" + movieString + "' to an integer");
            }
        }
    }

    public void removeFromList(Integer id) {
        watchList.remove(id);
    }

    @Override
    public String toString() {
        return watchList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
    }
}
