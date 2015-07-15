package com.tokko.cameandwentpeaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="client-agreements">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="client-agreement" name="client-agreement" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ClientAgreements{
    private List<ClientAgreement> clientAgreementList = new ArrayList<ClientAgreement>();

    /**
     * Get the list of 'client-agreement' element items.
     *
     * @return list
     */
    public List<ClientAgreement> getClientAgreementList(){
        return clientAgreementList;
    }

    /**
     * Set the list of 'client-agreement' element items.
     *
     * @param list
     */
    public void setClientAgreementList(List<ClientAgreement> list){
        clientAgreementList = list;
    }
}
