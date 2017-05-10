
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 * Used for any form data that may occur. May have
 * values that overlap with other forms.
 * @author Austin Sparks (aasparks)
 */
@Named(value = "employee")
@SessionScoped
@ManagedBean
public class Form implements Serializable {
    /* DB Connection */
    private DBConnect dbConnect = new DBConnect();
    /* New animal form data */
    private String animalName;
    private int    animalAgeY;
    private int    animalAgeM;
    private String animalDesc;
    private String animalType;
    private Part   animalImage;
    private int    animalId;
    
    
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
        query += "type) VALUES (?,?,?,?,?)";
        ps = con.prepareStatement(query);
        ps.setString(1, animalName);
        ps.setInt(2, animalAgeY);
        ps.setInt(3, animalAgeM);
        ps.setString(4, animalDesc);
        ps.setString(5, animalType);
        ps.executeUpdate();
        
        /* Get the id and consider animals might have the same name */
        query = "SELECT id FROM Animal WHERE name = ?";
        query += " AND ageYears = ? AND ageMonths = ?";
        query += " AND description = ? AND type = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, animalName);
        ps.setInt(2, animalAgeY);
        ps.setInt(3, animalAgeM);
        ps.setString(4, animalDesc);
        ps.setString(5, animalType);
        ResultSet rs = ps.executeQuery();
        rs.next();
        animalId = rs.getInt("id");
        
        /* Set the image for that id */
        query = "UPDATE Animal SET image=? ";
        query += "WHERE id = ?";
        ps = con.prepareStatement(query);
        //ps.setBinaryStream(1, fin, (int)img.length());
        InputStream is = animalImage.getInputStream();
        ps.setBinaryStream(1,is);
        ps.setInt(2, animalId);
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
}
