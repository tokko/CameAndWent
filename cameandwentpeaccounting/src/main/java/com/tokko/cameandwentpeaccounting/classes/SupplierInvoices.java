package com.tokko.cameandwentpeaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="supplier-invoices">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="supplier-invoice" name="supplier-invoice" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class SupplierInvoices{
    private List<SupplierInvoice> supplierInvoiceList = new ArrayList<SupplierInvoice>();

    /**
     * Get the list of 'supplier-invoice' element items.
     *
     * @return list
     */
    public List<SupplierInvoice> getSupplierInvoiceList(){
        return supplierInvoiceList;
    }

    /**
     * Set the list of 'supplier-invoice' element items.
     *
     * @param list
     */
    public void setSupplierInvoiceList(List<SupplierInvoice> list){
        supplierInvoiceList = list;
    }
}
