package com.tokko.cameandwentpeaccounting.classes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-rolling-schedule-readable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="xs:string" name="name"/>
 *     &lt;xs:element type="xs:string" name="description"/>
 *     &lt;xs:element type="xs:int" name="weeks"/>
 *     &lt;xs:element type="xs:decimal" name="hours-per-week"/>
 *     &lt;xs:element type="xs:decimal" name="level-of-employment"/>
 *     &lt;xs:element type="xs:int" name="usage-count" minOccurs="0"/>
 *     &lt;xs:element type="xs:boolean" name="editable"/>
 *     &lt;xs:element name="intervals">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element type="payroll-rolling-schedule-interval" name="interval"
 *           minOccurs="0" maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollRollingScheduleReadable{
    private int id;
    private String name;
    private String description;
    private int weeks;
    private BigDecimal hoursPerWeek;
    private BigDecimal levelOfEmployment;
    private Integer usageCount;
    private boolean editable;
    private List<PayrollRollingScheduleInterval> intervalList = new
            ArrayList<PayrollRollingScheduleInterval>();

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

    /**
     * Get the 'weeks' element value.
     *
     * @return value
     */
    public int getWeeks(){
        return weeks;
    }

    /**
     * Set the 'weeks' element value.
     *
     * @param weeks
     */
    public void setWeeks(int weeks){
        this.weeks = weeks;
    }

    /**
     * Get the 'hours-per-week' element value.
     *
     * @return value
     */
    public BigDecimal getHoursPerWeek(){
        return hoursPerWeek;
    }

    /**
     * Set the 'hours-per-week' element value.
     *
     * @param hoursPerWeek
     */
    public void setHoursPerWeek(BigDecimal hoursPerWeek){
        this.hoursPerWeek = hoursPerWeek;
    }

    /**
     * Get the 'level-of-employment' element value.
     *
     * @return value
     */
    public BigDecimal getLevelOfEmployment(){
        return levelOfEmployment;
    }

    /**
     * Set the 'level-of-employment' element value.
     *
     * @param levelOfEmployment
     */
    public void setLevelOfEmployment(BigDecimal levelOfEmployment){
        this.levelOfEmployment = levelOfEmployment;
    }

    /**
     * Get the 'usage-count' element value.
     *
     * @return value
     */
    public Integer getUsageCount(){
        return usageCount;
    }

    /**
     * Set the 'usage-count' element value.
     *
     * @param usageCount
     */
    public void setUsageCount(Integer usageCount){
        this.usageCount = usageCount;
    }

    /**
     * Get the 'editable' element value.
     *
     * @return value
     */
    public boolean isEditable(){
        return editable;
    }

    /**
     * Set the 'editable' element value.
     *
     * @param editable
     */
    public void setEditable(boolean editable){
        this.editable = editable;
    }

    /**
     * Get the list of 'interval' element items.
     *
     * @return list
     */
    public List<PayrollRollingScheduleInterval> getIntervalList(){
        return intervalList;
    }

    /**
     * Set the list of 'interval' element items.
     *
     * @param list
     */
    public void setIntervalList(List<PayrollRollingScheduleInterval> list){
        intervalList = list;
    }
}
