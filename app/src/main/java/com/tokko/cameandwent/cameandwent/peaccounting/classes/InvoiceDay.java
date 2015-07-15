package com.tokko.cameandwent.cameandwent.peaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="invoice-day">
 *   &lt;xs:restriction base="xs:string">
 *     &lt;xs:enumeration value="INVOICE_AT_START"/>
 *     &lt;xs:enumeration value="PAY_AT_START"/>
 *     &lt;xs:enumeration value="INVOICE_AT_END"/>
 *     &lt;xs:enumeration value="INVOICE_AT_START_MONTH"/>
 *     &lt;xs:enumeration value="INVOICE_AT_END_MONTH"/>
 *   &lt;/xs:restriction>
 * &lt;/xs:simpleType>
 * </pre>
 */
public enum InvoiceDay{
    INVOICE_AT_START, PAY_AT_START, INVOICE_AT_END, INVOICE_AT_START_MONTH, INVOICE_AT_END_MONTH
}
