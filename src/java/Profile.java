import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
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
    private String tags;
    private String breeds;
    private List<String> holds;
    /* Conditions are going to be stored in separate lists
       by type because they are needed in groups by type
    */
    private List<MedicalInfo> allergies;
    private List<MedicalInfo> conditions;
    private String            spay;
    private List<MedicalInfo> surgeries;
   
    
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
        this.tags         = ani.getTags();
        constructHolds();
        constructConditions();
        return "profile";
    }
    
    /** When profiles are edited on their page, they need
      * be completely reconstructed from the database.
      * @author Austin Sparks 
      */
    public void recreateProfile() throws SQLException {
        /* So what we want to do here is use the current animal id
           to get the animal information from the db
        */
        createProfile(Animal.getAnimalById(this.id));
    }
    
    public void constructHolds() throws SQLException {
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
                h += " " + rs.getString("lastName");
                h += " (";
                h += rs.getString("username");
                h += ")";
                holds.add(h);
            } while(rs.next());
        } else
            holds.add("None");
        con.commit();
        con.close();
    }
    
    public void constructConditions() throws SQLException {
        String query;
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        ResultSet rs;
        String binaryGenderRepresentationSJWCringe = this.sex.equals("male")? "neutered" : "spayed";
        
        
        query  = "SELECT * FROM MedicalInfo ";
        query += "WHERE animalId = ? ";
        query += "ORDER BY type";
        ps = con.prepareStatement(query);
        ps.setInt(1, id);
        rs = ps.executeQuery();
        
        setConditions(new ArrayList<MedicalInfo>());
        setAllergies(new ArrayList<MedicalInfo>());
        setSurgeries(new ArrayList<MedicalInfo>());
        this.spay = null;
        
        /* Add in all medical conditions */
        while (rs.next()) {
            MedicalInfo m = new MedicalInfo();
            m.setName(rs.getString("name"));
            m.setDescription(rs.getString("description"));
            m.setType(rs.getString("type"));
            m.setAction(rs.getString("action"));
            
            switch (m.getType()) {
                case "spay":
                    spay = binaryGenderRepresentationSJWCringe;
                    break;
                case "allergy":
                    allergies.add(m);
                    break;
                case "condition":
                    conditions.add(m);
                    break;
                case "surgery":
                    surgeries.add(m);
                    break;
                default:
                    throw new SQLException();
            }
        }
        
        /* If any are empty, give them a blank info, which
           says 'none'
        */
        if (spay == null || this.spay.isEmpty())
            spay = "not " + binaryGenderRepresentationSJWCringe;
        if (allergies.isEmpty()) {
            MedicalInfo m = new MedicalInfo();
            m.setName("None");
            m.setDescription(this.name + " has no allergies!");
            allergies.add(m);
        }
        if (surgeries.isEmpty()) {
            MedicalInfo m = new MedicalInfo();
            m.setName("None");
            m.setDescription(this.name + " has had no surgeries!");
            surgeries.add(m);
        }
        if (conditions.isEmpty()) {
            MedicalInfo m = new MedicalInfo();
            m.setName("None");
            m.setDescription(this.name + " has no known conditions!");
            conditions.add(m);
        }
        con.commit();
        con.close();
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
    public String getTags() {
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
     * @return the holds
     */
    public List<String> getHolds() throws SQLException {
        return holds;
    }
    
    /**
     * @param holds the holds to set
     */
    public void setHolds(List<String> holds) {
        this.holds = holds;
    }

    /**
     * @return the conditions
     */
    public List<MedicalInfo> getConditions() throws SQLException {
        constructConditions();
        return conditions;
    }

    /**
     * @param conditions the conditions to set
     */
    public void setConditions(List<MedicalInfo> conditions) {
        this.conditions = conditions;
    }

    /**
     * @return the allergies
     */
    public List<MedicalInfo> getAllergies() throws SQLException {
        return allergies;
    }

    /**
     * @param allergies the allergies to set
     */
    public void setAllergies(List<MedicalInfo> allergies) {
        this.allergies = allergies;
    }

    /**
     * @return the spay
     */
    public String getSpay() throws SQLException {
        return spay;
    }

    /**
     * @param spay the spay to set
     */
    public void setSpay(String spay) {
        this.spay = spay;
    }

    /**
     * @return the surgeries
     */
    public List<MedicalInfo> getSurgeries() throws SQLException {
        return surgeries;
    }

    /**
     * @param surgeries the surgeries to set
     */
    public void setSurgeries(List<MedicalInfo> surgeries) {
        this.surgeries = surgeries;
    }
    
    public String spaySeverity(){
        if ("spayed".equals(spay) || "neutered".equals(spay))
            return "success";
        else return "danger";
    }
}
