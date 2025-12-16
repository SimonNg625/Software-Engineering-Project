package sportapp.model;

/**
 * Represents the status of a booking in the sport management system.
 * <p>
 * This enum defines the possible states a booking can have during its lifecycle.
 */
public enum BookingStatus {

    /**
     * The booking is pending and has not yet been confirmed.
     */
    PENDING,

    /**
     * The booking has been confirmed and is active.
     */
    CONFIRMED,

    /**
     * The booking has ended and is no longer active.
     */
    ENDED
}
