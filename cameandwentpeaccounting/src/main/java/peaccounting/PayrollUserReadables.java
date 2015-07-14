package peaccounting;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-user-readables">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="payroll-user-readable" name="payroll-user-readable" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollUserReadables{
    private List<PayrollUserReadable> payrollUserReadableList = new
            ArrayList<PayrollUserReadable>();

    /**
     * Get the list of 'payroll-user-readable' element items.
     *
     * @return list
     */
    public List<PayrollUserReadable> getPayrollUserReadableList(){
        return payrollUserReadableList;
    }

    /**
     * Set the list of 'payroll-user-readable' element items.
     *
     * @param list
     */
    public void setPayrollUserReadableList(List<PayrollUserReadable> list){
        payrollUserReadableList = list;
    }
}
