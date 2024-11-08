package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;
import store.domain.promotion.Period;
import store.domain.promotion.Promotion;

public class ProductTest {

    Promotion promotion;
    Product product;

    @BeforeEach
    void init() {
        promotion = Promotion.of("1+1이벤트", 1, 1,
                Period.of(
                        LocalDate.of(2024, 11, 01),
                        LocalDate.of(2024, 11, 30))
        );
        product = Product.of("콜라", 1500, 10, 8, promotion);
    }

    @DisplayName("프로모션 날짜에 해당될 경우, 구매한 수량 만큼 프로모션 재고가 줄어들어야 한다.")
    @Test
    void contain_promotion_date() {

        assertStock(8,
                LocalDate.of(2024,11,15),
                10, 0);
    }

    @DisplayName("프로모션 날짜에 해당되고 프로모션 재고가 부족할 경우, 기본재고도 함께 줄어들어야 한다")
    @Test
    void contain_promotion_date_and_not_enough_promotion_stock_case() {
        assertStock(10,
                LocalDate.of(2024,11,15),
                8, 0);
    }

    @DisplayName("프로모션 날짜에 해당되지 않을 경우, 일반재고가 줄어들어야 한다")
    @Test
    void not_contains_promotion_date() {
        assertStock(8,
                LocalDate.of(2024,12,01),
                2,8);
    }

    private void assertStock(int quantity, LocalDate localDate,
            int expectedNormalStock, int expectedPromotionStock) {
        product.buy(quantity, localDate);
        assertThat(product.getStock().getNormal()).isEqualTo(expectedNormalStock);
        assertThat(product.getStock().getPromotion()).isEqualTo(expectedPromotionStock);
    }

}
