package sportapp.model;

/**
 * Represents a type of sport facility in the sport management system.
 * <p>
 * This class contains information about the facility type, the sport it is associated with,
 * and the price per hour for using the facility.
 */
public class SportFacilityType {

    /**
     * The name of the facility type (e.g., "Badminton Court", "Basketball Court").
     */
    private String typeName;

    /**
     * The type of sport associated with the facility (e.g., "Basketball", "Badminton").
     */
    private String sportType;

    /**
     * The price per hour for using the facility.
     */
    private double pricePerHour;

    /**
     * Constructs a SportFacilityType with the specified name, sport type, and price per hour.
     *
     * @param name The name of the facility type.
     * @param sportType The type of sport associated with the facility.
     * @param pricePerHour The price per hour for using the facility.
     */
    public SportFacilityType(String name, String sportType, double pricePerHour) {
        this.typeName = name;
        this.sportType = sportType;
        this.pricePerHour = pricePerHour;
    }

    /**
     * Gets the name of the facility type.
     *
     * @return The name of the facility type.
     */
    public String getFacilityTypeName() {
        return this.typeName;
    }

    /**
     * Gets the price per hour for using the facility.
     *
     * @return The price per hour for using the facility.
     */
    public double getPricePerHour() {
        return this.pricePerHour;
    }

    /**
     * Gets the type of sport associated with the facility.
     *
     * @return The type of sport associated with the facility.
     */
    public String getSportType() {
        return this.sportType;
    }
}
