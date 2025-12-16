package sportapp.membership;

/**
 * Represents a basic membership in the sport management system.
 * <p>
 * Basic members receive no discount and pay the base price defined for this tier.
 */
public class BasicMemberShip implements MemberShip {

    /**
     * Constructs a BasicMemberShip instance.
     * <p>
     * This constructor initializes the BasicMemberShip object with default values.
     */
    public BasicMemberShip() {
        // Default constructor
    }

    /**
     * Returns the discount rate for Basic membership.
     * <p>
     * Basic members do not receive any discount, so the rate is always 0.0.
     *
     * @return The discount rate, which is 0.0 for basic membership.
     */
    public double getDiscountRate() {
        return 0.0;
    }

    /**
     * Returns the monthly price for Basic membership.
     * <p>
     * The price is determined by the base rate defined for this membership tier.
     *
     * @return The price in the application's currency units.
     */
    public double getPrice() {
        return 0.0;
    }
}
