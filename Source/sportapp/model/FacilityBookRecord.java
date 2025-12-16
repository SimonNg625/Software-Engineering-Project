package sportapp.model;

import java.time.LocalDate;

import sportapp.User;

/**
 * Represents a booking record for a sport facility in the sport management system.
 * <p>
 * This class extends the BookRecord class and includes additional details specific to facility bookings,
 * such as the associated sport facility and methods to calculate the total price.
 */
public class FacilityBookRecord extends BookRecord implements Comparable<FacilityBookRecord> {

    /**
     * The sport facility associated with the booking.
     */
    private SportFacility sportFacility;

    /**
     * Constructs a FacilityBookRecord with the specified details.
     *
     * @param sportFacility The sport facility being booked.
     * @param user The user making the booking.
     * @param date The date of the booking.
     * @param starTime The start time of the booking.
     * @param endTime The end time of the booking.
     * @param status The status of the booking.
     */
    public FacilityBookRecord(SportFacility sportFacility, User user, LocalDate date, int starTime, int endTime, BookingStatus status) {
        super(user, date, starTime, endTime, status);
        this.sportFacility = sportFacility;
    }

    /**
     * Retrieves the sport facility associated with the booking.
     *
     * @return The sport facility.
     */
    public SportFacility getSportFacility() {
        return sportFacility;
    }

    /**
     * Sets the sport facility associated with the booking.
     *
     * @param sportFacility The new sport facility.
     */
    public void setSportFacility(SportFacility sportFacility) {
        this.sportFacility = sportFacility;
    }

    /**
     * Retrieves the timeslot of the booking.
     *
     * @return An array containing the start and end hours of the booking.
     */
    public int[] getTimeslot() {
        return (new int[] {startHour, endHour});
    }

    /**
     * Updates the sport facility associated with the booking.
     *
     * @param newFacility The new sport facility.
     */
    public void updateFacility(SportFacility newFacility) {
        this.sportFacility = newFacility;
    }

    /**
     * Calculates the total price of the booking based on the facility's hourly rate.
     *
     * @return The total price of the booking.
     */
    public double getTotalPrice() {
        return (this.sportFacility.getSportFacilityType().getPricePerHour()) * (this.endHour - this.startHour);
    }

    /**
     * Returns a string representation of the booking record.
     *
     * @return A string containing booking details.
     */
    @Override
    public String toString() {
			// Assuming the class has fields: sportFacility, bookingDate, startHour, endHour
			return "Booking confirmed for: " + this.sportFacility.getName() + "\n" +
						"Date: " + this.date + "\n" +
						"Time: " + this.startHour + ":00 - " + this.endHour + ":00\n" ;
	}

	@Override
	public int compareTo(FacilityBookRecord target) {
    if (this.date.compareTo(target.date) != 0) {
      return this.date.compareTo(target.date);
    } else {
      return Integer.compare(this.startHour, target.startHour);
    }
	}
}
