package model;

public class ItemsProduct {
    private double amount;
    private double sum;
    private double price;
    private int npp;
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNpp() {
        return npp;
    }

    public void setNpp(String npp) {
        this.npp = Integer.parseInt(npp);
    }

    @Override
    public String toString() {
        return "ItemsProduct{" +
                "amount=" + amount +
                ", sum=" + sum +
                ", price=" + price +
                ", npp=" + npp +
                ", product=" + product +
                '}';
    }
}
