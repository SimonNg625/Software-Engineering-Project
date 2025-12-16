package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.Clock;
import sportapp.Route;
import sportapp.User;
import sportapp.UserCollection;
import sportapp.UserSecurityAnswer;
import sportapp.manager.EquipmentBookManager;
import sportapp.manager.EquipmentManager;
import sportapp.manager.EquipmentTypeManager;
import sportapp.manager.FacilityBookManager;
import sportapp.model.BookingStatus;
import sportapp.model.Equipment;
import sportapp.model.EquipmentBookRecord;
import sportapp.model.EquipmentType;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;
import sportapp.model.SportFacilityType;
import sportapp.screen.BorrowEquipmentScreen;
import sportapp.ui.BorrowEquipmentUI;

public class TestBorrowEquipment {	
	private User user;
	private Scanner scanner;
	private ByteArrayInputStream inContent;

	private BorrowEquipmentScreen borrowEquipmentScreen;
	private BorrowEquipmentUI borrowEquipmentUI;
	
	private FacilityBookManager testFacilityBookManager;
	private EquipmentTypeManager testEquipmentTypeManager;
	private EquipmentManager testEquipmentManager;
	private EquipmentBookManager testEquipmentBookManager;

	private ArrayList<SportFacility> testFacilities;
	private ArrayList<FacilityBookRecord> testFacilityRecords;
	private ArrayList<EquipmentType> testTypes;
	private ArrayList<Equipment> testEquipments;
	
	@BeforeEach
	public void setUp() throws Exception {
		borrowEquipmentUI = new BorrowEquipmentUI(null, null);
		testEquipmentManager = EquipmentManager.getInstance();
		testEquipmentTypeManager = EquipmentTypeManager.getInstance();
		testEquipmentBookManager = EquipmentBookManager.getInstance();
		testFacilityBookManager = FacilityBookManager.getInstance();
		user = UserCollection.getInstance().addUser("admin", "password123", new UserSecurityAnswer("question", "answer"));
	}

	@AfterEach
	public void tearDown() throws Exception {
		testEquipmentTypeManager.reset();
		testEquipmentManager.reset();
		testEquipmentBookManager.reset();
		testFacilityBookManager.reset();
	}
	
	public void prepareFacilityRecord() {
		testFacilities = new ArrayList<>();
		testFacilityRecords = new ArrayList<>();
		
		testFacilities.add(new SportFacility("SF-001", new SportFacilityType("SFT-001", "Basketball", 30), SportFacility.Status.AVAILABLE));
		testFacilities.add(new SportFacility("SF-002", new SportFacilityType("SFT-002", "Badminton", 20), SportFacility.Status.AVAILABLE));
		testFacilities.add(new SportFacility("SF-003", new SportFacilityType("SFT-003", "TableTennis", 15), SportFacility.Status.AVAILABLE));

		for (SportFacility facility: testFacilities) {
			testFacilityRecords.add(new FacilityBookRecord(facility, user, Clock.getInstance().getToday().plusDays(1), 10, 12, BookingStatus.PENDING));
		}
		
		for (FacilityBookRecord record: testFacilityRecords) {			
			testFacilityBookManager.addBooking(record);
		}
	}

	public void prepareEquipmentType() {
		testTypes = new ArrayList<>();
		
		testTypes.add(new EquipmentType("ET-001", "Basketball Brand A", "BASKA", "Basketball", 15));
		testTypes.add(new EquipmentType("ET-003", "Badminton Racket Brand A", "BRACA", "Badminton", 10));
		testTypes.add(new EquipmentType("ET-005", "Table Tennis Racket Brand A", "TRACA", "TableTennis", 10));
		
		for (EquipmentType type: testTypes) {
			testEquipmentTypeManager.addBorrowableType(type);
		}
	}
	
	public void prepareEquipment() {
		int quantity = 5;
		for (EquipmentType type: testEquipmentTypeManager.getBorrowableTypes()) {
			for (int i=1; i<=quantity; i++) {
				testEquipmentManager.addEquipment(new Equipment(i, type));
			}
		}
	}
	
	public void prepareEquipmentRecord(FacilityBookRecord facilityRecord, int quantity) {
		String sportType = facilityRecord.getSportFacility().getSportFacilityType().getSportType();
		EquipmentType testEquipType = testEquipmentTypeManager.getBorrowableTypesBySportType(sportType).getFirst();
		ArrayList<Equipment> testEquips = testEquipmentManager.getBorrowableEquipmentByType(testEquipType);
		
		ArrayList<Equipment> targetEquips = new ArrayList<>();
		for (int i=0; i<quantity; i++) {
			targetEquips.add(testEquips.get(i));
		}
		
		testEquipmentBookManager.addBookRecord(new EquipmentBookRecord(facilityRecord, targetEquips, user));
	}
	
	public boolean matchEquipmentRecord(String TypeID, FacilityBookRecord facilityBookRecord, int quantity) {
		User user = facilityBookRecord.getUser();
		LocalDate date = facilityBookRecord.getDate();
		int startHour = facilityBookRecord.getStartHour();
		int endHour = facilityBookRecord.getEndHour();
		
		ArrayList<EquipmentBookRecord> records = testEquipmentBookManager.getBookingRecordsByUser(user);
		for (EquipmentBookRecord record: records) {
			if (record.getBookingEquipment().get(0).getEquipmentType().getEquipmentTypeID().equals(TypeID)
					&& record.getDate().equals(date)
					&& record.getStartHour() == startHour
					&& record.getEndHour() == endHour) {
					return true;
			}
		}
		return false;
	}
	
	
	@Test
	public void test_display_noEquipmentType() {
		prepareFacilityRecord();
		
		borrowEquipmentScreen = new BorrowEquipmentScreen(testFacilityBookManager.getBookingRecords().get(0));

		String input = "H\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.HOME;
        assertEquals(expected, borrowEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_haveEquipmentType_noAvailable() {
		prepareFacilityRecord();
		prepareEquipmentType();
		
		borrowEquipmentScreen = new BorrowEquipmentScreen(testFacilityBookManager.getBookingRecords().get(0));

		String input = "H\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.HOME;
        assertEquals(expected, borrowEquipmentScreen.display(scanner, user));
	}

	@Test
	public void test_display_inputEquipmentType_notB_notfound() {
		prepareFacilityRecord();
		prepareEquipmentType();
		prepareEquipment();
		
		borrowEquipmentScreen = new BorrowEquipmentScreen(testFacilityBookManager.getBookingRecords().get(0));

		String input = "et-1234\nB\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.BACK;
        assertEquals(expected, borrowEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_inputEquipmentType_notB_incorrectSportType() {
		prepareFacilityRecord();
		prepareEquipmentType();
		prepareEquipment();
		
		borrowEquipmentScreen = new BorrowEquipmentScreen(testFacilityBookManager.getBookingRecords().get(0));

		String input = "ET-003\nB\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.BACK;
        assertEquals(expected, borrowEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_inputEquipmentType_back() {
		prepareFacilityRecord();
		prepareEquipmentType();
		prepareEquipment();
		
		borrowEquipmentScreen = new BorrowEquipmentScreen(testFacilityBookManager.getBookingRecords().get(0));

		String input = "1\nb\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.BACK;
        assertEquals(expected, borrowEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_inputQuantity_belowZeroAndback() {
		prepareFacilityRecord();
		prepareEquipmentType();
		prepareEquipment();
		
		borrowEquipmentScreen = new BorrowEquipmentScreen(testFacilityBookManager.getBookingRecords().get(0));

		String input = "1\net-001\n-2\n0\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.BACK;
        assertEquals(expected, borrowEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_inputQuantity_aboveAvailSize_noRecord() {
		prepareFacilityRecord();
		prepareEquipmentType();
		prepareEquipment();
		
		borrowEquipmentScreen = new BorrowEquipmentScreen(testFacilityBookManager.getBookingRecords().get(0));

		String input = "1\nEt-001\n10\n0\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.BACK;
        assertEquals(expected, borrowEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_inputQuantity_aboveAvailSize_withRecord() {
		prepareFacilityRecord();
		
		FacilityBookRecord testFacilityBookRecord = testFacilityBookManager.getBookingRecords().get(0);
		borrowEquipmentScreen = new BorrowEquipmentScreen(testFacilityBookRecord);

		prepareEquipmentType();
		prepareEquipment();
		prepareEquipmentRecord(testFacilityBookRecord, 3);
		
		String input = "1\neT-001\n5\n0\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.BACK;
        assertEquals(expected, borrowEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_inputQuantity_notInteger() {
		prepareFacilityRecord();
		
		FacilityBookRecord testFacilityBookRecord = testFacilityBookManager.getBookingRecords().get(0);
		borrowEquipmentScreen = new BorrowEquipmentScreen(testFacilityBookRecord);

		prepareEquipmentType();
		prepareEquipment();
//		prepareEquipmentRecord(testFacilityBookRecord, 3);

		String input = "1\neT-001\nXYZ\n0\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.BACK;
        assertEquals(expected, borrowEquipmentScreen.display(scanner, user));
	}

	@Test
	public void test_display_borrowEquipment_back() {
		prepareFacilityRecord();
		
		FacilityBookRecord testFacilityBookRecord = testFacilityBookManager.getBookingRecords().get(0);
		borrowEquipmentScreen = new BorrowEquipmentScreen(testFacilityBookRecord);

		prepareEquipmentType();
		prepareEquipment();
//		prepareEquipmentRecord(testFacilityBookRecord, 3);

		String input = "1\nET-001\n3\nB\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.BACK;
        assertEquals(expected, borrowEquipmentScreen.display(scanner, user));
        assertEquals(true, matchEquipmentRecord("ET-001", testFacilityBookRecord, 3));
	}

	@Test
	public void test_display_borrowEquipment_home() {
		prepareFacilityRecord();
		
		FacilityBookRecord testFacilityBookRecord = testFacilityBookManager.getBookingRecords().get(0);
		borrowEquipmentScreen = new BorrowEquipmentScreen(testFacilityBookRecord);

		prepareEquipmentType();
		prepareEquipment();
//		prepareEquipmentRecord(testFacilityBookRecord, 3);

		String input = "1\nET-001\n3\nH\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.HOME;
        assertEquals(expected, borrowEquipmentScreen.display(scanner, user));
        assertEquals(true, matchEquipmentRecord("ET-001", testFacilityBookRecord, 3));
	}
}
