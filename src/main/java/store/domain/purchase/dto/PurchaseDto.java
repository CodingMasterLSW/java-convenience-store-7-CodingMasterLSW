package store.domain.purchase.dto;

import java.util.Collections;
import java.util.List;

public class PurchaseDto {

    private final List<PurchaseItemDto> purchaseItemDtos;
    private final List<PurchaseAlertDto> purchaseAlertDtos;
    private final int totalPrice;
    private final int totalQuantity;

    private PurchaseDto(List<PurchaseItemDto> purchaseItemDtos, List<PurchaseAlertDto> purchaseAlertDtos,
            int totalPrice, int totalQuantity) {
        this.purchaseItemDtos = purchaseItemDtos;
        this.purchaseAlertDtos = purchaseAlertDtos;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public static PurchaseDto from(List<PurchaseItemDto> purchaseItemDtos, List<PurchaseAlertDto> purchaseAlertDtos,
            int totalPrice, int totalQuantity) {
        return new PurchaseDto(purchaseItemDtos, purchaseAlertDtos, totalPrice, totalQuantity);
    }

    public List<PurchaseItemDto> getPurchaseItemDtos() {
        return Collections.unmodifiableList(purchaseItemDtos);
    }

    public List<PurchaseAlertDto> getPurchaseAlertDtos() {
        return Collections.unmodifiableList(purchaseAlertDtos);
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }
}
