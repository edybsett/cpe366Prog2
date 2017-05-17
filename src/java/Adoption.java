
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
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author matt
 */
public class Adoption {
    private DBConnect dbConnect = new DBConnect();
    private int customerid;
    private int animalid;
    
    public String submitAdoption() throws SQLException, IOException {
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        String query;
        query = "INSERT INTO Adoption(animalid, customerid, day) VALUES (?,?,?)";
        ps = con.prepareStatement(query);
        ps.setInt(1, animalid);
        ps.setInt(2, customerid);
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
        ps.setInt(1, animalid);
        ps.executeUpdate();
        
        con.commit();
        con.close();
        return "index";
    }
    
    public void setCustomerid(int id)
    {
        customerid = id;
    }
    public int getCustomerid()
    {
        return customerid;
    }
    public int getAnimalid()
    {
        return animalid;
    }
    public void setAnimalid(int id)
    {
        animalid = id;
    }
}
