package store.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import store.domain.product.Stock;

public class StockTest {

    @Test
    void stock_exception_test() {
        assertThrows(IllegalArgumentException.class, () -> {
            Stock stock = Stock.from(0, -1);
        });

    }

    @Test
    void stock_decrease_test() {
        Stock stock = Stock.from(10, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            stock.decreaseNormal(11);
        });
    }

}
