package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-run-create">
 *   &lt;xs:sequence>
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
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollRunCreate{
    private Date paymentDate;
    private List<Payout> payoutList = new ArrayList<Payout>();

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
     *       &lt;xs:element name="articles">
     *         &lt;xs:complexType>
     *           &lt;xs:sequence>
     *             &lt;xs:element type="payroll-payout-article" name="benefit" minOccurs="0"
     *             maxOccurs="unbounded"/>
     *             &lt;xs:element type="payroll-payout-article" name="salary" minOccurs="0"
     *             maxOccurs="unbounded"/>
     *             &lt;xs:element type="payroll-payout-article" name="provision" minOccurs="0"
     *             maxOccurs="unbounded"/>
     *           &lt;/xs:sequence>
     *         &lt;/xs:complexType>
     *       &lt;/xs:element>
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
        private List<PayrollPayoutArticle> articlesBenefitList = new
                ArrayList<PayrollPayoutArticle>();
        private List<PayrollPayoutArticle> articlesSalaryList = new
                ArrayList<PayrollPayoutArticle>();
        private List<PayrollPayoutArticle> articlesProvisionList = new ArrayList<PayrollPayoutArticle>();

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
         * Get the list of 'benefit' element items.
         *
         * @return list
         */
        public List<PayrollPayoutArticle> getArticlesBenefitList(){
            return articlesBenefitList;
        }

        /**
         * Set the list of 'benefit' element items.
         *
         * @param list
         */
        public void setArticlesBenefitList(List<PayrollPayoutArticle> list){
            articlesBenefitList = list;
        }

        /**
         * Get the list of 'salary' element items.
         *
         * @return list
         */
        public List<PayrollPayoutArticle> getArticlesSalaryList(){
            return articlesSalaryList;
        }

        /**
         * Set the list of 'salary' element items.
         *
         * @param list
         */
        public void setArticlesSalaryList(List<PayrollPayoutArticle> list){
            articlesSalaryList = list;
        }

        /**
         * Get the list of 'provision' element items.
         *
         * @return list
         */
        public List<PayrollPayoutArticle> getArticlesProvisionList(){
            return articlesProvisionList;
        }

        /**
         * Set the list of 'provision' element items.
         *
         * @param list
         */
        public void setArticlesProvisionList(List<PayrollPayoutArticle> list){
            articlesProvisionList = list;
        }
    }
}
