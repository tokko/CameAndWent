package com.tokko.cameandwentpeaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="client-invoices">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="client-invoice" name="client-invoice" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ClientInvoices{
    private List<ClientInvoice> clientInvoiceList = new ArrayList<ClientInvoice>();

    /**
     * Get the list of 'client-invoice' element items.
     *
     * @return list
     */
    public List<ClientInvoice> getClientInvoiceList(){
        return clientInvoiceList;
    }

    /**
     * Set the list of 'client-invoice' element items.
     *
     * @param list
     */
    public void setClientInvoiceList(List<ClientInvoice> list){
        clientInvoiceList = list;
    }
}
