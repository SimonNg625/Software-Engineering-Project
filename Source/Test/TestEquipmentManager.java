package Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.model.Equipment;
import sportapp.model.EquipmentCategory;
import sportapp.model.EquipmentType;

import sportapp.manager.EquipmentManager;

public class TestEquipmentManager {
	private EquipmentType testBorrowableType;
	private EquipmentType testSellableType;
	private ArrayList<Equipment> testBorrowableEquips;
	private ArrayList<Equipment> testSellableEquips;
	
	private EquipmentManager testEquipmentManager;
	
	@BeforeEach
	public void setUp() {
		testEquipmentManager = EquipmentManager.getInstance();
		testEquipmentManager.reset();
		
		testBorrowableType = new EquipmentType("ET-001", "Basketball Brand A", "BASKA", "Basketball", 15);
		testBorrowableType.setCategory(EquipmentCategory.BORROWABLE);
		
		testSellableType = new EquipmentType("ET-007", "Badminton Shuttlecock (6 pcs)", "BBALL6", "Badminton", 35);
		testSellableType.setCategory(EquipmentCategory.SELLABLE);
		
		testBorrowableEquips = new ArrayList<>();
		testSellableEquips = new ArrayList<>();
		for (int id=0; id<5; id++) {
			testBorrowableEquips.add(new Equipment(id, testBorrowableType));
		}
		testSellableEquips.add(new Equipment(1, testSellableType));
	}
	
	@Test
	public void TestGetBorrowEquipment() {
		for (Equipment equip: testBorrowableEquips) {
			testEquipmentManager.addEquipment(equip);
		}
		assertEquals(testEquipmentManager.getBorrowableCollection(), testBorrowableEquips);
	}
	
	@Test
	public void TestGetSellEquipment() {		
		for (Equipment equip: testSellableEquips) {
			testEquipmentManager.addEquipment(equip);
		}
		assertEquals(testEquipmentManager.getSellableCollection(), testSellableEquips);
	}
	
	@Test
	public void TestGetBorrowEquipbyType() {		
		for (Equipment equip: testBorrowableEquips) {
			testEquipmentManager.addEquipment(equip);
		}
		
		assertEquals(testEquipmentManager.getBorrowableEquipmentByType(testBorrowableType), testBorrowableEquips);
	}
	
	@Test
	public void TestGetSellEquipbyType() {		
		for (Equipment equip: testSellableEquips) {
			testEquipmentManager.addEquipment(equip);
		}
		
		assertEquals(testEquipmentManager.getSellableEquipmentByType(testSellableType), testSellableEquips);
	}

}
