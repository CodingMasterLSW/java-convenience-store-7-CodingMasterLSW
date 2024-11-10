package store.domain.purchase.dto;

import java.util.List;

public class PurchaseDto {
    private List<PurchaseItemDto> items;  // 구매한 상품 리스트
    private List<PurchaseGiftDto> gifts;  // 증정품 리스트
    private int totalPrice;               // 총 구매액
    private int totalQuantity;            // 총 구매 수량
    private int promotionDiscount;        // 행사 할인 금액
    private int membershipDiscount;       // 멤버십 할인 금액
    private int finalAmount;              // 내실 돈 (최종 결제 금액)

    public PurchaseDto(List<PurchaseItemDto> items, List<PurchaseGiftDto> gifts, int totalPrice, int totalQuantity, int promotionDiscount, int membershipDiscount, int finalAmount) {
        this.items = items;
        this.gifts = gifts;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.finalAmount = finalAmount;
    }

    // Getters
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