package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderOutfitBuilder {
    private HashMap<Integer, Product> productList;
    private static OrderOutfitBuilder builder;
    private HashMap<Integer, Contragent> listOfContragent;
    private List<Order> listOfOrder;

    private OrderOutfitBuilder(){
        productList = new HashMap<>();
        listOfContragent = new HashMap<>();
        listOfOrder = new ArrayList<>();
    }

    public static OrderOutfitBuilder getInstance(){
        if(builder == null){
            builder = new OrderOutfitBuilder();
        }
        return builder;
    }

    public void addProduct(Product product){
        productList.put(product.getNpp(), product);
    }

    public void addOrder(Order order){
        listOfOrder.add(order);
    }

    public void addContragent(Contragent contragent){
        listOfContragent.put(contragent.getNpp(), contragent);
    }

    public OrderOutfit build() {
        if(listOfContragent.size() != listOfContragent.size()) {
            System.err.println("error");
            return null;
        }
        for(Order order : listOfOrder){
            order.setContragent(listOfContragent.get(order.getNpp()));
            for(ItemsProduct product : order.getProducts()){
                product.setProduct(productList.get(product.getNpp()));
            }
        }
        return new OrderOutfit(productList, listOfContragent, listOfOrder);
    }
}
