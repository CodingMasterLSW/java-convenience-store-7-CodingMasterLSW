package store.utils;

import static store.exception.ErrorMessage.INVALID_INPUT;
import static store.exception.ErrorMessage.NOT_EXIST_PRODUCT;
import static store.exception.ErrorMessage.OVER_STOCK_PURCHASE;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import store.domain.product.Product;
import store.domain.purchase.PurchaseItem;
import store.domain.product.Products;

public class InputParser {

    private static final String ITEM_DELIMITER = ",";
    private static final String PROPERTY_DELIMITER = "-";
    private static final String ITEM_START_FORMAT = "[";
    private static final String ITEM_END_FORMAT = "]";
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final int ITEM_PROPERTY_SIZE = 2;

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
        String substring = removeBracket(item);
        List<String> itemProperties = splitProperties(substring);
        String name = extractProductName(itemProperties);
        int quantity = extractQuantity(itemProperties);
        Product product = products.findProductByName(name);
        if (product.hasLackOfStock(quantity)) {
            throw new IllegalArgumentException(OVER_STOCK_PURCHASE.getMessage());
        }
        return PurchaseItem.of(name, quantity);
    }

    private int extractQuantity(List<String> itemProperties) {
        String quantity = itemProperties.get(1);
        if (quantity.isBlank() || quantity == null || !NUMBER_PATTERN.matcher(quantity)
                .matches()) {
            throw new IllegalArgumentException(INVALID_INPUT.getMessage());
        }
        return Integer.parseInt(quantity);
    }

    private String extractProductName(List<String> itemProperties) {
        String name = itemProperties.get(0);
        validateProductNameNotEmpty(name);
        validateNotExistProduct(name);
        return name;
    }

    private List<String> splitProperties(String substring) {
        List<String> itemProperties = List.of(substring.split(PROPERTY_DELIMITER));
        validateDelimiter(itemProperties);
        return itemProperties;
    }

    private String removeBracket(String item) {
        validateBracketsFormat(item);
        return item.substring(1, item.length() - 1);
    }

    private void validateDelimiter(List<String> itemProperties) {
        if (itemProperties.size() != ITEM_PROPERTY_SIZE) {
            throw new IllegalArgumentException(INVALID_INPUT.getMessage());
        }
    }

    private void validateNotExistProduct(String name) {
        if (products.isExist(name)) {
            return;
        }
        throw new IllegalArgumentException(NOT_EXIST_PRODUCT.getMessage());
    }

    private void validateBracketsFormat(String item) {
        if (item.startsWith(ITEM_START_FORMAT) && item.endsWith(ITEM_END_FORMAT)) {
            return;
        }
        throw new IllegalArgumentException(INVALID_INPUT.getMessage());
    }

    private void validateProductNameNotEmpty(String name) {
        if (name.isBlank() || name == null) {
            throw new IllegalArgumentException(INVALID_INPUT.getMessage());
        }
    }

}
