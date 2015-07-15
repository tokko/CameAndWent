package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-runs">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="payroll-run" name="payroll-run" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollRuns{
    private List<PayrollRun> payrollRunList = new ArrayList<PayrollRun>();

    /**
     * Get the list of 'payroll-run' element items.
     *
     * @return list
     */
    public List<PayrollRun> getPayrollRunList(){
        return payrollRunList;
    }

    /**
     * Set the list of 'payroll-run' element items.
     *
     * @param list
     */
    public void setPayrollRunList(List<PayrollRun> list){
        payrollRunList = list;
    }
}
