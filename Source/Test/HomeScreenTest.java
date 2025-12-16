package Test;
import java.io.*;
import java.util.Scanner;

import sportapp.Route;
import sportapp.screen.HomeScreen;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class HomeScreenTest {
    private HomeScreen homescreen;
    @BeforeEach
    void setUp() {
        homescreen = new HomeScreen();
    }
    @Test
    void testDisplay_FacilityBookingChoice() {
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = homescreen.display(scanner, null);
        assertEquals(Route.FACILITY_BOOKING, result);
    }
    @Test
    void testDisplay_BookEquipmentChoice() {
        String input = "2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = homescreen.display(scanner, null);
        assertEquals(Route.BOOK_EQUIPMENT, result);
    }
    @Test
    void testDisplay_CurrentBookingsChoice() {
        String input = "3\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = homescreen.display(scanner, null);
        assertEquals(Route.CURRENT_BOOKINGS, result);
    }
    @Test
    void testDisplay_ConfirmedBookingsChoice() {
        String input = "4\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = homescreen.display(scanner, null);
        assertEquals(Route.CONFIRMED_BOOKINGS, result);
    }
    @Test
    void testDisplay_LogoutChoice() {
        String input = "L\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = homescreen.display(scanner, null);
        assertEquals(Route.LOGOUT, result);
    }
    @Test
    void testDisplay_ExitChoice() {
        String input = "E\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = homescreen.display(scanner, null);
        assertEquals(Route.EXIT, result);
    }
    @Test
    void testDisplay_InvalidInputChoice() {
        String input = "X\nE\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Route result = homescreen.display(scanner, null);
        assertEquals(Route.HOME, result);
    }
}