package peaccounting;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="client-invoice-row">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:decimal" name="quantity"/>
 *     &lt;xs:element type="xs:long" name="price"/>
 *     &lt;xs:element type="xs:decimal" name="vat"/>
 *     &lt;xs:element type="product-reference" name="product"/>
 *     &lt;xs:element type="xs:string" name="unit"/>
 *     &lt;xs:element type="xs:string" name="description"/>
 *     &lt;xs:element type="xs:int" name="account-nr"/>
 *     &lt;xs:element type="project-reference" name="project" minOccurs="0"/>
 *     &lt;xs:element name="period" minOccurs="0">
 *       &lt;!-- Reference to inner class Period -->
 *     &lt;/xs:element>
 *     &lt;xs:element type="xs:date" name="delivery-date" minOccurs="0"/>
 *     &lt;xs:element name="discount" minOccurs="0">
 *       &lt;!-- Reference to inner class Discount -->
 *     &lt;/xs:element>
 *     &lt;xs:element name="fields" minOccurs="0">
 *       &lt;!-- Reference to inner class Fields -->
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ClientInvoiceRow{
    private BigDecimal quantity;
    private long price;
    private BigDecimal vat;
    private ProductReference product;
    private String unit;
    private String description;
    private int accountNr;
    private ProjectReference project;
    private Period period;
    private Date deliveryDate;
    private Discount discount;
    private Fields fields;

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
     * Get the 'vat' element value.
     *
     * @return value
     */
    public BigDecimal getVat(){
        return vat;
    }

    /**
     * Set the 'vat' element value.
     *
     * @param vat
     */
    public void setVat(BigDecimal vat){
        this.vat = vat;
    }

    /**
     * Get the 'product' element value.
     *
     * @return value
     */
    public ProductReference getProduct(){
        return product;
    }

    /**
     * Set the 'product' element value.
     *
     * @param product
     */
    public void setProduct(ProductReference product){
        this.product = product;
    }

    /**
     * Get the 'unit' element value.
     *
     * @return value
     */
    public String getUnit(){
        return unit;
    }

    /**
     * Set the 'unit' element value.
     *
     * @param unit
     */
    public void setUnit(String unit){
        this.unit = unit;
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
     * Get the 'account-nr' element value.
     *
     * @return value
     */
    public int getAccountNr(){
        return accountNr;
    }

    /**
     * Set the 'account-nr' element value.
     *
     * @param accountNr
     */
    public void setAccountNr(int accountNr){
        this.accountNr = accountNr;
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
     * Get the 'discount' element value.
     *
     * @return value
     */
    public Discount getDiscount(){
        return discount;
    }

    /**
     * Set the 'discount' element value.
     *
     * @param discount
     */
    public void setDiscount(Discount discount){
        this.discount = discount;
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
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="discount" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:choice>
     *       &lt;xs:element type="xs:long" name="absolute"/>
     *       &lt;xs:element type="xs:decimal" name="percentage"/>
     *     &lt;/xs:choice>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Discount{
        private static final int ABSOLUTE_CHOICE = 0;
        private static final int PERCENTAGE_CHOICE = 1;
        private int discountSelect = -1;
        private long absolute;
        private BigDecimal percentage;

        /**
         * Clear the choice selection.
         */
        public void clearDiscountSelect(){
            discountSelect = -1;
        }

        /**
         * Check if Absolute is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifAbsolute(){
            return discountSelect == ABSOLUTE_CHOICE;
        }

        /**
         * Get the 'absolute' element value.
         *
         * @return value
         */
        public long getAbsolute(){
            return absolute;
        }

        /**
         * Set the 'absolute' element value.
         *
         * @param absolute
         */
        public void setAbsolute(long absolute){
            setDiscountSelect(ABSOLUTE_CHOICE);
            this.absolute = absolute;
        }

        private void setDiscountSelect(int choice){
            if(discountSelect == -1){
                discountSelect = choice;
            }
            else if(discountSelect != choice){
                throw new IllegalStateException(
                        "Need to call clearDiscountSelect() before changing existing choice");
            }
        }

        /**
         * Check if Percentage is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifPercentage(){
            return discountSelect == PERCENTAGE_CHOICE;
        }

        /**
         * Get the 'percentage' element value.
         *
         * @return value
         */
        public BigDecimal getPercentage(){
            return percentage;
        }

        /**
         * Set the 'percentage' element value.
         *
         * @param percentage
         */
        public void setPercentage(BigDecimal percentage){
            setDiscountSelect(PERCENTAGE_CHOICE);
            this.percentage = percentage;
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
