package peaccounting;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="client-agreement">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="xs:string" name="foreign-id"/>
 *     &lt;xs:element type="xs:int" name="revision-nr"/>
 *     &lt;xs:element type="client-reference" name="client-ref"/>
 *     &lt;xs:element type="xs:string" name="description"/>
 *     &lt;xs:element type="xs:date" name="start-date"/>
 *     &lt;xs:element type="xs:date" name="start-invoice-date"/>
 *     &lt;xs:element type="xs:date" nillable="true" name="end-date"/>
 *     &lt;xs:element type="xs:string" name="your-reference"/>
 *     &lt;xs:element type="user-reference" nillable="true" name="our-reference"/>
 *     &lt;xs:element type="user-reference" nillable="true" name="approver"/>
 *     &lt;xs:element type="xs:string" name="currency"/>
 *     &lt;xs:element type="xs:int" name="payment-days"/>
 *     &lt;xs:element type="xs:int" name="invoice-interval-months"/>
 *     &lt;xs:element type="invoice-day" name="invoice-day"/>
 *     &lt;xs:element type="xs:string" name="po-nr"/>
 *     &lt;xs:element name="accrual-configuration" minOccurs="0">
 *       &lt;!-- Reference to inner class AccrualConfiguration -->
 *     &lt;/xs:element>
 *     &lt;xs:element type="xs:boolean" name="deleted"/>
 *     &lt;xs:element name="fields" minOccurs="0">
 *       &lt;!-- Reference to inner class Fields -->
 *     &lt;/xs:element>
 *     &lt;xs:element name="first-invoice-rows">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element type="client-agreement-row" name="row" minOccurs="0"
 *           maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *     &lt;xs:element name="recurring-rows">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element type="client-agreement-row" name="row" minOccurs="0"
 *           maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ClientAgreement{
    private int id;
    private String foreignId;
    private int revisionNr;
    private ClientReference clientRef;
    private String description;
    private Date startDate;
    private Date startInvoiceDate;
    private Date endDate;
    private String yourReference;
    private UserReference ourReference;
    private UserReference approver;
    private String currency;
    private int paymentDays;
    private int invoiceIntervalMonths;
    private InvoiceDay invoiceDay;
    private String poNr;
    private AccrualConfiguration accrualConfiguration;
    private boolean deleted;
    private Fields fields;
    private List<ClientAgreementRow> firstInvoiceRowList = new ArrayList<ClientAgreementRow>();
    private List<ClientAgreementRow> recurringRowList = new ArrayList<ClientAgreementRow>();

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
     * Get the 'start-invoice-date' element value.
     *
     * @return value
     */
    public Date getStartInvoiceDate(){
        return startInvoiceDate;
    }

    /**
     * Set the 'start-invoice-date' element value.
     *
     * @param startInvoiceDate
     */
    public void setStartInvoiceDate(Date startInvoiceDate){
        this.startInvoiceDate = startInvoiceDate;
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
     * Get the 'payment-days' element value.
     *
     * @return value
     */
    public int getPaymentDays(){
        return paymentDays;
    }

    /**
     * Set the 'payment-days' element value.
     *
     * @param paymentDays
     */
    public void setPaymentDays(int paymentDays){
        this.paymentDays = paymentDays;
    }

    /**
     * Get the 'invoice-interval-months' element value.
     *
     * @return value
     */
    public int getInvoiceIntervalMonths(){
        return invoiceIntervalMonths;
    }

    /**
     * Set the 'invoice-interval-months' element value.
     *
     * @param invoiceIntervalMonths
     */
    public void setInvoiceIntervalMonths(int invoiceIntervalMonths){
        this.invoiceIntervalMonths = invoiceIntervalMonths;
    }

    /**
     * Get the 'invoice-day' element value.
     *
     * @return value
     */
    public InvoiceDay getInvoiceDay(){
        return invoiceDay;
    }

    /**
     * Set the 'invoice-day' element value.
     *
     * @param invoiceDay
     */
    public void setInvoiceDay(InvoiceDay invoiceDay){
        this.invoiceDay = invoiceDay;
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
     * Get the 'accrual-configuration' element value.
     *
     * @return value
     */
    public AccrualConfiguration getAccrualConfiguration(){
        return accrualConfiguration;
    }

    /**
     * Set the 'accrual-configuration' element value.
     *
     * @param accrualConfiguration
     */
    public void setAccrualConfiguration(
            AccrualConfiguration accrualConfiguration
    ){
        this.accrualConfiguration = accrualConfiguration;
    }

    /**
     * Get the 'deleted' element value.
     *
     * @return value
     */
    public boolean isDeleted(){
        return deleted;
    }

    /**
     * Set the 'deleted' element value.
     *
     * @param deleted
     */
    public void setDeleted(boolean deleted){
        this.deleted = deleted;
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
     * Get the list of 'row' element items.
     *
     * @return list
     */
    public List<ClientAgreementRow> getFirstInvoiceRowList(){
        return firstInvoiceRowList;
    }

    /**
     * Set the list of 'row' element items.
     *
     * @param list
     */
    public void setFirstInvoiceRowList(List<ClientAgreementRow> list){
        firstInvoiceRowList = list;
    }

    /**
     * Get the list of 'row' element items.
     *
     * @return list
     */
    public List<ClientAgreementRow> getRecurringRowList(){
        return recurringRowList;
    }

    /**
     * Set the list of 'row' element items.
     *
     * @param list
     */
    public void setRecurringRowList(List<ClientAgreementRow> list){
        recurringRowList = list;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="accrual-configuration"
     * minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:int" name="accrual-account-nr"/>
     *       &lt;xs:element type="xs:int" name="revenue-account-nr"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class AccrualConfiguration{
        private int accrualAccountNr;
        private int revenueAccountNr;

        /**
         * Get the 'accrual-account-nr' element value.
         *
         * @return value
         */
        public int getAccrualAccountNr(){
            return accrualAccountNr;
        }

        /**
         * Set the 'accrual-account-nr' element value.
         *
         * @param accrualAccountNr
         */
        public void setAccrualAccountNr(int accrualAccountNr){
            this.accrualAccountNr = accrualAccountNr;
        }

        /**
         * Get the 'revenue-account-nr' element value.
         *
         * @return value
         */
        public int getRevenueAccountNr(){
            return revenueAccountNr;
        }

        /**
         * Set the 'revenue-account-nr' element value.
         *
         * @param revenueAccountNr
         */
        public void setRevenueAccountNr(int revenueAccountNr){
            this.revenueAccountNr = revenueAccountNr;
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
}
