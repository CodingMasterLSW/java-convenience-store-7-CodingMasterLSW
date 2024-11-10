package store.domain.purchase.dto;

public class PurchaseGiftDto {

    private String name;
    private int quantity;

    private PurchaseGiftDto(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public static PurchaseGiftDto of(String name, int quantity) {
        return new PurchaseGiftDto(name, quantity);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

}
