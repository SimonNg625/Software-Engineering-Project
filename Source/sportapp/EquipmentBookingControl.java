package sportapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import sportapp.manager.EquipmentBookManager;
import sportapp.manager.EquipmentManager;
import sportapp.model.BookingStatus;
import sportapp.model.Equipment;
import sportapp.model.EquipmentBookRecord;
import sportapp.model.EquipmentType;

/**
 * Controls the booking operations for equipment.
 * <p>
 * This class provides methods to manage and retrieve equipment booking records,
 * check availability, and handle user-specific bookings.
 */
public class EquipmentBookingControl {

    /**
     * Manages the collection of equipment booking records.
     */
    private EquipmentBookManager equipmentBookManager;

    /**
     * Manages the list of equipment.
     */
    private EquipmentManager equipmentManager;

    /**
     * Provides the current time for booking operations.
     */
    private Clock clock;

    /**
     * Constructs an EquipmentBookingControl instance and initializes managers.
     */
    public EquipmentBookingControl() {
        equipmentBookManager = EquipmentBookManager.getInstance();
        equipmentManager = EquipmentManager.getInstance();
        clock = Clock.getInstance();
    }

    /**
     * Retrieves booking records for a specific user.
     *
     * @param user The user whose booking records are to be retrieved.
     * @return A list of booking records for the user.
     */
    public ArrayList<EquipmentBookRecord> getBookingRecordsByUser(User user) {
        ArrayList<EquipmentBookRecord> result = new ArrayList<>();
        for (EquipmentBookRecord bookRecord: equipmentBookManager.getBookRecords()) {
            if (bookRecord.getUser().equals(user))
                result.add(bookRecord);
        }

        return result;
    }

    /**
     * Retrieves booking records for a specific piece of equipment on a given date.
     *
     * @param target The equipment to check bookings for.
     * @param date The date to check bookings on.
     * @return A list of booking records matching the equipment and date.
     */
    public ArrayList<EquipmentBookRecord> getBookRecordByDate(Equipment target, LocalDate date) {
        ArrayList<EquipmentBookRecord> result = new ArrayList<>();
        for (EquipmentBookRecord equipBookRecord: equipmentBookManager.getBookRecords()) {
            if ((equipBookRecord.getBookingEquipment().contains(target)) && (equipBookRecord.getDate().equals(date))) {
                result.add(equipBookRecord);
            }
        }

        return result;
    }

    /**
     * Retrieves pending booking records for a specific user.
     *
     * @param user The user whose pending booking records are to be retrieved.
     * @return A list of pending booking records for the user.
     */
    public ArrayList<EquipmentBookRecord> getPeningBookRecords(User user) {
        ArrayList<EquipmentBookRecord> result = new ArrayList<>();
        for (EquipmentBookRecord equipBookRecord: equipmentBookManager.getBookRecords()) {
            if (equipBookRecord.getStatus() == BookingStatus.PENDING && equipBookRecord.getUser().equals(user)) {
                result.add(equipBookRecord);
            }
        }
        return result;
    }

    /**
     * Updates the date and time of a booking.
     *
     * @param bookRecord The booking record to be updated.
     * @param date The new date for the booking.
     * @param timeslot The new time slot for the booking.
     * @throws IllegalArgumentException if the time slot is invalid or if there is not enough equipment available.
     */
    public void updateBookingDateTime(EquipmentBookRecord bookRecord, LocalDate date, int[] timeslot) throws IllegalArgumentException {
        if ((timeslot[0] < 9) || (timeslot[1] > 21) || (timeslot[0] > timeslot[1])) {
            throw new IllegalArgumentException("Invalid time slot. Please select a time between 09:00 and 21:00.");
        }
        
        if (bookRecord.isBorrowable()) {
            ArrayList<Equipment> targetEquips = new ArrayList<>();
            ArrayList<Equipment> availableEquips = getAvailableEquipments(bookRecord.getBookingEquipment().get(0).getEquipmentType(), date, timeslot);

            if ((availableEquips.isEmpty()) || (availableEquips.size() < bookRecord.getQuantity())) {
                // not enough equipment for update
                throw new IllegalArgumentException("Not enough available equipments in selected time slot.");
            } else {
                // enough equipment for update
                for (int i=0; i<bookRecord.getQuantity(); i++) {
                    targetEquips.add(availableEquips.get(i));
                }
            }
            bookRecord.setBookingEquipment(targetEquips);
        }

        bookRecord.setDate(date);
        bookRecord.setStartHour(timeslot[0]);
        bookRecord.setEndHour(timeslot[1]);
        equipmentBookManager.sortCollection();
    }

    /**
     * Updates the status of bookings based on the current time.
     * <p>
     * This method sets the status of bookings to ENDED if the end time is before the current time.
     * </p>
     */
    public void updateBookingStatus() {
        for (EquipmentBookRecord record: equipmentBookManager.getBookRecords()) {
            if (record.getEndHour() < clock.getHour()) {
                record.setStatus(BookingStatus.ENDED);
            }
        }
    }

    /**
     * Cancels a booking.
     *
     * @param bookRecord The booking record to be canceled.
     */
    public void cancelBooking(EquipmentBookRecord bookRecord) {
        equipmentBookManager.removeBooking(bookRecord);
    }

    /**
     * Retrieves available equipment of a specific type for a given date and time slot.
     *
     * @param type The type of equipment to check availability for.
     * @param date The date to check availability on.
     * @param targetTimeslot The target time slot to check availability in.
     * @return A list of available equipment matching the criteria.
     */
    public ArrayList<Equipment> getAvailableEquipments(EquipmentType type, LocalDate date, int[] targetTimeslot) {
        // all available equipments in the target time slot
        ArrayList<Equipment> availableEquipment = new ArrayList<>();
        
        // get all equipments with the target type
        ArrayList<Equipment> targetEquipments = equipmentManager.getBorrowableEquipmentByType(type);

        // check which equipment is available in the target time slot.
        for (Equipment targetEquipment: targetEquipments) {
            for (int[] timeslot: getAvailableTimeSlot(targetEquipment, date)) {
                if ((timeslot[0] <= targetTimeslot[0]) && (timeslot[1] >= targetTimeslot[1])) {
                    availableEquipment.add(targetEquipment);
                }
            }
        }

        return availableEquipment;
    }

    /**
     * Calculates the available gap time slots for a list of booking equipments on a given date.
     *
     * @param bookingEquipments The list of equipment bookings to calculate available gaps for.
     * @param date The date to calculate available gaps on.
     * @return A list of available gap time slots.
     */
    public ArrayList<int[]> calculateAvailableGapTimeSlot(ArrayList<Equipment> bookingEquipments, LocalDate date) {
        // if(existBooking == null || existBooking.isEmpty()) {
        //   throw new IllegalArgumentException("No existing booking records found.");
        // }

        ArrayList<int[]> availableTimeSlot = new ArrayList<>();
        Map<Integer, ArrayList<Equipment>> availableTimeSlotMapping = new TreeMap<>();
        EquipmentType targetType = bookingEquipments.get(0).getEquipmentType();
        int quantity = bookingEquipments.size();

        for (Equipment equip: equipmentManager.getBorrowableEquipmentByType(targetType)) {
            // for every equipment with target type
            for (int[] targetTimeslot: getAvailableTimeSlot(equip, date)) {
                // extract timeslot: [9, 10], [12, 21] => {9, 12, 13, 14, ..., 20}
                for (int t=targetTimeslot[0]; t<targetTimeslot[1]; t++) {
                    if (!availableTimeSlotMapping.containsKey(t)) {
                        availableTimeSlotMapping.put(t, new ArrayList<Equipment>());
                    }
                    availableTimeSlotMapping.get(t).add(equip);
                }
            }
        }

        for (Map.Entry<Integer, ArrayList<Equipment>> timeslotEntry: availableTimeSlotMapping.entrySet()) {
            int time = timeslotEntry.getKey();
            if (timeslotEntry.getValue().size() >= quantity) {
                if (availableTimeSlot.isEmpty()) {
                    // first timeslot, return arraylist is empty
                    availableTimeSlot.add(new int[] {time, time+1});

                } else {
                    // return arraylist is not empty, do comparison
                    int[] last = availableTimeSlot.getLast();
                    if ((time - last[1]) >= 1) {
                        // time is not continue, add new record
                        availableTimeSlot.add(new int[]{time, time+1});

                    } else if (availableTimeSlotMapping.get(time).containsAll(availableTimeSlotMapping.get(last[0]))) {
                        // equipments is same as last added record timeslot, extends the timeslot
                        availableTimeSlot.getLast()[1] = (time+1);
                    } else if (availableTimeSlotMapping.get(last[0]).containsAll(availableTimeSlotMapping.get(time))) {
                        // equipments is same as last added record timeslot, extends the timeslot
                        availableTimeSlot.getLast()[1] = (time+1);
                    } else {
                        availableTimeSlot.add(new int[]{time, time+1});
                    }
                }
            } else {
                continue;
            }
        }

        return availableTimeSlot;
    }

    /**
     * Retrieves the quantity of available equipments by type for a list of equipment types, date, start hour, and end hour.
     *
     * @param equipmentTypesList The list of equipment types to check availability for.
     * @param date The date to check availability on.
     * @param startHour The start hour of the time slot to check availability in.
     * @param endHour The end hour of the time slot to check availability in.
     * @return A map containing the equipment types and their corresponding available quantities.
     */
    public Map<EquipmentType, Integer> getAvailableEquipmentsQuantityByType(ArrayList<EquipmentType> equipmentTypesList, LocalDate date, int startHour, int endHour) {
        Map<EquipmentType, Integer> availableEquipmentsQuantityByType = new HashMap<>();

        //find all equipment of each type and check their availability in the given time slot
        for(EquipmentType type: equipmentTypesList) {
            //check if the equipmentType is already in the map
            if(!availableEquipmentsQuantityByType.containsKey(type)) {
                availableEquipmentsQuantityByType.put(type, 0);
                continue;
            }
            availableEquipmentsQuantityByType.putIfAbsent(type, 0);

            for (Equipment equipment: equipmentManager.getBorrowableEquipmentByType(type)) {
                ArrayList<int[]> availableTimeSlots = getAvailableTimeSlot(equipment, date);

                for (int[] timeSlot: availableTimeSlots) {
                    if (timeSlot[0] <= startHour && timeSlot[1] >= endHour) {
                        availableEquipmentsQuantityByType.put(type, availableEquipmentsQuantityByType.getOrDefault(type, 0) + 1);
                        break; // No need to check other time slots for this equipment
                    }
                }
            }
        }

        return availableEquipmentsQuantityByType;
    }

    /**
     * Retrieves the available time slots for a specific piece of equipment on a given date.
     *
     * @param target The equipment to check available time slots for.
     * @param date The date to check available time slots on.
     * @return A list of available time slots for the equipment on the date.
     */
    private ArrayList<int[]> getAvailableTimeSlot(Equipment target, LocalDate date) {
        ArrayList<int[]> availableTimeSlot = new ArrayList<>();

        // get the existing booking of the facility in the day
        ArrayList<EquipmentBookRecord> equipmentBookings = getBookRecordByDate(target, date);

        // sort the booking records by the start time
        Collections.sort(equipmentBookings, Comparator.comparingInt(EquipmentBookRecord::getStartHour));

        // if there are no bookings, return the whole day as available
        availableTimeSlot.add(new int[] {9, 21});

        for (EquipmentBookRecord currentBookRecord: equipmentBookings) {
            int[] record = {currentBookRecord.getStartHour(), currentBookRecord.getEndHour()};
            for (int[] avail: availableTimeSlot) {
                if (avail[0] == record[0]) {
                    availableTimeSlot.remove(avail);
                    availableTimeSlot.add(new int[] {record[1], avail[1]});
                    break;
                    
                } else if (record[1] == avail[1]) {
                    availableTimeSlot.remove(avail);
                    availableTimeSlot.add(new int[] {avail[0], record[0]});
                    break;

                } else if ((avail[0] < record[0]) && (record[1] < avail[1])) {
                    availableTimeSlot.remove(avail);
                    availableTimeSlot.add(new int[] {avail[0], record[0]});
                    availableTimeSlot.add(new int[] {record[1], avail[1]});
                    break;
                }
            }
        }
        return availableTimeSlot;
    }
}
