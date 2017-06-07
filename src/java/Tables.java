
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
            
            query = "SELECT description FROM Personality JOIN Tag ";
            query += "ON tagId = Tag.id WHERE animalId = ?";
            bps = con.prepareStatement(query);
            bps.setInt(1, a.getId());
            ResultSet tagSet = bps.executeQuery();
            while (tagSet.next()) {
                a.addTag(tagSet.getString("description"));
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
    
    public List<PetClass> getAllClasses() throws SQLException {
        Connection con = Util.connect(dbConnect);
        List<PetClass> allClasses = new ArrayList<PetClass>();
        String query = "SELECT classId, startTime, endTime, name, price, ";
        query       += "ClassSession.id as sid, blockId, firstName, lastName ";
        query       += "FROM Class, ClassTimes, ClassSession, Login ";
        query       += "WHERE class.teacherId = Login.id ";
        query       += "AND ClassSession.blockId = ClassTimes.id ";
        query       += "AND ClassSession.classId = Class.id";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet result = ps.executeQuery();
        String[] days = new String[] {"M", "T", "W", "R", "F", "Sa", "Su"};

     
        while (result.next()) {
            PetClass pet = new PetClass();
            pet.setClassId(result.getInt("classId"));
            pet.setStartTime(result.getTime("startTime").toString());
            pet.setEndTime(result.getTime("endTime").toString());
            pet.setClassName(result.getString("name"));
            pet.setPrice(result.getFloat("price"));
            pet.setSessionId(result.getInt("sid"));
            pet.setUsername(result.getString("firstName") + " " + result.getString("lastName"));
            String day = days[(result.getInt("blockId") / 100) - 1];
            pet.setDay(day);
            allClasses.add(pet);
        }
        result.close();
        con.close();
        return allClasses;
    }
    
    public List<PetClass> showUserClasses(int loginId) throws SQLException {
        Connection con = Util.connect(dbConnect);
        List<PetClass> classes = new ArrayList<PetClass>();
        String query = "SELECT * FROM Class ";
        query       += "WHERE teacherId = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, loginId);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            PetClass temp = new PetClass();
            temp.setClassId(result.getInt("id"));
            temp.setClassName(result.getString("name"));
            classes.add(temp);
        }
        result.close();
        con.commit();
        con.close();
        return classes;
    }
    
    public List<PetClass> showAllClasses() throws SQLException {
        Connection con = Util.connect(dbConnect);
        List<PetClass> classes = new ArrayList<PetClass>();
        String query = "SELECT * FROM Class ";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            PetClass temp = new PetClass();
            temp.setClassId(result.getInt("id"));
            temp.setClassName(result.getString("name"));
            classes.add(temp);
        }
        result.close();
        con.commit();
        con.close();
        return classes;
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
