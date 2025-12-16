package sportapp.ui;

import java.util.ArrayList;
import java.util.Scanner;

import sportapp.User;
// import sportapp.*;
import sportapp.manager.EquipmentBookManager;
import sportapp.manager.EquipmentManager;
import sportapp.manager.EquipmentTypeManager;
import sportapp.model.Equipment;
import sportapp.model.EquipmentBookRecord;
import sportapp.model.EquipmentType;
import sportapp.model.FacilityBookRecord;

/**
 * Provides the user interface for selling equipment in the sport management system.
 * <p>
 * This class allows users to view available equipment, input equipment details,
 * and manage the selling process.
 */
public class SellEquipmentUI {

    /**
     * Scanner for user input.
     */
    private Scanner scanner;

    /**
     * The currently logged-in user.
     */
    private User currentUser;

    /**
     * The facility booking record associated with the equipment.
     */
    private FacilityBookRecord facilityBookRecord;

    /**
     * Manager for handling equipment data.
     */
    private EquipmentManager equipmentCollection;

    /**
     * Manager for handling equipment types.
     */
    private EquipmentTypeManager typeCollection;

    /**
     * Manager for handling equipment booking records.
     */
    private EquipmentBookManager bookRecordCollection;

    /**
     * Constructs a SellEquipmentUI with the specified scanner and user.
     *
     * @param scanner The scanner for user input.
     * @param currentUser The currently logged-in user.
     */
    public SellEquipmentUI(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;
        this.equipmentCollection = EquipmentManager.getInstance();
        this.typeCollection = EquipmentTypeManager.getInstance();
        this.bookRecordCollection = EquipmentBookManager.getInstance();
    }

    /**
     * Displays the equipment status for the specified facility and sport type.
     *
     * @param facilityBookRecord The facility booking record associated with the equipment.
     * @param sportType The type of sport associated with the equipment.
     * @return True if there are sellable equipment types, false otherwise.
     */
    public boolean showEquipmentStatus(FacilityBookRecord facilityBookRecord, String sportType) {
        this.facilityBookRecord = facilityBookRecord;

        // Display Equipment Table
        ArrayList<EquipmentType> types = typeCollection.getSellableTypesBySportType(sportType);
        if (types.isEmpty()) {
            System.out.printf("There are no sellable equipments for %s Court.\n", sportType);
            return false;
        } else {
            displayEquipmentTable(types);
            return true;
        }
    }

    /**
     * Prompts the user to input an equipment type ID.
     *
     * @param sportType The type of sport associated with the equipment.
     * @return The selected equipment type, or null if the user chooses to go back.
     */
    public EquipmentType inputEquipmentType(String sportType) {
        String input;
        EquipmentType targetType = null;

        do {
            System.out.print("Please input Equipment Type ID (e.g ET-001): ");
            input = scanner.next().toUpperCase();

            // Find target equipment
            targetType = typeCollection.getEquipmentTypeByID(input);

            if (input.equals("B")) {
                break;
            } else if ((targetType == null) || !(targetType.getSportType().equals(sportType))) {
                System.out.println("ERROR: Equipment Type ID is incorrect.");
                System.out.println("Please try again or input [B] go back.\n");
            }
        } while (((targetType == null) || !(targetType.getSportType().equals(sportType))) && (input != "B"));

        return targetType;
    }

    /**
     * Prompts the user to input the quantity of equipment to sell.
     *
     * @param targetType The type of equipment to sell.
     * @return The quantity of equipment to sell, or 0 if the user chooses to go back.
     */
    public int inputQuantity(EquipmentType targetType) {
        int quantity = -1;
        while (true) {
            try {
                System.out.println("Please input the quantity or input [0] go back: ");
                quantity = Integer.parseInt(scanner.next());

                if (quantity == 0) {
                    return 0;
                } else if (quantity < 0) {
                    System.out.println("Error: Input is incorrect. Please try again.\n");
                } else if (quantity > 50) {
                    System.out.println("Error: You can only buy at most 50 equipments at once.\n");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Input is incorrect. Please try again.\n");
                scanner.nextLine(); // Consume the invalid input
            }
        }
        return quantity;
    }

    /**
     * Processes the selling of equipment.
     *
     * @param targetType The type of equipment to sell.
     * @param quantity The quantity of equipment to sell.
     */
    public void sellEquipment(EquipmentType targetType, int quantity) {
        ArrayList<Equipment> targetEquips = equipmentCollection.getSellableEquipmentByType(targetType);

        EquipmentBookRecord record = new EquipmentBookRecord(
            facilityBookRecord, 
            targetEquips, 
            currentUser, 
            quantity
        );

        bookRecordCollection.addBookRecord(record);

        System.out.printf("%s %s x %d putted into pending list successfully.\n", 
            targetEquips.get(0).getEquipmentID(), 
            targetEquips.get(0).getEquipmentName(), 
            quantity
        );
    }

    /**
     * Displays the equipment table for the specified equipment types.
     *
     * @param types The list of equipment types to display.
     */
    private void displayEquipmentTable(ArrayList<EquipmentType> types) {
        System.out.println("\n==================== Equipment Sales =====================");
        System.out.printf("|| %-6s || %-30s || %-8s ||\n", 
            "ID",
            "Equipment Type",
            "Price"
        );
        System.out.println("==========================================================");

        for (EquipmentType type : types) {
            System.out.printf("|| %-6s || %-30s || $%.0f/pack ||\n", 
                type.getEquipmentTypeID(), 
                type.getEquipmentTypeName(), 
                type.getPrice()
            );
        }
        System.out.println("==========================================================");
    }
}