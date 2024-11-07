package store.utils;

import java.util.ArrayList;
import java.util.List;
import store.domain.PurchaseItem;

public class InputParser {

    private static final String ITEM_DELIMITER = ",";
    private static final String PROPERTY_DELIMITER = "-";

    public static List<PurchaseItem> parseInputToItems(String userInput) {
        List<PurchaseItem> parsedItems = new ArrayList<>();
        List<String> items = splitByItemDelimiter(userInput);
        for (String property : items) {
            parsedItems.add(parseItem(property));
        }
        return parsedItems;
    }

    private static List<String> splitByItemDelimiter(String userInput) {
        return List.of(userInput.split(ITEM_DELIMITER));
    }

    private static PurchaseItem parseItem(String item) {
        String substring = item.substring(1, item.length() - 1);
        List<String> itemProperties = List.of(substring.split(PROPERTY_DELIMITER));
        String name = itemProperties.get(0);
        int quantity = Integer.parseInt(itemProperties.get(1));
        return PurchaseItem.of(name, quantity);
    }

}
