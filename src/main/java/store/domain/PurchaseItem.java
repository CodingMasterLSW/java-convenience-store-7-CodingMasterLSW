package store.domain;

import java.util.Objects;

public class PurchaseItem {

    private final String name;
    private int quantity;

    private PurchaseItem(String name, int quantity) {
        validateQuantity(quantity);
        this.name = name;
        this.quantity = quantity;
    }

    public static PurchaseItem of(String name, int quantity) {
        return new PurchaseItem(name, quantity);
    }

    public void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("[ERROR] 구매수량은 1 이상이어야 합니다.");
        }
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PurchaseItem that = (PurchaseItem) object;
        return quantity == that.quantity && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity);
    }
}
