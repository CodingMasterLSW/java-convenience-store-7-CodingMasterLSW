package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import store.domain.purchase.PurchaseItem;
import store.domain.purchase.dto.PromotionStockDto;
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
            // purchaseAlert가 존재한다면 입력받기

            // ProductPromotionStock < purchaseAMount 일 경우, 입력받기
            boolean isEnoughStock = checkAndPromptPromotionStock();
            // 입력 아이템을 구매한다.
            PurchaseDto purchaseDto = purchaseService.purchase(DateTimes.now().toLocalDate(),
                    isEnoughStock);
            // 출력
            displayPurchaseResult(purchaseDto);
            // 종료 메서드
            if (isEnd()) {
                break;
            }
        }
    }

    private void showCurrentProduct() {
        outputView.printProductMessage();
        List<ProductDto> productDtos = purchaseService.getProductDtos();
        for (ProductDto productDto : productDtos) {
            outputView.printProductDto(productDto);
        }
    }

    private void displayPurchaseResult(PurchaseDto purchaseDto) {
        outputView.printPurchaseInfo(purchaseDto.getItems());
        outputView.printGive(purchaseDto.getGifts());
        outputView.printReceiptInfo(purchaseDto);
    }

    private List<PurchaseItem> purchaseItems() {
        String userInput = inputView.purchaseInput();
        return purchaseService.initializePurchase(userInput);
    }

    private boolean checkAndPromptPromotionStock() {
        PromotionStockDto promotionStockDto = purchaseService.checkPromotionStock();
        boolean isEnoughStock = true;
        if (promotionStockDto != null) {
            String userInput = inputView.printInsufficientPromotionStockInfo(
                    promotionStockDto.getProductName(), promotionStockDto.getLackPromotionStock());
            if (!userInput.equalsIgnoreCase("Y")) {
                isEnoughStock = false;
            }
        }
        return isEnoughStock;
    }

    private boolean isEnd() {
        String decision = inputView.continueInput();
        if (decision.equalsIgnoreCase("N")) {
            return true;
        }
        return false;
    }
}