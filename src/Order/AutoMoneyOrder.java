package Order;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.util.Arrays;

public class AutoMoneyOrder {

    BufferedReader bf;

    public static void main(String[] args) throws IOException {
        new AutoMoneyOrder().run();
    }

    private void run() throws IOException {
        bf = new BufferedReader(new InputStreamReader(new FileInputStream("test")));
        bf.readLine();
        String[] buf = bf.readLine().split("\t");
        //Workbook workbook = new HSSFWorkbook();
        //Sheet sheetService = workbook.createSheet("Money orders");
        System.out.println(Arrays.toString(buf));
        System.out.println(buf[0].split(" ")[0]);
        System.out.println(buf[2]);
        System.out.println(buf[4]);
        System.out.println();
    }
}
