package com.tokko.cameandwentpeaccounting.classes;

import java.math.BigDecimal;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="project-metadata">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="xs:string" name="name"/>
 *     &lt;xs:element type="xs:long" name="revenue"/>
 *     &lt;xs:element type="xs:long" name="cost"/>
 *     &lt;xs:element type="xs:long" name="profit"/>
 *     &lt;xs:element type="xs:decimal" name="margin"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ProjectMetadata{
    private int id;
    private String name;
    private long revenue;
    private long cost;
    private long profit;
    private BigDecimal margin;

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
     * Get the 'name' element value.
     *
     * @return value
     */
    public String getName(){
        return name;
    }

    /**
     * Set the 'name' element value.
     *
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Get the 'revenue' element value.
     *
     * @return value
     */
    public long getRevenue(){
        return revenue;
    }

    /**
     * Set the 'revenue' element value.
     *
     * @param revenue
     */
    public void setRevenue(long revenue){
        this.revenue = revenue;
    }

    /**
     * Get the 'cost' element value.
     *
     * @return value
     */
    public long getCost(){
        return cost;
    }

    /**
     * Set the 'cost' element value.
     *
     * @param cost
     */
    public void setCost(long cost){
        this.cost = cost;
    }

    /**
     * Get the 'profit' element value.
     *
     * @return value
     */
    public long getProfit(){
        return profit;
    }

    /**
     * Set the 'profit' element value.
     *
     * @param profit
     */
    public void setProfit(long profit){
        this.profit = profit;
    }

    /**
     * Get the 'margin' element value.
     *
     * @return value
     */
    public BigDecimal getMargin(){
        return margin;
    }

    /**
     * Set the 'margin' element value.
     *
     * @param margin
     */
    public void setMargin(BigDecimal margin){
        this.margin = margin;
    }
}
