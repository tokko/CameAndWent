package com.tokko.cameandwentpeaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-article-readables">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="payroll-article-readable" name="payroll-article-readable"
 *     minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollArticleReadables{
    private List<PayrollArticleReadable> payrollArticleReadableList = new
            ArrayList<PayrollArticleReadable>();

    /**
     * Get the list of 'payroll-article-readable' element items.
     *
     * @return list
     */
    public List<PayrollArticleReadable> getPayrollArticleReadableList(){
        return payrollArticleReadableList;
    }

    /**
     * Set the list of 'payroll-article-readable' element items.
     *
     * @param list
     */
    public void setPayrollArticleReadableList(List<PayrollArticleReadable> list){
        payrollArticleReadableList = list;
    }
}
