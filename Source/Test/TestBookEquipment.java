package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.Clock;
import sportapp.EquipmentBookingControl;
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
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;
import sportapp.model.SportFacilityType;
import sportapp.screen.BookEquipmentScreen;
import sportapp.ui.BookEquipmentUI;

public class TestBookEquipment {
	
	private User user;
	private Scanner scanner;
	private ByteArrayInputStream inContent;
	
	private BookEquipmentUI bookEquipmentUI;
	private BookEquipmentScreen bookEquipmentScreen;
	private StubBookEquipmentScreen stubBookEquipmentScreen;
	
//	private EquipmentManager testEquipmentManager;
//	private EquipmentTypeManager testEquipmentTypeManager;
//	private EquipmentBookManager testEquipmentBookManager;
	private FacilityBookManager testFacilityBookManager;
	
	
	public class StubBookEquipmentScreen extends BookEquipmentScreen {
		@Override
		protected Route forwardRoute() {
			Route forwarder = null;
			forwarder = bookEquipmentUI.selectBookEquipmentFunction();
			return forwarder;
		}
	}
	
	@BeforeEach
	public void setUp() {
		bookEquipmentScreen = new BookEquipmentScreen();
		stubBookEquipmentScreen = new StubBookEquipmentScreen();
//		testEquipmentManager = EquipmentManager.getInstance();
		testFacilityBookManager = FacilityBookManager.getInstance();
		user = UserCollection.getInstance().addUser("admin", "password123", new UserSecurityAnswer("question", "answer"));
	}
	
	@AfterEach
	public void tearDown() throws Exception {
//		testEquipmentManager.reset();
		testFacilityBookManager.reset();
	}
	
	public void prepareFacilityRecord() {
		ArrayList<SportFacility> testFacilities = new ArrayList<>();
		ArrayList<FacilityBookRecord> testFacilityRecords = new ArrayList<>();
		
		testFacilities.add(new SportFacility("SF-001", new SportFacilityType("SFT-001", "Basketball", 30), SportFacility.Status.AVAILABLE));
//		testFacilities.add(new SportFacility("SF-002", new SportFacilityType("SFT-002", "Badminton", 20), SportFacility.Status.AVAILABLE));
//		testFacilities.add(new SportFacility("SF-003", new SportFacilityType("SFT-003", "TableTennis", 15), SportFacility.Status.AVAILABLE));

		for (SportFacility facility: testFacilities) {
			testFacilityRecords.add(new FacilityBookRecord(facility, user, Clock.getInstance().getToday().plusDays(1), 10, 12, BookingStatus.PENDING));
		}
		
		for (FacilityBookRecord record: testFacilityRecords) {			
			testFacilityBookManager.addBooking(record);
		}
	}
	
	public void prepareAllFacilityRecord() {
		ArrayList<SportFacility> testFacilities = new ArrayList<>();
		ArrayList<FacilityBookRecord> testFacilityRecords = new ArrayList<>();
		
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
	
	@Test
	public void test_display_selectFacilityRecord_noRecord() {
		String input = "";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.HOME;
        assertEquals(expected, bookEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_selectFacilityRecord__oneRecord_back() {
		prepareFacilityRecord();
		
		String input = "0\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.HOME;
        assertEquals(expected, bookEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_selectFacilityRecord_back() {
		prepareAllFacilityRecord();
		
		String input = "0\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.HOME;
        assertEquals(expected, bookEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_selectFacilityRecord_belowZero() {
		prepareAllFacilityRecord();
		
		String input = "-2\n1\nH\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.HOME;
        assertEquals(expected, bookEquipmentScreen.display(scanner, user));
	}

	@Test
	public void test_display_selectFacilityRecord_aboveSize() {
		prepareAllFacilityRecord();
		
		String input = testFacilityBookManager.getBookingRecords().size()+2 + "\n1\nH\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.HOME;
        assertEquals(expected, bookEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_selectFacilityRecord_notInteger() {
		prepareAllFacilityRecord();
		
		String input = "XYZ\n1\nH\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        
        Route expected = Route.HOME;
        assertEquals(expected, bookEquipmentScreen.display(scanner, user));
	}

	
	
	@Test
	public void test_display_Borrow() {
		prepareAllFacilityRecord();
		
		String input = "1\n1\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        bookEquipmentUI = new BookEquipmentUI(scanner, user);
        
        Route expected = Route.BORROW_EQUIPMENT;
        assertEquals(expected, stubBookEquipmentScreen.display(scanner, user));
	}

	
	@Test
	public void test_display_Sell() {
		prepareAllFacilityRecord();
		
		String input = "1\n2\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        bookEquipmentUI = new BookEquipmentUI(scanner, user);
        
        Route expected = Route.SELL_EQUIPMENT;
        assertEquals(expected, stubBookEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_Home() {
		prepareAllFacilityRecord();
		
		String input = "1\nH\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        bookEquipmentUI = new BookEquipmentUI(scanner, user);
        
        Route expected = Route.HOME;
        assertEquals(expected, stubBookEquipmentScreen.display(scanner, user));
	}
	
	@Test
	public void test_display_ErrorMessage() {
		prepareAllFacilityRecord();
		
		String input = "1\nW\nH\n";
		inContent = new ByteArrayInputStream(input.getBytes());
        scanner = new Scanner(inContent);
        bookEquipmentUI = new BookEquipmentUI(scanner, user);
        
        Route expected = Route.HOME;
        assertEquals(expected, stubBookEquipmentScreen.display(scanner, user));
	}

	
}
