package carsharing;

import java.util.List;

public interface CompanyDao {
    public void createCompanyTable();
    public void insertCompany(String name);
    public List<Company> selectCompany();
    void deleteCompanyTable();
    public void createCarTable();
    public String chosenCompany(int id);
    public List<Car> selectCar(int option);
    public void insertCar(String name, int companyId);
    public void deleteCarTable();
    public void createCustomerTable();
}
