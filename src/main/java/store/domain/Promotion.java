package store.domain;

public class Promotion {

    private String name;
    private int buy;
    private int get;
    private PromotionPeriod promotionPeriod;

    private Promotion(String name, int buy, int get, PromotionPeriod promotionPeriod) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.promotionPeriod = promotionPeriod;
    }

    public static Promotion of(String name, int buy, int get, PromotionPeriod promotionPeriod) {
        return new Promotion(name, buy, get, promotionPeriod);
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

    public PromotionPeriod getPromotionPeriod() {
        return promotionPeriod;
    }
}
