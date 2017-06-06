import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Login implements Serializable {
    private int    id;
    private String username;
    private String password;
    private String role = "";
    private boolean loggedIn;
    
    public Login() {}
    
    public String createLogin(Form f) {
        this.id       = f.getLoginId();
        this.username = f.getLoginUsername();
        this.password = f.getLoginPassword();
        this.role     = f.getLoginRole();
        loggedIn      = true;
        return "home";
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    public String logout() {
        loggedIn = false;
        role = username = password = "";
        id = 0;
        Util.invalidateUserSession();
        return "home";
    }
    
    /**
     * Checks if the logged in user is a manager.
     * @return true for manager
     */
    public boolean isManager() {
        return role.equals("manager");
    }
    
    /**
     * Checks if the logged in user is an employee.
     * @return true for employee
     */
    public boolean isEmployee() {
        return role.equals("manager") || role.equals("employee");
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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
    
 
    
}
