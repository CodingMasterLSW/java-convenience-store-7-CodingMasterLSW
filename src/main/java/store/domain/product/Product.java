package store.domain.product;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import store.domain.promotion.Promotion;
import store.domain.product.dto.ProductDto;

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

    public boolean hasPromotion() {
        return promotion != null;
    }

    public int hasEnoughPromotionStock(int purchaseQuantity) {
        if (!hasPromotion() || !stock.hasPromotionStock()) {
            return 0;
        }
        return calculateLackPromotionStock(purchaseQuantity);
    }

    private int calculateLackPromotionStock(int purchaseQuantity) {
        int requiredBuyQuantity = promotion.getBuy();
        int freeQuantity = promotion.getGet();
        int totalPromoUnits = requiredBuyQuantity + freeQuantity;
        int maxSetsFromPromotionStock = stock.getPromotion() / totalPromoUnits;
        int maxSetsFromRequestedQuantity = purchaseQuantity / totalPromoUnits;
        int applicableSets = Math.min(maxSetsFromPromotionStock, maxSetsFromRequestedQuantity);
        int promotionApplicableQuantity = applicableSets * totalPromoUnits;
        int remainingQuantity = purchaseQuantity - promotionApplicableQuantity;
        return remainingQuantity;
    }

    public void notEnoughPromotionProduct(int quantity, boolean continuePurchase) {
        if (stock.lackOfPromotionStock(quantity)) {
            handleInsufficientPromotionStock(quantity, continuePurchase);
        }
    }

    private void handleInsufficientPromotionStock(int quantity, boolean continuePurchase) {
        if (continuePurchase) {
            stock.handlePromotionStock(quantity);
            return;
        }
        stock.decreasePromotion(stock.getPromotion());
    }

    public void purchaseNormalProduct(int quantity) {
        stock.decreaseNormal(quantity);
    }

    public void purchasePromotionProduct(int quantity) {
        stock.decreasePromotion(quantity);
    }



    public boolean checkAndBuyWithPromotion(int quantity, boolean useNormalStockIfNeeded) {
        if (promotion == null || !promotion.isDate(DateTimes.now().toLocalDate())) {
            stock.decreaseNormal(quantity);
            return true;
        }

        int availablePromotionStock = stock.getPromotion();

        int promotionStockToDeduct = Math.min(quantity, availablePromotionStock);
        stock.decreasePromotion(promotionStockToDeduct);

        int normalStockToDeduct = quantity - promotionStockToDeduct;
        if (normalStockToDeduct > 0) {
            if (useNormalStockIfNeeded) {
                stock.decreaseNormal(normalStockToDeduct);
            } else {
                // 구매하지 않기로 선택한 경우
                return false;
            }
        }
        return true;
    }

    public void buy(int quantity, LocalDate localDate) {
        if (promotion == null || !promotion.isDate(localDate)) {
            stock.decreaseNormal(quantity);
            return;
        }
        stock.handlePromotionStock(quantity);
    }

    public boolean isPromotionDate(LocalDate currentDate) {
        return promotion.isDate(currentDate);
    }

    public boolean hasName(String name) {
        return this.name.equals(name);
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

    public int getTotalStock() {
        return stock.getTotal();
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public ProductDto toNormalDto() {
        return ProductDto.of(
                name, price, stock.getNormal(), null
        );
    }

    public ProductDto toPromotionDto() {
        return ProductDto.of(
                name, price, stock.getPromotion(), promotion.getName());
    }

    public int getPromotionStock() {
        return stock.getPromotion();
    }
}

