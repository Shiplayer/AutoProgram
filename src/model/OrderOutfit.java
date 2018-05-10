package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderOutfit {
    private HashMap<Integer, Product> productList;
    private HashMap<Integer, Contragent> listOfContragent;
    private List<Order> listOfOrder;
    private List<Order> listOfOnlyService;

    public OrderOutfit(HashMap<Integer, Product> productList, HashMap<Integer, Contragent> listOfContragent, List<Order> listOfOrder) {
        this.productList = productList;
        this.listOfContragent = listOfContragent;
        this.listOfOrder = listOfOrder;
        listOfOnlyService = new ArrayList<>();
        ArrayList<Order> products = new ArrayList<>();
        for(Order order : listOfOrder){
            if(order.getSumOfProducts() == 0.){
                listOfOnlyService.add(order);
            } else
                products.add(order);
        }
        listOfOrder = products;
    }

    public HashMap<Integer, Product> getProductList() {
        return productList;
    }

    public HashMap<Integer, Contragent> getListOfContragent() {
        return listOfContragent;
    }

    public List<Order> getListOfOrder() {
        return listOfOrder;
    }

    public Order getOrderAt(int index){
        return listOfOrder.get(index);
    }

    public int size(){
        return listOfOrder.size();
    }

    public List<Order> getListOfOnlyService() {
        return listOfOnlyService;
    }

    @Override
    public String toString() {
        return "OrderOutfit{" +
                "productList=" + productList +
                ", listOfContragent=" + listOfContragent +
                ", listOfOrder=" + listOfOrder +
                '}';
    }
}
