package Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.model.SportFacility;
import sportapp.model.SportFacilityType;

import static org.junit.jupiter.api.Assertions.*;

public class TestSportFacility {

    private SportFacilityType basketballType;
    private SportFacilityType nullType;

    @BeforeEach
    void setUp() {
        basketballType = new SportFacilityType("Basketball court", "Basketball", 30.0);
        nullType = null;
    }

    @Test
    void testConstructorAndGetters_available() {
        SportFacility facility = new SportFacility("Room Bas201", basketballType, SportFacility.Status.AVAILABLE);

        assertEquals("Room Bas201", facility.getName(), "Name should be returned as set");
        assertSame(basketballType, facility.getSportFacilityType(), "Type getter should return the exact instance passed to constructor");
        assertEquals(SportFacility.Status.AVAILABLE, facility.getStatus(), "Status should be AVAILABLE as set");
    }

    @Test
    void testConstructorAndGetters_underMaintenance() {
        SportFacility facility = new SportFacility("Room TT301", basketballType, SportFacility.Status.UNDER_MAINTENANCE);

        assertEquals("Room TT301", facility.getName());
        assertSame(basketballType, facility.getSportFacilityType());
        assertEquals(SportFacility.Status.UNDER_MAINTENANCE, facility.getStatus(), "Status should be UNDER_MAINTENANCE as set");
    }

    @Test
    void testNullNameAndTypeAllowed() {
        // Ensure class accepts null values (no validation in constructor) and getters return them
        SportFacility facility = new SportFacility(null, nullType, SportFacility.Status.AVAILABLE);

        assertNull(facility.getName(), "Name can be null and should be returned as null");
        assertNull(facility.getSportFacilityType(), "SportFacilityType can be null and should be returned as null");
        assertEquals(SportFacility.Status.AVAILABLE, facility.getStatus());
    }

    @Test
    void testSportFacilityTypePropertiesPreserved() {
        // Verify SportFacilityType object passed remains intact and accessible
        SportFacility facility = new SportFacility("Room Bad101", basketballType, SportFacility.Status.AVAILABLE);
        SportFacilityType returned = facility.getSportFacilityType();

        assertNotNull(returned);
        assertEquals("Basketball court", returned.getFacilityTypeName());
        assertEquals("Basketball", returned.getSportType());
        assertEquals(30.0, returned.getPricePerHour(), 0.0001);
    }

    @Test
    void testStatusEnumValuesAccessible() {
        // Exercise enum API to ensure both branches/values are reachable in tests
        SportFacility.Status s1 = SportFacility.Status.valueOf("AVAILABLE");
        SportFacility.Status s2 = SportFacility.Status.valueOf("UNDER_MAINTENANCE");

        assertEquals(SportFacility.Status.AVAILABLE, s1);
        assertEquals(SportFacility.Status.UNDER_MAINTENANCE, s2);

        SportFacility.Status[] all = SportFacility.Status.values();
        assertTrue(all.length >= 2, "Enum should expose at least the two expected values");
    }
}
