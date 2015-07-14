package peaccounting;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="bank-transaction-writeable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="foreign-id" minOccurs="0"/>
 *     &lt;xs:element type="xs:date" name="date"/>
 *     &lt;xs:element type="xs:date" name="transaction-date" minOccurs="0"/>
 *     &lt;xs:element type="xs:date" name="currency-date" minOccurs="0"/>
 *     &lt;xs:element type="xs:long" name="amount"/>
 *     &lt;xs:element type="xs:string" name="reference" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="description" minOccurs="0"/>
 *     &lt;xs:element name="messages" minOccurs="0">
 *       &lt;!-- Reference to inner class Messages -->
 *     &lt;/xs:element>
 *     &lt;xs:element name="files">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element name="file" minOccurs="1" maxOccurs="unbounded">
 *             &lt;!-- Reference to inner class File -->
 *           &lt;/xs:element>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class BankTransactionWriteable{
    private String foreignId;
    private Date date;
    private Date transactionDate;
    private Date currencyDate;
    private long amount;
    private String reference;
    private String description;
    private Messages messages;
    private List<File> fileList = new ArrayList<File>();

    /**
     * Get the 'foreign-id' element value.
     *
     * @return value
     */
    public String getForeignId(){
        return foreignId;
    }

    /**
     * Set the 'foreign-id' element value.
     *
     * @param foreignId
     */
    public void setForeignId(String foreignId){
        this.foreignId = foreignId;
    }

    /**
     * Get the 'date' element value.
     *
     * @return value
     */
    public Date getDate(){
        return date;
    }

    /**
     * Set the 'date' element value.
     *
     * @param date
     */
    public void setDate(Date date){
        this.date = date;
    }

    /**
     * Get the 'transaction-date' element value.
     *
     * @return value
     */
    public Date getTransactionDate(){
        return transactionDate;
    }

    /**
     * Set the 'transaction-date' element value.
     *
     * @param transactionDate
     */
    public void setTransactionDate(Date transactionDate){
        this.transactionDate = transactionDate;
    }

    /**
     * Get the 'currency-date' element value.
     *
     * @return value
     */
    public Date getCurrencyDate(){
        return currencyDate;
    }

    /**
     * Set the 'currency-date' element value.
     *
     * @param currencyDate
     */
    public void setCurrencyDate(Date currencyDate){
        this.currencyDate = currencyDate;
    }

    /**
     * Get the 'amount' element value.
     *
     * @return value
     */
    public long getAmount(){
        return amount;
    }

    /**
     * Set the 'amount' element value.
     *
     * @param amount
     */
    public void setAmount(long amount){
        this.amount = amount;
    }

    /**
     * Get the 'reference' element value.
     *
     * @return value
     */
    public String getReference(){
        return reference;
    }

    /**
     * Set the 'reference' element value.
     *
     * @param reference
     */
    public void setReference(String reference){
        this.reference = reference;
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
     * Get the 'messages' element value.
     *
     * @return value
     */
    public Messages getMessages(){
        return messages;
    }

    /**
     * Set the 'messages' element value.
     *
     * @param messages
     */
    public void setMessages(Messages messages){
        this.messages = messages;
    }

    /**
     * Get the list of 'file' element items.
     *
     * @return list
     */
    public List<File> getFileList(){
        return fileList;
    }

    /**
     * Set the list of 'file' element items.
     *
     * @param list
     */
    public void setFileList(List<File> list){
        fileList = list;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="messages" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:string" name="message" minOccurs="0" maxOccurs="unbounded"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Messages{
        private List<String> messageList = new ArrayList<String>();

        /**
         * Get the list of 'message' element items.
         *
         * @return list
         */
        public List<String> getMessageList(){
            return messageList;
        }

        /**
         * Set the list of 'message' element items.
         *
         * @param list
         */
        public void setMessageList(List<String> list){
            messageList = list;
        }
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="file" minOccurs="1"
     * maxOccurs="unbounded">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:string" name="filename"/>
     *       &lt;xs:element type="xs:hexBinary" name="data"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class File{
        private String filename;
        private byte[] data;

        /**
         * Get the 'filename' element value.
         *
         * @return value
         */
        public String getFilename(){
            return filename;
        }

        /**
         * Set the 'filename' element value.
         *
         * @param filename
         */
        public void setFilename(String filename){
            this.filename = filename;
        }

        /**
         * Get the 'data' element value.
         *
         * @return value
         */
        public byte[] getData(){
            return data;
        }

        /**
         * Set the 'data' element value.
         *
         * @param data
         */
        public void setData(byte[] data){
            this.data = data;
        }
    }
}
