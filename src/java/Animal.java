import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Represents the animals
 * @author Austin Sparks (aasparks)
 */
@Named(value = "animal")
@SessionScoped
@ManagedBean
public class Animal implements Serializable {
    private String name;
    private String description;
    private int ageYears;
    private int ageMonths;
    private int ageWeeks;
    private byte[] image; 
    private DBConnect dbConnect = new DBConnect();
    private String type = "all";
    private int activeIndex = 0;
    
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
        if (!type.equals("all"))
            query += " WHERE species=?";
        
        /* Execute query */
        PreparedStatement ps = con.prepareStatement(query);
        if (!type.equals("all"))
            ps.setString(1, getType());
        /* Get results */
        ResultSet rs = ps.executeQuery();
        
        /* Get every result */
        while (rs.next()) {
            Animal a = new Animal();
            a.setName(rs.getString("name"));
            a.setAgeYears(rs.getInt("ageYears"));
            a.setAgeMonths(rs.getInt("ageMonths"));
            a.setDescription(rs.getString("description"));
            a.setImage(rs.getBytes("image"));
            allAnimals.add(a);
        }
        con.commit();
        con.close();
        return allAnimals;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    public String showAll(){
        this.type = "all";
        return "refresh";
    }
    
    public String showDogs() {
        this.type = "dog";
        return "refresh";
    }
    public String showCats() {
        this.type = "cat";
        return "refresh";
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
     * @return the activeIndex
     */
    public int getActiveIndex() {
        return activeIndex;
    }

    /**
     * @param activeIndex the activeIndex to set
     */
    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }
}