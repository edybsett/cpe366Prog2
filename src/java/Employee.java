import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

/**
 * Represents a single employee
 * @author Austin Sparks (aasparks)
 */
@Named(value = "employee")
@SessionScoped
@ManagedBean
public class Employee implements Serializable{
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private float  wage;
    private DBConnect dbConnect = new DBConnect();
    
    
    /**
     * Gets a list of all Employees in the Database
     */
    public List<Employee> getEmployees() throws SQLException {
         ArrayList<Employee> allEmps = new ArrayList<Employee>();
        Connection con = Util.connect(dbConnect);
        
        String query = "SELECT * from Login JOIN Employee ON Login.id=Employee.id";
        
        /* Execute query */
        PreparedStatement ps = con.prepareStatement(query);;
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
   
    
    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param id the id to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String toString(){
        return id + ": " + firstName;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the wage
     */
    public float getWage() {
        return wage;
    }

    /**
     * @param wage the wage to set
     */
    public void setWage(float wage) {
        this.wage = wage;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
