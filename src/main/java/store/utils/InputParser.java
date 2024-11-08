package store.utils;

import static store.exception.ErrorMessage.NOT_EXIST_PRODUCT;

import java.util.ArrayList;
import java.util.List;
import store.domain.PurchaseItem;
import store.domain.product.Products;

public class InputParser {

    private static final String ITEM_DELIMITER = ",";
    private static final String PROPERTY_DELIMITER = "-";
    private final Products products;

    private InputParser(Products products) {
        this.products = products;
    }

    public static InputParser from(Products products) {
        return new InputParser(products);
    }

    public List<PurchaseItem> parseInputToItems(String userInput) {
        List<PurchaseItem> parsedItems = new ArrayList<>();
        List<String> items = splitByItemDelimiter(userInput);
        for (String property : items) {
            parsedItems.add(parseItem(property));
        }
        return parsedItems;
    }

    private List<String> splitByItemDelimiter(String userInput) {
        return List.of(userInput.split(ITEM_DELIMITER));
    }

    private PurchaseItem parseItem(String item) {
        String substring = item.substring(1, item.length() - 1);
        List<String> itemProperties = List.of(substring.split(PROPERTY_DELIMITER));
        String name = itemProperties.get(0);
        int quantity = Integer.parseInt(itemProperties.get(1));
        validateProduct(name);
        return PurchaseItem.of(name, quantity);
    }

    private void validateProduct(String name) {
        if (products.isExist(name)) {
            return;
        }
        throw new IllegalArgumentException(NOT_EXIST_PRODUCT.getMessage());
    }

}
