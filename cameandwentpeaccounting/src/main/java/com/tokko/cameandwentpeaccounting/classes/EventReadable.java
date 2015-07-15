package com.tokko.cameandwentpeaccounting.classes;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="event-readable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="event-reference" name="id"/>
 *     &lt;xs:element type="xs:string" name="foreign-id"/>
 *     &lt;xs:element type="user-reference" name="user"/>
 *     &lt;xs:element type="activity-reference" name="activity"/>
 *     &lt;xs:element type="client-project-reference" name="client-project" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="child" minOccurs="0"/>
 *     &lt;xs:element type="xs:date" name="date"/>
 *     &lt;xs:element type="xs:decimal" name="hours"/>
 *     &lt;xs:element type="xs:string" name="comment"/>
 *     &lt;xs:element type="xs:boolean" name="approved"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class EventReadable{
    private EventReference id;
    private String foreignId;
    private UserReference user;
    private ActivityReference activity;
    private ClientProjectReference clientProject;
    private String child;
    private Date date;
    private BigDecimal hours;
    private String comment;
    private boolean approved;

    /**
     * Get the 'id' element value.
     *
     * @return value
     */
    public EventReference getId(){
        return id;
    }

    /**
     * Set the 'id' element value.
     *
     * @param id
     */
    public void setId(EventReference id){
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
     * Get the 'activity' element value.
     *
     * @return value
     */
    public ActivityReference getActivity(){
        return activity;
    }

    /**
     * Set the 'activity' element value.
     *
     * @param activity
     */
    public void setActivity(ActivityReference activity){
        this.activity = activity;
    }

    /**
     * Get the 'client-project' element value.
     *
     * @return value
     */
    public ClientProjectReference getClientProject(){
        return clientProject;
    }

    /**
     * Set the 'client-project' element value.
     *
     * @param clientProject
     */
    public void setClientProject(ClientProjectReference clientProject){
        this.clientProject = clientProject;
    }

    /**
     * Get the 'child' element value.
     *
     * @return value
     */
    public String getChild(){
        return child;
    }

    /**
     * Set the 'child' element value.
     *
     * @param child
     */
    public void setChild(String child){
        this.child = child;
    }

    /**
     * Get the 'date' element value.
     *
     * @return value
     */
    public Date getDate(){
        return date;
    }

    /**
     * Set the 'date' element value.
     *
     * @param date
     */
    public void setDate(Date date){
        this.date = date;
    }

    /**
     * Get the 'hours' element value.
     *
     * @return value
     */
    public BigDecimal getHours(){
        return hours;
    }

    /**
     * Set the 'hours' element value.
     *
     * @param hours
     */
    public void setHours(BigDecimal hours){
        this.hours = hours;
    }

    /**
     * Get the 'comment' element value.
     *
     * @return value
     */
    public String getComment(){
        return comment;
    }

    /**
     * Set the 'comment' element value.
     *
     * @param comment
     */
    public void setComment(String comment){
        this.comment = comment;
    }

    /**
     * Get the 'approved' element value.
     *
     * @return value
     */
    public boolean isApproved(){
        return approved;
    }

    /**
     * Set the 'approved' element value.
     *
     * @param approved
     */
    public void setApproved(boolean approved){
        this.approved = approved;
    }
}
