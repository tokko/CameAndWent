package com.tokko.cameandwentpeaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="supplier-invoice-bundle-create-result">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="xs:int" name="nr"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class SupplierInvoiceBundleCreateResult{
    private int nr;

    /**
     * Get the 'nr' element value.
     *
     * @return value
     */
    public int getNr(){
        return nr;
    }

    /**
     * Set the 'nr' element value.
     *
     * @param nr
     */
    public void setNr(int nr){
        this.nr = nr;
    }
}
