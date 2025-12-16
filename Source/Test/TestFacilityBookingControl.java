package Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.*;
import sportapp.manager.FacilityBookManager;
import sportapp.manager.SportFacilityManager;
import sportapp.model.BookingStatus;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;
import sportapp.model.SportFacilityType;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

public class TestFacilityBookingControl {

private FacilityBookingControl facilityBookingControl;
private SportFacilityManager facilityManager;
private FacilityBookManager facilityBookManager;

private SportFacility basketballCourt;
private SportFacility badmintonCourt;
private User testUser;
private LocalDate testDate;

// Helper to create a mock FacilityBookRecord
private FacilityBookRecord createBooking(int start, int end) {
         // Assuming a simple constructor or builder; adjust based on actual class
        return new FacilityBookRecord(null, null, null, start, end, null);
    }

     @Test
    public void testCalculateGaps_NullBookings() {
         FacilityBookingControl control = new FacilityBookingControl();
        ArrayList<int[]> result = control.calculateGapsBetweeenBookings(null, 9, 21);
        assertEquals(1, result.size());
         assertArrayEquals(new int[]{9, 21}, result.get(0));
     }

     @Test
     public void testCalculateGaps_EmptyBookings() {
         FacilityBookingControl control = new FacilityBookingControl();
         ArrayList<int[]> result = control.calculateGapsBetweeenBookings(new ArrayList<>(), 9, 21);
        assertEquals(1, result.size());
         assertArrayEquals(new int[]{9, 21}, result.get(0));
     }

    @Test
     public void testCalculateGaps_SingleBookingWithGaps() {
         FacilityBookingControl control = new FacilityBookingControl();
        ArrayList<FacilityBookRecord> bookings = new ArrayList<>();
         bookings.add(createBooking(12, 14));
        ArrayList<int[]> result = control.calculateGapsBetweeenBookings(bookings, 9, 21);
         assertEquals(2, result.size());
         System.out.println(result.get(0)[0] + ", " + result.get(0)[1]);
         System.out.println(result.get(1)[0] + ", " + result.get(1)[1]);
         assertArrayEquals(new int[]{9, 12}, result.get(0));
         assertArrayEquals(new int[]{14, 21}, result.get(1));
     }

     @Test
     public void testCalculateGaps_MultipleBookingsWithGaps() {
         FacilityBookingControl control = new FacilityBookingControl();
        ArrayList<FacilityBookRecord> bookings = new ArrayList<>();
         bookings.add(createBooking(10, 11));
         bookings.add(createBooking(13, 15));
         ArrayList<int[]> result = control.calculateGapsBetweeenBookings(bookings, 9, 21);
         assertEquals(3, result.size());
         assertArrayEquals(new int[]{9, 10}, result.get(0));
        assertArrayEquals(new int[]{11, 13}, result.get(1));
         assertArrayEquals(new int[]{15, 21}, result.get(2));
    }

     @Test
     public void testCalculateGaps_NoGaps() {
         FacilityBookingControl control = new FacilityBookingControl();
        ArrayList<FacilityBookRecord> bookings = new ArrayList<>();
         bookings.add(createBooking(9, 21));
         ArrayList<int[]> result = control.calculateGapsBetweeenBookings(bookings, 9, 21);
         assertTrue(result.isEmpty());
     }

     @Test
     public void testCalculateGaps_BookingsAtBoundaries() {
         FacilityBookingControl control = new FacilityBookingControl();
         ArrayList<FacilityBookRecord> bookings = new ArrayList<>();
         bookings.add(createBooking(9, 12));
         bookings.add(createBooking(20, 21));
         ArrayList<int[]> result = control.calculateGapsBetweeenBookings(bookings, 9, 21);
         assertEquals(1, result.size());
         assertArrayEquals(new int[]{12, 20}, result.get(0));
     }

     @Test
     public void testCalculateGaps_UnsortedBookings() {
         FacilityBookingControl control = new FacilityBookingControl();
         ArrayList<FacilityBookRecord> bookings = new ArrayList<>();
         bookings.add(createBooking(15, 17));
         bookings.add(createBooking(10, 12));
         ArrayList<int[]> result = control.calculateGapsBetweeenBookings(bookings, 9, 21);
         assertEquals(3, result.size());
         assertArrayEquals(new int[]{9, 10}, result.get(0));
         assertArrayEquals(new int[]{12, 15}, result.get(1));
         assertArrayEquals(new int[]{17, 21}, result.get(2));
     }
    
     // This method runs before each test to ensure a clean state
     @BeforeEach
     void setUp() {
        // We need to get the singleton instances
         facilityManager = SportFacilityManager.getInstance();
         facilityBookManager = FacilityBookManager.getInstance();
         facilityBookingControl = new FacilityBookingControl();

         // Clear any data from previous tests to prevent interference
         facilityManager.getSportFacilities().clear();
         facilityBookManager.getBookingRecords().clear();

         // Create and add fresh test data
         SportFacilityType basketballType = new SportFacilityType("Basketball court","Basketball", 30);
         SportFacilityType badmintonType = new SportFacilityType("Badminton court","Badminton", 20);
         basketballCourt = new SportFacility("Room Bas201", basketballType, SportFacility.Status.AVAILABLE);
         badmintonCourt = new SportFacility("Room Bad101", badmintonType, SportFacility.Status.AVAILABLE);
         facilityManager.addSportFacility(basketballCourt);
         facilityManager.addSportFacility(badmintonCourt);

         testUser = new User("testUser", 123, "password", null);
         testDate = LocalDate.of(2025, 10, 20);
     }

    
     // Helper method to quickly create bookings for tests
     private void addBooking(SportFacility facility, LocalDate date, int startTime, int endTime) {
         facilityBookManager.addBooking(new FacilityBookRecord(facility, testUser, date, startTime, endTime, BookingStatus.CONFIRMED));
     }

     // =================================================================
     // Tests for getAvailableTimeSlot()
     // =================================================================

     @Test
     void testGetAvailableTimeSlot_NoBookings_ShouldReturnFullDay() {
         ArrayList<int[]> slots = facilityBookingControl.getAvailableTimeSlot(basketballCourt, testDate);
         assertEquals(1, slots.size());
         assertArrayEquals(new int[]{9, 21}, slots.get(0));
     }

     @Test
     void testGetAvailableTimeSlot_OneBookingInMiddle_ShouldReturnTwoGaps() {
         addBooking(basketballCourt, testDate, 12, 14); // Book 12:00 - 14:00
         ArrayList<int[]> slots = facilityBookingControl.getAvailableTimeSlot(basketballCourt, testDate);
         assertEquals(2, slots.size());
         assertArrayEquals(new int[]{9, 12}, slots.get(0));
         assertArrayEquals(new int[]{14, 21}, slots.get(1));
     }

     @Test
     void testGetAvailableTimeSlot_BookingAtStart_ShouldReturnOneGap() {
         addBooking(basketballCourt, testDate, 9, 11); // Book 9:00 - 11:00
         ArrayList<int[]> slots = facilityBookingControl.getAvailableTimeSlot(basketballCourt, testDate);
         assertEquals(1, slots.size());
         assertArrayEquals(new int[]{11, 21}, slots.get(0));
     }

     @Test
     void testGetAvailableTimeSlot_BookingAtEnd_ShouldReturnOneGap() {
         addBooking(basketballCourt, testDate, 18, 21); // Book 18:00 - 21:00
         ArrayList<int[]> slots = facilityBookingControl.getAvailableTimeSlot(basketballCourt, testDate);
         assertEquals(1, slots.size());
         assertArrayEquals(new int[]{9, 18}, slots.get(0));
     }

     @Test
     void testGetAvailableTimeSlot_MultipleBookings_ShouldReturnAllGaps() {
         addBooking(basketballCourt, testDate, 10, 11);
         addBooking(basketballCourt, testDate, 14, 16);
         addBooking(basketballCourt, testDate, 18, 19);
         ArrayList<int[]> slots = facilityBookingControl.getAvailableTimeSlot(basketballCourt, testDate);
         assertEquals(4, slots.size());
         assertArrayEquals(new int[]{9, 10}, slots.get(0));
         assertArrayEquals(new int[]{11, 14}, slots.get(1));
         assertArrayEquals(new int[]{16, 18}, slots.get(2));
         assertArrayEquals(new int[]{19, 21}, slots.get(3));
     }

     @Test
    void testGetAvailableTimeSlot_FullyBooked_ShouldReturnEmptyList() {
         addBooking(basketballCourt, testDate, 9, 21);
         ArrayList<int[]> slots = facilityBookingControl.getAvailableTimeSlot(basketballCourt, testDate);
         assertTrue(slots.isEmpty());
     }

     @Test
     void testGetAvailableTimeSlot_UnsortedBookings_ShouldSortAndReturnCorrectGaps() {
         // Add bookings in a non-chronological order
         addBooking(basketballCourt, testDate, 15, 17);
         addBooking(basketballCourt, testDate, 10, 12);
         ArrayList<int[]> slots = facilityBookingControl.getAvailableTimeSlot(basketballCourt, testDate);
         assertEquals(3, slots.size());
         assertArrayEquals(new int[]{9, 10}, slots.get(0)); // Should be first gap
         assertArrayEquals(new int[]{12, 15}, slots.get(1)); // Should be second gap
         assertArrayEquals(new int[]{17, 21}, slots.get(2)); // Should be third gap
     }

     // =================================================================
     // Tests for getFacilityBookRecordsByDate()
     // =================================================================

     @Test
     void testGetFacilityBookRecordsByDate_ShouldReturnCorrectRecords() {
         addBooking(basketballCourt, testDate, 10, 12);
         addBooking(basketballCourt, testDate, 14, 16);
         addBooking(badmintonCourt, testDate, 10, 12); // Booking for another facility
         ArrayList<FacilityBookRecord> records = facilityBookingControl.getFacilityBookRecordsByDate(basketballCourt, testDate);
         assertEquals(2, records.size());
     }

     @Test
     void testGetFacilityBookRecordsByDate_NoMatchingRecords_ShouldReturnEmptyList() {
         addBooking(badmintonCourt, testDate, 10, 12);
         ArrayList<FacilityBookRecord> records = facilityBookingControl.getFacilityBookRecordsByDate(basketballCourt, testDate);
         assertTrue(records.isEmpty());
     }
}

//     @Test
//    void testGetFacilityBookRecordsByDate_NullFacility_ShouldThrowException() {
//         assertThrows(IllegalArgumentException.class, () -> {
//             facilityBookingControl.getFacilityBookRecordsByDate(null, testDate);
//         });
//     }
    

     // =================================================================
     // Tests for updateBookingDateTime()
     // =================================================================

    
    
//     @Test
//     void testUpdateBookingDateTime_InvalidTime_ShouldThrowException() {
//         addBooking(basketballCourt, testDate, 10, 12);
//         FacilityBookRecord originalRecord = facilityBookManager.getBookingRecords().get(0);
//
//         // End time is before start time
//        assertThrows(IllegalArgumentException.class, () -> {
//            facilityBookingControl.updateBookingDateTime(originalRecord, testDate, 14, 13);
//         });
//
//         // Time is outside business hours
//         assertThrows(IllegalArgumentException.class, () -> {
//             facilityBookingControl.updateBookingDateTime(originalRecord, testDate, 8, 10);
//         });
//         assertThrows(IllegalArgumentException.class, () -> {
//             facilityBookingControl.updateBookingDateTime(originalRecord, testDate, 20, 22);
//         });
//     }
// }
