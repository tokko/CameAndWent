package com.tokko.cameandwentpeaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-rolling-schedule-writeable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="name"/>
 *     &lt;xs:element type="xs:string" name="description"/>
 *     &lt;xs:element name="intervals" minOccurs="0">
 *       &lt;!-- Reference to inner class Intervals -->
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollRollingScheduleWriteable{
    private String name;
    private String description;
    private Intervals intervals;

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
     * Get the 'intervals' element value.
     *
     * @return value
     */
    public Intervals getIntervals(){
        return intervals;
    }

    /**
     * Set the 'intervals' element value.
     *
     * @param intervals
     */
    public void setIntervals(Intervals intervals){
        this.intervals = intervals;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="intervals" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="payroll-rolling-schedule-interval" name="interval"
     *       minOccurs="0" maxOccurs="unbounded"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Intervals{
        private List<PayrollRollingScheduleInterval> intervalList = new
                ArrayList<PayrollRollingScheduleInterval>();

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
}
