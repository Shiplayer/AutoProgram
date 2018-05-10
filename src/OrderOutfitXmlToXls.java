import model.Contragent;
import model.OrderOutfitBuilder;
import model.Product;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class OrderOutfitXmlToXls {
    private Document document;
    private OrderOutfitBuilder orderOutfitBuilder;

    public OrderOutfitXmlToXls(String file) throws ParserConfigurationException, IOException, SAXException {
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(file));
        orderOutfitBuilder = OrderOutfitBuilder.getInstance();
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            switch (node.getNodeName()) {
                case "Contragent":
                    Contragent contragent = loadContagentInBuilder(node);
                    System.out.println(contragent);
                    break;
                case "Ware":
                    System.out.println(node.getAttributes().getNamedItem("Npp").getNodeValue());
                    Product product = loadInBuilder(node);
                    if(product != null)
                        orderOutfitBuilder.addProduct(product);
                    break;
                case "OrderOrder":

            }
        }
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

    public String getListOfOrder(){
        return null;
    }
}
