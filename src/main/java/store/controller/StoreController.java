package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import java.util.function.Supplier;
import store.domain.purchase.PurchaseAlert;
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
            purchaseItems();
            processPurchaseAlerts();

            boolean isEnoughStock = checkAndPromptPromotionStock();
            boolean isMembershipApplied = handleMembershipInput();
            PurchaseDto purchaseDto = purchaseService.purchase(DateTimes.now().toLocalDate(),
                    isEnoughStock, isMembershipApplied);
            displayPurchaseResult(purchaseDto);
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

    private void purchaseItems() {
        inputView.printPurchaseMessage();
        retryOnInvalidInput(() -> {
            String userInput = inputView.purchaseInput();
            purchaseService.initializePurchase(userInput);
            return null;
        });
    }

    private boolean checkAndPromptPromotionStock() {
        PromotionStockDto promotionStockDto = purchaseService.checkPromotionStock();
        if (promotionStockDto == null) {
            return true;
        }
        String userInput = retryOnInvalidInput(() ->
                    inputView.printInsufficientPromotionStockInfo(
                            promotionStockDto.getProductName(),
                            promotionStockDto.getLackPromotionStock())
        );
        return userInput.equalsIgnoreCase("Y");
    }

    private boolean isEnd() {
        inputView.printEndMessage();
        String userInput = retryOnInvalidInput(() -> inputView.continueInput());
        return userInput.equalsIgnoreCase("N");
    }

    private void processPurchaseAlerts() {
        List<PurchaseAlert> alerts = purchaseService.generatePurchaseAlerts();
        for (PurchaseAlert alert : alerts) {
            handleGiftAlert(alert);
        }
    }

    private void handleGiftAlert(PurchaseAlert alert) {
        if (alert.isApplicable()) {
            inputView.freeAlertInput(alert);
            String userInput = retryOnInvalidInput(() -> inputView.promptYesOrNo());
            if (userInput.equalsIgnoreCase("Y")) {
                purchaseService.applyGiftIfApplicable(alert);
            }
        }
    }

    private boolean handleMembershipInput() {
        inputView.printMembershipMessage();
        String userInput = retryOnInvalidInput(() -> inputView.promptYesOrNo());
        return userInput.equalsIgnoreCase("Y");
    }

    private <T> T retryOnInvalidInput(Supplier<T> input) {
        while (true) {
            try {
                return input.get();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

}