import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

interface DataConnection {
    /**
     * method that loading data from file (1.txt) and calculate total sum for specific year (the specific year specified in the {@link MyApp#year})
     * @return total sum for specific year
     * @throws Exception if we have any exception in this method, we should throw that Exception
     */
    int loadDatas() throws Exception;

    /**
     * saving data in the file (statistika.txt) in format ({@link MyApp#COUNT1}    year
     * @param year the specific year
     * @param averageSum total sum for specific year divided by total number of years
     * @throws IOException if we have any exception with IO
     */
    void saveData(int year, int averageSum) throws IOException;
}

public class MyApp implements DataConnection {
    public static class MyAppFactory {
        public static MyApp create(String y) {
            return new MyApp(y);
        }
    }

    public MyApp(String year) {
        this.year = year;
    }

    private String year;
    private static int COUNT = 0;
    private static int COUNT1 = 0;
    protected static int startYear = 1990;
    protected static int endYear = 2020;

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            int sum;
            System.out.println("app v.1.13");
            MyApp app;
            for (int i = startYear; i < endYear; i++) {
                sum = 0;
                COUNT = 0;
                COUNT1 = 0;
                String year = String.valueOf(i);
                app = MyAppFactory.create(year);
                //sum = app.loadDatas(sum);
                sum = app.loadDatas();
                double averageSum = sum > 0 ? (double) sum / (double) COUNT : 0;
                if (averageSum > 0) {
                    System.out.println(i + "  " + averageSum);
                }
                app.saveData(i, (int) averageSum);
            }
            System.out.println("gotovo");
        } catch (Exception e) {
            System.out.println("oshibka!");
        }
    }

    public int loadDatas() throws Exception {
        File file = new File("1.txt");
        String content = readFromFile(file);
        return parseDatasFromContent(content);
    }


    /**
     * compare all lines in {@param content} with specified year
     * @param content content that contains in file
     * @return sum for specified year
     */
    public int parseDatasFromContent(String content){
        int sum = 0;
        int begin = 0;
        while (true) {
            int e = content.indexOf("\n", begin + 1);
            if (e == -1) {
                break;
            }
            String ss = content.substring(begin, e - 1);
            String[] sss = ss.split(" {5}");
            if (sss[2].contains(this.year) || sss[2].contains(year)) {
                sum += Integer.parseInt(sss[3]);
            }
            COUNT++;
            begin = e;
        }
        return sum;
    }

    /**
     * method for read all data from file and return it
     * @param file the input file from which will be read data
     * @return all data from input file
     * @throws IOException
     */
    public String readFromFile(File file) throws IOException {
        StringBuilder builder = new StringBuilder();
        FileInputStream fis = new FileInputStream(file);
        int i = fis.read();
        do {
            builder.append(new String(new byte[]{(byte) i}));
            i = fis.read();
        } while (i != -1);
        return builder.toString();
    }

    public void saveData(int year, int averageSum) throws IOException {
        FileOutputStream fis = new FileOutputStream(new File("statistika.txt"), true);
        String s;
        s = COUNT1 + "    " + year + "        " + averageSum + "\n";
        fis.write(s.getBytes());
        COUNT1++;
    }
}