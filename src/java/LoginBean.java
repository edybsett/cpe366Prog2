
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
    private UIInput loginUI;
    /* DB Connection */
    private DBConnect dbConnect = new DBConnect();
    
    @NotEmpty
    private String username;
    
    @NotEmpty
    private String password;
 
    private String navigation;
    
    private boolean loggedIn;
    
    public UIInput getLoginUI() {
        return loginUI;
    }

    public void setLoginUI(UIInput loginUI) {
        this.loginUI = loginUI;
    }
 
    public void setUsername(String name) {
        this.username = name;
    }
 
    public String getUsername() {
        return username;
    }
 
 
    public String getPassword() {
        return password;
    }
 
 
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNav() {
        return navigation;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    public String logout() {
        Util.invalidateUserSession();
        return "home";
    }
 
    public void loginValidate(FacesContext context, UIComponent component, Object value) throws ValidatorException, SQLException {
        Connection con = Util.connect(dbConnect);
        username = loginUI.getLocalValue().toString();
        password = value.toString();
        navigation = "";
        
        if (con == null) {
           throw new SQLException("Can't get database connection");
        }
        
        PreparedStatement ps
                    = con.prepareStatement(
                            "select role, id from Login where username=? and password=?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet result = ps.executeQuery();
        if (!result.next()) {
            navigation = "login";
            FacesMessage errorMessage = new FacesMessage("Wrong login/password");
            throw new ValidatorException(errorMessage);
        }
        String getTitle = result.getString("role");
        loggedIn = true;
        navigation = "home";
        result.close();
        con.close();
        /*
        if ("admin".equalsIgnoreCase(username) && "hi".equalsIgnoreCase(password)) {           
            navigation = "home";
        } else {
            navigation = "login";
            FacesMessage errorMessage = new FacesMessage("Wrong login/password");
            throw new ValidatorException(errorMessage);   
        }
*/
    }
 
    
}
