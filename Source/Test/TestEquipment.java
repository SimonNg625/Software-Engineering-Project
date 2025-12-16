package Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.model.Equipment;
import sportapp.model.EquipmentCategory;
import sportapp.model.EquipmentType;

public class TestEquipment {
	private int testID;
	private EquipmentType testBorrowableType;
	private EquipmentType testSellableType;
	private EquipmentType testTabletennisType;
	private Equipment testBorrowableEquip;
	private Equipment testSellableEquip;
	
	@BeforeEach
	public void setUp() {
		testID = 1;
		testBorrowableType = new EquipmentType("ET-001", "Basketball Brand A", "BASKA", "Basketball", 15);
		testBorrowableType.setCategory(EquipmentCategory.BORROWABLE);
		testSellableType = new EquipmentType("ET-007", "Badminton Shuttlecock (6 pcs)", "BBALL6", "Badminton", 35);
		testSellableType.setCategory(EquipmentCategory.SELLABLE);
		testTabletennisType = new EquipmentType("ET-009", "Table Tennis Ball (3 pcs)", "TBALL1", "TableTennis", 15);

		testBorrowableEquip = new Equipment(testID, testBorrowableType);
		testSellableEquip = new Equipment(testID, testSellableType);
	}
	
	@Test
	public void TestGetEquipmentID() {
		String expect_ID = String.format("%s-%03d", testBorrowableType.getEquipmentTypeShortName(), testID);
		assertEquals(testBorrowableEquip.getEquipmentID(), expect_ID);
	}

	@Test
	public void TestGetEquipmentName() {		
		assertEquals(testBorrowableEquip.getEquipmentName(), testBorrowableType.getEquipmentTypeName());
	}

	@Test
	public void TestGetEquipmentType() {		
		assertEquals(testBorrowableEquip.getEquipmentType(), testBorrowableType);
	}

	@Test
	public void TestGetEquipmentTypeName() {		
		assertEquals(testBorrowableEquip.getEquipmentTypeName(), testBorrowableType.getEquipmentTypeName());
	}

	@Test
	public void TestGetEquipmentPrice() {		
		assertEquals(testBorrowableEquip.getPrice(), testBorrowableType.getPrice());
	}

	@Test
	public void TestGetEquipmentCategory() {		
		assertEquals(testBorrowableEquip.getCategory(), testBorrowableType.getCategory());
	}

	@Test
	public void TestIsBorrowableEquipment() {		
		assertEquals(testBorrowableEquip.isBorrowable(), true);
	}
	
	@Test
	public void TestIsSellableEquipment() {		
		assertEquals(testSellableEquip.isSellable(), true);
	}

	@Test
	public void TestGetEquipmentTypeID() {		
		assertEquals(testBorrowableType.getEquipmentTypeID(), "ET-001");
	}

	@Test
	public void TestGetSportType() {		
		assertEquals(testBorrowableType.getSportType(), "Basketball");
		assertEquals(testSellableType.getSportType(), "Badminton");
		assertEquals(testTabletennisType.getSportType(), "TableTennis");
	}

	@Test
	public void TestEquipmentTypeCompareTo() {		
		assertTrue(testBorrowableType.compareTo(testSellableType) < 0);
		assertTrue(testSellableType.compareTo(testBorrowableType) > 0);
		assertTrue(testBorrowableType.compareTo(testBorrowableType) == 0);
	}
}
