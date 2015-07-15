package com.tokko.cameandwentpeaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="activity-readable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="activity-reference" name="id"/>
 *     &lt;xs:element type="xs:string" name="name"/>
 *     &lt;xs:element type="xs:string" name="description"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ActivityReadable{
    private ActivityReference id;
    private String name;
    private String description;

    /**
     * Get the 'id' element value.
     *
     * @return value
     */
    public ActivityReference getId(){
        return id;
    }

    /**
     * Set the 'id' element value.
     *
     * @param id
     */
    public void setId(ActivityReference id){
        this.id = id;
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
     * Get the 'description' element value.
     *
     * @return value
     */
    public String getDescription(){
        return description;
    }

    /**
     * Set the 'description' element value.
     *
     * @param description
     */
    public void setDescription(String description){
        this.description = description;
    }
}
