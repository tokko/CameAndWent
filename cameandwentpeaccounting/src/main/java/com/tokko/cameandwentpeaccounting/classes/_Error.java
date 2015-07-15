package com.tokko.cameandwentpeaccounting.classes;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="error">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element type="xs:string" name="message"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class _Error{
    private String message;

    /**
     * Get the 'message' element value.
     *
     * @return value
     */
    public String getMessage(){
        return message;
    }

    /**
     * Set the 'message' element value.
     *
     * @param message
     */
    public void setMessage(String message){
        this.message = message;
    }
}
