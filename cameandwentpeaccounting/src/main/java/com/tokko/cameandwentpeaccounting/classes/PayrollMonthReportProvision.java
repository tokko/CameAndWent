package com.tokko.cameandwentpeaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-month-report-provision">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:long" name="amount"/>
 *     &lt;xs:element type="xs:string" name="description"/>
 *     &lt;xs:element type="project-reference" name="project" minOccurs="0"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollMonthReportProvision{
    private long amount;
    private String description;
    private ProjectReference project;

    /**
     * Get the 'amount' element value.
     *
     * @return value
     */
    public long getAmount(){
        return amount;
    }

    /**
     * Set the 'amount' element value.
     *
     * @param amount
     */
    public void setAmount(long amount){
        this.amount = amount;
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
