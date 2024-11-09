package store.domain;

import static store.exception.ErrorMessage.NOT_EXIST_PRODUCT;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import store.domain.product.Product;
import store.domain.product.Products;
import store.dto.PurchaseDto;
import store.dto.PurchaseItemDto;

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

    public PurchaseDto calculatePurchaseInfo(Products products, LocalDate localDate) {
        List<PurchaseItemDto> purchaseItemDtos = new ArrayList<>();
        int totalPrice = 0;
        int totalQuantity = 0;
        for (PurchaseItem item : items) {
            List<Product> matchedProducts = products.findProductByName(item.getName());
            if (matchedProducts.isEmpty()) {
                throw new IllegalArgumentException(NOT_EXIST_PRODUCT.getMessage());
            }
            Product product = matchedProducts.get(0);
            product.buy(item.getQuantity(), localDate);
            totalPrice += product.getPrice() * item.getQuantity();
            totalQuantity += item.getQuantity();
            purchaseItemDtos.add(PurchaseItemDto.from(item.getName(), product.getPrice(),
                    item.getQuantity()));
        }
        return PurchaseDto.from(purchaseItemDtos, totalPrice, totalQuantity);
    }

    public List<PurchaseItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
