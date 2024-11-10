package store.view;

import java.util.List;
import store.domain.product.dto.ProductDto;
import store.domain.purchase.PurchaseAlert;
import store.domain.purchase.PurchaseGift;
import store.domain.purchase.PurchaseGifts;
import store.domain.purchase.dto.PurchaseDto;
import store.domain.purchase.dto.PurchaseItemDto;

public class OutputView {

    private static final String INTRODUCE_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String CONTAIN_PRODUCT_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String BLANK = "";
    private static final String PRODUCT_INFO = "- %s %,d원 %s %s";
    private static final String RECEIPT_BANNER = "==============W 편의점================";
    private static final String PURCHASE_SCHEMA = "상품명      수량  금액";
    private static final String PURCHASE_INFO = "%s     %s      %,d";
    private static final String PURCHASE_DELIMITER = "====================================";
    private static final String GIVE_MESSAGE = "=============증  정===============";
    private static final String GIFT_PRODUCT_INFO = "%s         %,d";
    private static final String FREE_ITEM_PROMPT_MESSAGE = "현재 %s은(는) %,d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String TOTAL_PURCHASE = "총구매액        %,d     %,d";
    private static final String PROMOTION_DISCOUNT = "행사할인              -%,d";
    private static final String MEMBERSHIP_DISCOUNT = "멤버십할인            -%,d";
    private static final String TOTAL_PAY_AMOUNT = "내실돈             %,d";

    private OutputView() {
    }

    public static OutputView create() {
        return new OutputView();
    }

    private static String getString(ProductDto productDto) {
        String quantity = String.valueOf(productDto.getQuantity()) + "개";
        if (quantity.equals("0개")) {
            quantity = "재고 없음";
        }
        return quantity;
    }

    public void printGive(PurchaseGifts purchaseGifts) {
        printMessage(GIVE_MESSAGE);
        for (PurchaseGift purchaseGift : purchaseGifts.getGifts()) {
            System.out.printf(GIFT_PRODUCT_INFO, purchaseGift.getName(), purchaseGift.getQuantity());
            printMessage(BLANK);
        }
        printMessage(BLANK);
    }

    public void printProductMessage() {
        printMessage(INTRODUCE_MESSAGE);
        printMessage(CONTAIN_PRODUCT_MESSAGE);
        printMessage(BLANK);
    }

    public void printReceiptInfo(PurchaseDto purchaseDto) {
        printMessage(PURCHASE_DELIMITER);
        System.out.printf(TOTAL_PURCHASE, purchaseDto.getTotalQuantity(),
                purchaseDto.getTotalPrice());
        printMessage(BLANK);
        System.out.printf(PROMOTION_DISCOUNT, purchaseDto.getPromotionDiscount());
        printMessage(BLANK);
        System.out.printf(MEMBERSHIP_DISCOUNT, purchaseDto.getMembershipDiscount());
        printMessage(BLANK);
        System.out.printf(TOTAL_PAY_AMOUNT, purchaseDto.getFinalAmount());
    }

    public void printProductDto(ProductDto productDto) {
        String name = productDto.getName();
        int price = productDto.getPrice();
        String quantity = getString(productDto);
        String promotion = getPromotion(productDto);
        System.out.printf(PRODUCT_INFO, name, price, quantity, promotion);
        System.out.println();
    }

    public void printFreeItemInfo(PurchaseAlert purchaseAlert) {
        if (purchaseAlert.isApplicable()) {
            System.out.printf(FREE_ITEM_PROMPT_MESSAGE, purchaseAlert.getItemName(),
                    purchaseAlert.getFreeQuantity());
            printMessage(BLANK);
        }
    }

    public void printPurchaseInfo(List<PurchaseItemDto> purchaseItemDtos) {
        printMessage(RECEIPT_BANNER);
        printMessage(PURCHASE_SCHEMA);
        for (PurchaseItemDto purchaseItemDto : purchaseItemDtos) {
            System.out.printf(PURCHASE_INFO, purchaseItemDto.getProductName(),
                    purchaseItemDto.getQuantity(),
                    purchaseItemDto.getPrice() * purchaseItemDto.getQuantity());
            System.out.println();
        }
    }

    private String getPromotion(ProductDto productDto) {
        String promotion = productDto.getPromotion();
        if (productDto.getPromotion() == "null" || productDto.getPromotion() == null) {
            promotion = "";
        }
        return promotion;
    }

    private void printMessage(String message) {
        System.out.println(message);
    }

}
