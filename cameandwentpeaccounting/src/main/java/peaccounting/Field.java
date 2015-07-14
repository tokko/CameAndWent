package peaccounting;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="field">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="name" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="alias"/>
 *     &lt;xs:element type="xs:string" name="value"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Field{
    private String name;
    private String alias;
    private String value;

    /**
     * Get the 'name' element value.
     *
     * @return value
     */
    public String getName(){
        return name;
    }

    /**
     * Set the 'name' element value.
     *
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Get the 'alias' element value.
     *
     * @return value
     */
    public String getAlias(){
        return alias;
    }

    /**
     * Set the 'alias' element value.
     *
     * @param alias
     */
    public void setAlias(String alias){
        this.alias = alias;
    }

    /**
     * Get the 'value' element value.
     *
     * @return value
     */
    public String getValue(){
        return value;
    }

    /**
     * Set the 'value' element value.
     *
     * @param value
     */
    public void setValue(String value){
        this.value = value;
    }
}
