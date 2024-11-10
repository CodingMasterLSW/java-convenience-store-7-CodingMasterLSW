package store.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;
import store.domain.product.Products;

public class PurchaseTest {
/*
    PurchaseItem purchaseItem1;
    PurchaseItem purchaseItem2;
    PurchaseItem purchaseItem3;
    List<PurchaseItem> items;
    List<Product> tmpProducts;

    Product product1;
    Product product2;

    @BeforeEach
    void init() {

        purchaseItem1 = PurchaseItem.of("사과", 2);
        purchaseItem2 = PurchaseItem.of("바나나", 1);
        items = List.of(
                purchaseItem1,
                purchaseItem2
        );
        product1 = Product.of("사과", 3000, 10, 10, null);
        product2 = Product.of("바나나", 2000, 5, 5, null);
        tmpProducts = List.of(
                product1,
                product2
        );
    }

    @DisplayName("프로모션이 없는 상품을 구매했을 때, 성공적으로 구매 가격이 반환되고, 일반재고가 줄어든다")
    @Test
    void success_purchase_then_return_total_price() {
        Products products = Products.create(tmpProducts);

        LocalDate localDate = LocalDate.of(2024,11,22);
        Purchase purchase = Purchase.from(items, localDate);
        PurchaseDto purchaseDto = purchase.calculatePurchaseInfo(products, localDate);
        assertThat(purchaseDto.getTotalPrice()).isEqualTo(8000);
        assertThat(purchaseDto.getTotalQuantity()).isEqualTo(3);
        assertThat(product1.getStock().getNormal()).isEqualTo(8);
        assertThat(product1.getStock().getPromotion()).isEqualTo(10);

        assertThat(product2.getStock().getNormal()).isEqualTo(4);
        assertThat(product2.getStock().getPromotion()).isEqualTo(5);
    }


 */
}
