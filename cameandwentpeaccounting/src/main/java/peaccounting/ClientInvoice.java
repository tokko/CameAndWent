package peaccounting;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="client-invoice">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="xs:int" name="invoice-nr"/>
 *     &lt;xs:element type="xs:int" name="debit-invoice-nr" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="foreign-id"/>
 *     &lt;xs:element type="xs:string" name="po-nr"/>
 *     &lt;xs:element type="client-reference" name="client-ref"/>
 *     &lt;xs:element type="client-invoice-template-reference" name="client-invoice-template-ref"
 *     minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="your-reference"/>
 *     &lt;xs:element type="user-reference" name="our-reference"/>
 *     &lt;xs:element type="user-reference" name="approver"/>
 *     &lt;xs:element type="address" name="invoice-address"/>
 *     &lt;xs:element type="xs:string" name="invoice-email"/>
 *     &lt;xs:element type="xs:string" name="delivery-name"/>
 *     &lt;xs:element type="address" name="delivery-address"/>
 *     &lt;xs:element type="xs:string" name="delivery-email"/>
 *     &lt;xs:element type="xs:date" name="invoice-date"/>
 *     &lt;xs:element type="xs:date" name="due-date"/>
 *     &lt;xs:element type="xs:date" name="delivery-date"/>
 *     &lt;xs:element name="period" minOccurs="0">
 *       &lt;!-- Reference to inner class Period -->
 *     &lt;/xs:element>
 *     &lt;xs:element type="xs:string" name="currency"/>
 *     &lt;xs:element type="xs:decimal" name="currency-rate" minOccurs="0"/>
 *     &lt;xs:element type="xs:boolean" name="certified"/>
 *     &lt;xs:element type="xs:boolean" name="sent"/>
 *     &lt;xs:element type="xs:string" name="notes" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="delivery-type" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="gln" minOccurs="0"/>
 *     &lt;xs:element type="xs:long" name="remaining"/>
 *     &lt;xs:element type="xs:boolean" name="disabled"/>
 *     &lt;xs:element name="fields" minOccurs="0">
 *       &lt;!-- Reference to inner class Fields -->
 *     &lt;/xs:element>
 *     &lt;xs:element name="files" minOccurs="0">
 *       &lt;!-- Reference to inner class Files -->
 *     &lt;/xs:element>
 *     &lt;xs:element name="rows">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element type="client-invoice-row" name="row" minOccurs="0"
 *           maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ClientInvoice{
    private int id;
    private int invoiceNr;
    private Integer debitInvoiceNr;
    private String foreignId;
    private String poNr;
    private ClientReference clientRef;
    private ClientInvoiceTemplateReference clientInvoiceTemplateRef;
    private String yourReference;
    private UserReference ourReference;
    private UserReference approver;
    private Address invoiceAddress;
    private String invoiceEmail;
    private String deliveryName;
    private Address deliveryAddress;
    private String deliveryEmail;
    private Date invoiceDate;
    private Date dueDate;
    private Date deliveryDate;
    private Period period;
    private String currency;
    private BigDecimal currencyRate;
    private boolean certified;
    private boolean sent;
    private String notes;
    private String deliveryType;
    private String gln;
    private long remaining;
    private boolean disabled;
    private Fields fields;
    private Files files;
    private List<ClientInvoiceRow> rowList = new ArrayList<ClientInvoiceRow>();

    /**
     * Get the 'id' element value.
     *
     * @return value
     */
    public int getId(){
        return id;
    }

    /**
     * Set the 'id' element value.
     *
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Get the 'invoice-nr' element value.
     *
     * @return value
     */
    public int getInvoiceNr(){
        return invoiceNr;
    }

    /**
     * Set the 'invoice-nr' element value.
     *
     * @param invoiceNr
     */
    public void setInvoiceNr(int invoiceNr){
        this.invoiceNr = invoiceNr;
    }

    /**
     * Get the 'debit-invoice-nr' element value.
     *
     * @return value
     */
    public Integer getDebitInvoiceNr(){
        return debitInvoiceNr;
    }

    /**
     * Set the 'debit-invoice-nr' element value.
     *
     * @param debitInvoiceNr
     */
    public void setDebitInvoiceNr(Integer debitInvoiceNr){
        this.debitInvoiceNr = debitInvoiceNr;
    }

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
     * Get the 'po-nr' element value.
     *
     * @return value
     */
    public String getPoNr(){
        return poNr;
    }

    /**
     * Set the 'po-nr' element value.
     *
     * @param poNr
     */
    public void setPoNr(String poNr){
        this.poNr = poNr;
    }

    /**
     * Get the 'client-ref' element value.
     *
     * @return value
     */
    public ClientReference getClientRef(){
        return clientRef;
    }

    /**
     * Set the 'client-ref' element value.
     *
     * @param clientRef
     */
    public void setClientRef(ClientReference clientRef){
        this.clientRef = clientRef;
    }

    /**
     * Get the 'client-invoice-template-ref' element value.
     *
     * @return value
     */
    public ClientInvoiceTemplateReference getClientInvoiceTemplateRef(){
        return clientInvoiceTemplateRef;
    }

    /**
     * Set the 'client-invoice-template-ref' element value.
     *
     * @param clientInvoiceTemplateRef
     */
    public void setClientInvoiceTemplateRef(
            ClientInvoiceTemplateReference clientInvoiceTemplateRef
    ){
        this.clientInvoiceTemplateRef = clientInvoiceTemplateRef;
    }

    /**
     * Get the 'your-reference' element value.
     *
     * @return value
     */
    public String getYourReference(){
        return yourReference;
    }

    /**
     * Set the 'your-reference' element value.
     *
     * @param yourReference
     */
    public void setYourReference(String yourReference){
        this.yourReference = yourReference;
    }

    /**
     * Get the 'our-reference' element value.
     *
     * @return value
     */
    public UserReference getOurReference(){
        return ourReference;
    }

    /**
     * Set the 'our-reference' element value.
     *
     * @param ourReference
     */
    public void setOurReference(UserReference ourReference){
        this.ourReference = ourReference;
    }

    /**
     * Get the 'approver' element value.
     *
     * @return value
     */
    public UserReference getApprover(){
        return approver;
    }

    /**
     * Set the 'approver' element value.
     *
     * @param approver
     */
    public void setApprover(UserReference approver){
        this.approver = approver;
    }

    /**
     * Get the 'invoice-address' element value.
     *
     * @return value
     */
    public Address getInvoiceAddress(){
        return invoiceAddress;
    }

    /**
     * Set the 'invoice-address' element value.
     *
     * @param invoiceAddress
     */
    public void setInvoiceAddress(Address invoiceAddress){
        this.invoiceAddress = invoiceAddress;
    }

    /**
     * Get the 'invoice-email' element value.
     *
     * @return value
     */
    public String getInvoiceEmail(){
        return invoiceEmail;
    }

    /**
     * Set the 'invoice-email' element value.
     *
     * @param invoiceEmail
     */
    public void setInvoiceEmail(String invoiceEmail){
        this.invoiceEmail = invoiceEmail;
    }

    /**
     * Get the 'delivery-name' element value.
     *
     * @return value
     */
    public String getDeliveryName(){
        return deliveryName;
    }

    /**
     * Set the 'delivery-name' element value.
     *
     * @param deliveryName
     */
    public void setDeliveryName(String deliveryName){
        this.deliveryName = deliveryName;
    }

    /**
     * Get the 'delivery-address' element value.
     *
     * @return value
     */
    public Address getDeliveryAddress(){
        return deliveryAddress;
    }

    /**
     * Set the 'delivery-address' element value.
     *
     * @param deliveryAddress
     */
    public void setDeliveryAddress(Address deliveryAddress){
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * Get the 'delivery-email' element value.
     *
     * @return value
     */
    public String getDeliveryEmail(){
        return deliveryEmail;
    }

    /**
     * Set the 'delivery-email' element value.
     *
     * @param deliveryEmail
     */
    public void setDeliveryEmail(String deliveryEmail){
        this.deliveryEmail = deliveryEmail;
    }

    /**
     * Get the 'invoice-date' element value.
     *
     * @return value
     */
    public Date getInvoiceDate(){
        return invoiceDate;
    }

    /**
     * Set the 'invoice-date' element value.
     *
     * @param invoiceDate
     */
    public void setInvoiceDate(Date invoiceDate){
        this.invoiceDate = invoiceDate;
    }

    /**
     * Get the 'due-date' element value.
     *
     * @return value
     */
    public Date getDueDate(){
        return dueDate;
    }

    /**
     * Set the 'due-date' element value.
     *
     * @param dueDate
     */
    public void setDueDate(Date dueDate){
        this.dueDate = dueDate;
    }

    /**
     * Get the 'delivery-date' element value.
     *
     * @return value
     */
    public Date getDeliveryDate(){
        return deliveryDate;
    }

    /**
     * Set the 'delivery-date' element value.
     *
     * @param deliveryDate
     */
    public void setDeliveryDate(Date deliveryDate){
        this.deliveryDate = deliveryDate;
    }

    /**
     * Get the 'period' element value.
     *
     * @return value
     */
    public Period getPeriod(){
        return period;
    }

    /**
     * Set the 'period' element value.
     *
     * @param period
     */
    public void setPeriod(Period period){
        this.period = period;
    }

    /**
     * Get the 'currency' element value.
     *
     * @return value
     */
    public String getCurrency(){
        return currency;
    }

    /**
     * Set the 'currency' element value.
     *
     * @param currency
     */
    public void setCurrency(String currency){
        this.currency = currency;
    }

    /**
     * Get the 'currency-rate' element value.
     *
     * @return value
     */
    public BigDecimal getCurrencyRate(){
        return currencyRate;
    }

    /**
     * Set the 'currency-rate' element value.
     *
     * @param currencyRate
     */
    public void setCurrencyRate(BigDecimal currencyRate){
        this.currencyRate = currencyRate;
    }

    /**
     * Get the 'certified' element value.
     *
     * @return value
     */
    public boolean isCertified(){
        return certified;
    }

    /**
     * Set the 'certified' element value.
     *
     * @param certified
     */
    public void setCertified(boolean certified){
        this.certified = certified;
    }

    /**
     * Get the 'sent' element value.
     *
     * @return value
     */
    public boolean isSent(){
        return sent;
    }

    /**
     * Set the 'sent' element value.
     *
     * @param sent
     */
    public void setSent(boolean sent){
        this.sent = sent;
    }

    /**
     * Get the 'notes' element value.
     *
     * @return value
     */
    public String getNotes(){
        return notes;
    }

    /**
     * Set the 'notes' element value.
     *
     * @param notes
     */
    public void setNotes(String notes){
        this.notes = notes;
    }

    /**
     * Get the 'delivery-type' element value.
     *
     * @return value
     */
    public String getDeliveryType(){
        return deliveryType;
    }

    /**
     * Set the 'delivery-type' element value.
     *
     * @param deliveryType
     */
    public void setDeliveryType(String deliveryType){
        this.deliveryType = deliveryType;
    }

    /**
     * Get the 'gln' element value.
     *
     * @return value
     */
    public String getGln(){
        return gln;
    }

    /**
     * Set the 'gln' element value.
     *
     * @param gln
     */
    public void setGln(String gln){
        this.gln = gln;
    }

    /**
     * Get the 'remaining' element value.
     *
     * @return value
     */
    public long getRemaining(){
        return remaining;
    }

    /**
     * Set the 'remaining' element value.
     *
     * @param remaining
     */
    public void setRemaining(long remaining){
        this.remaining = remaining;
    }

    /**
     * Get the 'disabled' element value.
     *
     * @return value
     */
    public boolean isDisabled(){
        return disabled;
    }

    /**
     * Set the 'disabled' element value.
     *
     * @param disabled
     */
    public void setDisabled(boolean disabled){
        this.disabled = disabled;
    }

    /**
     * Get the 'fields' element value.
     *
     * @return value
     */
    public Fields getFields(){
        return fields;
    }

    /**
     * Set the 'fields' element value.
     *
     * @param fields
     */
    public void setFields(Fields fields){
        this.fields = fields;
    }

    /**
     * Get the 'files' element value.
     *
     * @return value
     */
    public Files getFiles(){
        return files;
    }

    /**
     * Set the 'files' element value.
     *
     * @param files
     */
    public void setFiles(Files files){
        this.files = files;
    }

    /**
     * Get the list of 'row' element items.
     *
     * @return list
     */
    public List<ClientInvoiceRow> getRowList(){
        return rowList;
    }

    /**
     * Set the list of 'row' element items.
     *
     * @param list
     */
    public void setRowList(List<ClientInvoiceRow> list){
        rowList = list;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="period" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:date" name="start-date"/>
     *       &lt;xs:element type="xs:date" name="end-date"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Period{
        private Date startDate;
        private Date endDate;

        /**
         * Get the 'start-date' element value.
         *
         * @return value
         */
        public Date getStartDate(){
            return startDate;
        }

        /**
         * Set the 'start-date' element value.
         *
         * @param startDate
         */
        public void setStartDate(Date startDate){
            this.startDate = startDate;
        }

        /**
         * Get the 'end-date' element value.
         *
         * @return value
         */
        public Date getEndDate(){
            return endDate;
        }

        /**
         * Set the 'end-date' element value.
         *
         * @param endDate
         */
        public void setEndDate(Date endDate){
            this.endDate = endDate;
        }
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="fields" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="field" name="field" minOccurs="0" maxOccurs="unbounded"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Fields{
        private List<Field> fieldList = new ArrayList<Field>();

        /**
         * Get the list of 'field' element items.
         *
         * @return list
         */
        public List<Field> getFieldList(){
            return fieldList;
        }

        /**
         * Set the list of 'field' element items.
         *
         * @param list
         */
        public void setFieldList(List<Field> list){
            fieldList = list;
        }
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="files" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element name="file" minOccurs="0" maxOccurs="unbounded">
     *         &lt;!-- Reference to inner class File -->
     *       &lt;/xs:element>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Files{
        private List<File> fileList = new ArrayList<File>();

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
         * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="file" minOccurs="0"
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
}
