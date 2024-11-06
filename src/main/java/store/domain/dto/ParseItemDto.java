package store.domain.dto;

import java.util.Objects;

public class ParseItemDto {

    private String name;
    private int quantity;

    private ParseItemDto(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public static ParseItemDto of(String name, int quantity) {
        return new ParseItemDto(name, quantity);
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
        ParseItemDto that = (ParseItemDto) object;
        return quantity == that.quantity && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity);
    }
}
