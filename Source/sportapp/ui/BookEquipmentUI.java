package sportapp.ui;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import sportapp.Clock;
import sportapp.Route;
import sportapp.User;
import sportapp.manager.FacilityBookManager;
import sportapp.model.FacilityBookRecord;

/**
 * Provides the user interface for booking equipment in the sport management system.
 * <p>
 * This class allows users to select a facility booking record and manage equipment bookings
 * associated with that facility.
 */
public class BookEquipmentUI {

    /**
     * Scanner for user input.
     */
    private Scanner scanner;

    /**
     * The currently logged-in user.
     */
    private User currentUser;

    /**
     * Manager for handling facility bookings.
     */
    private FacilityBookManager facilityBookManager;

    /**
     * Constructs a BookEquipmentUI with the specified scanner and user.
     *
     * @param scanner The scanner for user input.
     * @param currentUser The currently logged-in user.
     */
    public BookEquipmentUI(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
        this.facilityBookManager = FacilityBookManager.getInstance();
    }

    /**
     * Allows the user to select a facility booking record.
     *
     * @return The selected facility booking record, or null if no selection is made.
     */
    public FacilityBookRecord selectFacilityBookRecord() {
        FacilityBookRecord facilityBookRecord = null;

        // Select user's facility booking record
        ArrayList<FacilityBookRecord> facilityBookRecords = facilityBookManager.getUserBooking(currentUser);
        if (facilityBookRecords.size() == 0) {
            // No record, throw error and return to home page
            System.out.println("ERROR: You do not have any Facility Booking Record.");
            System.out.println("Please book at least one facility before booking equipments.");
        } else {
            // Have records, display facility records to select.
            int selection = selectFacilityRecordIndex(facilityBookRecords);

            if (selection == -1) {
                return facilityBookRecord;
            }
            facilityBookRecord = facilityBookRecords.get(selection);
        }
        return facilityBookRecord;
    }

    // with parameter, book equipment with facility
    // public void bookEquipment(FacilityBookRecord facilityBookRecord) {
    // 	doBookEquipment(facilityBookRecord);
    // }

    /**
     * Allows the user to select a function for booking equipment.
     *
     * @return The route to navigate to based on the selected function.
     */
    public Route selectBookEquipmentFunction() {
        displayFunctionMenu();
        Route forward_result = selectFunction(); 
        return forward_result;
    }

    private int selectFacilityRecordIndex(ArrayList<FacilityBookRecord> facilityBookRecords) {
        System.out.printf("Booking User: %s\n", currentUser.getUsername());
        int count = 0;
        for (FacilityBookRecord bookRecord: facilityBookRecords) {
            System.out.printf("%d. %s || %s || %s-%s\n", 
                (++count),
                bookRecord.getDate().toString(),
                bookRecord.getSportFacility().getName(),
                bookRecord.getStartHour(),
                bookRecord.getEndHour()
            );
        }

        int select;
        while (true) {
            try {
                if (count == 1) { System.out.print("Please select a booked facility [1] or go back [0]: "); }
                else { System.out.printf("Please select a booked facility [1 ~ %d] or go back [0]: ", count); }
                
                select = Integer.parseInt(scanner.next());
                
                if (select == 0) {
                    scanner.nextLine();
                    break;
                } else if ((select < 0) || (select > facilityBookRecords.size())) {
                    // invaild input, show error
                    System.out.println("Error: Input is incorrect. Please try again.");
                } else if (Clock.getInstance().getToday().isEqual(facilityBookRecords.get(select-1).getDate())) {
                    // invalid date, show error
                    System.out.println("Error: You can not book an equipment for today's booking record.");
                } else {
                    // vaild input, exit the loop
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: Input is incorrect. Please try again.");
        scanner.nextLine(); // Consume the invalid input
            }
        }

        return (select-1);
    }

    private void displayFunctionMenu() {
            System.out.println("\n=== Equipment Rental and Sales ===");
            System.out.println("1. Borrow Equipment");
            System.out.println("2. Buy Equipment");
            System.out.println("H. Go back to Home Page");
            System.out.println("==============================");
    }

    private Route selectFunction() {
        String option;
        String[] correct_options = {"1", "2", "H"};
        Route forward_option = null;

        // displayFunctionMenu();
        do {
            System.out.print("Please select an option (1-2) or go back (H): ");
            
            option = scanner.next();
            option = option.toUpperCase();
            // executeFunction(option.toUpperCase(), facilityBookRecord);

            switch (option) {
                case "1":
                    // borrowEquipment(facilityBookRecord);
                    forward_option = Route.BORROW_EQUIPMENT;
                    break;

                case "2":
                    // sellEquipment(facilityBookRecord);
                    forward_option = Route.SELL_EQUIPMENT;
                    break;

                case "H":
                    forward_option = Route.HOME;
                    break;
                
                default:
                    System.out.println("Error: Incorrect Input. Please input again.\n");
            }
        } while (!(Arrays.asList(correct_options).contains(option)));
        return forward_option;
    }
}