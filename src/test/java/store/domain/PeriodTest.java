package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.promotion.Period;

public class PeriodTest {

    @ParameterizedTest
    @ValueSource(strings = {"2024-11-06", "2024-11-22", "2024-11-30"})
    void test1(String value) {
        LocalDate startDate = LocalDate.parse("2024-11-06");
        LocalDate endDate = LocalDate.parse("2024-11-30");
        Period period = Period.of(startDate, endDate);
        boolean promotionDate = period.isPromotionDate(LocalDate.parse(value));
        assertThat(promotionDate).isTrue();
    }


    @ParameterizedTest
    @ValueSource(strings = {"2024-11-05", "2023-11-09", "2025-11-02"})
    void test2(String value) {
        LocalDate startDate = LocalDate.parse("2024-11-06");
        LocalDate endDate = LocalDate.parse("2024-11-30");
        Period period = Period.of(startDate, endDate);

        boolean promotionDate = period.isPromotionDate(LocalDate.parse(value));
        assertThat(promotionDate).isFalse();
    }

}
