import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CheckXLS {
    public static void main(String[] args) throws IOException {
        new CheckXLS().run();
    }

    private void run() throws IOException {
        Workbook first = new HSSFWorkbook(new FileInputStream("xls/Остатки товаров 1 января 2017 г. ИП Моженков В. И.xls"));
        Workbook second = new HSSFWorkbook(new FileInputStream("xls/Отчет об остатках 01 ян 2017.xls"));
        HashMap<String, Double> table = new HashMap<>();
        Sheet sheet = first.getSheetAt(0);
        for (Iterator<Row> it = sheet.rowIterator(); it.hasNext(); ) {
            Row row = it.next();
            if(row.getRowNum() > 7){
                Cell cell = row.getCell(3);
                table.put(LowerCase(cell.getStringCellValue().trim()), row.getCell(4).getNumericCellValue());
            }
        }
        first.close();
        sheet = second.getSheetAt(0);
        for (Iterator<Row> it = sheet.rowIterator(); it.hasNext(); ) {
            Row row = it.next();
            try {
                if (row.getRowNum() > 71) {
                    Cell cell = row.getCell(15);
                    if (table.containsKey(LowerCase(cell.getStringCellValue().toLowerCase().trim()))) {
                        table.put(LowerCase(cell.getStringCellValue().toLowerCase().trim()), row.getCell(20).getNumericCellValue());
                    } else {
                        System.err.println("can't find this is key \"" + cell.getStringCellValue() + "\" with value " + row.getCell(20).getNumericCellValue());
                    }
                }
            } catch (IllegalStateException | NullPointerException e){
                System.err.println(row.getRowNum() + " " + row.getCell(15).getStringCellValue());
                e.printStackTrace();
            }
        }

        for(String key : table.keySet()){
            //table.get
        }
    }

    public String LowerCase(String s){
        char[] chars = s.toCharArray();
        for(int i = 0; i < chars.length; i++){
            char ch = chars[i];
            if(ch >= '0' && ch <= '9')
                continue;
            else if(ch >= 'A' && ch <= 'Z'){
                chars[i] = (char) (chars[i] - ('a' - 'z' - 7));
            }
        }
        return new String(chars);
    }
}

/*
can't find this is key "30177" with value 2.0

0404161 1.0
pa349 1.0
7701208724 1.0
1366446 1.0
713850400 1.0
2124810 1.0
46085512b 1.0
8200276850 1.0
7700732903 -2.0
1607387880 1.0
re3904s 1.0
nplp6905 1.0
zs052 1.0
082026 1.0
0537388 1.0
372424 1.0
05620 1.0
7703074626 2.0
135000849r 1.0
progrid 1.0
543026226r 1.0
d02593 2.0
132300 1.0
504483 1.0
30603 1.0
254930 1.0
1640020 1.0
9036137 1.0
4625 2.0
7703179010 1.0
nlc4110b10 1.0
24903 1.0
432007595r -1.0
134088 1.0
8200412349 1.0
7711238597 1.0
ci112055fl 1.0
432007556r 1.0
0514c8 1.0
rn202009s0l00 1.0
402109533r 1.0
7701475118 1.0
gs601113535 2.0
7246asmv1h 1.0
jar1031 1.0
jar1030 1.0
546127371r 2.0
8200821816 1.0
d4617 2.0
7701048678 1.0
2833502 1.0
27150ed70a 1.0
ti18289 1.0
3100401 1.0
7701066088 1.0
7701476496 1.0
770135 2.0
5206qc 2.0
792839 1.0
6001550576 -6.0
1319n5 1.0
63606a 1.0
0398 1.0
1457431724 1.0
0281002996 1.0
303256 -1.0
8200768927 1.0
7701041015 1.0
12630 1.0
30030 2.0
3532501 2.0
509536 1.0
7104fv 1.0
0348q4 1.0
0258986602 1.0
ls932 1.0
9818914980 1.0
7711428132 8.0
50157100 1.0
212050 1.0
17177 1.0
745740 1.0
7703034304 1.0
9015x0 1.0
3397118905 1.0
806476047r 1.0
134211 4.0
1613328880 1.0
6923e9 1.0
1904031 1.0
8200366590 1.0
7588vx 10.0
087361 1.0
381791 1.0
8200537047 1.0
080739 2.0
rta5444 1.0
9808647280 1.0
7701473149 1.0
7246ags6zben 1.0
jar1257 2.0
30933 1.0
6450p7 1.0
61612349870 1.0
3017702 2.0
0249a9 1.0
3019500 1.0
30543 1.0
jbj738 2.0
cet84 2.0
575795 1.0
78089 1.0
422006010 1.0
8200188902 1.0

Process finished with exit code 0

 */