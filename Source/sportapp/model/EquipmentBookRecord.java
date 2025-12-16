package sportapp.model;
import java.time.LocalDate;
import java.util.ArrayList;

import sportapp.User;

/**
 * Represents a booking record for equipment in the sport management system.
 * <p>
 * This class extends the BookRecord class and includes additional details specific to equipment bookings,
 * such as the list of booked equipment and their quantities.
 */
public class EquipmentBookRecord extends BookRecord implements Comparable<EquipmentBookRecord> {

    /**
     * The list of equipment included in the booking.
     */
    private ArrayList<Equipment> bookingEquipments;

    /**
     * The quantity of equipment booked.
     */
    private int quantity;

    /**
     * Constructs an EquipmentBookRecord for testing purposes.
     *
     * @param equipment The equipment being booked.
     * @param user The user making the booking.
     * @param date The date of the booking.
     * @param startTime The start time of the booking.
     * @param endTime The end time of the booking.
     * @param status The status of the booking.
     * @param quantity The quantity of equipment booked.
     */
    public EquipmentBookRecord(Equipment equipment, User user, LocalDate date, int startTime, int endTime, BookingStatus status, int quantity) {
        super(user, date, startTime, endTime, status);
        this.bookingEquipments = new ArrayList<>();
        bookingEquipments.add(equipment);
        this.quantity = quantity;
    }

    /**
     * Constructs an EquipmentBookRecord for testing purposes with multiple equipment items.
     *
     * @param equipments The list of equipment being booked.
     * @param user The user making the booking.
     * @param date The date of the booking.
     * @param startTime The start time of the booking.
     * @param endTime The end time of the booking.
     * @param status The status of the booking.
     */
    public EquipmentBookRecord(ArrayList<Equipment> equipments, User user, LocalDate date, int startTime, int endTime, BookingStatus status) {
        super(user, date, startTime, endTime, status);
        this.bookingEquipments = new ArrayList<>();
        bookingEquipments = equipments;
        this.quantity = equipments.size();
    }

    /**
     * Constructs an EquipmentBookRecord for borrowable equipment bookings.
     *
     * @param facilityBookRecord The associated facility booking record.
     * @param bookingEquipments The list of equipment being booked.
     * @param bookingUser The user making the booking.
     */
    public EquipmentBookRecord(FacilityBookRecord facilityBookRecord, ArrayList<Equipment> bookingEquipments, User bookingUser) {
        super(bookingUser, facilityBookRecord.getDate(), facilityBookRecord.getStartHour(), facilityBookRecord.getEndHour(), BookingStatus.PENDING);
        this.bookingEquipments = bookingEquipments;
        this.quantity = bookingEquipments.size();
    }

    /**
     * Constructs an EquipmentBookRecord for sellable equipment bookings.
     *
     * @param facilityBookRecord The associated facility booking record.
     * @param bookingEquipments The list of equipment being booked.
     * @param bookingUser The user making the booking.
     * @param quantity The quantity of equipment booked.
     */
    public EquipmentBookRecord(FacilityBookRecord facilityBookRecord, ArrayList<Equipment> bookingEquipments, User bookingUser, int quantity) {
        super(bookingUser, facilityBookRecord.getDate(), facilityBookRecord.getStartHour(), facilityBookRecord.getEndHour(), BookingStatus.PENDING);
        this.bookingEquipments = bookingEquipments;
        this.quantity = quantity;
    }

    /**
     * Returns the list of equipment booked.
     *
     * @return The list of equipment.
     */
    public ArrayList<Equipment> getBookingEquipment() {
        return this.bookingEquipments;
    }

    /**
     * Sets the list of equipment to be booked.
     *
     * @param equips The new list of equipment.
     */
    public void setBookingEquipment(ArrayList<Equipment> equips) {
        this.bookingEquipments = equips;
        this.quantity = bookingEquipments.size();
    }

    /**
     * Returns the quantity of equipment booked.
     *
     * @return The quantity of equipment.
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Calculates the total price of the booking.
     *
     * @return The total price.
     */
    public double getTotalPrice() {
        double sum = 0;
        if(this.isBorrowable()){
          for (int i=0; i<this.quantity; i++) {
            sum += bookingEquipments.get(i).getPrice();
          }
        } 
        if(this.isSellable()){
          sum = bookingEquipments.get(0).getPrice() * this.quantity;
        }
        return sum;
    }

    /**
     * Checks if the booking is for borrowable equipment.
     *
     * @return True if the equipment is borrowable, false otherwise.
     */
    public boolean isBorrowable() {
        return this.bookingEquipments.get(0).isBorrowable();
    }

    /**
     * Checks if the booking is for sellable equipment.
     *
     * @return True if the equipment is sellable, false otherwise.
     */
    public boolean isSellable() {
        return this.bookingEquipments.get(0).isSellable();
    }

    /**
     * Returns a string representation of the booking record.
     *
     * @return A string describing the booking record.
     */
    public String toString() {
        return String.format("Equipment Book Record: \nEquipments: %s \nQuantity: %d\n%s",
          bookingEquipments,
          quantity,
          super.toString()
        );
    }

    /**
     * Compares this booking record to another for ordering.
     *
     * @param target The target booking record to compare to.
     * @return A negative integer, zero, or a positive integer as this record is less than, equal to, or greater than the target.
     */
    @Override
    public int compareTo(EquipmentBookRecord target) {
      if (this.date.compareTo(target.date) != 0) {
        return this.date.compareTo(target.date);
      } else {
        return Integer.compare(this.startHour, target.startHour);
      }
    }
}