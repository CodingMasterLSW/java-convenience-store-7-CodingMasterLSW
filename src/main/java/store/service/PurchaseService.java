package store.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.domain.purchase.PurchaseAlert;
import store.domain.purchase.PurchaseItem;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.dto.ProductDto;
import store.utils.InputParser;

public class PurchaseService {

    private final Products products;

    public PurchaseService(Products products) {
        this.products = products;
    }

    public List<PurchaseItem> purchaseItems(String userInput) {
        InputParser inputParser = InputParser.from(products);
        List<PurchaseItem> purchaseItems = inputParser.parseInputToItems(userInput);
        return purchaseItems;
    }

    public Optional<PurchaseAlert> canAddFreeProduct(List<PurchaseItem> purchaseItems,
            LocalDate currentDate) {
        for (PurchaseItem purchaseItem : purchaseItems) {
            Product product = findProduct(products, purchaseItem);
            if (product.getPromotion() == null || !product.isPromotionDate(currentDate)) {
                continue;
            }
            PurchaseAlert purchaseAlert = PurchaseAlert.of(purchaseItem.getName(),
                    product.getPromotion(),
                    purchaseItem.getQuantity());
            if (purchaseAlert.isApplicable()) {
                return Optional.of(purchaseAlert);
            }
        }
        return Optional.empty();
    }

    private Product findProduct(Products products, PurchaseItem item) {
        List<Product> matchedProducts = products.findProductByName(item.getName());
        return matchedProducts.get(0);
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


}
