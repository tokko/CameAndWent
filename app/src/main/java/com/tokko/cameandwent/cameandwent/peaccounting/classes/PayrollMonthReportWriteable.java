package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-month-report-writeable">
 *   &lt;xs:sequence>
 *     &lt;xs:element name="provisions" minOccurs="0">
 *       &lt;!-- Reference to inner class Provisions -->
 *     &lt;/xs:element>
 *     &lt;xs:element name="articles" minOccurs="0">
 *       &lt;!-- Reference to inner class Articles -->
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollMonthReportWriteable{
    private Provisions provisions;
    private Articles articles;

    /**
     * Get the 'provisions' element value.
     *
     * @return value
     */
    public Provisions getProvisions(){
        return provisions;
    }

    /**
     * Set the 'provisions' element value.
     *
     * @param provisions
     */
    public void setProvisions(Provisions provisions){
        this.provisions = provisions;
    }

    /**
     * Get the 'articles' element value.
     *
     * @return value
     */
    public Articles getArticles(){
        return articles;
    }

    /**
     * Set the 'articles' element value.
     *
     * @param articles
     */
    public void setArticles(Articles articles){
        this.articles = articles;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="provisions" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="payroll-month-report-provision" name="provision" minOccurs="0"
     *       maxOccurs="unbounded"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Provisions{
        private List<PayrollMonthReportProvision> provisionList = new
                ArrayList<PayrollMonthReportProvision>();

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
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="articles" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="payroll-month-report-article" name="article" minOccurs="0"
     *       maxOccurs="unbounded"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Articles{
        private List<PayrollMonthReportArticle> articleList = new
                ArrayList<PayrollMonthReportArticle>();

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
    }
}
