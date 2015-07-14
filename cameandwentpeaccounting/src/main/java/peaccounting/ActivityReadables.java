package peaccounting;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="activity-readables">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="activity-readable" name="activity-readable" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ActivityReadables{
    private List<ActivityReadable> activityReadableList = new ArrayList<ActivityReadable>();

    /**
     * Get the list of 'activity-readable' element items.
     *
     * @return list
     */
    public List<ActivityReadable> getActivityReadableList(){
        return activityReadableList;
    }

    /**
     * Set the list of 'activity-readable' element items.
     *
     * @param list
     */
    public void setActivityReadableList(List<ActivityReadable> list){
        activityReadableList = list;
    }
}
