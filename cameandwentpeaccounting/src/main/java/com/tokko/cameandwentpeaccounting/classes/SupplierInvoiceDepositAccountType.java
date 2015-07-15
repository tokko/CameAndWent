package com.tokko.cameandwentpeaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema"
 * name="supplier-invoice-deposit-account-type">
 *   &lt;xs:restriction base="xs:string">
 *     &lt;xs:enumeration value="BankGiro"/>
 *     &lt;xs:enumeration value="PlusGiro"/>
 *     &lt;xs:enumeration value="Iban"/>
 *     &lt;xs:enumeration value="BankAccount"/>
 *     &lt;xs:enumeration value="Other"/>
 *   &lt;/xs:restriction>
 * &lt;/xs:simpleType>
 * </pre>
 */
public enum SupplierInvoiceDepositAccountType{
    BANK_GIRO("BankGiro"), PLUS_GIRO("PlusGiro"), IBAN("Iban"), BANK_ACCOUNT(
            "BankAccount"), OTHER("Other");
    private final String value;

    SupplierInvoiceDepositAccountType(String value){
        this.value = value;
    }

    public static SupplierInvoiceDepositAccountType convert(String value){
        for(SupplierInvoiceDepositAccountType inst : values()){
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
