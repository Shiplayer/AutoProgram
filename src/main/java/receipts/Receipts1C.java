package receipts;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Receipts1C {
    File file;
    Document document;
    public Receipts1C(String file) throws ParserConfigurationException, IOException, SAXException {
        this.file = new File(file);
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        document = documentBuilder.parse(this.file);
    }

    public String[] typeOfObjects(){
        List<String> listOfTypes = new ArrayList<>();
        Element root = document.getDocumentElement();
        NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if(list.item(i).hasAttributes()){
                Node node = list.item(i).getAttributes().getNamedItem("Тип");
                if(node != null)
                    listOfTypes.add(node.getNodeValue());
            }
        }
        return listOfTypes.toArray(new String[0]);
    }
}
