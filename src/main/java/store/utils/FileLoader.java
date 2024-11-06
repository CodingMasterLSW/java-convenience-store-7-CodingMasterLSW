package store.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.product.NormalProduct;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.domain.promotion.Period;
import store.domain.product.PromotionProduct;

public class FileLoader {

    private static final String PRODUCTS_PATH = "src/main/resources/products.md";
    private static final String PROMOTION_PATH = "src/main/resources/promotions.md";

    public static Map<String, Promotion> loadPromotion() throws IOException {
        Map<String, Promotion> promotions = new HashMap<>();

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
        return promotions;
    }

    public static List<Product> loadProduct(Map<String, Promotion> promotions) throws IOException {
        List<Product> products = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(PRODUCTS_PATH));
        String line;

        br.readLine();

        while ((line = br.readLine()) != null) {
            List<String> split = List.of(line.split(","));
            String name = split.get(0);
            int price = Integer.parseInt(split.get(1));
            int quantity = Integer.parseInt(split.get(2));
            String promotionName = split.get(3);

            if (promotionName == null || promotionName.equals("null")) {
                Product product = NormalProduct.of(name, price, quantity);
                products.add(product);
                continue;
            }

            Promotion promotion = promotions.get(promotionName);
            Product product = PromotionProduct.of(name, price, quantity, promotion);
            products.add(product);
        }
        return products;

    }

}
