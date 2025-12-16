package sportapp.model;

/**
 * Represents a type of equipment in the sport management system.
 * <p>
 * This class provides details about the equipment type, including its ID, name, category, sport type, and price.
 */
public class EquipmentType implements Comparable<EquipmentType> {

    /**
     * The category of the equipment (e.g., sellable or borrowable).
     */
    private EquipmentCategory category;

    /**
     * The unique identifier for the equipment type.
     */
    private String equipmentTypeID;

    /**
     * The full name of the equipment type.
     */
    private String equipmentTypeName;

    /**
     * The short name of the equipment type.
     */
    private String equipmentTypeShortName;

    /**
     * The sport type associated with the equipment.
     */
    private String sportType;

    /**
     * The price of the equipment type.
     */
    private double price;

    /**
     * Constructs a new EquipmentType instance with the specified details.
     *
     * @param equipmentTypeID The unique identifier for the equipment type.
     * @param equipmentTypeName The full name of the equipment type.
     * @param equipmentTypeShortName The short name of the equipment type.
     * @param sportType The sport type associated with the equipment.
     * @param price The price of the equipment type.
     */
    public EquipmentType(String equipmentTypeID, String equipmentTypeName, String equipmentTypeShortName, String sportType, double price) {
        this.equipmentTypeID = equipmentTypeID;
        this.equipmentTypeName = equipmentTypeName;
        this.equipmentTypeShortName = equipmentTypeShortName;
        this.sportType = sportType;
        this.price = price;
    }

    /**
     * Retrieves the unique identifier for the equipment type.
     *
     * @return The equipment type ID.
     */
    public String getEquipmentTypeID() {
        return this.equipmentTypeID;
    }

    /**
     * Retrieves the full name of the equipment type.
     *
     * @return The equipment type name.
     */
    public String getEquipmentTypeName() {
        return this.equipmentTypeName;
    }

    /**
     * Retrieves the short name of the equipment type.
     *
     * @return The equipment type short name.
     */
    public String getEquipmentTypeShortName() {
        return this.equipmentTypeShortName;
    }

    /**
     * Retrieves the sport type associated with the equipment.
     *
     * @return The sport type.
     */
    public String getSportType() {
        return this.sportType;
    }

    /**
     * Retrieves the price of the equipment type.
     *
     * @return The price of the equipment type.
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Retrieves the category of the equipment.
     *
     * @return The equipment category.
     */
    public EquipmentCategory getCategory() {
        return this.category;
    }

    /**
     * Sets the category of the equipment.
     *
     * @param cat The equipment category to set.
     */
    public void setCategory(EquipmentCategory cat) {
        this.category = cat;
    }

    @Override
    public int compareTo(EquipmentType type) {
        return this.equipmentTypeID.compareTo(type.equipmentTypeID);
    }
}