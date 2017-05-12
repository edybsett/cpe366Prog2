
import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

@ManagedBean
@RequestScoped
public class LoginBean implements Serializable {
    private UIInput loginUI;
    
    @NotEmpty
    private String username;
    
    @NotEmpty
    private String password;
 
    private String navigation;
    
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
 
    public void loginValidate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        username = loginUI.getLocalValue().toString();
        password = value.toString();
        navigation = "";
        if ("admin".equalsIgnoreCase(username) && "hi".equalsIgnoreCase(password)) {           
            navigation = "home";
        } else {
            navigation = "login";
            FacesMessage errorMessage = new FacesMessage("Wrong login/password");
            throw new ValidatorException(errorMessage);   
        }
    }
 
    
}
