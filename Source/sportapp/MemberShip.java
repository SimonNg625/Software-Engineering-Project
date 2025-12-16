package sportapp;

/**
 * Interface that represents a membership tier.
 * Implementations provide price and discount rate information used by billing logic.
 */
public interface MemberShip{
    /**
     * Returns discount rate applied to the member's purchases.
     * @return discount rate in range [0.0, 1.0)
     */
    double getDiscountRate();

    /**
     * Returns the monthly price of this membership tier.
     * @return price in the application's currency
     */
    double getPrice();
}
