package peaccounting;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="bank-accounts-readable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="bank-account-readable" name="bank-account-readable" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class BankAccountsReadable{
    private List<BankAccountReadable> bankAccountReadableList = new
            ArrayList<BankAccountReadable>();

    /**
     * Get the list of 'bank-account-readable' element items.
     *
     * @return list
     */
    public List<BankAccountReadable> getBankAccountReadableList(){
        return bankAccountReadableList;
    }

    /**
     * Set the list of 'bank-account-readable' element items.
     *
     * @param list
     */
    public void setBankAccountReadableList(List<BankAccountReadable> list){
        bankAccountReadableList = list;
    }
}
