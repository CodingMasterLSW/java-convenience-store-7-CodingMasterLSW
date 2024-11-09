package store.view;

import java.util.List;
import store.domain.product.dto.ProductDto;
import store.domain.purchase.dto.PurchaseAlertDto;
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
    private static final String TOTAL_INFO = "상품명      %,d  %,d";
    private static final String FREE_ITEM_PROMPT_MESSAGE = "현재 %s은(는) %,d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";

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

    public void printProductMessage() {
        printMessage(INTRODUCE_MESSAGE);
        printMessage(CONTAIN_PRODUCT_MESSAGE);
        printMessage(BLANK);
    }

    public void printProductDto(ProductDto productDto) {
        String name = productDto.getName();
        int price = productDto.getPrice();
        String quantity = getString(productDto);
        String promotion = getPromotion(productDto);
        System.out.printf(PRODUCT_INFO, name, price, quantity, promotion);
        System.out.println();
    }

    public void printTotalInfo(PurchaseDto purchaseDto) {
        printMessage(PURCHASE_DELIMITER);
        System.out.printf(TOTAL_INFO, purchaseDto.getTotalQuantity(), purchaseDto.getTotalPrice());
    }

    public void printFreeItemInfo(List<PurchaseAlertDto> purchaseAlertDtos) {
        for (PurchaseAlertDto purchaseAlertDto : purchaseAlertDtos) {
            if (purchaseAlertDto == null) {
                return;
            }
            System.out.printf(FREE_ITEM_PROMPT_MESSAGE, purchaseAlertDto.getProductName(),
                    purchaseAlertDto.getFreeQuantity());
            printMessage(BLANK);
            return;
        }
    }

    public void printPurchaseInfo(List<PurchaseItemDto> purchaseItemDtos) {
        printMessage(RECEIPT_BANNER);
        printMessage(PURCHASE_SCHEMA);
        for (PurchaseItemDto purchaseItemDto : purchaseItemDtos) {
            System.out.printf(PURCHASE_INFO, purchaseItemDto.getProductName(),
                    purchaseItemDto.getQuantity(), purchaseItemDto.getPrice());
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
