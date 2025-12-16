package Test;

import org.junit.jupiter.api.Test;

import sportapp.User;
import sportapp.model.BookingStatus;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;
import sportapp.model.SportFacilityType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestFacilityBookRecord {

    @Test
    public void testConstructorAndGetters() {
        SportFacilityType type = new SportFacilityType("Badminton court", "Badminton", 20);
        SportFacility facility = new SportFacility("Room Bad101", type, SportFacility.Status.AVAILABLE);
        User user = new User("alice", 1, "pw", null);
        LocalDate date = LocalDate.of(2025, 6, 15);

        FacilityBookRecord rec = new FacilityBookRecord(facility, user, date, 10, 12, BookingStatus.PENDING);

        // Basic getters
        assertSame(facility, rec.getSportFacility());
        assertSame(user, rec.getUser());
//        assertSame(user, rec.getBookedBy()); // both accessors should return same user reference
        assertEquals(date, rec.getDate());
        assertEquals(10, rec.getStartHour());
        assertEquals(12, rec.getEndHour());
        assertEquals(BookingStatus.PENDING, rec.getStatus());

        // timeslot array contents
        int[] timeslot = rec.getTimeslot();
        assertArrayEquals(new int[]{10, 12}, timeslot);

        // toString contains expected substrings (full exact check is in another test)
        String s = rec.toString();
        assertTrue(s.contains("Booking confirmed for: " + facility.getName()));
        assertTrue(s.contains(date.toString()));
        assertTrue(s.contains("10:00 - 12:00"));
    }

    @Test
    public void testSettersUpdateFacilityAndTimeslotReflectsChanges() {
        SportFacilityType t1 = new SportFacilityType("Basketball court", "Basketball", 30);
        SportFacility f1 = new SportFacility("Room Bas201", t1, SportFacility.Status.AVAILABLE);
        SportFacilityType t2 = new SportFacilityType("Table Tennis court", "TableTennis", 40);
        SportFacility f2 = new SportFacility("Room TT301", t2, SportFacility.Status.AVAILABLE);

        User user = new User("bob", 2, "pw", null);
        LocalDate initialDate = LocalDate.of(2025, 7, 1);

        FacilityBookRecord rec = new FacilityBookRecord(f1, user, initialDate, 9, 11, BookingStatus.CONFIRMED);

        // change facility via setter
        rec.setSportFacility(f2);
        assertSame(f2, rec.getSportFacility());

        // change facility via updateFacility
        SportFacilityType t3 = new SportFacilityType("Extra", "ExtraSport", 10);
        SportFacility f3 = new SportFacility("Extra Room", t3, SportFacility.Status.AVAILABLE);
        rec.updateFacility(f3);
        assertSame(f3, rec.getSportFacility());

        // change date/time/status via setters
        LocalDate newDate = LocalDate.of(2025, 7, 2);
        rec.setDate(newDate);
        rec.setStartHour(14);
        rec.setEndHour(16);
        rec.setStatus(BookingStatus.ENDED);

        assertEquals(newDate, rec.getDate());
        assertEquals(14, rec.getStartHour());
        assertEquals(16, rec.getEndHour());
        assertEquals(BookingStatus.ENDED, rec.getStatus());

        // timeslot updated
        assertArrayEquals(new int[]{14, 16}, rec.getTimeslot());
    }

    @Test
    public void testToStringExactFormat() {
        SportFacilityType type = new SportFacilityType("Badminton court", "Badminton", 20);
        SportFacility facility = new SportFacility("Room Bad101", type, SportFacility.Status.AVAILABLE);
        User user = new User("charlie", 3, "pw", null);
        LocalDate date = LocalDate.of(2025, 12, 31);

        FacilityBookRecord rec = new FacilityBookRecord(facility, user, date, 18, 20, BookingStatus.CONFIRMED);

        String expected = "Booking confirmed for: " + facility.getName() + "\n" +
                          "Date: " + date + "\n" +
                          "Time: " + 18 + ":00 - " + 20 + ":00\n" ;
        assertEquals(expected, rec.toString());
    }
}
