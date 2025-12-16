package sportapp.manager;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sportapp.User;
import sportapp.model.BookingStatus;
import sportapp.model.Equipment;
import sportapp.model.EquipmentBookRecord;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;
import sportapp.Clock;
import sportapp.EquipmentBookingControl;
import sportapp.FacilityBookingControl;

/**
 * Manages confirmed bookings for facilities and equipment.
 * <p>
 * This singleton class provides methods to retrieve and manage confirmed booking records
 * for both facilities and equipment, ensuring centralized access and control.
 */
public class ConfirmedBookManager {

    /**
     * Singleton instance of ConfirmedBookManager.
     */
    private static ConfirmedBookManager instance = new ConfirmedBookManager();

    /**
     * List of confirmed facility booking records.
     */
    private static ArrayList<FacilityBookRecord> confirmedFacilityBookRecords;

    /**
     * List of confirmed equipment booking records.
     */
    private static ArrayList<EquipmentBookRecord> confirmedEquipmentBookRecords;

    /**
     * Manager for facility bookings.
     */
    private static FacilityBookManager fbm;

    /**
     * Manager for equipment bookings.
     */
    private static EquipmentBookManager ebm;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private ConfirmedBookManager() {
        confirmedFacilityBookRecords = new ArrayList<>();
        confirmedEquipmentBookRecords = new ArrayList<>();
        fbm = FacilityBookManager.getInstance();
        ebm = EquipmentBookManager.getInstance();
    }

    /**
     * Retrieves the singleton instance of ConfirmedBookManager.
     *
     * @return The singleton instance of ConfirmedBookManager.
     */
    public static ConfirmedBookManager getInstance() {
        return instance;
    }

    /**
     * Sets up the user's collection of confirmed bookings.
     *
     * @param user The user whose confirmed bookings are to be set up.
     */
    public void setUpUserCollection(User user) {
        ArrayList<FacilityBookRecord>tempFacilityRecord = fbm.getUserConfirmedBooking(user);
        for (FacilityBookRecord i : tempFacilityRecord) {
            //String temp = i.getSportFacility().getStatus();    //outdated, as status might be available or booked only
            if (i.getStatus().equals(BookingStatus.CONFIRMED)) { //now only get future and not ended booking
				confirmedFacilityBookRecords.add(i);
			}
        }

        ArrayList<EquipmentBookRecord> tempEquipmentRecord = ebm.getBookingRecordsByUser(user);
        for (EquipmentBookRecord i : tempEquipmentRecord) {

            if (i.getStatus().equals(BookingStatus.CONFIRMED)) { 
                confirmedEquipmentBookRecords.add(i);
            }
        }
    }



    /**
     * Resets the user's collection of confirmed bookings.
     *
     * @param user The user whose confirmed bookings are to be reset.
     */
    public void resetUserCollection(User user) {
    	if (user == null) {
			return;
		}
    	if (!confirmedEquipmentBookRecords.isEmpty()) {
			confirmedEquipmentBookRecords.clear();
		}
    	if (!confirmedFacilityBookRecords.isEmpty()) {
			confirmedFacilityBookRecords.clear();
		}
        setUpUserCollection(user);
    }

    /**
     * Checks if the collection of confirmed bookings is empty.
     *
     * @return true if the collection is empty, false otherwise.
     */
    public boolean isCollectionEmpty() {
        return confirmedEquipmentBookRecords.isEmpty() && confirmedFacilityBookRecords.isEmpty();
    }

    /**
     * Displays the confirmed booking records.
     *
     * @param displayType The type of display: 0 for equipment, 1 for facility, 2 for all.
     * @return true if records were displayed, false otherwise.
     */
    public boolean displayConfirmedRecord(int displayType) { // 2 for all, 0: Equipment, 1: Facility
        // display in [date, venue/equipment, time, status(paid)]
        boolean didDisplay;
        switch (displayType) {
            case 0:
                didDisplay = !confirmedEquipmentBookRecords.isEmpty();
                break;
            case 1:
                didDisplay = !confirmedFacilityBookRecords.isEmpty();
                break;
            case 2:
                didDisplay = !isCollectionEmpty();
                break;
            default:
                didDisplay = false;
                break;
        }
        if (!didDisplay) {
            System.out.println("No record can be displayed.");
            return didDisplay;
        }

        System.out.println("================================= Confirmed Booking ===================================");
        System.out.println("|| ID || Date       || Facility/Equipment             || Time          || Status     ||");
        System.out.println("=======================================================================================");

        int currIndex = 1;
        if (displayType == 2 || displayType == 1) {
            for (FacilityBookRecord facilityBookDetails: confirmedFacilityBookRecords) {

                System.out.printf("|| %-2d || %-10s || %-30s || %02d:00 ~ %02d:00 || %-10s ||\n",
                currIndex++,
                facilityBookDetails.getDate(),
                facilityBookDetails.getSportFacility().getName(),
                //facilityBookDetails.getSportFacility().getSportFacilityType().getFacilityTypeName(),
                facilityBookDetails.getStartHour(),
                facilityBookDetails.getEndHour(),
                facilityBookDetails.getStatus()
                );
                // System.out.println("===================================================================================");
            }
        }

        if (displayType == 2 || displayType == 0) {
            for (EquipmentBookRecord equiupmentBookDetails : confirmedEquipmentBookRecords) {
                String time;
                if (equiupmentBookDetails.isSellable()) {time = "N/A";}
                else {
                    time = String.format("%02d:00 ~ %02d:00", 
                    equiupmentBookDetails.getStartHour(), 
                    equiupmentBookDetails.getEndHour());
                }

                System.out.printf("|| %-2d || %-10s || %-30s || %-13s || %-10s ||\n",
                currIndex++,
                equiupmentBookDetails.getDate(),
                equiupmentBookDetails.getBookingEquipment().get(0).getEquipmentName() + " x " + equiupmentBookDetails.getQuantity(),
                time,
                equiupmentBookDetails.getStatus()
                );
                // System.out.println("===================================================================================");
            }
        }
        System.out.println("=======================================================================================");
        return didDisplay;
    }

    /**
     * Retrieves the facility booking record at the specified index.
     *
     * @param index The index of the facility booking record to retrieve.
     * @return The facility booking record at the specified index, or null if not found.
     */
    public FacilityBookRecord getFacilityRecord(int index) {
        //assume no more than one user book same time selection
        FacilityBookRecord result = null;
        if (index > confirmedFacilityBookRecords.size() || confirmedFacilityBookRecords.size() == 0 || index < 0 ) {
			return result;
		}
        result = confirmedFacilityBookRecords.get(index);
        return result;
    }

    /**
     * Retrieves the equipment booking record at the specified index.
     *
     * @param index The index of the equipment booking record to retrieve.
     * @return The equipment booking record at the specified index, or null if not found.
     */
    public EquipmentBookRecord getEquipmentRecord(int index) {
        //assume no more than one user book same time selection
        EquipmentBookRecord result = null;
        if (index > confirmedEquipmentBookRecords.size() || confirmedEquipmentBookRecords.size() == 0 || index < 0) {
        	// would size == 0 here even make no sense...
			return result;
		}
        result = confirmedEquipmentBookRecords.get(index);
        return result;
    }

//     public void updateFacilityRecordTimeDate(FacilityBookRecord facilityBookRecord, LocalDate date, int startHour, int endHour) {
//         //update SportFacility and FacilityBookRecord
//     	facilityBookRecord.setDate(date);
//     	facilityBookRecord.setStartHour(startHour);
//     	facilityBookRecord.setEndHour(endHour);
// //        facilityBookRecord.updateDateTime(date, startHour, endHour);

//     }

//     public void updateEquipmentRecordTimeDate(EquipmentBookRecord equipmentBookRecord, LocalDate date, int startHour, int endHour) {
//         //update Equipment and EquipmentBookRecord
// //        equipmentBookRecord.updateDateTime(date, startHour, endHour);
//         // equipmentBookRecord.setDate(date);
//         // equipmentBookRecord.setStartHour(startHour);
//         // equipmentBookRecord.setEndHour(endHour);

//         equipmentBookingControl.updateBookingDateTime(equipmentBookRecord, date, new int[] {startHour, endHour});
//     }

    /**
     * Updates the facility associated with the given facility booking record.
     *
     * @param facilityBookRecord The facility booking record to update.
     * @param facility The new facility to associate with the booking record.
     */
    public void updateFacilityRecordFacility(FacilityBookRecord facilityBookRecord, SportFacility facility) {
        facilityBookRecord.updateFacility(facility);
    }

    /**
     * Removes the given facility booking record.
     *
     * @param facilityBookRecord The facility booking record to remove.
     */
    public void removeFacilityRecord(FacilityBookRecord facilityBookRecord) {
//        facilityBookRecord.emptyFacilitySlot();
        confirmedFacilityBookRecords.remove(facilityBookRecord);
        fbm.removeBooking(facilityBookRecord);
        facilityBookRecord = null;
    }

    /**
     * Removes the given equipment booking record.
     *
     * @param equipmentBookRecord The equipment booking record to remove.
     */
    public void removeEquipmentRecord(EquipmentBookRecord equipmentBookRecord) {
        confirmedEquipmentBookRecords.remove(equipmentBookRecord);  //some may still want facility holds.
        ebm.removeBooking(equipmentBookRecord);
        equipmentBookRecord = null;
    }

    // public ArrayList<int[]> getFacilityAvailableSlots(SportFacility sportFacility, LocalDate date) {
    //     ArrayList<int[]> availableSlots = facilityBookingControl.getAvailableTimeSlot(sportFacility, date);
    //     return availableSlots;
    // }
    
    // public ArrayList<int[]> getEquipmentAvailableSlots(ArrayList<Equipment> equipments, LocalDate date) {
    //     return equipmentBookingControl.calculateAvailableGapTimeSlot(equipments, date);
    // }
	
	//for testing. 
	/**
     * Retrieves the list of confirmed equipment booking records.
     *
     * @return A list of confirmed equipment booking records.
     */
	public ArrayList<EquipmentBookRecord> getEquipmentList() {
		
		return confirmedEquipmentBookRecords;
	}
	
	/**
     * Retrieves the list of confirmed facility booking records.
     *
     * @return A list of confirmed facility booking records.
     */
	public ArrayList<FacilityBookRecord> getFacilityList() {
		
		return confirmedFacilityBookRecords;
	}

}