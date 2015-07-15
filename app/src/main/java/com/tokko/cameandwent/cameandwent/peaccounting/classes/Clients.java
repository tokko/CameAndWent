package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="clients">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="client" name="client" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Clients{
    private List<Client> clientList = new ArrayList<Client>();

    /**
     * Get the list of 'client' element items.
     *
     * @return list
     */
    public List<Client> getClientList(){
        return clientList;
    }

    /**
     * Set the list of 'client' element items.
     *
     * @param list
     */
    public void setClientList(List<Client> list){
        clientList = list;
    }
}
