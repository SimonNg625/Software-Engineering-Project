package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sportapp.Route;
import sportapp.User;
import sportapp.UserSecurityAnswer;
import sportapp.ViewCurrentBookingControl;
import sportapp.model.*;
import sportapp.ui.ViewCurrentBookingUI;
import sportapp.model.SportFacility;
import sportapp.model.SportFacilityType;
import sportapp.PaymentCalculator;
import sportapp.membership.MemberShip;
import sportapp.membership.BasicMemberShip;

class ViewCurrentBookingUITest {
    private PrintStream originalOut;
    private ByteArrayOutputStream outContent;

    // A lightweight fake controller to inject predictable behavior
    private static class FakeController extends ViewCurrentBookingControl {
        List<FacilityBookRecord> pendingFacility = new ArrayList<>();
        List<EquipmentBookRecord> pendingEquipment = new ArrayList<>();
        List<SportFacility> availableFacilities = new ArrayList<>();
        Map<Object, ArrayList<int[]>> slots = new HashMap<>();
        boolean updateBookingDateTimeResult = true;
        boolean updateBookingFacilityResult = true;

        @Override
        public ArrayList<FacilityBookRecord> getPendingFacilityBookingRecord(User user) {
            return new ArrayList<>(pendingFacility);
        }

        @Override
        public ArrayList<EquipmentBookRecord> getPendingEquipmentBookingRecord(User user) {
            return new ArrayList<>(pendingEquipment);
        }

        @Override
        public ArrayList<SportFacility> getAvailableFacilities() {
            return new ArrayList<>(availableFacilities);
        }

        @Override
        public ArrayList<int[]> getAvailableTimeSlots(SportFacility facility, LocalDate date) {
            return slots.getOrDefault(facility, new ArrayList<>());
        }

        @Override
        public ArrayList<int[]> getAvailableTimeSlots(ArrayList<Equipment> equipments, LocalDate date) {
            return slots.getOrDefault(equipments, new ArrayList<>());
        }

        @Override
        public boolean updateBookingDateTime(FacilityBookRecord booking, LocalDate date, int startTime, int endTime) {
            return updateBookingDateTimeResult;
        }

        @Override
        public boolean updateBookingDateTime(EquipmentBookRecord booking, LocalDate date, int startTime, int endTime) {
            return updateBookingDateTimeResult;
        }

        @Override
        public boolean updateBookingFacility(FacilityBookRecord booking, SportFacility facility, LocalDate date, int startTime, int endTime) {
            return updateBookingFacilityResult;
        }

        @Override
        public void cancelBooking(FacilityBookRecord booking) {
            pendingFacility.remove(booking);
        }

        @Override
        public void cancelBooking(EquipmentBookRecord booking) {
            pendingEquipment.remove(booking);
        }
    }

    // Helpers to build minimal bookings
    private static FacilityBookRecord makeFacilityBooking(String name, LocalDate date, int sh, int eh, BookingStatus status) {
        SportFacilityType sft = new SportFacilityType("T-" + name, "Type-" + name, 10);
        SportFacility sf = new SportFacility("F-" + name, sft, SportFacility.Status.AVAILABLE) {
            @Override public String getName() { return name; }
        };
        return new FacilityBookRecord(sf, new User("u", 1, "p", new UserSecurityAnswer("q","a")), date, sh, eh, status);
    }

    private static EquipmentBookRecord makeEquipmentBooking(String equipName, LocalDate date, int sh, int eh, BookingStatus status, boolean borrowable, int qty) {
        EquipmentType et = new EquipmentType("ET-" + equipName, equipName + " Type", "TRACK", "Cat", 5.0);
        Equipment equipment = new Equipment(1, et) {
            @Override public String getEquipmentName() { return equipName; }
            @Override public boolean isBorrowable() { return borrowable; }
            @Override public boolean isSellable() { return !borrowable; }
        };
        return new EquipmentBookRecord(equipment, new User("u", 1, "p", new UserSecurityAnswer("q","a")), date, sh, eh, status, qty);
    }

    private static User testUser() {
        return new User("tester", 100, "Pw@12345", new UserSecurityAnswer("q","a"));
    }

    @BeforeEach
    void setUpStreams() {
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    // Reflection helper to inject fake controller
    private ViewCurrentBookingUI makeUIWithController(FakeController fc, String input) throws Exception {
        Scanner sc = new Scanner(new ByteArrayInputStream((input == null ? "" : input).getBytes(StandardCharsets.UTF_8)));
        ViewCurrentBookingUI ui = new ViewCurrentBookingUI(sc, testUser());
        Field f = ViewCurrentBookingUI.class.getDeclaredField("controller");
        f.setAccessible(true);
        f.set(ui, fc);
        return ui;
    }

    @Test
    void display_noBookings_showsNoBookingMessageAndReturnsHome() throws Exception {
        FakeController fc = new FakeController();
        ViewCurrentBookingUI ui = makeUIWithController(fc, "5\n");
        Route result = ui.display(new Scanner(new ByteArrayInputStream("5\n".getBytes(StandardCharsets.UTF_8))), testUser());
        String out = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("No current booking found."));
        assertEquals(Route.HOME, result);
    }

    @Test
    void display_withFacilityAndEquipment_listsBoth() throws Exception {
        FakeController fc = new FakeController();
        FacilityBookRecord fr = makeFacilityBooking("CourtA", LocalDate.now().plusDays(1), 9, 11, BookingStatus.PENDING);
        EquipmentBookRecord er = makeEquipmentBooking("Racket", LocalDate.now().plusDays(1), 12, 13, BookingStatus.PENDING, true, 1);
        fc.pendingFacility.add(fr);
        fc.pendingEquipment.add(er);

        ViewCurrentBookingUI ui = makeUIWithController(fc, "5\n");
        Route r = ui.display(new Scanner(new ByteArrayInputStream("5\n".getBytes(StandardCharsets.UTF_8))), testUser());
        String out = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("Current Booking") || out.contains("Current Booking (Shopping Cart)"));
        assertTrue(out.contains("CourtA"));
        assertTrue(out.contains("Racket"));
        assertEquals(Route.HOME, r);
    }

    @Test
    void cancelFacility_confirmYes_removesBooking() throws Exception {
        FakeController fc = new FakeController();
        FacilityBookRecord fr = makeFacilityBooking("CourtB", LocalDate.now().plusDays(2), 10, 12, BookingStatus.PENDING);
        fc.pendingFacility.add(fr);

        // menu: display -> choose 3 (cancel) -> select 1 -> confirm Y -> exit
        String input = String.join("\n", "3", "1", "Y", "5") + "\n";
        ViewCurrentBookingUI ui = makeUIWithController(fc, input);
        Route r = ui.display(new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), testUser());
        assertFalse(fc.pendingFacility.contains(fr));
        String out = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("Booking cancelled successfully!"));
        assertEquals(Route.HOME, r);
    }

    @Test
    void updateDateTime_facility_noSlots_printsNoAvailable() throws Exception {
        FakeController fc = new FakeController();
        FacilityBookRecord f = makeFacilityBooking("F2", LocalDate.now().plusDays(2), 9, 10, BookingStatus.PENDING);
        fc.pendingFacility.add(f);

        String date = LocalDate.now().plusDays(5).toString();
        String input = String.join("\n", "1", "1", date, "5") + "\n"; // choose update date/time, select booking 1, input date, then return
        ViewCurrentBookingUI ui = makeUIWithController(fc, input);
        Route r = ui.display(new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), testUser());
        String out = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("No available time slots."));
        assertEquals(Route.HOME, r);
    }

    @Test
    void updateDateTime_facility_success_and_failure() throws Exception {
        FakeController fc = new FakeController();
        FacilityBookRecord f = makeFacilityBooking("F3", LocalDate.now().plusDays(3), 9, 10, BookingStatus.PENDING);
        fc.pendingFacility.add(f);

        ArrayList<int[]> s = new ArrayList<>();
        s.add(new int[]{9,21});
        fc.slots.put(f.getSportFacility(), s);

        // success path
        fc.updateBookingDateTimeResult = true;
        String date = LocalDate.now().plusDays(6).toString();
        String input1 = String.join("\n", "1", "1", date, "10", "12", "5") + "\n"; // pick update, select booking 1, date, start 10 end 12
        ViewCurrentBookingUI ui1 = makeUIWithController(fc, input1);
        Route r1 = ui1.display(new Scanner(new ByteArrayInputStream(input1.getBytes(StandardCharsets.UTF_8))), testUser());
        String out1 = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out1.contains("Booking updated successfully!"));
        assertEquals(Route.HOME, r1);

        outContent.reset();

        // failure path
        fc.updateBookingDateTimeResult = false;
        String input2 = String.join("\n", "1", "1", date, "13", "14", "5") + "\n";
        ViewCurrentBookingUI ui2 = makeUIWithController(fc, input2);
        Route r2 = ui2.display(new Scanner(new ByteArrayInputStream(input2.getBytes(StandardCharsets.UTF_8))), testUser());
        String out2 = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out2.contains("Failed to update booking."));
        assertEquals(Route.HOME, r2);
    }

    @Test
    void updateFacility_success_and_failure() throws Exception {
        FakeController fc = new FakeController();
        FacilityBookRecord fr = makeFacilityBooking("FUF", LocalDate.now().plusDays(2), 10, 11, BookingStatus.PENDING);
        fc.pendingFacility.add(fr);

        SportFacilityType sft = new SportFacilityType("T-1", "Type1", 10);
        SportFacility newF = new SportFacility("NF-1", sft, SportFacility.Status.AVAILABLE) {
            @Override public String getName() { return "NewFacility"; }
        };
        fc.availableFacilities.add(newF);
        ArrayList<int[]> slots = new ArrayList<>();
        slots.add(new int[]{9,21});
        fc.slots.put(newF, slots);

        String date = LocalDate.now().plusDays(5).toString();

        // success
        fc.updateBookingFacilityResult = true;
        String input3 = String.join("\n", "2", "1", "1", date, "10", "12", "5") + "\n";
        ViewCurrentBookingUI ui3 = makeUIWithController(fc, input3);
        Route r3 = ui3.display(new Scanner(new ByteArrayInputStream(input3.getBytes(StandardCharsets.UTF_8))), testUser());
        String out3 = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out3.contains("Facility updated successfully!"));
        assertEquals(Route.HOME, r3);

        outContent.reset();

        // failure
        fc.updateBookingFacilityResult = false;
        String input4 = String.join("\n", "2", "1", "1", date, "10", "12", "5") + "\n";
        ViewCurrentBookingUI ui4 = makeUIWithController(fc, input4);
        Route r4 = ui4.display(new Scanner(new ByteArrayInputStream(input4.getBytes(StandardCharsets.UTF_8))), testUser());
        String out4 = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out4.contains("Failed to update facility."));
        assertEquals(Route.HOME, r4);
    }

    @Test
    void selectBooking_invalidThenValid_thenCancelEquipment() throws Exception {
        FakeController fc = new FakeController();
        // two facilities and one equipment
        fc.pendingFacility.add(makeFacilityBooking("F1", LocalDate.now().plusDays(1), 9, 10, BookingStatus.PENDING));
        fc.pendingFacility.add(makeFacilityBooking("F2", LocalDate.now().plusDays(1), 10, 11, BookingStatus.PENDING));
        EquipmentBookRecord er = makeEquipmentBooking("E1", LocalDate.now().plusDays(1), 12, 13, BookingStatus.PENDING, true, 1);
        fc.pendingEquipment.add(er);

        // choose cancel (3), then invalid input 'abc', then out-of-range 99, then choose 3 (equipment), confirm Y, exit
        String input = String.join("\n", "3", "abc", "99", "3", "Y", "5") + "\n";
        ViewCurrentBookingUI ui = makeUIWithController(fc, input);
        Route r = ui.display(new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), testUser());
        String out = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("Invalid input. Please enter a number."));
        assertTrue(out.contains("Invalid selection. Please try again."));
        assertFalse(fc.pendingEquipment.contains(er));
        assertEquals(Route.HOME, r);
    }

    @Test
    void inputDate_empty_invalid_past_then_future_noSlots() throws Exception {
        FakeController fc = new FakeController();
        FacilityBookRecord f = makeFacilityBooking("FD", LocalDate.now().plusDays(1), 9, 10, BookingStatus.PENDING);
        fc.pendingFacility.add(f);

        String datePast = LocalDate.now().minusDays(1).toString();
        String dateFuture = LocalDate.now().plusDays(3).toString();
        String input = String.join("\n", "1", "1", "", "bad", datePast, dateFuture, "5") + "\n";
        ViewCurrentBookingUI ui = makeUIWithController(fc, input);
        Route r = ui.display(new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), testUser());
        String out = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("Date cannot be empty"));
        assertTrue(out.contains("Invalid date format"));
        assertTrue(out.contains("Date must be in the future"));
        assertTrue(out.contains("No available time slots."));
        assertEquals(Route.HOME, r);
    }

    @Test
    void selectTimeSlot_invalidThenValid() throws Exception {
        FakeController fc = new FakeController();
        FacilityBookRecord fr = makeFacilityBooking("TS", LocalDate.now().plusDays(2), 9, 10, BookingStatus.PENDING);
        fc.pendingFacility.add(fr);
        ArrayList<int[]> s = new ArrayList<>();
        s.add(new int[]{9,21});
        fc.slots.put(fr.getSportFacility(), s);

        String date = LocalDate.now().plusDays(4).toString();
        // invalid (end <= start), then start outside slot, then valid
        String input = String.join("\n", "1", "1", date, "15", "14", "8", "10", "10", "12", "5") + "\n";
        ViewCurrentBookingUI ui = makeUIWithController(fc, input);
        Route r = ui.display(new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), testUser());
        String out = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("End time must be after start time") || out.contains("End time must be after start time. Please try again."));
        assertTrue(out.contains("Selected time is not within available slots") || out.contains("Selected time is not within available slots. Please try again."));
        assertTrue(out.contains("Booking updated successfully!"));
        assertEquals(Route.HOME, r);
    }

    @Test
    void equipment_borrowable_and_nonBorrowable_updatePaths() throws Exception {
        FakeController fc = new FakeController();
        EquipmentBookRecord eqBorrow = makeEquipmentBooking("EQB", LocalDate.now().plusDays(1), 9, 10, BookingStatus.PENDING, true, 1);
        EquipmentBookRecord eqNon = makeEquipmentBooking("EQN", LocalDate.now().plusDays(1), 10, 11, BookingStatus.PENDING, false, 1);
        fc.pendingEquipment.add(eqBorrow);
        fc.pendingEquipment.add(eqNon);

        ArrayList<int[]> slots = new ArrayList<>();
        slots.add(new int[]{9,21});
        // key for borrowable is the booking.getBookingEquipment() (ArrayList<Equipment>)
        fc.slots.put(eqBorrow.getBookingEquipment(), slots);

        String date = LocalDate.now().plusDays(3).toString();
        // update borrowable (choice 1 -> booking 1), then update non-borrowable (choice 1 -> booking 2)
        String input = String.join("\n",
                "1", "2", date, "10", "12",
                "1", "2", date, "11", "12",
                "5") + "\n";
        // Explanation: menu 1 (updateDateTime), then select booking 2 (since no facility bookings, equipment index starts at 1), then date etc.
        ViewCurrentBookingUI ui = makeUIWithController(fc, input);
        Route r = ui.display(new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), testUser());
        String out = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("Booking updated successfully!") || out.contains("Failed to update booking."));
        assertEquals(Route.HOME, r);
    }

    @Test
    void display_menuInvalidAndDefault() throws Exception {
        FakeController fc = new FakeController();
        // need at least one booking so menu appears
        fc.pendingFacility.add(makeFacilityBooking("M1", LocalDate.now().plusDays(1), 9, 10, BookingStatus.PENDING));
        String input = String.join("\n", "abc", "9", "5") + "\n";
        ViewCurrentBookingUI ui = makeUIWithController(fc, input);
        Route r = ui.display(new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), testUser());
        String out = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("Invalid input. Please enter a number corresponding to the options."));
        assertTrue(out.contains("Invalid option. Please try again."));
        assertEquals(Route.HOME, r);
    }

    @Test
    void payment_returnsPaymentRoute_onConfirm() throws Exception {
        FakeController fc = new FakeController();
        // Create a pending facility booking
        FacilityBookRecord fr = new FacilityBookRecord(
                makeFacilityBooking("PayF", LocalDate.now().plusDays(1), 9, 10, BookingStatus.PENDING).getSportFacility(),
                testUser(),
                LocalDate.now().plusDays(1), 9, 10, BookingStatus.PENDING) {
            @Override public double getTotalPrice() { return 25.0; }
        };
        fc.pendingFacility.add(fr);

        // Choose payment (4) then confirm (1). Expect display to return Route.PAYMENT immediately.
        String input = String.join("\n", "4", "1") + "\n";
        ViewCurrentBookingUI ui = makeUIWithController(fc, input);
        Route r = ui.display(new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))), testUser());
        String out = outContent.toString(StandardCharsets.UTF_8);

        assertEquals(Route.PAYMENT, r, "display should return PAYMENT when payment is confirmed");
        // Also ensure the booking status has been updated to CONFIRMED by PaymentCalculator.ComfirmPay
        assertEquals(BookingStatus.CONFIRMED, fr.getStatus());
        assertTrue(out.contains("Total Price") || out.contains("TotalPrice"));
    }
}
