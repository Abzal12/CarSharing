package carsharing;

import carsharing.CompanyDaoImp;
import carsharing.Database;
import carsharing.Company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String databaseFileName = "carsharing";
        if (args.length > 0 && args[0].equals("-databaseFileName")) {
            databaseFileName = args[1];
        }
        Database databaseCarsharing = new Database(databaseFileName);
        CompanyDaoImp companyDaoImp = new CompanyDaoImp(databaseCarsharing);
        companyDaoImp.deleteTable();
        companyDaoImp.createTable();


        Scanner scanner = new Scanner(System.in);
        startMenu();
        int option = scanner.nextInt();
        while (option != 0) {
            if (option == 1) {
                secondMenu();
                option = scanner.nextInt();
                while (option != 0) {
                    if (option == 1) {
                        List<Company> companyList = companyDaoImp.selectCompany();
                        if (companyList.isEmpty()) {
                            System.out.println("The company list is empty!");
                        } else {
                            System.out.println("Company list:");
                            companyList.forEach(System.out::println);
                            System.out.println();
                        }
                    } else if (option == 2) {
                        System.out.println("Enter the company name:");
                        String name = scanner.nextLine();
                        if (name.isEmpty()) {
                            name = scanner.nextLine();
                        }
                        companyDaoImp.insertCompany(name);
                        System.out.println("The company was created!");
                    }
                    secondMenu();
                    option = scanner.nextInt();
                }
            }
            startMenu();
            option = scanner.nextInt();
        }

        databaseCarsharing.closeConnection();
    }

    public static void startMenu() {
        System.out.println("1. Log in as a manager\n" +
                "0. Exit");
    }

    private static void secondMenu() {
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
    }
}