package store.domain.product;

import store.domain.promotion.Promotion;

public class Product {

    private String name;
    private int price;
    private int normalStock;
    private int promotionStock;
    private Promotion promotion;

    private Product(String name, int price, int normalStock, int promotionStock,
            Promotion promotion) {
        this.name = name;
        this.price = price;
        this.normalStock = normalStock;
        this.promotionStock = promotionStock;
        this.promotion = promotion;
    }

    public static Product ofNormal(String name, int price, int normalStock) {
        return new Product(name, price, normalStock, 0, null);
    }

    public static Product ofPromotion(String name, int price, int promotionStock,
            Promotion promotion) {
        return new Product(name, price, 0, promotionStock, promotion);
    }

    public void addNormalStock(int stock) {
        this.normalStock += stock;
    }

    public void addPromotionStock(int stock, Promotion promotion) {
        this.promotionStock += stock;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getNormalStock() {
        return normalStock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }
}

