package store.domain.promotion;

import java.time.LocalDate;

public class Period {

    private LocalDate startDate;
    private LocalDate endDate;

    private Period(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Period of(LocalDate startDate, LocalDate endDate) {
        return new Period(startDate, endDate);
    }

    public boolean isPromotionDate(LocalDate localDate) {
        return !localDate.isAfter(endDate) && !localDate.isBefore(startDate);
    }

}
