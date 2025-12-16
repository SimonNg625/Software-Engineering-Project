package sportapp;

/**
 * Implementation of a basic membership tier.
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
     *
     * @return The discount rate, which is 0.0 for basic membership.
     */
    public double getDiscountRate() {
        return 0.0;
    }

    /**
     * Returns the monthly price for Basic membership.
     *
     * @return The price in the application's currency units.
     */
    public double getPrice() {
        return 0.0;
    }
}
