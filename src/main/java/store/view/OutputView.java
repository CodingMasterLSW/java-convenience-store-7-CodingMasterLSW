package store.view;

import java.util.List;
import store.dto.ProductDto;
import store.dto.PurchaseDto;
import store.dto.PurchaseItemDto;

public class OutputView {

    private static final String INTRODUCE_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String CONTAIN_PRODUCT_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String BLANK = "";
    private static final String PRODUCT_INFO = "- %s %,d원 %s %s";

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
        System.out.println(purchaseDto.getTotalPrice());
        System.out.println(purchaseDto.getTotalQuantity());
    }

    public void printPurchaseInfo(List<PurchaseItemDto> purchaseItemDtos) {
        for (PurchaseItemDto purchaseItemDto : purchaseItemDtos) {
            System.out.println(purchaseItemDto.getProductName());
            System.out.println(purchaseItemDto.getPrice());
            System.out.println(purchaseItemDto.getQuantity());
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
