package store.domain.purchase;

import static store.exception.ErrorMessage.OVER_STOCK_PURCHASE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.promotion.Promotion;
import store.domain.purchase.dto.PurchaseAlertDto;
import store.domain.purchase.dto.PurchaseDto;
import store.domain.purchase.dto.PurchaseItemDto;

public class Purchase {

    private final List<PurchaseItem> items;

    private Purchase(List<PurchaseItem> items) {
        this.items = items;
    }

    public static Purchase from(List<PurchaseItem> items) {
        return new Purchase(items);
    }

    public PurchaseDto calculatePurchaseInfo(Products products, LocalDate localDate) {
        List<PurchaseItemDto> purchaseItemDtos = new ArrayList<>();
        List<PurchaseAlertDto> purchaseAlertDtos = new ArrayList<>();

        int totalPrice = 0;
        int totalQuantity = 0;
        for (PurchaseItem item : items) {
            Product product = findProduct(products, item);
            buyProduct(localDate, item, product);
            totalPrice += product.getPrice() * item.getQuantity();
            totalQuantity += item.getQuantity();
            purchaseItemDtos.add(PurchaseItemDto.from(item.getName(), product.getPrice(),
                    item.getQuantity()));
        }
        return PurchaseDto.from(purchaseItemDtos, purchaseAlertDtos, totalPrice, totalQuantity);
    }


    private void buyProduct(LocalDate localDate, PurchaseItem item, Product product) {
        validatePurchaseQuantity(item.getQuantity(), product.getTotalStock());
        product.buy(item.getQuantity(), localDate);
    }

    private Product findProduct(Products products, PurchaseItem item) {
        List<Product> matchedProducts = products.findProductByName(item.getName());
        return matchedProducts.get(0);
    }

    public Optional<PurchaseItem> findItemByName(String name) {
        return items.stream()
                .filter(purchaseItem -> purchaseItem.getName().equals(name))
                .findFirst();
    }

    public List<PurchaseItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private void validatePurchaseQuantity(int purchaseQuantity, int totalStock) {
        if (purchaseQuantity > totalStock) {
            throw new IllegalArgumentException(OVER_STOCK_PURCHASE.getMessage());
        }
    }
}
