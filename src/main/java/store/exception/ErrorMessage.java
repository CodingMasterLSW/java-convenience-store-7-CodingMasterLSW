package store.exception;

public enum ErrorMessage {

    INVALID_INPUT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    NOT_EXIST_PRODUCT("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    OVER_STOCK_PURCHASE("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    MINIMUM_PURCHASE_AMOUNT("구매수량은 1 이상이어야 합니다."),
    FILE_LOAD_EXCEPTION("파일을 읽는 중 오류가 발생했습니다."),
    SAME_PRODUCT_ANOTHER_PURCHASE("동일한 상품에 다른 가격이 존재합니다."),
    NOT_FIND_PROMOTION("프로모션을 찾을 수 없습니다"),
    STOCK_NOT_UNDER_ZERO("재고는 0 미만일 수 없습니다."),
    CURRENT_NOT_STOCK("현재 재고가 없습니다.");

    private static final String ERROR_PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = ERROR_PREFIX + message;
    }

    public String getMessage() {
        return message;
    }

}
