package com.tokko.cameandwentpeaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-article-readable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="payroll-article-reference" name="id"/>
 *     &lt;xs:element type="xs:string" name="name"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollArticleReadable{
    private PayrollArticleReference id;
    private String name;

    /**
     * Get the 'id' element value.
     *
     * @return value
     */
    public PayrollArticleReference getId(){
        return id;
    }

    /**
     * Set the 'id' element value.
     *
     * @param id
     */
    public void setId(PayrollArticleReference id){
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
}
