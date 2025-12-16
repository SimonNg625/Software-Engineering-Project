package sportapp.screen;

import java.util.Arrays;
import java.util.Scanner;

import sportapp.Route;
import sportapp.Screen;
import sportapp.User;
import sportapp.model.EquipmentType;
import sportapp.model.FacilityBookRecord;
import sportapp.ui.BorrowEquipmentUI;

/**
 * Represents the screen for borrowing equipment in the sport management system.
 * <p>
 * This class implements the Screen interface and provides functionality for users to
 * borrow equipment associated with a facility booking record.
 */
public class BorrowEquipmentScreen implements Screen {

    /**
     * The scanner for user input.
     */
    private Scanner scanner;

    /**
     * The currently logged-in user.
     */
    private User currentUser;

    /**
     * The UI component for borrowing equipment.
     */
    private BorrowEquipmentUI borrowEquipmentUI;

    /**
     * The facility booking record associated with the equipment borrowing.
     */
    private FacilityBookRecord facilityBookRecord;

    /**
     * Constructs a BorrowEquipmentScreen with the specified facility booking record.
     *
     * @param facilityBookRecord The facility booking record associated with the equipment borrowing.
     */
    public BorrowEquipmentScreen(FacilityBookRecord facilityBookRecord) {
        this.facilityBookRecord = facilityBookRecord;
    }

    /**
     * Displays the BorrowEquipmentScreen to the user.
     *
     * @param scanner The scanner for user input.
     * @param user The currently logged-in user.
     * @return The next route to navigate to.
     */
    @Override
    public Route display(Scanner scanner, User user) {
        this.scanner = scanner;
        this.currentUser = user;
        borrowEquipmentUI = new BorrowEquipmentUI(scanner, currentUser);
        String sportType = facilityBookRecord.getSportFacility().getSportFacilityType().getSportType();

        // Obtain all available equipment and display status table
        boolean hasEquipment = borrowEquipmentUI.showEquipmentStatus(facilityBookRecord, sportType);

        if (hasEquipment) {
            int quantity = -1;
            EquipmentType targetType;

            System.out.println("Please input Equipment ID and Quantity separately.");

            // Ask for equipment Type ID
            targetType = borrowEquipmentUI.inputEquipmentType(sportType);
            if (targetType == null) { return Route.BACK; }

            // Ask for quantity
            quantity = borrowEquipmentUI.inputQuantity(targetType);
            if (quantity == 0) { return Route.BACK; }

            // Do borrow equipment
            borrowEquipmentUI.borrowEquipment(targetType, quantity);
        }

        return selectRoute();
    }

    private Route selectRoute() {
        Route forward = null;
        String option = null;
        String[] correct_option = {"B", "H"};

        System.out.println("Do you want to go back Book Equipment Page [B] or Home Page [H]?");
        do {
            option = scanner.nextLine();
            option = option.toUpperCase();

            switch (option) {
                case "B":
                    forward = Route.BACK;
                    break;
                case "H":
                    forward = Route.HOME;
                    break;
            }
        } while (!(Arrays.asList(correct_option).contains(option)));
        return forward;
    }
}
