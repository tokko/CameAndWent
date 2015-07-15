package com.tokko.cameandwent.cameandwent.peaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="address">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="address1"/>
 *     &lt;xs:element type="xs:string" name="address2"/>
 *     &lt;xs:element type="xs:string" name="zip-code"/>
 *     &lt;xs:element type="xs:string" name="state"/>
 *     &lt;xs:element type="xs:string" name="country"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Address{
    private String address1;
    private String address2;
    private String zipCode;
    private String state;
    private String country;

    /**
     * Get the 'address1' element value.
     *
     * @return value
     */
    public String getAddress1(){
        return address1;
    }

    /**
     * Set the 'address1' element value.
     *
     * @param address1
     */
    public void setAddress1(String address1){
        this.address1 = address1;
    }

    /**
     * Get the 'address2' element value.
     *
     * @return value
     */
    public String getAddress2(){
        return address2;
    }

    /**
     * Set the 'address2' element value.
     *
     * @param address2
     */
    public void setAddress2(String address2){
        this.address2 = address2;
    }

    /**
     * Get the 'zip-code' element value.
     *
     * @return value
     */
    public String getZipCode(){
        return zipCode;
    }

    /**
     * Set the 'zip-code' element value.
     *
     * @param zipCode
     */
    public void setZipCode(String zipCode){
        this.zipCode = zipCode;
    }

    /**
     * Get the 'state' element value.
     *
     * @return value
     */
    public String getState(){
        return state;
    }

    /**
     * Set the 'state' element value.
     *
     * @param state
     */
    public void setState(String state){
        this.state = state;
    }

    /**
     * Get the 'country' element value.
     *
     * @return value
     */
    public String getCountry(){
        return country;
    }

    /**
     * Set the 'country' element value.
     *
     * @param country
     */
    public void setCountry(String country){
        this.country = country;
    }
}
