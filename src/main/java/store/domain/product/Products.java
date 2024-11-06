package store.domain.product;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import store.domain.promotion.Promotion;
import store.utils.PromotionLoader;

public class Products {

    private List<Product> products;

    private Products(List<Product> products) {
        this.products = products;
    }

    public static Products create(Map<String, Promotion> promotions) throws IOException {
        return new Products(PromotionLoader.loadProduct(promotions));
    }
    public List<Product> getProducts() {
        return products;
    }
}
