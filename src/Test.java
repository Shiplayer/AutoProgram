import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class Test {
    private Robot robot;
    public static void main(String[] args) throws UnsupportedEncodingException, AWTException, InterruptedException {
        String str = "1 750.00";
        String newStr = new String("1 750.00".getBytes(), "UTF-8");
        for(int i = 0; i < newStr.length(); i++){
            if(newStr.charAt(i) >= '0' && newStr.charAt(i) <= '9') {
                System.out.print(newStr.charAt(i));
            } else if(newStr.charAt(i) == '.')
                System.out.print(newStr.charAt(i));
        }
        //System.out.println(Double.parseDouble(str.replaceAll("[ ]", "")));
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
