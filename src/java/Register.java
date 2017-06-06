/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.SQLException;
import javax.annotation.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
/**
 *
 * @author Jason
 * 
 */
@Named(value = "register")
@ViewScoped
@ManagedBean
public class Register implements Serializable {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private int customerId;
    private String email;
    private DBConnect dbConnect = new DBConnect();
    
    public void setLastname(String name){
        this.lastname = name;
    }
    
    public String getLastname(){
        return lastname;
    }

    public void setFirstname(String name){
        this.firstname = name;
    }
    
    public String getFirstname(){
        return firstname;
    }

    public void setEmail(String name){
        this.email = name;
    }
    
    public String getEmail(){
        return email;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    public String addCustomer() throws SQLException, IOException {
 
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        String query;
        query = "Insert into login(username, password, firstname, lastname, role)"
                + "Values(?,?,?,?,?)"
                + "RETURNING id";
        ps = con.prepareStatement(query);
        ps.setString(1,username);
        ps.setString(2,password);
        ps.setString(3,firstname);
        ps.setString(4,lastname);
        ps.setString(5,"customer");
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            customerId = rs.getInt("id");
            System.out.println(customerId);
        ps.close();
        rs.close();
        ps = con.prepareStatement(query);
        query = "Insert into customer(id, email) values(?,?)";
        ps.setInt(1, customerId);
        ps.setString(2, email);
        ps.executeQuery();
        ps.close();

        con.commit();
        con.close();       
        return "success";
        
    }
    
    
    
}
