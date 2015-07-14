package peaccounting;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-user-month-report-strategy">
 *   &lt;xs:restriction base="xs:string">
 *     &lt;xs:enumeration value="DontCreate"/>
 *     &lt;xs:enumeration value="Create"/>
 *     &lt;xs:enumeration value="CreateApproveSelf"/>
 *   &lt;/xs:restriction>
 * &lt;/xs:simpleType>
 * </pre>
 */
public enum PayrollUserMonthReportStrategy{
    DONT_CREATE("DontCreate"), CREATE("Create"), CREATE_APPROVE_SELF(
            "CreateApproveSelf");
    private final String value;

    PayrollUserMonthReportStrategy(String value){
        this.value = value;
    }

    public static PayrollUserMonthReportStrategy convert(String value){
        for(PayrollUserMonthReportStrategy inst : values()){
            if(inst.xmlValue().equals(value)){
                return inst;
            }
        }
        return null;
    }

    public String xmlValue(){
        return value;
    }
}
