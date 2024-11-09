package store.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static store.exception.ErrorMessage.MINIMUM_PURCHASE_AMOUNT;
import static store.exception.ErrorMessage.NOT_EXIST_PRODUCT;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.purchase.PurchaseItem;
import store.domain.product.Product;
import store.domain.product.Products;

public class InputParserTest {

    private Products products;
    private InputParser inputParser;

    @BeforeEach
    void init() {
        Product product1 =  Product.of("바나나", 1000, 10, 10, null);
        Product product2 = Product.of("키위", 2000, 5, 5, null);
        List<Product> tmpProducts = List.of(product1, product2);
        products = Products.create(tmpProducts);
        inputParser = InputParser.from(products);
    }

    @DisplayName("사용자의 입력이 주어졌을 때, 구매 리스트로 변환한다.")
    @Test
    void input_parser_test() {
        String userInput = "[바나나-2],[키위-1]";
        List<PurchaseItem> purchaseItems = inputParser.parseInputToItems(userInput);

        assertThat(purchaseItems)
                .containsExactly(
                        PurchaseItem.of("바나나", 2),
                        PurchaseItem.of("키위", 1));
    }

    @DisplayName("존재하지 않은 상품을 입력했을 때, 예외가 발생한다.")
    @Test
    void not_exist_product_then_throw_exception() {
        String userInput = "[바나나-2],[키위-1],[토마토-3]";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            List<PurchaseItem> purchaseItems = inputParser.parseInputToItems(userInput);
        });
        assertThat(exception.getMessage()).isEqualTo(NOT_EXIST_PRODUCT.getMessage());
    }

    @DisplayName("상품 구매수량이 0이하일 경우, 예외가 발생한다")
    @Test
    void not_minimum_product_purchase_stock() {
        String userInput = "[바나나-0],[키위-1]";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            List<PurchaseItem> purchaseItems = inputParser.parseInputToItems(userInput);
        });
        assertThat(exception.getMessage()).isEqualTo(MINIMUM_PURCHASE_AMOUNT.getMessage());
    }

}
