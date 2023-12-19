package carsharing;

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
        companyDaoImp.deleteCarTable();
        companyDaoImp.deleteCompanyTable();
        companyDaoImp.createCompanyTable();
        companyDaoImp.createCarTable();

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
                            System.out.println("Choose a company:");
                            companyList.forEach(System.out::println);
                            System.out.println("0. Back");
                            System.out.println();
                            option = scanner.nextInt();
                            int optionForCompany = option;
                            while (option != 0) {
                                System.out.println("'" + companyDaoImp.chosenCompany(optionForCompany) + "' company");
                                thirdMenu();
                                option = scanner.nextInt();
                                while (option != 0) {
                                    if (option == 1) {
                                        List<Car> carList = companyDaoImp.selectCar(optionForCompany);
                                        if (carList.isEmpty()) {
                                            System.out.println("The car list is empty!");
                                        } else {
                                            System.out.println("Car list:");
                                            carList.forEach(System.out::println);
                                            System.out.println();
                                        }
                                    } else if (option == 2) {
                                        System.out.println("Enter the car name:");
                                        String name = scanner.nextLine();
                                        if (name.isEmpty()) {
                                            name = scanner.nextLine();
                                        }
                                        companyDaoImp.insertCar(name, optionForCompany);
                                        System.out.println("The car was added!");
                                    }
                                    thirdMenu();
                                    option = scanner.nextInt();
                                }
                            }
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

    private static void thirdMenu() {
        System.out.println("1. Car list\n" +
                "2. Create a car\n" +
                "0. Back");
    }
}