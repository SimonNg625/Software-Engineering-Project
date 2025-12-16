package sportapp.model;

import sportapp.model.EquipmentCategory;

/**
 * Represents a piece of equipment in the sport management system.
 * <p>
 * This class provides details about the equipment, including its ID, type, name, and category.
 */
public class Equipment {

    /**
     * The unique identifier for the equipment.
     */
    private int equipmentID;

    /**
     * The type of the equipment.
     */
    private EquipmentType equipmentType;

    /**
     * The full name of the equipment.
     */
    private String equipmentName;

    /**
     * The short name of the equipment.
     */
    private String equipmentShortName;

    /**
     * Constructs a new Equipment instance with the specified ID and type.
     *
     * @param equipmentID The unique identifier for the equipment.
     * @param equipmentType The type of the equipment.
     */
    public Equipment(int equipmentID, EquipmentType equipmentType) {
        this.equipmentID = equipmentID;
        this.equipmentType = equipmentType;
        this.equipmentName = equipmentType.getEquipmentTypeName();
        this.equipmentShortName = equipmentType.getEquipmentTypeShortName();
    }

    /**
     * Retrieves the formatted equipment ID.
     *
     * @return The formatted equipment ID as a string.
     */
    public String getEquipmentID() {
        return String.format("%s-%03d", equipmentShortName, equipmentID);
    }

    /**
     * Retrieves the full name of the equipment.
     *
     * @return The full name of the equipment.
     */
    public String getEquipmentName() {
        return this.equipmentName;
    }

    /**
     * Retrieves the type of the equipment.
     *
     * @return The type of the equipment.
     */
    public EquipmentType getEquipmentType() {
        return this.equipmentType;
    }

    /**
     * Retrieves the name of the equipment type.
     *
     * @return The name of the equipment type.
     */
    public String getEquipmentTypeName() {
        return this.equipmentType.getEquipmentTypeName();
    }

    /**
     * Retrieves the price of the equipment.
     *
     * @return The price of the equipment.
     */
    public double getPrice() {
        return this.equipmentType.getPrice();
    }

    /**
     * Retrieves the category of the equipment.
     *
     * @return The category of the equipment.
     */
    public EquipmentCategory getCategory() {
        return this.equipmentType.getCategory();
    }

    /**
     * Determines if the equipment is borrowable.
     *
     * @return True if the equipment is borrowable, false otherwise.
     */
    public boolean isBorrowable() {
        return this.getCategory().equals(EquipmentCategory.BORROWABLE);
    }

    /**
     * Determines if the equipment is sellable.
     *
     * @return True if the equipment is sellable, false otherwise.
     */
    public boolean isSellable() {
        return this.getCategory().equals(EquipmentCategory.SELLABLE);
    }
}