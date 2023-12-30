package MovieManagement;

public class InvalidReleaseYearException extends Exception {
    public InvalidReleaseYearException(String message) {
        super(message);
    }
}