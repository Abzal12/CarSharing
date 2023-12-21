package carsharing;

import java.util.ArrayList;
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
        //companyDaoImp.deleteCustomerTable();
        //companyDaoImp.deleteCarTable();
        //companyDaoImp.deleteCompanyTable();
        companyDaoImp.createCompanyTable();
        companyDaoImp.createCarTable();
        companyDaoImp.createCustomerTable();

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

            } else if (option == 2) {
                List<Customer> customerList = companyDaoImp.selectCustomer();
                if (customerList.isEmpty()) {
                    System.out.println("The customer list is empty!");
                } else {
                    System.out.println("Customer list:");
                    customerList.forEach(System.out::println);
                    System.out.println("0. Back");
                    System.out.println();
                    option = scanner.nextInt();
                    int customerOption = option;
                    while (option != 0) {
                        rentCarMenu();
                        option = scanner.nextInt();
                        while (option != 0) {
                            if (option == 1) {
                                boolean hasAlreadyRented = companyDaoImp.hasAlreadyRented(customerOption);
                                if (hasAlreadyRented) {
                                    System.out.println("You've already rented a car!");
                                } else {
                                    List<Company> companyList = companyDaoImp.selectCompany();
                                    if (companyList.isEmpty()) {
                                        System.out.println("The company list is empty!");
                                    } else {
                                        System.out.println("Choose a company:");
                                        companyList.forEach(System.out::println);
                                        System.out.println("0. Back");
                                        System.out.println();
                                        option = scanner.nextInt();
                                        int companyOption = option;
                                        while (option != 0) {

                                            List<Car> carList = companyDaoImp.selectCar(option);
                                            if (carList.isEmpty()) {
                                                System.out.println("The car list is empty!");
                                            } else {
                                                System.out.println("Choose a car:");
                                                carList.forEach(System.out::println);
                                                System.out.println("0. Back");
                                                System.out.println();
                                                option = scanner.nextInt();
                                                if (option != 0) {
                                                    int carOption = option;
                                                    String chosenCar = companyDaoImp.rentedCar(companyOption, carOption).getName();
                                                    System.out.printf("You rented '%s'\n", chosenCar);
                                                    companyDaoImp.insertRentedCarToCustomerTable(carOption, customerOption);
                                                    option = 0;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (option == 2) {
                                boolean hasReturn = companyDaoImp.returnCar(customerOption);

                                if (!hasReturn) {
                                    System.out.println("You didn't rent a car!");

                                } else {
                                    System.out.println("You've returned a rented car!");

                                }

                            } else if (option == 3) {
                                String[] carAndCompany = companyDaoImp.findRentedCarNameThroughCustomerId(customerOption);
                                if (carAndCompany[0]==null) {
                                    System.out.println("You didn't rent a car!");
                                } else {
                                    System.out.println("Your rented car:");
                                    String rentedCarName = carAndCompany[0];
                                    System.out.println(rentedCarName);
                                    System.out.println("Company:");
                                    String companyName = carAndCompany[1];
                                    System.out.println(companyName);
                                }

                            }
                            rentCarMenu();
                            option = scanner.nextInt();
                        }
                    }
                }

            } else if (option == 3) {
                System.out.println("Enter the customer name:");
                scanner.nextLine();
                String customerName = scanner.nextLine();
                companyDaoImp.addCustomer(customerName);
                System.out.println("The customer was added!");

            }
            startMenu();
            option = scanner.nextInt();
        }
        databaseCarsharing.closeConnection();
    }

    public static void startMenu() {
        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
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
    private static void rentCarMenu() {
        System.out.println("1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
    }
}