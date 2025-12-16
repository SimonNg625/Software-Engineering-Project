package Test;
import sportapp.SportApp;
import sportapp.User;
import sportapp.UserSecurityAnswer;

import java.io.*;
import java.util.Scanner;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SportAppTest {
    private SportApp sportApp;
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        scanner = null;
        sportApp = new SportApp(scanner);
    }

    @AfterEach
    void tearDown() {   
        sportApp = null;
    }   

    @Test
    void test_setAndGetCurrentUser(){
        User user = new User("bot", 1, "123456", new UserSecurityAnswer("What is your age?", "21"));
        SportApp.setCurrentUser(user);
        assertEquals(user, SportApp.getCurrentUser());
    }

    @Test
    void test_ContinueOrNot_Continue(){
        Scanner scanner = null;
        String choice = "Y";
        boolean result = SportApp.ContinueOrNot(choice, scanner);
        assertEquals(true, result);
    }

    @Test
    void test_ContinueOrNot_NotContinue(){
        Scanner scanner = null;
        String choice = "N";
        boolean result = SportApp.ContinueOrNot(choice, scanner);
        assertEquals(false, result);
    }

    @Test
    void test_ContinueOrNot_InvalidChoice_Continue(){
        String input = "Y\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        String choice = "X";
        boolean result = SportApp.ContinueOrNot(choice, scanner);
        assertEquals(true, result);
    }

    @Test
    void test_ContinueOrNot_InvalidChoice_NotContinue(){
        String input = "N\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        String choice = "X";
        boolean result = SportApp.ContinueOrNot(choice, scanner);
        assertEquals(false, result);
    }
}
