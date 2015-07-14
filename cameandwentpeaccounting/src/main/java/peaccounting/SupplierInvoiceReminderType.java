package peaccounting;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="supplier-invoice-reminder-type">
 *   &lt;xs:restriction base="xs:string">
 *     &lt;xs:enumeration value="None"/>
 *     &lt;xs:enumeration value="Reminder"/>
 *     &lt;xs:enumeration value="DebtCollection"/>
 *     &lt;xs:enumeration value="Enforcement"/>
 *   &lt;/xs:restriction>
 * &lt;/xs:simpleType>
 * </pre>
 */
public enum SupplierInvoiceReminderType{
    NONE("None"), REMINDER("Reminder"), DEBT_COLLECTION("DebtCollection"), ENFORCEMENT(
            "Enforcement");
    private final String value;

    SupplierInvoiceReminderType(String value){
        this.value = value;
    }

    public static SupplierInvoiceReminderType convert(String value){
        for(SupplierInvoiceReminderType inst : values()){
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
