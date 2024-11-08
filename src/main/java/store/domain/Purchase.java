package store.domain;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import store.domain.product.Product;
import store.domain.product.Products;

public class Purchase {

    private final List<PurchaseItem> items;
    private LocalDate currentDate;

    private Purchase(List<PurchaseItem> items, LocalDate currentDate) {
        this.items = items;
        this.currentDate = currentDate;
    }

    public static Purchase from(List<PurchaseItem> items, LocalDate currentDate) {
        return new Purchase(items, currentDate);
    }

    public int execute(Products products, LocalDate localDate) {
        int totalPrice = 0;
        for (PurchaseItem item : items) {
            List<Product> matchedProducts = products.findProductByName(item.getName());
            if (matchedProducts.isEmpty()) {
                throw new IllegalArgumentException("[ERROR] 상품이 존재하지 않습니다.");
            }
            Product product = matchedProducts.get(0);
            product.buy(item.getQuantity(), localDate);
            totalPrice += product.getPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    public List<PurchaseItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}