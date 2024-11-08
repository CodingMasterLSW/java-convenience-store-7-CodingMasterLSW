package store.dto;

public class PurchaseDto {

    private final int totalPrice;
    private final int totalQuantity;

    private PurchaseDto(int totalPrice, int totalQuantity) {
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public static PurchaseDto from(int totalPrice, int totalQuantity) {
        return new PurchaseDto(totalPrice, totalQuantity);
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }
}
