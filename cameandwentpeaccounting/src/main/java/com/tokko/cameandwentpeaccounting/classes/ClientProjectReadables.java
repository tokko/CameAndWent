package com.tokko.cameandwentpeaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="client-project-readables">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="client-project-readable" name="client-project-readable" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ClientProjectReadables{
    private List<ClientProjectReadable> clientProjectReadableList = new
            ArrayList<ClientProjectReadable>();

    /**
     * Get the list of 'client-project-readable' element items.
     *
     * @return list
     */
    public List<ClientProjectReadable> getClientProjectReadableList(){
        return clientProjectReadableList;
    }

    /**
     * Set the list of 'client-project-readable' element items.
     *
     * @param list
     */
    public void setClientProjectReadableList(List<ClientProjectReadable> list){
        clientProjectReadableList = list;
    }
}
