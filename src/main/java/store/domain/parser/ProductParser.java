package store.domain.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.product.Product;
import store.domain.promotion.Promotion;

public class ProductParser {

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

    private Product parseLineToProduct(String line, Map<String, Product> productMap, Map<String, Promotion> promotions) {
        List<String> split = List.of(line.split(","));
        String name = split.get(0);
        int price = Integer.parseInt(split.get(1));
        int quantity = Integer.parseInt(split.get(2));
        String promotionName = split.get(3);

        Product existingProduct = productMap.get(name);
        if (existingProduct != null) {
            addStockToExistingProduct(existingProduct, price, quantity, promotionName, promotions);
            return existingProduct;
        }
        if (isNullOrEmpty(promotionName)) {
            return Product.ofNormal(name, price, quantity);
        }
        Promotion promotion = getPromotion(promotionName, promotions);
        return Product.ofPromotion(name, price, quantity, promotion);
    }

    private void addStockToExistingProduct(Product product, int price, int quantity, String promotionName, Map<String, Promotion> promotions) {
        validatePriceConsistency(product, price);
        if (isNullOrEmpty(promotionName)) {
            product.getStock().addNormal(quantity);
        } else {
            product.getStock().addPromotion(quantity);
        }
    }

    private void validatePriceConsistency(Product product, int price) {
        if (product.getPrice() != price) {
            throw new IllegalArgumentException("[ERROR] 동일한 상품에 다른 가격이 존재합니다: " + product.getName());
        }
    }

    private Promotion getPromotion(String promotionName, Map<String, Promotion> promotions) {
        Promotion promotion = promotions.get(promotionName);
        if (promotion == null) {
            throw new IllegalArgumentException("[ERROR] 프로모션을 찾을 수 없습니다: " + promotionName);
        }
        return promotion;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.equals("null");
    }

}
