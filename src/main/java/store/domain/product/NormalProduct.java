package store.domain.product;

public class NormalProduct extends Product {

    private int stock;

    private NormalProduct(String name, int price, int stock) {
        super(name, price);
        this.stock= stock;
    }

    public static NormalProduct of(String name, int price, int stock) {
        return new NormalProduct(name, price, stock);
    }

    @Override
    public boolean isPromotionProduct() {
        return false;
    }

    @Override
    public int getStock() {
        return this.stock;
    }
}
