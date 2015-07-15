package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="voucher-readable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="serie"/>
 *     &lt;xs:element type="xs:int" name="number"/>
 *     &lt;xs:element type="xs:string" name="description"/>
 *     &lt;xs:element type="xs:date" name="date"/>
 *     &lt;xs:element type="xs:long" name="turnover"/>
 *     &lt;xs:element name="transactions">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element name="transaction" minOccurs="0" maxOccurs="unbounded">
 *             &lt;!-- Reference to inner class Transaction -->
 *           &lt;/xs:element>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class VoucherReadable{
    private String serie;
    private int number;
    private String description;
    private Date date;
    private long turnover;
    private List<Transaction> transactionList = new ArrayList<Transaction>();

    /**
     * Get the 'serie' element value.
     *
     * @return value
     */
    public String getSerie(){
        return serie;
    }

    /**
     * Set the 'serie' element value.
     *
     * @param serie
     */
    public void setSerie(String serie){
        this.serie = serie;
    }

    /**
     * Get the 'number' element value.
     *
     * @return value
     */
    public int getNumber(){
        return number;
    }

    /**
     * Set the 'number' element value.
     *
     * @param number
     */
    public void setNumber(int number){
        this.number = number;
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
     * Get the 'turnover' element value.
     *
     * @return value
     */
    public long getTurnover(){
        return turnover;
    }

    /**
     * Set the 'turnover' element value.
     *
     * @param turnover
     */
    public void setTurnover(long turnover){
        this.turnover = turnover;
    }

    /**
     * Get the list of 'transaction' element items.
     *
     * @return list
     */
    public List<Transaction> getTransactionList(){
        return transactionList;
    }

    /**
     * Set the list of 'transaction' element items.
     *
     * @param list
     */
    public void setTransactionList(List<Transaction> list){
        transactionList = list;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="transaction"
     * minOccurs="0" maxOccurs="unbounded">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:int" name="account-nr"/>
     *       &lt;xs:element type="xs:long" name="amount"/>
     *       &lt;xs:element name="dimension-entries">
     *         &lt;xs:complexType>
     *           &lt;xs:sequence>
     *             &lt;xs:element name="dimension-entry" minOccurs="0" maxOccurs="unbounded">
     *               &lt;!-- Reference to inner class DimensionEntry -->
     *             &lt;/xs:element>
     *           &lt;/xs:sequence>
     *         &lt;/xs:complexType>
     *       &lt;/xs:element>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Transaction{
        private Integer accountNr;
        private Long amount;
        private List<DimensionEntry> dimensionEntryList = new ArrayList<DimensionEntry>();

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
         * Get the list of 'dimension-entry' element items.
         *
         * @return list
         */
        public List<DimensionEntry> getDimensionEntryList(){
            return dimensionEntryList;
        }

        /**
         * Set the list of 'dimension-entry' element items.
         *
         * @param list
         */
        public void setDimensionEntryList(List<DimensionEntry> list){
            dimensionEntryList = list;
        }

        /**
         * Schema fragment(s) for this class:
         * <pre>
         * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="dimension-entry"
         * minOccurs="0" maxOccurs="unbounded">
         *   &lt;xs:complexType>
         *     &lt;xs:sequence>
         *       &lt;xs:element type="dimension-entry-reference" name="id"/>
         *       &lt;xs:element type="xs:long" name="amount"/>
         *     &lt;/xs:sequence>
         *   &lt;/xs:complexType>
         * &lt;/xs:element>
         * </pre>
         */
        public static class DimensionEntry{
            private DimensionEntryReference id;
            private Long amount;

            /**
             * Get the 'id' element value.
             *
             * @return value
             */
            public DimensionEntryReference getId(){
                return id;
            }

            /**
             * Set the 'id' element value.
             *
             * @param id
             */
            public void setId(DimensionEntryReference id){
                this.id = id;
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
        }
    }
}
