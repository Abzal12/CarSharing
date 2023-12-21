package carsharing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImp implements CompanyDao{

    private Database dataBaseCarSharing;
    private Connection connection;

    public CompanyDaoImp(Database dataBaseCarSharing) {
        this.dataBaseCarSharing = dataBaseCarSharing;
        this.connection = dataBaseCarSharing.getConnection();
    }
    @Override
    public void createCompanyTable() {

        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(id INTEGER AUTO_INCREMENT, " +
                    " name VARCHAR(255) UNIQUE NOT NULL," +
                    "PRIMARY KEY (id))";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertCompany(String nameCompany) {

        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO COMPANY (name) VALUES ('" + nameCompany + "')";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Company> selectCompany() {
        List<Company> companies = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM COMPANY";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                companies.add(new Company(id, name));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public void deleteCompanyTable() {

        try {
            Statement statement = connection.createStatement();
            String sql = "DROP TABLE IF EXISTS COMPANY";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String chosenCompany(int id) {
        String name = null;
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM COMPANY WHERE ID = " + id;
            ResultSet resultSet = statement.executeQuery(sql);

            resultSet.next();
            name = resultSet.getString("name");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public void createCarTable() {
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS CAR " +
                    "(id INTEGER AUTO_INCREMENT, " +
                    " name VARCHAR(255) UNIQUE NOT NULL," +
                    "company_id INTEGER NOT NULL," +
                    "FOREIGN KEY (company_id) REFERENCES COMPANY (id)," +
                    "PRIMARY KEY (id))";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Car> selectCar(int optionForCompany) {
        List<Car> cars = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM CAR WHERE company_id = %d", optionForCompany);
            String sql2 = "SELECT * FROM CAR LEFT JOIN CUSTOMER ON car.id = customer.rented_car_id WHERE customer.name IS NULL;";
            String sql3 = String.format("SELECT * FROM CAR WHERE COMPANY_ID = %d AND ID NOT IN (SELECT RENTED_CAR_ID FROM CUSTOMER WHERE RENTED_CAR_ID IS NOT NULL)", optionForCompany);
            ResultSet resultSet = statement.executeQuery(sql3);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                cars.add(new Car(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < cars.size(); i++) {
            cars.get(i).setId(i + 1);
        }
        return cars;
    }

    @Override
    public void insertCar(String name, int companyId) {
        try {
            Statement statement = connection.createStatement();
            String sql2 = String.format("INSERT INTO CAR (name, company_id) " +
                    "VALUES ('%s', %d)", name, companyId);
            statement.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCarTable() {

        try {
            Statement statement = connection.createStatement();
            String sql = "DROP TABLE IF EXISTS CAR";
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void createCustomerTable() {
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                    "(id INTEGER AUTO_INCREMENT, " +
                    "name VARCHAR(255) UNIQUE NOT NULL, " +
                    "rented_car_id INT DEFAULT NULL, " +
                    "FOREIGN KEY (rented_car_id) REFERENCES CAR (id), " +
                    "PRIMARY KEY (id))";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomerTable() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DROP TABLE IF EXISTS CUSTOMER";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(String customerName) {
        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO CUSTOMER (name)" +
                    "VALUES ('" + customerName + "')";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> selectCustomer() {
        List<Customer> customerList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM CUSTOMER";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                customerList.add(new Customer(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    public Car rentedCar(int companyOption, int carOption) {
        Car car1 = null;
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM CAR WHERE company_id = %d AND id = %d", companyOption, carOption);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int id = resultSet.getInt("id");
                car1 = new Car(id, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car1;
    }

    public void insertRentedCarToCustomerTable(int carId, int customerOption) {
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("UPDATE CUSTOMER SET rented_car_id = %d WHERE id = %d", carId, customerOption);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] findRentedCarNameThroughCustomerId(int customerId) {
        String[] carAndCompany = new String[2];
        String name = null;
        String companyName = null;
        int rentedCarId = 0;
        int companyId = 0;
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM CUSTOMER WHERE id = %d", customerId);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                rentedCarId = resultSet.getInt("rented_car_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM CAR WHERE id = %d", rentedCarId);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                name = resultSet.getString("name");
                companyId = resultSet.getInt("company_id");
            }
            carAndCompany[0] = name;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM COMPANY WHERE id = %d", companyId);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                companyName = resultSet.getString("name");
            }
            carAndCompany[1] = companyName;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carAndCompany;
    }

    public boolean returnCar(int customerOption) {
        int rentedCarId = 0;
        boolean hasRented = false;
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM CUSTOMER WHERE id = %d", customerOption);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                rentedCarId = resultSet.getInt("rented_car_id");
            }
            if (rentedCarId != 0) {
                hasRented = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (hasRented) {
            try {
                Statement statement = connection.createStatement();
                String sql = String.format("UPDATE CUSTOMER SET rented_car_id = NULL WHERE id = %d", customerOption);
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return hasRented;
    }

    public boolean hasAlreadyRented(int customerOption) {
        int rentedCarId = 0;
        boolean hasRented = false;
        try {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM CUSTOMER WHERE id = %d", customerOption);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                rentedCarId = resultSet.getInt("rented_car_id");
            }
            System.out.println("##rentedCarId = " + rentedCarId);
            if (rentedCarId != 0) {
                hasRented = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("##hasRented = " + hasRented);
        return hasRented;
    }
    //Когда делаем рент на авто, это авто уже не должен отображаться в выборке для нового клиента
    // You may add columns in a table to track which cars are rented.
 }
