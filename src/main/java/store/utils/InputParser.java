package store.utils;

import java.util.ArrayList;
import java.util.List;
import store.domain.dto.ParseItemDto;

public class InputParser {

    private static final String ITEM_DELIMITER = ",";
    private static final String PROPERTY_DELIMITER = "-";

    public static List<ParseItemDto> parseInputToItems(String userInput) {
        List<ParseItemDto> parsedItems = new ArrayList<>();
        List<String> item = splitByItemDelimiter(userInput);
        for (String property : item) {
            parsedItems.add(parseItem(property));
        }
        return parsedItems;
    }

    private static List<String> splitByItemDelimiter(String userInput) {
        return List.of(userInput.split(ITEM_DELIMITER));
    }

    private static ParseItemDto parseItem(String item) {
        String substring = item.substring(1, item.length() - 1);
        List<String> itemProperties = List.of(substring.split(PROPERTY_DELIMITER));
        String name = itemProperties.get(0);
        int quantity = Integer.parseInt(itemProperties.get(1));
        return ParseItemDto.of(name, quantity);
    }

}
