package store.domain.purchase.dto;

public class PurchaseItemDto {

    private final String productName;
    private final int price;
    private final int quantity;


    private PurchaseItemDto(String productName, int price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public static PurchaseItemDto of(String productName, int price, int quantity) {
        return new PurchaseItemDto(productName, price, quantity);
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

}
