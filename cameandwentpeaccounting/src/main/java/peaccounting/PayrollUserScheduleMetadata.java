package peaccounting;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-user-schedule-metadata">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:date" name="date"/>
 *     &lt;xs:element type="xs:decimal" name="level-of-employment"/>
 *     &lt;xs:element type="xs:decimal" name="gross-hours"/>
 *     &lt;xs:element type="xs:decimal" name="net-hours"/>
 *     &lt;xs:element type="xs:decimal" name="actual-hours"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollUserScheduleMetadata{
    private Date date;
    private BigDecimal levelOfEmployment;
    private BigDecimal grossHours;
    private BigDecimal netHours;
    private BigDecimal actualHours;

    /**
     * Get the 'date' element value.
     *
     * @return value
     */
    public Date getDate(){
        return date;
    }

    /**
     * Set the 'date' element value.
     *
     * @param date
     */
    public void setDate(Date date){
        this.date = date;
    }

    /**
     * Get the 'level-of-employment' element value.
     *
     * @return value
     */
    public BigDecimal getLevelOfEmployment(){
        return levelOfEmployment;
    }

    /**
     * Set the 'level-of-employment' element value.
     *
     * @param levelOfEmployment
     */
    public void setLevelOfEmployment(BigDecimal levelOfEmployment){
        this.levelOfEmployment = levelOfEmployment;
    }

    /**
     * Get the 'gross-hours' element value.
     *
     * @return value
     */
    public BigDecimal getGrossHours(){
        return grossHours;
    }

    /**
     * Set the 'gross-hours' element value.
     *
     * @param grossHours
     */
    public void setGrossHours(BigDecimal grossHours){
        this.grossHours = grossHours;
    }

    /**
     * Get the 'net-hours' element value.
     *
     * @return value
     */
    public BigDecimal getNetHours(){
        return netHours;
    }

    /**
     * Set the 'net-hours' element value.
     *
     * @param netHours
     */
    public void setNetHours(BigDecimal netHours){
        this.netHours = netHours;
    }

    /**
     * Get the 'actual-hours' element value.
     *
     * @return value
     */
    public BigDecimal getActualHours(){
        return actualHours;
    }

    /**
     * Set the 'actual-hours' element value.
     *
     * @param actualHours
     */
    public void setActualHours(BigDecimal actualHours){
        this.actualHours = actualHours;
    }
}
