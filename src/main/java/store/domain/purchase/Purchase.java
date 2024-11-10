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
import store.domain.purchase.dto.PurchaseGiftDto;
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

    // 필드값 설정하는 메서드. totalPrice, totalQuantity, discount, purchaseGifts 설정
    public void calculatePurchaseInfo(Products products, LocalDate localDate, boolean isContinue) {
        for (PurchaseItem item : items) {
            int purchaseQuantity = item.getQuantity();
            Product product = products.findProductByName(item.getName());
            decreaseStock(localDate, isContinue, product, purchaseQuantity);
            int price = product.getPrice();
            totalPrice += price * purchaseQuantity;
            totalQuantity += purchaseQuantity;
            checkAndApplyPromotion(localDate, item, product, purchaseQuantity, price);
        }
    }

    public List<PurchaseItemDto> getItemsAsDTO(Products products) {
        return items.stream()
                .map(item -> {
                    Product product = products.findProductByName(item.getName());
                    int price = product.getPrice();
                    return PurchaseItemDto.of(item.getName(), price, item.getQuantity());
                })
                .collect(Collectors.toList());
    }

    List<PurchaseGiftDto> giftDtos = Optional.ofNullable(purchaseGifts)
            .map(gifts -> gifts.getGifts().stream()
                    .map(PurchaseGift::toDto)
                    .collect(Collectors.toList()))
            .orElse(Collections.emptyList());

    public PurchaseDto toDto(Products products) {
        int finalAmount = totalPrice - discount.getPromotionAmount() - discount.getMembershipAmount();
        return new PurchaseDto(
                getItemsAsDTO(products),
                giftDtos,
                totalPrice,
                totalQuantity,
                discount.getPromotionAmount(),
                discount.getMembershipAmount(),
                finalAmount
        );
    }

    private void decreaseStock(LocalDate localDate, boolean isContinue, Product product, int purchaseQuantity) {
        if (product.hasPromotion() && product.isPromotionDate(localDate)) {
            product.notEnoughPromotionProduct(purchaseQuantity, isContinue);
            return;
        }
        product.purchaseNormalProduct(purchaseQuantity);
    }

    private void checkAndApplyPromotion(LocalDate localDate, PurchaseItem item, Product product, int quantity,
            int price) {
        if (product.hasPromotion() && product.isPromotionDate(localDate)) {
            Promotion promotion = product.getPromotion();
            applyPromotionDiscounts(item, promotion, quantity, price);
        }
    }

    private void applyPromotionDiscounts(PurchaseItem item, Promotion promotion, int quantity, int price) {
        int requiredQuantityPerSet = promotion.getBuy();
        int freeQuantityPerSet = promotion.getGet();

        int applicableSets = quantity / requiredQuantityPerSet;
        int freeQuantity = applicableSets * freeQuantityPerSet;

        discount.addPromotionAmount(freeQuantity * price);
        purchaseGifts.addGift(PurchaseGift.of(item.getName(), freeQuantity));
    }
    ////////////////////////////////////////// 리팩토링 구분자
    private int calculateFreeItems(int quantity, Promotion promotion) {
        int requiredBuyQuantity = promotion.getBuy();
        int freeQuantity = promotion.getGet();

        // 구매 수량을 기준으로 프로모션 혜택 수량 계산
        int applicableTimes = quantity / (requiredBuyQuantity + freeQuantity);
        return applicableTimes * freeQuantity;
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

    private void validatePurchaseQuantity(int purchaseQuantity, int totalStock) {
        if (purchaseQuantity > totalStock) {
            throw new IllegalArgumentException(OVER_STOCK_PURCHASE.getMessage());
        }
    }
}

