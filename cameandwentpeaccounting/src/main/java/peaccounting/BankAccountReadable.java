package peaccounting;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="bank-account-readable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="xs:string" name="foreign-id"/>
 *     &lt;xs:element type="xs:string" name="number"/>
 *     &lt;xs:element type="xs:string" name="currency-type"/>
 *     &lt;xs:element type="xs:string" name="bic"/>
 *     &lt;xs:element type="xs:string" name="iban"/>
 *     &lt;xs:element type="xs:string" name="name"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class BankAccountReadable{
    private int id;
    private String foreignId;
    private String number;
    private String currencyType;
    private String bic;
    private String iban;
    private String name;

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

    /**
     * Get the 'foreign-id' element value.
     *
     * @return value
     */
    public String getForeignId(){
        return foreignId;
    }

    /**
     * Set the 'foreign-id' element value.
     *
     * @param foreignId
     */
    public void setForeignId(String foreignId){
        this.foreignId = foreignId;
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
     * Get the 'currency-type' element value.
     *
     * @return value
     */
    public String getCurrencyType(){
        return currencyType;
    }

    /**
     * Set the 'currency-type' element value.
     *
     * @param currencyType
     */
    public void setCurrencyType(String currencyType){
        this.currencyType = currencyType;
    }

    /**
     * Get the 'bic' element value.
     *
     * @return value
     */
    public String getBic(){
        return bic;
    }

    /**
     * Set the 'bic' element value.
     *
     * @param bic
     */
    public void setBic(String bic){
        this.bic = bic;
    }

    /**
     * Get the 'iban' element value.
     *
     * @return value
     */
    public String getIban(){
        return iban;
    }

    /**
     * Set the 'iban' element value.
     *
     * @param iban
     */
    public void setIban(String iban){
        this.iban = iban;
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
