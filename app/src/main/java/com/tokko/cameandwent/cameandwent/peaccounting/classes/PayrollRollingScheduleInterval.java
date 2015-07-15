package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.math.BigDecimal;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-rolling-schedule-interval">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="day"/>
 *     &lt;xs:element type="xs:decimal" name="hours"/>
 *     &lt;xs:element type="payroll-schedule-interval-type" name="type"/>
 *     &lt;xs:element type="xs:string" name="comment"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollRollingScheduleInterval{
    private int day;
    private BigDecimal hours;
    private PayrollScheduleIntervalType type;
    private String comment;

    /**
     * Get the 'day' element value.
     *
     * @return value
     */
    public int getDay(){
        return day;
    }

    /**
     * Set the 'day' element value.
     *
     * @param day
     */
    public void setDay(int day){
        this.day = day;
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
