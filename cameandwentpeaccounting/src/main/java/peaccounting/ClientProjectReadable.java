package peaccounting;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="client-project-readable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="client-project-reference" name="id"/>
 *     &lt;xs:element type="client-reference" name="client" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="number"/>
 *     &lt;xs:element type="xs:string" name="name"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ClientProjectReadable{
    private ClientProjectReference id;
    private ClientReference client;
    private String number;
    private String name;

    /**
     * Get the 'id' element value.
     *
     * @return value
     */
    public ClientProjectReference getId(){
        return id;
    }

    /**
     * Set the 'id' element value.
     *
     * @param id
     */
    public void setId(ClientProjectReference id){
        this.id = id;
    }

    /**
     * Get the 'client' element value.
     *
     * @return value
     */
    public ClientReference getClient(){
        return client;
    }

    /**
     * Set the 'client' element value.
     *
     * @param client
     */
    public void setClient(ClientReference client){
        this.client = client;
    }

    /**
     * Get the 'number' element value.
     *
     * @return value
     */
    public String getNumber(){
        return number;
    }

    /**
     * Set the 'number' element value.
     *
     * @param number
     */
    public void setNumber(String number){
        this.number = number;
    }

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
}
