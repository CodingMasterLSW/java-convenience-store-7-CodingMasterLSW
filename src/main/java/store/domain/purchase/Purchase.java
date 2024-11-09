package store.domain.purchase;

import static store.exception.ErrorMessage.OVER_STOCK_PURCHASE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.promotion.Promotion;
import store.domain.purchase.dto.PurchaseAlertDto;
import store.domain.purchase.dto.PurchaseDto;
import store.domain.purchase.dto.PurchaseItemDto;

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

            PurchaseAlertDto purchaseAlert = checkFreeProduct(localDate, item, product);
            if (purchaseAlert != null) {
                purchaseAlertDtos.add(purchaseAlert);
            }
        }
        return PurchaseDto.from(purchaseItemDtos, purchaseAlertDtos, totalPrice, totalQuantity);
    }

    public PurchaseAlertDto checkFreeProduct(LocalDate localDate, PurchaseItem item, Product product) {
        if (product.getPromotion() == null || !product.isPromotionDate(localDate)) {
            return null;
        }
        Promotion promotion = product.getPromotion();
        int requiredBuyQuantity = promotion.getBuy();
        int freeQuantity = promotion.getGet();
        int totalQuantity = item.getQuantity();

        // 프로모션 조건을 충족했지만 혜택을 받지 않았을 때 PromotionAlertDto 반환
        if (totalQuantity >= requiredBuyQuantity &&
                totalQuantity % (requiredBuyQuantity + freeQuantity) != 0) {
            return PurchaseAlertDto.from(product.getName(), freeQuantity);
        }
        return null;
    }

    private void buyProduct(LocalDate localDate, PurchaseItem item, Product product) {
        validatePurchaseQuantity(item.getQuantity(), product.getTotalStock());
        product.buy(item.getQuantity(), localDate);
    }

    private Product findProduct(Products products, PurchaseItem item) {
        List<Product> matchedProducts = products.findProductByName(item.getName());
        return matchedProducts.get(0);
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
