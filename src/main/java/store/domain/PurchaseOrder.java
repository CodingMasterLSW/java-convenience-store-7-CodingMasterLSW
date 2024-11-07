package store.domain;

import java.util.Collections;
import java.util.List;

public class PurchaseOrder {

    private List<PurchaseItem> items;

    private PurchaseOrder(List<PurchaseItem> items) {
        this.items = items;
    }

    public static PurchaseOrder from(List<PurchaseItem> items) {
        return new PurchaseOrder(items);
    }

    public List<PurchaseItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
