package com.tokko.cameandwentpeaccounting.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-month-report-readable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="user-reference" name="user"/>
 *     &lt;xs:element type="xs:int" name="year"/>
 *     &lt;xs:element type="xs:int" name="month"/>
 *     &lt;xs:element name="provisions">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element type="payroll-month-report-provision" name="provision" minOccurs="0"
 *           maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *     &lt;xs:element type="xs:string" name="notes"/>
 *     &lt;xs:element type="xs:long" name="gross"/>
 *     &lt;xs:element type="xs:long" name="benefits"/>
 *     &lt;xs:element type="xs:long" name="payout"/>
 *     &lt;xs:element type="xs:long" name="tax"/>
 *     &lt;xs:element type="xs:long" name="social-fees"/>
 *     &lt;xs:element name="articles">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element type="payroll-month-report-article" name="article" minOccurs="0"
 *           maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *     &lt;xs:element name="approved" minOccurs="0">
 *       &lt;!-- Reference to inner class Approved -->
 *     &lt;/xs:element>
 *     &lt;xs:element name="certified" minOccurs="0">
 *       &lt;!-- Reference to inner class Certified -->
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollMonthReportReadable{
    private UserReference user;
    private int year;
    private int month;
    private List<PayrollMonthReportProvision> provisionList = new
            ArrayList<PayrollMonthReportProvision>();
    private String notes;
    private long gross;
    private long benefits;
    private long payout;
    private long tax;
    private long socialFees;
    private List<PayrollMonthReportArticle> articleList = new
            ArrayList<PayrollMonthReportArticle>();
    private Approved approved;
    private Certified certified;

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
     * Get the 'year' element value.
     *
     * @return value
     */
    public int getYear(){
        return year;
    }

    /**
     * Set the 'year' element value.
     *
     * @param year
     */
    public void setYear(int year){
        this.year = year;
    }

    /**
     * Get the 'month' element value.
     *
     * @return value
     */
    public int getMonth(){
        return month;
    }

    /**
     * Set the 'month' element value.
     *
     * @param month
     */
    public void setMonth(int month){
        this.month = month;
    }

    /**
     * Get the list of 'provision' element items.
     *
     * @return list
     */
    public List<PayrollMonthReportProvision> getProvisionList(){
        return provisionList;
    }

    /**
     * Set the list of 'provision' element items.
     *
     * @param list
     */
    public void setProvisionList(List<PayrollMonthReportProvision> list){
        provisionList = list;
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
     * Get the 'gross' element value.
     *
     * @return value
     */
    public long getGross(){
        return gross;
    }

    /**
     * Set the 'gross' element value.
     *
     * @param gross
     */
    public void setGross(long gross){
        this.gross = gross;
    }

    /**
     * Get the 'benefits' element value.
     *
     * @return value
     */
    public long getBenefits(){
        return benefits;
    }

    /**
     * Set the 'benefits' element value.
     *
     * @param benefits
     */
    public void setBenefits(long benefits){
        this.benefits = benefits;
    }

    /**
     * Get the 'payout' element value.
     *
     * @return value
     */
    public long getPayout(){
        return payout;
    }

    /**
     * Set the 'payout' element value.
     *
     * @param payout
     */
    public void setPayout(long payout){
        this.payout = payout;
    }

    /**
     * Get the 'tax' element value.
     *
     * @return value
     */
    public long getTax(){
        return tax;
    }

    /**
     * Set the 'tax' element value.
     *
     * @param tax
     */
    public void setTax(long tax){
        this.tax = tax;
    }

    /**
     * Get the 'social-fees' element value.
     *
     * @return value
     */
    public long getSocialFees(){
        return socialFees;
    }

    /**
     * Set the 'social-fees' element value.
     *
     * @param socialFees
     */
    public void setSocialFees(long socialFees){
        this.socialFees = socialFees;
    }

    /**
     * Get the list of 'article' element items.
     *
     * @return list
     */
    public List<PayrollMonthReportArticle> getArticleList(){
        return articleList;
    }

    /**
     * Set the list of 'article' element items.
     *
     * @param list
     */
    public void setArticleList(List<PayrollMonthReportArticle> list){
        articleList = list;
    }

    /**
     * Get the 'approved' element value.
     *
     * @return value
     */
    public Approved getApproved(){
        return approved;
    }

    /**
     * Set the 'approved' element value.
     *
     * @param approved
     */
    public void setApproved(Approved approved){
        this.approved = approved;
    }

    /**
     * Get the 'certified' element value.
     *
     * @return value
     */
    public Certified getCertified(){
        return certified;
    }

    /**
     * Set the 'certified' element value.
     *
     * @param certified
     */
    public void setCertified(Certified certified){
        this.certified = certified;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="approved" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="user-reference" name="user"/>
     *       &lt;xs:element type="xs:dateTime" name="datetime"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Approved{
        private UserReference user;
        private Date datetime;

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
        public Date getDatetime(){
            return datetime;
        }

        /**
         * Set the 'datetime' element value.
         *
         * @param datetime
         */
        public void setDatetime(Date datetime){
            this.datetime = datetime;
        }
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="certified" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="user-reference" name="user"/>
     *       &lt;xs:element type="xs:dateTime" name="datetime"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Certified{
        private UserReference user;
        private Date datetime;

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
        public Date getDatetime(){
            return datetime;
        }

        /**
         * Set the 'datetime' element value.
         *
         * @param datetime
         */
        public void setDatetime(Date datetime){
            this.datetime = datetime;
        }
    }
}
