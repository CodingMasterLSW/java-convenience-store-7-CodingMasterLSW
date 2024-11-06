package store.domain.promotion;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import store.utils.PromotionLoader;

public class Promotions {

    private Map<String, Promotion> promotions;

    private Promotions(Map<String, Promotion> promotions) {
        this.promotions = promotions;
    }

    public static Promotions create() throws IOException {
        return new Promotions(PromotionLoader.loadPromotion());
    }

    public Map<String, Promotion> getPromotions() {
        return Collections.unmodifiableMap(promotions);
    }
}
