package peaccounting;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="update-client-agreement">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="client-agreement" name="client-agreement"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class UpdateClientAgreement{
    private ClientAgreement clientAgreement;

    /**
     * Get the 'client-agreement' element value.
     *
     * @return value
     */
    public ClientAgreement getClientAgreement(){
        return clientAgreement;
    }

    /**
     * Set the 'client-agreement' element value.
     *
     * @param clientAgreement
     */
    public void setClientAgreement(ClientAgreement clientAgreement){
        this.clientAgreement = clientAgreement;
    }
}
