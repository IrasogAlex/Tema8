package Exercitiul1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/lab8";
    private static final String USER = "root"; // Utilizatorul MySQL
    private static final String PASSWORD = "root"; // Parola MySQL

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Nu s-a putut conecta la baza de date.", e);
        }
    }
}
