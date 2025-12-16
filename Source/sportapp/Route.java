package sportapp;

/**
 * Enumeration of application navigation routes / screens.
 */
public enum Route {
    /**
     * Represents the portal screen where users can navigate to various features.
     */
    PORTAL,

    /**
     * Represents the login screen for user authentication.
     */
    LOGIN,

    /**
     * Represents the registration screen for new users.
     */
    REGISTER,

    /**
     * Represents the password reset screen.
     */
    RESET,

    /**
     * Represents the home screen after successful login.
     */
    HOME,

    /**
     * Represents the facility booking screen.
     */
    FACILITY_BOOKING,

    /**
     * Represents the screen for booking equipment.
     */
    BOOK_EQUIPMENT,

    /**
     * Represents the screen for borrowing equipment.
     */
    BORROW_EQUIPMENT,

    /**
     * Represents the screen for selling equipment.
     */
    SELL_EQUIPMENT,

    /**
     * Represents the screen for viewing current bookings.
     */
    CURRENT_BOOKINGS,

    /**
     * Represents the screen for viewing confirmed bookings.
     */
    CONFIRMED_BOOKINGS,

    /**
     * Represents the payment screen for transactions.
     */
    PAYMENT,

    /**
     * Represents the logout action.
     */
    LOGOUT,

    /**
     * Represents the exit action to close the application.
     */
    EXIT,

    /**
     * Represents the action to navigate back to the previous screen.
     */
    BACK

}
