package store.controller;

import java.util.List;
import store.domain.service.PurchaseService;
import store.dto.ProductDto;
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
        //InputParser inputParser = InputParser.from();
        //List<PurchaseItem> purchaseItems = inputParser.parseInputToItems(userInput);

    }

}
