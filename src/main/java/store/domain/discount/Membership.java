package store.domain.discount;

public class Membership {

    private static final int MAXIMUM_DISCOUNT = 8_000;
    private static final double DISCOUNT_RATE = 0.3;

    private int currentDiscount;

    private Membership() {
        this.currentDiscount = 0;
    }

    public static Membership create() {
        return new Membership();
    }

    public int apply(int amount) {
        int remainingDiscount = MAXIMUM_DISCOUNT - currentDiscount;
        if (remainingDiscount <= 0) {
            return 0;
        }
        int potentialDiscount = (int) (amount * DISCOUNT_RATE);
        int discountAmount = Math.min(potentialDiscount, remainingDiscount);
        currentDiscount += discountAmount;
        return discountAmount;
    }

    public int getCurrentDiscount() {
        return currentDiscount;
    }

}
