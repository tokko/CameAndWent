package peaccounting;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-month-report-readables">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="payroll-month-report-readable" name="payroll-month-report-readable"
 *     minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollMonthReportReadables{
    private List<PayrollMonthReportReadable> payrollMonthReportReadableList = new
            ArrayList<PayrollMonthReportReadable>();

    /**
     * Get the list of 'payroll-month-report-readable' element items.
     *
     * @return list
     */
    public List<PayrollMonthReportReadable> getPayrollMonthReportReadableList(){
        return payrollMonthReportReadableList;
    }

    /**
     * Set the list of 'payroll-month-report-readable' element items.
     *
     * @param list
     */
    public void setPayrollMonthReportReadableList(
            List<PayrollMonthReportReadable> list
    ){
        payrollMonthReportReadableList = list;
    }
}
