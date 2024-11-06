package store.domain;

import java.time.LocalDate;

public class PromotionPeriod {

    private LocalDate startDate;
    private LocalDate endDate;

    private PromotionPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static PromotionPeriod of(LocalDate startDate, LocalDate endDate) {
        return new PromotionPeriod(startDate, endDate);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

}
