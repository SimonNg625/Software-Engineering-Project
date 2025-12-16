package sportapp;

/**
 * Platinum membership tier. Provides the highest discount and price.
 */
public class PlatinumMemberShip implements MemberShip {
    /**
     * Returns the discount rate for Platinum membership.
     * @return discount rate (e.g. 0.40 for 40%)
     */
    public double getDiscountRate() {
        return 0.40;
    }
    
    /**
     * Returns the monthly price for Platinum membership.
     * @return membership price
     */
    public double getPrice() {
        return 100.0;
    }
}
