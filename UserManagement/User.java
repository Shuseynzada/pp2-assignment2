package UserManagement;

import Exceptions.*;

public class User {
    private int id;
    private String username, password;
    private Watchlist watchlist = new Watchlist();;

    // Constructors
    User(String username, String password) { // for temporary user objects
        this.username = username;
        this.password = password;
    }

    User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    User(int id, String username, String password, Watchlist watchlist) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.watchlist = watchlist;
    }

    User(int id, String username, String password, boolean save) { // Save user directly with constructor
        this.id = id;
        this.username = username;
        this.password = password;
        if (save)
            appendToFile();
    }

    // Getters
    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.username;
    }
    public Watchlist getWatchList() {
        return this.watchlist;
    }


    // Setters
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
    private void appendToFile() {
        UsersDatabase.appendToFile(this);
    }

    public static User login(String username, String password) throws UserNotFoundException {
        User tempUser = new User(-1, username, password);
        for (User user : UsersDatabase.users) {
            if (tempUser.equals(user)) {
                return user;
            }
        }
        throw new UserNotFoundException("No such User found");
    }

    public static User register(String username, String password) throws UsernameAlreadyExistsException, InvalidPasswordLengthException, InvalidPasswordDigitException, InvalidPasswordUppercaseException, InvalidUsernameException {
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

    // Overrided and other static functions

    public static boolean isPasswordValid(String password) throws InvalidPasswordLengthException, InvalidPasswordDigitException, InvalidPasswordUppercaseException {
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
            throw new InvalidPasswordUppercaseException("Password must contain upper case letters");
        }

        return true;
    }

    @Override
    public String toString() {
        return id + "," + username + "," + password+","+watchlist;
    }

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