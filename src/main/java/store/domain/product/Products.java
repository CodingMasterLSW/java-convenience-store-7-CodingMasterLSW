package store.domain.product;

import static store.exception.ErrorMessage.NOT_EXIST_PRODUCT;

import java.util.Collections;
import java.util.List;

public class Products {

    private List<Product> products;

    private Products(List<Product> products) {
        this.products = products;
    }

    public static Products create(List<Product> products) {
        return new Products(products);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public Product findProductByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_PRODUCT.getMessage()));
    }

    public boolean isExist(String name) {
        return products.stream()
                .anyMatch(product -> product.hasName(name));
    }

}
