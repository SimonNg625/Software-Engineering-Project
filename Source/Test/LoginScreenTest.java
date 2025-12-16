package Test;
import java.io.*;
import java.util.Scanner;

import sportapp.Route;
import sportapp.SportApp;
import sportapp.UserCollection;
import sportapp.UserSecurityAnswer;
import sportapp.screen.LoginScreen;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class LoginScreenTest {
    private LoginScreen loginscreen;
    private UserCollection userCollection;

    @BeforeEach
    void setUp() {
        loginscreen = new LoginScreen();
        userCollection = UserCollection.getInstance();
    }
    
    @AfterEach
    void tearDown() {
    	SportApp.setCurrentUser(null);
        userCollection.clear();
    }

    @Test
    void testDisplay_SuccessfulLogin() {
        userCollection.addUser("bot", "12345", new UserSecurityAnswer("What is your age?", "21"));
        String input = "bot\n12345\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result =loginscreen.display(scanner, null);
        assertEquals(Route.HOME, result);
        assertEquals("bot", SportApp.getCurrentUser().getUsername());
    }
    @Test
    void testDisplay_FailedLogin_Continue(){
        userCollection.addUser("bot", "12345", new UserSecurityAnswer("What is your age?", "21"));
        String input = "bot\n123456\nY\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result =loginscreen.display(scanner, null);
        assertEquals(Route.LOGIN, result);
        assertEquals(null, SportApp.getCurrentUser());
    }
    @Test
    void testDisplay_FailedLogin_NotContinue(){
        userCollection.addUser("bot", "12345", new UserSecurityAnswer("What is your age?", "21"));
        String input = "bot\n123456\nN\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = loginscreen.display(scanner, null);
        assertEquals(Route.PORTAL, result);
    }
}