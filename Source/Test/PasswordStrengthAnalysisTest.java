package Test;

import sportapp.PasswordStrengthAnalysis;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordStrengthAnalysisTest {
    PasswordStrengthAnalysis passwordStrengthAnalysis;

    @BeforeEach
    void setUp() {
        passwordStrengthAnalysis = new PasswordStrengthAnalysis();
    }

    @AfterEach
    void tearDown() {
        passwordStrengthAnalysis = null;
    }

    @Test
    void test_isStrongPassword_Strong(){
        String password = "@Strongp4ssword";
        assertEquals(true, PasswordStrengthAnalysis.isStrongPassword(password));
    }

    @Test
    void test_isStrongPassword_Weak_LessThan8(){
        String password = "@A1b";
        assertEquals(false, PasswordStrengthAnalysis.isStrongPassword(password));
    }

    @Test
    void test_isStrongPassword_Weak_MissingUppercase(){
        String password = "@strongp4ssword";
        assertEquals(false, PasswordStrengthAnalysis.isStrongPassword(password));
    }

    @Test
    void test_isStrongPassword_Weak_MissingLowercase(){
        String password = "@STRONGP4SSWORD";
        assertEquals(false, PasswordStrengthAnalysis.isStrongPassword(password));
    }

    @Test
    void test_isStrongPassword_Weak_MissingDigit(){
        String password = "@StrongPassword";
        assertEquals(false, PasswordStrengthAnalysis.isStrongPassword(password));
    }

    @Test
    void test_isStrongPassword_Weak_MissingSymbol(){
        String password = "Strongp4ssword";
        assertEquals(false, PasswordStrengthAnalysis.isStrongPassword(password));
    }
}
