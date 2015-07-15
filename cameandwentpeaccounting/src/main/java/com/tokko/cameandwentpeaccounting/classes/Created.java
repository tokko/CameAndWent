package com.tokko.cameandwentpeaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="created">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="xs:int" name="id"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class Created{
    private int id;

    /**
     * Get the 'id' element value.
     *
     * @return value
     */
    public int getId(){
        return id;
    }

    /**
     * Set the 'id' element value.
     *
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }
}
