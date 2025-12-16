
// language: java
package Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.User;
import sportapp.UserCollection;
import sportapp.manager.FacilityBookManager;
import sportapp.model.BookingStatus;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;
import sportapp.model.SportFacilityType;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestFacilityBookManager {

    private FacilityBookManager manager;
    private SportFacility sf;
    private User userA;
    private User userB;

    @BeforeEach
    public void setUp() {
        manager = FacilityBookManager.getInstance();
        // Clear shared singleton state before each test
        manager.getBookingRecords().clear();

        // Prepare a simple facility
        SportFacilityType t = new SportFacilityType("Court", "SomeSport", 10.0);
        sf = new SportFacility("Court 1", t, SportFacility.Status.AVAILABLE);

        // Users
        userA = new User("alice", 1, "pw", null);
        userB = new User("bob", 2, "pw", null);

//        // Ensure user collection state is predictable (add userA only when tests need it)
//        try {
//            UserCollection.getInstance().removeAllUsers();
//        } catch (Throwable ignored) {
//            // If there's no helper to clear users, ignore; tests that rely on user existence will add explicitly.
//        }
    }

    // -------------------------
    // addBooking tests
    // -------------------------
    @Test
    public void addBooking_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> manager.addBooking(null));
    }

    @Test
    public void addBooking_duplicate_throws() {
        FacilityBookRecord rec = new FacilityBookRecord(sf, userA, LocalDate.of(2025,1,1), 9, 10, BookingStatus.PENDING);
        manager.addBooking(rec);
        // adding same reference again should trigger the "already exists" branch
        assertThrows(IllegalArgumentException.class, () -> manager.addBooking(rec));
    }

    @Test
    public void addBooking_success_addsRecord() {
        FacilityBookRecord rec = new FacilityBookRecord(sf, userA, LocalDate.of(2025,1,2), 10, 11, BookingStatus.PENDING);
        manager.addBooking(rec);
        assertTrue(manager.getBookingRecords().contains(rec));
        assertEquals(1, manager.getBookingRecords().size());
    }

    // -------------------------
    // removeBooking tests
    // -------------------------
    @Test
    public void removeBooking_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> manager.removeBooking(null));
    }

    @Test
    public void removeBooking_nonexistent_throws() {
        FacilityBookRecord notAdded = new FacilityBookRecord(sf, userA, LocalDate.of(2025,2,1), 11, 12, BookingStatus.PENDING);
        // notAdded is never added -> should throw
        assertThrows(IllegalArgumentException.class, () -> manager.removeBooking(notAdded));
    }

    @Test
    public void removeBooking_existing_removes() {
        FacilityBookRecord rec = new FacilityBookRecord(sf, userA, LocalDate.of(2025,2,2), 12, 13, BookingStatus.PENDING);
        manager.addBooking(rec);
        // now remove should succeed
        manager.removeBooking(rec);
        assertFalse(manager.getBookingRecords().contains(rec));
        assertEquals(0, manager.getBookingRecords().size());
    }

    // -------------------------
    // isBookingExist tests
    // -------------------------
    @Test
    public void isBookingExist_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> manager.isBookingExist(null));
    }

    @Test
    public void isBookingExist_true_and_false() {
        FacilityBookRecord rec = new FacilityBookRecord(sf, userA, LocalDate.of(2025,3,1), 9, 10, BookingStatus.PENDING);
        assertFalse(manager.isBookingExist(rec)); // not added yet
        manager.addBooking(rec);
        assertTrue(manager.isBookingExist(rec));  // now exists
        // a different instance with same values should still be considered not present (contains uses object equality)
        FacilityBookRecord differentInstance = new FacilityBookRecord(sf, userA, LocalDate.of(2025,3,1), 9, 10, BookingStatus.PENDING);
        assertFalse(manager.isBookingExist(differentInstance));
    }

    // -------------------------
    // getUserBooking tests (PENDING)
    // -------------------------
    @Test
    public void getUserBooking_nullUser_throws() {
        assertThrows(IllegalArgumentException.class, () -> manager.getUserBooking(null));
    }

    @Test
    public void getUserBooking_userNotRegistered_throws() {
        // userA has not been registered with UserCollection -> should trigger user-not-exist branch
        assertThrows(IllegalArgumentException.class, () -> manager.getUserBooking(userA));
    }

    @Test
    public void getUserBooking_returnsOnlyPendingForUser() {
        // Register userA in UserCollection so the "user exists" branch is satisfied
        UserCollection.getInstance().addUser(userA);

        // add bookings:
        FacilityBookRecord pendingForA = new FacilityBookRecord(sf, userA, LocalDate.of(2025,4,1), 9, 10, BookingStatus.PENDING);
        FacilityBookRecord confirmedForA = new FacilityBookRecord(sf, userA, LocalDate.of(2025,4,2), 11, 12, BookingStatus.CONFIRMED);
        FacilityBookRecord pendingForB = new FacilityBookRecord(sf, userB, LocalDate.of(2025,4,3), 12, 13, BookingStatus.PENDING);

        manager.addBooking(pendingForA);
        manager.addBooking(confirmedForA);
        manager.addBooking(pendingForB);

        ArrayList<FacilityBookRecord> result = manager.getUserBooking(userA);
        assertEquals(1, result.size());
        assertTrue(result.contains(pendingForA));
        assertFalse(result.contains(confirmedForA));
        assertFalse(result.contains(pendingForB));
    }

    // -------------------------
    // getUserConfirmedBooking tests (CONFIRMED)
    // -------------------------
    @Test
    public void getUserConfirmedBooking_nullUser_throws() {
        assertThrows(IllegalArgumentException.class, () -> manager.getUserConfirmedBooking(null));
    }

    @Test
    public void getUserConfirmedBooking_userNotRegistered_throws() {
        // userB not registered -> should throw
        assertThrows(IllegalArgumentException.class, () -> manager.getUserConfirmedBooking(userB));
    }

    @Test
    public void getUserConfirmedBooking_returnsOnlyConfirmedForUser() {
        // Register userA in UserCollection
        UserCollection.getInstance().addUser(userA);

        FacilityBookRecord confirmedForA1 = new FacilityBookRecord(sf, userA, LocalDate.of(2025,5,1), 9, 10, BookingStatus.CONFIRMED);
        FacilityBookRecord confirmedForA2 = new FacilityBookRecord(sf, userA, LocalDate.of(2025,5,2), 10, 11, BookingStatus.CONFIRMED);
        FacilityBookRecord pendingForA = new FacilityBookRecord(sf, userA, LocalDate.of(2025,5,3), 11, 12, BookingStatus.PENDING);
        FacilityBookRecord confirmedForB = new FacilityBookRecord(sf, userB, LocalDate.of(2025,5,4), 12, 13, BookingStatus.CONFIRMED);

        manager.addBooking(confirmedForA1);
        manager.addBooking(confirmedForA2);
        manager.addBooking(pendingForA);
        manager.addBooking(confirmedForB);

        ArrayList<FacilityBookRecord> confirmed = manager.getUserConfirmedBooking(userA);
        assertEquals(2, confirmed.size());
        assertTrue(confirmed.contains(confirmedForA1));
        assertTrue(confirmed.contains(confirmedForA2));
        assertFalse(confirmed.contains(pendingForA));
        assertFalse(confirmed.contains(confirmedForB));
    }
}

