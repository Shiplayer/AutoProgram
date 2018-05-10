package model;

public class Product {
    private String originalCode;
    private int npp;

    public Product(String originalCode, String npp){
        this.originalCode = originalCode;
        this.npp = Integer.parseInt(npp);
    }
}
