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
 *
 * @author eric
 */
@Named(value = "class")
@ManagedBean
@SessionScoped
public class PetClass implements Serializable{
    private String startTime;
    private String endTime;
    private String username;
    private String className;
    private double price;
    private int classId;
    private DBConnect dbConnect = new DBConnect();
    
    private int time;
    private int day;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public List<PetClass> getMondayClasses() throws SQLException {
        return getClasses(1);
    }
    
    public List<PetClass> getTuesdayClasses() throws SQLException {
        return getClasses(2);
    }
    
    public List<PetClass> getWednesdayClasses() throws SQLException {
        return getClasses(3);
    }
    
    public List<PetClass> getThursdayClasses() throws SQLException {
        return getClasses(4);
    }
    
    public List<PetClass> getFridayClasses() throws SQLException {
        return getClasses(5);
    }
    
    public List<PetClass> getSaturdayClasses() throws SQLException {
        return getClasses(6);
    }
    
    public List<PetClass> getSundayClasses() throws SQLException {
        return getClasses(7);
    }
    
    public String addClass(String username) {
        return "classes";
    }
    
    public List<PetClass> getClasses(int day) throws SQLException {
        Connection con = Util.connect(dbConnect);
        List<PetClass> list = new ArrayList<PetClass>();
        PreparedStatement ps
                = con.prepareStatement("select c.id, ct.startTime, ct.endTime, t.className, l.firstName, l.lastName, c.price \n" +
                                        "from Class c, ClassTimes ct, Teachers t, Login l\n" +
                                        "where c.teachId = t.classId and c.blockId = ct.blockId and l.id = t.teacherId\n" +
                                        "and (ct.blockId >= " + day +"00 and ct.blockId < " + day+1 +"00)");
        ResultSet result = ps.executeQuery();

     
        while (result.next()) {
            PetClass pet = new PetClass();
            pet.setClassId(result.getInt("id"));
            pet.setStartTime(result.getTime("startTime").toString());
            pet.setEndTime(result.getTime("endTime").toString());
            pet.setClassName(result.getString("className"));
            pet.setPrice(result.getFloat("price"));
            pet.setUsername(result.getString("firstName") + " " + result.getString("lastName"));
            list.add(pet);
        }
        result.close();
        con.close();
        return list;
        
    }
    
    
    
}
