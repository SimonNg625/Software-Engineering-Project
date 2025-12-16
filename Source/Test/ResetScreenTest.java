package Test;
import java.io.*;
import java.util.Scanner;

import sportapp.Route;
import sportapp.UserCollection;
import sportapp.UserSecurityAnswer;
import sportapp.screen.ResetScreen;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ResetScreenTest {
    private ResetScreen resetScreen;
    private UserCollection userCollection;

    @BeforeEach
    void setUp() {
        resetScreen = new ResetScreen();
        userCollection = UserCollection.getInstance();
        userCollection.clear();
    }

    @Test
    void testDisplay_SuccessfulReset() {
        userCollection.addUser("bot", "12345", new UserSecurityAnswer("What is your age?", "21"));
        String input = "bot\n21\nStrongP@ss1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = resetScreen.display(scanner, null);
        assertEquals(Route.HOME, result);
        assertEquals("StrongP@ss1", userCollection.findUserByName("bot").getPassword());
    }

    @Test
    void testDisplay_FailedReset_NonExistentUser() {
        userCollection.addUser("bot", "12345", new UserSecurityAnswer("What is your age?", "21"));
        String input = "Tommy\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = resetScreen.display(scanner, null);
        assertEquals(Route.RESET, result);
    }

    @Test
    void testDisplay_FailedReset_WrongSecurityAnswer_Continue(){
        userCollection.addUser("bot", "12345", new UserSecurityAnswer("What is your age?", "21"));
        String input = "bot\n20\nY\n21\nStrongP@ss1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = resetScreen.display(scanner, null);
        assertEquals(Route.HOME, result);
        assertEquals("StrongP@ss1", userCollection.findUserByName("bot").getPassword());
    }

    @Test
    void testDisplay_FailedReset_WrongSecurityAnswer_NotContinue(){
        userCollection.addUser("bot", "12345", new UserSecurityAnswer("What is your age?", "21"));
        String input = "bot\n20\nN\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = resetScreen.display(scanner, null);
        assertEquals(Route.PORTAL, result);
        assertEquals("12345", userCollection.findUserByName("bot").getPassword());
    }

    @Test
    void testDisplay_FailedReset_WeakPassword_Continue(){
        userCollection.addUser("bot", "12345", new UserSecurityAnswer("What is your age?", "21"));
        String input = "bot\n21\nweakpass\nY\nStrongP@ss1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = resetScreen.display(scanner, null);
        assertEquals(Route.HOME, result);
        assertEquals("StrongP@ss1", userCollection.findUserByName("bot").getPassword());
    }

    @Test
    void testDisplay_FailedReset_WeakPassword_NotContinue(){
        userCollection.addUser("bot", "12345", new UserSecurityAnswer("What is your age?", "21"));
        String input = "bot\n21\nweakpass\nN\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = resetScreen.display(scanner, null);
        assertEquals(Route.PORTAL, result);
        assertEquals("12345", userCollection.findUserByName("bot").getPassword());
    }
}