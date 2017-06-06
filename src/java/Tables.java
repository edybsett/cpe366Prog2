
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Represents the tables of Animals and Employees. This is mostly
 * just functions that return lists of both, but also includes values
 * for editing the tables.
 * @author Austin
 */
@ManagedBean
@SessionScoped
public class Tables implements Serializable {
    private List<Employee> allEmployees;
    private List<Animal> allAnimals;
    private DBConnect dbConnect = new DBConnect();
    private boolean showCats = true;
    private boolean showDogs = true;
    
    /**
     * Gets a list of all animals to be displayed where 
     * the specific type of animal to get is stored
     * @author Austin Sparks
     * @return list of all animals 
     */
    public List<Animal> getAnimals() throws SQLException{
        ArrayList<Animal> allAnimals = new ArrayList<Animal>();
        Connection con = Util.connect(dbConnect);
        
        String query = "SELECT * from Animal ";
        
        if (!showDogs) {
            query += "WHERE species != 'dog' ";
            
            if (!showCats)
                query += "AND species != 'cat' ";
        }
        
        else if (!showCats) {
            query += "WHERE species != 'cat' ";
            
            if (!showDogs)
                query += "AND species != 'dog'";
        }
        
        
        /* Execute query */
        System.out.println("Executing query: " + query);
        PreparedStatement ps = con.prepareStatement(query);
        /* Get results */
        ResultSet rs = ps.executeQuery();
        
        /* Get every result */
        while (rs.next()) {
            Animal a = new Animal();
            a.setId(rs.getInt("id"));
            a.setAgeYears(rs.getInt("ageYears"));
            a.setAgeMonths(rs.getInt("ageMonths"));
            a.setAgeWeeks(rs.getInt("ageWeeks"));
            a.setName(rs.getString("name"));
            a.setWeight(rs.getFloat("weight"));
            a.setSpecies(rs.getString("species"));
            a.setDescription(rs.getString("description"));
            a.setDateAdmitted(rs.getDate("dateAdmitted"));
            a.setColor(rs.getString("color"));
            a.setFoodType(rs.getString("foodType"));
            a.setEnergyLevel(rs.getInt("energyLevel"));
            a.setSex(rs.getString("sex"));
            a.setImage(rs.getBytes("image"));
            query = "SELECT type FROM Breed JOIN BreedXAnimal ON ";
            query += "Breed.id = breedId WHERE animalId = ?";
            PreparedStatement bps = con.prepareStatement(query);
            bps.setInt(1, a.getId());
            ResultSet breedSet = bps.executeQuery();

            while (breedSet.next()) {
                if (a.getBreeds().equals("Unknown"))
                    a.setBreeds(breedSet.getString("type"));
                else
                    a.setBreeds(a.getBreeds() + ", " + breedSet.getString("type"));
            }
            allAnimals.add(a);
        }
        con.commit();
        con.close();
        return allAnimals;
    }
    
     /**
     * Gets a list of all Employees in the Database
     */
    public List<Employee> getEmployees() throws SQLException {
        ArrayList<Employee> allEmps = new ArrayList<Employee>();
        Connection con = Util.connect(dbConnect);
        
        String query = "SELECT * from Login JOIN Employee ON Login.id=Employee.id";
        
        /* Execute query */
        PreparedStatement ps = con.prepareStatement(query);
        /* Get results */
        ResultSet rs = ps.executeQuery();
        
        /* Get every result */
        while (rs.next()) {
            Employee emp = new Employee();
            emp.setId(rs.getInt("id"));
            emp.setUsername(rs.getString("username"));
            emp.setFirstName(rs.getString("firstName"));
            emp.setLastName(rs.getString("lastName"));
            emp.setPassword(rs.getString("password"));
            emp.setRole(rs.getString("role"));
            emp.setWage(rs.getFloat("wage"));
            allEmps.add(emp);
        }
        con.commit();
        con.close();
        return allEmps;
    }
    
    
    public List<Employee> getAllEmployees() throws SQLException {
        if (allEmployees == null)
            allEmployees = getEmployees();
        return allEmployees;
    }
    
    public void updateAllEmployees() throws SQLException {
        Connection con = Util.connect(dbConnect);
        
        for (Employee e : allEmployees) {
            String query = "UPDATE Login ";
            query += "SET password=? ";
            query += "WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, e.getPassword());
            ps.setInt(2, e.getId());
            ps.executeUpdate();
        }
        
        con.commit();
        con.close();
    }
    
    
   
    
    /**
     * @return the showCats
     */
    public boolean isShowCats() {
        return showCats;
    }

    /**
     * @param showCats the showCats to set
     */
    public void setShowCats(boolean showCats) {
        this.showCats = showCats;
    }

    /**
     * @return the showDogs
     */
    public boolean isShowDogs() {
        return showDogs;
    }

    /**
     * @param showDogs the showDogs to set
     */
    public void setShowDogs(boolean showDogs) {
        this.showDogs = showDogs;
    }

    /**
     * @return the allAnimals
     */
    public List<Animal> getAllAnimals() throws SQLException {
        return getAnimals();
    }

    /**
     * @param allAnimals the allAnimals to set
     */
    public void setAllAnimals(List<Animal> allAnimals) {
        this.allAnimals = allAnimals;
    }
}
