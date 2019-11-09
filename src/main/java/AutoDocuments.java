import java.util.Arrays;

public class AutoDocuments {

    class Products{
        String id;
        double count;
        double cost;
        double totalCost;

        @Override
        public String toString() {
            return id + "\t" + cost + "\t" + count + "\t" + totalCost;
        }
    }

    private String StrConvert(String str){
        StringBuilder buf = new StringBuilder();
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) >= '0' && str.charAt(i) <= '9')
                buf.append(str.charAt(i));
            else if(str.charAt(i) == '.')
                buf.append(str.charAt(i));
        }
        return buf.toString();
    }


    //for products 1, 6, 7, 9
    private int number;
    private String date;
    private String clientName;
    private double discount;
    private double costServices;
    private double costProducts;
    private Products[] products;
    private String comments;

    public AutoDocuments() {
        number = -1;
        date = null;
        clientName = null;
        discount = -1;
        costProducts = -1;
        costServices = -1;
        products = null;
        comments = null;
    }

    public String getClientName() {
        return clientName;
    }

    public double getDiscount() {
        return discount;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCostProducts() {
        return costProducts;
    }

    public double getCostServices() {
        return costServices;
    }

    public void setCostProducts(double costProducts) {
        this.costProducts = costProducts;
    }

    public void setCostServices(double costServices) {
        this.costServices = costServices;
    }

    public void setProducts(String[] products) {
        String buf[];
        Products prod[] = new Products[products.length - 1];
        for(int  i = 0; i < products.length - 1; i++) {
            buf = products[i + 1].split("\t");
            prod[i] = new Products();
            prod[i].id = buf[1].trim();
            prod[i].cost = Double.parseDouble(StrConvert(buf[6].replaceAll("[ ]","").replace(",", ".")));
            prod[i].count = Double.parseDouble(StrConvert(buf[7].replace(",", ".")));
            prod[i].totalCost = Double.parseDouble(StrConvert(buf[9].replaceAll("[ ]","").replace(",", ".")));
        }
        this.products = prod;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getProducts() {
        return String.join("|", Arrays.toString(products));
    }

    public String getComments() {
        return comments == null ? "" : comments;
    }
}
