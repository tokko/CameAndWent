package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="vouchers-readable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="voucher-readable" name="voucher-readable" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class VouchersReadable{
    private List<VoucherReadable> voucherReadableList = new ArrayList<VoucherReadable>();

    /**
     * Get the list of 'voucher-readable' element items.
     *
     * @return list
     */
    public List<VoucherReadable> getVoucherReadableList(){
        return voucherReadableList;
    }

    /**
     * Set the list of 'voucher-readable' element items.
     *
     * @param list
     */
    public void setVoucherReadableList(List<VoucherReadable> list){
        voucherReadableList = list;
    }
}
