package peaccounting;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="account-metadata">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="accountNr"/>
 *     &lt;xs:element type="xs:string" name="description"/>
 *     &lt;xs:element type="xs:long" name="in-balance"/>
 *     &lt;xs:element type="xs:long" name="out-balance"/>
 *     &lt;xs:element type="xs:int" name="debit-count"/>
 *     &lt;xs:element type="xs:int" name="credit-count"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class AccountMetadata{
    private int accountNr;
    private String description;
    private long inBalance;
    private long outBalance;
    private int debitCount;
    private int creditCount;

    /**
     * Get the 'accountNr' element value.
     *
     * @return value
     */
    public int getAccountNr(){
        return accountNr;
    }

    /**
     * Set the 'accountNr' element value.
     *
     * @param accountNr
     */
    public void setAccountNr(int accountNr){
        this.accountNr = accountNr;
    }

    /**
     * Get the 'description' element value.
     *
     * @return value
     */
    public String getDescription(){
        return description;
    }

    /**
     * Set the 'description' element value.
     *
     * @param description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Get the 'in-balance' element value.
     *
     * @return value
     */
    public long getInBalance(){
        return inBalance;
    }

    /**
     * Set the 'in-balance' element value.
     *
     * @param inBalance
     */
    public void setInBalance(long inBalance){
        this.inBalance = inBalance;
    }

    /**
     * Get the 'out-balance' element value.
     *
     * @return value
     */
    public long getOutBalance(){
        return outBalance;
    }

    /**
     * Set the 'out-balance' element value.
     *
     * @param outBalance
     */
    public void setOutBalance(long outBalance){
        this.outBalance = outBalance;
    }

    /**
     * Get the 'debit-count' element value.
     *
     * @return value
     */
    public int getDebitCount(){
        return debitCount;
    }

    /**
     * Set the 'debit-count' element value.
     *
     * @param debitCount
     */
    public void setDebitCount(int debitCount){
        this.debitCount = debitCount;
    }

    /**
     * Get the 'credit-count' element value.
     *
     * @return value
     */
    public int getCreditCount(){
        return creditCount;
    }

    /**
     * Set the 'credit-count' element value.
     *
     * @param creditCount
     */
    public void setCreditCount(int creditCount){
        this.creditCount = creditCount;
    }
}
