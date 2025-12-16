package sportapp;

/**
 * Gold membership tier. Provides a moderate discount and a mid-level price.
 */
public class GoldMemberShip implements MemberShip {

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
     * @return The membership price in the application's currency units.
     */
    public double getPrice() {
        return 50.0;
    }
}