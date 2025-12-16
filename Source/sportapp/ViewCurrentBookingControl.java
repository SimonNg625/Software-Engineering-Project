package sportapp;

import java.util.*;

import sportapp.manager.SportFacilityManager;
import sportapp.model.*;
import java.time.LocalDate;

/**
 * Controls the operations related to viewing current bookings in the sport management system.
 * <p>
 * This class provides methods to retrieve, update, and cancel current bookings for both
 * facilities and equipment.
 */
public class ViewCurrentBookingControl {

    /**
     * Controller for managing facility bookings.
     */
    private FacilityBookingControl facilityBookingControl;

    /**
     * Controller for managing equipment bookings.
     */
    private EquipmentBookingControl equipmentBookingControl;

    /**
     * Constructs a ViewCurrentBookingControl with default booking controllers.
     */
    public ViewCurrentBookingControl() {
        this.facilityBookingControl = new FacilityBookingControl();
        this.equipmentBookingControl = new EquipmentBookingControl();
    }

    /**
     * Retrieves the facility bookings for the specified user.
     *
     * @param user The user whose facility bookings are to be retrieved.
     * @return A list of facility booking records for the user.
     */
    public ArrayList<FacilityBookRecord> getUserFacilityBookings(User user) {
        return facilityBookingControl.getUserBooking(user);
    }

    /**
     * Retrieves the equipment bookings for the specified user.
     *
     * @param user The user whose equipment bookings are to be retrieved.
     * @return A list of equipment booking records for the user.
     */
    public ArrayList<EquipmentBookRecord> getUserEquipmentBookings(User user) {
        return equipmentBookingControl.getBookingRecordsByUser(user);
    }

    /**
     * Retrieves the list of available sport facilities.
     *
     * @return A list of available sport facilities.
     */
    public ArrayList<SportFacility> getAvailableFacilities() {
        return SportFacilityManager.getInstance().getSportFacilities();
    }

    /**
     * Retrieves the pending equipment booking records for the specified user.
     *
     * @param user The user whose pending equipment booking records are to be retrieved.
     * @return A list of pending equipment booking records for the user.
     */
    public ArrayList<EquipmentBookRecord> getPendingEquipmentBookingRecord(User user) {
        return equipmentBookingControl.getPeningBookRecords(user);
    }

    /**
     * Retrieves the pending facility booking records for the specified user.
     *
     * @param user The user whose pending facility booking records are to be retrieved.
     * @return A list of pending facility booking records for the user.
     */
    public ArrayList<FacilityBookRecord> getPendingFacilityBookingRecord(User user) {
        return facilityBookingControl.getPeningBookRecords(user);
    }

    /**
     * Retrieves the available time slots for the specified facility and date.
     *
     * @param facility The facility for which to retrieve available time slots.
     * @param date The date for which to retrieve available time slots.
     * @return A list of available time slots for the facility on the specified date.
     */
    public ArrayList<int[]> getAvailableTimeSlots(SportFacility facility, LocalDate date) {
        return facilityBookingControl.getAvailableTimeSlot(facility, date);
    }

    /**
     * Retrieves the available time slots for the specified equipment and date.
     *
     * @param equipments The list of equipment for which to retrieve available time slots.
     * @param date The date for which to retrieve available time slots.
     * @return A list of available time slots for the equipment on the specified date.
     */
    public ArrayList<int[]> getAvailableTimeSlots(ArrayList<Equipment> equipments, LocalDate date) {
        return equipmentBookingControl.calculateAvailableGapTimeSlot(equipments, date);
    }

    /**
     * Updates the date and time of the specified facility booking.
     *
     * @param booking The facility booking to update.
     * @param date The new date for the booking.
     * @param startTime The new start time for the booking.
     * @param endTime The new end time for the booking.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateBookingDateTime(FacilityBookRecord booking, LocalDate date, int startTime, int endTime) {
        try {
            facilityBookingControl.updateBookingDateTime(booking, date, startTime, endTime);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Updates the date and time of the specified equipment booking.
     *
     * @param booking The equipment booking to update.
     * @param date The new date for the booking.
     * @param startTime The new start time for the booking.
     * @param endTime The new end time for the booking.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateBookingDateTime(EquipmentBookRecord booking, LocalDate date, int startTime, int endTime) {
        try {
            equipmentBookingControl.updateBookingDateTime(booking, date, new int[]{startTime, endTime});
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Updates the facility and date/time of the specified facility booking.
     *
     * @param booking The facility booking to update.
     * @param facility The new facility for the booking.
     * @param date The new date for the booking.
     * @param startTime The new start time for the booking.
     * @param endTime The new end time for the booking.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateBookingFacility(FacilityBookRecord booking, SportFacility facility, LocalDate date, int startTime, int endTime) {
        try {
            facilityBookingControl.updateBookingFacility(booking, facility);
            facilityBookingControl.updateBookingDateTime(booking, date, startTime, endTime);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Cancels the specified facility booking.
     *
     * @param booking The facility booking to cancel.
     */
    public void cancelBooking(FacilityBookRecord booking) {
        facilityBookingControl.cancelBooking(booking);
    }

    /**
     * Cancels the specified equipment booking.
     *
     * @param booking The equipment booking to cancel.
     */
    public void cancelBooking(EquipmentBookRecord booking) {
        equipmentBookingControl.cancelBooking(booking);
    }

    /**
     * Validates whether the specified time slot is available for the given facility and date.
     *
     * @param facility The facility to check.
     * @param date The date to check.
     * @param startTime The start time of the time slot.
     * @param endTime The end time of the time slot.
     * @return True if the time slot is valid, false otherwise.
     */
    public boolean isValidTimeSlot(SportFacility facility, LocalDate date, int startTime, int endTime) {
        if (startTime < 9 || endTime > 21 || startTime >= endTime) {
            return false;
        }
        ArrayList<int[]> slots = getAvailableTimeSlots(facility, date);
        for (int[] slot : slots) {
            if (slot[0] <= startTime && slot[1] >= endTime) {
                return true;
            }
        }
        return false;
    }
}
