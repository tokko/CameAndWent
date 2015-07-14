package peaccounting;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="payroll-schedule-interval-type">
 *   &lt;xs:restriction base="xs:string">
 *     &lt;xs:enumeration value="Work"/>
 *     &lt;xs:enumeration value="WorkReduced"/>
 *     &lt;xs:enumeration value="WorkFree"/>
 *   &lt;/xs:restriction>
 * &lt;/xs:simpleType>
 * </pre>
 */
public enum PayrollScheduleIntervalType{
    WORK("Work"), WORK_REDUCED("WorkReduced"), WORK_FREE("WorkFree");
    private final String value;

    PayrollScheduleIntervalType(String value){
        this.value = value;
    }

    public static PayrollScheduleIntervalType convert(String value){
        for(PayrollScheduleIntervalType inst : values()){
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
