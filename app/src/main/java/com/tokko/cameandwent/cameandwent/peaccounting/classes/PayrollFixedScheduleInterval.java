package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-fixed-schedule-interval">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:date" name="date"/>
 *     &lt;xs:element type="xs:decimal" name="hours"/>
 *     &lt;xs:element type="payroll-schedule-interval-type" name="type"/>
 *     &lt;xs:element type="xs:string" name="comment"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollFixedScheduleInterval{
    private Date date;
    private BigDecimal hours;
    private PayrollScheduleIntervalType type;
    private String comment;

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
    public PayrollScheduleIntervalType getType(){
        return type;
    }

    /**
     * Set the 'type' element value.
     *
     * @param type
     */
    public void setType(PayrollScheduleIntervalType type){
        this.type = type;
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
