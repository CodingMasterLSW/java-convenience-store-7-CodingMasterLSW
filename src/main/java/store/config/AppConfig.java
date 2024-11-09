package store.config;


import store.controller.StoreController;
import store.domain.product.Products;
import store.service.PurchaseService;
import store.view.InputView;
import store.view.OutputView;

public class AppConfig {

    private static final StoreConfig storeConfig = StoreConfig.create();

    private AppConfig() {
    }

    public static PurchaseService createPurchaseService() {
        Products products = storeConfig.initializeStore();
        return new PurchaseService(products);
    }

    public static StoreController createController() {
        PurchaseService purchaseService = createPurchaseService();
        return StoreController.of(InputView.create(), OutputView.create(), purchaseService);
    }

}
