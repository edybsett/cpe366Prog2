
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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;


/**
 * Represents an animal's profile to create the page
 * @author Austin Sparks (aasparks)
 */
@Named(value = "profile")
@SessionScoped
@ManagedBean
public class Profile implements Serializable{
    /* Inherited from Animal */
    private int    id;
    private int    ageYears;
    private int    ageMonths;
    private int    ageWeeks;
    private String name;
    private float  weight;
    private String species;
    private String description;
    private Date   dateAdmitted;
    private String color;
    private String foodType;
    private int    energyLevel;
    private String sex;
    private byte[] image;
    /* Need to be constructed from tables */
    private DBConnect dbConnect = new DBConnect();
    private String[] tags;
    private String breeds;
    private List<String> holds;
    /* Used for holds and adoptions */
    private String customerUsername;
    private int    customerId;
    private int    topHolder;
    private boolean removing;
    /* Need to bind the animal id */
    private UIInput profileUI;
    
    /**
     * Using the given animal information, finds the rest of
     * the information about the animal to construct its
     * full profile.
     * @param ani - Animal to create profile for
     * @return "profile". You should never need to go anywhere else
     */
    public String createProfile(Animal ani) throws SQLException {
        this.id           = ani.getId();
        this.ageYears     = ani.getAgeYears();
        this.ageMonths    = ani.getAgeMonths();
        this.ageWeeks     = ani.getAgeWeeks();
        this.name         = ani.getName();
        this.weight       = ani.getWeight();
        this.species      = ani.getSpecies();
        this.description  = ani.getDescription();
        this.dateAdmitted = ani.getDateAdmitted();
        this.color        = ani.getColor();
        this.foodType     = ani.getFoodType();
        this.energyLevel  = ani.getEnergyLevel();
        this.sex          = ani.getSex();
        this.breeds       = ani.getBreeds();
        this.image        = ani.getImage();
        constructHolds();
        profileUI = new UIInput();
        profileUI.setValue(id);
        return "profile";
    }
    
    private void constructHolds() throws SQLException {
        String query;
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        ResultSet rs;
        
        query  = "SELECT * FROM TempHold JOIN Login ";
        query += "ON customerId = id ";
        query += "WHERE animalId = ? ";
        query += "ORDER BY priority";
        ps = con.prepareStatement(query);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        
 
        holds = new ArrayList<String>();
        if (rs.next()) {
            do {
                String h = Integer.toString(rs.getInt("priority"));
                h += ". ";
                h += rs.getString("firstName");
                holds.add(h);
            } while(rs.next());
        } else
            holds.add("None");
        con.commit();
        con.close();
    }
    
    public void validateHold(FacesContext context, UIComponent component, Object value) throws ValidatorException, SQLException {
        Connection con   = Util.connect(dbConnect);
        id               = (int)getProfileUI().getLocalValue();
        customerUsername = (String)value;
        boolean customerExists = false;
        boolean customerHolds  = false;
        boolean tooManyHolds   = false;
        String query;
        
        /* Are there too many holds on the animal? */
        query = "SELECT max(priority) as holds ";
        query += "FROM TempHold WHERE animalId = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            topHolder = rs.getInt("holds");
        tooManyHolds = topHolder > 2;
        
        /* Does the customer exist? */
        query = "SELECT id FROM Login WHERE username = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, customerUsername);
        rs = ps.executeQuery();
        customerExists = rs.next();
        
        if (!customerExists) {
            rs.close();
            con.commit();
            con.close();
            FacesMessage m = new FacesMessage("Customer does not exist!");
            throw new ValidatorException(m);
        }
        customerId = rs.getInt("id");
        
        /* Does the customer already have a hold? */
        query = "SELECT customerId FROM TempHold WHERE customerId = ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, customerId);
        rs = ps.executeQuery();
        customerHolds = rs.next();
        
        /* Placing hold, but customer already has one */
        if (customerHolds && !removing) {
            con.commit();
            con.close();
            FacesMessage m = new FacesMessage(customerUsername + " aleady has a hold");
            throw new ValidatorException(m);
        }
        
        /* Customer trying to pale hold, but there are too many */
        if (tooManyHolds && !removing) {
            con.commit();
            con.close();
            FacesMessage m = new FacesMessage("Too many holds already");
            throw new ValidatorException(m);
        }
        
        /* Removing hold, customer does not hold */
        if (removing && !customerHolds) {
            con.commit();
            con.close();
            FacesMessage m = new FacesMessage(customerUsername + " does not have a hold on this animal");
            throw new ValidatorException(m);
        }
        
        con.commit();
        con.close();
    }
    
    public void validateAdopt(FacesContext context, UIComponent component, Object value) throws ValidatorException, SQLException {
        System.out.println("Validating removing: " + removing);
        Connection con   = Util.connect(dbConnect);
        id               = (int)getProfileUI().getLocalValue();
        customerUsername = (String)value;
        PreparedStatement ps;
        ResultSet rs;
        String query;

        /* Does the customer exist? */
        query = "SELECT id FROM Login WHERE username = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, customerUsername);
        rs = ps.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            con.commit();
            con.close();
            FacesMessage m = new FacesMessage("Customer does not exist!");
            throw new ValidatorException(m);
        }
        customerId = rs.getInt("id");
        
                
        /* Are there any holds on the animal other than the customer? */
        query = "SELECT customerId FROM TempHold ";
        query += "WHERE animalId = ? AND customerId != ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.setInt(2, customerId);
        rs = ps.executeQuery();
        if (rs.next()) {
            FacesMessage m = new FacesMessage("Animal on hold");
            throw new ValidatorException(m);
        }
        
        con.commit();
        con.close();       
    }
    
    public String buttonAction() throws SQLException {
        return removing ? removeHold() : placeHold();
    }
    
    /**
     * @author Austin Sparks
     * @return
     * @throws SQLException 
     */
    public String placeHold() throws SQLException {
        System.out.println("Placing hold...");
        String query;
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        query = "INSERT INTO TempHold ";
        query += "VALUES (?, ?, ?, ?)";
        ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.setInt(2, customerId);
        ps.setDate(3, new Date(System.currentTimeMillis()));
        ps.setInt(4, ++topHolder);
        ps.executeUpdate();
        con.commit();
        con.close();
        constructHolds();
        return "profile";
    }
    
    /**
     * @author Austin Sparks
     * @param p
     * @return
     * @throws SQLException 
     */
    public String removeHold() throws SQLException {
        System.out.println("Removing hold...");
        String query;
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        query = "DELETE FROM TempHold ";
        query += "WHERE animalId = ? AND customerId = ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.setInt(2, customerId);
        ps.executeUpdate();
        con.commit();
        con.close();
        constructHolds();
        return "profile";
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @return the ageYears
     */
    public int getAgeYears() {
        return ageYears;
    }
    
    /**
     * @param ageYears the ageYears to set
     */
    public void setAgeYears(int ageYears) {
        this.ageYears = ageYears;
    }
    
    /**
     * @return the ageMonths
     */
    public int getAgeMonths() {
        return ageMonths;
    }
    
    /**
     * @param ageMonths the ageMonths to set
     */
    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }
    
    /**
     * @return the ageWeeks
     */
    public int getAgeWeeks() {
        return ageWeeks;
    }
    
    /**
     * @param ageWeeks the ageWeeks to set
     */
    public void setAgeWeeks(int ageWeeks) {
        this.ageWeeks = ageWeeks;
    }
    
    /**
     * @return the image
     */
    public byte[] getImage() {
        return image;
    }
    
    /**
     * @param image the image to set
     */
    public void setImage(byte[] image) {
        this.setImage(image);
    }
    
    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }
    
    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    /**
     * @return the weight
     */
    public float getWeight() {
        return weight;
    }
    
    /**
     * @param weight the weight to set
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }
    
    /**
     * @return the tags
     */
    public String[] getTags() {
        return tags;
    }
    
    /**
     * @param tags the tags to set
     */
    public void setTags(String[] tags) {
        this.setTags(tags);
    }
    
    /**
     * @return the breeds
     */
    public String getBreeds() {
        return breeds;
    }
    
    /**
     * @param breeds the breeds to set
     */
    public void setBreeds(String breeds) {
        this.setBreeds(breeds);
    }
    
    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }
    
    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
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
     * @return the species
     */
    public String getSpecies() {
        return species;
    }
    
    /**
     * @param species the species to set
     */
    public void setSpecies(String species) {
        this.species = species;
    }
    
    /**
     * @return the dateAdmitted
     */
    public Date getDateAdmitted() {
        return dateAdmitted;
    }
    
    /**
     * @param dateAdmitted the dateAdmitted to set
     */
    public void setDateAdmitted(Date dateAdmitted) {
        this.dateAdmitted = dateAdmitted;
    }
    
    /**
     * @return the foodType
     */
    public String getFoodType() {
        return foodType;
    }
    
    /**
     * @param foodType the foodType to set
     */
    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
    
    /**
     * @return the energyLevel
     */
    public int getEnergyLevel() {
        return energyLevel;
    }
    
    /**
     * @param energyLevel the energyLevel to set
     */
    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
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
     * @return the profileUI
     */
    public UIInput getProfileUI() {
        return profileUI;
    }
    
    /**
     * @param profileUI the profileUI to set
     */
    public void setProfileUI(UIInput profileUI) {
        this.profileUI = profileUI;
    }
    
    /**
     * @return the holds
     */
    public List<String> getHolds() {
        return holds;
    }
    
    /**
     * @param holds the holds to set
     */
    public void setHolds(List<String> holds) {
        this.holds = holds;
    }

    /**
     * @return the removing
     */
    public boolean getRemoving() {
        return removing;
    }

    /**
     */
    public void setRemoving(boolean removing) {
        this.removing = removing;
    }
    
    /**
     */
    public void setNotRemoving() {
        this.removing = false;
    }
}
