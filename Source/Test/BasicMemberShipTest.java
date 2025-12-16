package Test;
import sportapp.membership.BasicMemberShip;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BasicMemberShipTest {
    private BasicMemberShip basicMemberShip;

    @BeforeEach
    void setUp() {
        basicMemberShip = new BasicMemberShip();
    }

    @AfterEach
    void tearDown() {
        basicMemberShip = null;
    }

    @Test
    void test_getDiscountRate(){
        assertEquals(0.0, basicMemberShip.getDiscountRate());
    }

    @Test
    void test_getPrice(){
        assertEquals(0.0, basicMemberShip.getPrice());
    }
}
