package sportapp.screen;

import java.util.Arrays;
import java.util.Scanner;

import sportapp.Route;
import sportapp.Screen;
import sportapp.User;
import sportapp.model.EquipmentType;
import sportapp.model.FacilityBookRecord;
import sportapp.ui.SellEquipmentUI;

/**
 * Represents the screen for selling equipment in the sport management system.
 * <p>
 * This class implements the Screen interface and provides functionality for users
 * to sell equipment associated with a specific sport facility.
 */
public class SellEquipmentScreen implements Screen {

    /**
     * Scanner for user input.
     */
    private Scanner scanner;

    /**
     * The currently logged-in user.
     */
    private User currentUser;

    /**
     * UI component for selling equipment.
     */
    private SellEquipmentUI sellEquipmentUI;

    /**
     * The facility book record associated with the equipment.
     */
    private FacilityBookRecord facilityBookRecord;

    /**
     * Constructs a SellEquipmentScreen with the specified facility book record.
     *
     * @param facilityBookRecord The facility book record associated with the equipment.
     */
    public SellEquipmentScreen(FacilityBookRecord facilityBookRecord) {
        this.facilityBookRecord = facilityBookRecord;
    }

    /**
     * Displays the SellEquipmentScreen to the user.
     *
     * @param scanner The scanner for user input.
     * @param user The currently logged-in user.
     * @return The next route to navigate to based on user input.
     */
    @Override
    public Route display(Scanner scanner, User user) {
        this.scanner = scanner;
        this.currentUser = user;
        sellEquipmentUI = new SellEquipmentUI(scanner, currentUser);
        String sportType = facilityBookRecord.getSportFacility().getSportFacilityType().getSportType();

        // Obtain all available equipments and display status table
        boolean hasEquipment = sellEquipmentUI.showEquipmentStatus(facilityBookRecord, sportType);

        if (hasEquipment) {
            int quantity = -1;
            EquipmentType targetType;

            System.out.println("Please input Equipment ID and Quantity separately.");

            // Ask for equipment Type ID 
            targetType = sellEquipmentUI.inputEquipmentType(sportType);
            if (targetType == null) { return Route.BACK; }

            // Ask for quantity
            quantity = sellEquipmentUI.inputQuantity(targetType);
            if (quantity == 0) { return Route.BACK; }

            // Do borrow equipment
            sellEquipmentUI.sellEquipment(targetType, quantity);
        }

        return selectRoute();
    }

    /**
     * Selects the next route based on user input.
     *
     * @return The next route to navigate to.
     */
    private Route selectRoute() {
        Route forward = null;
        String option = null;
        String[] correct_option = {"B", "H"};

        System.out.println("Do you want to go back Book Equipment Page [B] or Home Page [H]?");
        do {
            option = scanner.next();
            option = option.toUpperCase();

            switch (option.toUpperCase()) {
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
