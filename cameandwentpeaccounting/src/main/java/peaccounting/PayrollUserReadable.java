package peaccounting;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-user-readable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="user-reference" name="user"/>
 *     &lt;xs:element type="xs:string" name="ssn"/>
 *     &lt;xs:element type="xs:string" name="address1"/>
 *     &lt;xs:element type="xs:string" name="address2"/>
 *     &lt;xs:element type="xs:string" name="zip-code"/>
 *     &lt;xs:element type="xs:string" name="state"/>
 *     &lt;xs:element type="xs:string" name="country-code"/>
 *     &lt;xs:element type="xs:string" name="phone"/>
 *     &lt;xs:element type="xs:int" name="tax-table"/>
 *     &lt;xs:element type="xs:int" name="tax-table-column"/>
 *     &lt;xs:element type="xs:decimal" name="tax-percentage"/>
 *     &lt;xs:element type="xs:decimal" name="one-time-tax-percentage"/>
 *     &lt;xs:element type="xs:string" name="clearing-nr"/>
 *     &lt;xs:element type="xs:string" name="account-nr"/>
 *     &lt;xs:element type="xs:date" name="employment-start-date"/>
 *     &lt;xs:element type="xs:date" name="employment-end-date" minOccurs="0"/>
 *     &lt;xs:element type="xs:long" name="travel-reimbursement-per-mile"/>
 *     &lt;xs:element type="xs:long" name="monthly-salary"/>
 *     &lt;xs:element type="xs:long" name="hourly-salary"/>
 *     &lt;xs:element type="xs:boolean" name="provision"/>
 *     &lt;xs:element type="payroll-user-month-report-strategy" name="month-report-strategy"/>
 *     &lt;xs:element name="schedules">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element type="payroll-user-schedule" name="schedule" minOccurs="0"
 *           maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *     &lt;xs:element name="dimension-entries">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element type="dimension-entry-reference" name="dimension-entry" minOccurs="0"
 *           maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class PayrollUserReadable{
    private UserReference user;
    private String ssn;
    private String address1;
    private String address2;
    private String zipCode;
    private String state;
    private String countryCode;
    private String phone;
    private int taxTable;
    private int taxTableColumn;
    private BigDecimal taxPercentage;
    private BigDecimal oneTimeTaxPercentage;
    private String clearingNr;
    private String accountNr;
    private Date employmentStartDate;
    private Date employmentEndDate;
    private long travelReimbursementPerMile;
    private long monthlySalary;
    private long hourlySalary;
    private boolean provision;
    private PayrollUserMonthReportStrategy monthReportStrategy;
    private List<PayrollUserSchedule> scheduleList = new ArrayList<PayrollUserSchedule>();
    private List<DimensionEntryReference> dimensionEntryList = new
            ArrayList<DimensionEntryReference>();

    /**
     * Get the 'user' element value.
     *
     * @return value
     */
    public UserReference getUser(){
        return user;
    }

    /**
     * Set the 'user' element value.
     *
     * @param user
     */
    public void setUser(UserReference user){
        this.user = user;
    }

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
    public int getTaxTable(){
        return taxTable;
    }

    /**
     * Set the 'tax-table' element value.
     *
     * @param taxTable
     */
    public void setTaxTable(int taxTable){
        this.taxTable = taxTable;
    }

    /**
     * Get the 'tax-table-column' element value.
     *
     * @return value
     */
    public int getTaxTableColumn(){
        return taxTableColumn;
    }

    /**
     * Set the 'tax-table-column' element value.
     *
     * @param taxTableColumn
     */
    public void setTaxTableColumn(int taxTableColumn){
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

    /**
     * Get the 'travel-reimbursement-per-mile' element value.
     *
     * @return value
     */
    public long getTravelReimbursementPerMile(){
        return travelReimbursementPerMile;
    }

    /**
     * Set the 'travel-reimbursement-per-mile' element value.
     *
     * @param travelReimbursementPerMile
     */
    public void setTravelReimbursementPerMile(long travelReimbursementPerMile){
        this.travelReimbursementPerMile = travelReimbursementPerMile;
    }

    /**
     * Get the 'monthly-salary' element value.
     *
     * @return value
     */
    public long getMonthlySalary(){
        return monthlySalary;
    }

    /**
     * Set the 'monthly-salary' element value.
     *
     * @param monthlySalary
     */
    public void setMonthlySalary(long monthlySalary){
        this.monthlySalary = monthlySalary;
    }

    /**
     * Get the 'hourly-salary' element value.
     *
     * @return value
     */
    public long getHourlySalary(){
        return hourlySalary;
    }

    /**
     * Set the 'hourly-salary' element value.
     *
     * @param hourlySalary
     */
    public void setHourlySalary(long hourlySalary){
        this.hourlySalary = hourlySalary;
    }

    /**
     * Get the 'provision' element value.
     *
     * @return value
     */
    public boolean isProvision(){
        return provision;
    }

    /**
     * Set the 'provision' element value.
     *
     * @param provision
     */
    public void setProvision(boolean provision){
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
