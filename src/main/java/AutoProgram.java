import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Anton on 05.10.2017.
 */
public class AutoProgram {
    private Robot robot;
    private Clipboard clipboard;
    private ArrayList<AutoDocuments> list;
    enum Status {DRAFT, IN_WORK, CLOSED, REFUSAL, WHITE}
    enum Selected {TOP, MIDDLE, DOWN, END}

    private PrintWriter pw;

    private class Color {
        public final int red;
        public final int green;
        public final int blue;

        public Color(int rgb){
            red = (rgb & 0x00ff0000) >> 16;
            green = (rgb & 0x0000ff00) >> 8;
            blue = rgb & 0x000000ff;
        }

        public Color(int red, int green, int blue){
            this.red = red;
            this.green = green;
            this.blue = blue;
        }


        public boolean equals(Color color) {
            double position = abs(sqrt(pow((this.red - color.red), 2) + pow((this.green - color.green), 2) + pow(this.blue - color.blue,2)));
            return position < 15;
        }

        @Override
        public String toString() {
            return "[r=" + red + ",g=" + green + ",b=" + blue + "]";
        }
    }

    public Color color;
    public Color colorClosed = new Color(99, 150,239);
    public Color colorInWork = new Color(181,239,247);
    public Color colorRefusal = new Color(231,113,156);
    public Color colorDraft = new Color(156,255,156);
    public Color colorSelected = new Color(49, 105,198);
    public Color white = new Color(255,255,255);
    public Status docks[] = new Status[21];

    public static void main(String[] args) throws IOException, AWTException, InterruptedException, UnsupportedFlavorException {
        new AutoProgram().run();
    }

    /*
    # 1139, 263 (1150, 265) to (1151, 287) 265-287 =23

     */

    private boolean checkTask(BufferedImage bi) throws InterruptedException {
        int d = 23;
        for(int i = 0; i < 21; i++) {
            color = new Color(bi.getRGB(1140, 260 + i * d));
            if (color.equals(colorClosed))
                docks[i] = Status.CLOSED;
            if(color.equals(colorDraft))
                docks[i] = Status.DRAFT;
            if(color.equals(colorInWork))
                docks[i] = Status.IN_WORK;
            if(color.equals(colorRefusal))
                docks[i] = Status.REFUSAL;
        }
        color = new Color(bi.getRGB(1140, 260 + 21 * d));
        System.out.println(Arrays.toString(docks));
        return !color.equals(white);
    }

    //boxes: first area((1033, 78), (1339, 98)); second area ((1345, 78), (1674, 81))

    private boolean CloseTab(int tabs, boolean all) throws IOException, InterruptedException {
        Rectangle r = new Rectangle(1000, 78, 800, 20);
        BufferedImage bi = robot.createScreenCapture(r);
        File file_img = new File("areaField.png");
        Color colorCloseTabs = new Color(206, 60,57);
        int[] arrRBG = bi.getRGB(bi.getMinX(), bi.getMinY() + 11, 800, 1, null, 0, 800);
        int countTabs = 0;
        ArrayList<Integer> arrCoordsTabs = new ArrayList<>();
        for(int i = 0; i < 799; i++){
            if(colorCloseTabs.equals(new Color(arrRBG[i])) && white.equals(new Color(arrRBG[i + 1]))) {
                int newX = 1000 + bi.getMinX() + i;
                int newY = 78 + bi.getMinY() + 11;
                countTabs++;
                arrCoordsTabs.add(newX);
                arrCoordsTabs.add(newY);
            }
        }
        if(all){
            for(int i = countTabs - 1; i >= 0; i--){
                robot.mouseMove(arrCoordsTabs.get(i * 2), arrCoordsTabs.get(i * 2 + 1));
                MousePress(KeyEvent.BUTTON1_MASK);
                Thread.sleep(100);
            }
        }else {
            robot.mouseMove(arrCoordsTabs.get((tabs - 1) * 2), arrCoordsTabs.get((tabs - 1) * 2 + 1));
            MousePress(KeyEvent.BUTTON1_MASK);
        }
        ImageIO.write(bi, "png", file_img);
        //System.out.println(bytes.length + " " + (800*20));
        return false;
    }

    /* # (655, 265) - начало

# (390, 224) - позиния даты
# (374, 248) - позиция номера документа
# (929, 247) - позиция скидки товара
# (382, 646) - позиция наименования клиента

# (552, 139) - позиция вкладки работа
# (485, 232) - первая позиция услуги; (1696, 232) - цена услуги
# (485, 255) - второя позиция услуги; (1696, 255) - цена услуги
#
#
# (606, 140) - позиция вкладки товары
# (439, 232) - первая позиция товара
# (439, 255) - вторая позиция товара; (1415, 255) - цена товара; (1491, 255) - количество товаров
*/

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

    private String GetDataFromPoint(int x, int y) throws InterruptedException, IOException, UnsupportedFlavorException {
        Thread.sleep(200);
        robot.mouseMove(x, y);
        MousePress(KeyEvent.BUTTON1_MASK);
        SelectAll();
        CopyPress();
        return clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor).toString();
    }

    private AutoDocuments getInfoFromDock(int x, int y) throws InterruptedException, IOException, UnsupportedFlavorException {
        AutoDocuments documents = new AutoDocuments();
        robot.mouseMove(x, y);
        MousePress(KeyEvent.BUTTON1_MASK);
        keyPress(KeyEvent.VK_ENTER);
        Thread.sleep(4000);

        documents.setDate(GetDataFromPoint(390, 224));
        documents.setNumber(Integer.parseInt(GetDataFromPoint(390, 248)));
        documents.setClientName(GetDataFromPoint(390, 650));
        documents.setDiscount(Double.parseDouble(StrConvert(GetDataFromPoint(929,247).replace(',','.'))));
        documents.setCostServices(Double.parseDouble(StrConvert(GetDataFromPoint(996, 802).replace(',','.'))));
        documents.setCostProducts(Double.parseDouble(StrConvert(GetDataFromPoint(551, 802).replace(',','.'))));
        if(documents.getCostProducts() != 0){
            robot.mouseMove(604, 134);
            MousePress(KeyEvent.BUTTON1_MASK);
            documents.setProducts(GetDataFromPoint(996, 802).split("\n"));
        }
        robot.mouseMove(913, 138);
        MousePress(KeyEvent.BUTTON1_MASK);
        System.out.println(GetDataFromPoint(907, 827).length());
        if(GetDataFromPoint(907, 827).split("\n").length <= 1) {
            documents.setComments("!!!");
        } else if(!GetDataFromPoint(907, 827).split("\n")[1].split("\t")[2].contains("ПКО")){
            robot.mouseMove(720, 217);
            MousePress(KeyEvent.BUTTON1_MASK);
            Thread.sleep(1000);
            documents.setComments(GetDataFromPoint(400,415) + " " + GetDataFromPoint(372, 365) + " " + GetDataFromPoint(1242, 212));
        }
        CloseTab(2, true);
        Thread.sleep(1000);
        return documents;
    }

    private void keyPress(int key) throws InterruptedException {
        robot.keyPress(key);
        Thread.sleep(50);
        robot.keyRelease(key);
    }

    private void SelectAll() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        Thread.sleep(50);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(500);
    }

    private void CopyPress() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_C);
        Thread.sleep(50);
        robot.keyRelease(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(500);
    }

    private void MousePress(int buttonEvent) throws InterruptedException {
        robot.mousePress(buttonEvent);
        robot.mouseRelease( buttonEvent);
        Thread.sleep(50);
        robot.mousePress(buttonEvent);
        robot.mouseRelease( buttonEvent);
        Thread.sleep(100);
    }

    public void printDocument(AutoDocuments documents){
        System.out.println(documents.getDate() + "\t" + documents.getNumber() + "\t" + documents.getClientName() + "\t"
                + documents.getDiscount() + "\t" + documents.getCostProducts() + "\t" + documents.getCostServices() + "\t" + documents.getComments() + "\n"
                + documents.getProducts());
        pw.println(documents.getDate() + "\t" + documents.getNumber() + "\t" + documents.getClientName() + "\t"
                + documents.getDiscount() + "\t" + documents.getCostProducts() + "\t" + documents.getCostServices() + "\t" + documents.getComments() + "\n"
                + documents.getProducts());
        pw.flush();
    }

    public int GetSelectedPosition(){
        BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        for(int i = 0; i < 21; i++){
            Color color = new Color(bufferedImage.getRGB(270, 263 + i * 23));
            if(color.equals(colorSelected)){
                return i;
            }
        }
        return -1;
    }

    public Status getStatus(int index){
        BufferedImage bi = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        color = new Color(bi.getRGB(1140, 260 + index * 23));
        if (color.equals(colorClosed))
            return Status.CLOSED;
        if(color.equals(colorDraft))
            return Status.DRAFT;
        if(color.equals(colorInWork))
            return Status.IN_WORK;
        if(color.equals(colorRefusal))
            return Status.REFUSAL;
        return Status.WHITE;
    }

    public boolean NextDocument(int index) throws InterruptedException, UnsupportedFlavorException, IOException {
        int d = 23;
        if(index == 21){
            robot.mouseMove(707, 263);
            for (int i = 0; i < 2; i++) {
                robot.mouseWheel(100);
                Thread.sleep(1000);
            }
            index = GetSelectedPosition() + 1;
        }

        Status s = getStatus(index);
        if(s == Status.CLOSED) {
            list.add(getInfoFromDock(707, 263 + index * d));
            printDocument(list.get(list.size() - 1));
        } else if(s == Status.WHITE) {
            return false;
        } else
            NextDocument(index + 1);
        return true;
    }

    private void run() throws IOException, AWTException, InterruptedException, UnsupportedFlavorException {
        list = new ArrayList<>();
        robot = new Robot();
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        pw = new PrintWriter(new FileOutputStream("заказ-наряды.txt", true));

        boolean runnable = false;

        //robot.mouseMove(50,50);
        Thread.sleep(1000);
        // TODO доработать
        boolean check;
        CloseTab(2, true);
        do {
            Thread.sleep(1000);
            //check = checkTask(robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())));
            //System.out.println(check);
            int index = GetSelectedPosition() + 1;
            if(runnable){
                index = 0;

                runnable = false;
            }
            if(!NextDocument(index))
                break;

            if(index % 20 > 10 && getStatus(21) != Status.WHITE) {
                robot.mouseMove(707, 263);
                for (int i = 0; i < 2; i++) {
                    robot.mouseWheel(100);
                    Thread.sleep(1000);
                }
            }
            //System.out.println(check);
        } while(true);

        //robot.mouseMove(1888, 17);
        //MousePress(KeyEvent.BUTTON1_MASK);

        /*MousePress(InputEvent.BUTTON1_MASK);
        BufferedImage bi = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        Thread.sleep(2000);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
*/
    }
}
