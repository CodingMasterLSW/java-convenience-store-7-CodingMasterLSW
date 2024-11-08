package store.domain.service;

import java.util.ArrayList;
import java.util.List;
import store.domain.PurchaseItem;
import store.domain.product.Product;
import store.domain.product.Products;
import store.dto.ProductDto;
import store.utils.InputParser;

public class PurchaseService {

    private final Products products;

    public PurchaseService(Products products) {
        this.products = products;
    }

    public List<ProductDto> getProductDtos() {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products.getProducts()) {
            extracted(product, productDtos);
        }
        return productDtos;
    }

    private void extracted(Product product, List<ProductDto> productDtos) {
        if (product.getName() != null && product.getPromotion() != null) {
            productDtos.add(product.toPromotionDto());
        }
        if (product.getName() != null) {
            productDtos.add(product.toNormalDto());
        }
    }

    public List<PurchaseItem> purchaseItems(String userInput, Products products) {
        InputParser inputParser = InputParser.from(products);
        return inputParser.parseInputToItems(userInput);
    }

}
