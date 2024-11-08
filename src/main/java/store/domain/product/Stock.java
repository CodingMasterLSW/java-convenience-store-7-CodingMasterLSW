package store.domain.product;

public class Stock {

    private int normal;
    private int promotion;

    private Stock(int normal, int promotion) {
        validateInitQuantity(normal, promotion);
        this.normal = normal;
        this.promotion = promotion;
    }

    public static Stock from(int normal, int promotion) {
        return new Stock(normal, promotion);
    }

    public void decreaseNormal(int quantity) {
        validateQuantity(quantity, normal);
        normal -= quantity;
    }

    public void decreasePromotion(int quantity) {
        validateQuantity(quantity, promotion);
        promotion -= quantity;
    }

    public void validateInitQuantity(int normalQuantity, int promotionQuantity) {
        if (normalQuantity < 0 || promotionQuantity < 0) {
            throw new IllegalArgumentException("[ERROR] 재고는 0 이하일 수 없습니다.");
        }
    }

    public void validateQuantity(int quantity, int currentStock) {
        if (currentStock - quantity < 0) {
            throw new IllegalArgumentException("[ERROR] 현재 재고가 없습니다.");
        }
    }

    public void addNormal(int stock) {
        this.normal += stock;
    }

    public void addPromotion(int stock) {
        this.promotion += stock;
    }

    public void handlePromotionStockShortage(int quantity) {
        if (promotion < quantity) {
            int lackQuantity = quantity - promotion;
            promotion = 0;
            normal -= lackQuantity;
            return;
        }
        promotion -= quantity;
    }

    public int getNormal() {
        return normal;
    }

    public int getPromotion() {
        return promotion;
    }

}
