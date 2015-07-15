package com.tokko.cameandwentpeaccounting.classes;

import java.math.BigDecimal;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-payout-article">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="description"/>
 *     &lt;xs:element type="xs:decimal" name="quantity"/>
 *     &lt;xs:element type="xs:long" name="price"/>
 *     &lt;xs:element type="project-reference" name="project" minOccurs="0"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollPayoutArticle{
    private String description;
    private BigDecimal quantity;
    private long price;
    private ProjectReference project;

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
}
