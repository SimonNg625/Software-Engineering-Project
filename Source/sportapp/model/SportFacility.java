package sportapp.model;

/**
 * Represents a sport facility in the sport management system.
 * <p>
 * This class provides details about the facility, including its name, type, and status.
 */
public class SportFacility {

    /**
     * Represents the status of the sport facility.
     */
    public enum Status {

        /**
         * The facility is available for use.
         */
        AVAILABLE,

        /**
         * The facility is under maintenance and not available for use.
         */
        UNDER_MAINTENANCE
    }

    /**
     * The name of the sport facility.
     */
    private String name;

    /**
     * The type of the sport facility.
     */
    private SportFacilityType sportFacilityType;

    /**
     * The current status of the sport facility.
     */
    private Status status;

    /**
     * Constructs a new SportFacility instance with the specified details.
     *
     * @param name The name of the sport facility.
     * @param sportFacilityType The type of the sport facility.
     * @param status The current status of the sport facility.
     */
    public SportFacility(String name, SportFacilityType sportFacilityType, Status status) {
        this.name = name;
        this.sportFacilityType = sportFacilityType;
        this.status = status;
    }

    /**
     * Retrieves the name of the sport facility.
     *
     * @return The name of the sport facility.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the type of the sport facility.
     *
     * @return The type of the sport facility.
     */
    public SportFacilityType getSportFacilityType() {
        return sportFacilityType;
    }

    /**
     * Retrieves the current status of the sport facility.
     *
     * @return The current status of the sport facility.
     */
    public Status getStatus() {
        return status;
    }
}

