package Tests;

import org.junit.*;

import Source.Exceptions.*;
import Source.UserManagement.*;

import static org.junit.Assert.*;

public class UserTests {

    private static final String TEST_USERS_FILE_PATH = "Resources/test_users.csv";
    private static final String ORIGINAL_USERS_FILE_PATH = UsersDatabase.getFilePath(); 


    @BeforeClass
    public static void setupClass() {
        UsersDatabase.setFilePath(TEST_USERS_FILE_PATH); // Set to the test file
    }

    @Before
    public void setUp() {
        clearFile(TEST_USERS_FILE_PATH);
    }
    
    @Test
    public void testUserRegistrationSuccess() throws Exception {
        String username = "newuser";
        String password = "Password123";
        User result = User.register(username, password);
        assertNotNull("User should be registered successfully", result);
    }

    @Test(expected = UsernameAlreadyExistsException.class)
    public void testUserRegistrationWithDuplicateUsername() throws Exception {
        String username = "emil";
        String password = "Emil2004";
        User.register(username, password); // Assuming this user already exists
        User.register(username, password); // Attempting to register again
    }

    @Test
    public void testUserLoginSuccess() throws Exception {
        String username = "emil";
        String password = "Emil2004";
        User.register(username, password); // Ensure user exists
        User result = User.login(username, password);
        assertNotNull("User should be able to login", result);
    }

    @Test(expected = UserNotFoundException.class)
    public void testUserLoginWithNonExistentUser() throws Exception {
        User.login("nonexistentuser", "Password123");
    }

    @Test(expected = IncorrectPasswordException.class)
    public void testUserLoginWithIncorrectPassword() throws Exception {
        String username = "emil";
        String correctPassword = "eMil2004";
        String wrongPassword = "WrongPassword";
        User.register(username, correctPassword); // Ensure user exists
        User.login(username, wrongPassword); // Attempting to login with wrong password
    }

    @After
    public void tearDown() {
        clearFile(TEST_USERS_FILE_PATH);
    }

    @AfterClass
    public static void tearDownClass() {
       UsersDatabase.setFilePath(ORIGINAL_USERS_FILE_PATH); // Reset to the original file
    }

    private static void clearFile(String path) {
        try {
            new File(path).delete();
            Files.createFile(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
