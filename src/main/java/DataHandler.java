import interfaces.IDataHandler;
import model.ItemsProduct;
import model.Order;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;

public class DataHandler implements IDataHandler {
    private Workbook workbook;
    private Sheet sheetService;
    private Sheet sheetProduct;
    private int rowProducts;
    private int countService;

    public DataHandler() {
        workbook = new HSSFWorkbook();
        sheetService = workbook.createSheet("Service");
        sheetProduct = workbook.createSheet("Product");
        rowProducts = 0;
        countService = 0;
    }

    @Override
    public void writeOrderWithProducts(Order order) {
        Row row = sheetProduct.createRow(rowProducts++);
        writeCommonInformation(row, order);
        row.createCell(6).setCellValue(order.getProducts().size());
        for (ItemsProduct itemsProduct : order.getProducts()) {
            row = sheetProduct.createRow(rowProducts++);
            row.createCell(0).setCellValue(itemsProduct.getProduct().getOriginalCode());
            row.createCell(1).setCellValue(itemsProduct.getAmount());
            row.createCell(2).setCellValue(itemsProduct.getPrice());
            row.createCell(3).setCellValue(itemsProduct.getSum());
        }
    }

    private void writeCommonInformation(Row row, Order order) {
        try {
            row.createCell(0).setCellValue(order.getNumber());
            row.createCell(1).setCellValue(order.getContragent().getName());
            row.createCell(2).setCellValue(order.getDate());
            row.createCell(3).setCellValue(order.getSumOfProducts());
            row.createCell(4).setCellValue(order.getSumOfService());
            row.createCell(5).setCellValue(order.getTotalSum());
        } catch (NullPointerException e) {
            System.out.println(order.getNumber());
            e.printStackTrace();
        }
    }

    @Override
    public void writeOrderWithService(Order order) {
        Row row = sheetService.createRow(countService++);
        writeCommonInformation(row, order);
    }

    @Override
    public void close() throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        System.out.print("\n Enter name of file: ");
        workbook.write(new FileOutputStream(reader.readLine()));
        workbook.close();
    }
}
