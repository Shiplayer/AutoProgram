import javafx.util.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class CompareTwoXLSFiles {
    private final HashMap<String, Pair<Double, Double>> hashMap = new HashMap<>();
    private Workbook first;
    private Workbook second;
    private Workbook result;
    private Sheet notFound;

    public static void main(String[] args) throws IOException {
        new CompareTwoXLSFiles().run();
    }

    public void run() throws IOException {
        second = new HSSFWorkbook(new FileInputStream("xls/остатки товара на 01 янв 2018 из 1 с на проверку.xls"));
        first = new HSSFWorkbook(new FileInputStream("xls/Расширенный отчет об остатках на 01 янв 2018 года из АИ.xls"));
        result = new HSSFWorkbook();
        notFound = result.createSheet("not found");

        loadDataToHashMap();
        checkDataFromHashMap();


        writeInSheet(result.createSheet());
        writeInSheetWithZeroKey(result.createSheet("Zero"));
        result.write(new FileOutputStream("xls/result_остатки.xls"));
        result.close();
    }

    private void writeInSheet(Sheet sheet){
        int indexRow = 0;
        for(String key : hashMap.keySet()){
            Pair<Double, Double> pair = hashMap.get(key);
            if(pair.getKey() != pair.getValue() && pair.getValue() != 0) {
                Row row = sheet.createRow(indexRow++);
                putValueInRow(row, key, pair.getKey(), pair.getValue());
            }
        }
    }

    private void writeInSheetWithZeroKey(Sheet sheet){
        int indexRow = 0;
        for(String key : hashMap.keySet()){
            Pair<Double, Double> pair = hashMap.get(key);
            if(pair.getKey() != 0) {
                Row row = sheet.createRow(indexRow++);
                putValueInRow(row, key, pair.getKey(), pair.getValue());
            }
        }
    }

    private void putValueInRow(Row row, String key, double count, double cost){
        row.createCell(0).setCellValue(key);
        row.createCell(1).setCellValue(count);
        row.createCell(2).setCellValue(cost);
    }

    private void checkDataFromHashMap() {
        Sheet sheet = first.getSheetAt(0);
        boolean readContinue = false;
        Iterator<Row> it = sheet.rowIterator();
        while(it.hasNext()) {
            Row row = it.next();
            if (row.getRowNum() > 5) {
                Cell cell = row.getCell(4);
                try {
                    if (cell.getCellStyle().getFillForegroundColor() == 10) {
                        readContinue = true;
                        continue;
                    }
                    if (cell.getStringCellValue().isEmpty())
                        readContinue = false;
                    if (readContinue) {
                        if (!hashMap.containsKey(cell.getStringCellValue())) {
                            System.err.println(cell.getStringCellValue() + " " + cell.getRow().getRowNum() + " " + cell.getCellStyle().getFillForegroundColor());
                            putValueInRow(notFound.createRow(notFound.getLastRowNum() + 1), cell.getStringCellValue(), row.getCell(7).getNumericCellValue(), row.getCell(9).getNumericCellValue());
                        }
                        else
                            hashMap.put(cell.getStringCellValue(), updateDegreePair(hashMap.get(cell.getStringCellValue()), new Pair<>(row.getCell(7).getNumericCellValue(), row.getCell(9).getNumericCellValue())));
                    }
                } catch (NullPointerException e) {
                    System.err.println(row.getRowNum());
                    e.printStackTrace();
                }
            }
        }
    }

    public Pair<Double, Double> updateDegreePair(Pair<Double, Double> first, Pair<Double, Double> second){
        return new Pair<>(first.getKey() - second.getKey(), first.getValue() - second.getValue());
    }

    private void loadDataToHashMap(){
        Sheet sheet = second.getSheetAt(0);
        for (Iterator<Row> it = sheet.rowIterator(); it.hasNext(); ) {
            Row row = it.next();
            if(row.getRowNum() > 5){
                Cell cell = row.getCell(2);
                if(cell.getCellStyle().getFillForegroundColor() == 64) {
                    if(hashMap.containsKey(cell.getStringCellValue()))
                        hashMap.put(cell.getStringCellValue(), updatePair(hashMap.get(cell.getStringCellValue()), new Pair<>(row.getCell(3).getNumericCellValue(), row.getCell(5).getNumericCellValue())));
                    else
                        hashMap.put(cell.getStringCellValue(), new Pair<>(row.getCell(3).getNumericCellValue(), row.getCell(5).getNumericCellValue()));
                }
            }
        }
    }

    private Pair<Double, Double> updatePair(Pair<Double, Double> first, Pair<Double, Double> second){
        return new Pair<>(first.getKey() + second.getKey(), first.getValue() + second.getValue());
    }
}
