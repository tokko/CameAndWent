package com.tokko.cameandwentpeaccounting.classes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-run">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="xs:int" name="nr" minOccurs="0"/>
 *     &lt;xs:element type="xs:date" name="payment-date"/>
 *     &lt;xs:element name="payouts">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element name="payout" minOccurs="0" maxOccurs="unbounded">
 *             &lt;!-- Reference to inner class Payout -->
 *           &lt;/xs:element>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *     &lt;xs:element name="paid" minOccurs="0">
 *       &lt;!-- Reference to inner class Paid -->
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollRun{
    private int id;
    private Integer nr;
    private Date paymentDate;
    private List<Payout> payoutList = new ArrayList<Payout>();
    private Paid paid;

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
     * Get the 'nr' element value.
     *
     * @return value
     */
    public Integer getNr(){
        return nr;
    }

    /**
     * Set the 'nr' element value.
     *
     * @param nr
     */
    public void setNr(Integer nr){
        this.nr = nr;
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
     * Get the list of 'payout' element items.
     *
     * @return list
     */
    public List<Payout> getPayoutList(){
        return payoutList;
    }

    /**
     * Set the list of 'payout' element items.
     *
     * @param list
     */
    public void setPayoutList(List<Payout> list){
        payoutList = list;
    }

    /**
     * Get the 'paid' element value.
     *
     * @return value
     */
    public Paid getPaid(){
        return paid;
    }

    /**
     * Set the 'paid' element value.
     *
     * @param paid
     */
    public void setPaid(Paid paid){
        this.paid = paid;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payout" minOccurs="0"
     * maxOccurs="unbounded">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="user-reference" name="user"/>
     *       &lt;xs:element type="project-reference" name="project" minOccurs="0"/>
     *       &lt;xs:element type="xs:date" name="period-start"/>
     *       &lt;xs:element type="xs:date" name="period-end"/>
     *       &lt;xs:element type="xs:long" name="gross"/>
     *       &lt;xs:element type="xs:long" name="benefits"/>
     *       &lt;xs:element type="xs:long" name="payout"/>
     *       &lt;xs:element type="xs:long" name="tax"/>
     *       &lt;xs:element type="xs:long" name="social-fees"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Payout{
        private UserReference user;
        private ProjectReference project;
        private Date periodStart;
        private Date periodEnd;
        private Long gross;
        private Long benefits;
        private Long payout;
        private Long tax;
        private Long socialFees;

        /**
         * Get the 'user' element value.
         *
         * @return value
         */
        public UserReference getUser(){
            return user;
        }

        /**
         * Set the 'user' element value.
         *
         * @param user
         */
        public void setUser(UserReference user){
            this.user = user;
        }

        /**
         * Get the 'project' element value.
         *
         * @return value
         */
        public ProjectReference getProject(){
            return project;
        }

        /**
         * Set the 'project' element value.
         *
         * @param project
         */
        public void setProject(ProjectReference project){
            this.project = project;
        }

        /**
         * Get the 'period-start' element value.
         *
         * @return value
         */
        public Date getPeriodStart(){
            return periodStart;
        }

        /**
         * Set the 'period-start' element value.
         *
         * @param periodStart
         */
        public void setPeriodStart(Date periodStart){
            this.periodStart = periodStart;
        }

        /**
         * Get the 'period-end' element value.
         *
         * @return value
         */
        public Date getPeriodEnd(){
            return periodEnd;
        }

        /**
         * Set the 'period-end' element value.
         *
         * @param periodEnd
         */
        public void setPeriodEnd(Date periodEnd){
            this.periodEnd = periodEnd;
        }

        /**
         * Get the 'gross' element value.
         *
         * @return value
         */
        public Long getGross(){
            return gross;
        }

        /**
         * Set the 'gross' element value.
         *
         * @param gross
         */
        public void setGross(Long gross){
            this.gross = gross;
        }

        /**
         * Get the 'benefits' element value.
         *
         * @return value
         */
        public Long getBenefits(){
            return benefits;
        }

        /**
         * Set the 'benefits' element value.
         *
         * @param benefits
         */
        public void setBenefits(Long benefits){
            this.benefits = benefits;
        }

        /**
         * Get the 'payout' element value.
         *
         * @return value
         */
        public Long getPayout(){
            return payout;
        }

        /**
         * Set the 'payout' element value.
         *
         * @param payout
         */
        public void setPayout(Long payout){
            this.payout = payout;
        }

        /**
         * Get the 'tax' element value.
         *
         * @return value
         */
        public Long getTax(){
            return tax;
        }

        /**
         * Set the 'tax' element value.
         *
         * @param tax
         */
        public void setTax(Long tax){
            this.tax = tax;
        }

        /**
         * Get the 'social-fees' element value.
         *
         * @return value
         */
        public Long getSocialFees(){
            return socialFees;
        }

        /**
         * Set the 'social-fees' element value.
         *
         * @param socialFees
         */
        public void setSocialFees(Long socialFees){
            this.socialFees = socialFees;
        }
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="paid" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="user-reference" name="user"/>
     *       &lt;xs:element type="xs:dateTime" name="datetime"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Paid{
        private UserReference user;
        private java.util.Date datetime;

        /**
         * Get the 'user' element value.
         *
         * @return value
         */
        public UserReference getUser(){
            return user;
        }

        /**
         * Set the 'user' element value.
         *
         * @param user
         */
        public void setUser(UserReference user){
            this.user = user;
        }

        /**
         * Get the 'datetime' element value.
         *
         * @return value
         */
        public java.util.Date getDatetime(){
            return datetime;
        }

        /**
         * Set the 'datetime' element value.
         *
         * @param datetime
         */
        public void setDatetime(java.util.Date datetime){
            this.datetime = datetime;
        }
    }
}
