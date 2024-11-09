package store.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.Purchase;
import store.domain.PurchaseItem;
import store.domain.product.Product;
import store.domain.product.Products;
import store.dto.ProductDto;
import store.dto.PurchaseDto;
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

    public PurchaseDto purchaseItems(String userInput, LocalDate currentDate) {
        InputParser inputParser = InputParser.from(products);
        List<PurchaseItem> purchaseItems = inputParser.parseInputToItems(userInput);
        Purchase purchase = Purchase.from(purchaseItems, currentDate);
        PurchaseDto purchaseDto = purchase.calculatePurchaseInfo(products, currentDate);
        return purchaseDto;
    }

}
