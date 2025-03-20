package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=8386shop_new";
    private static final String USERNAME = "sa";  // Tên người dùng SQL Server (thường là 'sa')
    private static final String PASSWORD = "123456";  // Mật khẩu người dùng SQL Server
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  // Đảm bảo sử dụng driver cho SQL Server
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Kết nối thành công");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu", e);
        }
        return connection;
    }
}
