package store.domain.product;

import store.domain.promotion.Promotion;

public class PromotionProduct extends Product {

    private int stock;
    private Promotion promotion;

    private PromotionProduct(String name, int price, int stock, Promotion promotion) {
        super(name, price);
        this.stock = stock;
        this.promotion = promotion;
    }

    public static PromotionProduct of(String name, int price, int stock, Promotion promotion) {
        return new PromotionProduct(name, price, stock, promotion);
    }

    @Override
    public boolean isPromotionProduct() {
        return true;
    }

    @Override
    public int getStock() {
        return this.stock;
    }

}
