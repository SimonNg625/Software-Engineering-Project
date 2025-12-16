package sportapp.manager;
import java.util.ArrayList;

import sportapp.model.SportFacility;
import sportapp.model.SportFacility.Status;
import sportapp.model.SportFacilityType;

/**
 * Manages the list of sport facilities.
 * <p>
 * This singleton class provides methods to add, retrieve, and check the existence of sport facilities.
 */
public class SportFacilityManager {

    /**
     * Singleton instance of SportFacilityManager, eagerly initialized for thread safety.
     */
    private static final SportFacilityManager instance = new SportFacilityManager();

    /**
     * List of sport facilities.
     */
    private ArrayList<SportFacility> sportFacilities;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private SportFacilityManager() {
        sportFacilities = new ArrayList<SportFacility>();
    }

    /**
     * Retrieves the singleton instance of SportFacilityManager.
     *
     * @return The singleton instance of SportFacilityManager.
     */
    public static SportFacilityManager getInstance() {
        return instance;
    }

    /**
     * Adds a sport facility to the collection.
     *
     * @param facility The sport facility to add.
     */
    public void addSportFacility(SportFacility facility) {
        //if facility already exists, do not add
        sportFacilities.add(facility);
    }

    /**
     * Retrieves the list of sport facilities.
     *
     * @return A list of sport facilities.
     */
    public ArrayList<SportFacility> getSportFacilities() {
        return sportFacilities;
    }

    /**
     * Checks if a sport facility exists in the collection.
     *
     * @param facility The sport facility to check.
     * @return True if the facility exists, false otherwise.
     */
    public boolean checkFacilityExist(SportFacility facility) {
        return sportFacilities.contains(facility);
    }

    /**
     * Retrieves a sport facility by its name.
     *
     * @param name The name of the sport facility.
     * @return The sport facility with the specified name, or null if not found.
     */
    public SportFacility getSportFacilityByName(String name) {
		for (SportFacility facility : sportFacilities) {
			if (facility.getName().equals(name)) {
				return facility;
			}
		}
		return null; // Return null if no facility with the given name is found
	}

    /**
     * Resets the manager by clearing the list of sport facilities.
     */
    public void reset() {
        sportFacilities.clear();
    }
}


