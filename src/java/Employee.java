import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private boolean editing;
    private DBConnect dbConnect = new DBConnect();
   
    /**
     * Allows editing of this employee's password
     */
    public void edit() throws SQLException {
        this.editing = !this.editing;
        
        if (!this.editing) {
            updateMe();
        }
    }
    
    private void updateMe() throws SQLException {
        Connection con = Util.connect(dbConnect);
        String query = "UPDATE Login ";
        query += "SET password=? ";
        query += "WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, this.password);
        ps.setInt(2, this.id);
        ps.executeUpdate();
        con.commit();
        con.close();
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

    /**
     * @return the editing
     */
    public boolean isEditing() {
        return editing;
    }

    /**
     * @param editing the editing to set
     */
    public void setEditing(boolean editing) {
        this.editing = editing;
    }
}
