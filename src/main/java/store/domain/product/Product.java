package store.domain.product;

public abstract class Product {

    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public abstract boolean isPromotionProduct();

    public abstract int getStock();

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean hasName(String name) {
        return this.name.equals(name);
    }

}
