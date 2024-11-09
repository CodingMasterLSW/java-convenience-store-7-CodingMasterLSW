package store.domain.purchase.dto;

import java.util.Collections;
import java.util.List;

public class PurchaseDto {

    private final List<PurchaseItemDto> purchaseItemDtos;
    private final int totalPrice;
    private final int totalQuantity;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final int finalAmount;

    private PurchaseDto(List<PurchaseItemDto> purchaseItemDtos, int totalPrice, int totalQuantity, int promotionDiscount, int membershipDiscount, int finalAmount) {
        this.purchaseItemDtos = purchaseItemDtos;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.finalAmount = finalAmount;
    }

    public static PurchaseDto from(List<PurchaseItemDto> purchaseItemDtos, int totalPrice, int totalQuantity, int promotionDiscount, int membershipDiscount, int finalAmount) {
        return new PurchaseDto(purchaseItemDtos, totalPrice, totalQuantity, promotionDiscount, membershipDiscount, finalAmount);
    }

    public List<PurchaseItemDto> getPurchaseItemDtos() {
        return Collections.unmodifiableList(purchaseItemDtos);
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
