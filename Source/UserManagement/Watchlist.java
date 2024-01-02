package Source.UserManagement;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a watchlist of movies associated with a user.
 * This class provides methods for managing the user's watchlist.
 *
 * @author Shamkhal Huseynzade
 * @version 1.0
 * @since 02/01/2024
 */
public class Watchlist {
    private Set<Integer> watchList;

    /**
     * Default constructor for creating an empty watchlist.
     */
    Watchlist() {
        this.watchList = new HashSet<>();
    }

    /**
     * Constructor for creating a watchlist from a set of movie IDs.
     *
     * @param watchList The set of movie IDs to initialize the watchlist.
     */
    Watchlist(Set<Integer> watchList) {
        this.watchList = watchList;
    }

    /**
     * Gets the set of movie IDs in the watchlist.
     *
     * @return The set of movie IDs in the watchlist.
     */
    public Set<Integer> getSet(){
        return this.watchList;
    }

    /**
     * Sets the watchlist to a new set of movie IDs.
     *
     * @param idSet The new set of movie IDs to set as the watchlist.
     */
    public void setWatchList(Set<Integer> idSet) {
        this.watchList = idSet;
    }

    /**
     * Adds a movie ID to the watchlist.
     *
     * @param i The movie ID to add to the watchlist.
     */
    public void addToWatchList(int i) {
        watchList.add(i);
    }

    /**
     * Adds a set of movie IDs to the watchlist.
     *
     * @param idSet The set of movie IDs to add to the watchlist.
     */
    public void addToWatchList(Set<Integer> idSet) {
        watchList.addAll(idSet);
    }

    /**
     * Adds movie IDs from a string array to the watchlist.
     *
     * @param idList The string array of movie IDs to add to the watchlist.
     */
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

    /**
     * Removes a movie ID from the watchlist.
     *
     * @param id The movie ID to remove from the watchlist.
     */
    public void removeFromList(Integer id) {
        watchList.remove(id);
    }

    /**
     * Converts the watchlist to a string representation.
     *
     * @return A string containing the movie IDs in the watchlist, separated by semicolons.
     */
    @Override
    public String toString() {
        return watchList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
    }
}
