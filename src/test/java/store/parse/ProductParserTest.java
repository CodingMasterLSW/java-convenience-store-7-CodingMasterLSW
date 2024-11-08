package store.parse;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.parser.ProductParser;
import store.domain.product.Product;
import store.domain.promotion.Period;
import store.domain.promotion.Promotion;

public class ProductParserTest {

    private Map<String, Promotion> promotions;

    @BeforeEach
    void setUp() {
        promotions = new HashMap<>();
        promotions.put("탄산2+1", Promotion.of("탄산2+1", 2, 1, Period.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))));
        promotions.put("MD추천상품", Promotion.of("MD추천상품", 1, 1, Period.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))));
        promotions.put("반짝할인", Promotion.of("반짝할인", 1, 1, Period.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))));

    }

    @Test
    void test() {
        ProductParser parser = ProductParser.create();

        List<String> lines = List.of(
                "콜라,1000,10,탄산2+1",
                "콜라,1000,10,null",
                "오렌지주스,1800,9,MD추천상품",
                "물,500,10,null",
                "감자칩,1500,5,반짝할인",
                "감자칩,1500,5,null",
                "정식도시락,6400,8,null"
        );
        List<Product> products = parser.parseProducts(lines, promotions);
        assertThat(products.size()).isEqualTo(5);

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getName, product -> product));

        assertThat(productMap.get("콜라").getName()).isEqualTo("콜라");
        assertThat(productMap.get("콜라").getStock().getNormal()).isEqualTo(10);
        assertThat(productMap.get("콜라").getStock().getPromotion()).isEqualTo(10);
        assertThat(productMap.get("콜라").getPromotion().getName()).isEqualTo("탄산2+1");
        assertThat(productMap.get("오렌지주스").getStock().getNormal()).isEqualTo(0);
        assertThat(productMap.get("오렌지주스").getStock().getPromotion()).isEqualTo(9);

    }

}
