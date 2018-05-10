package model;

public class Contragent {
    public String name;
    public int npp;

    public Contragent(String name, String npp){
        this.name = name;
        this.npp = Integer.parseInt(npp);
    }

    public String getName() {
        return name;
    }

    public int getNpp() {
        return npp;
    }

    @Override
    public String toString() {
        return "Contragent{" +
                "name='" + name + '\'' +
                ", npp='" + npp + '\'' +
                '}';
    }
}
