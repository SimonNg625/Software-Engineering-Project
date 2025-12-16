package Test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import sportapp.model.SportFacilityType;

import static org.junit.jupiter.api.Assertions.*;

public class TestSportFacilityType {

    private SportFacilityType normalType;

    @BeforeEach
    void setUp() {
        normalType = new SportFacilityType("Badminton court", "Badminton", 20.0);
    }

    @Test
    void testConstructorAndGetters_normalValues() {
        assertEquals("Badminton court", normalType.getFacilityTypeName());
        assertEquals("Badminton", normalType.getSportType());
        assertEquals(20.0, normalType.getPricePerHour(), 0.000001);
    }

    @Test
    void testConstructorWithNullAndEmptyStrings() {
        SportFacilityType nullName = new SportFacilityType(null, null, 0.0);
        assertNull(nullName.getFacilityTypeName());
        assertNull(nullName.getSportType());
        assertEquals(0.0, nullName.getPricePerHour(), 0.000001);

        SportFacilityType emptyStrings = new SportFacilityType("", "", 5.5);
        assertEquals("", emptyStrings.getFacilityTypeName());
        assertEquals("", emptyStrings.getSportType());
        assertEquals(5.5, emptyStrings.getPricePerHour(), 0.000001);
    }

    @Test
    void testPriceNegativeAndLargeValues() {
        SportFacilityType negativePrice = new SportFacilityType("X", "Y", -10.75);
        assertEquals(-10.75, negativePrice.getPricePerHour(), 0.000001);

        double large = 1e12;
        SportFacilityType largePrice = new SportFacilityType("Large", "Z", large);
        assertEquals(large, largePrice.getPricePerHour(), 0.000001);
    }

    @Test
    void testPriceNaNAndInfinity() {
        SportFacilityType nanPrice = new SportFacilityType("NaN", "N", Double.NaN);
        assertTrue(Double.isNaN(nanPrice.getPricePerHour()));

        SportFacilityType posInf = new SportFacilityType("Inf", "I", Double.POSITIVE_INFINITY);
        assertTrue(Double.isInfinite(posInf.getPricePerHour()));
        assertTrue(posInf.getPricePerHour() > 0);

        SportFacilityType negInf = new SportFacilityType("NegInf", "I", Double.NEGATIVE_INFINITY);
        assertTrue(Double.isInfinite(negInf.getPricePerHour()));
        assertTrue(negInf.getPricePerHour() < 0);
    }

    @Test
    void testMultipleInstancesIndependent() {
        SportFacilityType a = new SportFacilityType("A", "S1", 1.0);
        SportFacilityType b = new SportFacilityType("B", "S2", 2.0);

        assertEquals("A", a.getFacilityTypeName());
        assertEquals("S1", a.getSportType());
        assertEquals(1.0, a.getPricePerHour(), 0.000001);

        assertEquals("B", b.getFacilityTypeName());
        assertEquals("S2", b.getSportType());
        assertEquals(2.0, b.getPricePerHour(), 0.000001);

        // Ensure changing reference doesn't affect the other instance (defensive check)
        a = new SportFacilityType("A-mod", "S1-mod", 10.0);
        assertEquals("B", b.getFacilityTypeName());
        assertEquals(2.0, b.getPricePerHour(), 0.000001);
    }
}
