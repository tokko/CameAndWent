package peaccounting;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="supplier-invoice">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="xs:string" name="foreign-id"/>
 *     &lt;xs:element type="supplier-reference" name="supplier-ref"/>
 *     &lt;xs:element type="user-reference" name="your-reference" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="our-reference"/>
 *     &lt;xs:element type="xs:string" name="deposit-account-type"/>
 *     &lt;xs:element type="xs:string" name="deposit-account-nr"/>
 *     &lt;xs:element type="xs:date" name="invoice-date"/>
 *     &lt;xs:element type="xs:date" name="due-date"/>
 *     &lt;xs:element type="xs:long" name="amount"/>
 *     &lt;xs:element type="xs:long" name="vat"/>
 *     &lt;xs:element type="xs:string" name="currency"/>
 *     &lt;xs:element type="xs:decimal" name="currency-rate"/>
 *     &lt;xs:element type="xs:string" name="po-nr"/>
 *     &lt;xs:element type="xs:string" name="reference-nr"/>
 *     &lt;xs:element type="xs:string" name="ocr"/>
 *     &lt;xs:element type="xs:date" name="payment-date"/>
 *     &lt;xs:element type="xs:boolean" name="certified"/>
 *     &lt;xs:element type="supplier-invoice-reminder-type" name="reminder-type"/>
 *     &lt;xs:element type="xs:long" name="remaining"/>
 *     &lt;xs:element name="accounts">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element name="account" minOccurs="0" maxOccurs="unbounded">
 *             &lt;!-- Reference to inner class Account -->
 *           &lt;/xs:element>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *     &lt;xs:element type="xs:string" name="country-code" minOccurs="0"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class SupplierInvoice{
    private int id;
    private String foreignId;
    private SupplierReference supplierRef;
    private UserReference yourReference;
    private String ourReference;
    private String depositAccountType;
    private String depositAccountNr;
    private Date invoiceDate;
    private Date dueDate;
    private long amount;
    private long vat;
    private String currency;
    private BigDecimal currencyRate;
    private String poNr;
    private String referenceNr;
    private String ocr;
    private Date paymentDate;
    private boolean certified;
    private SupplierInvoiceReminderType reminderType;
    private long remaining;
    private List<Account> accountList = new ArrayList<Account>();
    private String countryCode;

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
     * Get the 'supplier-ref' element value.
     *
     * @return value
     */
    public SupplierReference getSupplierRef(){
        return supplierRef;
    }

    /**
     * Set the 'supplier-ref' element value.
     *
     * @param supplierRef
     */
    public void setSupplierRef(SupplierReference supplierRef){
        this.supplierRef = supplierRef;
    }

    /**
     * Get the 'your-reference' element value.
     *
     * @return value
     */
    public UserReference getYourReference(){
        return yourReference;
    }

    /**
     * Set the 'your-reference' element value.
     *
     * @param yourReference
     */
    public void setYourReference(UserReference yourReference){
        this.yourReference = yourReference;
    }

    /**
     * Get the 'our-reference' element value.
     *
     * @return value
     */
    public String getOurReference(){
        return ourReference;
    }

    /**
     * Set the 'our-reference' element value.
     *
     * @param ourReference
     */
    public void setOurReference(String ourReference){
        this.ourReference = ourReference;
    }

    /**
     * Get the 'deposit-account-type' element value.
     *
     * @return value
     */
    public String getDepositAccountType(){
        return depositAccountType;
    }

    /**
     * Set the 'deposit-account-type' element value.
     *
     * @param depositAccountType
     */
    public void setDepositAccountType(String depositAccountType){
        this.depositAccountType = depositAccountType;
    }

    /**
     * Get the 'deposit-account-nr' element value.
     *
     * @return value
     */
    public String getDepositAccountNr(){
        return depositAccountNr;
    }

    /**
     * Set the 'deposit-account-nr' element value.
     *
     * @param depositAccountNr
     */
    public void setDepositAccountNr(String depositAccountNr){
        this.depositAccountNr = depositAccountNr;
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
     * Get the 'vat' element value.
     *
     * @return value
     */
    public long getVat(){
        return vat;
    }

    /**
     * Set the 'vat' element value.
     *
     * @param vat
     */
    public void setVat(long vat){
        this.vat = vat;
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
     * Get the 'reference-nr' element value.
     *
     * @return value
     */
    public String getReferenceNr(){
        return referenceNr;
    }

    /**
     * Set the 'reference-nr' element value.
     *
     * @param referenceNr
     */
    public void setReferenceNr(String referenceNr){
        this.referenceNr = referenceNr;
    }

    /**
     * Get the 'ocr' element value.
     *
     * @return value
     */
    public String getOcr(){
        return ocr;
    }

    /**
     * Set the 'ocr' element value.
     *
     * @param ocr
     */
    public void setOcr(String ocr){
        this.ocr = ocr;
    }

    /**
     * Get the 'payment-date' element value.
     *
     * @return value
     */
    public Date getPaymentDate(){
        return paymentDate;
    }

    /**
     * Set the 'payment-date' element value.
     *
     * @param paymentDate
     */
    public void setPaymentDate(Date paymentDate){
        this.paymentDate = paymentDate;
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
     * Get the 'reminder-type' element value.
     *
     * @return value
     */
    public SupplierInvoiceReminderType getReminderType(){
        return reminderType;
    }

    /**
     * Set the 'reminder-type' element value.
     *
     * @param reminderType
     */
    public void setReminderType(SupplierInvoiceReminderType reminderType){
        this.reminderType = reminderType;
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
     * Get the list of 'account' element items.
     *
     * @return list
     */
    public List<Account> getAccountList(){
        return accountList;
    }

    /**
     * Set the list of 'account' element items.
     *
     * @param list
     */
    public void setAccountList(List<Account> list){
        accountList = list;
    }

    /**
     * Get the 'country-code' element value.
     *
     * @return value
     */
    public String getCountryCode(){
        return countryCode;
    }

    /**
     * Set the 'country-code' element value.
     *
     * @param countryCode
     */
    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="account" minOccurs="0"
     * maxOccurs="unbounded">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:int" name="account-nr"/>
     *       &lt;xs:element type="xs:long" name="amount"/>
     *       &lt;xs:element name="dimension-entries" minOccurs="0">
     *         &lt;!-- Reference to inner class DimensionEntries -->
     *       &lt;/xs:element>
     *       &lt;xs:element name="period" minOccurs="0">
     *         &lt;!-- Reference to inner class Period -->
     *       &lt;/xs:element>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Account{
        private Integer accountNr;
        private Long amount;
        private DimensionEntries dimensionEntries;
        private Period period;

        /**
         * Get the 'account-nr' element value.
         *
         * @return value
         */
        public Integer getAccountNr(){
            return accountNr;
        }

        /**
         * Set the 'account-nr' element value.
         *
         * @param accountNr
         */
        public void setAccountNr(Integer accountNr){
            this.accountNr = accountNr;
        }

        /**
         * Get the 'amount' element value.
         *
         * @return value
         */
        public Long getAmount(){
            return amount;
        }

        /**
         * Set the 'amount' element value.
         *
         * @param amount
         */
        public void setAmount(Long amount){
            this.amount = amount;
        }

        /**
         * Get the 'dimension-entries' element value.
         *
         * @return value
         */
        public DimensionEntries getDimensionEntries(){
            return dimensionEntries;
        }

        /**
         * Set the 'dimension-entries' element value.
         *
         * @param dimensionEntries
         */
        public void setDimensionEntries(DimensionEntries dimensionEntries){
            this.dimensionEntries = dimensionEntries;
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
         * Schema fragment(s) for this class:
         * <pre>
         * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="dimension-entries"
         * minOccurs="0">
         *   &lt;xs:complexType>
         *     &lt;xs:sequence>
         *       &lt;xs:element type="dimension-entry-reference" name="dimension-entry"
         *       minOccurs="0" maxOccurs="unbounded"/>
         *     &lt;/xs:sequence>
         *   &lt;/xs:complexType>
         * &lt;/xs:element>
         * </pre>
         */
        public static class DimensionEntries{
            private List<DimensionEntryReference> dimensionEntryList = new
                    ArrayList<DimensionEntryReference>();

            /**
             * Get the list of 'dimension-entry' element items.
             *
             * @return list
             */
            public List<DimensionEntryReference> getDimensionEntryList(){
                return dimensionEntryList;
            }

            /**
             * Set the list of 'dimension-entry' element items.
             *
             * @param list
             */
            public void setDimensionEntryList(List<DimensionEntryReference> list){
                dimensionEntryList = list;
            }
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
    }
}
