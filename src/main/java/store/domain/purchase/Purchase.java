package store.domain.purchase;

import static store.exception.ErrorMessage.OVER_STOCK_PURCHASE;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import store.domain.discount.Discount;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.promotion.Promotion;
import store.domain.purchase.dto.PromotionStockDto;
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

    public void calculatePurchaseInfo(Products products, LocalDate localDate,
            boolean isIncludeNormalStock) {
        for (PurchaseItem item : items) {
            processPurchaseItem(products, item, localDate, isIncludeNormalStock);
        }
    }

    public void applyMembershipDiscount(Products products) {
        int amountForMembershipDiscount = calculateAmountAfterPromotion(products);
        discount.applyMembership(amountForMembershipDiscount);
    }

    private int calculateAmountAfterPromotion(Products products) {
        int amount = 0;
        for (PurchaseItem item : items) {
            if (!item.isPromotionApplied()) {
                Product product = products.findProductByName(item.getName());
                amount += item.getQuantity() * product.getPrice();
            }
        }
        return amount;
    }

    private void processPurchaseItem(Products products, PurchaseItem item, LocalDate localDate,
            boolean isIncludeNormalStock) {
        Product product = products.findProductByName(item.getName());
        int requestedQuantity = item.getQuantity();
        validatePurchaseQuantity(requestedQuantity, product.getTotalStock());

        int nonPromotionQuantity = calculateNonPromotionQuantity(product, requestedQuantity);
        if (shouldAdjustQuantity(isIncludeNormalStock, nonPromotionQuantity)) {
            adjustPurchaseItemQuantity(item, requestedQuantity, nonPromotionQuantity);
        }
        int purchaseQuantity = item.getQuantity();
        int promotionApplicableQuantity = handleStockDeduction(product, purchaseQuantity,
                isIncludeNormalStock, localDate);
        updateTotals(purchaseQuantity, product.getPrice());
        applyPromotionDiscounts(product, item, promotionApplicableQuantity);
    }

    private int calculateNonPromotionQuantity(Product product, int purchaseQuantity) {
        return product.getNonPromotionQuantity(purchaseQuantity);
    }

    private boolean shouldAdjustQuantity(boolean isIncludeNormalStock, int nonPromotionQuantity) {
        return !isIncludeNormalStock && nonPromotionQuantity > 0;
    }

    private void adjustPurchaseItemQuantity(PurchaseItem item, int requestedQuantity,
            int nonPromotionQuantity) {
        int promotionApplicableQuantity = requestedQuantity - nonPromotionQuantity;
        item.setQuantity(promotionApplicableQuantity);
    }

    private int handleStockDeduction(Product product, int purchaseQuantity,
            boolean isIncludeNormalStock, LocalDate localDate) {
        if (product.hasPromotion() && product.isPromotionDate(localDate)) {
            return deductStockBasedOnNormalStock(product, purchaseQuantity, isIncludeNormalStock);
        }
        product.purchaseNormalProduct(purchaseQuantity);
        return 0;
    }

    private int deductStockBasedOnNormalStock(Product product, int purchaseQuantity, boolean isIncludeNormalStock) {
        if (isIncludeNormalStock) {
            return deductStockWithNormal(product, purchaseQuantity);
        }
        return deductStockOnlyPromotion(product, purchaseQuantity);
    }

    private int deductStockWithNormal(Product product, int purchaseQuantity) {
        int promotionApplicableQuantity = Math.min(purchaseQuantity, product.getPromotionStock());
        product.decreasePromotionStock(promotionApplicableQuantity);
        int normalQuantity = purchaseQuantity - promotionApplicableQuantity;
        if (normalQuantity > 0) {
            product.purchaseNormalProduct(normalQuantity);
        }
        return promotionApplicableQuantity;
    }

    private int deductStockOnlyPromotion(Product product, int purchaseQuantity) {
        product.decreasePromotionStock(purchaseQuantity);
        return purchaseQuantity;
    }

    private void updateTotals(int purchaseQuantity, int price) {
        totalPrice += price * purchaseQuantity;
        totalQuantity += purchaseQuantity;
    }

    private void applyPromotionDiscounts(Product product, PurchaseItem item, int promotionApplicableQuantity) {
        if (promotionApplicableQuantity > 0) {
            Promotion promotion = product.getPromotion();
            int freeQuantity = calculateFreeQuantity(promotion, promotionApplicableQuantity);
            discount.addPromotionAmount(freeQuantity * product.getPrice());
            if (freeQuantity > 0) {
                purchaseGifts.addGift(PurchaseGift.of(product.getName(), freeQuantity));
                item.changePromotionStatus();
            }
        }
    }

    private int calculateFreeQuantity(Promotion promotion, int promotionApplicableQuantity) {
        int requiredQuantityPerSet = promotion.getBuy();
        int freeQuantityPerSet = promotion.getGet();
        int totalPromoUnits = requiredQuantityPerSet + freeQuantityPerSet;
        int applicableSets = promotionApplicableQuantity / totalPromoUnits;
        return applicableSets * freeQuantityPerSet;
    }

    public PromotionStockDto verifyPromotionStock(Products products) {
        for (PurchaseItem item : items) {
            int purchaseQuantity = item.getQuantity();
            Product product = products.findProductByName(item.getName());
            int lackPromotionStock = product.hasEnoughPromotionStock(purchaseQuantity);
            if (lackPromotionStock > 0 && product.lackOfPromotion(purchaseQuantity)) {
                return PromotionStockDto.of(product.getName(), lackPromotionStock);
            }
        }
        return null;
    }

    public List<PurchaseItemDto> getItemsAsDto(Products products) {
        return items.stream()
                .map(item -> {
                    Product product = products.findProductByName(item.getName());
                    int price = product.getPrice();
                    return PurchaseItemDto.of(item.getName(), price, item.getQuantity());
                })
                .collect(Collectors.toList());
    }

    public List<PurchaseItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<PurchaseGiftDto> getGiftsAsDto(PurchaseGifts purchaseGifts) {
        return Optional.ofNullable(purchaseGifts)
                .map(gifts -> gifts.getGifts().stream()
                        .map(PurchaseGift::toDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public PurchaseDto toDto(Products products) {
        int finalAmount =
                totalPrice - discount.getPromotionAmount() - discount.getMembershipAmount();
        return new PurchaseDto(
                getItemsAsDto(products),
                getGiftsAsDto(purchaseGifts),
                totalPrice,
                totalQuantity,
                discount.getPromotionAmount(),
                discount.getMembershipAmount(),
                finalAmount
        );
    }

    private void validatePurchaseQuantity(int purchaseQuantity, int totalStock) {
        if (purchaseQuantity > totalStock) {
            throw new IllegalArgumentException(OVER_STOCK_PURCHASE.getMessage());
        }
    }

}
