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
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author eric
 */
@Named(value = "petclass")
@ManagedBean
@SessionScoped
public class PetClass implements Serializable{
    private String startTime;
    private String endTime;
    private String username;
    private String className;
    private double price;
    private int classId;
    private int sessionId;
    private String day;
    private DBConnect dbConnect = new DBConnect();
    
    public String deleteClass() throws SQLException {
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps
                = con.prepareStatement("delete from Class where id = ?");
        ps.setInt(1, classId);
        ps.executeUpdate();
        con.commit();
        con.close();
        return "refresh";
    }
    
    public String deleteClassSession() throws SQLException {
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps
                = con.prepareStatement("delete from ClassSession where id = ?");
        ps.setInt(1, sessionId);
        ps.executeUpdate();
        con.commit();
        con.close();
        return "refresh";
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

    /**
     * @return the day
     */
    public String getDay() {
        return day;
    }

    /**
     * @param day the day to set
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * @return the sessionId
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
