package sportapp.ui;

import java.util.*;
// import sportapp.*;
// import sportapp.manager.EquipmentBookManager;
// import sportapp.model.*;

import sportapp.PaymentCalculator;
import sportapp.Route;
import sportapp.Screen;
import sportapp.User;
import sportapp.ViewCurrentBookingControl;
import sportapp.model.BookingStatus;
import sportapp.model.EquipmentBookRecord;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;

// import sportapp.membership.*;

// import java.lang.reflect.Member;
import java.time.LocalDate;

/**
 * The ViewCurrentBookingUI class is responsible for displaying the current bookings to the user.
 * <p>
 * This class provides methods to display booking details and interact with the user interface
 * for managing and viewing current bookings.
 */
public class ViewCurrentBookingUI implements Screen {

    /**
     * Controller for managing current bookings.
     */
    private ViewCurrentBookingControl controller;

    /**
     * Scanner for user input.
     */
    private Scanner scanner;

    /**
     * The currently logged-in user.
     */
    private User currentUser;

    /**
     * Constructs a ViewCurrentBookingUI with the specified scanner and user.
     *
     * @param scanner The scanner for user input.
     * @param currentUser The currently logged-in user.
     */
    public ViewCurrentBookingUI(Scanner scanner, User currentUser) {
        this.controller = new ViewCurrentBookingControl();
        this.scanner = scanner;
        this.currentUser = currentUser;
    }

    /**
     * Displays the current booking interface to the user.
     *
     * @param scanner The scanner for user input.
     * @param user The currently logged-in user.
     * @return The next route to navigate to based on user input.
     */
    @Override
    public Route display(Scanner scanner, User user) {
        this.scanner = scanner;
        this.currentUser = user;

        int choice;
        do {
            try {
                boolean isContinue = displayCurrentBooking();
                if (!isContinue) {
                    break;
                }

                showBookingOptions();
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        handleUpdateDateTime();
                        break;
                    case 2:
                        handleUpdateFacility();
                        break;
                    case 3:
                        handleCancelBooking();
                        break;
                    case 4:
                        Route paymentResult = PaymentCalculator.Pay(controller, scanner, currentUser.getMembership(), currentUser);
                        if (paymentResult == Route.PAYMENT) {
                            return Route.PAYMENT;
                        }
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number corresponding to the options.");
                scanner.nextLine();
                choice = 0;
            }
        } while (choice != 5);
        System.out.println("Now jump back to Home page.");
        return Route.HOME;
    }

    /**
     * Displays the current bookings to the user.
     *
     * @return True if the user chooses to continue, false otherwise.
     */
    private boolean displayCurrentBooking() {
        ArrayList<FacilityBookRecord> facilitybookings = controller.getPendingFacilityBookingRecord(currentUser);
        ArrayList<EquipmentBookRecord> equipmentBookings = controller.getPendingEquipmentBookingRecord(currentUser);
        
        int index = 0;
        double totalPrice = 0;
        if ((facilitybookings == null || facilitybookings.isEmpty()) && (equipmentBookings == null || equipmentBookings.isEmpty())) {
            System.out.println("No current booking found.");
            return false;
        }

        System.out.println("================================ Current Booking (Shopping Cart) ==================================================");
        System.out.println("|| No. || Date        || Facility/Equipment                  || Time           || Status        || Price         ||");
        System.out.println("===================================================================================================================");
        
        for (int i = 0; i < facilitybookings.size(); i++) {
            FacilityBookRecord record = facilitybookings.get(i);
            double price = record.getTotalPrice();
            totalPrice += price;
            System.out.printf("|| %-3s || %-11s || %-35s || %-14s || %-14s|| %-14s||%n", 
                ++index,
                record.getDate(), 
                record.getSportFacility().getName(), 
                String.format("%02d:00 - %02d:00", record.getStartHour(), record.getEndHour()), 
                record.getStatus().name(),
                price);
        }

        for(int i = 0; i < equipmentBookings.size(); i++){
            EquipmentBookRecord record = equipmentBookings.get(i);
            if (record.getStatus() != BookingStatus.PENDING) {
                continue;
            }

            String time;
            double price = record.getTotalPrice();
            totalPrice += price;

            if (record.isBorrowable()) {
                time = String.format("%02d:00 - %02d:00", record.getStartHour(), record.getEndHour());
            } else {
                time = "N/A";
            }

            System.out.printf("|| %-3s || %-11s || %-35s || %-14s || %-14s|| %-14s||%n", 
                ++index,
                record.getDate(), 
                String.format("%s x %d", record.getBookingEquipment().get(0).getEquipmentName(), record.getQuantity()), 
                time,
                record.getStatus().name(),
                price);
        }
        System.out.println("===================================================================================================================");
        System.out.println(String.format("||                                                                                   Total price: %-5s          ||", totalPrice));
        System.out.println("===================================================================================================================");
        return true;
    }

    /**
     * Displays the booking options to the user.
     */
    private void showBookingOptions() {
        System.out.println("\nBooking Options:");
        System.out.println("1. Update date and time");
        System.out.println("2. Update facility");
        System.out.println("3. Cancel booking");
        System.out.println("4. Proceed to payment");
        System.out.println("5. Return to main menu");
        System.out.print("Please select an option (1-5): ");
    }

    // Wrapper class to handle both FacilityBookRecord and EquipmentBookRecord
    private static class BookRecordWrapper {
        private FacilityBookRecord facilityBooking;
        private EquipmentBookRecord equipmentBooking;
        
        public BookRecordWrapper(FacilityBookRecord booking) {
            this.facilityBooking = booking;
            this.equipmentBooking = null;
        }
        
        public BookRecordWrapper(EquipmentBookRecord booking) {
            this.equipmentBooking = booking;
            this.facilityBooking = null;
        }
        
        public boolean isFacility() {
            return facilityBooking != null;
        }

        public boolean isEquipment() {
            return equipmentBooking != null;
        }
        
        public FacilityBookRecord getFacility() {
            return facilityBooking;
        }
        
        public EquipmentBookRecord getEquipment() {
            return equipmentBooking;
        }
    }

    /**
     * Handles the process of updating the date and time of a booking.
     */
    private void handleUpdateDateTime() {
        BookRecordWrapper selection = selectBooking();

        if (selection == null) throw new IllegalStateException("No booking selected.");

        if (selection.isFacility()) {
            FacilityBookRecord booking = selection.getFacility();
            if (booking.getDate().isEqual(LocalDate.now())) {
                System.out.println("You can only cancel today's Booking");
                return;
            }
        }else if (selection.isEquipment()) {
            EquipmentBookRecord booking = selection.getEquipment();
            if (booking.getDate().isEqual(LocalDate.now())) {
                System.out.println("You can only cancel today's Booking");
                return;
            }
        }

        int[] timeSlot;
        LocalDate date = inputDate();
        if (selection.isFacility()) {
            FacilityBookRecord booking = selection.getFacility();
            ArrayList<int[]> slots = controller.getAvailableTimeSlots(booking.getSportFacility(), date);
            timeSlot = selectTimeSlot(slots, date);
        }
        else if (selection.getEquipment().isBorrowable()) {
            EquipmentBookRecord booking = selection.getEquipment();
            ArrayList<int[]> slots = controller.getAvailableTimeSlots(booking.getBookingEquipment(), date);
            timeSlot = selectTimeSlot(slots, date);
        } else {
            ArrayList<int[]> slots = new ArrayList<>();
            slots.add(new int[] {9, 21});
            timeSlot = selectTimeSlot(slots, date);
        }
        
        
        if (timeSlot == null) {
            System.out.println("No available time slots.");
            return;
        }

        boolean updateSuccess;
        if (selection.isFacility()) {
            FacilityBookRecord booking = selection.getFacility();
            updateSuccess = controller.updateBookingDateTime(booking, date, timeSlot[0], timeSlot[1]);
        } else {
            EquipmentBookRecord booking = selection.getEquipment();
            updateSuccess = controller.updateBookingDateTime(booking, date, timeSlot[0], timeSlot[1]);
        }

        if (updateSuccess) {
            System.out.println("Booking updated successfully!");
        } else {
            System.out.println("Failed to update booking.");
        } 
    }

    /**
     * Handles the process of updating the facility of a booking.
     */
    private void handleUpdateFacility() {
        BookRecordWrapper selection = selectBooking();
        if (selection == null) return;
        
        if (!selection.isFacility()) {
            System.out.println("Cannot update facility for equipment bookings.");
            return;
        }
        
        FacilityBookRecord booking = selection.getFacility();
        SportFacility facility = selectFacility();
        if (facility == null) return;
        
        LocalDate date = inputDate();
        ArrayList<int[]> slots = controller.getAvailableTimeSlots(facility, date);
        int[] timeSlot = selectTimeSlot(slots, date);
        
        if (timeSlot == null) {
            System.out.println("No available time slots.");
            return;
        }

        if (controller.updateBookingFacility(booking, facility, date, timeSlot[0], timeSlot[1])) {
            System.out.println("Facility updated successfully!");
        } else {
            System.out.println("Failed to update facility.");
        }
    }

    /**
     * Handles the process of canceling a booking.
     */
    private void handleCancelBooking() {
        BookRecordWrapper selection = selectBooking();
        if (selection == null) return;
        
        System.out.print("Are you sure you want to cancel this booking? (Y/N): ");
        String response = scanner.nextLine().trim();
        
        if (response.equalsIgnoreCase("Y")) {
            if (selection.isFacility()) {
                controller.cancelBooking(selection.getFacility());
            } else {
                controller.cancelBooking(selection.getEquipment());
            }
            System.out.println("Booking cancelled successfully!");
        } else {
            System.out.println("Cancellation aborted.");
        }
    }

    private BookRecordWrapper selectBooking() {
        ArrayList<FacilityBookRecord> facilityBookings = controller.getPendingFacilityBookingRecord(currentUser);
        ArrayList<EquipmentBookRecord> equipmentBookingsPending = controller.getPendingEquipmentBookingRecord(currentUser);

        if (facilityBookings.isEmpty() && equipmentBookingsPending.isEmpty()) {
            System.out.println("No booking to select.");
            return null;
        }
        
        int totalSize = facilityBookings.size() + equipmentBookingsPending.size();
        
        while (true) {
            System.out.print("Select a booking (1-" + totalSize + "): ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                if (choice > 0 && choice <= facilityBookings.size()) {
                    return new BookRecordWrapper(facilityBookings.get(choice - 1));
                } else if (choice > facilityBookings.size() && choice <= totalSize) {
                    return new BookRecordWrapper(equipmentBookingsPending.get(choice - facilityBookings.size() - 1));
                }
                
                System.out.println("Invalid selection. Please try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private SportFacility selectFacility() {
        ArrayList<SportFacility> facilities = controller.getAvailableFacilities();
        
        System.out.println("================= Available Sport Facilities ==================");
        System.out.println("|| No. || Facility Name         || Type                    ||");
        System.out.println("============================================================");
        for (int i = 0; i < facilities.size(); i++) {
            SportFacility facility = facilities.get(i);
            System.out.printf("||%-4s ||%-21s ||%-23s||%n", 
                (i + 1), 
                facility.getName(), 
                facility.getSportFacilityType().getSportType());
        }
        System.out.println("============================================================");
        
        while (true) {
            System.out.print("Select a facility (1-" + facilities.size() + "): ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice > 0 && choice <= facilities.size()) {
                    return facilities.get(choice - 1);
                }
                System.out.println("Invalid selection. Please try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }
    
    private LocalDate inputDate() {
        while (true) {
            System.out.print("Enter booking date (yyyy-MM-dd): ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Date cannot be empty. Please try again.");
                continue;
            }
            
            try {
                LocalDate date = LocalDate.parse(input);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Date must be in the future. Please try again.");
                    continue;
                }
                return date;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }
    }

    private int[] selectTimeSlot(ArrayList<int[]> slots, LocalDate date) {
        if (slots.isEmpty()) {
            return null;
        }

        System.out.println("Available Time Slots:");
        for (int i = 0; i < slots.size(); i++) {
            int[] slot = slots.get(i);
            System.out.printf("%d. %02d:00 - %02d:00%n", i + 1, slot[0], slot[1]);
        }

        while (true) {
            try {
                System.out.print("Select the start hour (24-hour format): ");
                int startHour = scanner.nextInt();
                System.out.print("Select the end hour (24-hour format): ");
                int endHour = scanner.nextInt();
                scanner.nextLine();

                if (startHour >= endHour) {
                    System.out.println("End time must be after start time. Please try again.");
                    continue;
                } else {
                    for (int[] slot: slots) {
                        if (slot[1] < startHour) {
                            continue;
                        } else if ((startHour < slot[0]) || (slot[1] < endHour)) {
                            System.out.println("Selected time is not within available slots. Please try again.");
                            break;
                        } else {
                            return new int[]{startHour, endHour};
                        }
                    }
                }

                // no need to proof check here 
                // if (controller.isValidTimeSlot(facility, date, startHour, endHour)) {
                //     return new int[]{startHour, endHour};
                // }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter numbers only.");
                scanner.nextLine();
            }
        }
    }
    
}
