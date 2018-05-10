import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.Arrays;

public class HandleData {
    private BufferedReader bf;
    private Workbook workbook;

    public HandleData(String fileName) throws FileNotFoundException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        Workbook workbook = new HSSFWorkbook();
    }

    public static void main(String[] args) throws IOException {
        new HandleData("заказ-наряды.txt").run();
    }

    private void run() throws IOException {
        writeInSheet();
    }

    public void writeInSheet() throws IOException {

        Sheet sheetService = workbook.createSheet("Service");
        Sheet sheetProduct = workbook.createSheet("Product");
        Row row;
        int countService = 0;
        int countProduct = 0;

        String line1, line2;
        String[] buf, buf2;
        double costs = 0;
        while(bf.ready()){
            line1 = bf.readLine();
            line2 = bf.readLine();
            if(line2.contains("null")){
                row = sheetService.createRow(countService++);
                buf = line1.split("\t");
                for(int i = 0; i < buf.length; i++){
                    row.createCell(i).setCellValue(buf[i]);
                }
            } else {
                costs = 0;
                double discount;
                row = sheetProduct.createRow(countProduct++);
                buf = line1.split("\t");
                int len = buf.length;
                System.out.println(Arrays.toString(buf));
                discount = Double.parseDouble(buf[3]);
                double costProducts = Double.parseDouble(buf[4]);
                for(int i = 0; i < buf.length; i++){
                    row.createCell(i).setCellValue(buf[i]);
                }
                buf = line2.split(",");
                System.out.println(Arrays.toString(buf));
                row.createCell(8).setCellValue(buf.length);
                for(int i = 0; i < buf.length; i++){
                    System.out.println("create row");
                    row = sheetProduct.createRow(countProduct++);
                    buf2 = buf[i].split("\t");
                    row.createCell(0).setCellValue(buf2[0].substring(1));
                    System.out.println(Arrays.toString(buf2));
                    double cost = Double.parseDouble(buf2[1]) - Double.parseDouble(buf2[1]) * discount / 100;
                    row.createCell(1).setCellValue(cost);
                    double count = Double.parseDouble(buf2[2]);
                    row.createCell(2).setCellValue(count);
                    row.createCell(3).setCellValue(cost * count);
                    costs += row.getCell(3).getNumericCellValue();
                }
                System.out.println(costs + " " + costProducts);
                if(costs != costProducts){
                    row.getCell(3).setCellValue(row.getCell(3).getNumericCellValue() - (costs - costProducts));
                }

            }

        }

        workbook.write(new FileOutputStream("заказ наряды.xls"));
        workbook.close();
    }

    public void writeInSheet(OrderOutfitXmlToXls order) {

    }
}
