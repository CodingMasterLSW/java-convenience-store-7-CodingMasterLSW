package store.utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.promotion.Period;
import store.domain.promotion.Promotion;

public class PromotionParser {

    private PromotionParser() {
    }

    public static PromotionParser create() {
        return new PromotionParser();
    }

    public Map<String, Promotion> parsePromotion(List<String> lines) {
        Map<String, Promotion> promotionMaps = new HashMap<>();
        for (String line : lines) {
            Promotion promotion = parseLineTopromotion(line);
            promotionMaps.put(promotion.getName(), promotion);
        }
        return promotionMaps;
    }

    private Promotion parseLineTopromotion(String line) {
        List<String> split = List.of(line.split(","));
        String name = split.get(0);
        int buy = Integer.parseInt(split.get(1));
        int get = Integer.parseInt(split.get(2));
        LocalDate startDate = LocalDate.parse(split.get(3));
        LocalDate endDate = LocalDate.parse(split.get(4));
        Period promotionPeriod = Period.of(startDate, endDate);
        return Promotion.of(name, buy, get, promotionPeriod);
    }

}