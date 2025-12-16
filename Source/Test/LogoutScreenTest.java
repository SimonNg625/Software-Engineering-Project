package Test;
import java.io.*;
import java.util.Scanner;

import sportapp.Route;
import sportapp.screen.LogoutScreen;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class LogoutScreenTest {
    private LogoutScreen logoutScreen;
    @BeforeEach
    void setUp() {
        logoutScreen = new LogoutScreen();
    }
    @Test
    void testDisplay_Logout() {
        String input = "";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = logoutScreen.display(scanner,null);
        assertEquals(Route.PORTAL, result);
    }
}