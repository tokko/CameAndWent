package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import org.simpleframework.xml.Element;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="user">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="xs:string" name="foreign-id" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="name"/>
 *     &lt;xs:element type="xs:string" name="email"/>
 *     &lt;xs:element type="xs:boolean" name="active"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
@Element
public class User{
    @Element
    private int id;
    @Element
    private String foreignId;
    @Element
    private String name;
    @Element
    private String email;
    @Element
    private boolean active;

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
     * Get the 'active' element value.
     *
     * @return value
     */
    public boolean isActive(){
        return active;
    }

    /**
     * Set the 'active' element value.
     *
     * @param active
     */
    public void setActive(boolean active){
        this.active = active;
    }
}
