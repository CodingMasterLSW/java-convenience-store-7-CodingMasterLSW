package store.domain.product;

import store.domain.promotion.Promotion;

public class PromotionProduct extends Product {

    private Promotion promotion;

    private PromotionProduct(String name, int price, int quantity, Promotion promotion) {
        super(name, price, quantity);
        this.promotion = promotion;
    }

    public static PromotionProduct of(String name, int price, int quantity, Promotion promotion) {
        return new PromotionProduct(name, price, quantity, promotion);
    }

    @Override
    public boolean isPromotionProduct() {
        return true;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
