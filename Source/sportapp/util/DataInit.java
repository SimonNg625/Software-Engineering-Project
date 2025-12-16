package sportapp.util;

import sportapp.User;
import sportapp.UserCollection;
import sportapp.UserSecurityAnswer;
import sportapp.manager.EquipmentBookManager;
import sportapp.manager.EquipmentManager;
import sportapp.manager.EquipmentTypeManager;
import sportapp.manager.FacilityBookManager;
import sportapp.manager.SportFacilityManager;
import sportapp.model.BookingStatus;
import sportapp.model.Equipment;
import sportapp.model.EquipmentBookRecord;
import sportapp.model.EquipmentType;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;
import sportapp.model.SportFacilityType;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Utility class for initializing default data in the sport management system.
 * <p>
 * This class provides methods to set up default users, equipment, facilities,
 * and bookings for the application.
 */
public class DataInit {

    /**
     * The current date used for initializing data.
     */
    public static LocalDate today = LocalDate.now();

    /**
     * Constructs a DataInit instance.
     * <p>
     * This constructor initializes the DataInit object with default values.
     */
    public DataInit() {
        // Default constructor
    }

    /**
     * Initializes the default data for the application.
     *
     * @param loadRecord A flag indicating whether to load existing records.
     */
    public static void initDefaultData(String loadRecord) {
        System.out.println("Importing default data...");

        initDefaultUsers();
        User user = UserCollection.getInstance().findUserByName("admin");
        initDefaultEquipments(user, loadRecord);
        initDefaultFacilitiesAndBookings(user, loadRecord);

        System.out.println("Complete import default data.\n");
    }

    /**
     * Initializes the default equipment data.
     *
     * @param user The user associated with the equipment data.
     * @param loadRecord A flag indicating whether to load existing records.
     */
    private static void initDefaultEquipments(User user, String loadRecord) {
        // Collection Class instance
        EquipmentTypeManager typeCollection = EquipmentTypeManager.getInstance();
        EquipmentManager equipmentCollection = EquipmentManager.getInstance();
        EquipmentBookManager equipmentBookManager = EquipmentBookManager.getInstance();

        // Borrowable and Sellable Equipment Types
        // EquipmentType.Category BORROWABLE = EquipmentType.Category.BORROWABLE;
        EquipmentType BASKETBALL_A = new EquipmentType("ET-001", "Basketball Brand A", "BASKA", "Basketball", 15);
        EquipmentType BASKETBALL_B = new EquipmentType("ET-002", "Basketball Brand B", "BASKB", "Basketball", 20);
        EquipmentType BADMINTON_RACKET_A = new EquipmentType("ET-003", "Badminton Racket Brand A", "BRACA", "Badminton", 10);
        EquipmentType BADMINTON_RACKET_B = new EquipmentType("ET-004", "Badminton Racket Brand A", "BRACB", "Badminton", 15);
        EquipmentType TABLETENNIS_RACKET_A = new EquipmentType("ET-005", "Table Tennis Racket Brand A", "TRACA", "TableTennis", 10);
        EquipmentType TABLETENNIS_RACKET_B = new EquipmentType("ET-006", "Table Tennis Racket Brand B", "TRACB", "TableTennis", 20);

        // EquipmentType.Category SELLABLE = EquipmentType.Category.SELLABLE;
        EquipmentType BADMINTON_SHUTTLECOCK_6 = new EquipmentType("ET-007", "Badminton Shuttlecock (6 pcs)", "BBALL6", "Badminton", 35);
        EquipmentType BADMINTON_SHUTTLECOCK_12 = new EquipmentType("ET-008", "Badminton Shuttlecock (12 pcs)", "BBALL12", "Badminton", 60);
        EquipmentType TABLETENNIS_BALL_3 = new EquipmentType("ET-009", "Table Tennis Ball (3 pcs)", "TBALL1", "TableTennis", 15);
        EquipmentType TABLETENNIS_BALL_6 = new EquipmentType("ET-010", "Table Tennis Ball (6 pcs)", "TBALL3", "TableTennis", 25);

        typeCollection.addBorrowableType(BASKETBALL_A);
        typeCollection.addBorrowableType(BASKETBALL_B);
        typeCollection.addBorrowableType(BADMINTON_RACKET_A);
        typeCollection.addBorrowableType(BADMINTON_RACKET_B);
        typeCollection.addBorrowableType(TABLETENNIS_RACKET_A);
        typeCollection.addBorrowableType(TABLETENNIS_RACKET_B);

        typeCollection.addSellableType(BADMINTON_SHUTTLECOCK_6);
        typeCollection.addSellableType(BADMINTON_SHUTTLECOCK_12);
        typeCollection.addSellableType(TABLETENNIS_BALL_3);
        typeCollection.addSellableType(TABLETENNIS_BALL_6);

        // add Default Equipments into Equipment Collection
        for (EquipmentType type: typeCollection.getBorrowableTypes()) {
            for (int i=1; i<=10; i++) {
                equipmentCollection.addEquipment(new Equipment(i, type));
            }
        }

        for (EquipmentType type: typeCollection.getSellableTypes()) {
            equipmentCollection.addEquipment(new Equipment(1, type));
        }

        if (loadRecord.equals("true")) {
            //add pending equipment booking
            EquipmentBookRecord booking1 = new EquipmentBookRecord(new ArrayList<Equipment>(equipmentCollection.getBorrowableEquipmentByType(BASKETBALL_A).subList(0, 2)), user, today.plusDays(1), 10, 12, BookingStatus.PENDING);
            EquipmentBookRecord booking2 = new EquipmentBookRecord(new ArrayList<Equipment>(equipmentCollection.getBorrowableEquipmentByType(BASKETBALL_A).subList(1, 3)), user, today.plusDays(2), 10, 12, BookingStatus.PENDING);
            EquipmentBookRecord booking3 = new EquipmentBookRecord(new ArrayList<Equipment>(equipmentCollection.getBorrowableEquipmentByType(BASKETBALL_A).subList(2, 4)), user, today.plusDays(3), 10, 12, BookingStatus.PENDING);

            //add confirmed equipment booking
            EquipmentBookRecord booking4 = new EquipmentBookRecord(new ArrayList<Equipment>(equipmentCollection.getBorrowableEquipmentByType(BASKETBALL_A).subList(0, 2)), user, today.plusDays(1), 14, 16, BookingStatus.CONFIRMED);
            EquipmentBookRecord booking5 = new EquipmentBookRecord(new ArrayList<Equipment>(equipmentCollection.getBorrowableEquipmentByType(BASKETBALL_B).subList(0, 2)), user, today.plusDays(2), 14, 16, BookingStatus.CONFIRMED);
            EquipmentBookRecord booking6 = new EquipmentBookRecord(new ArrayList<Equipment>(equipmentCollection.getBorrowableEquipmentByType(BASKETBALL_B).subList(1, 4)), user, today.plusDays(3), 9, 11, BookingStatus.CONFIRMED);


            equipmentBookManager.addBookRecord(booking1);
            equipmentBookManager.addBookRecord(booking2);
            equipmentBookManager.addBookRecord(booking3);
            equipmentBookManager.addBookRecord(booking4);
            equipmentBookManager.addBookRecord(booking5);
            equipmentBookManager.addBookRecord(booking6);

            System.out.println("Default equipment data and records has been initialized.");
        } else {
            System.out.println("Default equipment data has been initialized.");
        }

    }

    /**
     * Initializes the default facilities and bookings data.
     *
     * @param user The user associated with the facility data.
     * @param loadRecord A flag indicating whether to load existing records.
     */
    public static void initDefaultFacilitiesAndBookings(User user, String loadRecord) {
        SportFacilityManager sportFacilityManager = SportFacilityManager.getInstance();

        SportFacilityType basketballCourt = new SportFacilityType("SFT-001", "Basketball", 30);
        SportFacilityType badmintonCourt = new SportFacilityType("SFT-002", "Badminton", 20);
        SportFacilityType tableTennisRoom = new SportFacilityType("SFT-003", "TableTennis", 15);

        SportFacility facilityBasketballCourt = new SportFacility("SF-001", basketballCourt, SportFacility.Status.AVAILABLE);
        SportFacility facilitybadmintonCourt = new SportFacility("SF-002", badmintonCourt, SportFacility.Status.AVAILABLE);
        SportFacility facilitytableTennisRoom = new SportFacility("SF-003", tableTennisRoom, SportFacility.Status.AVAILABLE);

        sportFacilityManager.addSportFacility(facilityBasketballCourt);
        sportFacilityManager.addSportFacility(facilitybadmintonCourt);
        sportFacilityManager.addSportFacility(facilitytableTennisRoom);

        if (loadRecord.equals("true")) {
            FacilityBookManager facilityBookManager = FacilityBookManager.getInstance();

            //add pending booking for testing and demo
            FacilityBookRecord booking1 = new FacilityBookRecord(facilityBasketballCourt, user, today.plusDays(1), 10, 12, BookingStatus.PENDING);
            FacilityBookRecord booking3 = new FacilityBookRecord(facilitytableTennisRoom, user, today.plusDays(2), 12, 13, BookingStatus.PENDING);
            FacilityBookRecord booking5 = new FacilityBookRecord(facilitybadmintonCourt, user, today.plusDays(3), 9, 10, BookingStatus.PENDING);

            //add confirmed booking for testing and demo
            FacilityBookRecord booking2 = new FacilityBookRecord(facilitybadmintonCourt, user, today.plusDays(2), 14, 16, BookingStatus.CONFIRMED);
            FacilityBookRecord booking4 = new FacilityBookRecord(facilitybadmintonCourt, user, today.plusDays(1), 9, 11, BookingStatus.CONFIRMED);

            facilityBookManager.addBooking(booking1);
            facilityBookManager.addBooking(booking2);
            facilityBookManager.addBooking(booking3);
            facilityBookManager.addBooking(booking4);
            facilityBookManager.addBooking(booking5);

            System.out.println("Default Sport facility data and records have been initialized.");
        } else {
            System.out.println("Default Sport facility data have been initialized.");
        }
    }

    /**
     * Initializes the default user data.
     */
    private static void initDefaultUsers() {
        UserCollection userCollection = UserCollection.getInstance();

        UserSecurityAnswer adminSecurityQuestion = new UserSecurityAnswer("test", "test");
        userCollection.addUser("admin", "password123", adminSecurityQuestion);
    }
}