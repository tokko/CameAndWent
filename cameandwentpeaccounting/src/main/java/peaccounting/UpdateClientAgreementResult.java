package peaccounting;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="update-client-agreement-result">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="xs:int" name="revision-nr"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class UpdateClientAgreementResult{
    private int revisionNr;

    /**
     * Get the 'revision-nr' element value.
     *
     * @return value
     */
    public int getRevisionNr(){
        return revisionNr;
    }

    /**
     * Set the 'revision-nr' element value.
     *
     * @param revisionNr
     */
    public void setRevisionNr(int revisionNr){
        this.revisionNr = revisionNr;
    }
}
