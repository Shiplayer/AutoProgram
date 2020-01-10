
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class Starter {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        OrderOutfitXmlToXls order =
                new OrderOutfitXmlToXls("C:\\Users\\vlada\\Desktop\\закнаряд 21.10 по 09.11.xml.tmp.xml");
        System.out.println(order.getOrderAt(0));
        order.writeInXls();
    }

}
