package store.domain.purchase;

import static store.exception.ErrorMessage.MINIMUM_PURCHASE_AMOUNT;

import java.util.Objects;
import store.domain.purchase.dto.PurchaseItemDto;

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
        if (quantity <= 0) {
            throw new IllegalArgumentException(MINIMUM_PURCHASE_AMOUNT.getMessage());
        }
    }

    public PurchaseItemDto toDto(String name, int quantity, int price) {
        return PurchaseItemDto.of(name, quantity, price);
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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