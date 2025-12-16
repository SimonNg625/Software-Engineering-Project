package Test;
import java.io.*;
import java.util.Scanner;

import sportapp.Route;
import sportapp.screen.PortalScreen;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class PortalScreenTest {
    private PortalScreen portalscreen;
    @BeforeEach
    void setUp() {
        portalscreen = new PortalScreen();
    }
    @Test
    void testDisplay_LoginChoice() {
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = portalscreen.display(scanner, null);
        assertEquals(Route.LOGIN, result);
    }
    @Test
    void testDisplay_RegisterChoice() {
        String input = "2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = portalscreen.display(scanner, null);
        assertEquals(Route.REGISTER, result);
    }
    @Test
    void testDisplay_ResetChoice() {
        String input = "3\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = portalscreen.display(scanner, null);
        assertEquals(Route.RESET, result);
    }
    @Test
    void testDisplay_ExitChoice() {
        String input = "E\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = portalscreen.display(scanner, null);
        assertEquals(Route.EXIT, result);
    }
    @Test
    void testDisplay_InvalidInputChoice() {
    	String input = "X\n";
    	Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
    	Route result = portalscreen.display(scanner, null);
    	assertEquals(Route.PORTAL, result);
    }
}