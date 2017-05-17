
import java.io.Serializable;
import java.sql.Date;
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
    private String[] tags;
    private String[] breeds;
    
    /**
     * Using the given animal information, finds the rest of 
     * the information about the animal to construct its 
     * full profile.
     * @param ani - Animal to create profile for
     * @return "profile". You should never need to go anywhere else
     */
    public String createProfile(Animal ani) {
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
        this.image        = ani.getImage();
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
    public String[] getBreeds() {
        return breeds;
    }

    /**
     * @param breeds the breeds to set
     */
    public void setBreeds(String[] breeds) {
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
}
