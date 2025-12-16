package sportapp.manager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sportapp.User;
import sportapp.model.Equipment;
import sportapp.model.EquipmentBookRecord;

/**
 * Manages booking records for equipment.
 * <p>
 * This singleton class provides methods to add, remove, and retrieve booking records
 * for equipment, ensuring centralized management and sorting of records.
 */
public class EquipmentBookManager {

    /**
     * Singleton instance of EquipmentBookManager.
     */
    private static EquipmentBookManager instance;

    /**
     * List of equipment booking records.
     */
    private ArrayList<EquipmentBookRecord> bookRecords;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private EquipmentBookManager() {
        bookRecords = new ArrayList<>();
    }

    /**
     * Retrieves the singleton instance of EquipmentBookManager.
     *
     * @return The singleton instance of EquipmentBookManager.
     */
    public static EquipmentBookManager getInstance() {
        if (instance == null) {
            instance = new EquipmentBookManager();
        }
        return instance;
    }

    /**
     * Adds a booking record to the collection and sorts the records.
     *
     * @param record The booking record to add.
     */
    public void addBookRecord(EquipmentBookRecord record) {
        bookRecords.add(record);
        sortCollection();
    }

    /**
     * Removes a booking record from the collection.
     *
     * @param record The booking record to remove.
     */
    public void removeBooking(EquipmentBookRecord record) {
        bookRecords.remove(record);
    }

    /**
     * Retrieves all equipment booking records.
     *
     * @return A list of all equipment booking records.
     */
    public ArrayList<EquipmentBookRecord> getBookRecords() {
        return this.bookRecords;
    }

    /**
     * Retrieves booking records for a specific user.
     *
     * @param user The user whose booking records are to be retrieved.
     * @return A list of booking records for the user.
     */
    public ArrayList<EquipmentBookRecord> getBookingRecordsByUser(User user) {
        ArrayList<EquipmentBookRecord> result = new ArrayList<>();
        for (EquipmentBookRecord bookRecord : bookRecords ) {
          if (bookRecord.getUser().equals(user))
            result.add(bookRecord);
        }

        return result;
    }

    /**
     * Retrieves booking records for a specific equipment and date.
     *
     * @param target The equipment whose booking records are to be retrieved.
     * @param date The date of the booking records to retrieve.
     * @return A list of booking records for the equipment and date.
     */
    public ArrayList<EquipmentBookRecord> getBookRecordByDate(Equipment target, LocalDate date) {
        ArrayList<EquipmentBookRecord> result = new ArrayList<>();
        for (EquipmentBookRecord equipBookRecord: bookRecords) {
          if ((equipBookRecord.getBookingEquipment().equals(target)) && (equipBookRecord.getDate().equals(date))) {
            result.add(equipBookRecord);
          }
        }

        return result;
    }

    // public ArrayList<int[]> getAvailableTimeSlot(Equipment target, LocalDate date) {
    //   ArrayList<int[]> availableTimeSlot = new ArrayList<>();

    //   // get the existing booking of the facility in the day
    //   ArrayList<EquipmentBookRecord> equipmentBookings = getBookRecordByDate(target, date);

    //   // sort the booking records by the start time
    //   Collections.sort(equipmentBookings, Comparator.comparingInt(EquipmentBookRecord::getStartHour));

    //   // if there are no bookings, return the whole day as available
    //   availableTimeSlot.add(new int[] {9, 21});

    //   for (EquipmentBookRecord currentBookRecord: equipmentBookings) {
    //     int[] record = {currentBookRecord.getStartHour(), currentBookRecord.getEndHour()};
    //     for (int[] avail: availableTimeSlot) {
    //       if (avail[0] == record[0]) {
    //         availableTimeSlot.remove(avail);
    //         availableTimeSlot.add(new int[] {record[1], avail[1]});
    //         break;
            
    //       } else if (record[1] == avail[1]) {
    //         availableTimeSlot.remove(avail);
    //         availableTimeSlot.add(new int[] {avail[0], record[0]});
    //         break;

    //       } else if ((avail[0] < record[0]) && (record[1] < avail[1])) {
    //         availableTimeSlot.remove(avail);
    //         availableTimeSlot.add(new int[] {avail[0], record[0]});
    //         availableTimeSlot.add(new int[] {record[1], avail[1]});
    //         break;
    //       }
    //     }
    //   }
    //   return availableTimeSlot;
    // }

    /**
     * Sorts the collection of booking records.
     */
    public void sortCollection() {
        Collections.sort(bookRecords);
    }

    /**
     * Resets the manager by clearing all booking records.
     */
    public void reset() {
        bookRecords.clear();
    }
}