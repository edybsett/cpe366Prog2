
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 * Forms for the Animal's profile when editing
 * information about it.
 * @author Austin Sparks (aasparks)
 */
public class AnimalForm implements Serializable{
    private int    id;
     /* Used for holds and adoptions */
    private String customerUsername;
    private int    customerId;
    private int    topHolder;
    private boolean removing;
    /* Used for medical conditions */
    private String conditionTitle;
    private String conditionDesc;
    private String conditionAction;
    private String conditionType;
    /* Need to bind the animal id */
    private UIInput profileUI;
    private DBConnect dbConnect = new DBConnect();

    public void validateHold(FacesContext context, UIComponent component, Object value) throws ValidatorException, SQLException {
        Connection con   = Util.connect(dbConnect);
        id               = (int)getProfileUI().getLocalValue();
        customerUsername = (String)value;
        boolean customerExists = false;
        boolean customerHolds  = false;
        boolean tooManyHolds   = false;
        String query;
        
        /* Are there too many holds on the animal? */
        query = "SELECT max(priority) as holds ";
        query += "FROM TempHold WHERE animalId = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            topHolder = rs.getInt("holds");
        tooManyHolds = topHolder > 2;
        
        /* Does the customer exist? */
        query = "SELECT id FROM Login WHERE username = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, customerUsername);
        rs = ps.executeQuery();
        customerExists = rs.next();
        
        if (!customerExists) {
            rs.close();
            con.commit();
            con.close();
            FacesMessage m = new FacesMessage("Customer does not exist!");
            throw new ValidatorException(m);
        }
        customerId = rs.getInt("id");
        
        /* Does the customer already have a hold? */
        query = "SELECT customerId FROM TempHold WHERE customerId = ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, customerId);
        rs = ps.executeQuery();
        customerHolds = rs.next();
        
        /* Placing hold, but customer already has one */
        if (customerHolds && !removing) {
            con.commit();
            con.close();
            FacesMessage m = new FacesMessage(customerUsername + " aleady has a hold");
            throw new ValidatorException(m);
        }
        
        /* Customer trying to pale hold, but there are too many */
        if (tooManyHolds && !removing) {
            con.commit();
            con.close();
            FacesMessage m = new FacesMessage("Too many holds already");
            throw new ValidatorException(m);
        }
        
        /* Removing hold, customer does not hold */
        if (removing && !customerHolds) {
            con.commit();
            con.close();
            FacesMessage m = new FacesMessage(customerUsername + " does not have a hold on this animal");
            throw new ValidatorException(m);
        }
        
        con.commit();
        con.close();
    }
    
    public void validateAdopt(FacesContext context, UIComponent component, Object value) throws ValidatorException, SQLException {
        System.out.println("Validating removing: " + removing);
        Connection con   = Util.connect(dbConnect);
        id               = (int)getProfileUI().getLocalValue();
        customerUsername = (String)value;
        PreparedStatement ps;
        ResultSet rs;
        String query;

        /* Does the customer exist? */
        query = "SELECT id FROM Login WHERE username = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, customerUsername);
        rs = ps.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            con.commit();
            con.close();
            FacesMessage m = new FacesMessage("Customer does not exist!");
            throw new ValidatorException(m);
        }
        customerId = rs.getInt("id");
        
                
        /* Are there any holds on the animal other than the customer? */
        query = "SELECT customerId FROM TempHold ";
        query += "WHERE animalId = ? AND customerId != ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.setInt(2, customerId);
        rs = ps.executeQuery();
        if (rs.next()) {
            FacesMessage m = new FacesMessage("Animal on hold");
            throw new ValidatorException(m);
        }
        
        con.commit();
        con.close();       
    }
    
    public String buttonAction() throws SQLException {
        return removing ? removeHold() : placeHold();
    }
    
    public String addCondition() throws SQLException {
        String query;
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        
        query = "INSERT INTO MedicalInfo ";
        query += "VALUES (?, ?, ?, ?, ?)";
        ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.setString(2, conditionTitle);
        ps.setString(3, conditionDesc);
        ps.setString(4, conditionType);
        ps.setString(5, conditionAction);
        ps.executeUpdate();
        con.commit();
        con.close();
        return "profile";
    }
    
    /**
     * @author Austin Sparks
     * @return
     * @throws SQLException 
     */
    public String placeHold() throws SQLException {
        String query;
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        query = "INSERT INTO TempHold ";
        query += "VALUES (?, ?, ?, ?)";
        ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.setInt(2, customerId);
        ps.setDate(3, new Date(System.currentTimeMillis()));
        ps.setInt(4, ++topHolder);
        ps.executeUpdate();
        con.commit();
        con.close();
        return "profile";
    }
    
    /**
     * @author Austin Sparks
     * @param p
     * @return
     * @throws SQLException 
     */
    public String removeHold() throws SQLException {
        String query;
        Connection con = Util.connect(dbConnect);
        PreparedStatement ps;
        query = "DELETE FROM TempHold ";
        query += "WHERE animalId = ? AND customerId = ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.setInt(2, customerId);
        ps.executeUpdate();
        con.commit();
        con.close();
        return "profile";
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
        profileUI = new UIInput();
        profileUI.setValue(id);
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
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the topHolder
     */
    public int getTopHolder() {
        return topHolder;
    }

    /**
     * @param topHolder the topHolder to set
     */
    public void setTopHolder(int topHolder) {
        this.topHolder = topHolder;
    }

    /**
     * @return the removing
     */
    public boolean isRemoving() {
        return removing;
    }

    /**
     * @param removing the removing to set
     */
    public void setRemoving(boolean removing) {
        this.removing = removing;
    }

    /**
     * @return the profileUI
     */
    public UIInput getProfileUI() {
        return profileUI;
    }

    /**
     * @param profileUI the profileUI to set
     */    
    public void setProfileUI(UIInput profileUI) {
        this.profileUI = profileUI;
    }

    /**
     * @return the conditionTitle
     */
    public String getConditionTitle() {
        return conditionTitle;
    }

    /**
     * @param conditionTitle the conditionTitle to set
     */
    public void setConditionTitle(String conditionTitle) {
        this.conditionTitle = conditionTitle;
    }

    /**
     * @return the conditionDesc
     */
    public String getConditionDesc() {
        return conditionDesc;
    }

    /**
     * @param conditionDesc the conditionDesc to set
     */
    public void setConditionDesc(String conditionDesc) {
        this.conditionDesc = conditionDesc;
    }

    /**
     * @return the conditionAction
     */
    public String getConditionAction() {
        return conditionAction;
    }

    /**
     * @param conditionAction the conditionAction to set
     */
    public void setConditionAction(String conditionAction) {
        this.conditionAction = conditionAction;
    }

    /**
     * @return the conditionType
     */
    public String getConditionType() {
        return conditionType;
    }

    /**
     * @param conditionType the conditionType to set
     */
    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }
}
