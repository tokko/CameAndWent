package peaccounting;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-unit-type">
 *   &lt;xs:restriction base="xs:string">
 *     &lt;xs:enumeration value="Generic"/>
 *     &lt;xs:enumeration value="Mile"/>
 *     &lt;xs:enumeration value="Hour"/>
 *     &lt;xs:enumeration value="Day"/>
 *   &lt;/xs:restriction>
 * &lt;/xs:simpleType>
 * </pre>
 */
public enum PayrollUnitType{
    GENERIC("Generic"), MILE("Mile"), HOUR("Hour"), DAY("Day");
    private final String value;

    PayrollUnitType(String value){
        this.value = value;
    }

    public static PayrollUnitType convert(String value){
        for(PayrollUnitType inst : values()){
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
