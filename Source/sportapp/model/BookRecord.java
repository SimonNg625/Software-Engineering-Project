package sportapp.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import sportapp.User;

/**
 * Represents a booking record in the sport management system.
 * <p>
 * This abstract class provides the common structure for different types of booking records,
 * including user details, booking date, time, and status.
 */
public abstract class BookRecord {

    /**
     * The user associated with the booking.
     */
    protected User user;

    /**
     * The date of the booking.
     */
    protected LocalDate date;

    /**
     * The start hour of the booking.
     */
    protected int startHour;

    /**
     * The end hour of the booking.
     */
    protected int endHour;

    /**
     * The status of the booking.
     */
    protected BookingStatus status;

    /**
     * Constructs a new booking record with the specified details.
     *
     * @param user The user associated with the booking.
     * @param date The date of the booking.
     * @param startHour The start hour of the booking.
     * @param endHour The end hour of the booking.
     * @param status The status of the booking.
     */
    public BookRecord(User user, LocalDate date, int startHour, int endHour, BookingStatus status) {
        this.user = user;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
        this.status = status;
    }

    /**
     * Retrieves the user associated with the booking.
     *
     * @return The user associated with the booking.
     */
    public User getUser() {
        return user;
    }

    /**
     * Retrieves the date of the booking.
     *
     * @return The date of the booking.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the booking.
     *
     * @param date The new date of the booking.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Retrieves the start hour of the booking.
     *
     * @return The start hour of the booking.
     */
    public int getStartHour() {
        return startHour;
    }

    /**
     * Sets the start hour of the booking.
     *
     * @param startHour The new start hour of the booking.
     */
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    /**
     * Retrieves the end hour of the booking.
     *
     * @return The end hour of the booking.
     */
    public int getEndHour() {
        return endHour;
    }

    /**
     * Sets the end hour of the booking.
     *
     * @param endHour The new end hour of the booking.
     */
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    /**
     * Retrieves the status of the booking.
     *
     * @return The status of the booking.
     */
    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the booking.
     *
     * @param status The new status of the booking.
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    /**
     * Calculates the total price of the booking.
     *
     * @return The total price of the booking.
     */
    public abstract double getTotalPrice();

    /**
     * Returns a string representation of the booking record.
     *
     * @return A string representation of the booking record.
     */
    public String toString() {
        return String.format("User: %s\nDate: %s\nStart: %s\nEnd: %s\nStatus: %s", 
            user, 
            date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            startHour,
            endHour,
            status
        );
    }
}
