package store.domain.product;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Product> findProductByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .collect(Collectors.toList());
    }

    public boolean isExist(String name) {
        return products.stream()
                .anyMatch(product -> product.hasName(name));
    }

}
