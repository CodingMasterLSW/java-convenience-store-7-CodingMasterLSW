package store.config;

import java.util.List;
import java.util.Map;
import store.domain.parser.ProductParser;
import store.domain.parser.PromotionParser;
import store.domain.product.Products;
import store.domain.promotion.Promotion;
import store.utils.FileLoader;

public class StoreConfig {

    private static final String PRODUCT_PATH = "src/main/resources/products.md";
    private static final String PROMOTION_PATH = "src/main/resources/promotions.md";

    private final FileLoader fileLoader;
    private final ProductParser productParser;
    private final PromotionParser promotionParser;

    private StoreConfig(FileLoader fileLoader, ProductParser productParser,
            PromotionParser promotionParser) {
        this.fileLoader = fileLoader;
        this.productParser = productParser;
        this.promotionParser = promotionParser;
    }

    public static StoreConfig create() {
        return new StoreConfig(
                FileLoader.create(),
                ProductParser.create(),
                PromotionParser.create()
        );
    }

    public Products initializeStore() {
        List<String> productLines = fileLoader.loadFile(PRODUCT_PATH);
        List<String> promotionLines = fileLoader.loadFile(PROMOTION_PATH);
        Map<String, Promotion> promotionMap = promotionParser.parsePromotion(promotionLines);
        return Products.create(productParser.parseProducts(productLines, promotionMap));
    }

}
