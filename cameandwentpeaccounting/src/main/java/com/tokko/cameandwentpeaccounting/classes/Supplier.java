package com.tokko.cameandwentpeaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="supplier">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="xs:string" name="foreign-id"/>
 *     &lt;xs:element type="xs:string" name="name"/>
 *     &lt;xs:element type="address" name="address"/>
 *     &lt;xs:element type="xs:string" name="email"/>
 *     &lt;xs:element type="xs:long" name="plusgiro"/>
 *     &lt;xs:element type="xs:long" name="bankgiro"/>
 *     &lt;xs:element type="xs:string" name="iban"/>
 *     &lt;xs:element type="user-reference" name="user" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="our-reference" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="country-code" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="vat-nr" minOccurs="0"/>
 *     &lt;xs:element type="xs:int" name="account-nr" minOccurs="0"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Supplier{
    private int id;
    private String foreignId;
    private String name;
    private Address address;
    private String email;
    private long plusgiro;
    private long bankgiro;
    private String iban;
    private UserReference user;
    private String ourReference;
    private String countryCode;
    private String vatNr;
    private Integer accountNr;

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
     * Get the 'address' element value.
     *
     * @return value
     */
    public Address getAddress(){
        return address;
    }

    /**
     * Set the 'address' element value.
     *
     * @param address
     */
    public void setAddress(Address address){
        this.address = address;
    }

    /**
     * Get the 'email' element value.
     *
     * @return value
     */
    public String getEmail(){
        return email;
    }

    /**
     * Set the 'email' element value.
     *
     * @param email
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Get the 'plusgiro' element value.
     *
     * @return value
     */
    public long getPlusgiro(){
        return plusgiro;
    }

    /**
     * Set the 'plusgiro' element value.
     *
     * @param plusgiro
     */
    public void setPlusgiro(long plusgiro){
        this.plusgiro = plusgiro;
    }

    /**
     * Get the 'bankgiro' element value.
     *
     * @return value
     */
    public long getBankgiro(){
        return bankgiro;
    }

    /**
     * Set the 'bankgiro' element value.
     *
     * @param bankgiro
     */
    public void setBankgiro(long bankgiro){
        this.bankgiro = bankgiro;
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
     * Get the 'user' element value.
     *
     * @return value
     */
    public UserReference getUser(){
        return user;
    }

    /**
     * Set the 'user' element value.
     *
     * @param user
     */
    public void setUser(UserReference user){
        this.user = user;
    }

    /**
     * Get the 'our-reference' element value.
     *
     * @return value
     */
    public String getOurReference(){
        return ourReference;
    }

    /**
     * Set the 'our-reference' element value.
     *
     * @param ourReference
     */
    public void setOurReference(String ourReference){
        this.ourReference = ourReference;
    }

    /**
     * Get the 'country-code' element value.
     *
     * @return value
     */
    public String getCountryCode(){
        return countryCode;
    }

    /**
     * Set the 'country-code' element value.
     *
     * @param countryCode
     */
    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }

    /**
     * Get the 'vat-nr' element value.
     *
     * @return value
     */
    public String getVatNr(){
        return vatNr;
    }

    /**
     * Set the 'vat-nr' element value.
     *
     * @param vatNr
     */
    public void setVatNr(String vatNr){
        this.vatNr = vatNr;
    }

    /**
     * Get the 'account-nr' element value.
     *
     * @return value
     */
    public Integer getAccountNr(){
        return accountNr;
    }

    /**
     * Set the 'account-nr' element value.
     *
     * @param accountNr
     */
    public void setAccountNr(Integer accountNr){
        this.accountNr = accountNr;
    }
}
