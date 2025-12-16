package sportapp.manager;


import java.util.ArrayList;
import java.util.Collections;

import sportapp.User;
import sportapp.UserCollection;
import sportapp.model.BookingStatus;
import sportapp.model.FacilityBookRecord;

/**
 * Manages booking records for facilities.
 * <p>
 * This singleton class provides methods to add, remove, and retrieve booking records
 * for facilities, ensuring centralized management and sorting of records.
 */
public class FacilityBookManager {

    /**
     * Singleton instance of FacilityBookManager, eagerly initialized for thread safety.
     */
    private static final FacilityBookManager instance = new FacilityBookManager();

    /**
     * List of facility booking records.
     */
    private ArrayList<FacilityBookRecord> bookingRecords;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private FacilityBookManager() {
        bookingRecords = new ArrayList<>();
    }

    /**
     * Retrieves the singleton instance of FacilityBookManager.
     *
     * @return The singleton instance of FacilityBookManager.
     */
    public static FacilityBookManager getInstance() {
        return instance;
    }

    /**
     * Adds a booking record to the collection and sorts the records.
     *
     * @param bookingRecord The booking record to add.
     * @throws IllegalArgumentException If the booking record is null or already exists.
     */
    public void addBooking(FacilityBookRecord bookingRecord) throws IllegalArgumentException {
        if(bookingRecord == null) {
            throw new IllegalArgumentException("Booking record cannot be null");
        }
        if(bookingRecords.contains(bookingRecord)) {
            throw new IllegalArgumentException("Booking record already exists");
        }
        bookingRecords.add(bookingRecord);
        sortCollection();
    }

    /**
     * Removes a booking record from the collection.
     *
     * @param bookingRecord The booking record to remove.
     * @throws IllegalArgumentException If the booking record is null or does not exist.
     */
    public void removeBooking(FacilityBookRecord bookingRecord) throws IllegalArgumentException {
        if(bookingRecord == null || !bookingRecords.contains(bookingRecord)) {
            throw new IllegalArgumentException("Booking record does not exist");
        }
        bookingRecords.remove(bookingRecord);
    }
    
    /**
     * Checks if a booking record exists in the collection.
     *
     * @param bookingRecord The booking record to check.
     * @return true if the booking record exists, false otherwise.
     * @throws IllegalArgumentException If the booking record is null.
     */
    public boolean isBookingExist(FacilityBookRecord bookingRecord) throws IllegalArgumentException {
        if(bookingRecord == null) {
            throw new IllegalArgumentException("Booking record cannot be null");
        }
        return bookingRecords.contains(bookingRecord);
    }

    /**
     * Retrieves all booking records.
     *
     * @return An ArrayList of all booking records.
     */
    public ArrayList<FacilityBookRecord> getBookingRecords() {
        return bookingRecords;
    }

    /**
     * Retrieves all pending booking records for a specific user.
     *
     * @param user The user whose pending bookings are to be retrieved.
     * @return An ArrayList of the user's pending booking records.
     * @throws IllegalArgumentException If the user is null or does not exist.
     */
    public ArrayList<FacilityBookRecord> getUserBooking(User user) throws IllegalArgumentException {
        if(user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if(!UserCollection.getInstance().checkUserExist(user)) {
            throw new IllegalArgumentException("User does not exist");
        }

        ArrayList<FacilityBookRecord> bookingRecords = new ArrayList<>();
        for(FacilityBookRecord facilityBookRecord : getBookingRecords()) {
            //need to be confirm the facility book record status name
            if(facilityBookRecord.getUser() == user && facilityBookRecord.getStatus() == BookingStatus.PENDING) {
                bookingRecords.add(facilityBookRecord);
            }
        }
        return bookingRecords;
    }
    
    /**
     * Retrieves all confirmed booking records for a specific user.
     *
     * @param user The user whose confirmed bookings are to be retrieved.
     * @return An ArrayList of the user's confirmed booking records.
     * @throws IllegalArgumentException If the user is null or does not exist.
     */
    public ArrayList<FacilityBookRecord> getUserConfirmedBooking(User user) throws IllegalArgumentException {
        if(user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if(!UserCollection.getInstance().checkUserExist(user)) {
            throw new IllegalArgumentException("User does not exist");
        }

        ArrayList<FacilityBookRecord> bookingRecords = new ArrayList<>();
        for(FacilityBookRecord facilityBookRecord : getBookingRecords()) {
            //need to be confirm the facility book record status name
            if(facilityBookRecord.getUser() == user && facilityBookRecord.getStatus() == BookingStatus.CONFIRMED) {
                bookingRecords.add(facilityBookRecord);
            }
        }
        return bookingRecords;
    }

    /**
     * Sorts the collection of booking records.
     */
    public void sortCollection() {
        Collections.sort(bookingRecords);
    }

    /**
     * Resets the manager by clearing all booking records.
     */
    public void reset() {
        bookingRecords.clear();
    }
}
