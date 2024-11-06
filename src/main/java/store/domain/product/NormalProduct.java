package store.domain.product;

public class NormalProduct extends Product {

    private NormalProduct(String name, int price, int quantity) {
        super(name, price, quantity);
    }

    public static NormalProduct of(String name, int price, int quantity) {
        return new NormalProduct(name, price, quantity);
    }

    @Override
    public boolean isPromotionProduct() {
        return false;
    }

}
