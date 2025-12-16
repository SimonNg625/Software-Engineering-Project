package Test;
import java.io.*;
import java.util.Scanner;

import sportapp.Route;
import sportapp.UserCollection;
import sportapp.UserSecurityAnswer;
import sportapp.screen.RegisterScreen;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterScreenTest {

    private RegisterScreen registerScreen;
    private UserCollection userCollection;

    @BeforeEach
    void setUp() {
        registerScreen = new RegisterScreen();
        userCollection = UserCollection.getInstance();
        userCollection.clear(); // Ensure clean state
    }

    @Test
    void testSuccessfulRegistration() {
        String input = "newUser\nStrongPass1!\nMy pet?\nDog\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = registerScreen.display(scanner, null);

        assertEquals(Route.HOME, result);
        assertNotNull(userCollection.findUserByName("newUser"));
    }

    @Test
    void testUsernameAlreadyTakenThenSuccess() {
        userCollection.addUser("existingUser", "StrongPass1!", new UserSecurityAnswer("Q", "A"));
        String input = "existingUser\nY\nnewUser\nStrongPass1!\nFav color?\nBlue\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        Route result = registerScreen.display(scanner, null);

        assertEquals(Route.HOME, result);
        assertNotNull(userCollection.findUserByName("newUser"));
    }

    @Test
    void testWeakPasswordThenSuccess() {
        String input = "user123\nweak\nY\nStrongPass1!\nFav food?\nPizza\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        Route result = registerScreen.display(scanner, null);

        assertEquals(Route.HOME, result);
        assertEquals("StrongPass1!", userCollection.findUserByName("user123").getPassword());
    }

    @Test
    void testAbortOnUsernameConflict() {
        userCollection.addUser("taken", "StrongPass1!", new UserSecurityAnswer("Q", "A"));
        String input = "taken\nN\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        Route result = registerScreen.display(scanner, null);

        assertEquals(Route.PORTAL, result);
    }

    @Test
    void testAbortOnWeakPassword() {
        String input = "userX\nweak\nN\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        Route result = registerScreen.display(scanner, null);

        assertEquals(Route.PORTAL, result);
    }
}