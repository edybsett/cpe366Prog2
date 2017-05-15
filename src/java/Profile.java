
import java.io.Serializable;
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
    private String   name;
    private String   description;
    private int      ageYears;
    private int      ageMonths;
    private int      ageWeeks;
    private byte[]   image;
    private String[] breed;
    private String   sex;
    private float    weight;
    private String[] tags;
    
    /**
     * Using the given animal information, finds the rest of 
     * the information about the animal to construct its 
     * full profile.
     * @param ani - Animal to create profile for
     * @return "profile". You should never need to go anywhere else
     */
    public String createProfile(Animal ani) {
        this.setName(ani.getName());
        this.setDescription(ani.getDescription());
        this.setAgeYears(ani.getAgeYears());
        this.setAgeMonths(ani.getAgeMonths());
        this.setImage(ani.getImage());
        
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
        this.image = image;
    }

    /**
     * @return the breed
     */
    public String[] getBreed() {
        return breed;
    }

    /**
     * @param breed the breed to set
     */
    public void setBreed(String[] breed) {
        this.breed = breed;
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
        this.tags = tags;
    }
}
