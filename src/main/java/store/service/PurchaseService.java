package store.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.promotion.Promotion;
import store.domain.purchase.Purchase;
import store.domain.purchase.PurchaseAlert;
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

    public List<PurchaseItem> initializePurchase(String userInput) {
        InputParser inputParser = InputParser.from(products);
        List<PurchaseItem> purchaseItems = inputParser.parseInputToItems(userInput);
        purchase = Purchase.from(purchaseItems);
        return purchaseItems;
    }

    public PurchaseDto purchase(LocalDate currentDate, boolean isContinue, boolean isMembershipApplied) {
        purchase.calculatePurchaseInfo(products, currentDate, isContinue);
        if (isMembershipApplied) {
            purchase.applyMembershipDiscount(products);
        }
        return purchase.toDto(products);
    }

    public PromotionStockDto checkPromotionStock() {
        return purchase.verifyPromotionStock(products);
    }

    public List<PurchaseAlert> generatePurchaseAlerts() {
        List<PurchaseAlert> alerts = new ArrayList<>();
        for (PurchaseItem item : purchase.getItems()) {
            Product product = products.findProductByName(item.getName());
            if (product.hasPromotion() && !product.lackOfPromotion(item.getQuantity())) {
                int purchasedQuantity = item.getQuantity();
                Promotion promotion = product.getPromotion();
                PurchaseAlert alert = PurchaseAlert.of(product.getName(), promotion, purchasedQuantity);
                if (alert.isApplicable()) {
                    alerts.add(alert);
                }
            }
        }
        return alerts;
    }

    public void applyGiftIfApplicable(PurchaseAlert alert) {
        PurchaseItem item = findItemByName(alert.getItemName());
        if (item != null) {
            item.addQuantity(alert.getFreeQuantity());
        }
    }

    public PurchaseItem findItemByName(String itemName) {
        return purchase.getItems().stream()
                .filter(item -> item.getName().equals(itemName))
                .findFirst()
                .orElse(null);
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
