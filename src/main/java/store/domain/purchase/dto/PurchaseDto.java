package store.domain.purchase.dto;

import java.util.List;

public class PurchaseDto {
    private List<PurchaseItemDto> items;
    private List<PurchaseGiftDto> gifts;
    private int totalPrice;
    private int totalQuantity;
    private int promotionDiscount;
    private int membershipDiscount;
    private int finalAmount;

    public PurchaseDto(List<PurchaseItemDto> items, List<PurchaseGiftDto> gifts, int totalPrice, int totalQuantity, int promotionDiscount, int membershipDiscount, int finalAmount) {
        this.items = items;
        this.gifts = gifts;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.finalAmount = finalAmount;
    }

    public List<PurchaseItemDto> getItems() {
        return items;
    }

    public List<PurchaseGiftDto> getGifts() {
        return gifts;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }
}