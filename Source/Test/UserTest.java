package Test;

import sportapp.User;
import sportapp.UserSecurityAnswer;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.beans.Transient;

public class UserTest {
    private User user;

    @BeforeEach
    void SetUp(){
        user = new User("bot", 1, "123456", new UserSecurityAnswer("What is your age?", "21"));
    }

    @AfterEach
    void tearDown(){
        user = null;
    }

    @Test
    void test_getUsername(){
        assertEquals("bot", user.getUsername());
    }

    @Test
    void test_getUserID(){
        assertEquals(1, user.getUserID());
    }

    @Test
    void test_getPassword(){
        assertEquals("123456", user.getPassword());
    }

    @Test
    void test_setPassword(){
        user.setPassword("654321");
        assertEquals("654321", user.getPassword());
    }

    @Test
    void test_getQuestion(){
        assertEquals("What is your age?", user.getQuestion());
    }

    @Test
    void test_verifySecurityAnswer_CorrectAnswer(){
        assertEquals(true, user.verifySercurityAnswer("21"));
    }

    @Test
    void test_verifySecurityAnswer_WrongAnswer(){
        assertEquals(false, user.verifySercurityAnswer("22"));
    }

    @Test
    void test_verifyPassword_CorrectPassword(){
        assertEquals(true, user.verifyPassword("123456"));
    }

    @Test
    void test_verifyPassword_WrongPassword(){
        assertEquals(false, user.verifyPassword("654321"));
    }

}
