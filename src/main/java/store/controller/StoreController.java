package store.controller;

import java.util.List;
import java.util.Map;
import store.domain.parser.ProductParser;
import store.domain.parser.PromotionParser;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.dto.ProductDto;
import store.utils.FileLoader;
import store.view.OutputView;

public class StoreController {

    private OutputView outputView = new OutputView();

    public void start() {
        outputView.printProductMessage();
        List<String> productLines = FileLoader.loadFile("src/main/resources/products.md");
        List<String> promotionLines = FileLoader.loadFile("src/main/resources/promotions.md");

        ProductParser productParser = ProductParser.create();
        PromotionParser promotionParser = PromotionParser.create();

        Map<String, Promotion> stringPromotionMap = promotionParser.parsePromotion(promotionLines);
        List<Product> products = productParser.parseProducts(productLines, stringPromotionMap);

        for (Product product : products) {
            // 프로모션 재고가 있는 경우에만 promotionDto 생성 및 출력
            if (product.getName() != null && product.getPromotion() != null) {
                ProductDto promotionDto = product.toPromotionDto();
                outputView.printProductDto(promotionDto);
            }

            if (product.getName() != null) {
                ProductDto normalDto = product.toNormalDto();
                outputView.printProductDto(normalDto);
            }
        }

    }

}
