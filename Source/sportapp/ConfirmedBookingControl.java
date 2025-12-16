package sportapp;

import java.time.LocalDate;
import java.util.ArrayList;

import sportapp.manager.ConfirmedBookManager;
import sportapp.model.Equipment;
import sportapp.model.EquipmentBookRecord;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;
import sportapp.ui.ViewConfirmedBookingUI.OBJECT_TYPE;

/**
 * Controls the operations related to confirmed bookings in the sport management system.
 * <p>
 * This class manages confirmed bookings for both facilities and equipment, allowing
 * users to interact with and modify their confirmed bookings.
 */
public class ConfirmedBookingControl {

    /**
     * The facility booking record associated with the confirmed booking.
     */
    public FacilityBookRecord facilityRecord;

    /**
     * The equipment booking record associated with the confirmed booking.
     */
    public EquipmentBookRecord equipmentRecord;

    /**
     * The current target type for the booking (equipment or facility).
     */
    public OBJECT_TYPE currTargetType;

    /**
     * Manager for handling confirmed bookings.
     */
    public ConfirmedBookManager cbm = ConfirmedBookManager.getInstance();

    /**
     * Controller for managing facility bookings.
     */
    private static FacilityBookingControl facilityBookingControl;

    /**
     * Controller for managing equipment bookings.
     */
    private static EquipmentBookingControl equipmentBookingControl;

    /**
     * Constructs a ConfirmedBookingControl with default values.
     */
    public ConfirmedBookingControl() {
        facilityRecord = null;
        equipmentRecord = null;
        facilityBookingControl = new FacilityBookingControl();
        equipmentBookingControl = new EquipmentBookingControl();
    }

    /**
     * Constructs a ConfirmedBookingControl with the specified booking records and target type.
     *
     * @param fbr The facility booking record.
     * @param ebr The equipment booking record.
     * @param typeInput The target type for the booking (equipment or facility).
     */
    public ConfirmedBookingControl(FacilityBookRecord fbr, EquipmentBookRecord ebr, OBJECT_TYPE typeInput) {
        facilityRecord = fbr;
        equipmentRecord = ebr;
        currTargetType = typeInput;
        facilityBookingControl = new FacilityBookingControl();
        equipmentBookingControl = new EquipmentBookingControl();
    }

    /**
     * Sets the target type for the booking.
     *
     * @param typeInput The target type (0 for equipment, 1 for facility).
     * @throws Exception If the input type is invalid.
     */
    public void setType(int typeInput) throws Exception { //equip(0), faci(1)
        if (typeInput != 0 && typeInput != 1) {
            throw new Exception("Invalid Input");
        }
        currTargetType = OBJECT_TYPE.values()[typeInput];
    }
 
    
	/**
	 * Sets the booking record based on the update choice and action.
	 *
	 * @param updateChoice The choice for updating the record.
	 * @param action       The action to perform on the record (e.g., "update" or "cancel").
	 * @throws Exception If the record is invalid, the action cannot be performed, or the booking is not eligible for the specified action.
	 */
	public void setRecord(int updateChoice, String action) throws Exception {
	    if (currTargetType == OBJECT_TYPE.OBJECT_TYPE_FACILITY) {
	        facilityRecord = cbm.getFacilityRecord(updateChoice);
	        currTargetType = OBJECT_TYPE.OBJECT_TYPE_FACILITY;
	        if (facilityRecord == null) {
	            throw new Exception("No Chosen Record with Input Found!");
	        } else {
                int compareDate = facilityRecord.getDate().compareTo(Clock.getInstance().getToday()); 
                switch (action) {
                    case "update":
                        if (compareDate <= 0) {
                            facilityRecord = null;
                            throw new Exception("You can not update the booking.");
                        }

                    case "cancel":
                        
                        if (compareDate < 0 || (compareDate == 0 && facilityRecord.getStartHour() <= Clock.getInstance().getHour())) {
                            facilityRecord = null;
                            throw new Exception("You can not cancel started booking.");
                        }
                }
            }
	    } else {
	        equipmentRecord = cbm.getEquipmentRecord(updateChoice);
	        if (equipmentRecord == null) {
	            throw new Exception("No Chosen Record with Input Found!");
	        } else if (equipmentRecord.isSellable()) {
                    throw new Exception("Cannot update Equipment which is sellable!");
            }else {
                int compareDate = equipmentRecord.getDate().compareTo(Clock.getInstance().getToday()); 
                switch (action) {
                    case "update":
                        if (compareDate <= 0) {
                            equipmentRecord = null;
                            throw new Exception("You can not update the booking.");
                        }
                        
                    case "cancel":
                        if (compareDate < 0 || (compareDate == 0 && equipmentRecord.getStartHour() <= Clock.getInstance().getHour())) {
                            equipmentRecord = null;
                            throw new Exception("You can not cancel started booking.");
                        }
                }
            }
	    }
	}

    /**
     * Retrieves the start hour of the current booking record.
     *
     * @return The start hour of the current booking record.
     */
    public int getStartHour() {
        if (currTargetType == OBJECT_TYPE.OBJECT_TYPE_FACILITY) 
            return facilityRecord.getStartHour();
        else
            return equipmentRecord.getStartHour();
    }

    /**
     * Retrieves the end hour of the current booking record.
     *
     * @return The end hour of the current booking record.
     */
    public int getEndHour() {   
        if (currTargetType == OBJECT_TYPE.OBJECT_TYPE_FACILITY) 
            return facilityRecord.getEndHour();
        else
            return equipmentRecord.getEndHour();
    }

    /**
     * Retrieves the date of the current booking record.
     *
     * @return The date of the current booking record.
     */
    public LocalDate getDate() {
        if (currTargetType == OBJECT_TYPE.OBJECT_TYPE_FACILITY) 
            return facilityRecord.getDate();
        else
            return equipmentRecord.getDate();
    }

    /**
     * Retrieves the available time slots for the specified date.
     *
     * @param date The date for which to retrieve available time slots.
     * @param doDisplay Whether to display the available time slots.
     * @return A list of available time slots as arrays of start and end times.
     * @throws Exception If an error occurs while retrieving the time slots.
     */
    public ArrayList<int[]> getAvailableTimeSlot(LocalDate date, boolean doDisplay) throws Exception {
        ArrayList<int[]> result = new ArrayList<>();
        if (currTargetType == OBJECT_TYPE.OBJECT_TYPE_FACILITY) {
            result = facilityBookingControl.getAvailableTimeSlot(facilityRecord.getSportFacility(), date);
        } else {
            result = equipmentBookingControl.calculateAvailableGapTimeSlot(equipmentRecord.getBookingEquipment(), date);
        }

        if (doDisplay) {
            if (result.isEmpty()) {
                //System.out.println("No available time slots for the selected date.");
                return result;
            }

            //print available time slots
            System.out.println("Available time slots for the selected date:");
            for (int[] slot : result) {
                System.out.printf("Start: %d, End: %d\n", slot[0], slot[1]);
            }
        }
        return result;
    }

    /**
     * Updates the date and time of the current booking record.
     *
     * @param date The new date for the booking.
     * @param startHour The new start hour for the booking.
     * @param endHour The new end hour for the booking.
     */
    public void updateRecordDateTime(LocalDate date, int startHour, int endHour) {
            

            if (currTargetType == OBJECT_TYPE.OBJECT_TYPE_FACILITY) {
                //cbm.updateFacilityRecordTimeDate(facilityRecord, date, startHour, endHour);
                facilityRecord.setDate(date);
    	        facilityRecord.setStartHour(startHour);
    	        facilityRecord.setEndHour(endHour);
            } else if (currTargetType == OBJECT_TYPE.OBJECT_TYPE_EQUIPMENT) {
                //cbm.updateEquipmentRecordTimeDate(equipmentRecord, date, startHour, endHour);
                equipmentBookingControl.updateBookingDateTime(equipmentRecord, date, new int[] {startHour, endHour});
            }

    }
    /**
     * Retrieves the available time slots for a specific facility.
     *
     * @param sportFacility The facility for which to retrieve available slots.
     * @param date The date for which to retrieve available slots.
     * @return A list of available time slots as arrays of start and end times.
     */
    public ArrayList<int[]> getFacilityAvailableSlots(SportFacility sportFacility, LocalDate date) {
        ArrayList<int[]> availableSlots = facilityBookingControl.getAvailableTimeSlot(sportFacility, date);
        return availableSlots;
    }
    /**
     * Retrieves the available time slots for a list of equipment.
     *
     * @param equipments The list of equipment to check availability for.
     * @param date The date for which to retrieve available slots.
     * @return A list of available time slots as arrays of start and end times.
     */
    public ArrayList<int[]> getEquipmentAvailableSlots(ArrayList<Equipment> equipments, LocalDate date) {
        return equipmentBookingControl.calculateAvailableGapTimeSlot(equipments, date);
    }
    /**
     * Updates the facility associated with the current booking record.
     *
     * @param sportFacility The new facility to associate with the booking.
     */
    public void updateFacilityRecordFacility(SportFacility sportFacility) {
        facilityRecord.updateFacility(sportFacility);
    }
    /**
     * Removes the current booking record.
     */
    public void removeRecord() {
        if (currTargetType == OBJECT_TYPE.OBJECT_TYPE_FACILITY) {
            cbm.removeFacilityRecord(facilityRecord);
        } else {
            cbm.removeEquipmentRecord(equipmentRecord); //set cancelled?
        }
    }


}