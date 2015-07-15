package com.tokko.cameandwentpeaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="payroll-event-type">
 *   &lt;xs:restriction base="xs:string">
 *     &lt;xs:enumeration value="Sick"/>
 *     &lt;xs:enumeration value="Vacation"/>
 *     &lt;xs:enumeration value="LeaveOfAbsence"/>
 *     &lt;xs:enumeration value="LeaveOfAbsenceVacationEarned"/>
 *     &lt;xs:enumeration value="WorkHour"/>
 *     &lt;xs:enumeration value="ParentalLeave"/>
 *     &lt;xs:enumeration value="Childcare"/>
 *     &lt;xs:enumeration value="CloseRelativeCare"/>
 *     &lt;xs:enumeration value="PaternityLeave"/>
 *     &lt;xs:enumeration value="OtherLeave"/>
 *   &lt;/xs:restriction>
 * &lt;/xs:simpleType>
 * </pre>
 */
public enum PayrollEventType{
    SICK("Sick"), VACATION("Vacation"), LEAVE_OF_ABSENCE("LeaveOfAbsence"),
    LEAVE_OF_ABSENCE_VACATION_EARNED(
            "LeaveOfAbsenceVacationEarned"), WORK_HOUR("WorkHour"), PARENTAL_LEAVE(
            "ParentalLeave"), CHILDCARE("Childcare"), CLOSE_RELATIVE_CARE(
            "CloseRelativeCare"), PATERNITY_LEAVE("PaternityLeave"), OTHER_LEAVE(
            "OtherLeave");
    private final String value;

    PayrollEventType(String value){
        this.value = value;
    }

    public static PayrollEventType convert(String value){
        for(PayrollEventType inst : values()){
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
