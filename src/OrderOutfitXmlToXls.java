import model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderOutfitXmlToXls {
    private Document document;
    private OrderOutfitBuilder orderOutfitBuilder;
    OrderOutfit orderOutfit;

    public OrderOutfitXmlToXls(String file) throws ParserConfigurationException, IOException, SAXException {
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(file));
        orderOutfitBuilder = OrderOutfitBuilder.getInstance();
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            switch (node.getNodeName()) {
                case "Contragent":
                    Contragent contragent = loadContagentInBuilder(node);
                    if(contragent != null)
                        orderOutfitBuilder.addContragent(contragent);
                    break;
                case "Ware":
                    System.out.println(node.getAttributes().getNamedItem("Npp").getNodeValue());
                    Product product = loadInBuilder(node);
                    if(product != null)
                        orderOutfitBuilder.addProduct(product);
                    break;
                case "OrderOrder":
                    orderOutfitBuilder.addOrder(loadOrderInBuilder(node));
                    break;
            }
        }
        orderOutfit = orderOutfitBuilder.build();
    }

    private Order loadOrderInBuilder(Node node) {
        NodeList nodeList = node.getChildNodes();
        Order order = new Order();
        for(int i = 0; i < nodeList.getLength(); i++){
            Node child = nodeList.item(i);
            switch (child.getNodeName()){
                case "Items":
                    order.setProducts(handleItems(child.getChildNodes()));
                    break;
                case "Number":
                    order.setNumber(Integer.parseInt(child.getTextContent()));
                    break;
                case "Date":
                    order.setDate(child.getTextContent());
                    break;
                case "Sum":
                    order.setTotalSum(Double.parseDouble(child.getTextContent()));
                    break;
                case "SumOfWorks":
                    order.setSumOfService(Double.parseDouble(child.getTextContent()));
                    break;
                case "SumOfWares":
                    order.setSumOfProducts(Double.parseDouble(child.getTextContent()));
                    break;
                case "Contragent":
                    order.setNpp(Integer.parseInt(child.getAttributes()
                            .getNamedItem("Npp").getNodeValue()));
                    break;

            }
        }
        return order;
    }

    private List<ItemsProduct> handleItems(NodeList items) {
        List<ItemsProduct> list = new ArrayList<>();
        for(int i = 0; i < items.getLength(); i++){
            NodeList itemFields = items.item(i).getChildNodes();
            ItemsProduct product = new ItemsProduct();
            for(int j = 0; j < itemFields.getLength(); j++){
                Node field = itemFields.item(j);
                switch (field.getNodeName()){
                    case "Ware":
                        product.setNpp(field.getAttributes().getNamedItem("Npp").getNodeValue());
                        break;
                    case "Amount":
                        product.setAmount(Double.parseDouble(field.getTextContent()));
                        break;
                    case "Price":
                        product.setPrice(Double.parseDouble(field.getTextContent()));
                        break;
                    case "Sum":
                        product.setSum(Double.parseDouble(field.getTextContent()));
                        break;
                }
            }
            if(!items.item(i).getTextContent().trim().isEmpty())
                list.add(product);
        }
        return list;
    }

    private Contragent loadContagentInBuilder(Node node) {
        if(node.getAttributes().getNamedItem("IsOurFirm").getNodeValue().equals("True"))
            return null;
        String npp = node.getAttributes().getNamedItem("Npp").getNodeValue();
        NodeList list = node.getChildNodes();
        for(int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);
            switch (child.getNodeName()) {
                case "Caption":
                    return new Contragent(child.getTextContent(), npp);
            }
        }
         return null;
    }

    private Product loadInBuilder(Node node) {
        NodeList list = node.getChildNodes();
        for(int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);
            if (child.getNodeName().equals("OriginalCode")) {
                System.out.println(child.getTextContent());
                return new Product(child.getTextContent(), node.getAttributes()
                        .getNamedItem("Npp").getNodeValue());
            }
        }
        return null;
    }

    public Order getOrderAt(int index){
        return orderOutfit.getOrderAt(index);
    }

    public void writeInXls() throws FileNotFoundException {
        HandleData handleData = new HandleData("order outfit.txt");
    }
}
