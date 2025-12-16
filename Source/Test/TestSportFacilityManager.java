package Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.manager.SportFacilityManager;
import sportapp.model.SportFacility;
import sportapp.model.SportFacilityType;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TestSportFacilityManager {

    private SportFacilityManager manager;
    private SportFacilityType basketballType;
    private SportFacilityType badmintonType;
    private SportFacilityType tableTennisType;

    private SportFacility basketballCourt;
    private SportFacility badmintonCourt;
    private SportFacility tableTennisCourt;

    @BeforeEach
    public void setUp() {
        manager = SportFacilityManager.getInstance();
        // ensure deterministic starting state
        manager.getSportFacilities().clear();

        basketballType = new SportFacilityType("Basketball court", "Basketball", 30);
        badmintonType = new SportFacilityType("Badminton court", "Badminton", 20);
        tableTennisType = new SportFacilityType("Table Tennis court", "TableTennis", 40);

        basketballCourt = new SportFacility("Room Bas201", basketballType, SportFacility.Status.AVAILABLE);
        badmintonCourt = new SportFacility("Room Bad101", badmintonType, SportFacility.Status.AVAILABLE);
        tableTennisCourt = new SportFacility("Room TT301", tableTennisType, SportFacility.Status.AVAILABLE);

        manager.addSportFacility(badmintonCourt);
        manager.addSportFacility(basketballCourt);
        manager.addSportFacility(tableTennisCourt);
    }

    @Test
    public void testSingletonInstanceIsSame() {
        SportFacilityManager secondRef = SportFacilityManager.getInstance();
        assertSame(manager, secondRef, "getInstance should always return the same singleton instance");
    }

    @Test
    public void testInitialFacilitiesArePresent() {
        List<SportFacility> list = manager.getSportFacilities();
        assertEquals(3, list.size(), "Three facilities should be present after setup");
        assertTrue(list.contains(badmintonCourt));
        assertTrue(list.contains(basketballCourt));
        assertTrue(list.contains(tableTennisCourt));
    }

    @Test
    public void testAddSportFacility_increasesSize() {
        SportFacility extra = new SportFacility("Extra Room", basketballType, SportFacility.Status.AVAILABLE);
        int before = manager.getSportFacilities().size();
        manager.addSportFacility(extra);
        assertEquals(before + 1, manager.getSportFacilities().size());
        assertTrue(manager.getSportFacilities().contains(extra));
    }

    @Test
    public void testAddSportFacility_duplicateAllowed_increasesSizeAgain() {
        SportFacility extra = new SportFacility("Duplicate Room", badmintonType, SportFacility.Status.AVAILABLE);
        manager.addSportFacility(extra);
        int afterFirstAdd = manager.getSportFacilities().size();
        // adding the same object again should still add (no uniqueness enforced)
        manager.addSportFacility(extra);
        assertEquals(afterFirstAdd + 1, manager.getSportFacilities().size(), "Duplicate additions are allowed and increase the list size");
    }

    @Test
    public void testCheckFacilityExist_true_false_and_null() {
        assertTrue(manager.checkFacilityExist(basketballCourt), "Existing facility should be reported as present");
        SportFacility notAdded = new SportFacility("Non Existent", basketballType, SportFacility.Status.AVAILABLE);
        assertFalse(manager.checkFacilityExist(notAdded), "Non-added facility should be reported as absent");
        assertFalse(manager.checkFacilityExist(null), "Null passed to checkFacilityExist should return false (no exception)");
    }

    @Test
    public void testGetSportFacilityByName_found_and_notFound_and_nullName() {
        SportFacility found = manager.getSportFacilityByName("Room Bas201");
        assertNotNull(found);
        assertEquals(basketballCourt, found);

        SportFacility missing = manager.getSportFacilityByName("NoSuchRoom");
        assertNull(missing, "Lookup of a non-existent name should return null");

        SportFacility nullLookup = manager.getSportFacilityByName(null);
        assertNull(nullLookup, "Lookup with null name should return null (no exception)");
    }

    @Test
    public void testGetSportFacilities_returnsInternalList_aliasingBehavior() {
        List<SportFacility> returned = manager.getSportFacilities();
        int sizeBefore = returned.size();
        // Mutate returned list and verify manager's internal list is affected (aliasing)
        returned.remove(0);
        assertEquals(sizeBefore - 1, manager.getSportFacilities().size(), "Modifying returned list should affect internal list (aliasing)");
    }
}
