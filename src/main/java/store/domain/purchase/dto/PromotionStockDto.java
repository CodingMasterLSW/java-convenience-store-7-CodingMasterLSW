package store.domain.purchase.dto;

public class PromotionStockDto {

    private String productName;
    private int lackPromotionStock;

    private PromotionStockDto(String productName, int lackPromotionStock) {
        this.productName = productName;
        this.lackPromotionStock = lackPromotionStock;
    }

    public static PromotionStockDto of (String productName, int lackPromotionStock) {
        return new PromotionStockDto(productName, lackPromotionStock);
    }

    public String getProductName() {
        return productName;
    }

    public int getLackPromotionStock() {
        return lackPromotionStock;
    }
}
