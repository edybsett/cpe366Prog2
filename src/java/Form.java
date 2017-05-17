import java.io.IOException;
import java.io.InputStream;
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
import javax.inject.Named;
import javax.servlet.http.Part;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Used for any form data that may occur. May have
 * values that overlap with other forms.
 * @author Austin Sparks (aasparks)
 */
@Named(value = "form")
@SessionScoped
@ManagedBean
public class Form implements Serializable {
    /* DB Connection */
    private DBConnect dbConnect = new DBConnect();
    /* Navigation result */
    private String navigation;
    /* New animal form data */
    private String animalName;
    private int    animalAgeY;
    private int    animalAgeM;
    private String animalDesc;
    private String animalType;
    private Part   animalImage;
    private int    animalId;
    /* Login form data */
    private UIInput loginUI;
    @NotEmpty
    private String loginUsername;
    @NotEmpty
    private String loginPassword;
    private int    loginId;
    private String loginRole = "";
    
    
    /**
     * Store the new animal in the database
     * @author Austin Sparks
     * @return
     * @throws ValidatorException
     * @throws SQLException 
     */
    public String submitAnimal() throws SQLException, IOException {
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        String query;
        query = "INSERT INTO Animal(name, ageYears, ageMonths, description, ";
        query += "species, image) VALUES (?,?,?,?,?, ?)";
        ps = con.prepareStatement(query);
        ps.setString(1, animalName);
        ps.setInt(2, animalAgeY);
        ps.setInt(3, animalAgeM);
        ps.setString(4, animalDesc);
        ps.setString(5, animalType);
        InputStream is = animalImage.getInputStream();
        ps.setBinaryStream(6,is);
        ps.executeUpdate();
        
        con.commit();
        con.close();
        return "index";
    }
    
    /**
     * Validates user's login
     * @author Eric Dybsetter
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException
     * @throws SQLException 
     */
    public void loginValidate(FacesContext context, UIComponent component, Object value) throws ValidatorException, SQLException {
        Connection con = Util.connect(dbConnect);
        loginUsername = getLoginUI().getLocalValue().toString();
        loginPassword = value.toString();
        
        if (con == null) {
           throw new SQLException("Can't get database connection");
        }
        
        PreparedStatement ps
                    = con.prepareStatement(
                            "select role, id from Login where username=? and password=?");
        ps.setString(1, loginUsername);
        ps.setString(2, loginPassword);
        ResultSet result = ps.executeQuery();
        if (!result.next()) {
            setNavigation("login");
            FacesMessage errorMessage = new FacesMessage("Wrong login/password");
            throw new ValidatorException(errorMessage);
        }
        loginRole  = result.getString("role");
        loginId    = result.getInt("id");
        navigation = "home";
        result.close();
        con.commit();
        con.close();
    }
    
    
    /**
     * @return the animalName
     */
    public String getAnimalName() {
        return animalName;
    }

    /**
     * @param animalName the animalName to set
     */
    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    /**
     * @return the animalAgeY
     */
    public int getAnimalAgeY() {
        return animalAgeY;
    }

    /**
     * @param animalAgeY the animalAgeY to set
     */
    public void setAnimalAgeY(int animalAgeY) {
        this.animalAgeY = animalAgeY;
    }

    /**
     * @return the animalAgeM
     */
    public int getAnimalAgeM() {
        return animalAgeM;
    }

    /**
     * @param animalAgeM the animalAgeM to set
     */
    public void setAnimalAgeM(int animalAgeM) {
        this.animalAgeM = animalAgeM;
    }

    /**
     * @return the animalDesc
     */
    public String getAnimalDesc() {
        return animalDesc;
    }

    /**
     * @param animalDesc the animalDesc to set
     */
    public void setAnimalDesc(String animalDesc) {
        this.animalDesc = animalDesc;
    }

    /**
     * @return the animalType
     */
    public String getAnimalType() {
        return animalType;
    }

    /**
     * @param animalType the animalType to set
     */
    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    /**
     * @return the animalImage
     */
    public Part getAnimalImage() {
        return animalImage;
    }

    /**
     * @param animalImage the animalImage to set
     */
    public void setAnimalImage(Part animalImage) {
        this.animalImage = animalImage;
    }

    /**
     * @return the animalId
     */
    public int getAnimalId() {
        return animalId;
    }

    /**
     * @param animalId the animalId to set
     */
    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }
    
    /**
     * @return the loginUsername
     */
    public String getLoginUsername() {
        return loginUsername;
    }

    /**
     * @param loginUsername the loginUsername to set
     */
    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    /**
     * @return the loginPassword
     */
    public String getLoginPassword() {
        return loginPassword;
    }

    /**
     * @param loginPassword the loginPassword to set
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    /**
     * @return the role
     */
    public String getLoginRole() {
        return loginRole;
    }

    /**
     * @param loginRole the role to set
     */
    public void setLoginRole(String loginRole) {
        this.loginRole = loginRole;
    }

    /**
     * @return the navigation
     */
    public String getNavigation() {
        return navigation;
    }

    /**
     * @param navigation the navigation to set
     */
    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    /**
     * @return the loginId
     */
    public int getLoginId() {
        return loginId;
    }

    /**
     * @param loginId the loginId to set
     */
    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }
    
    /**
     * @return the loginUI
     */
    public UIInput getLoginUI() {
        return loginUI;
    }

    /**
     * @param loginUI the loginUI to set
     */
    public void setLoginUI(UIInput loginUI) {
        this.loginUI = loginUI;
    }
}
