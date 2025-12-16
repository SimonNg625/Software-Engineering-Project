package Test;

import sportapp.UserSecurityAnswer;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserSecurityAnswerTest {

    private UserSecurityAnswer userSecurityAnswer;

    @BeforeEach
    void setUp() {
        userSecurityAnswer = new UserSecurityAnswer("What is your age?", "21");
    }

    @AfterEach
    void tearDown() {
        userSecurityAnswer = null;
    }
    
    @Test
    void test_getQuestion(){
        assertEquals("What is your age?", userSecurityAnswer.getQuestion());
    }

    @Test
    void test_getAnswer(){
        assertEquals("21", userSecurityAnswer.getAnswer());
    }
}
