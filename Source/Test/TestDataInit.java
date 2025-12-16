package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.User;
import sportapp.UserCollection;
import sportapp.manager.EquipmentBookManager;
import sportapp.manager.FacilityBookManager;
import sportapp.EquipmentBookingControl;
import sportapp.FacilityBookingControl;
import sportapp.util.DataInit;

public class TestDataInit {
	EquipmentBookingControl testEquipmentBookingControl;
	EquipmentBookManager testEquipmentBookManager;
	FacilityBookingControl testFacilityBookingControl;
	FacilityBookManager testFacilityBookManager;
	User admin;
	
	@BeforeEach
	public void setUp() {
		testEquipmentBookingControl = new EquipmentBookingControl();
		testEquipmentBookManager = EquipmentBookManager.getInstance();
		testFacilityBookingControl = new FacilityBookingControl();
		testFacilityBookManager = FacilityBookManager.getInstance();
		admin = UserCollection.getInstance().findUserByName("admin");
	}
	
	@AfterEach
	public void tearDown() {
		testFacilityBookManager.reset();
		testEquipmentBookManager.reset();
	}

	@Test
	public void testLoadEquipmentRecords() {
		DataInit.initDefaultData("true");
		admin = UserCollection.getInstance().findUserByName("admin");
		assertEquals(6, testEquipmentBookingControl.getBookingRecordsByUser(admin).size());
		assertEquals(3, testEquipmentBookingControl.getPeningBookRecords(admin).size());
		assertEquals(3, testEquipmentBookingControl.getBookingRecordsByUser(admin).size() - testEquipmentBookingControl.getPeningBookRecords(admin).size());
	}
	
	@Test
	public void testLoadFacilityRecords() {
		DataInit.initDefaultData("true");
		admin = UserCollection.getInstance().findUserByName("admin");
		assertEquals(5, testFacilityBookManager.getUserBooking(admin).size() + testFacilityBookManager.getUserConfirmedBooking(admin).size());
		assertEquals(3, testFacilityBookManager.getUserBooking(admin).size());
		assertEquals(2, testFacilityBookManager.getUserConfirmedBooking(admin).size());
	}
}
