package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.List;
import store.service.PurchaseService;
import store.domain.product.dto.ProductDto;
import store.domain.purchase.dto.PurchaseDto;
import store.domain.purchase.dto.PurchaseItemDto;
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
        PurchaseDto purchaseDto = purchaseService.purchaseItems(userInput,
                DateTimes.now().toLocalDate());

        List<PurchaseItemDto> purchaseItemDtos = purchaseDto.getPurchaseItemDtos();
        outputView.printPurchaseInfo(purchaseItemDtos);
        outputView.printTotalInfo(purchaseDto);

    }

}
