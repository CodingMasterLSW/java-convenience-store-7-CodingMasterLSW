package store.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.Promotion;
import store.domain.PromotionPeriod;

public class PromotionLoader {

    private static final String PRODUCTS_PATH = "src/main/esources/products.md";
    private static final String PROMOTION_PATH = "src/main/resources/promotions.md";

    public static List<Promotion> loadPromotion() throws IOException {
        List<Promotion> promotions = new ArrayList<>();

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

            PromotionPeriod promotionPeriod = PromotionPeriod.of(startDate, endDate);
            Promotion promotion = Promotion.of(name, buy, get, promotionPeriod);
            promotions.add(promotion);
        }
        return promotions;
    }

}
