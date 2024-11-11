package store.domain.purchase;

import store.domain.promotion.Promotion;
import store.domain.purchase.dto.PurchaseAlertDto;

public class PurchaseAlert {

    private final String itemName;
    private final int freeQuantity;
    private final boolean isApplicable;

    public PurchaseAlert(String itemName, int freeQuantity, boolean isApplicable) {
        this.itemName = itemName;
        this.freeQuantity = freeQuantity;
        this.isApplicable = isApplicable;
    }

    public static PurchaseAlert of(String productName, Promotion promotion, int currentQuantity) {
        int requiredBuyQuantity = promotion.getBuy();
        int freeQuantity = promotion.getGet();
        boolean isApplicable = currentQuantity >= requiredBuyQuantity &&
                currentQuantity % (requiredBuyQuantity + freeQuantity) != 0;
        return new PurchaseAlert(productName, freeQuantity, isApplicable);
    }

    public boolean isApplicable() {
        return isApplicable;
    }

    public String getItemName() {
        return itemName;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

}
