package store.domain.product;

import java.util.List;
import java.util.Map;
import store.domain.promotion.Promotion;
import store.utils.FileLoader;

public class Products {

    private List<Product> products;

    private Products(List<Product> products) {
        this.products = products;
    }

    public static Products create(Map<String, Promotion> promotions) {
        return new Products(FileLoader.loadProduct(promotions));
    }
    public List<Product> getProducts() {
        return products;
    }
}
