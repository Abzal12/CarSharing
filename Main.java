package carsharing;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static PreparedStatement preparedStatement;
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String url = "jdbc:h2:./src/carsharing/db/carsharing";
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection(url);
        System.out.println(connection.isValid(0));
        connection.setAutoCommit(true);

//        String sqlStatement2 = "DROP TABLE COMPANY";
//        preparedStatement = connection.prepareStatement(sqlStatement2);
//        preparedStatement.execute();
        String sqlStatement = """
                CREATE TABLE IF NOT EXISTS COMPANY (
                    ID INT AUTO_INCREMENT PRIMARY KEY,
                    NAME VARCHAR(100) UNIQUE NOT NULL
                )
                """;
        preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.execute();

        printMenu(connection);

        preparedStatement.close();
        connection.close();
    }

    private static void printMenu(Connection connection) throws SQLException {
        System.out.println("""
                1. Log in as a manager
                0. Exit
                """);

        String menuChoice;
        String afterLogInMenuChoice;
        menuChoice= scanner.nextLine();
        boolean flag = true;
        while (flag) {
            switch (menuChoice) {
                case "0" -> {
                    return;
                }
                case "1" -> {

                    while (flag){
                        printAfterLogInMenu();
                        afterLogInMenuChoice = scanner.nextLine();
                        switch (afterLogInMenuChoice) {
                            case "1" -> showCompanyList(connection);
                            case "2" -> createCompanyList(connection);
                            case "0" -> {
                                printMenu(connection);
                                flag = false;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void printAfterLogInMenu() {
        System.out.println("""
                1. Company list
                2. Create a company
                0. Back
                """);
    }

    private static void showCompanyList(Connection connection) throws SQLException {
        System.out.println("Company list:");
        String selectAllCompanies = """
                SELECT * FROM COMPANY""";
//        statement.execute(selectAllCompanies);
        preparedStatement = connection.prepareStatement(selectAllCompanies);
        preparedStatement.execute();
        ResultSet companyList = preparedStatement.getResultSet();
        if (!companyList.next()) {
            System.out.println("The company list is empty!");
        } else {
            do {
                System.out.println(companyList.getInt("ID") + ". "
                        + companyList.getString("NAME"));
            } while (companyList.next());
        }
        System.out.println();
    }

    private static void createCompanyList(Connection connection) throws SQLException {
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        String insertCompanySql = "INSERT INTO COMPANY (NAME) VALUES (?)";
        preparedStatement = connection.prepareStatement(insertCompanySql);
        preparedStatement.setString(1, companyName);
        preparedStatement.executeUpdate();
        System.out.println("The company was created!\n");
    }
}