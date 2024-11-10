package store.domain.purchase;

public class PurchaseGift {

    private final String name;
    private int quantity;

    private PurchaseGift(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public static PurchaseGift of(String name, int quantity) {
        return new PurchaseGift(name, quantity);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
