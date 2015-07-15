package com.tokko.cameandwent.cameandwent.peaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="client">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="xs:string" name="foreign-id"/>
 *     &lt;xs:element type="xs:string" name="name"/>
 *     &lt;xs:element type="xs:string" name="contact"/>
 *     &lt;xs:element type="address" name="address"/>
 *     &lt;xs:element type="xs:string" name="email"/>
 *     &lt;xs:element type="xs:string" name="country-code"/>
 *     &lt;xs:element type="xs:int" name="accountnr"/>
 *     &lt;xs:element type="xs:int" name="payment-days"/>
 *     &lt;xs:element type="xs:string" name="orgno"/>
 *     &lt;xs:element type="xs:string" name="phone"/>
 *     &lt;xs:element type="user-reference" name="user"/>
 *     &lt;xs:element type="xs:string" name="delivery-type" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="vat-nr" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="gln" minOccurs="0"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Client{
    private int id;
    private String foreignId;
    private String name;
    private String contact;
    private Address address;
    private String email;
    private String countryCode;
    private int accountnr;
    private int paymentDays;
    private String orgno;
    private String phone;
    private UserReference user;
    private String deliveryType;
    private String vatNr;
    private String gln;

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
     * Get the 'contact' element value.
     *
     * @return value
     */
    public String getContact(){
        return contact;
    }

    /**
     * Set the 'contact' element value.
     *
     * @param contact
     */
    public void setContact(String contact){
        this.contact = contact;
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
     * Get the 'accountnr' element value.
     *
     * @return value
     */
    public int getAccountnr(){
        return accountnr;
    }

    /**
     * Set the 'accountnr' element value.
     *
     * @param accountnr
     */
    public void setAccountnr(int accountnr){
        this.accountnr = accountnr;
    }

    /**
     * Get the 'payment-days' element value.
     *
     * @return value
     */
    public int getPaymentDays(){
        return paymentDays;
    }

    /**
     * Set the 'payment-days' element value.
     *
     * @param paymentDays
     */
    public void setPaymentDays(int paymentDays){
        this.paymentDays = paymentDays;
    }

    /**
     * Get the 'orgno' element value.
     *
     * @return value
     */
    public String getOrgno(){
        return orgno;
    }

    /**
     * Set the 'orgno' element value.
     *
     * @param orgno
     */
    public void setOrgno(String orgno){
        this.orgno = orgno;
    }

    /**
     * Get the 'phone' element value.
     *
     * @return value
     */
    public String getPhone(){
        return phone;
    }

    /**
     * Set the 'phone' element value.
     *
     * @param phone
     */
    public void setPhone(String phone){
        this.phone = phone;
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
     * Get the 'delivery-type' element value.
     *
     * @return value
     */
    public String getDeliveryType(){
        return deliveryType;
    }

    /**
     * Set the 'delivery-type' element value.
     *
     * @param deliveryType
     */
    public void setDeliveryType(String deliveryType){
        this.deliveryType = deliveryType;
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
     * Get the 'gln' element value.
     *
     * @return value
     */
    public String getGln(){
        return gln;
    }

    /**
     * Set the 'gln' element value.
     *
     * @param gln
     */
    public void setGln(String gln){
        this.gln = gln;
    }
}
