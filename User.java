public class User {
    private int id;
    private String username, password;

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

    User(int id, String username, String password, boolean save) { // Save user directly with constructor
        this.id = id;
        this.username = username;
        this.password = password;
        if (save)
            saveToFile();
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.username;
    }

    // Setters
    public boolean setName(String newUsername, String password) {
        if (this.password != password) {
            System.out.println("Password doesn't match");
            return false;
        }
        this.username = newUsername;
        return true;
    }

    public boolean setPassword(String newPassword, String oldPassword) {
        if (this.password != oldPassword) {
            System.out.println("Password doesn't match");
            return false;
        }
        this.password = newPassword;
        return true;
    }

    // Database methods
    private void saveToFile() {
        UsersDatabase.saveToFile(this);
    }

    public static User login(String username, String password) {
        User tempUser = new User(-1, username, password);
        for (User user : UsersDatabase.users) {
            if (tempUser.equals(user)) {
                return user;
            }
        }
        System.out.println("No such User found");
        return null;
    }

    public static User register(String username, String password) {
        boolean userFlag = false;
        for (User user : UsersDatabase.users) {
            if (user.username.equals(username)) {
                userFlag = true;
                break;
            }
        }

        if (userFlag) {
            System.out.println("Username already exists"); // Exception handling needed
            return null;
        }

        if (!(isPasswordValid(password))) {
            System.out.println("Password is not valid");
            return null;// Exception handling needed
        }
        return new User(UsersDatabase.users.size(), username, password, true);

    }

    // Overrided and other static functions

    public static boolean isPasswordValid(String password) { // Exception handling needed
        boolean lessLength = true;
        if (password.length() <= 6)
            lessLength = false;

        boolean containsDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                containsDigit = true;
                break;
            }
        }

        boolean containsUppercase = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                containsUppercase = true;
                break;
            }
        }

        if (!lessLength) System.out.println("Password must contain more than 6 characters");
        if (!containsDigit) System.out.println("Password must contain digits");
        if (!containsUppercase)System.out.println("Password must contain upper case letters");
        
        return lessLength && containsDigit && containsUppercase;
    }

    @Override
    public String toString() {
        return id + "," + username + "," + password;
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

    public static void main(String[] args) {
        User us1 = User.register("samxal2", "Aas1234");
        System.out.println(us1);
    }
}