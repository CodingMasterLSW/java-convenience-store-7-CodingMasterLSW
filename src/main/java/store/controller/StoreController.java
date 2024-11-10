package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import java.util.Optional;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.domain.purchase.PurchaseAlert;
import store.domain.purchase.PurchaseGifts;
import store.domain.purchase.PurchaseItem;
import store.domain.purchase.dto.PurchaseDto;
import store.service.PurchaseService;
import store.domain.product.dto.ProductDto;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private InputView inputView;
    private OutputView outputView;
    private PurchaseService purchaseService;

    private StoreController(InputView inputView, OutputView outputView,
            PurchaseService purchaseService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.purchaseService = purchaseService;
    }

    public static StoreController of(InputView inputView, OutputView outputView,
            PurchaseService purchaseService) {
        return new StoreController(inputView, outputView, purchaseService);
    }

    public void start() {

        while (true) {
            outputView.printProductMessage();
            List<ProductDto> productDtos = purchaseService.getProductDtos();
            for (ProductDto productDto : productDtos) {
                outputView.printProductDto(productDto);
            }
            String userInput = inputView.purchaseInput();
            List<PurchaseItem> purchaseItems = purchaseService.initializePurchase(userInput);

            for (PurchaseItem item : purchaseItems) {
                boolean useNormalStockIfNeeded = false;
                int applicableSets = 0;

                if (purchaseService.isPromotionApplicable(item, DateTimes.now().toLocalDate())) {
                    Product product = purchaseService.findProduct(item);
                    Promotion promotion = product.getPromotion();
                    int availablePromotionStock = product.getPromotionStock();

                    int requiredBuyQuantity = promotion.getBuy();
                    int freeQuantity = promotion.getGet();
                    int totalPromoUnits = requiredBuyQuantity + freeQuantity;

                    int maxSetsFromPromotionStock = availablePromotionStock / totalPromoUnits;
                    int maxSetsFromRequestedQuantity = item.getQuantity() / totalPromoUnits;
                    applicableSets = Math.min(maxSetsFromPromotionStock, maxSetsFromRequestedQuantity);

                    int promotionApplicableQuantity = applicableSets * totalPromoUnits;
                    int remainingQuantity = item.getQuantity() - promotionApplicableQuantity;

                    item.setPromotionSetsApplied(applicableSets); // 프로모션 적용 세트 수 저장

                    if (remainingQuantity > 0) {
                        outputView.printInsufficientPromotionStockInfo(item.getName(), remainingQuantity);
                        String prompt = inputView.promptYesOrNo();
                        if (prompt.equalsIgnoreCase("Y")) {
                            useNormalStockIfNeeded = true;
                        } else {
                            // 구매하지 않기로 선택한 경우, 아이템의 수량을 프로모션 적용 수량으로 조정
                            item.setQuantity(promotionApplicableQuantity);
                        }
                    }
                }

                // **재고 차감 메서드 호출 추가**
                boolean purchaseSuccess = purchaseService.checkPromotionAndHandlePurchase(item, useNormalStockIfNeeded);
                if (!purchaseSuccess) {
                    // 재고 차감에 실패한 경우에 대한 처리 (필요 시)
                    // 예를 들어, 사용자에게 알림을 주거나 예외를 던질 수 있습니다.
                }
            }

            // 이후 코드 계속...
            // 멤버십 할인 여부 확인 등 추가 로직 필요

            PurchaseDto purchaseDto = purchaseService.purchaseInfo(DateTimes.now().toLocalDate());
            outputView.printPurchaseInfo(purchaseDto.getPurchaseItemDtos());
            PurchaseGifts purchaseGifts = purchaseService.getPurchaseGifts();
            outputView.printGive(purchaseGifts);
            outputView.printReceiptInfo(purchaseDto);

            String decision = inputView.continueInput();
            if (decision.equals("N")) {
                break;
            }
        }
    }

    private int getPromotionApplicableQuantity(Product product) {
        Promotion promotion = product.getPromotion();
        int availablePromotionStock = product.getPromotionStock();

        int requiredBuyQuantity = promotion.getBuy();
        int freeQuantity = promotion.getGet();
        int totalPromoUnits = requiredBuyQuantity + freeQuantity;

        int maxSetsFromPromotionStock = availablePromotionStock / totalPromoUnits;
        int promotionApplicableQuantity = maxSetsFromPromotionStock * totalPromoUnits;
        return promotionApplicableQuantity;
    }

}