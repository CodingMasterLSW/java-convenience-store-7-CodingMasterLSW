package store.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import store.utils.PromotionParser;
import store.domain.promotion.Promotion;

public class PromotionParserTest {

    @Test
    void test() {
        PromotionParser promotionParser = PromotionParser.create();
        List<String> lines = List.of(
                "탄산2+1,2,1,2024-01-01,2024-12-31",
                "MD추천상품,1,1,2024-01-01,2024-12-31",
                "반짝할인,1,1,2024-11-01,2024-11-30"
        );
        Map<String, Promotion> promotions = promotionParser.parsePromotion(lines);
        assertThat(promotions.size()).isEqualTo(3);

        Promotion promotion1 = promotions.get("탄산2+1");
        assertThat(promotion1).isNotNull();
        assertThat(promotion1.getName()).isEqualTo("탄산2+1");
        assertThat(promotion1.getBuy()).isEqualTo(2);
    }

}
