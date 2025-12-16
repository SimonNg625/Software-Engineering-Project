package sportapp.ui;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import sportapp.Clock;
import sportapp.FacilityBookingControl;
import sportapp.User;
import sportapp.manager.FacilityBookManager;
import sportapp.manager.SportFacilityManager;
import sportapp.model.BookingStatus;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;
import sportapp.Route;
import sportapp.Screen;

import java.util.ArrayList;

/**
 * Provides the user interface for booking sport facilities in the sport management system.
 * <p>
 * This class allows users to view available facilities, select a facility to book,
 * and manage the booking process.
 */
public class FacilityBookingUI implements Screen {

    /**
     * Scanner for user input.
     */
    private Scanner scanner;

    /**
     * The currently logged-in user.
     */
    private User currentUser;

    /**
     * Manager for handling sport facilities.
     */
    private SportFacilityManager facilityManager;

    /**
     * Manager for handling facility bookings.
     */
    private FacilityBookManager bookingManager;

    /**
     * Controller for managing facility booking operations.
     */
    private FacilityBookingControl facilityBookingControl;

    /**
     * Constructs a FacilityBookingUI with the specified scanner and user.
     *
     * @param scanner The scanner for user input.
     * @param currentUser The currently logged-in user.
     */
    public FacilityBookingUI(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
        this.facilityManager = SportFacilityManager.getInstance();
        this.bookingManager = FacilityBookManager.getInstance();
        this.facilityBookingControl = new FacilityBookingControl();
    }

    /**
     * Displays the facility booking interface to the user.
     *
     * @param scanner The scanner for user input.
     * @param user The currently logged-in user.
     * @return The next route to navigate to based on user input.
     */
    @Override
    public Route display(Scanner scanner, User user) {
        //display and allow useer to select facility to book
        ArrayList<SportFacility> availableFacilities = facilityBookingControl.getAvailableFacilitys();
        int choice;
        this.currentUser = user;
        
        displayFacility(availableFacilities);
        
        while (true) {
            try {
                System.out.print("Select the facility to book (Enter the number[1-3] or back home page[0]): ");
                choice = Integer.parseInt(scanner.nextLine());
                // scanner.nextLine();
                
                if ((choice < 0) || (choice > availableFacilities.size())) {
                    System.out.println("ERROR: invaild input."); 
                } else if (choice == 0) { return Route.HOME; }
                else { break; }

            } catch (NumberFormatException ex) {
                System.out.println("ERROR: invaild input.");
            }
        }

        SportFacility facilityToBook = availableFacilities.get(choice - 1);

        handleBookingForFacility(facilityToBook);
        System.out.print("\nDo you want to book another facility/time slot? (y/n): ");
        String continueBooking = scanner.nextLine();
        if(continueBooking.equalsIgnoreCase("y")){
            return Route.FACILITY_BOOKING;
        }
        return Route.HOME;
    }

    /**
     * Displays the list of available sport facilities to the user.
     *
     * @param availableFacilities The list of available sport facilities.
     */
    private void displayFacility(ArrayList<SportFacility> availableFacilities) {
        System.out.println("User " + currentUser.getUsername() + " is booking facility.");
        System.out.println("================ Available Sport Facilities =================");
        System.out.println("|| No. || Facility Name         || Type                    ||");
        System.out.println("=============================================================");
        for (int i = 0; i < availableFacilities.size(); i++) {
            SportFacility facility = availableFacilities.get(i);
            System.out.printf("|| %-3s || %-21s || %-23s ||%n", 
                (i + 1), 
                facility.getName(), 
                facility.getSportFacilityType().getSportType());
        }
        System.out.println("=============================================================");
    }

    private void handleBookingForFacility(SportFacility facility) {
        LocalDate date = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Loop until a valid date is entered
        while (true) {
            System.out.println("Which day do you want to book a " + facility.getSportFacilityType().getFacilityTypeName() + "? e.g. 22/09/2025");
            String dateString = scanner.nextLine();

            try {
                date = LocalDate.parse(dateString, formatter);
                if (date.isAfter(Clock.getInstance().getToday())) {
                    break;
                } else {
                    System.out.println("Invalid date. You can only make a booking for the day after today.");
                }

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            }
        }

        int startHourInput;
        int endHourInput;

        // Get the available slots once before the loop
        ArrayList<int[]> availableSlots = facilityBookingControl.getAvailableTimeSlot(facility, date);
        
        if (availableSlots.isEmpty()) {
            System.out.println("Sorry, there are no available slot for that day.");
            return;
        }

        System.out.println("Available time slots on " + date + " are:");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.printf("%d . %d:00 - %d:00 \n", (i + 1), availableSlots.get(i)[0], availableSlots.get(i)[1]);
        }

        // System.out.println("number of existing booking" + bookingManager.getBookingRecords().size());
        while (true) {
            try {
                System.out.println("Please input the start hour (e.g 09)");
                startHourInput = scanner.nextInt();

                System.out.println("Please input the end hour (e.g 21)");
                endHourInput = scanner.nextInt();
                scanner.nextLine(); // Consume the trailing newline

                // 1. Check if input is within business hours (9 to 21)
                if (startHourInput < 9 || endHourInput > 21) {
                    System.out.println("Error: Time must be within operating hours (0900 to 2100). Please try again.");
                    continue;
                }

                // 2. Check if start time is before end time
                if (startHourInput >= endHourInput) {
                    System.out.println("Error: Start time must be before the end time. Please try again.");
                    continue;
                }

                // 3. CRUCIAL: Check if the requested slot is actually available
                boolean isSlotAvailable = false;
                for (int[] slot : availableSlots) {
                    if (startHourInput >= slot[0] && endHourInput <= slot[1]) {
                        isSlotAvailable = true;
                        break; // Found a valid slot, no need to check further
                    }
                }

                if (!isSlotAvailable) {
                    System.out.println("Error: The requested time slot is not available. Please select a time from the list.");
                    continue;
                }

                // If all checks pass, exit the loop
                break;

            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for the hour.");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        // --- The rest of your code to create and confirm the booking ---
        FacilityBookRecord newBooking = new FacilityBookRecord(facility, currentUser, date, startHourInput, endHourInput, BookingStatus.PENDING);
        bookingManager.addBooking(newBooking);
        // System.out.println("number of existing booking after " + bookingManager.getBookingRecords().size());
        System.out.println("User: " + currentUser.getUsername());

        System.out.println("Booking confirmed for: " + facility.getName());
        System.out.println("Date: " + date);
        System.out.printf("Time: %d:00 - %d:00\n", startHourInput, endHourInput);
        System.out.println("\nLate return of facility will face penalty!");
    }
}










