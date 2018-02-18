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

    public static void main(String[] args) throws IOException {
        new CompareTwoXLSFiles().run();
    }

    public void run() throws IOException {
        first = new HSSFWorkbook(new FileInputStream("xls/Оборотно-Сальдовый  2015 по 2017.xls"));
        second = new HSSFWorkbook(new FileInputStream("xls/Анализ субконто на 01 янв  2018 примерно.xls"));

        loadDataToHashMap();
        checkDataFromHashMap();

        result = new HSSFWorkbook();
        writeInSheet(result.createSheet());
        result.write(new FileOutputStream("xls/result.xls"));
        result.close();
    }

    private void writeInSheet(Sheet sheet){
        int indexRow = 0;
        for(String key : hashMap.keySet()){
            Pair<Double, Double> pair = hashMap.get(key);
            if(pair.getKey() != pair.getValue() && pair.getValue() != 0) {
                Row row = sheet.createRow(indexRow++);
                row.createCell(0).setCellValue(key);
                row.createCell(1).setCellValue(pair.getKey());
                row.createCell(2).setCellValue(pair.getValue());
            }
        }
    }

    private void checkDataFromHashMap() {
        Sheet sheet = first.getSheetAt(0);
        for (Iterator<Row> it = sheet.rowIterator(); it.hasNext(); ) {
            Row row = it.next();
            if (row.getRowNum() % 22 > 2) {
                Cell cell = row.getCell(0);
                if(cell.getStringCellValue().isEmpty())
                    break;
                if (!hashMap.containsKey(cell.getStringCellValue()))
                    System.err.println(cell.getStringCellValue());
                else
                    hashMap.put(cell.getStringCellValue(), updateDegreePair(hashMap.get(cell.getStringCellValue()), new Pair<>(row.getCell(6).getNumericCellValue(), row.getCell(7).getNumericCellValue())));
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
            if(row.getRowNum() > 6){
                Cell cell = row.getCell(0);
                if(cell.getCellStyle().getFillForegroundColor() == 30) {
                    if(hashMap.containsKey(cell.getStringCellValue()))
                        hashMap.put(cell.getStringCellValue(), updatePair(hashMap.get(cell.getStringCellValue()), new Pair<>(row.getCell(6).getNumericCellValue(), row.getCell(7).getNumericCellValue())));
                    else
                        hashMap.put(cell.getStringCellValue(), new Pair<>(row.getCell(6).getNumericCellValue(), row.getCell(7).getNumericCellValue()));
                }
            }
        }
    }

    private Pair<Double, Double> updatePair(Pair<Double, Double> first, Pair<Double, Double> second){
        return new Pair<>(first.getKey() + second.getKey(), first.getValue() + second.getValue());
    }
}
