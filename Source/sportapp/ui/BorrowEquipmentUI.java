package sportapp.ui;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import sportapp.EquipmentBookingControl;
import sportapp.User;
import sportapp.manager.EquipmentBookManager;
import sportapp.manager.EquipmentManager;
import sportapp.manager.EquipmentTypeManager;
import sportapp.model.Equipment;
import sportapp.model.EquipmentType;
import sportapp.model.EquipmentBookRecord;
import sportapp.model.FacilityBookRecord;

/**
 * Provides the user interface for borrowing equipment in the sport management system.
 * <p>
 * This class allows users to borrow equipment associated with a specific facility booking record.
 */
public class BorrowEquipmentUI {

    /**
     * Counter for tracking equipment borrowing operations.
     */
    private int count;

    /**
     * Scanner for user input.
     */
    private Scanner scanner;

    /**
     * The currently logged-in user.
     */
    private User currentUser;

    /**
     * The facility booking record associated with the equipment borrowing.
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
     * Controller for managing equipment booking operations.
     */
    private EquipmentBookingControl bookingControll;

    /**
     * Map of available equipment types and their corresponding equipment.
     */
    private Map<EquipmentType, ArrayList<Equipment>> availableEquipments;

    /**
     * Constructs a BorrowEquipmentUI with the specified scanner and user.
     *
     * @param scanner The scanner for user input.
     * @param currentUser The currently logged-in user.
     */
    public BorrowEquipmentUI(Scanner scanner, User currentUser) {
        this.scanner = scanner;
        this.currentUser = currentUser;

        this.equipmentCollection = EquipmentManager.getInstance();
        this.typeCollection = EquipmentTypeManager.getInstance();
        this.bookRecordCollection = EquipmentBookManager.getInstance();
        this.bookingControll = new EquipmentBookingControl();
    }

  /**
   * Shows the equipment status for borrowing based on the facility booking record and sport type.
   *
   * @param facilityBookRecord The facility booking record.
   * @param sportType The sport type.
   * @return True if there are available equipments, false otherwise.
   */
  public boolean showEquipmentStatus(FacilityBookRecord facilityBookRecord, String sportType) {
    this.facilityBookRecord = facilityBookRecord;
    // String sportType = facilityBookRecord.getSportFacility().getSportFacilityType().getSportType();
    
    // store available Equipments for all Borrowable Equipment type
    availableEquipments = getAvailableEquipments(sportType);

    // display Equipment Table
    if (availableEquipments.isEmpty()) {
      System.out.printf("There are no borrowable equipments for %s Court.\n", sportType);
      return false;
    } else {
      displayEquipmentTable(availableEquipments);

      count = 0;
      availableEquipments.values().forEach((value) -> {
        count += value.size();
      });

      if (count > 0) { return true; }
      else {
        System.out.printf("There are no available equipments for %s Court in this moment.\n", sportType);
        return false; 
      }
    }
  }

  /**
   * Inputs the equipment type based on user selection.
   *
   * @param sportType The sport type.
   * @return The selected equipment type.
   */
  public EquipmentType inputEquipmentType(String sportType) {
    String input;
    EquipmentType targetType = null;
    
    do {
      System.out.print("Please input Equipment Type ID (e.g ET-001): ");
      input = scanner.next().toUpperCase();

      // find target equipment
      targetType = typeCollection.getEquipmentTypeByID(input);

      if (input.equals("B")) {
        break;
      } else if ((targetType == null) || !(targetType.getSportType().equals(sportType))) {
        System.out.println("ERROR: Equipment Type not found.");
        System.out.println("Please try again or input [B] go back.\n");
      }
    } while (!(input.equals("B")) && ((targetType == null) || !(targetType.getSportType().equals(sportType))));

    return targetType; // if can not found, ask again repeatly. If return null, go back home page. 
  }

  /**
   * Inputs the quantity of equipment to borrow.
   *
   * @param targetType The target equipment type.
   * @return The quantity of equipment to borrow.
   */
  public int inputQuantity(EquipmentType targetType) {
    int quantity = -1;
    while (true) {
      try {
        System.out.println("Please input the quantity or input [0] go back.");
        quantity = Integer.parseInt(scanner.next());
        
        if (quantity == 0) {
          return 0;
        } else if (quantity < 0) {
          System.out.println("Error: Input is incorrect. Please try again.");
        } else if (quantity > availableEquipments.get(targetType).size()) {
          System.out.println("ERROR: Not enough equipments for you to borrow.");
        } else {
          break;
        }
      } catch (NumberFormatException e) {
        System.out.println("Error: Input is incorrect. Please try again.");
        scanner.nextLine(); // Consume the invalid input
      }
    }
    return quantity;
  }

  /**
   * Borrows the equipment for the user.
   *
   * @param targetType The target equipment type.
   * @param quantity The quantity of equipment to borrow.
   */
  public void borrowEquipment(EquipmentType targetType, int quantity) {
    // available nums > quantity, enough to do borrow
    ArrayList<Equipment> targetEquips = new ArrayList<>();
    for (int i=0; i<quantity; i++) {
      targetEquips.add(availableEquipments.get(targetType).get(i));
    }

    EquipmentBookRecord bookRecord = new EquipmentBookRecord(
      facilityBookRecord, 
      targetEquips, 
      currentUser
    );

    bookRecordCollection.addBookRecord(bookRecord);
    
    for (Equipment equip: targetEquips) {
      System.out.printf("Success borrow %s %s by %s\n", 
        equip.getEquipmentID(), 
        equip.getEquipmentName(),
        bookRecord.getUser().getUsername()
      );
    }
  }

  /**
   * Displays the equipment table for borrowing.
   *
   * @param availableEquipments The map of available equipments.
   */
  private void displayEquipmentTable(Map<EquipmentType, ArrayList<Equipment>> availableEquipments) {
    System.out.println("\n========================== Equipment Rental ===========================");
    System.out.printf("|| %-6s || %-30s || %-9s || %-8s ||\n", 
    "ID",
      "Equipment Type",
      "Available",
      "Price"
    );
    System.out.println("=======================================================================");
    
    for (Map.Entry<EquipmentType, ArrayList<Equipment>> type: availableEquipments.entrySet()) {
      System.out.printf("|| %-6s || %-30s || %9d || $%.0f/Hour ||\n", 
        type.getKey().getEquipmentTypeID(), 
        type.getKey().getEquipmentTypeName(), 
        type.getValue().size(),
        type.getKey().getPrice()
      );
    }
    System.out.println("=======================================================================");
  }

  /**
   * Gets the available equipments for borrowing based on the sport type.
   *
   * @param sportType The sport type.
   * @return The map of available equipments.
   */
  private Map<EquipmentType, ArrayList<Equipment>> getAvailableEquipments(String sportType) {
    Map<EquipmentType, ArrayList<Equipment>> availableEquipments = new TreeMap<>();
    for (EquipmentType type: typeCollection.getBorrowableTypesBySportType(sportType)) {
      availableEquipments.put(
        type, 
        bookingControll.getAvailableEquipments(
          type, 
          facilityBookRecord.getDate(), 
          facilityBookRecord.getTimeslot()
        )
      );
    }
    return availableEquipments;
  }

  // private ArrayList<Equipment> getAvailableEquipsinTimeSlot(EquipmentType type) {
  //   // all available equipments in the target time slot
  //   ArrayList<Equipment> availableEquipment = new ArrayList<>();
    
  //   // get all equipments with the target type
  //   ArrayList<Equipment> targetEquipments = equipmentCollection.getBorrowableEquipmentByType(type);

  //   // check which equipment is available in the target time slot.
  //   for (Equipment targetEquipment: targetEquipments) {
  //     for (int[] timeslot: bookRecordCollection.getAvailableTimeSlot(targetEquipment, facilityBookRecord.getDate())) {
  //       if ((timeslot[0] <= facilityBookRecord.getStartTime()) && (timeslot[1] >= facilityBookRecord.getStartTime())) {
  //         availableEquipment.add(targetEquipment);
  //       }
  //     }
  //   }

  //   return availableEquipment;
  // }
}