package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.EquipmentBookingControl;
import sportapp.manager.EquipmentBookManager;
import sportapp.manager.EquipmentManager;

public class TestEquipmentBookingControl {
	private EquipmentBookingControl testEquipmentBookingControl;
	private EquipmentBookManager testEquipmentBookManager;
	private EquipmentManager testEquipmentManager;
	
	@BeforeAll
	public static void setUpBeforeClass() {
		
	}

	@BeforeEach
	public void setUp() {
		testEquipmentBookingControl = new EquipmentBookingControl();
		testEquipmentBookManager = EquipmentBookManager.getInstance();
		testEquipmentManager = EquipmentManager.getInstance();
	}

	@AfterEach
	public void tearDown() {
		
	}

	@Test
	public void test() {
		
	}

}
