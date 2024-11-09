package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import java.util.Optional;
import store.domain.purchase.PurchaseAlert;
import store.domain.purchase.PurchaseItem;
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
        List<ProductDto> productDtos = purchaseService.getProductDtos();
        for (ProductDto productDto : productDtos) {
            outputView.printProductDto(productDto);
        }
        String userInput = inputView.purchaseInput();
        List<PurchaseItem> purchaseItems = purchaseService.purchaseItems(userInput);
        Optional<PurchaseAlert> purchaseAlert = purchaseService.canAddFreeProduct(purchaseItems, DateTimes.now()
                .toLocalDate());
        if(purchaseAlert.isPresent()) {
            outputView.printFreeItemInfo(purchaseAlert.get());
            inputView.promptYesOrNo();
        }



    }

}
