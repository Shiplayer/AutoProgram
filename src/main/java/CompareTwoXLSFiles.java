import javafx.util.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.DoubleUnaryOperator;

public class CompareTwoXLSFiles {
    private final HashMap<Integer, DataEntity> hashMap = new HashMap<>();
    private Workbook first;
    private Workbook second;
    private Workbook result;
    private Sheet sheetMap;

    public static void main(String[] args) throws IOException {
        new CompareTwoXLSFiles().run();
    }

    public void run() throws IOException {
        second = new HSSFWorkbook(new FileInputStream("пко 2018 по авг1 АИ.xls")); //Автоинтеллект
        first = new HSSFWorkbook(new FileInputStream("xls/пко.xls")); //1С

        //result = new HSSFWorkbook();
        //sheetMap = result.createSheet("Карты");

        loadDataToHashMap();
        checkDataFromHashMap();

        if(!hashMap.isEmpty()){
            System.out.println("не найдены соответсвия с 1C");
            for (Integer number : hashMap.keySet()) {
                System.out.println(hashMap.get(number).toString());
            }
        }

        //writeInSheet(sheetMap);
        //writeInSheetWithZeroKey(result.createSheet());
//        result.write(new FileOutputStream("xls/накладные.xls"));
//        result.close();
    }


    /*private void writeInSheet(Sheet sheet){
        int indexRow = 0;
        for(String key : hashMap.keySet()){
            Pair<String, Double> pair = hashMap.get(key);
            if(pair.getValue() != 0) {
                Row row = sheet.createRow(indexRow++);
                putValueInRow(row, key, pair.getKey(), pair.getValue());
            }
        }
    }*/

    /*private void writeInSheetWithZeroKey(Sheet sheet){
        int indexRow = 0;
        for(String key : hashMap.keySet()){
            Pair<String, Double> pair = hashMap.get(key);
            if(pair.getKey() != 0) {
                Row row = sheet.createRow(indexRow++);
                putValueInRow(row, key, pair.getKey(), pair.getValue());
            }
        }
    }*/

    private void putValueInRow(Row row, String key, String date, double cost){
        row.createCell(0).setCellValue(key);
        row.createCell(1).setCellValue(date);
        row.createCell(2).setCellValue(cost);
    }

    private void checkDataFromHashMap() {
        Sheet sheet = first.getSheetAt(0);
        //boolean readContinue = false;
        Iterator<Row> it = sheet.rowIterator();
        Cell cell;
        DataEntity entity, secondEntity;
        List<String> list = new ArrayList<>();
        while(it.hasNext()) {
            Row row = it.next();
            cell = row.getCell(1);
            if(cell == null)
                continue;
            String date = cell.getStringCellValue().trim();
            if(date.contains(" ")){
                date = date.split(" ")[0];
            }
            cell = row.getCell(5);
            if(cell == null) {
                System.out.println(row.getRowNum());
                continue;
            }
            int number;
            if(cell.getCellTypeEnum() == CellType.STRING){
                number = Integer.parseInt(cell.getStringCellValue().trim());
            } else
                number = (int) cell.getNumericCellValue();
            cell = row.getCell(6);
            double cost = (double) Math.round(Math.ceil(cell.getNumericCellValue()*1000) / 10) / 100;
            String name = row.getCell(9).getStringCellValue().trim();
            entity = hashMap.remove(number);
            secondEntity = new DataEntity(date, number, name, cost);


            if(entity == null){
                list.add(number + "\t" + name + "\t" + date + "\t" + cost);
            } else if(!entity.equals(secondEntity)){
                System.out.println(entity.toString() + " vs " + secondEntity.toString());
            }
            /*if (row.getRowNum() > 5) {
                Cell cell = row.getCell(11);
                try {
                    if (!cell.getStringCellValue().isEmpty()) {
                        String name = cell.getStringCellValue().trim();

                        if (!hashMap.containsKey(name)) {
                            System.err.println(name + " " + cell.getRow().getRowNum());
                            putValueInRow(sheetMap.createRow(sheetMap.getLastRowNum() + 1), name, row.getCell(1).getStringCellValue(), row.getCell(6).getNumericCellValue());
                        }
                        else
                            hashMap.put(name, updateDegreePair(hashMap.get(name), new Pair<>(row.getCell(1).getStringCellValue(), row.getCell(6).getNumericCellValue())));
                    }
                } catch (NullPointerException e) {
                    System.err.println(row.getRowNum());
                    e.printStackTrace();
                }
            }*/
        }
        System.out.println("Не были найдены соответствия с автоинтеллектом");
        for (String buf :
                list) {
            System.out.println(buf);
        }
    }

    public Pair<String, Double> updateDegreePair(Pair<String, Double> first, Pair<String, Double> second){
        return new Pair<>(first.getKey() + " - " + second.getKey(), first.getValue() - second.getValue());
    }

    //загрузка с 1С
    private void loadDataToHashMap(){
        Sheet sheet = second.getSheetAt(0);
        Cell cell;
        for (Iterator<Row> it = sheet.rowIterator(); it.hasNext(); ) {
            Row row = it.next();
            cell = row.getCell(9);
            if(cell == null) {
                System.out.println(row.getRowNum());
                continue;
            }
            int number;
            if( cell.getCellTypeEnum() == CellType.STRING){
                if(!cell.getStringCellValue().isEmpty())
                    number = Integer.parseInt(cell.getStringCellValue().trim());
                else continue;
                /*DataEntity et = hashMap.get(num);
                if(et == null) {
                    System.out.println(num);
                    System.exit(1);
                }

                et.cost += row.getCell(6).getNumericCellValue();
                hashMap.put(num, et);
                continue;*/
            } else
                number = (int)cell.getNumericCellValue();
            cell = row.getCell(1);
            String date;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
            if(cell.getCellTypeEnum() == CellType.STRING)
                date = cell.getStringCellValue().trim().split(" ")[0];
            else
                date = sdf.format(cell.getDateCellValue());

            cell = row.getCell(3);
            String name = cell.getStringCellValue().trim();
            cell = row.getCell(6);
            double cost = cell.getNumericCellValue();

            if (hashMap.containsKey(number)) {
                DataEntity et = hashMap.get(number);
                et.cost = (double)Math.round((et.cost + cost) * 100) / 100;
                hashMap.put(number, et);
                //System.err.println("error: " + number);
            } else
                hashMap.put(number, new DataEntity(date, number, name, cost));
            /*if(row.getRowNum() > 755){
                cell = row.getCell(3);
                if(!cell.getStringCellValue().isEmpty()) {
                    String name = cell.getStringCellValue().trim();
                    if(hashMap.containsKey(name))
                        hashMap.put(name, updatePair(hashMap.get(name), new Pair<>(row.getCell(12).getStringCellValue(), row.getCell(9).getNumericCellValue())));
                    else
                        hashMap.put(name, new Pair<>(row.getCell(12).getStringCellValue(), row.getCell(9).getNumericCellValue()));
                }
            }*/
        }
    }

    private class DataEntity{
        public String date;
        public int number;
        public String name;
        public double cost;

        public DataEntity(String date, int number, String name, double cost) {
            this.date = date;
            this.number = number;
            this.name = name;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return number + " " + name + " " + date + " " + cost;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DataEntity that = (DataEntity) o;

            //if (number != that.number) return false;
            if (Double.compare(that.cost, cost) != 0) return false;
            return !(date != null ? !date.equals(that.date) : that.date != null);
            //return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = date != null ? date.hashCode() : 0;
            result = 31 * result + number;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            temp = Double.doubleToLongBits(cost);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }

    private Pair<String, Double> updatePair(Pair<String, Double> first, Pair<String, Double> second){
        return new Pair<>(first.getKey() + ", " + second.getKey(), first.getValue() + second.getValue());
    }
}
