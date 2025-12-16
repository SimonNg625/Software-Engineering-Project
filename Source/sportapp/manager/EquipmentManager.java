package sportapp.manager;
import java.util.ArrayList;

import sportapp.model.Equipment;
import sportapp.model.EquipmentType;
// import sportapp.model.EquipmentType.Category;

/**
 * Manages the equipment inventory.
 * <p>
 * This singleton class provides methods to manage borrowable and sellable equipment,
 * including adding equipment and retrieving collections.
 */
public class EquipmentManager {

    /**
     * Singleton instance of EquipmentManager.
     */
    private static EquipmentManager instance = null;

    /**
     * List of borrowable equipment.
     */
    private ArrayList<Equipment> borrowableEquipments;

    /**
     * List of sellable equipment.
     */
    private ArrayList<Equipment> sellableEquipments;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private EquipmentManager() {
        borrowableEquipments = new ArrayList<Equipment>();
        sellableEquipments = new ArrayList<Equipment>();
    }

    /**
     * Retrieves the singleton instance of EquipmentManager.
     *
     * @return The singleton instance of EquipmentManager.
     */
    public static EquipmentManager getInstance() {
        if (instance == null) {
            instance = new EquipmentManager();
        }
        return instance;
    }

    /**
     * Retrieves the collection of borrowable equipment.
     *
     * @return A list of borrowable equipment.
     */
    public ArrayList<Equipment> getBorrowableCollection() {
        return borrowableEquipments;
    }

    /**
     * Retrieves the collection of sellable equipment.
     *
     * @return A list of sellable equipment.
     */
    public ArrayList<Equipment> getSellableCollection() {
        return sellableEquipments;
    }

    /**
     * Adds equipment to the appropriate collection based on its category.
     *
     * @param equipment The equipment to add.
     */
    public void addEquipment(Equipment equipment) {
        switch (equipment.getEquipmentType().getCategory()) {
            case BORROWABLE:
                borrowableEquipments.add(equipment);
                break;
            case SELLABLE:
                sellableEquipments.add(equipment);
                break;
        }
    }

    /**
     * Retrieves a list of borrowable equipment of the specified type.
     *
     * @param type The type of equipment to retrieve.
     * @return A list of borrowable equipment of the specified type.
     */
    public ArrayList<Equipment> getBorrowableEquipmentByType(EquipmentType type) {
        ArrayList<Equipment> result = new ArrayList<>();

        for (Equipment equipment: borrowableEquipments) {
            if (equipment.getEquipmentType().equals(type)) { result.add(equipment); }
        }

        return result;
    }

    /**
     * Retrieves a list of sellable equipment of the specified type.
     *
     * @param type The type of equipment to retrieve.
     * @return A list of sellable equipment of the specified type.
     */
    public ArrayList<Equipment> getSellableEquipmentByType(EquipmentType type) {
        ArrayList<Equipment> result = new ArrayList<>();

        for (Equipment equipment: sellableEquipments) {
            if (equipment.getEquipmentType().equals(type)) { result.add(equipment); break; }
        }
        
        return result;
    }

    /**
     * Resets the equipment manager, clearing all equipment collections.
     */
    public void reset() {
        borrowableEquipments.clear();
        sellableEquipments.clear();
    }
}