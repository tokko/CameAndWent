package peaccounting;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="client-invoice-templates">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="client-invoice-template" name="client-invoice-template" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ClientInvoiceTemplates{
    private List<ClientInvoiceTemplate> clientInvoiceTemplateList = new
            ArrayList<ClientInvoiceTemplate>();

    /**
     * Get the list of 'client-invoice-template' element items.
     *
     * @return list
     */
    public List<ClientInvoiceTemplate> getClientInvoiceTemplateList(){
        return clientInvoiceTemplateList;
    }

    /**
     * Set the list of 'client-invoice-template' element items.
     *
     * @param list
     */
    public void setClientInvoiceTemplateList(List<ClientInvoiceTemplate> list){
        clientInvoiceTemplateList = list;
    }
}
