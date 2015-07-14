package peaccounting;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-user-writable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="ssn" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="address1" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="address2" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="zip-code" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="state" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="country-code" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="phone" minOccurs="0"/>
 *     &lt;xs:element type="xs:int" name="tax-table" minOccurs="0"/>
 *     &lt;xs:element type="xs:int" name="tax-table-column" minOccurs="0"/>
 *     &lt;xs:element type="xs:decimal" name="tax-percentage" minOccurs="0"/>
 *     &lt;xs:element type="xs:decimal" name="one-time-tax-percentage" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="clearing-nr" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="account-nr" minOccurs="0"/>
 *     &lt;xs:element type="xs:date" name="employment-start-date" minOccurs="0"/>
 *     &lt;xs:element name="employment-end-date" minOccurs="0">
 *       &lt;!-- Reference to inner class EmploymentEndDate -->
 *     &lt;/xs:element>
 *     &lt;xs:element type="xs:long" name="travel-reimbursement-per-mile" minOccurs="0"/>
 *     &lt;xs:element type="xs:long" name="monthly-salary" minOccurs="0"/>
 *     &lt;xs:element type="xs:long" name="hourly-salary" minOccurs="0"/>
 *     &lt;xs:element type="xs:boolean" name="provision" minOccurs="0"/>
 *     &lt;xs:element type="payroll-user-month-report-strategy" name="month-report-strategy"
 *     minOccurs="0"/>
 *     &lt;xs:element name="schedules" minOccurs="0">
 *       &lt;!-- Reference to inner class Schedules -->
 *     &lt;/xs:element>
 *     &lt;xs:element name="dimension-entries" minOccurs="0">
 *       &lt;!-- Reference to inner class DimensionEntries -->
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollUserWritable{
    private String ssn;
    private String address1;
    private String address2;
    private String zipCode;
    private String state;
    private String countryCode;
    private String phone;
    private Integer taxTable;
    private Integer taxTableColumn;
    private BigDecimal taxPercentage;
    private BigDecimal oneTimeTaxPercentage;
    private String clearingNr;
    private String accountNr;
    private Date employmentStartDate;
    private EmploymentEndDate employmentEndDate;
    private Long travelReimbursementPerMile;
    private Long monthlySalary;
    private Long hourlySalary;
    private Boolean provision;
    private PayrollUserMonthReportStrategy monthReportStrategy;
    private Schedules schedules;
    private DimensionEntries dimensionEntries;

    /**
     * Get the 'ssn' element value.
     *
     * @return value
     */
    public String getSsn(){
        return ssn;
    }

    /**
     * Set the 'ssn' element value.
     *
     * @param ssn
     */
    public void setSsn(String ssn){
        this.ssn = ssn;
    }

    /**
     * Get the 'address1' element value.
     *
     * @return value
     */
    public String getAddress1(){
        return address1;
    }

    /**
     * Set the 'address1' element value.
     *
     * @param address1
     */
    public void setAddress1(String address1){
        this.address1 = address1;
    }

    /**
     * Get the 'address2' element value.
     *
     * @return value
     */
    public String getAddress2(){
        return address2;
    }

    /**
     * Set the 'address2' element value.
     *
     * @param address2
     */
    public void setAddress2(String address2){
        this.address2 = address2;
    }

    /**
     * Get the 'zip-code' element value.
     *
     * @return value
     */
    public String getZipCode(){
        return zipCode;
    }

    /**
     * Set the 'zip-code' element value.
     *
     * @param zipCode
     */
    public void setZipCode(String zipCode){
        this.zipCode = zipCode;
    }

    /**
     * Get the 'state' element value.
     *
     * @return value
     */
    public String getState(){
        return state;
    }

    /**
     * Set the 'state' element value.
     *
     * @param state
     */
    public void setState(String state){
        this.state = state;
    }

    /**
     * Get the 'country-code' element value.
     *
     * @return value
     */
    public String getCountryCode(){
        return countryCode;
    }

    /**
     * Set the 'country-code' element value.
     *
     * @param countryCode
     */
    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }

    /**
     * Get the 'phone' element value.
     *
     * @return value
     */
    public String getPhone(){
        return phone;
    }

    /**
     * Set the 'phone' element value.
     *
     * @param phone
     */
    public void setPhone(String phone){
        this.phone = phone;
    }

    /**
     * Get the 'tax-table' element value.
     *
     * @return value
     */
    public Integer getTaxTable(){
        return taxTable;
    }

    /**
     * Set the 'tax-table' element value.
     *
     * @param taxTable
     */
    public void setTaxTable(Integer taxTable){
        this.taxTable = taxTable;
    }

    /**
     * Get the 'tax-table-column' element value.
     *
     * @return value
     */
    public Integer getTaxTableColumn(){
        return taxTableColumn;
    }

    /**
     * Set the 'tax-table-column' element value.
     *
     * @param taxTableColumn
     */
    public void setTaxTableColumn(Integer taxTableColumn){
        this.taxTableColumn = taxTableColumn;
    }

    /**
     * Get the 'tax-percentage' element value.
     *
     * @return value
     */
    public BigDecimal getTaxPercentage(){
        return taxPercentage;
    }

    /**
     * Set the 'tax-percentage' element value.
     *
     * @param taxPercentage
     */
    public void setTaxPercentage(BigDecimal taxPercentage){
        this.taxPercentage = taxPercentage;
    }

    /**
     * Get the 'one-time-tax-percentage' element value.
     *
     * @return value
     */
    public BigDecimal getOneTimeTaxPercentage(){
        return oneTimeTaxPercentage;
    }

    /**
     * Set the 'one-time-tax-percentage' element value.
     *
     * @param oneTimeTaxPercentage
     */
    public void setOneTimeTaxPercentage(BigDecimal oneTimeTaxPercentage){
        this.oneTimeTaxPercentage = oneTimeTaxPercentage;
    }

    /**
     * Get the 'clearing-nr' element value.
     *
     * @return value
     */
    public String getClearingNr(){
        return clearingNr;
    }

    /**
     * Set the 'clearing-nr' element value.
     *
     * @param clearingNr
     */
    public void setClearingNr(String clearingNr){
        this.clearingNr = clearingNr;
    }

    /**
     * Get the 'account-nr' element value.
     *
     * @return value
     */
    public String getAccountNr(){
        return accountNr;
    }

    /**
     * Set the 'account-nr' element value.
     *
     * @param accountNr
     */
    public void setAccountNr(String accountNr){
        this.accountNr = accountNr;
    }

    /**
     * Get the 'employment-start-date' element value.
     *
     * @return value
     */
    public Date getEmploymentStartDate(){
        return employmentStartDate;
    }

    /**
     * Set the 'employment-start-date' element value.
     *
     * @param employmentStartDate
     */
    public void setEmploymentStartDate(Date employmentStartDate){
        this.employmentStartDate = employmentStartDate;
    }

    /**
     * Get the 'employment-end-date' element value.
     *
     * @return value
     */
    public EmploymentEndDate getEmploymentEndDate(){
        return employmentEndDate;
    }

    /**
     * Set the 'employment-end-date' element value.
     *
     * @param employmentEndDate
     */
    public void setEmploymentEndDate(EmploymentEndDate employmentEndDate){
        this.employmentEndDate = employmentEndDate;
    }

    /**
     * Get the 'travel-reimbursement-per-mile' element value.
     *
     * @return value
     */
    public Long getTravelReimbursementPerMile(){
        return travelReimbursementPerMile;
    }

    /**
     * Set the 'travel-reimbursement-per-mile' element value.
     *
     * @param travelReimbursementPerMile
     */
    public void setTravelReimbursementPerMile(Long travelReimbursementPerMile){
        this.travelReimbursementPerMile = travelReimbursementPerMile;
    }

    /**
     * Get the 'monthly-salary' element value.
     *
     * @return value
     */
    public Long getMonthlySalary(){
        return monthlySalary;
    }

    /**
     * Set the 'monthly-salary' element value.
     *
     * @param monthlySalary
     */
    public void setMonthlySalary(Long monthlySalary){
        this.monthlySalary = monthlySalary;
    }

    /**
     * Get the 'hourly-salary' element value.
     *
     * @return value
     */
    public Long getHourlySalary(){
        return hourlySalary;
    }

    /**
     * Set the 'hourly-salary' element value.
     *
     * @param hourlySalary
     */
    public void setHourlySalary(Long hourlySalary){
        this.hourlySalary = hourlySalary;
    }

    /**
     * Get the 'provision' element value.
     *
     * @return value
     */
    public Boolean getProvision(){
        return provision;
    }

    /**
     * Set the 'provision' element value.
     *
     * @param provision
     */
    public void setProvision(Boolean provision){
        this.provision = provision;
    }

    /**
     * Get the 'month-report-strategy' element value.
     *
     * @return value
     */
    public PayrollUserMonthReportStrategy getMonthReportStrategy(){
        return monthReportStrategy;
    }

    /**
     * Set the 'month-report-strategy' element value.
     *
     * @param monthReportStrategy
     */
    public void setMonthReportStrategy(
            PayrollUserMonthReportStrategy monthReportStrategy
    ){
        this.monthReportStrategy = monthReportStrategy;
    }

    /**
     * Get the 'schedules' element value.
     *
     * @return value
     */
    public Schedules getSchedules(){
        return schedules;
    }

    /**
     * Set the 'schedules' element value.
     *
     * @param schedules
     */
    public void setSchedules(Schedules schedules){
        this.schedules = schedules;
    }

    /**
     * Get the 'dimension-entries' element value.
     *
     * @return value
     */
    public DimensionEntries getDimensionEntries(){
        return dimensionEntries;
    }

    /**
     * Set the 'dimension-entries' element value.
     *
     * @param dimensionEntries
     */
    public void setDimensionEntries(DimensionEntries dimensionEntries){
        this.dimensionEntries = dimensionEntries;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="employment-end-date"
     * minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:date" name="employment-end-date" minOccurs="0"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class EmploymentEndDate{
        private Date employmentEndDate;

        /**
         * Get the 'employment-end-date' element value.
         *
         * @return value
         */
        public Date getEmploymentEndDate(){
            return employmentEndDate;
        }

        /**
         * Set the 'employment-end-date' element value.
         *
         * @param employmentEndDate
         */
        public void setEmploymentEndDate(Date employmentEndDate){
            this.employmentEndDate = employmentEndDate;
        }
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="schedules" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="payroll-user-schedule" name="schedule" minOccurs="0"
     *       maxOccurs="unbounded"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Schedules{
        private List<PayrollUserSchedule> scheduleList = new ArrayList<PayrollUserSchedule>();

        /**
         * Get the list of 'schedule' element items.
         *
         * @return list
         */
        public List<PayrollUserSchedule> getScheduleList(){
            return scheduleList;
        }

        /**
         * Set the list of 'schedule' element items.
         *
         * @param list
         */
        public void setScheduleList(List<PayrollUserSchedule> list){
            scheduleList = list;
        }
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="dimension-entries"
     * minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="dimension-entry-reference" name="dimension-entry" minOccurs="0"
     *       maxOccurs="unbounded"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class DimensionEntries{
        private List<DimensionEntryReference> dimensionEntryList = new ArrayList
                <DimensionEntryReference>();

        /**
         * Get the list of 'dimension-entry' element items.
         *
         * @return list
         */
        public List<DimensionEntryReference> getDimensionEntryList(){
            return dimensionEntryList;
        }

        /**
         * Set the list of 'dimension-entry' element items.
         *
         * @param list
         */
        public void setDimensionEntryList(List<DimensionEntryReference> list){
            dimensionEntryList = list;
        }
    }
}
