package store.domain;

public class Discount {

    private int promotionAmount;
    private Membership membership;

    private Discount() {
        this.promotionAmount = 0;
        this.membership = Membership.create();
    }

    public static Discount create() {
        return new Discount();
    }

    public void addPromotionAmount(int quantity) {
        this.promotionAmount += quantity;
    }

    public int getPromotionAmount() {
        return promotionAmount;
    }

    public int getMembershipAmount() {
        return membership.getCurrentDiscount();
    }

    public int applyMembership(int amount) {
        return membership.apply(amount);
    }

}
