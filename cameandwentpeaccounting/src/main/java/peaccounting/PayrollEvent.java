package peaccounting;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-event">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id" minOccurs="0"/>
 *     &lt;xs:element type="xs:date" name="date"/>
 *     &lt;xs:element type="xs:decimal" name="hours"/>
 *     &lt;xs:element type="payroll-event-type" name="type"/>
 *     &lt;xs:element type="xs:string" name="child" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="comment"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollEvent{
    private Integer id;
    private Date date;
    private BigDecimal hours;
    private PayrollEventType type;
    private String child;
    private String comment;

    /**
     * Get the 'id' element value.
     *
     * @return value
     */
    public Integer getId(){
        return id;
    }

    /**
     * Set the 'id' element value.
     *
     * @param id
     */
    public void setId(Integer id){
        this.id = id;
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
     * Get the 'type' element value.
     *
     * @return value
     */
    public PayrollEventType getType(){
        return type;
    }

    /**
     * Set the 'type' element value.
     *
     * @param type
     */
    public void setType(PayrollEventType type){
        this.type = type;
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
}
