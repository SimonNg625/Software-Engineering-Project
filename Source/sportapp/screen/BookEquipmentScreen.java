package sportapp.screen;

import java.util.Scanner;

import sportapp.ui.BookEquipmentUI;
import sportapp.Route;
import sportapp.Screen;
import sportapp.User;
import sportapp.model.FacilityBookRecord;

/**
 * Represents the screen for booking equipment in the sport management system.
 * <p>
 * This class implements the Screen interface and provides functionality for users to
 * select and book equipment associated with a facility booking record.
 */
public class BookEquipmentScreen implements Screen {

    /**
     * The scanner for user input.
     */
    private Scanner scanner;

    /**
     * The currently logged-in user.
     */
    private User currentUser;

    /**
     * The UI component for booking equipment.
     */
    private BookEquipmentUI bookEquipmentUI;

    /**
     * The facility booking record associated with the equipment booking.
     */
    private FacilityBookRecord facilityBookRecord;

    /**
     * Constructs a BookEquipmentScreen instance.
     * <p>
     * This constructor initializes the BookEquipmentScreen object with default values.
     */
    public BookEquipmentScreen() {
        // Default constructor
    }

    /**
     * Displays the BookEquipmentScreen to the user.
     *
     * @param scanner The scanner for user input.
     * @param user The currently logged-in user.
     * @return The next route to navigate to.
     */
    @Override
    public Route display(Scanner scanner, User user) {
        this.scanner = scanner;
        this.currentUser = user;
        this.bookEquipmentUI = new BookEquipmentUI(scanner, user);

        facilityBookRecord = selectFacilityRecord();
        if (facilityBookRecord == null) {
            return Route.HOME;
        }

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        return forwardRoute();
    }

    /**
     * Forwards the user to the appropriate route based on their selection.
     *
     * @return The next route to navigate to.
     */
    protected Route forwardRoute() {
        Route forwarder = null;
        do {
            forwarder = bookEquipmentUI.selectBookEquipmentFunction();
            switch (forwarder) {
                case BORROW_EQUIPMENT:
                    Screen borrowEquipmentScreen = new BorrowEquipmentScreen(facilityBookRecord);
                    forwarder = borrowEquipmentScreen.display(scanner, currentUser);
                    break;
                case SELL_EQUIPMENT:
                    Screen sellEquipmentScreen = new SellEquipmentScreen(facilityBookRecord);
                    forwarder = sellEquipmentScreen.display(scanner, currentUser);
                    break;
                case HOME:
                case BACK:
                case LOGIN:
                case PAYMENT:
                case PORTAL:
                case RESET:
                case FACILITY_BOOKING:
                case BOOK_EQUIPMENT:
                case CURRENT_BOOKINGS:
                case CONFIRMED_BOOKINGS:
                case REGISTER:
                case LOGOUT:
                case EXIT:
                    // Handle other routes if necessary
                    break;
                default:
                    System.out.println("Invalid route selected.");
                    break;
            }
        } while (!(forwarder.equals(Route.HOME)));
        scanner.nextLine();
        return forwarder;
    }

    private FacilityBookRecord selectFacilityRecord() {
        return bookEquipmentUI.selectFacilityBookRecord();
    }
}
