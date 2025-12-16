package Test;

import sportapp.User;
import sportapp.UserCollection;
import sportapp.UserSecurityAnswer;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.beans.Transient;



public class UserCollectionTest {
    private UserCollection userCollection;

    @BeforeEach
    void setUp() {
        userCollection = UserCollection.getInstance();
    }

    @AfterEach
    void tearDown() {
        userCollection.clear();
    }

    @Test
    void test_addUserAndFindUserByNameSuccessfully(){
        User newuser = userCollection.addUser("bot", "123456", new UserSecurityAnswer("What is your age?", "21"));
        assertEquals(newuser, userCollection.findUserByName("bot"));
    }

    @Test
    void test_removeUserByNameSuccessfullyAndFindUserByNameFailed(){
        User newuser = userCollection.addUser("bot", "123456", new UserSecurityAnswer("What is your age?", "21"));
        userCollection.removeUserByName("bot");
        assertEquals(null, userCollection.findUserByName("bot"));
    }

    @Test
    void test_checkUserExistSuccessfully(){
        User newuser = userCollection.addUser("bot", "123456", new UserSecurityAnswer("What is your age?", "21"));
        assertEquals(true, userCollection.checkUserExist(newuser));
    }

    @Test
    void test_checkUserExistFailed(){
        User newuser = new User("bot", 1, "123456", new UserSecurityAnswer("What is your age?", "21"));
        assertEquals(false, userCollection.checkUserExist(newuser));
    }
}
