package sportapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sportapp.manager.FacilityBookManager;
import sportapp.manager.SportFacilityManager;
import sportapp.model.BookingStatus;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;

/**
 * Controls the booking operations for sport facilities.
 * <p>
 * This class provides methods to manage and retrieve booking records, check availability,
 * and calculate gaps between bookings.
 */
public class FacilityBookingControl {

    /**
     * Manages the collection of facility booking records.
     */
    private FacilityBookManager collectionManager;

    /**
     * Manages the list of sport facilities.
     */
    private SportFacilityManager facilityManager;

    /**
     * Provides the current time for booking operations.
     */
    private Clock clock;

    /**
     * Constructs a FacilityBookingControl instance and initializes managers.
     */
    public FacilityBookingControl() {
        this.collectionManager = FacilityBookManager.getInstance();
        this.facilityManager = SportFacilityManager.getInstance();
        this.clock = Clock.getInstance();
    }

    /**
     * Retrieves a list of available sport facilities.
     *
     * @return A list of sport facilities that are currently available.
     */
    public ArrayList<SportFacility> getAvailableFacilitys() {
        ArrayList<SportFacility> facilityList = facilityManager.getSportFacilities();
        ArrayList<SportFacility> availableFacilities = new ArrayList<>();
        for(SportFacility facility : facilityList) {
            if(facility.getStatus() == SportFacility.Status.AVAILABLE) {
                availableFacilities.add(facility);
            }
        }
        return availableFacilities;
    }

    /**
     * Retrieves booking records for a specific facility on a given date.
     *
     * @param requestedFacility The facility to check bookings for.
     * @param requestedDate The date to check bookings on.
     * @return A list of booking records matching the facility and date.
     * @throws IllegalArgumentException If the facility or date is null, or the facility does not exist.
     */
    public ArrayList<FacilityBookRecord> getFacilityBookRecordsByDate(SportFacility requestedFacility , LocalDate requestedDate) throws IllegalArgumentException {
        if(requestedFacility == null || requestedDate == null) {
            throw new IllegalArgumentException("Requested facility and date cannot be null");
        }

        if(!SportFacilityManager.getInstance().checkFacilityExist(requestedFacility)) {
            throw new IllegalArgumentException("Requested facility does not exist");
        }

        ArrayList<FacilityBookRecord> matchedBookings = new ArrayList<>();
        for(FacilityBookRecord facilityBookRecord : collectionManager.getBookingRecords()) {
            if(facilityBookRecord.getDate().isEqual(requestedDate) && facilityBookRecord.getSportFacility() == requestedFacility) {
                matchedBookings.add(facilityBookRecord);
            }
        }
        return matchedBookings;
    }

    /**
     * Retrieves pending booking records for a specific user.
     *
     * @param user The user whose pending bookings are to be retrieved.
     * @return A list of pending booking records for the user.
     */
    public ArrayList<FacilityBookRecord> getPeningBookRecords(User user) {
    ArrayList<FacilityBookRecord> result = new ArrayList<>();
    for (FacilityBookRecord faciBookRecord: collectionManager.getBookingRecords()) {
      if (faciBookRecord.getStatus() == BookingStatus.PENDING && faciBookRecord.getUser().equals(user)) {
        result.add(faciBookRecord);
      }
    }
    return result;
  }
    
    /**
     * Calculates gaps between bookings for a facility.
     *
     * @param bookings The list of bookings to analyze.
     * @param openingHour The opening hour of the facility.
     * @param closingHour The closing hour of the facility.
     * @return A list of time gaps represented as arrays of start and end hours.
     */
    public ArrayList<int[]> calculateGapsBetweeenBookings(ArrayList<FacilityBookRecord> bookings, int openingHour, int closingHour) {
		ArrayList<int[]> gaps = new ArrayList<>();
		if(bookings == null || bookings.isEmpty()) {
			gaps.add(new int[]{9, 21});
			return gaps;
		}

		// Sort bookings by start time
		Collections.sort(bookings, Comparator.comparingInt(FacilityBookRecord::getStartHour));

		// Find gaps between bookings
		int startHour = 9;
		for(FacilityBookRecord booking : bookings) {
			if(booking.getStartHour() > startHour) {
				gaps.add(new int[]{startHour, booking.getStartHour()});
			}
			System.out.println("Gap: " + startHour + " to " + booking.getStartHour());
			startHour = booking.getEndHour();
		}

		// Add gap from last booking to end of day
		if(startHour < 21) {
			gaps.add(new int[]{startHour, 21});
		}

		return gaps;
	}

    /**
     * Returns a 2D list of start and end times for each available time slot for a given date.
     * For example, if there are bookings from 12:00 to 13:00 and 14:00 to 15:00,
     * the result will be: [[9, 12], [13, 14], [15, 21]].
     * Assumes operating hours from 09:00 to 21:00.
     *
     * @param facility The facility for which to retrieve available time slots.
     * @param date The date for which to retrieve available time slots.
     * @return A list of available time slots represented as start and end times.
     */
    public ArrayList<int[]> getAvailableTimeSlot(SportFacility facility, LocalDate date) {
        // 1. Get all bookings for the specific facility and date
        ArrayList<FacilityBookRecord> facilityBookings = getFacilityBookRecordsByDate(facility, date);
        ArrayList<int[]> availableTimeSlot = new ArrayList<>();

        // 2. Sort the bookings by their start time. This is the crucial step.
        Collections.sort(facilityBookings, Comparator.comparingInt(FacilityBookRecord::getStartHour));

        int lastEndTime = 9; // Start tracking from the opening time (9 AM)

        // 3. Loop through the SORTED bookings to find gaps
        for (FacilityBookRecord booking : facilityBookings) {
            // If there's a gap between the last booking's end time and the current booking's start time
            if (booking.getStartHour() > lastEndTime) {
                availableTimeSlot.add(new int[]{lastEndTime, booking.getStartHour()});
            }
            // Update the last end time to the end time of the current booking
            lastEndTime = booking.getEndHour();
        }

        // 4. Check for a final gap between the last booking and the closing time (9 PM)
        if (lastEndTime < 21) {
            availableTimeSlot.add(new int[]{lastEndTime, 21});
        }
        return availableTimeSlot;
    }
   
    /**
     * Retrieves all booking records for a specific user.
     *
     * @param user The user whose booking records are to be retrieved.
     * @return A list of booking records associated with the user.
     * @throws IllegalArgumentException If the user is null.
     */
    //get all the booking record of a user
    public ArrayList<FacilityBookRecord> getUserBooking(User user) throws IllegalArgumentException {
        if(user == null) {
            System.out.println("Facility booking control error, User is null");
            throw new IllegalArgumentException("User cannot be null");
        }

        if(!UserCollection.getInstance().checkUserExist(user)) {
            System.out.println("Facility booking control error, User does not exist in the system");
            throw new IllegalArgumentException("User does not exist");
        }

        ArrayList<FacilityBookRecord> bookingRecords = new ArrayList<>();
        for(FacilityBookRecord facilityBookRecord : collectionManager.getBookingRecords()) {
            //need to be confirm the facility book record status name
            if(facilityBookRecord.getUser() == user) {
                bookingRecords.add(facilityBookRecord);
            }
        }
        return bookingRecords;
    }

    /**
     * Cancels a booking record.
     *
     * @param bookRecord The booking record to cancel.
     * @throws IllegalArgumentException If the booking record does not exist.
     */

    public void cancelBooking(FacilityBookRecord bookRecord) throws IllegalArgumentException {
        if(bookRecord == null || !collectionManager.isBookingExist(bookRecord)) {
            throw new IllegalArgumentException("The booking record does not exist.");
        }
        collectionManager.removeBooking(bookRecord);
        collectionManager.sortCollection();
    }

    /**
     * Updates the facility associated with a booking record.
     *
     * @param bookRecord The booking record to update.
     * @param newFacility The new facility to associate with the booking.
     * @throws IllegalArgumentException If the facility does not exist or is unavailable.
     */

    public void updateBookingFacility(FacilityBookRecord bookRecord, SportFacility newFacility) throws IllegalArgumentException {
        //check if the sport facility is available
        if(!SportFacilityManager.getInstance().checkFacilityExist(newFacility) || newFacility.getStatus() != SportFacility.Status.AVAILABLE) {
            throw new IllegalArgumentException("The sport facility does not exist.");
        }
        
        bookRecord.setSportFacility(newFacility);
    }

    /**
     * Updates the date and time of a booking record.
     *
     * @param bookRecord The booking record to update.
     * @param newDate The new date for the booking.
     * @param newStartTime The new start time for the booking.
     * @param newEndTime The new end time for the booking.
     * @throws IllegalArgumentException If the time slot is invalid or unavailable.
     */

    public void updateBookingDateTime(FacilityBookRecord bookRecord, LocalDate newDate, int newStartTime, int newEndTime) throws IllegalArgumentException {
        //check if the sport facility is available
        if(!SportFacilityManager.getInstance().checkFacilityExist(bookRecord.getSportFacility()) || bookRecord.getSportFacility().getStatus() != SportFacility.Status.AVAILABLE) {
            throw new IllegalArgumentException("The booking record does not exist.");
        }

        //check if the time slot is valid
        if(newStartTime < 9 || newEndTime > 21 || newStartTime >= newEndTime) {
            throw new IllegalArgumentException("Invalid time slot. Please select a time between 09:00 and 21:00.");
        }

        //check if the new date and time slot is available
        ArrayList<int[]> availableSlots = getAvailableTimeSlot(bookRecord.getSportFacility(), newDate);
        boolean isAvailable = false;
        for(int[] slot : availableSlots) {
            if(slot[0] <= newStartTime && slot[1] >= newEndTime) {
                isAvailable = true;
                break;
            }
        }
        if(!isAvailable) {
            throw new IllegalArgumentException("The selected time slot is not available on that day.");
        }

        bookRecord.setDate(newDate);
        bookRecord.setStartHour(newStartTime);
        bookRecord.setEndHour(newEndTime);
        collectionManager.sortCollection();
    }
    
    /**
     * Updates the status of all booking records based on the current time.
     */
    public void updateBookingStatus() {
        for (FacilityBookRecord record: collectionManager.getBookingRecords()) {
            if (record.getEndHour() <= clock.getHour()) {
                record.setStatus(BookingStatus.ENDED);
            }
        }
    }

}


