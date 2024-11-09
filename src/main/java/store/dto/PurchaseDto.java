package store.dto;

import java.util.Collections;
import java.util.List;

public class PurchaseDto {

    private final List<PurchaseItemDto> purchaseItemDtos;
    private final int totalPrice;
    private final int totalQuantity;

    private PurchaseDto(List<PurchaseItemDto> purchaseItemDtos, int totalPrice, int totalQuantity) {
        this.purchaseItemDtos = purchaseItemDtos;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public static PurchaseDto from(List<PurchaseItemDto> purchaseItemDtos, int totalPrice, int totalQuantity) {
        return new PurchaseDto(purchaseItemDtos, totalPrice, totalQuantity);
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
}
