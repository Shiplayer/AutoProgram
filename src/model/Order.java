package model;

import java.util.List;

public class Order {
    private List<ItemsProduct> products;
    private int number;
    private String date;
    private double totalSum;
    private String caption;
    private double sumOfService;
    private double sumOfProducts;
    private int npp; //parent Node is Contragent
    private Contragent contragent;

    public Contragent getContragent() {
        return contragent;
    }

    public void setContragent(Contragent contragent) {
        this.contragent = contragent;
    }

    public List<ItemsProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ItemsProduct> products) {
        this.products = products;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public double getSumOfService() {
        return sumOfService;
    }

    public void setSumOfService(double sumOfService) {
        this.sumOfService = sumOfService;
    }

    public double getSumOfProducts() {
        return sumOfProducts;
    }

    public void setSumOfProducts(double sumOfProducts) {
        this.sumOfProducts = sumOfProducts;
    }

    public int getNpp() {
        return npp;
    }

    public void setNpp(int npp) {
        this.npp = npp;
    }

    @Override
    public String toString() {
        return "Order{" +
                "products=" + products +
                ", number=" + number +
                ", date='" + date + '\'' +
                ", totalSum=" + totalSum +
                ", caption='" + caption + '\'' +
                ", sumOfService=" + sumOfService +
                ", sumOfProducts=" + sumOfProducts +
                ", npp=" + npp +
                ", contragent=" + contragent +
                '}';
    }
}
