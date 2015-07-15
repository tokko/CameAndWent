package com.tokko.cameandwentpeaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-user-schedule-metadatas">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="payroll-user-schedule-metadata" name="payroll-user-schedule-metadata"
 *     minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollUserScheduleMetadatas{
    private List<PayrollUserScheduleMetadata> payrollUserScheduleMetadataList = new
            ArrayList<PayrollUserScheduleMetadata>();

    /**
     * Get the list of 'payroll-user-schedule-metadata' element items.
     *
     * @return list
     */
    public List<PayrollUserScheduleMetadata> getPayrollUserScheduleMetadataList(){
        return payrollUserScheduleMetadataList;
    }

    /**
     * Set the list of 'payroll-user-schedule-metadata' element items.
     *
     * @param list
     */
    public void setPayrollUserScheduleMetadataList(
            List<PayrollUserScheduleMetadata> list
    ){
        payrollUserScheduleMetadataList = list;
    }
}
