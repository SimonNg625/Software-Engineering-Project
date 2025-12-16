package sportapp.manager;
import java.util.ArrayList;

import sportapp.model.EquipmentType;
import sportapp.model.EquipmentCategory;

/**
 * Manages the types of equipment available for borrowing and selling.
 * <p>
 * This singleton class provides methods to manage equipment types,
 * including retrieving types by category and sport type.
 */
public class EquipmentTypeManager {

    /**
     * Singleton instance of EquipmentTypeManager.
     */
    private static EquipmentTypeManager instance = null;

    /**
     * List of borrowable equipment types.
     */
    private ArrayList<EquipmentType> borrowableTypes;

    /**
     * List of sellable equipment types.
     */
    private ArrayList<EquipmentType> sellableTypes;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private EquipmentTypeManager() {
        borrowableTypes = new ArrayList<EquipmentType>();
        sellableTypes = new ArrayList<EquipmentType>();
    }

    /**
     * Retrieves the singleton instance of EquipmentTypeManager.
     *
     * @return The singleton instance of EquipmentTypeManager.
     */
    public static EquipmentTypeManager getInstance() {
        if (instance == null) {
            instance = new EquipmentTypeManager();
        }
        return instance;
    }

    /**
     * Retrieves the collection of borrowable equipment types.
     *
     * @return A list of borrowable equipment types.
     */
    public ArrayList<EquipmentType> getBorrowableTypes() {
        return borrowableTypes;
    }

    /**
     * Retrieves the collection of sellable equipment types.
     *
     * @return A list of sellable equipment types.
     */
    public ArrayList<EquipmentType> getSellableTypes() {
        return sellableTypes;
    }

    /**
     * Retrieves borrowable equipment types for a specific sport type.
     *
     * @param sportType The sport type to filter equipment types by.
     * @return A list of borrowable equipment types matching the sport type.
     */
    public ArrayList<EquipmentType> getBorrowableTypesBySportType(String sportType) {
        ArrayList<EquipmentType> targetTypes = new ArrayList<>();
        
        for (EquipmentType type: borrowableTypes) {
          if (type.getSportType().equals(sportType)) {
            targetTypes.add(type);
          }
        }
        return targetTypes;
      }

    /**
     * Retrieves sellable equipment types for a specific sport type.
     *
     * @param sportType The sport type to filter equipment types by.
     * @return A list of sellable equipment types matching the sport type.
     */
    public ArrayList<EquipmentType> getSellableTypesBySportType(String sportType) {
        ArrayList<EquipmentType> targetTypes = new ArrayList<>();
        
        for (EquipmentType type: sellableTypes) {
          if (type.getSportType().equals(sportType)) {
            targetTypes.add(type);
          }
        }
        return targetTypes;
      }

    /**
     * Adds a new borrowable equipment type.
     *
     * @param type The equipment type to add as borrowable.
     */
    public void addBorrowableType(EquipmentType type) {
        borrowableTypes.add(type);
        type.setCategory(EquipmentCategory.BORROWABLE);
    }

    /**
     * Adds a new sellable equipment type.
     *
     * @param type The equipment type to add as sellable.
     */
    public void addSellableType(EquipmentType type) {
        sellableTypes.add(type);
        type.setCategory(EquipmentCategory.SELLABLE);
    }

    /**
     * Retrieves the category of a specific equipment type.
     *
     * @param type The equipment type whose category is to be retrieved.
     * @return The category of the equipment type.
     */
    public EquipmentCategory getEquipmentTypeCategory(EquipmentType type) {
        return type.getCategory();
    }

    /**
     * Retrieves an equipment type by its ID.
     *
     * @param ID The ID of the equipment type to retrieve.
     * @return The equipment type matching the ID, or null if not found.
     */
    public EquipmentType getEquipmentTypeByID(String ID) {
        ID = ID.toUpperCase();
        EquipmentType result = null;
        
        for (EquipmentType type: borrowableTypes) {
          if (type.getEquipmentTypeID().equals(ID)) {result = type;}
        }
        
        for (EquipmentType type: sellableTypes) {
          if (type.getEquipmentTypeID().equals(ID)) {result = type;}
        }

        return result;
    }

    /**
     * Resets the manager, clearing all borrowable and sellable types.
     */
    public void reset() {
        borrowableTypes.clear();
        sellableTypes.clear();
    }
}