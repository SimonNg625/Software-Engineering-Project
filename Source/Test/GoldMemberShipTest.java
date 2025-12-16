package Test;
import sportapp.membership.GoldMemberShip;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GoldMemberShipTest {
    private GoldMemberShip goldMemberShip = new GoldMemberShip();

    @BeforeEach
    void SetUp(){
        goldMemberShip = new GoldMemberShip();
    }

    @AfterEach
    void tearDown() {
        goldMemberShip = null;
    }

    @Test
    void test_getDiscountRate(){
        assertEquals(0.20, goldMemberShip.getDiscountRate());
    }

    @Test
    void test_getPrice(){
        assertEquals(50.0, goldMemberShip.getPrice());
    }
}
