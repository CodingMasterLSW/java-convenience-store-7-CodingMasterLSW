package store.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.domain.promotion.Period;

public class FileLoader {

    private static final String PRODUCTS_PATH = "src/main/resources/products.md";
    private static final String PROMOTION_PATH = "src/main/resources/promotions.md";

    public static Map<String, Promotion> loadPromotion() {
        Map<String, Promotion> promotions = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(PROMOTION_PATH));
            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {
                List<String> split = List.of(line.split(","));
                String name = split.get(0);
                int buy = Integer.parseInt(split.get(1));
                int get = Integer.parseInt(split.get(2));
                LocalDate startDate = LocalDate.parse(split.get(3));
                LocalDate endDate = LocalDate.parse(split.get(4));

                Period promotionPeriod = Period.of(startDate, endDate);
                Promotion promotion = Promotion.of(name, buy, get, promotionPeriod);
                promotions.put(name, promotion);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        return promotions;
    }


    public static List<Product> loadProduct(Map<String, Promotion> promotions) {
        Map<String, Product> productMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCTS_PATH))) {
            String line;
            br.readLine(); // 헤더 라인 스킵
            while ((line = br.readLine()) != null) {
                processLine(line, productMap, promotions);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] 파일을 읽는 중 오류가 발생했습니다.", e);
        }
        return new ArrayList<>(productMap.values());
    }

    private static void processLine(String line, Map<String, Product> productMap, Map<String, Promotion> promotions) {
        List<String> split = List.of(line.split(","));
        String name = split.get(0);
        int price = Integer.parseInt(split.get(1));
        int quantity = Integer.parseInt(split.get(2));
        String promotionName = split.get(3);
        Product product = productMap.get(name);
        if (product != null) {
            processExistingProduct(product, price, quantity, promotionName, promotions);
            return;
        }
        Product newProduct = createProduct(name, price, quantity, promotionName, promotions);
        productMap.put(name, newProduct);
    }

    private static void processExistingProduct(Product product, int price, int quantity, String promotionName, Map<String, Promotion> promotions) {
        validatePriceConsistency(product, price);
        if (isNullOrEmpty(promotionName)) {
            product.addNormalStock(quantity);
            return;
        }
        Promotion promotion = getPromotion(promotionName, promotions);
        product.addPromotionStock(quantity, promotion);
    }

    private static Product createProduct(String name, int price, int quantity, String promotionName, Map<String, Promotion> promotions) {
        if (isNullOrEmpty(promotionName)) {
            return Product.ofNormal(name, price, quantity);
        }
        Promotion promotion = getPromotion(promotionName, promotions);
        return Product.ofPromotion(name, price, quantity, promotion);
    }

    private static void validatePriceConsistency(Product product, int price) {
        if (product.getPrice() != price) {
            throw new IllegalArgumentException("[ERROR] 동일한 상품에 다른 가격이 존재합니다: " + product.getName());
        }
    }

    private static Promotion getPromotion(String promotionName, Map<String, Promotion> promotions) {
        Promotion promotion = promotions.get(promotionName);
        if (promotion == null) {
            throw new IllegalArgumentException("[ERROR] 프로모션을 찾을 수 없습니다: " + promotionName);
        }
        return promotion;
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.equals("null");
    }

}
