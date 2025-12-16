package Test;
import sportapp.membership.PlatinumMemberShip;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlatinumMemberShipTest {
    private PlatinumMemberShip platinumMemberShip;

    @BeforeEach
    void SetUp(){
        platinumMemberShip = new PlatinumMemberShip();
    }

    @AfterEach
    void tearDown() {
        platinumMemberShip = null;
    }

    @Test
    void test_getDiscountRate(){
        assertEquals(0.40, platinumMemberShip.getDiscountRate());
    }

    @Test
    void test_getPrice(){
        assertEquals(100.0, platinumMemberShip.getPrice());
    }
}
