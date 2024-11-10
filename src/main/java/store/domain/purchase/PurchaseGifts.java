package store.domain.purchase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PurchaseGifts {

    private final List<PurchaseGift> gifts;

    private PurchaseGifts() {
        this.gifts = new ArrayList<>();
    }

    public static PurchaseGifts create() {
        return new PurchaseGifts();
    }

    public List<PurchaseGift> getGifts() {
        return Collections.unmodifiableList(gifts);
    }

    public void addGift(PurchaseGift purchaseGift) {
        gifts.add(purchaseGift);
    }
}
