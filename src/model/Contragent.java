package model;

public class Contragent {
    public String name;
    public String npp;

    public Contragent(String name, String npp){
        this.name = name;
        this.npp = npp;
    }

    @Override
    public String toString() {
        return "Contragent{" +
                "name='" + name + '\'' +
                ", npp='" + npp + '\'' +
                '}';
    }
}
