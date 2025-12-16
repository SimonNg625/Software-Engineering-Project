package sportapp.membership;

/**
 * Represents a gold membership in the sport management system.
 * <p>
 * Gold members receive a moderate discount and pay a mid-level price.
 */
public class GoldMemberShip implements MemberShip {

    /**
     * Constructs a GoldMemberShip instance.
     * <p>
     * This constructor initializes the GoldMemberShip object with default values.
     */
    public GoldMemberShip() {
        // Default constructor
    }

    /**
     * Returns the discount rate for Gold membership.
     *
     * @return The discount rate, e.g., 0.20 for 20%.
     */
    public double getDiscountRate() {
        return 0.20;
    }

    /**
     * Returns the monthly price for Gold membership.
     *
     * @return The price in the application's currency units.
     */
    public double getPrice() {
        return 50.0;
    }
}