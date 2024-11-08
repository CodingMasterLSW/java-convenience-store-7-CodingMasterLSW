package store.domain.product;

import java.time.LocalDate;
import store.domain.promotion.Promotion;

public class Product {

    private String name;
    private int price;
    private Stock stock;
    private Promotion promotion;

    private Product(String name, int price, Stock stock,
            Promotion promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    public static Product ofNormal(String name, int price, int normalStock) {
        Stock stock = Stock.from(normalStock, 0);
        return new Product(name, price, stock, null);
    }

    public static Product ofPromotion(String name, int price, int promotionStock,
            Promotion promotion) {
        Stock stock = Stock.from(0, promotionStock);
        return new Product(name, price, stock, promotion);
    }

    public static Product of(String name, int price, int normalStock, int promotionStock,
            Promotion promotion) {
        Stock stock = Stock.from(normalStock, promotionStock);
        return new Product(name, price, stock, promotion);
    }

    public void buy(int quantity, LocalDate localDate) {
        if (promotion.isDate(localDate)) {
            stock.handlePromotionStock(quantity);
            return;
        }
        stock.decreaseNormal(quantity);
    }


    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Stock getStock() {
        return stock;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}

