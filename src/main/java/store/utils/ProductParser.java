package store.utils;

import static store.exception.ErrorMessage.NOT_FIND_PROMOTION;
import static store.exception.ErrorMessage.SAME_PRODUCT_ANOTHER_PURCHASE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.product.Product;
import store.domain.promotion.Promotion;

public class ProductParser {

    private static final String SPLIT_DELIMITER = ",";
    private static final String NULL = "null";

    private ProductParser() {
    }

    public static ProductParser create() {
        return new ProductParser();
    }

    public List<Product> parseProducts(List<String> lines, Map<String, Promotion> promotions) {
        Map<String, Product> productMap = new HashMap<>();
        for (String line : lines) {
            Product product = parseLineToProduct(line, productMap, promotions);
            productMap.putIfAbsent(product.getName(), product);
        }
        return new ArrayList<>(productMap.values());
    }

    private Product parseLineToProduct(String line, Map<String, Product> productMap,
            Map<String, Promotion> promotions) {
        List<String> split = parseLine(line);
        String name = split.get(0);
        int price = Integer.parseInt(split.get(1));
        int quantity = Integer.parseInt(split.get(2));
        String promotionName = split.get(3);
        return createOrUpdateProduct(name, price, quantity, promotionName, productMap, promotions);
    }

    private List<String> parseLine(String line) {
        return List.of(line.split(SPLIT_DELIMITER));
    }

    private Product createOrUpdateProduct(String name, int price, int quantity,
            String promotionName,
            Map<String, Product> productMap, Map<String, Promotion> promotions) {
        Product existingProduct = productMap.get(name);
        if (existingProduct != null) {
            addStockToExistingProduct(existingProduct, price, quantity, promotionName);
            return existingProduct;
        }
        return createNewProduct(name, price, quantity, promotionName, promotions);
    }

    private Product createNewProduct(String name, int price, int quantity, String promotionName,
            Map<String, Promotion> promotions) {
        if (isNullOrEmpty(promotionName)) {
            return Product.ofNormal(name, price, quantity);
        }
        Promotion promotion = getPromotion(promotionName, promotions);
        return Product.ofPromotion(name, price, quantity, promotion);
    }

    private void addStockToExistingProduct(Product product, int price, int quantity,
            String promotionName) {
        validatePriceConsistency(product, price);
        if (isNullOrEmpty(promotionName)) {
            product.addNormalStock(quantity);
            return;
        }
        product.addPromotionStock(quantity);
    }

    private void validatePriceConsistency(Product product, int price) {
        if (product.getPrice() != price) {
            throw new IllegalArgumentException(SAME_PRODUCT_ANOTHER_PURCHASE.getMessage());
        }
    }

    private Promotion getPromotion(String promotionName, Map<String, Promotion> promotions) {
        Promotion promotion = promotions.get(promotionName);
        if (promotion == null) {
            throw new IllegalArgumentException(NOT_FIND_PROMOTION.getMessage());
        }
        return promotion;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.equals(NULL);
    }

}
