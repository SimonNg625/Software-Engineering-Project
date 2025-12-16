package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.manager.EquipmentTypeManager;
import sportapp.model.EquipmentCategory;
import sportapp.model.EquipmentType;

public class TestEquipmentTypeManager {	
	private static EquipmentTypeManager testEquipmentTypeManager;

	private static EquipmentType testBask_Borrow;
	private static EquipmentType testBadm_Borrow;
	private static EquipmentType testTable_Borrow;

	private static EquipmentType testBadm_Sell;
	private static EquipmentType testTable_Sell;
	
	private static ArrayList<EquipmentType> borrowableTypes;
	private static ArrayList<EquipmentType> sellableTypes;
	
	@BeforeAll
	public static void setUpforAll() {		
		borrowableTypes = new ArrayList<>();
		sellableTypes = new ArrayList<>();
		
		testBask_Borrow = new EquipmentType("ET-001", "Basketball Brand A", "BASKA", "Basketball", 15);
		testBadm_Borrow = new EquipmentType("ET-003", "Badminton Racket Brand A", "BRACA", "Badminton", 10);
		testTable_Borrow = new EquipmentType("ET-005", "Table Tennis Racket Brand A", "TRACA", "TableTennis", 10);
		borrowableTypes.add(testBask_Borrow);
		borrowableTypes.add(testBadm_Borrow);
		borrowableTypes.add(testTable_Borrow);
		
		testBadm_Sell = new EquipmentType("ET-007", "Badminton Shuttlecock (6 pcs)", "BBALL6", "Badminton", 35);
		testTable_Sell = new EquipmentType("ET-009", "Table Tennis Ball (3 pcs)", "TBALL1", "TableTennis", 15);
		sellableTypes.add(testBadm_Sell);
		sellableTypes.add(testTable_Sell);
	}
	
	@BeforeEach
	public void setUp() {
		testEquipmentTypeManager = EquipmentTypeManager.getInstance();

		for (EquipmentType type: borrowableTypes) {
			testEquipmentTypeManager.addBorrowableType(type);
		}
		
		for (EquipmentType type: sellableTypes) {
			testEquipmentTypeManager.addSellableType(type);
		}	
	}
	
	@AfterEach
	public void tearDown() {
		testEquipmentTypeManager.reset();
	}
	
	@Test
	public void testAddGetBorrowableEquips() {
		assertEquals(borrowableTypes, testEquipmentTypeManager.getBorrowableTypes());
	}
	
	@Test
	public void testAddGetSellableEquips() {
		assertEquals(sellableTypes, testEquipmentTypeManager.getSellableTypes());
	}
	
	@Test
	public void testGetBorrowableTypesBySportType() {
		for (EquipmentType type: borrowableTypes) {
			String sportType = type.getSportType();
			assertTrue(testEquipmentTypeManager.getBorrowableTypesBySportType(sportType).contains(type));
		}
	}
	
	@Test
	public void testGetSellableTypesBySportType() {
		for (EquipmentType type: sellableTypes) {
			String sportType = type.getSportType();
			assertTrue(testEquipmentTypeManager.getSellableTypesBySportType(sportType).contains(type));
		}
	}
	
	@Test
	public void testGetEquipmentTypeCategory() {
		for (EquipmentType type: borrowableTypes) {
			assertEquals(testEquipmentTypeManager.getEquipmentTypeCategory(type), EquipmentCategory.BORROWABLE);
		}
		
		for (EquipmentType type: sellableTypes) {
			assertEquals(testEquipmentTypeManager.getEquipmentTypeCategory(type), EquipmentCategory.SELLABLE);
		}
	}
	
	@Test
	public void testGetEquipmentTypeByID() {
		for (EquipmentType type: borrowableTypes) {
			String id = type.getEquipmentTypeID();
			assertEquals(testEquipmentTypeManager.getEquipmentTypeByID(id), type);
		}
		
		for (EquipmentType type: sellableTypes) {
			String id = type.getEquipmentTypeID();
			assertEquals(testEquipmentTypeManager.getEquipmentTypeByID(id), type);
		}
	}
}
