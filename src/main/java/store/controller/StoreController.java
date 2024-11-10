package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import store.domain.Membership;
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
            try {
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
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
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
        while (true) {
            try {
                String userInput = inputView.purchaseInput();
                purchaseService.initializePurchase(userInput);
                break;
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private boolean checkAndPromptPromotionStock() {
        PromotionStockDto promotionStockDto = purchaseService.checkPromotionStock();
        boolean isEnoughStock = true;
        if (promotionStockDto != null) {
            while (true) {
                try {
                    String userInput = inputView.printInsufficientPromotionStockInfo(
                            promotionStockDto.getProductName(),
                            promotionStockDto.getLackPromotionStock());
                    if (!userInput.equalsIgnoreCase("Y")) {
                        isEnoughStock = false;
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    outputView.printErrorMessage(e.getMessage());
                }
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

    private void processPurchaseAlerts() {
        List<PurchaseAlert> alerts = purchaseService.generatePurchaseAlerts();
        for (PurchaseAlert alert : alerts) {
            handleGiftAlert(alert);
        }
    }

    private void handleGiftAlert(PurchaseAlert alert) {
        while (true) {
            try {
                if (alert.isApplicable()) {
                    String userInput = inputView.freeAlertInput(alert);
                    if (userInput.equalsIgnoreCase("Y")) {
                        purchaseService.applyGiftIfApplicable(alert);
                    }
                }
                break;
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private boolean handleMembershipInput() {
        while (true) {
            try {
                String userInput = inputView.promptMembershipDiscount();
                if (userInput.equalsIgnoreCase("Y")) {
                    return true;
                }
                break;
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }

        }
        return false;
    }

}