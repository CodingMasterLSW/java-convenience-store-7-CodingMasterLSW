package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
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

            showCurrentProduct();

            // 구매를 진행한다. 입력을 받고, 입력 아이템을 출력함.
            List<PurchaseItem> purchaseItems = purchaseItems();
            // 입력 아이템을 구매한다.
            PurchaseDto purchaseDto = purchaseService.purchase(DateTimes.now().toLocalDate(), true);
            outputView.printPurchaseInfo(purchaseDto.getItems());
            outputView.printGive(purchaseDto.getGifts());
            outputView.printReceiptInfo(purchaseDto);
            //

            // 종료 메서드
            if (isEnd()) {
                break;
            }
            /*
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

                    // **1. 추가 구매를 통한 프로모션 혜택 확인**
                    Optional<PurchaseAlert> purchaseAlert = purchaseService.canAddFreeProduct(
                            purchaseItems, DateTimes.now().toLocalDate());
                    if (purchaseAlert.isPresent() && purchaseAlert.get().isApplicable()) {
                        outputView.printFreeItemInfo(purchaseAlert.get());
                        String prompt = inputView.promptYesOrNo();
                        if (prompt.equalsIgnoreCase("Y")) {
                            item.addQuantity(purchaseAlert.get().getFreeQuantity());
                            // 구매 수량이 변경되었으므로 적용 가능한 세트 수 재계산
                            int maxSetsFromRequestedQuantity = item.getQuantity() / totalPromoUnits;
                            applicableSets = Math.min(availablePromotionStock / totalPromoUnits, maxSetsFromRequestedQuantity);
                            item.setPromotionSetsApplied(applicableSets);
                        }
                    }

                    // **2. 프로모션 적용 가능한 세트 수 계산 및 남은 수량 확인**
                    int maxSetsFromPromotionStock = availablePromotionStock / totalPromoUnits;
                    int maxSetsFromRequestedQuantity = item.getQuantity() / totalPromoUnits;
                    applicableSets = Math.min(maxSetsFromPromotionStock, maxSetsFromRequestedQuantity);
                    int promotionApplicableQuantity = applicableSets * totalPromoUnits;
                    int remainingQuantity = item.getQuantity() - promotionApplicableQuantity;

                    item.setPromotionSetsApplied(applicableSets); // 프로모션 적용 세트 수 저장

                    // **3. 프로모션 재고 부족 시 처리**
                    if (remainingQuantity > 0) {
                        outputView.printInsufficientPromotionStockInfo(item.getName(), remainingQuantity);
                        String prompt = inputView.promptYesOrNo();
                        if (prompt.equalsIgnoreCase("Y")) {
                            useNormalStockIfNeeded = true;
                        } else {
                            // 초과된 수량만큼 구매 수량 감소
                            item.setQuantity(promotionApplicableQuantity);
                        }
                    }
                }

                // **재고 차감 메서드 호출**
                boolean purchaseSuccess = purchaseService.checkPromotionAndHandlePurchase(item, useNormalStockIfNeeded);
                if (!purchaseSuccess) {

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
            if (decision.equalsIgnoreCase("N")) {
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

             */
        }
    }

    private void showCurrentProduct() {
        outputView.printProductMessage();
        List<ProductDto> productDtos = purchaseService.getProductDtos();
        for (ProductDto productDto : productDtos) {
            outputView.printProductDto(productDto);
        }
    }

    private List<PurchaseItem> purchaseItems() {
        String userInput = inputView.purchaseInput();
        return purchaseService.initializePurchase(userInput);
    }

    private boolean isEnd() {
        String decision = inputView.continueInput();
        if (decision.equalsIgnoreCase("N")) {
            return true;
        }
        return false;
    }
}