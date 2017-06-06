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
    private int userId;
    private String email;
    private DBConnect dbConnect = new DBConnect();
    private boolean manager;
    private double wage;
    private String role;
   
    
     public void setRole(String role){
        this.role = role;
    }
    
    public String getRole(){
        return role;
    }
    
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

    public void setManager(boolean name){
        this.manager = name;
    }
    
    public boolean getManager(){
        return manager;
    }

    public void setWage(double name){
        this.wage = name;
    }
    
    public double getWage(){
        return wage;
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
        if (rs.next()) {
            userId = rs.getInt("id");
            System.out.println("user ID " + userId);
        }
        else {
            return "fail";
        }
        ps.close();
        rs.close();
        query = "Insert into customer(id, email) Values(?,?)";
        ps = con.prepareStatement(query);
        ps.setInt(1, userId);
        ps.setString(2, email);
        ps.execute();
        ps.close();

        con.commit();
        con.close();       
        return "success";
        
    }
    
    public String addEmployee() throws SQLException, IOException {
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
   
        ps.setString(5,role);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            userId = rs.getInt("id");
        ps.close();
        rs.close();
        query = "Insert into Employee(id, wage) Values(?,?)";
        ps = con.prepareStatement(query);
        ps.setInt(1, userId);
        ps.setDouble(2, wage);
        ps.execute();
        ps.close();

        username = "";
        password = "";
        firstname = "";
        lastname = "";
        role ="";
        wage = 0.0;
        con.commit();
        con.close();       
        return "success";        
    }
    
    
    
}
