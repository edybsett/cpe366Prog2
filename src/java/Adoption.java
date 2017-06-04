
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author matt
 */
@Named(value = "adoption")
@SessionScoped
public class Adoption implements Serializable {
    private DBConnect dbConnect = new DBConnect();
    private int customerId;
    private int animalId;
    private int animalAgeY;
    private String customerUsername;
    private float  price;
    private String animalName;
    
    /**
     * Gets ready to adopt the animal
     * @author Austin Sparks (aasparks)
     * @return
     * @throws SQLException
     * @throws IOException 
     */
    public String checkout(Profile p, AnimalForm a) throws SQLException {
        customerUsername = a.getCustomerUsername();
        animalId         = p.getId();
        animalAgeY       = p.getAgeYears();
        setAnimalName(p.getName());
        Connection con = Util.connect(new DBConnect());
        String query = "SELECT price FROM Price WHERE ";
        query += "? < endAgeYear AND ? >= startAgeYear";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, animalAgeY);
        ps.setInt(2, animalAgeY);
        ResultSet rs = ps.executeQuery();
        rs.next();
        price = rs.getFloat("price");
        con.commit();
        con.close();       
        return "adoption";
    }
    
    public String submitAdoption() throws SQLException, IOException {
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        String query;
        query = "INSERT INTO Adoption(animalid, customerid, day) VALUES (?,?,?)";
        ps = con.prepareStatement(query);
        ps.setInt(1, animalId);
        ps.setInt(2, customerId);
        ps.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
        ps.executeUpdate();
        
        con.commit();
        con.close();
        return "index";
    }
    
    public String deleteAnimal () throws SQLException, IOException {
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        String query;
        query = "DELETE Animal where animalid = ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, animalId);
        ps.executeUpdate();
        
        con.commit();
        con.close();
        return "index";
    }
    
    public void setCustomerId(int id)
    {
        customerId = id;
    }
    public int getCustomerId()
    {
        return customerId;
    }
    public int getAnimalId()
    {
        return animalId;
    }
    public void setAnimalId(int id)
    {
        animalId = id;
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
     * @return the price
     */
    public float getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(float price) {
        this.price = price;
    }
}
