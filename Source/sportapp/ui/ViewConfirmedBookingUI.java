package sportapp.ui;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
//<- include Scanner
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import sportapp.Clock;
import sportapp.ConfirmedBookingControl;
import sportapp.Route;
import sportapp.Screen;
import sportapp.User;
import sportapp.manager.ConfirmedBookManager;
import sportapp.manager.SportFacilityManager;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;


/**
 * The ViewConfirmedBookingUI class is responsible for displaying confirmed bookings to the user.
 * <p>
 * This class provides methods to display confirmed booking details and interact with the user interface
 * for managing and viewing confirmed bookings.
 */
public class ViewConfirmedBookingUI implements Screen {

    /**
     * Scanner for user input.
     */
    private Scanner scanner;

    /**
     * Manager for handling confirmed bookings.
     */
    private ConfirmedBookManager cbm;

    /**
     * The currently logged-in user.
     */
    private User currUser;

    /**
     * List of object types for booking options.
     */
    private List<String> objectTypeList = Arrays.asList("Equipment", "Sport Facility");

    /**
     * Enum representing object types for booking.
     */
    public static enum OBJECT_TYPE {
        /**
         * Represents equipment as an object type for booking.
         */
        OBJECT_TYPE_EQUIPMENT,

        /**
         * Represents a sport facility as an object type for booking.
         */
        OBJECT_TYPE_FACILITY
    }

    /**
     * Constructs a ViewConfirmedBookingUI with the default confirmed booking manager.
     */
    public ViewConfirmedBookingUI() {
        this.cbm = ConfirmedBookManager.getInstance();
    }

    /**
     * Sets up the user and scanner for testing purposes.
     *
     * @param scanner The scanner for user input.
     * @param user The currently logged-in user.
     */
    public void setUpTest(Scanner scanner, User user) {	//for testing only
    	this.scanner = scanner;
    	this.currUser = user;
    }

    /**
     * Displays the confirmed booking interface to the user.
     *
     * @param scanner The scanner for user input.
     * @param user The currently logged-in user.
     * @return The next route to navigate to based on user input.
     */
    @Override
    public Route display(Scanner scanner, User user) {


        this.scanner = scanner;
        this.currUser = user;
        cbm.setUpUserCollection(currUser);
        Route result = showBookingOptions();
        return result;
    }


    /**
     * Represents a class for managing date and time information.
     */
	class DateTimeClass {
	    /**
	     * The start hour of the booking.
	     */
	    public int startHour;

	    /**
	     * The end hour of the booking.
	     */
	    public int endHour;

	    /**
	     * The date of the booking.
	     */
	    public LocalDate date;

	    /**
	     * Constructs a DateTimeClass with no initial date.
	     */
	    public DateTimeClass() {date = null;}

	    /**
	     * Constructs a DateTimeClass with specified start and end hours.
	     *
	     * @param a The start hour.
	     * @param b The end hour.
	     */
	    public DateTimeClass(int a, int b) {startHour = a; endHour = b;}
	}


    /**
     * Displays booking options and handles user actions.
     *
     * @return The next route to navigate to based on user actions.
     */
    public Route showBookingOptions() {
        Route forward = null;
        boolean inLoop = true;
        
        while (inLoop) {
            try {
            	cbm.resetUserCollection(currUser);
                if (cbm.isCollectionEmpty()) {
                    System.out.println("The collection of the user is empty. No display can be shown.");
                    return Route.HOME;
                }

            	cbm.displayConfirmedRecord(2);
                System.out.println("Booking Options:");
                System.out.println("1. Update date and time");
                System.out.println("2. Update facility");
                System.out.println("3. Cancel booking");
                System.out.println("4. Return to main menu");
                System.out.print("Please select an option (1-4): ");

                int input = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (input) {    //can update equipment/venue/Datetime thought equipmentID or VenueID
                    case 1:
                        updateDateTime();
                        break;
                    case 2:
                        updateSportFacility();
                        break;
                    case 3:
                        cancelBooking();
                        break;
                    case 4:
                        forward = Route.HOME;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        //showBookingOptions();
                        forward = Route.CONFIRMED_BOOKINGS;
                        break;
                }

                if (forward == Route.HOME) { return forward; }
                
                // System.out.println("Do you want to perform another update operation? (y/n): ");
                // String anotherOperation = scanner.nextLine();
                // if (anotherOperation.toLowerCase().equals("n")) {
                //     forward = Route.HOME;
                // } else {
                //     forward = Route.CONFIRMED_BOOKINGS;
                // }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Invalid Input.");
                scanner.nextLine();
                forward = Route.CONFIRMED_BOOKINGS;
                break;
            }
        }
        return forward;
    }
    

    /**
     * Display Facility menu, then get user selected facility from menu display
     *
     * @return Selected Sport Facility
     * @throws Exception if the update process encounters an error.
     */
    private SportFacility displayFacilityMenu() throws Exception { //"Room Bad101", "Room Bas201", "Room TT301"
        ArrayList<SportFacility> availableFacilities = SportFacilityManager.getInstance().getSportFacilities();
        //System.out.println("Total number of venue: " + availableFacilities.size());
        int currIndex = 1;
        for (SportFacility i : availableFacilities ) {
            System.out.printf("%d. %s\n", currIndex++, i.getName());
        }
        int inputChoice = scanner.nextInt() - 1;
        scanner.nextLine();
        
        SportFacility ChosenFacility= null;

        if (inputChoice > availableFacilities.size() || inputChoice < 0) {
            throw new Exception("Invalid Input");
        }
        ChosenFacility = availableFacilities.get(inputChoice);
        return ChosenFacility;
    }

    /**
     * handle select date time selection
     *
     * @return DateTimeClass of new selected new date time information
     * @throws Exception if the update process encounters an error.
     */
    private DateTimeClass handleSelectNewDateTime(ConfirmedBookingControl confirmedBookingControl) throws Exception{
        System.out.print("Input New Date to Update (dd/MM/yyyy): ");
        scanner.nextLine(); // consume leftover newline //**Check  */
        DateTimeClass newDateTime = new DateTimeClass();
        String inputNewDateString = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        newDateTime.date = LocalDate.parse(inputNewDateString, formatter);

        if (newDateTime.date.compareTo(Clock.getInstance().getToday()) <= 0) { //added fixed bug
            throw new Exception("Input Invalid New Date update");
        }

        //display timeslot of the OrgID
        ArrayList<int[]> availableSlots = confirmedBookingControl.getAvailableTimeSlot(newDateTime.date, true);

        if (availableSlots.isEmpty()) {
            System.out.println("No available time slots for the selected date.");
            return null;
        }


        System.out.print("Input New Start Time to Update: ");
        int inputStTime = scanner.nextInt();
        scanner.nextLine();
            
        System.out.print("Input New End Time to Update: ");
        int inputEndTime = scanner.nextInt();
        scanner.nextLine();
            
        if (inputStTime >= inputEndTime) {
            System.out.println("Invalid Start time and End time.");
            throw new Exception("Invalid Input");
        }

            //check if input time is in available slot
        boolean foundTimeSlot = false;
        for (int[] slot : availableSlots) {
            if (slot[0] <= inputStTime && slot[1] >= inputEndTime) {
                //found same time slot, thus can update
                foundTimeSlot = true;
                break;
            }
        }
        if (!foundTimeSlot) { 
            if (confirmedBookingControl.currTargetType == OBJECT_TYPE.OBJECT_TYPE_FACILITY)
                throw new Exception("This Sport Facility with this Date/Time can not be found.");
            else
                throw new Exception("This Equipment with this Date/Time can not be found.");
        }

        newDateTime.startHour = inputStTime;
        newDateTime.endHour = inputEndTime;
        
        return newDateTime;
    }

    /**
     * Checks if two DateTimeClass objects represent the same date and time.
     *
     * @param orgDateTime The original date and time.
     * @param newDateTime The new date and time.
     * @return True if the dates and times are the same, false otherwise.
     */
    private boolean isSameDate(DateTimeClass orgDateTime, DateTimeClass newDateTime) {
        boolean sameDate = orgDateTime.date.compareTo(newDateTime.date) == 0;
        boolean sameTimeSection = (orgDateTime.startHour == newDateTime.startHour && orgDateTime.endHour == newDateTime.endHour);
        return sameDate && sameTimeSection;
    }

    /**
     * Updates the date and time of a confirmed booking.
     *
     * @throws Exception if the update process encounters an error.
     */
    public void updateDateTime() throws Exception {
        ConfirmedBookingControl confirmedBookingControl = new ConfirmedBookingControl();
        System.out.print("\nInput which type of booking to update with (1: Equipment, 2: Facility): ");
        confirmedBookingControl.setType(scanner.nextInt() - 1);
        boolean didDisplay = cbm.displayConfirmedRecord(confirmedBookingControl.currTargetType.ordinal());
        if (!didDisplay) 
            return;

        System.out.printf("Choose %s to update with (input integer): ", objectTypeList.get(confirmedBookingControl.currTargetType.ordinal()));
        int updateChoice = scanner.nextInt() - 1;

        confirmedBookingControl.setRecord(updateChoice, "update"); //also has checked if not sellable for equipment

        DateTimeClass orgDateTime = new DateTimeClass();
       
        orgDateTime.startHour = confirmedBookingControl.getStartHour();
        orgDateTime.endHour = confirmedBookingControl.getEndHour();
        orgDateTime.date = confirmedBookingControl.getDate();
        
        
        DateTimeClass newDateTime = handleSelectNewDateTime(confirmedBookingControl);

        if (newDateTime == null)
            return;

        if (isSameDate(orgDateTime, newDateTime)) {
            throw new Exception("No changes detected in date/time.");
        } else {
            confirmedBookingControl.updateRecordDateTime(newDateTime.date, newDateTime.startHour, newDateTime.endHour);
        }
    }

    /**
     * Updates the sport facility of a confirmed booking.
     *
     * @throws Exception if the update process encounters an error.
     */
    public void updateSportFacility() throws Exception {
        ConfirmedBookingControl confirmedBookingControl = new ConfirmedBookingControl();
        confirmedBookingControl.setType(1);

        boolean didDisplay = cbm.displayConfirmedRecord(OBJECT_TYPE.OBJECT_TYPE_FACILITY.ordinal());
        if (!didDisplay) 
            return;
        
        System.out.print("\nChoose Facility to update with (input integer): ");
        int updateChoice = scanner.nextInt() - 1;
        scanner.nextLine();

        //FacilityBookRecord facilityBookRecord = cbm.getFacilityRecord(updateChoice);
        confirmedBookingControl.setRecord(updateChoice, "update");
        // if (facilityBookRecord == null) {
        //     throw new Exception("No Chosen Record with Input Found!");
        // }
        // if (facilityBookRecord.getDate().equals(Clock.getInstance().getToday())) {
        //     System.out.println("You can not update booking from the same day.");
        //     return;
        // }

        DateTimeClass orgDateTime = new DateTimeClass(confirmedBookingControl.facilityRecord.getStartHour(), confirmedBookingControl.facilityRecord.getEndHour());
        orgDateTime.date = confirmedBookingControl.facilityRecord.getDate();

        System.out.println("Input New Sport Facility ID to Update: ");
        SportFacility inputNewFacility = displayFacilityMenu();
        //ArrayList<int[]> availableSlots = confirmedBookingControl.getAvailableTimeSlot(orgDateTime.date, false);
        ArrayList<int[]> availableSlots = confirmedBookingControl.getFacilityAvailableSlots(inputNewFacility, orgDateTime.date);

        //if no available slot, cannot update
        if (availableSlots.isEmpty()) {
            System.out.println("No available time slots for the selected date.");
            return;
        } else { //if no start time && end time found in available slot, then return
            boolean foundTimeSlot = false;
            for (int[] slot : availableSlots) {
                if (slot[0] <= orgDateTime.startHour && slot[1] >= orgDateTime.endHour) {
                    //found same time slot, thus can update
                    foundTimeSlot = true;
                    break;
                }
            }
            if (!foundTimeSlot) {
                System.out.println("This Sport Facility with this Date/Time is already booked.");
                return;
            }

            System.out.println("Update Sport Facility");
            confirmedBookingControl.updateFacilityRecordFacility(inputNewFacility);
        }
    }

    /**
     * Cancels a confirmed booking.
     *
     * @throws Exception if the cancelation process encounters an error.
     */
    public void cancelBooking() throws Exception {
        ConfirmedBookingControl confirmedBookingControl = new ConfirmedBookingControl();

        System.out.print("\nInput which type of booking to update with (1: Equipment, 2: Facility): ");
        int objectType = scanner.nextInt() - 1;
        scanner.nextLine();
        confirmedBookingControl.setType(objectType);


        boolean didDisplay = cbm.displayConfirmedRecord(confirmedBookingControl.currTargetType.ordinal());
        if (!didDisplay) 
            return;
        System.out.printf("Choose %s to update with (input integer): ", objectTypeList.get(objectType));
        int updateChoice = scanner.nextInt() - 1;
        scanner.nextLine();

        confirmedBookingControl.setRecord(updateChoice, "cancel");

        System.out.print("Are you sure to cancel booking even with no refunds (y/n)?");
        String response = scanner.nextLine().toLowerCase();

        if (response.equals("y")) {
            confirmedBookingControl.removeRecord();
            return;
        } else if (response.equals("n")) {
            return;
        } else {
            System.out.println("Invalid response, please try again.");
        }
    }
}