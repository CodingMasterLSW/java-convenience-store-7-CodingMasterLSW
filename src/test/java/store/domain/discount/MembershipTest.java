package store.domain.discount;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MembershipTest {

    Discount discount;

    @BeforeEach
    void init() {
        discount = Discount.create();
    }

    @DisplayName("멤버십 할인을 적용한다면, 30% 할인이 되어야한다.")
    @Test
    void membership_test() {
        int purchaseAmount = 10_000;
        int membershipAmount = discount.applyMembership(purchaseAmount);
        int expectedAmount = 3000;
        assertThat(membershipAmount).isEqualTo(expectedAmount);
    }

    @DisplayName("할인 금액이 8천원이 넘을 경우, 8천원이 적용되어야 한다.")
    @Test
    void validate_over_maximum_amount() {
        int purchaseAmount = 100_000;
        int membershipAmount = discount.applyMembership(purchaseAmount);
        int expectedAmount = 8000;
        assertThat(membershipAmount).isEqualTo(expectedAmount);
    }

    @DisplayName("상품을 여러번 구매했을 경우, 구적 할인금액은 8천원이 넘어선 안 된다")
    @Test
    void validate_purchase_many_time() {
        int purchaseAmount = 10_000;
        int first = discount.applyMembership(purchaseAmount); // 3000
        int second = discount.applyMembership(purchaseAmount); // 3000
        int third = discount.applyMembership(purchaseAmount); // 2000 ( 8000 초과이기에, 2000 적용)
        int fourth = discount.applyMembership(purchaseAmount); // 0
        assertThat(third).isEqualTo(2000);
        assertThat(fourth).isEqualTo(0);
    }
}
