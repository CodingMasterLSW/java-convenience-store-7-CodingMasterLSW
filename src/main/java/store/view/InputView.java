package store.view;

import static store.exception.ErrorMessage.INVALID_INPUT;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String BLANK = "";

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

    public String purchaseInput() {
        printMessage(BLANK);
        printMessage(PURCHASE_MESSAGE);
        String userInput = Console.readLine();
        return userInput;
    }

    private void printMessage(String message) {
        System.out.println(message);
    }

    private void validatePrompt(String prompt) {
        if (prompt.equals("Y") || prompt.equals("Y")) {
            return;
        }
        throw new IllegalArgumentException(INVALID_INPUT.getMessage());
    }


}
