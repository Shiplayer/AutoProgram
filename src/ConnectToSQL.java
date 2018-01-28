import java.sql.*;

public class ConnectToSQL {

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://192.168.0.13/demo_base1", "Buhgalter", "12345");
        System.out.println("hello");
    }
}
