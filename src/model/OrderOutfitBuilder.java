package model;

import java.util.ArrayList;
import java.util.List;

public class OrderOutfitBuilder {
    private List<Product> productList;
    private static OrderOutfitBuilder builder;

    private OrderOutfitBuilder(){
        productList = new ArrayList<>();
    }

    public static OrderOutfitBuilder getInstance(){
        if(builder == null){
            builder = new OrderOutfitBuilder();
        }
        return builder;
    }

    public void addProduct(Product product){
        productList.add(product);
    }
}
