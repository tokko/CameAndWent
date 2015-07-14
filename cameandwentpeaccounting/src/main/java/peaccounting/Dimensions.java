package peaccounting;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="dimensions">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="dimension" name="dimension" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Dimensions{
    private List<Dimension> dimensionList = new ArrayList<Dimension>();

    /**
     * Get the list of 'dimension' element items.
     *
     * @return list
     */
    public List<Dimension> getDimensionList(){
        return dimensionList;
    }

    /**
     * Set the list of 'dimension' element items.
     *
     * @param list
     */
    public void setDimensionList(List<Dimension> list){
        dimensionList = list;
    }
}
