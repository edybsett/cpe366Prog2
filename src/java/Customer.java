import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

/**
 * Represents a customer
 * @author Austin Sparks (aasparks)
 */
//@Named(value = "customer")
//@SessionScoped
//@ManagedBean
public class Customer implements Serializable {
    private DBConnect dbConnect = new DBConnect();
    private Integer cid;
    private String firstName;
    private String lastName;
}
