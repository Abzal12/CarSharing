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
    private int companyNumber = 1;
    private boolean flag = false;

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
                if (id != companyNumber) {
                    companyNumber++;
                    flag = true;
                }
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
            ResultSet resultSet = statement.executeQuery(sql);

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
            String sql2 = String.format("INSERT INTO CAR (name, company_id) VALUES ('%s', %d)", name, companyId);
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

    public void autoIncrementToOne() {
        try {
            Statement statement = connection.createStatement();
            String sql = "ALTER TABLE CAR ALTER COLUMN id RESTART WITH 1";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
