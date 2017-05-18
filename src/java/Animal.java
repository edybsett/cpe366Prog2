import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.util.List;
import java.util.ArrayList;
import java.sql.Date;

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
    private DBConnect dbConnect = new DBConnect();
    
    /**
     * Gets a list of all animals to be displayed where 
     * the specific type of animal to get is stored
     * @author Austin Sparks
     * @return list of all animals 
     */
    public List<Animal> getAllAnimals() throws SQLException{
        ArrayList<Animal> allAnimals = new ArrayList<Animal>();
        Connection con = Util.connect(dbConnect);
        
        String query = "SELECT * from Animal";
        
        /* Execute query */
        PreparedStatement ps = con.prepareStatement(query);
        /* Get results */
        ResultSet rs = ps.executeQuery();
        
        /* Get every result */
        while (rs.next()) {
            Animal a = new Animal();
            a.setId(rs.getInt("id"));
            a.setAgeYears(rs.getInt("ageYears"));
            a.setAgeMonths(rs.getInt("ageMonths"));
            a.setAgeWeeks(rs.getInt("ageWeeks"));
            a.setName(rs.getString("name"));
            a.setWeight(rs.getFloat("weight"));
            a.setSpecies(rs.getString("species"));
            a.setDescription(rs.getString("description"));
            a.setDateAdmitted(rs.getDate("dateAdmitted"));
            a.setColor(rs.getString("color"));
            a.setFoodType(rs.getString("foodType"));
            a.setEnergyLevel(rs.getInt("energyLevel"));
            a.setSex(rs.getString("sex"));
            a.setImage(rs.getBytes("image"));
            query = "SELECT type FROM Breed JOIN BreedXAnimal ON ";
            query += "Breed.id = breedId WHERE animalId = ?";
            PreparedStatement bps = con.prepareStatement(query);
            bps.setInt(1, a.getId());
            ResultSet breedSet = bps.executeQuery();

            while (breedSet.next()) {
                if (a.getBreeds().equals("Unknown"))
                    a.setBreeds(breedSet.getString("type"));
                else
                    a.setBreeds(a.getBreeds() + ", " + breedSet.getString("type"));
            }
            allAnimals.add(a);
        }
        con.commit();
        con.close();
        return allAnimals;
    }
    
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
}