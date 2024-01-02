package Source.UserManagement;

import Source.Exceptions.*;

/**
 * Represents a user in the system with associated properties and functionality.
 * 
 * @author Shamkhal Huseynzade
 * @version 1.0
 * @since 02/01/2024
 */
public class User {
    private int id;
    private String username, password;
    private Watchlist watchlist = new Watchlist();

    /**
     * Constructor for creating a temporary user object.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     */
    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor for creating a user object with an ID.
     * 
     * @param id       The ID of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     */
    User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor for creating a user object with an ID and a watchlist.
     * 
     * @param id        The ID of the user.
     * @param username  The username of the user.
     * @param password  The password of the user.
     * @param watchlist The user's watchlist.
     */
    User(int id, String username, String password, Watchlist watchlist) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.watchlist = watchlist;
    }

    /**
     * Constructor for creating a user object with an ID and optionally saving it.
     * 
     * @param id    The ID of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param save  Whether to save the user directly with the constructor.
     */
    User(int id, String username, String password, boolean save) {
        this.id = id;
        this.username = username;
        this.password = password;
        if (save)
            appendToFile();
    }

    /**
     * Gets the ID of the user.
     * 
     * @return The ID of the user.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the username of the user.
     * 
     * @return The username of the user.
     */
    public String getName() {
        return this.username;
    }

    /**
     * Gets the user's watchlist.
     * 
     * @return The user's watchlist.
     */
    public Watchlist getWatchList() {
        return this.watchlist;
    }

    /**
     * Gets the password of the user.
     * 
     * @return The password of the user.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets a new username for the user.
     * 
     * @param newUsername The new username to set.
     * @param password    The user's current password.
     * @return True if the username is successfully updated, false otherwise.
     */
    public boolean setName(String newUsername, String password) {
        try {
            if (newUsername == null || newUsername.isEmpty()) {
                throw new InvalidUsernameException("Username cannot be null or empty.");
            }
            if (!this.password.equals(password)) {
                throw new IncorrectPasswordException("Password doesn't match.");
            }
            this.username = newUsername;
            UsersDatabase.updateFile();
            return true;
        } catch (InvalidUsernameException | IncorrectPasswordException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Sets a new password for the user.
     * 
     * @param newPassword The new password to set.
     * @param oldPassword The user's current password.
     * @return True if the password is successfully updated, false otherwise.
     */
    public boolean setPassword(String newPassword, String oldPassword) {
        try {
            if (!this.password.equals(oldPassword)) {
                throw new IncorrectPasswordException("Old password is incorrect.");
            }
            this.password = newPassword;
            UsersDatabase.updateFile();
            return true;
        } catch (IncorrectPasswordException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Database methods

    /**
     * Appends the user information to a database file.
     */
    private void appendToFile() {
        UsersDatabase.appendToFile(this);
    }

    /**
     * Logs in a user with the provided username and password.
     * 
     * @param username The username to log in with.
     * @param password The password to log in with.
     * @return The logged-in user.
     * @throws UserNotFoundException      If the user is not found.
     * @throws IncorrectPasswordException If the password is incorrect.
     */
    public static User login(String username, String password) throws UserNotFoundException, IncorrectPasswordException {
        for (User user : UsersDatabase.users) {
            if (user.username.equals(username)) {
                if (user.getPassword().equals(password)) {
                    return user;
                } else {
                    throw new IncorrectPasswordException("Incorrect password.");
                }
            }
        }
        throw new UserNotFoundException("No such User found");
    }

    /**
     * Registers a new user with the provided username and password.
     * 
     * @param username The username to register.
     * @param password The password to set for the new user.
     * @return The registered user.
     * @throws UsernameAlreadyExistsException If the username already exists.
     * @throws InvalidPasswordLengthException  If the password length is too short.
     * @throws InvalidPasswordDigitException  If the password does not contain digits.
     * @throws InvalidPasswordUppercaseException If the password does not contain uppercase letters.
     * @throws InvalidUsernameException       If the username is invalid.
     */
    public static User register(String username, String password)
            throws UsernameAlreadyExistsException, InvalidPasswordLengthException, InvalidPasswordDigitException,
            InvalidPasswordUppercaseException, InvalidUsernameException {
        boolean userFlag = false;
        for (User user : UsersDatabase.users) {
            if (user.username.equals(username)) {
                userFlag = true;
                break;
            }
        }

        if (username == null || username.isEmpty()) {
            throw new InvalidUsernameException("Username cannot be null or empty.");
        }
        if (userFlag) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if (!isPasswordValid(password)) {
            System.out.println("Password is not valid");
            return new User("true", null);
        }
        return new User(UsersDatabase.users.size(), username, password, true);
    }

    /**
     * Checks if a password meets certain validity criteria.
     * 
     * @param password The password to check.
     * @return True if the password is valid, false otherwise.
     * @throws InvalidPasswordLengthException   If the password length is too short.
     * @throws InvalidPasswordDigitException   If the password does not contain digits.
     * @throws InvalidPasswordUppercaseException If the password does not contain uppercase letters.
     */
    public static boolean isPasswordValid(String password)
            throws InvalidPasswordLengthException, InvalidPasswordDigitException, InvalidPasswordUppercaseException {
        if (password.length() <= 6) {
            throw new InvalidPasswordLengthException("Password must contain more than 6 characters");
        }

        boolean containsDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                containsDigit = true;
                break;
            }
        }

        if (!containsDigit) {
            throw new InvalidPasswordDigitException("Password must contain digits");
        }

        boolean containsUppercase = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                containsUppercase = true;
                break;
            }
        }

        if (!containsUppercase) {
            throw new InvalidPasswordUppercaseException("Password must contain uppercase letters");
        }

        return true;
    }

    /**
     * Converts the user object to a string representation.
     * 
     * @return A string representation of the user object.
     */
    @Override
    public String toString() {
        return id + "," + username + "," + password + "," + watchlist;
    }

    /**
     * Checks if two user objects are equal.
     * 
     * @param o The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return this.username.equals(user.username) && this.password.equals(user.password);
    }
}
