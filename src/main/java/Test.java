import jdk.nashorn.internal.runtime.ParserException;
import org.apache.poi.ss.formula.functions.T;
import org.xml.sax.SAXException;
import receipts.Receipts1C;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class Test {
    private Robot robot;
    
    public static void main(String[] args) throws IOException, AWTException, InterruptedException, SAXException, ParserConfigurationException {
        /*String str = "1 750.00";
        String newStr = new String("1 750.00".getBytes(), "UTF-8");
        for(int i = 0; i < newStr.length(); i++){
            if(newStr.charAt(i) >= '0' && newStr.charAt(i) <= '9') {
                System.out.print(newStr.charAt(i));
            } else if(newStr.charAt(i) == '.')
                System.out.print(newStr.charAt(i));
        }*/

        /*        outer:
                for(int i = 0; i < 3; i++)
                {
                    for(int j = 3; j >= 0; j--)
                    {
                        if(i == j) continue outer;
                        System.out.println(i + " " + j);
                    }

        }*/


        /*Receipts1C receipts1C = new Receipts1C("xls/Новая папка/ПН 1109.xml");
        for(String type : receipts1C.typeOfObjects()){
            System.out.println(type);
        }*/

        //new Test().run();

        OrderOutfitXmlToXls order = new OrderOutfitXmlToXls("xml/зак нар с 03.10 по 21.10.xml.tmp.xml");
        System.out.println(order.getOrderAt(0));
        order.writeInXls();


        //System.out.println(Double.parseDouble(str.replaceAll("[ ]", "")));
    }

    public void run(){
        FastScanner fastScanner = new FastScanner(System.in);
        Number number;
        double sum = 0;
        while((number = fastScanner.nextNumber()) != null){
            sum += number.doubleValue();
        }
        System.out.printf("%.6f", sum);
    }

    class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream is) {
            br = new BufferedReader(new InputStreamReader(is));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    String line = br.readLine();
                    if(line == null){
                        return null;
                    } else
                        st = new StringTokenizer(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        Number nextNumber() {
            String word = next();
            if(word == null){
                return null;
            }
            try{
                return Double.parseDouble(word);
            } catch (NumberFormatException p){
                return nextNumber();
            }
        }
    }

    /*private String GetDataFromPoint(int x, int y) throws InterruptedException, IOException, UnsupportedFlavorException {
        Thread.sleep(200);
        robot.mouseMove(x, y);
        MousePress(KeyEvent.BUTTON1_MASK);
        SelectAll();
        CopyPress();
        return clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor).toString();
    }

    private void MousePress(int buttonEvent) throws InterruptedException {
        robot.mousePress(buttonEvent);
        robot.mouseRelease( buttonEvent);
        Thread.sleep(50);
        robot.mousePress(buttonEvent);
        robot.mouseRelease( buttonEvent);
    }*/
}
