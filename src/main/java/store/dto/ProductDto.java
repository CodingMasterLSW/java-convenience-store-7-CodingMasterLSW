package store.dto;

public class ProductDto {

    private String name;
    private int price;
    private int quantity;
    private String promotion;

    private ProductDto(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public static ProductDto of(String name, int price, int quantity, String promotion) {
        return new ProductDto(name, price, quantity, promotion);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }
}
