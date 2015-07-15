package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.math.BigDecimal;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="exchange-rate">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="xs:string" name="from-currency"/>
 *       &lt;xs:element type="xs:string" name="to-currency"/>
 *       &lt;xs:element type="xs:decimal" name="rate"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class ExchangeRate{
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;

    /**
     * Get the 'from-currency' element value.
     *
     * @return value
     */
    public String getFromCurrency(){
        return fromCurrency;
    }

    /**
     * Set the 'from-currency' element value.
     *
     * @param fromCurrency
     */
    public void setFromCurrency(String fromCurrency){
        this.fromCurrency = fromCurrency;
    }

    /**
     * Get the 'to-currency' element value.
     *
     * @return value
     */
    public String getToCurrency(){
        return toCurrency;
    }

    /**
     * Set the 'to-currency' element value.
     *
     * @param toCurrency
     */
    public void setToCurrency(String toCurrency){
        this.toCurrency = toCurrency;
    }

    /**
     * Get the 'rate' element value.
     *
     * @return value
     */
    public BigDecimal getRate(){
        return rate;
    }

    /**
     * Set the 'rate' element value.
     *
     * @param rate
     */
    public void setRate(BigDecimal rate){
        this.rate = rate;
    }
}
