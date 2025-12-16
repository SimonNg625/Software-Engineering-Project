
// File: sportcentre-management-system-Merged/Sport_management_app/src/Test/TestFacilityBookingUI.java
package Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import sportapp.Route;
import sportapp.User;
import sportapp.ui.FacilityBookingUI;
import sportapp.manager.FacilityBookManager;
import sportapp.manager.SportFacilityManager;
import sportapp.model.SportFacility;
import sportapp.model.SportFacilityType;
import sportapp.model.BookingStatus;
import sportapp.model.FacilityBookRecord;

public class TestFacilityBookingUI {

    private SportFacilityManager facilityManager;
    private FacilityBookManager bookingManager;
    private SportFacility basketballCourt;
    private SportFacility badmintonCourt;
    private SportFacility tableTennisCourt;
    private User testUser;
    private LocalDate testValidDate;
    private DateTimeFormatter formatter;

    @BeforeEach
    void setUp() {
        facilityManager = SportFacilityManager.getInstance();
        bookingManager = FacilityBookManager.getInstance();

        // Clear existing data
        facilityManager.getSportFacilities().clear();
        bookingManager.getBookingRecords().clear();

        // Recreate facilities used by the UI
        SportFacilityType basketballType = new SportFacilityType("Basketball court", "Basketball", 30);
        SportFacilityType badmintonType = new SportFacilityType("Badminton court", "Badminton", 20);
        SportFacilityType ttType = new SportFacilityType("Table Tennis court", "TableTennis", 40);

        basketballCourt = new SportFacility("Room Bas201", basketballType, SportFacility.Status.AVAILABLE);
        badmintonCourt = new SportFacility("Room Bad101", badmintonType, SportFacility.Status.AVAILABLE);
        tableTennisCourt = new SportFacility("Room TT301", ttType, SportFacility.Status.AVAILABLE);

        facilityManager.addSportFacility(basketballCourt);
        facilityManager.addSportFacility(badmintonCourt);
        facilityManager.addSportFacility(tableTennisCourt);

        testUser = new User("tester", 1, "pw", null);
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        testValidDate = LocalDate.now().plusDays(1);
//        System.out.println((testValidDate.format(formatter));
    }

    // Helper to create scanner from the provided multiline input
    private Scanner createScanner(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        return new Scanner(in);
    }

    @Test
    void testSuccessfulBooking_ReturnsHomeAndAddsBooking() {
        // Choose facility 1, valid date, valid times, then 'n' to not continue booking
        String input = String.join("\n",
            "1",               // facility choice
            testValidDate.format(formatter),      // date (valid)
            "09",            // start hour
            "10",            // end hour
            "n"                // continueBooking -> no
        ) + "\n";

        Scanner scanner = createScanner(input);
        FacilityBookingUI ui = new FacilityBookingUI(scanner, testUser);

        Route route = ui.display(scanner, testUser);

        assertEquals(Route.HOME, route);
        assertEquals(1, bookingManager.getBookingRecords().size());

        FacilityBookRecord rec = bookingManager.getBookingRecords().get(0);
        assertEquals("Room Bas201", rec.getSportFacility().getName());
        assertEquals(testValidDate, rec.getDate());
        assertEquals(9, rec.getStartHour());
        assertEquals(10, rec.getEndHour());
    }

    @Test
    void testInvalidDateThenValidDate() {
        // First date invalid, then valid date, then valid time, then 'n'
        String input = String.join("\n",
            "1",
            "invalid-date",
            testValidDate.format(formatter),
            "09",
            "10",
            "n"
        ) + "\n";

        Scanner scanner = createScanner(input);
        FacilityBookingUI ui = new FacilityBookingUI(scanner, testUser);

        Route route = ui.display(scanner, testUser);

        assertEquals(Route.HOME, route);
        assertEquals(1, bookingManager.getBookingRecords().size());
        FacilityBookRecord rec = bookingManager.getBookingRecords().get(0);
        assertEquals(testValidDate, rec.getDate());
    }

    @Test
    void testInputMismatchDuringHourInput_RecoveredAndBooked() {
        // Provide non-integer for start hour (causes InputMismatchException), then valid ints
        String input = String.join("\n",
            "1",
            testValidDate.format(formatter),
            "abc",    // invalid integer -> InputMismatchException
            "09",   // valid start
            "10",   // valid end
            "n"
        ) + "\n";

        Scanner scanner = createScanner(input);
        FacilityBookingUI ui = new FacilityBookingUI(scanner, testUser);

        Route route = ui.display(scanner, testUser);

        assertEquals(Route.HOME, route);
        assertEquals(1, bookingManager.getBookingRecords().size());
        FacilityBookRecord rec = bookingManager.getBookingRecords().get(0);
        assertEquals(9, rec.getStartHour());
        assertEquals(10, rec.getEndHour());
    }

    @Test
    void testTimeOutsideBusinessHours_ThenValid() {
        // First attempt outside business hours (0800), then valid attempt
        String input = String.join("\n",
            "1",
            testValidDate.format(formatter),
            "08",   // start outside business hours -> error
            "10",   // end (read together)
            "09",   // next valid start
            "10",   // next valid end
            "n"
        ) + "\n";

        Scanner scanner = createScanner(input);
        FacilityBookingUI ui = new FacilityBookingUI(scanner, testUser);

        Route route = ui.display(scanner, testUser);

        assertEquals(Route.HOME, route);
        assertEquals(1, bookingManager.getBookingRecords().size());
        FacilityBookRecord rec = bookingManager.getBookingRecords().get(0);
        assertEquals(9, rec.getStartHour());
        assertEquals(10, rec.getEndHour());
    }

    @Test
    void testNonOnTheHourTimes_ThenValid() {
        // First attempt on half-hour (0930) -> error, then valid 0900-1000
        String input = String.join("\n",
            "1",
            testValidDate.format(formatter),
            "0930",
            "1030",
            "09",
            "10",
            "n"
        ) + "\n";

        Scanner scanner = createScanner(input);
        FacilityBookingUI ui = new FacilityBookingUI(scanner, testUser);

        Route route = ui.display(scanner, testUser);

        assertEquals(Route.HOME, route);
        assertEquals(1, bookingManager.getBookingRecords().size());
        FacilityBookRecord rec = bookingManager.getBookingRecords().get(0);
        assertEquals(9, rec.getStartHour());
        assertEquals(10, rec.getEndHour());
    }

    @Test
    void testStartNotBeforeEnd_ThenValid() {
        // First attempt start >= end (1400 >= 1300) -> error, then valid 0900-1000
        String input = String.join("\n",
            "1",
            testValidDate.format(formatter),
            "14",
            "13",
            "09",
            "10",
            "n"
        ) + "\n";

        Scanner scanner = createScanner(input);
        FacilityBookingUI ui = new FacilityBookingUI(scanner, testUser);

        Route route = ui.display(scanner, testUser);

        assertEquals(Route.HOME, route);
        assertEquals(1, bookingManager.getBookingRecords().size());
        FacilityBookRecord rec = bookingManager.getBookingRecords().get(0);
        assertEquals(9, rec.getStartHour());
        assertEquals(10, rec.getEndHour());
    }

    @Test
    void testRequestedSlotNotAvailableThenChooseAvailableSlot() {
        // Pre-add a confirmed booking 9-11 to make early slot unavailable
        bookingManager.addBooking(new FacilityBookRecord(basketballCourt, testUser, testValidDate, 9, 11, BookingStatus.CONFIRMED));

        // Attempt 0900-1000 (not available) then attempt 1100-1200 (available)
        String input = String.join("\n",
            "1",
            testValidDate.format(formatter),
            "09",
            "10",
            "11",
            "12",
            "n"
        ) + "\n";

        Scanner scanner = createScanner(input);
        FacilityBookingUI ui = new FacilityBookingUI(scanner, testUser);

        Route route = ui.display(scanner, testUser);

        assertEquals(Route.HOME, route);
        // one pre-existing + one new = 2 bookings total
        assertEquals(2, bookingManager.getBookingRecords().size());

        // The new booking should be the second entry and store 11-12
        FacilityBookRecord rec = bookingManager.getBookingRecords().get(1);
        assertEquals(11, rec.getStartHour());
        assertEquals(12, rec.getEndHour());
        assertEquals(testValidDate, rec.getDate());
    }

    @Test
    void testContinueBooking_YieldsFacilityBookingRoute() {
        // After successful booking, input 'y' to continue booking -> expect FACILITY_BOOKING
        String input = String.join("\n",
            "1",
            testValidDate.format(formatter),
            "09",
            "10",
            "y"
        ) + "\n";

        Scanner scanner = createScanner(input);
        FacilityBookingUI ui = new FacilityBookingUI(scanner, testUser);

        Route route = ui.display(scanner, testUser);

        assertEquals(Route.FACILITY_BOOKING, route);
        assertEquals(1, bookingManager.getBookingRecords().size());
    }
    
    @Test
    void testSelectBadmintonBooking_ReturnsHomeAndAddsBooking() {
        // Choose facility 2 (Badminton), valid date, valid times, then 'n'
        String input = String.join("\n",
            "2",               // facility choice -> Room Bad101
            testValidDate.format(formatter),      // date
            "09",            // start
            "10",            // end
            "n"
        ) + "\n";

        Scanner scanner = createScanner(input);
        FacilityBookingUI ui = new FacilityBookingUI(scanner, testUser);

        Route route = ui.display(scanner, testUser);

        assertEquals(Route.HOME, route);
        assertEquals(1, bookingManager.getBookingRecords().size());

        FacilityBookRecord rec = bookingManager.getBookingRecords().get(0);
        assertEquals("Room Bad101", rec.getSportFacility().getName());
        assertEquals(testValidDate, rec.getDate());
        assertEquals(9, rec.getStartHour());
        assertEquals(10, rec.getEndHour());
    }
    
    @Test
    void testSelectTableTennisBooking_ReturnsHomeAndAddsBooking() {
        // Choose facility 3 (Table Tennis), valid date, valid times, then 'n'
        String input = String.join("\n",
            "3",               // facility choice -> Room TT301
            testValidDate.format(formatter),      // date
            "11",            // start
            "12",            // end
            "n"
        ) + "\n";

        Scanner scanner = createScanner(input);
        FacilityBookingUI ui = new FacilityBookingUI(scanner, testUser);

        Route route = ui.display(scanner, testUser);

        assertEquals(Route.HOME, route);
        assertEquals(1, bookingManager.getBookingRecords().size());

        FacilityBookRecord rec = bookingManager.getBookingRecords().get(0);
        assertEquals("Room TT301", rec.getSportFacility().getName());
        assertEquals(testValidDate, rec.getDate());
        assertEquals(11, rec.getStartHour());
        assertEquals(12, rec.getEndHour());
    }
    
    @Test
    void testInvalidFacilityChoiceThenValidChoice() {
        // First provide an invalid facility choice, then a valid one (1)
        // NOTE: the UI logic is expected to allow re-trying the facility choice.
        String input = String.join("\n",
            "5",               // invalid facility choice (should trigger default branch)
            "1",               // then valid choice
            testValidDate.format(formatter),      // date
            "09",
            "10",
            "n"
        ) + "\n";

        Scanner scanner = createScanner(input);
        FacilityBookingUI ui = new FacilityBookingUI(scanner, testUser);

        Route route = ui.display(scanner, testUser);

        assertEquals(Route.HOME, route);
        assertEquals(1, bookingManager.getBookingRecords().size());

        FacilityBookRecord rec = bookingManager.getBookingRecords().get(0);
        // the final chosen facility should be the valid one '1'
        assertEquals("Room Bas201", rec.getSportFacility().getName());
        assertEquals(testValidDate, rec.getDate());
        assertEquals(9, rec.getStartHour());
        assertEquals(10, rec.getEndHour());
    }
}

