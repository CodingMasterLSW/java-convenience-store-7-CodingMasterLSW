package store.domain.purchase.dto;

public class PurchaseAlertDto {

    private final String productName;
    private final int freeQuantity;

    private PurchaseAlertDto(String productName, int freeQuantity) {
        this.productName = productName;
        this.freeQuantity = freeQuantity;
    }

    public static PurchaseAlertDto from(String productName, int freeQuantity) {
        return new PurchaseAlertDto(productName, freeQuantity);
    }

}
