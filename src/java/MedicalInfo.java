
import java.io.Serializable;

/**
 * Stores simple information about medical conditions.
 * These may be allergies, spaying, surgeries, or conditions.
 * @author Austin
 */
public class MedicalInfo implements Serializable {
    private String name = "None";
    private String description;
    private String action;
    private String type;
    private String severity;
    
    public boolean hasDescription() {
        return description != null;
    }
    
    public boolean hasType() {
        return type != null;
    }
    
    public boolean hasAction() {
        return action != null;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the severity
     */
    public String getSeverity() {
        if ("spay".equals(action))
        {
            if (this.name.equals("None"))
                this.severity = "danger";
            else
                this.severity = "success";
        }
        return severity;
    }

    /**
     * @param severity the severity to set
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
