package store;

import store.config.AppConfig;
import store.controller.StoreController;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = AppConfig.createController();
        storeController.start();
    }
}
