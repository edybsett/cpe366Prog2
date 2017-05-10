
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named(value = "util")
@SessionScoped
@ManagedBean
public class Util implements Serializable {

    public static void invalidateUserSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
    }

    public void validateDate(FacesContext context, UIComponent component, Object value)
            throws Exception {
        try {
            Date d = (Date) value;
        } catch (Exception e) {
            FacesMessage errorMessage = new FacesMessage("Input is not a valid date");
            throw new ValidatorException(errorMessage);
        }
    }
    
    /**
     * Saves a little space at the beginning and end of each function
     * @author Austin Sparks
     * @param connection - The DBConnection to connect to
     * @return successful connection
     * @throws SQLException 
     */
    public static Connection connect(DBConnect connection) throws SQLException {
        Connection con = connection.getConnection();
        if (con == null) {
            throw new SQLException("Can't get database connection");
        }
        con.setAutoCommit(false);
        return con;
    }
}
