package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String url = "jdbc:h2:./src/carsharing/db/carsharing";
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection(url);
        System.out.println(connection.isValid(0));
        connection.setAutoCommit(true);
        Statement statement = connection.createStatement();
        String sqlStatement = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                "ID INT," +
                "NAME VARCHAR" +
                ")";
        String sqlStatement2 = "DROP TABLE COMPANY";
        statement.execute(sqlStatement);

        statement.close();
        connection.close();
    }
}