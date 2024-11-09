package store.domain;

public class Discount {

    private int promotionAmount;
    private int membershipAmount;

    private Discount() {
        this.promotionAmount = 0;
        this.membershipAmount = 0;
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
        return membershipAmount;
    }

    public int getTotalAmount() {
        return promotionAmount + membershipAmount;
    }
}
