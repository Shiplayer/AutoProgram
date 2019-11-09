import model.ItemsProduct;
import model.Order;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.Arrays;

public class HandleData {
    private BufferedReader bf;
    private Workbook workbook;
    private Sheet sheetService;
    private Sheet sheetProduct;
    private int rowProducts;
    private int countService;

    public HandleData(String fileName) throws FileNotFoundException {
        bf = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        workbook = new HSSFWorkbook();
        sheetService = workbook.createSheet("Service");
        sheetProduct = workbook.createSheet("Product");
        rowProducts = 0;
        countService = 0;
    }

    public HandleData() throws FileNotFoundException {
        workbook = new HSSFWorkbook();
        sheetService = workbook.createSheet("Service");
        sheetProduct = workbook.createSheet("Product");
        rowProducts = 0;
        countService = 0;
    }


    public static void main(String[] args) throws IOException {
        new HandleData("заказ-наряды.txt").run();
    }

    private void run() throws IOException {
        writeInSheet();
    }

    public void writeOrderWithProducts(Order order){
        Row row = sheetProduct.createRow(rowProducts++);
        writeCommonInformation(row, order);
        row.createCell(6).setCellValue(order.getProducts().size());
        for(ItemsProduct itemsProduct : order.getProducts()){
            row = sheetProduct.createRow(rowProducts++);
            row.createCell(0).setCellValue(itemsProduct.getProduct().getOriginalCode());
            row.createCell(1).setCellValue(itemsProduct.getAmount());
            row.createCell(2).setCellValue(itemsProduct.getPrice());
            row.createCell(3).setCellValue(itemsProduct.getSum());
        }
    }

    private void writeCommonInformation(Row row, Order order){
        try{
            row.createCell(0).setCellValue(order.getNumber());
            row.createCell(1).setCellValue(order.getContragent().getName());
            row.createCell(2).setCellValue(order.getDate());
            row.createCell(3).setCellValue(order.getSumOfProducts());
            row.createCell(4).setCellValue(order.getSumOfService());
            row.createCell(5).setCellValue(order.getTotalSum());
        } catch (NullPointerException e){
            System.out.println(order.getNumber());
            e.printStackTrace();
        }
    }

    public void writeOrderWithService(Order order){
        Row row = sheetService.createRow(countService++);
        writeCommonInformation(row, order);
    }

    public void writeInSheet() throws IOException {
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

    public void close() throws IOException {
        workbook.write(new FileOutputStream("заказ наряды.xls"));
        workbook.close();
    }
}
