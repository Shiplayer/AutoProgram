import javafx.util.Pair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ExpendableInvoices {
    private class ExpendableInvoicesEntity{
        public String name;
        public String date;
        public double total;

        public ExpendableInvoicesEntity(String name, String date, double total) {
            this.name = name;
            this.date = date;
            this.total = total;
        }
    }
    private HashMap<Integer, ExpendableInvoicesEntity> map;
    public static void main(String[] args) throws IOException, InvalidFormatException {
        new ExpendableInvoices().run();
    }

    private void run() throws IOException, InvalidFormatException {
        File file = new File("xls/расходные накладные/расходные накладные.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        map = new HashMap<>();
        XSSFSheet sheet = workbook.getSheetAt(0);
        Row row;
        Cell cell;
        Iterator<Row> rowIterator = sheet.rowIterator();
        if(rowIterator.hasNext()){
            rowIterator.next();
        }
        while(rowIterator.hasNext()){
            row = rowIterator.next();
            cell = row.getCell(0);
            double value = cell.getNumericCellValue();
            cell = row.getCell(2);
            String date = cell.getStringCellValue();
            cell = row.getCell(3);
            String name = cell.getStringCellValue();
            cell = row.getCell(4);
            int number = Integer.parseInt(cell.getStringCellValue());
            ExpendableInvoicesEntity entity = map.getOrDefault(number, null);
            if(entity == null){
                entity = new ExpendableInvoicesEntity(name, date, value);
            } else {
                if(entity.name.equals(name)){
                    entity.total = (double) entity.total + value;

                }
            }
            map.put(number, entity);
        }
        sheet = workbook.createSheet("result");

        Iterator<Integer> keyIterator = map.keySet().iterator();
        while(keyIterator.hasNext()){
            int number = keyIterator.next();
            ExpendableInvoicesEntity entity = map.get(number);
            row = sheet.createRow(sheet.getLastRowNum() + 1 );
            cell = row.createCell(0);
            cell.setCellValue(number);
            cell = row.createCell(1);
            cell.setCellValue(entity.date);
            cell = row.createCell(2);
            cell.setCellValue(entity.name);
            cell = row.createCell(3);
            cell.setCellValue(entity.total);
        }
        workbook.write(new FileOutputStream(file));


    }
}
