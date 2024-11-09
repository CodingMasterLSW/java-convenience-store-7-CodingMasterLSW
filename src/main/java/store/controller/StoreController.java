package store.controller;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import java.util.Optional;
import store.domain.purchase.PurchaseAlert;
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

        while(true) {
            outputView.printProductMessage();
            List<ProductDto> productDtos = purchaseService.getProductDtos();
            for (ProductDto productDto : productDtos) {
                outputView.printProductDto(productDto);
            }
            String userInput = inputView.purchaseInput();
            List<PurchaseItem> purchaseItems = purchaseService.purchaseItems(userInput);
            Optional<PurchaseAlert> purchaseAlert = purchaseService.canAddFreeProduct(purchaseItems,
                    DateTimes.now()
                            .toLocalDate());
            if (purchaseAlert.isPresent()) {
                outputView.printFreeItemInfo(purchaseAlert.get());
                String prompt = inputView.promptYesOrNo();
                if (prompt.equals("Y")) {
                    purchaseService.addPurchaseItemStock(purchaseItems, purchaseAlert.get());
                }
            }
            PurchaseDto purchaseDto = purchaseService.purchaseInfo(purchaseItems,
                    DateTimes.now().toLocalDate());
            outputView.printPurchaseInfo(purchaseDto.getPurchaseItemDtos());
            outputView.printTotalInfo(purchaseDto);

            String decision = inputView.continueInput();
            if (decision.equals("N")) {
                break;
            }
        }
    }

}
