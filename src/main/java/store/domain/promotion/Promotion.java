package store.domain.promotion;

import java.time.LocalDate;

public class Promotion {

    private String name;
    private int buy;
    private int get;
    private Period period;

    private Promotion(String name, int buy, int get, Period period) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.period = period;
    }

    public static Promotion of(String name, int buy, int get, Period promotionPeriod) {
        return new Promotion(name, buy, get, promotionPeriod);
    }

    public boolean isDate(LocalDate localDate) {
        return period.isPromotionDate(localDate);
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

}
