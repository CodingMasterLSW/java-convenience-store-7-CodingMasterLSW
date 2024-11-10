package store.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.purchase.Purchase;
import store.domain.purchase.PurchaseItem;
import store.domain.product.Product;
import store.domain.product.Products;
import store.domain.product.dto.ProductDto;
import store.domain.purchase.dto.PromotionStockDto;
import store.domain.purchase.dto.PurchaseDto;
import store.utils.InputParser;

public class PurchaseService {

    private final Products products;
    private Purchase purchase;

    public PurchaseService(Products products) {
        this.products = products;
    }

    // 상품을 입력받고 구매목록으로 바꿔서 출력한다.
    public List<PurchaseItem> initializePurchase(String userInput) {
        InputParser inputParser = InputParser.from(products);
        List<PurchaseItem> purchaseItems = inputParser.parseInputToItems(userInput);
        purchase = Purchase.from(purchaseItems);
        return purchaseItems;
    }

    public PurchaseDto purchase(LocalDate currentDate, boolean isContinue) {
        purchase.calculatePurchaseInfo(products, currentDate, isContinue);
        return purchase.toDto(products);
    }

    public PromotionStockDto checkPromotionStock() {
        return purchase.verifyPromotionStock(products);
    }

/*
    public boolean isPromotionApplicable(PurchaseItem item, LocalDate date) {
        Product product = findProduct(products, item);
        return product.getPromotion() != null && product.isPromotionDate(date);
    }



    public void addPurchaseItemStock(PurchaseAlert purchaseAlert) {
        Optional<PurchaseItem> purchaseItem = purchase.findItemByName(purchaseAlert.getItemName());
        purchaseItem.get().addQuantity(purchaseAlert.getFreeQuantity());
    }

    public PurchaseGifts getPurchaseGifts() {
        return purchase.getPurchaseGifts();
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
            return Optional.of(purchaseAlert);
        }
        return Optional.empty();
    }


 */

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
/*
    public Product findProduct(PurchaseItem item) {
        List<Product> matchedProducts = products.findProductByName(item.getName());
        return matchedProducts.get(0);
    }


 */

}
