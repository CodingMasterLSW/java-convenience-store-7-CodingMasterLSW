package store.domain;

public class Membership {

    private static final int MAXIMUM_DISCOUNT = 8_000;
    private static final double DISCOUNT_RATE = 0.3;

    private int currentDiscount;

    private Membership() {
        this.currentDiscount = 0;
    }

    public static Membership create() {
        return new Membership();
    }

    public int apply(int amount) {
        // 현재까지 남은 최대 할인 가능 금액 계산
        int remainingDiscount = MAXIMUM_DISCOUNT - currentDiscount;

        if (remainingDiscount <= 0) {
            return 0; // 최대 할인 한도에 도달하여 추가 할인 불가
        }

        // 이번 거래에서 적용 가능한 할인 금액 계산
        int potentialDiscount = (int) (amount * DISCOUNT_RATE);

        // 실제로 적용할 할인 금액은 남은 할인 한도와 계산된 할인 금액 중 최소값
        int discountAmount = Math.min(potentialDiscount, remainingDiscount);

        // 누적 할인 금액 업데이트
        currentDiscount += discountAmount;

        return discountAmount;
    }

    public int getCurrentDiscount() {
        return currentDiscount;
    }

}
