package store.domain.purchase;

import static store.exception.ErrorMessage.OVER_STOCK_PURCHASE;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import store.domain.Discount;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.promotion.Promotion;
import store.domain.purchase.dto.PurchaseDto;
import store.domain.purchase.dto.PurchaseItemDto;

public class Purchase {

    private final List<PurchaseItem> items;
    private int totalPrice;
    private int totalQuantity;
    private Discount discount;
    private PurchaseGifts purchaseGifts;

    private Purchase(List<PurchaseItem> items) {
        this.items = items;
        totalPrice = 0;
        totalQuantity = 0;
        discount = Discount.create();
        this.purchaseGifts = PurchaseGifts.create();
    }

    public static Purchase from(List<PurchaseItem> items) {
        return new Purchase(items);
    }

    public void calculatePurchaseInfo(Products products, LocalDate localDate) {
        for (PurchaseItem item : items) {
            Product product = findProduct(products, item);
            int quantity = item.getQuantity();
            int price = product.getPrice();

            totalPrice += price * quantity;
            totalQuantity += quantity;

            Promotion promotion = product.getPromotion();
            if (promotion != null && product.isPromotionDate(localDate)) {
                int freeQuantityPerSet = promotion.getGet();
                int applicableSets = item.getPromotionSetsApplied(); // 실제 적용된 프로모션 세트 수 사용

                int freeQuantity = applicableSets * freeQuantityPerSet;

                discount.addPromotionAmount(freeQuantity * price);
                purchaseGifts.addGift(PurchaseGift.of(item.getName(), freeQuantity));
            }
        }
    }

    private int calculateFreeItems(int quantity, Promotion promotion) {
        int requiredBuyQuantity = promotion.getBuy();
        int freeQuantity = promotion.getGet();

        // 구매 수량을 기준으로 프로모션 혜택 수량 계산
        int applicableTimes = quantity / (requiredBuyQuantity + freeQuantity);
        return applicableTimes * freeQuantity;
    }

    public PurchaseDto toDto(Products products) {
        List<PurchaseItemDto> purchaseItemDtos = items.stream()
                .map(item -> {
                    Product product = findProduct(products, item);
                    return PurchaseItemDto.from(item.getName(), product.getPrice(), item.getQuantity());
                })
                .collect(Collectors.toList());

        int promotionDiscount = discount.getPromotionAmount();
        int membershipDiscount = discount.getMembershipAmount();
        int finalAmount = totalPrice - promotionDiscount - membershipDiscount;

        return PurchaseDto.from(
                purchaseItemDtos,
                totalPrice,
                totalQuantity,
                promotionDiscount,
                membershipDiscount,
                finalAmount
        );
    }

    public PurchaseGifts getPurchaseGifts() {
        return purchaseGifts;
    }

    public void addPromotionDiscount(int price) {
        discount.addPromotionAmount(price);
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

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    private void validatePurchaseQuantity(int purchaseQuantity, int totalStock) {
        if (purchaseQuantity > totalStock) {
            throw new IllegalArgumentException(OVER_STOCK_PURCHASE.getMessage());
        }
    }
}

