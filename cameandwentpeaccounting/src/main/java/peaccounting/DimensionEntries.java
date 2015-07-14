package peaccounting;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="dimension-entries">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="dimension-entry" name="dimension-entry" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class DimensionEntries{
    private List<DimensionEntry> dimensionEntryList = new ArrayList<DimensionEntry>();

    /**
     * Get the list of 'dimension-entry' element items.
     *
     * @return list
     */
    public List<DimensionEntry> getDimensionEntryList(){
        return dimensionEntryList;
    }

    /**
     * Set the list of 'dimension-entry' element items.
     *
     * @param list
     */
    public void setDimensionEntryList(List<DimensionEntry> list){
        dimensionEntryList = list;
    }
}
