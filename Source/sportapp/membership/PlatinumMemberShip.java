package sportapp.membership;

/**
 * Represents a platinum membership in the sport management system.
 * <p>
 * Platinum members receive the highest discount and pay the highest price among all tiers.
 */
public class PlatinumMemberShip implements MemberShip {

    /**
     * Constructs a PlatinumMemberShip instance.
     * <p>
     * This constructor initializes the PlatinumMemberShip object with default values.
     */
    public PlatinumMemberShip() {
        // Default constructor
    }

    /**
     * Returns the discount rate for Platinum membership.
     *
     * @return The discount rate, e.g., 0.40 for 40%.
     */
    public double getDiscountRate() {
        return 0.40;
    }

    /**
     * Returns the monthly price for Platinum membership.
     *
     * @return The price in the application's currency units.
     */
    public double getPrice() {
        return 100.0;
    }
}
