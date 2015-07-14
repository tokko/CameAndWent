package peaccounting;

import java.sql.Date;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-user-schedule">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="payroll-schedule-reference" name="schedule"/>
 *     &lt;xs:element type="xs:date" name="start-date" minOccurs="0"/>
 *     &lt;xs:element type="xs:date" name="end-date" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="comment"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollUserSchedule{
    private PayrollScheduleReference schedule;
    private Date startDate;
    private Date endDate;
    private String comment;

    /**
     * Get the 'schedule' element value.
     *
     * @return value
     */
    public PayrollScheduleReference getSchedule(){
        return schedule;
    }

    /**
     * Set the 'schedule' element value.
     *
     * @param schedule
     */
    public void setSchedule(PayrollScheduleReference schedule){
        this.schedule = schedule;
    }

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

    /**
     * Get the 'comment' element value.
     *
     * @return value
     */
    public String getComment(){
        return comment;
    }

    /**
     * Set the 'comment' element value.
     *
     * @param comment
     */
    public void setComment(String comment){
        this.comment = comment;
    }
}
