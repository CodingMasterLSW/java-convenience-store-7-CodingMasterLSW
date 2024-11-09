package store.view;

import java.util.List;
import store.domain.product.dto.ProductDto;
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

    private OutputView(){
    }

    public static OutputView create() {
        return new OutputView();
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

    public void printPurchaseInfo(List<PurchaseItemDto> purchaseItemDtos) {
        printMessage(RECEIPT_BANNER);
        printMessage(PURCHASE_SCHEMA);
        for (PurchaseItemDto purchaseItemDto : purchaseItemDtos) {
            System.out.printf(PURCHASE_INFO, purchaseItemDto.getProductName(), purchaseItemDto.getQuantity(), purchaseItemDto.getPrice());
            System.out.println();
        }

    }

    private static String getPromotion(ProductDto productDto) {
        String promotion = productDto.getPromotion();
        if (productDto.getPromotion() == "null" || productDto.getPromotion() == null) {
            promotion = "";
        }
        return promotion;
    }

    private static String getString(ProductDto productDto) {
        String quantity = String.valueOf(productDto.getQuantity()) +"개";
        if (quantity.equals("0개")) {
            quantity = "재고 없음";
        }
        return quantity;
    }

    private void printMessage(String message) {
        System.out.println(message);
    }

}
