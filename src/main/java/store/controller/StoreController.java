package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import java.util.Optional;
import store.domain.purchase.PurchaseAlert;
import store.domain.purchase.PurchaseItem;
import store.domain.purchase.dto.PurchaseAlertDto;
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

            Optional<PurchaseAlert> purchaseAlert = purchaseService.canAddFreeProduct(purchaseItems,
                    DateTimes.now()
                            .toLocalDate());
            if (purchaseAlert.isPresent()) {
                if (purchaseAlert.get().isApplicable()) {
                    outputView.printFreeItemInfo(purchaseAlert.get());
                    String prompt = inputView.promptYesOrNo();
                    if (prompt.equals("Y")) {
                        purchaseService.addPurchaseItemStock(purchaseAlert.get());
                    }
                }
            }

            PurchaseDto purchaseDto = purchaseService.purchaseInfo(DateTimes.now().toLocalDate());
            outputView.printPurchaseInfo(purchaseDto.getPurchaseItemDtos());

            if (purchaseAlert.isPresent()) {
                PurchaseAlertDto dto = purchaseAlert.get().toDto();
                outputView.printGive(dto);
            }

            outputView.printReceiptInfo(purchaseDto);

            String decision = inputView.continueInput();
            if (decision.equals("N")) {
                break;
            }
        }
    }

}
