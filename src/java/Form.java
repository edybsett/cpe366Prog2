import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
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
    private int    animalAgeW;
    private String animalDesc;
    private float  animalWeight;
    private String animalSpecies;
    private String animalColor;
    private String animalFoodType;
    private int    animalEnergyLevel;
    private String animalSex;
    private Part   animalImage;
    private int    animalId;
    private String animalBreeds;
    /* Login form data */
    private UIInput loginUI;
    @NotEmpty
    private String loginUsername;
    @NotEmpty
    private String loginPassword;
    private int    loginId;
    private String loginRole = "";
    /* Checkout form data */
    private String cardNumber;
    private int    cvc;
    private int    expirationM;
    private int    expirationY;
    
    
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
        query = "INSERT INTO Animal(name, ageYears, ageMonths, ageWeeks, ";
        query += "description, weight, species, color, foodType, energyLevel, ";
        query += "sex, image, dateAdmitted) ";
        query += "VALUES (?,?,?,?,?,?,?,?,?,?,?,?, ?) ";
        query += "RETURNING id";
        ps = con.prepareStatement(query);
        ps.setString(1, animalName);
        ps.setInt(2, animalAgeY);
        ps.setInt(3, animalAgeM);
        ps.setInt(4, animalAgeW);
        ps.setString(5, animalDesc);
        ps.setFloat(6, animalWeight);
        ps.setString(7,animalSpecies);
        ps.setString(8, animalColor);
        ps.setString(9, animalFoodType);
        ps.setInt(10, animalEnergyLevel);
        ps.setString(11, animalSex);
        InputStream is = animalImage.getInputStream();
        ps.setBinaryStream(12,is);
        ps.setDate(13, new Date(System.currentTimeMillis()));
        ResultSet rs = ps.executeQuery();
        /* Get the animal id */
        if (rs.next())
            animalId = rs.getInt("id");
        ps.close();
        rs.close();
        
        /* Insert breeds into table */
        for(String s : animalBreeds.split(",")) {
            addBreed(s, con);
        }
        
        con.commit();
        con.close();
        return "home";
    }
    
    /**
     * Adds the breed if it doesn't already exist
     * @param breed
     * @param con 
     */
    private void addBreed(String breed, Connection con) throws SQLException {
        int breedId = -1;
        /* Try to get breed id from table */
        String query = "SELECT id FROM Breed WHERE type=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, breed);
        ResultSet rs = ps.executeQuery();
        
        /* If breed doesn't exist */
        if (!rs.next()) {
            /* Add to table and get id */
            query = "INSERT INTO Breed(type) VALUES (?) RETURNING id";
            ps = con.prepareStatement(query);
            ps.setString(1, breed);
            rs = ps.executeQuery();
            rs.next();
 
        }
            
        /* Add id's to BreedXAnimal */
        breedId = rs.getInt("id");
        
        query = "INSERT INTO BreedXAnimal VALUES (?, ?)";
        ps = con.prepareStatement(query);
        ps.setInt(2, animalId);
        ps.setInt(1, breedId);
        ps.executeUpdate();
        ps.close();
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
    
    public String deleteAnimal (int id) throws SQLException, IOException {
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        String query;
        query = "DELETE FROM Animal where id = ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
        
        con.commit();
        con.close();
        return "index";
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
    public String getAnimalSpecies() {
        return animalSpecies;
    }

    /**
     * @param animalSpecies the animalType to set
     */
    public void setAnimalSpecies(String animalSpecies) {
        this.animalSpecies = animalSpecies;
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

    /**
     * @return the animalAgeW
     */
    public int getAnimalAgeW() {
        return animalAgeW;
    }

    /**
     * @param animalAgeW the animalAgeW to set
     */
    public void setAnimalAgeW(int animalAgeW) {
        this.animalAgeW = animalAgeW;
    }

    /**
     * @return the animalWeight
     */
    public float getAnimalWeight() {
        return animalWeight;
    }

    /**
     * @param animalWeight the animalWeight to set
     */
    public void setAnimalWeight(float animalWeight) {
        this.animalWeight = animalWeight;
    }

    /**
     * @return the animalColor
     */
    public String getAnimalColor() {
        return animalColor;
    }

    /**
     * @param animalColor the animalColor to set
     */
    public void setAnimalColor(String animalColor) {
        this.animalColor = animalColor;
    }

    /**
     * @return the animalFoodType
     */
    public String getAnimalFoodType() {
        return animalFoodType;
    }

    /**
     * @param animalFoodType the animalFoodType to set
     */
    public void setAnimalFoodType(String animalFoodType) {
        this.animalFoodType = animalFoodType;
    }

    /**
     * @return the energyLevel
     */
    public int getAnimalEnergyLevel() {
        return animalEnergyLevel;
    }

    /**
     * @param animalEnergyLevel the energyLevel to set
     */
    public void setAnimalEnergyLevel(int animalEnergyLevel) {
        this.animalEnergyLevel = animalEnergyLevel;
    }

    /**
     * @return the sex
     */
    public String getAnimalSex() {
        return animalSex;
    }

    /**
     * @param animalSex the sex to set
     */
    public void setAnimalSex(String animalSex) {
        this.animalSex = animalSex;
    }

    /**
     * @return the animalBreeds
     */
    public String getAnimalBreeds() {
        return animalBreeds;
    }

    /**
     * @param animalBreeds the animalBreeds to set
     */
    public void setAnimalBreeds(String animalBreeds) {
        this.animalBreeds = animalBreeds;
    }

    /**
     * @return the cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * @param cardNumber the cardNumber to set
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return the cvc
     */
    public int getCvc() {
        return cvc;
    }

    /**
     * @param cvc the cvc to set
     */
    public void setCvc(int cvc) {
        this.cvc = cvc;
    }

    /**
     * @return the expirationM
     */
    public int getExpirationM() {
        return expirationM;
    }

    /**
     * @param expirationM the expirationM to set
     */
    public void setExpirationM(int expirationM) {
        this.expirationM = expirationM;
    }

    /**
     * @return the expirationY
     */
    public int getExpirationY() {
        return expirationY;
    }

    /**
     * @param expirationY the expirationY to set
     */
    public void setExpirationY(int expirationY) {
        this.expirationY = expirationY;
    }
}
