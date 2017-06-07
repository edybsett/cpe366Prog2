import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Represents the animals
 * @author Austin Sparks (aasparks)
 */
@Named(value = "animal")
@SessionScoped
@ManagedBean
public class Animal implements Serializable {
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
    private String breeds = "Unknown";
    private byte[] image; 
    private String tags;
    private static final DBConnect dbConnect = new DBConnect();
    
    private void collectBreeds(Animal a, Connection con) throws SQLException {
        String query = "SELECT type FROM Breed JOIN BreedXAnimal ON ";
        query += "Breed.id = breedId WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, a.getId());
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            a.setBreeds(a.getBreeds() + ", " + rs.getString("type"));
        }
    }
    
    public static Animal getAnimalById(int id) throws SQLException {
        Animal ani = new Animal();
        Connection con = Util.connect(dbConnect);
        
        String query = "SELECT * from Animal ";
        query       += "WHERE id = ?";
        
        /* Execute query */
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        /* Get results */
        ResultSet rs = ps.executeQuery();
        
        /* Get every result */
        if (rs.next()) {
            ani.setId(rs.getInt("id"));
            ani.setAgeYears(rs.getInt("ageYears"));
            ani.setAgeMonths(rs.getInt("ageMonths"));
            ani.setAgeWeeks(rs.getInt("ageWeeks"));
            ani.setName(rs.getString("name"));
            ani.setWeight(rs.getFloat("weight"));
            ani.setSpecies(rs.getString("species"));
            ani.setDescription(rs.getString("description"));
            ani.setDateAdmitted(rs.getDate("dateAdmitted"));
            ani.setColor(rs.getString("color"));
            ani.setFoodType(rs.getString("foodType"));
            ani.setEnergyLevel(rs.getInt("energyLevel"));
            ani.setSex(rs.getString("sex"));
            ani.setImage(rs.getBytes("image"));
            query = "SELECT type FROM Breed JOIN BreedXAnimal ON ";
            query += "Breed.id = breedId WHERE animalId = ?";
            PreparedStatement bps = con.prepareStatement(query);
            bps.setInt(1, ani.getId());
            ResultSet breedSet = bps.executeQuery();

            while (breedSet.next()) {
                if (ani.getBreeds().equals("Unknown"))
                    ani.setBreeds(breedSet.getString("type"));
                else
                    ani.setBreeds(ani.getBreeds() + ", " + breedSet.getString("type"));
            }
            query = "SELECT description FROM Personality JOIN Tag ";
            query += "ON tagId = Tag.id WHERE animalId = ?";
            bps = con.prepareStatement(query);
            bps.setInt(1, ani.getId());
            ResultSet tagSet = bps.executeQuery();
            while (tagSet.next()) {
                ani.addTag(tagSet.getString("description"));
            }
        }
        con.commit();
        con.close();
        return ani;
    }
    
    public void addTag(String newTag) {
        if (this.tags == null || this.tags.isEmpty())
            this.tags = newTag;
        else
            this.tags += ", " + newTag;
    }
    
    public String getShortAge() {
        if (ageYears < 3)
            return "young";
        if (ageYears > 9)
            return "senior";
        return "adult";
    }
    
    public String getFirstBreed() {
        return breeds.split(",")[0];
    }
    
    public String onClick() {
        // Do stuff
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
     * @return the image
     */
    public byte[] getImage() {
        
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(byte[] image) {
        this.image = image;
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
     * @return the breeds
     */
    public String getBreeds() {
        return breeds;
    }

    /**
     * @param breeds the breeds to set
     */
    public void setBreeds(String breeds) {
        this.breeds = breeds;
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
    public void setTags(String tags) {
        this.tags = tags;
    }

}