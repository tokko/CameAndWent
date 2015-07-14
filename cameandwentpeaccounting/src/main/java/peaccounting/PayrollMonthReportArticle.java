package peaccounting;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-month-report-article">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="foreign-id"/>
 *     &lt;xs:element type="payroll-article-reference" name="article"/>
 *     &lt;xs:element type="xs:string" name="description"/>
 *     &lt;xs:element type="xs:decimal" name="quantity"/>
 *     &lt;xs:element type="xs:long" name="price"/>
 *     &lt;xs:element type="payroll-unit-type" name="unit"/>
 *     &lt;xs:element name="period" minOccurs="0">
 *       &lt;!-- Reference to inner class Period -->
 *     &lt;/xs:element>
 *     &lt;xs:element type="project-reference" name="project" minOccurs="0"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollMonthReportArticle{
    private String foreignId;
    private PayrollArticleReference article;
    private String description;
    private BigDecimal quantity;
    private long price;
    private PayrollUnitType unit;
    private Period period;
    private ProjectReference project;

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
     * Get the 'article' element value.
     *
     * @return value
     */
    public PayrollArticleReference getArticle(){
        return article;
    }

    /**
     * Set the 'article' element value.
     *
     * @param article
     */
    public void setArticle(PayrollArticleReference article){
        this.article = article;
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
     * Get the 'quantity' element value.
     *
     * @return value
     */
    public BigDecimal getQuantity(){
        return quantity;
    }

    /**
     * Set the 'quantity' element value.
     *
     * @param quantity
     */
    public void setQuantity(BigDecimal quantity){
        this.quantity = quantity;
    }

    /**
     * Get the 'price' element value.
     *
     * @return value
     */
    public long getPrice(){
        return price;
    }

    /**
     * Set the 'price' element value.
     *
     * @param price
     */
    public void setPrice(long price){
        this.price = price;
    }

    /**
     * Get the 'unit' element value.
     *
     * @return value
     */
    public PayrollUnitType getUnit(){
        return unit;
    }

    /**
     * Set the 'unit' element value.
     *
     * @param unit
     */
    public void setUnit(PayrollUnitType unit){
        this.unit = unit;
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
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="period" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:date" name="start"/>
     *       &lt;xs:element type="xs:date" name="end"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Period{
        private Date start;
        private Date end;

        /**
         * Get the 'start' element value.
         *
         * @return value
         */
        public Date getStart(){
            return start;
        }

        /**
         * Set the 'start' element value.
         *
         * @param start
         */
        public void setStart(Date start){
            this.start = start;
        }

        /**
         * Get the 'end' element value.
         *
         * @return value
         */
        public Date getEnd(){
            return end;
        }

        /**
         * Set the 'end' element value.
         *
         * @param end
         */
        public void setEnd(Date end){
            this.end = end;
        }
    }
}
