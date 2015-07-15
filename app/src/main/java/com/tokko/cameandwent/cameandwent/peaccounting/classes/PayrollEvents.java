package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-events">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="payroll-event" name="payroll-event" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollEvents{
    private List<PayrollEvent> payrollEventList = new ArrayList<PayrollEvent>();

    /**
     * Get the list of 'payroll-event' element items.
     *
     * @return list
     */
    public List<PayrollEvent> getPayrollEventList(){
        return payrollEventList;
    }

    /**
     * Set the list of 'payroll-event' element items.
     *
     * @param list
     */
    public void setPayrollEventList(List<PayrollEvent> list){
        payrollEventList = list;
    }
}
