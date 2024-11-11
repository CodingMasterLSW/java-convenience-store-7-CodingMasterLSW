package store.view;

import static store.exception.ErrorMessage.INVALID_INPUT;

import camp.nextstep.edu.missionutils.Console;
import store.domain.purchase.PurchaseAlert;

public class InputView {

    private static final String PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String BLANK = "";
    private static final String END_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String LACK_PROMOTION_STOCK_MESSAGE = "현재 %s %,d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String FREE_PRODUCT_INFO = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String MEMBERSHIP_DISCOUNT_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";

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

    public void freeAlertInput(PurchaseAlert purchaseAlert) {
        System.out.printf(FREE_PRODUCT_INFO, purchaseAlert.getItemName(), purchaseAlert.getFreeQuantity());
        printMessage(BLANK);
    }

    public String printInsufficientPromotionStockInfo(String productName, int remainingQuantity) {
        System.out.printf(LACK_PROMOTION_STOCK_MESSAGE, productName, remainingQuantity);
        printMessage(BLANK);
        return promptYesOrNo();
    }

    public void printMembershipMessage() {
        printMessage(MEMBERSHIP_DISCOUNT_MESSAGE);
    }


    public void printPurchaseMessage() {
        printMessage(BLANK);
        printMessage(PURCHASE_MESSAGE);
    }

    public String purchaseInput() {
        String userInput = Console.readLine();
        return userInput;
    }

    public void printEndMessage() {
        printMessage(BLANK);
        printMessage(BLANK);
        printMessage(END_MESSAGE);
    }

    public String continueInput() {
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
