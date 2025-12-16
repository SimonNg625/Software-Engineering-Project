package sportapp;

import java.util.*;
import sportapp.membership.*;
import sportapp.manager.EquipmentBookManager;
import sportapp.model.*;

/**
 * Helper class responsible for handling payment flow for pending bookings.
 * <p>
 * This class calculates totals and applies membership discounts, then
 * confirms bookings when the user accepts payment.
 */
public class PaymentCalculator {

    /**
     * Constructs a PaymentCalculator instance.
     * <p>
     * This constructor initializes the PaymentCalculator object with default values.
     */
    public PaymentCalculator() {
        // Default constructor
    }

    /**
     * Entry point to perform payment for the current user's pending bookings.
     * This method prints the total, prompts the user for confirmation and
     * confirms booking records when payment is accepted.
     *
     * @param controller view controller used to obtain pending bookings
     * @param scanner scanner used to read user input
     * @param membership membership tier of the user (used to compute discount)
     * @param currentUser the user who pays
     * @return next application route after payment attempt
     */
    public static Route Pay(ViewCurrentBookingControl controller, Scanner scanner, MemberShip membership, User currentUser) {
        ArrayList<FacilityBookRecord> facilityBookingsPending = controller.getPendingFacilityBookingRecord(currentUser);
        ArrayList<EquipmentBookRecord> equipmentBookingsPending = controller.getPendingEquipmentBookingRecord(currentUser);

        boolean noFacility = (facilityBookingsPending == null || facilityBookingsPending.isEmpty());
        boolean noEquipment = (equipmentBookingsPending == null || equipmentBookingsPending.isEmpty());

        if (noFacility && noEquipment) {
            System.out.println("No items to pay!");
            return Route.CURRENT_BOOKINGS;
        }

        double totalPrice = 0.0;

        if (!noFacility) {
            for (FacilityBookRecord facilityBookRecord : facilityBookingsPending) {
                if (facilityBookRecord != null) {
                    totalPrice += facilityBookRecord.getTotalPrice();
                }
            }
        }

        if (!noEquipment) {
            for (EquipmentBookRecord equipmentBookRecord : equipmentBookingsPending) {
                if (equipmentBookRecord != null) {
                    totalPrice += equipmentBookRecord.getTotalPrice();
                }
            }
        }

        Double discountPrice = totalPrice * (1 - membership.getDiscountRate());

        System.out.println("\nTotal Price: " + String.format("$%.2f", discountPrice));
        System.out.println("Non-refundable notice: Once paid, this purchase is non-refundable.");
        System.out.println("1. Confirm");
        System.out.println("2. Cancel");
        System.out.print("Input [1/2]: ");

        int response = -1;
        try {
            if (scanner.hasNextInt()) {
                response = scanner.nextInt();
                scanner.nextLine();
            } else {
                String junk = scanner.next();
                System.out.println("Invalid input: " + junk);
            }
        } catch (Exception e) {
            System.err.println("Failed to read input: " + e.getMessage());
        }

        if (response == 1) {
            PaymentCalculator.ComfirmPay(controller, scanner, membership, currentUser);
            return Route.PAYMENT;
        } else if (response == 2) {
            return Route.CURRENT_BOOKINGS;
        } else {
            System.out.println("Cancelled (unrecognized option).");
            return Route.CURRENT_BOOKINGS;
        }
    }

    /**
     * Confirms all pending bookings associated with the user by marking them as CONFIRMED.
     * @param controller booking controller used to obtain pending records
     * @param scanner input scanner (unused here but kept for API symmetry)
     * @param membership membership of the user (unused in confirmation step)
     * @param currentUser user whose bookings will be confirmed
     */
    private static void ComfirmPay(ViewCurrentBookingControl controller, Scanner scanner, MemberShip membership, User currentUser) {
        ArrayList<FacilityBookRecord> facilityBookingsPending = controller.getPendingFacilityBookingRecord(currentUser);
        ArrayList<EquipmentBookRecord> equipmentBookingsPending = controller.getPendingEquipmentBookingRecord(currentUser);

        if (facilityBookingsPending != null) {
            for (FacilityBookRecord facilityBookRecord : facilityBookingsPending) {
                if (facilityBookRecord != null) {
                    facilityBookRecord.setStatus(BookingStatus.CONFIRMED);
                }
            }
        }
        if (equipmentBookingsPending != null) {
            for (EquipmentBookRecord equipmentBookRecord : equipmentBookingsPending) {
                if (equipmentBookRecord != null) {
                    equipmentBookRecord.setStatus(BookingStatus.CONFIRMED);
                }
            }
        }
        return;
    }
}

