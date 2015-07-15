package com.tokko.cameandwentpeaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="suppliers">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="supplier" name="supplier" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Suppliers{
    private List<Supplier> supplierList = new ArrayList<Supplier>();

    /**
     * Get the list of 'supplier' element items.
     *
     * @return list
     */
    public List<Supplier> getSupplierList(){
        return supplierList;
    }

    /**
     * Set the list of 'supplier' element items.
     *
     * @param list
     */
    public void setSupplierList(List<Supplier> list){
        supplierList = list;
    }
}
