import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
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
@ViewScoped
@ManagedBean
public class Form implements Serializable {
    /* DB Connection */
    private DBConnect dbConnect = new DBConnect();
    /* Navigation result */
    private String navigation;
    private List<String> tagValues;
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
    private String animalTags;
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
    /* Hold form data */
    private String customerUsername;
    /* Class form data */
    private String newClassName;
    private int    classId;
    private int    classDay;
    private int    classTime;
    private int    classPrice;
    
    
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
        ps.setBinaryStream(12,is,is.available());
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
        
        /* Insert personality tags into table */
        for (String s : animalTags.split(",")) {
            addTag(s, con);
        }
        
        con.commit();
        con.close();
        return "refresh";
    }
    
    
    public String updateAnimal() throws SQLException, IOException {
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        String query;
        query = "UPDATE Animal set name = ?, ageYears = ?, ageMonths = ?, ageWeeks = ?, ";
        query += "description = ?, weight = ?, species = ?, color =?, foodType =?, energyLevel =?, ";
        query += "sex = ?, dateAdmitted = ? where id = ? ";
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
        ps.setDate(12, new Date(System.currentTimeMillis()));
        ps.setInt(13, animalId);
        ps.executeUpdate();
        ps.close();
        
        /* Remove this animal's personality */
        query = "DELETE FROM Personality WHERE animalId = ?;";
        ps = con.prepareStatement(query);
        ps.setInt(1, animalId);
        ps.executeUpdate();
        ps.close();
                
        /* Fill the personality table back up */
        /* Insert personality tags into table */
        for (String s : animalTags.split(",")) {
            addTag(s, con);
        }
        
        con.commit();
        con.close();
        return "refresh";
    }
    
    private void addTag(String tag, Connection con) throws SQLException {
        /* this query is gonna be wild. we want to add tags that 
           don't already exist but we don't want to overwrite 
           tags that already do.
        */
        String query = "with s as (select id from tag where description=?";
        query += "), i as (insert into tag (description) ";
        query += "select ? where not exists (select id from s) returning id) ";
        query += "insert into personality ";
        query += "select ?, id from i union all select ?, id from s";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, tag);
        ps.setString(2, tag);
        ps.setInt(3, animalId);
        ps.setInt(4, animalId);
        ps.executeUpdate();
        ps.close();
    }
    
    public void fillForm(Profile p) {
        this.animalId = p.getId();
        this.animalName = p.getName();
        this.animalSpecies = p.getSpecies();
        this.animalSex = p.getSex();
        this.animalWeight = p.getWeight();
        this.animalAgeY = p.getAgeYears();
        this.animalAgeM = p.getAgeMonths();
        this.animalAgeW = p.getAgeWeeks();
        this.animalColor = p.getColor();
        this.animalEnergyLevel = p.getEnergyLevel();
        this.animalFoodType = p.getFoodType();
        this.animalDesc = p.getDescription();
        this.animalBreeds = p.getBreeds();
        this.animalTags = p.getTags();
    }
    
    /**
     * 
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
        return "home";
    }
    
    public String addClass(int loginId) throws SQLException {
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        String query;
        query = "INSERT INTO Class(name, teacherId) ";
        query += "VALUES (?, ?)";
        ps = con.prepareStatement(query);
        ps.setString(1, getNewClassName());
        ps.setInt(2, loginId);
        ps.executeUpdate();
        
        con.commit();
        con.close();
        return "refresh";
    }
     
    public String addClassSession(int loginId) throws SQLException {
        Connection con = Util.connect(dbConnect);
        String query = "INSERT INTO ClassSession(classId, blockId, price) ";
        query       += "VALUES(?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, classId);
        ps.setInt(2, classDay + classTime);
        ps.setFloat(3, classPrice);
        ps.executeUpdate();
        con.commit();
        con.close();
        return "refresh";
        
    }
    
    public List<String> tagValues() throws SQLException {
        Connection con = Util.connect(dbConnect);
        String query = "SELECT description FROM Tag";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet tags = ps.executeQuery();
        ArrayList<String> tagV = new ArrayList<String>();
        while (tags.next())
            tagV.add(tags.getString("description"));
        tags.close();
        ps.close();
        con.commit();
        con.close();
        return tagV;
    }
    
    public List<String> breedValues() throws SQLException {
        Connection con = Util.connect(dbConnect);
        String query = "SELECT type FROM Breed";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet tags = ps.executeQuery();
        ArrayList<String> tagV = new ArrayList<String>();
        while (tags.next())
            tagV.add(tags.getString("type"));
        tags.close();
        ps.close();
        con.commit();
        con.close();
        return tagV;
        
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

    /**
     * @return the customerUsername
     */
    public String getCustomerUsername() {
        return customerUsername;
    }

    /**
     * @param customerUsername the customerUsername to set
     */
    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    /**
     * @return the newClassName
     */
    public String getNewClassName() {
        return newClassName;
    }

    /**
     * @param newClassName the newClassName to set
     */
    public void setNewClassName(String newClassName) {
        this.newClassName = newClassName;
    }

    /**
     * @return the classId
     */
    public int getClassId() {
        return classId;
    }

    /**
     * @param classId the classId to set
     */
    public void setClassId(int classId) {
        this.classId = classId;
    }

    /**
     * @return the classDay
     */
    public int getClassDay() {
        return classDay;
    }

    /**
     * @param classDay the classDay to set
     */
    public void setClassDay(int classDay) {
        this.classDay = classDay;
    }

    /**
     * @return the classTime
     */
    public int getClassTime() {
        return classTime;
    }

    /**
     * @param classTime the classTime to set
     */
    public void setClassTime(int classTime) {
        this.classTime = classTime;
    }

    /**
     * @return the classPrice
     */
    public int getClassPrice() {
        return classPrice;
    }

    /**
     * @param classPrice the classPrice to set
     */
    public void setClassPrice(int classPrice) {
        this.classPrice = classPrice;
    }

    /**
     * @return the animalTags
     */
    public String getAnimalTags() {
        return animalTags;
    }

    /**
     * @param animalTags the animalTags to set
     */
    public void setAnimalTags(String animalTags) {
        this.animalTags = animalTags;
    }
}
