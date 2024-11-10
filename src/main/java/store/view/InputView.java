package store.view;

import static store.exception.ErrorMessage.INVALID_INPUT;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String BLANK = "";
    private static final String END_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String LACK_PROMOTION_STOCK_MESSAGE = "현재 %s %,d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";

    private InputView(){
    }

    public static InputView create() {
        return new InputView();
    }

    public String promptYesOrNo() {
        String prompt = Console.readLine();
        validatePrompt(prompt);
        return prompt;
    }

    public String printInsufficientPromotionStockInfo(String productName, int remainingQuantity) {
        System.out.printf(LACK_PROMOTION_STOCK_MESSAGE, productName, remainingQuantity);
        printMessage(BLANK);
        return promptYesOrNo();
    }


    public String purchaseInput() {
        printMessage(BLANK);
        printMessage(PURCHASE_MESSAGE);
        String userInput = Console.readLine();
        return userInput;
    }

    public String continueInput() {
        printMessage(BLANK);
        printMessage(BLANK);
        printMessage(END_MESSAGE);
        String prompt = Console.readLine();
        validatePrompt(prompt);
        return prompt;
    }

    private void printMessage(String message) {
        System.out.println(message);
    }

    private void validatePrompt(String prompt) {
        if (prompt.equals("Y") || prompt.equals("N")) {
            return;
        }
        throw new IllegalArgumentException(INVALID_INPUT.getMessage());
    }


}
