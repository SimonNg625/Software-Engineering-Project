package Test;

import org.junit.jupiter.api.*;

import sportapp.User;
import sportapp.UserCollection;
import sportapp.UserSecurityAnswer;
import sportapp.ViewCurrentBookingControl;
import sportapp.manager.*;
import sportapp.model.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ViewCurrentBookingControllerTest {
    
    private ViewCurrentBookingControl controller;
    private User testUser;
    private SportFacility testFacility;
    private Equipment testEquipment;
    private SportFacilityType testFacilityType;
    private FacilityBookRecord testFacilityBooking;
    private EquipmentBookRecord testEquipmentBooking;
    private LocalDate testDate;
    
    @BeforeEach
    void setUp() {
    	// Clear all data
        UserCollection.getInstance().clear();
        FacilityBookManager.getInstance().getBookingRecords().clear();
        SportFacilityManager.getInstance().getSportFacilities().clear();
        
        // Create test data
        UserSecurityAnswer securityAnswer = new UserSecurityAnswer("What is your pet's name?", "Fluffy");
        testUser = new User("testUser", 1, "password123", securityAnswer);
        UserCollection.getInstance().addUser(testUser);
        
        testFacilityType = new SportFacilityType("Basketball", "BT-001", 10);
        testFacility = new SportFacility("Basketball Court A", testFacilityType, SportFacility.Status.AVAILABLE);
        SportFacilityManager.getInstance().addSportFacility(testFacility);
        
        testDate = LocalDate.now().plusDays(1);
        testFacilityBooking = new FacilityBookRecord(testFacility, testUser, testDate, 10, 12, BookingStatus.PENDING);
        testEquipmentBooking = new EquipmentBookRecord(testEquipment, testUser, testDate, 10, 12, BookingStatus.PENDING, 5);
        controller = new ViewCurrentBookingControl();
        

        
    }
    
    @Test
    @DisplayName("Test getUserBookings - returns user bookings")
    void testGetUserBookings_Success() {
        // Arrange
        FacilityBookManager.getInstance().addBooking(testFacilityBooking);
        
        // Act
        ArrayList<FacilityBookRecord> result = controller.getUserFacilityBookings(testUser);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFacilityBooking, result.get(0));
    }
    
    @Test
    @DisplayName("Test getUserBookings - returns empty list")
    void testGetUserBookings_EmptyList() {
        // Act (no bookings added)
        ArrayList<FacilityBookRecord> result = controller.getUserFacilityBookings(testUser);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    @DisplayName("Test getUserEquipmentBookings")
    void testGetUserEquipmentBookings() {
        // Act
    	EquipmentBookManager.getInstance().addBookRecord(testEquipmentBooking);
    	
    	ArrayList<EquipmentBookRecord> testRecords = controller.getUserEquipmentBookings(testUser);
        // Assert
    	assertNotNull(testRecords);
    	assertTrue(testRecords.contains(testEquipmentBooking));
    }
    
    @Test
    @DisplayName("Test getAvailableFacilities - returns facilities")
    void testGetAvailableFacilities() {
        // Act
        ArrayList<SportFacility> result = controller.getAvailableFacilities();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFacility, result.get(0));
    }
    
    
    
    @Test
    @DisplayName("Test getAvailableTimeSlots - returns time slots")
    void testGetFacilityAvailableTimeSlots() {
    	FacilityBookManager.getInstance().addBooking(new FacilityBookRecord(testFacility, testUser, testDate, 9, 11, BookingStatus.PENDING));
        FacilityBookManager.getInstance().addBooking(new FacilityBookRecord(testFacility, testUser, testDate, 11, 13, BookingStatus.PENDING));
        FacilityBookManager.getInstance().addBooking(new FacilityBookRecord(testFacility, testUser, testDate, 13, 15, BookingStatus.PENDING));

        ArrayList<int[]> result = controller.getAvailableTimeSlots(testFacility, testDate);

        assertNotNull(result);
        // 15 - 21 is the only gap
        assertEquals(1, result.size());
        assertArrayEquals(new int[]{15, 21}, result.get(0));
    }
    
    @Test
    @DisplayName("Test getAvailableTimeSlots - returns availabe time slots")
    void testGetEquipmentAvailableTimeSlots() {
        // Arrange
        EquipmentManager equipmentManager = EquipmentManager.getInstance();
        EquipmentBookManager bookingManager = EquipmentBookManager.getInstance();
        EquipmentTypeManager typeManager = EquipmentTypeManager.getInstance();

        controller = new ViewCurrentBookingControl();

        EquipmentType testType = new EquipmentType("ET-TEST", "Test Basketball", "TBB", "Basketball", 10);
        testType.setCategory(EquipmentCategory.BORROWABLE);
        typeManager.addBorrowableType(testType);

        Equipment e1 = new Equipment(1, testType);
        Equipment e2 = new Equipment(2, testType);
        Equipment e3 = new Equipment(3, testType);
        equipmentManager.addEquipment(e1);
        equipmentManager.addEquipment(e2);
        equipmentManager.addEquipment(e3);

        EquipmentBookRecord bookedRecord = new EquipmentBookRecord(e1, testUser, testDate, 10, 12, BookingStatus.CONFIRMED, 1);
        bookingManager.addBookRecord(bookedRecord);

        ArrayList<Equipment> equipmentRequest = new ArrayList<>();
        equipmentRequest.add(e1); // reference for type
        equipmentRequest.add(e2);

        ArrayList<int[]> availableSlots = controller.getAvailableTimeSlots(equipmentRequest, testDate);

        assertNotNull(availableSlots);
        assertFalse(availableSlots.isEmpty());
    }
    
    @Test
    @DisplayName("Test getAvailableTimeSlots - should return empty when all equipment are booked")
    void testGetEquipmentAvailableTimeSlots_Empty() {
        // Arrange
        EquipmentManager equipmentManager = EquipmentManager.getInstance();
        EquipmentBookManager bookingManager = EquipmentBookManager.getInstance();
        EquipmentTypeManager typeManager = EquipmentTypeManager.getInstance();

        controller = new ViewCurrentBookingControl();

        EquipmentType testType = new EquipmentType("ET-TEST", "Test Ball", "TBB", "Basketball", 10);
        testType.setCategory(EquipmentCategory.BORROWABLE);
        typeManager.addBorrowableType(testType);

        // Create 2 equipments
        Equipment e1 = new Equipment(1, testType);
        Equipment e2 = new Equipment(2, testType);
        equipmentManager.addEquipment(e1);
        equipmentManager.addEquipment(e2);

        // Book both equipments fully (9–21)
        EquipmentBookRecord booking1 = new EquipmentBookRecord(e1, testUser, testDate, 9, 21, BookingStatus.CONFIRMED, 1);
        EquipmentBookRecord booking2 = new EquipmentBookRecord(e2, testUser, testDate, 9, 21, BookingStatus.CONFIRMED, 1);
        bookingManager.addBookRecord(booking1);
        bookingManager.addBookRecord(booking2);

        // Prepare request needing 2 units
        ArrayList<Equipment> request = new ArrayList<>();
        request.add(e1);
        request.add(e2);

        // Act
        ArrayList<int[]> availableSlots = controller.getAvailableTimeSlots(request, testDate);

        // Assert
        assertNotNull(availableSlots, "Available slots list should not be null");
        assertTrue(availableSlots.isEmpty(), "Expected no available time slots when all equipment are booked");
    }
    
    @Test
    @DisplayName("Test updateBookingDateTime - success")
    void testUpdateBookingDateTime_Success() {
        // Arrange
        FacilityBookManager.getInstance().addBooking(testFacilityBooking);
        
        // Act
        boolean result = controller.updateBookingDateTime(testFacilityBooking, testDate, 14, 16);
        
        // Assert
        assertTrue(result);
        assertEquals(14, testFacilityBooking.getStartHour());
        assertEquals(16, testFacilityBooking.getEndHour());
    }
    
    @Test
    @DisplayName("Test updateBookingDateTime - failure")
    void testUpdateBookingDateTime_Failure() {
        // Arrange
        FacilityBookManager.getInstance().addBooking(testFacilityBooking);
        
        // Act - invalid time (after 21:00)
        boolean result = controller.updateBookingDateTime(testFacilityBooking, testDate, 22, 23);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Test updateBookingFacility - success")
    void testUpdateBookingFacility_Success() {
        // Arrange
        SportFacility newFacility = new SportFacility("Tennis Court", testFacilityType, SportFacility.Status.AVAILABLE);
        SportFacilityManager.getInstance().addSportFacility(newFacility);
        FacilityBookManager.getInstance().addBooking(testFacilityBooking);
        
        String originalFacilityName = testFacilityBooking.getSportFacility().getName();

        // Use a different non-conflicting time slot
        boolean result = controller.updateBookingFacility(testFacilityBooking, newFacility, testDate, 14, 16);
        
        // Assert
        assertTrue(result);
//        assertNotEquals(originalFacilityName, testBooking.getSportFacility().getName());
//        assertEquals("Tennis Court", testBooking.getSportFacility().getName());
//        assertEquals(14, testBooking.getStartHour());
//        assertEquals(16, testBooking.getEndHour());
    }
    
    @Test
    @DisplayName("Test updateBookingFacility - failure on facility update")
    void testUpdateBookingFacility_FailureOnFacility() {
        // Arrange
        SportFacility newFacility = new SportFacility("Tennis Court", testFacilityType, SportFacility.Status.UNDER_MAINTENANCE);
        FacilityBookManager.getInstance().addBooking(testFacilityBooking);

        // Act
        boolean result = controller.updateBookingFacility(testFacilityBooking, newFacility, testDate, 10, 12);

        // Assert
        assertFalse(result); 
    }
    
    @Test
    @DisplayName("Test updateBookingFacility - failure on datetime update")
    void testUpdateBookingFacility_FailureOnDateTime() {
        // Arrange
        SportFacility newFacility = new SportFacility("Tennis Court", testFacilityType, SportFacility.Status.AVAILABLE);
        SportFacilityManager.getInstance().addSportFacility(newFacility);
        FacilityBookManager.getInstance().addBooking(testFacilityBooking);
        
        // Act - invalid time
        boolean result = controller.updateBookingFacility(testFacilityBooking, newFacility, testDate, 22, 23);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Test cancelFacilityBooking - success")
    void testCancelFacalityBooking() {
        // Arrange
        FacilityBookManager.getInstance().addBooking(testFacilityBooking);
        
        // Act
        controller.cancelBooking(testFacilityBooking);
        
        // Assert
        assertFalse(FacilityBookManager.getInstance().isBookingExist(testFacilityBooking));
    }
    
    @Test
    @DisplayName("Test cancelEquipmentBooking - success")
    void testCancelEquipmentBooking() {
        // Arrange
    	EquipmentBookManager.getInstance().addBookRecord(testEquipmentBooking);
    	//Check if successfully add the booking record
    	assertTrue(EquipmentBookManager.getInstance().getBookRecords().contains(testEquipmentBooking));
        // Act
        controller.cancelBooking(testEquipmentBooking);
        
        // Assert
        assertFalse(EquipmentBookManager.getInstance().getBookRecords().contains(testEquipmentBooking));
    }
    
    @Test
    @DisplayName("Test isValidTimeSlot - valid slot within range")
    void testIsValidTimeSlot_Valid() {
        // Act
        boolean result = controller.isValidTimeSlot(testFacility, testDate, 10, 12);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    @DisplayName("Test isValidTimeSlot - start time too early")
    void testIsValidTimeSlot_StartTimeTooEarly() {
        // Act
        boolean result = controller.isValidTimeSlot(testFacility, testDate, 8, 10);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Test isValidTimeSlot - end time too late")
    void testIsValidTimeSlot_EndTimeTooLate() {
        // Act
        boolean result = controller.isValidTimeSlot(testFacility, testDate, 20, 22);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Test isValidTimeSlot - start time equals end time")
    void testIsValidTimeSlot_StartEqualsEnd() {
        // Act
        boolean result = controller.isValidTimeSlot(testFacility, testDate, 10, 10);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Test isValidTimeSlot - start time after end time")
    void testIsValidTimeSlot_StartAfterEnd() {
        // Act
        boolean result = controller.isValidTimeSlot(testFacility, testDate, 15, 10);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Test isValidTimeSlot - not within available slots")
    void testIsValidTimeSlot_NotInAvailableSlots() {
        // Arrange - book entire day except 9-11
        FacilityBookRecord booking = new FacilityBookRecord(testFacility, testUser, testDate, 11, 21, BookingStatus.PENDING);
        FacilityBookManager.getInstance().addBooking(booking);
        
        // Act - try to book 12-15 (not available)
        boolean result = controller.isValidTimeSlot(testFacility, testDate, 12, 15);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Test isValidTimeSlot - boundary case start at 9")
    void testIsValidTimeSlot_BoundaryStart() {
        // Act
        boolean result = controller.isValidTimeSlot(testFacility, testDate, 9, 10);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    @DisplayName("Test isValidTimeSlot - boundary case end at 21")
    void testIsValidTimeSlot_BoundaryEnd() {
        // Act
        boolean result = controller.isValidTimeSlot(testFacility, testDate, 20, 21);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    @DisplayName("Test isValidTimeSlot - with existing booking")
    void testIsValidTimeSlot_WithExistingBooking() {
        // Arrange - book 12-14
        FacilityBookRecord booking = new FacilityBookRecord(testFacility, testUser, testDate, 12, 14, BookingStatus.PENDING);
        FacilityBookManager.getInstance().addBooking(booking);
        
        // Act - try to book 10-11 (should be available)
        boolean result = controller.isValidTimeSlot(testFacility, testDate, 10, 11);
        
        // Assert
        assertTrue(result);
    }
    

    
    @Test
    @DisplayName("Test getUserBookings - multiple bookings")
    void testGetUserBookings_MultipleBookings() {
        // Arrange
        FacilityBookRecord booking1 = new FacilityBookRecord(testFacility, testUser, testDate, 10, 12, BookingStatus.PENDING);
        FacilityBookRecord booking2 = new FacilityBookRecord(testFacility, testUser, testDate.plusDays(1), 14, 16, BookingStatus.PENDING);
        FacilityBookManager.getInstance().addBooking(booking1);
        FacilityBookManager.getInstance().addBooking(booking2);
        
        // Act
        ArrayList<FacilityBookRecord> result = controller.getUserFacilityBookings(testUser);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    
    @Test
    @DisplayName("Test getAvailableTimeSlots - no bookings")
    void testGetAvailableTimeSlots_NoBookings() {
        // Act
        ArrayList<int[]> result = controller.getAvailableTimeSlots(testFacility, testDate);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(9, result.get(0)[0]);
        assertEquals(21, result.get(0)[1]);
    }
    
    @Test
    @DisplayName("Test getPendingFacilityBookingRecord - returns only pending bookings for user")
    void testGetPendingFacilityBookingRecord() {
        // Arrange: add one pending and one confirmed booking for testUser
        FacilityBookRecord pending = new FacilityBookRecord(testFacility, testUser, testDate, 9, 10, BookingStatus.PENDING);
        FacilityBookRecord confirmed = new FacilityBookRecord(testFacility, testUser, testDate.plusDays(1), 11, 12, BookingStatus.CONFIRMED);
        FacilityBookManager.getInstance().addBooking(pending);
        FacilityBookManager.getInstance().addBooking(confirmed);

        // Act
        ArrayList<FacilityBookRecord> pendingList = controller.getPendingFacilityBookingRecord(testUser);

        // Assert
        assertNotNull(pendingList);
        assertEquals(1, pendingList.size());
        assertEquals(BookingStatus.PENDING, pendingList.get(0).getStatus());
        assertEquals(pending, pendingList.get(0));
    }

    @Test
    @DisplayName("Test getPendingEquipmentBookingRecord - returns only pending equipment bookings for user")
    void testGetPendingEquipmentBookingRecord() {
        // Arrange: create an equipment type and equipment to avoid null references
        EquipmentType et = new EquipmentType("ET-TEST-2", "Test", "T", "Cat", 5.0);
        et.setCategory(EquipmentCategory.BORROWABLE);
        Equipment e1 = new Equipment(101, et);
        EquipmentManager.getInstance().addEquipment(e1);

        // create pending and confirmed equipment bookings
        EquipmentBookRecord pendingEq = new EquipmentBookRecord(e1, testUser, testDate, 9, 10, BookingStatus.PENDING, 1);
        EquipmentBookRecord confirmedEq = new EquipmentBookRecord(e1, testUser, testDate.plusDays(1), 10, 11, BookingStatus.CONFIRMED, 1);
        EquipmentBookManager.getInstance().addBookRecord(pendingEq);
        EquipmentBookManager.getInstance().addBookRecord(confirmedEq);

        // Act
        ArrayList<EquipmentBookRecord> pendingEquipment = controller.getPendingEquipmentBookingRecord(testUser);
        ArrayList<EquipmentBookRecord> pendingEquipmentViaMethod = controller.getPendingEquipmentBookingRecord(testUser);

        // Assert: both controller methods should show only the pending booking
        assertNotNull(pendingEquipment);
        assertTrue(pendingEquipment.contains(pendingEq));
        assertFalse(pendingEquipment.contains(confirmedEq));

        assertNotNull(pendingEquipmentViaMethod);
        assertEquals(1, pendingEquipmentViaMethod.size());
        assertEquals(BookingStatus.PENDING, pendingEquipmentViaMethod.get(0).getStatus());
        assertEquals(pendingEq, pendingEquipmentViaMethod.get(0));
    }
    
    @Test
    @DisplayName("Test updateBookingDateTime for Equipment - success (borrowable)")
    void testUpdateBookingDateTime_Equipment_SuccessBorrowable() {
        // Clear equipment-related managers
        EquipmentManager.getInstance().getBorrowableCollection().clear();
        EquipmentManager.getInstance().getSellableCollection().clear();
        EquipmentBookManager.getInstance().getBookRecords().clear();
        EquipmentTypeManager.getInstance().getBorrowableTypes().clear();
        EquipmentTypeManager.getInstance().getSellableTypes().clear();

        // Arrange: create a borrowable equipment type and two equipment items
        EquipmentType type = new EquipmentType("ET-SUCC", "Success Equip", "EQ", "GEN", 5.0);
        EquipmentTypeManager.getInstance().addBorrowableType(type);
        Equipment e1 = new Equipment(201, type);
        Equipment e2 = new Equipment(202, type);
        EquipmentManager.getInstance().addEquipment(e1);
        EquipmentManager.getInstance().addEquipment(e2);

        // booking initially references e1 and quantity 1
        EquipmentBookRecord booking = new EquipmentBookRecord(e1, testUser, testDate, 9, 10, BookingStatus.PENDING, 1);

        // Act: try to update to a future date and valid time
        boolean result = controller.updateBookingDateTime(booking, testDate.plusDays(2), 11, 13);

        // Assert
        assertTrue(result, "Expected updateBookingDateTime to succeed when enough equipments are available");
        assertEquals(11, booking.getStartHour());
        assertEquals(13, booking.getEndHour());
    }

    @Test
    @DisplayName("Test updateBookingDateTime for Equipment - failure invalid time")
    void testUpdateBookingDateTime_Equipment_InvalidTime() {
        // Clear equipment managers
        EquipmentManager.getInstance().getBorrowableCollection().clear();
        EquipmentManager.getInstance().getSellableCollection().clear();
        EquipmentBookManager.getInstance().getBookRecords().clear();
        EquipmentTypeManager.getInstance().getBorrowableTypes().clear();
        EquipmentTypeManager.getInstance().getSellableTypes().clear();

        // Arrange
        EquipmentType type = new EquipmentType("ET-INV", "InvalidTimeEquip", "EQ", "GEN", 5.0);
        EquipmentTypeManager.getInstance().addBorrowableType(type);
        Equipment e1 = new Equipment(301, type);
        EquipmentManager.getInstance().addEquipment(e1);
        EquipmentBookRecord booking = new EquipmentBookRecord(e1, testUser, testDate, 9, 10, BookingStatus.PENDING, 1);

        // Act - invalid start time (before 9)
        boolean result = controller.updateBookingDateTime(booking, testDate.plusDays(2), 8, 10);

        // Assert
        assertFalse(result, "Expected updateBookingDateTime to fail for invalid time slot");
    }

    @Test
    @DisplayName("Test updateBookingDateTime for Equipment - failure insufficient equipments")
    void testUpdateBookingDateTime_Equipment_Insufficient() {
        // Clear equipment managers
        EquipmentManager.getInstance().getBorrowableCollection().clear();
        EquipmentManager.getInstance().getSellableCollection().clear();
        EquipmentBookManager.getInstance().getBookRecords().clear();
        EquipmentTypeManager.getInstance().getBorrowableTypes().clear();
        EquipmentTypeManager.getInstance().getSellableTypes().clear();

        // Arrange: only one equipment of given type exists
        EquipmentType type = new EquipmentType("ET-ONE", "SingleEquip", "EQ", "GEN", 5.0);
        EquipmentTypeManager.getInstance().addBorrowableType(type);
        Equipment e1 = new Equipment(401, type);
        EquipmentManager.getInstance().addEquipment(e1);

        // Create a booking that requires quantity 2
        EquipmentBookRecord booking = new EquipmentBookRecord(e1, testUser, testDate, 9, 10, BookingStatus.PENDING, 2);

        // Act: attempt to update
        boolean result = controller.updateBookingDateTime(booking, testDate.plusDays(2), 11, 12);

        // Assert - should fail because only 1 equipment available but quantity requested is 2
        assertFalse(result, "Expected updateBookingDateTime to fail when insufficient equipments are available");
    }
}
