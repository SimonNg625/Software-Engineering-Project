package sportapp.membership;

/**
 * Represents a membership in the sport management system.
 * <p>
 * This interface defines the basic structure for different membership tiers,
 * including methods to retrieve the discount rate and price.
 */
public interface MemberShip {

    /**
     * Retrieves the discount rate for the membership.
     *
     * @return The discount rate as a decimal value.
     */
    double getDiscountRate();

    /**
     * Retrieves the monthly price for the membership.
     *
     * @return The price in the application's currency units.
     */
    double getPrice();
}
