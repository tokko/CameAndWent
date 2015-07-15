package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="client-delivery-types">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element name="client-delivery-type" minOccurs="0" maxOccurs="unbounded">
 *         &lt;!-- Reference to inner class ClientDeliveryType -->
 *       &lt;/xs:element>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class ClientDeliveryTypes{
    private List<ClientDeliveryType> clientDeliveryTypeList = new ArrayList<ClientDeliveryType>();

    /**
     * Get the list of 'client-delivery-type' element items.
     *
     * @return list
     */
    public List<ClientDeliveryType> getClientDeliveryTypeList(){
        return clientDeliveryTypeList;
    }

    /**
     * Set the list of 'client-delivery-type' element items.
     *
     * @param list
     */
    public void setClientDeliveryTypeList(List<ClientDeliveryType> list){
        clientDeliveryTypeList = list;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="client-delivery-type"
     * minOccurs="0" maxOccurs="unbounded">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:string" name="name"/>
     *       &lt;xs:element type="xs:boolean" name="default"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class ClientDeliveryType{
        private String name;
        private Boolean _default;

        /**
         * Get the 'name' element value.
         *
         * @return value
         */
        public String getName(){
            return name;
        }

        /**
         * Set the 'name' element value.
         *
         * @param name
         */
        public void setName(String name){
            this.name = name;
        }

        /**
         * Get the 'default' element value.
         *
         * @return value
         */
        public Boolean getDefault(){
            return _default;
        }

        /**
         * Set the 'default' element value.
         *
         * @param _default
         */
        public void setDefault(Boolean _default){
            this._default = _default;
        }
    }
}
