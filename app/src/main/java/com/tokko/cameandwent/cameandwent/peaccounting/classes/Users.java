package com.tokko.cameandwent.cameandwent.peaccounting.classes;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="users">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="user" name="user" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
@Root
public class Users{
    @ElementList(inline = true)
    private List<User> userList = new ArrayList<User>();

    /**
     * Get the list of 'user' element items.
     *
     * @return list
     */
    public List<User> getUserList(){
        return userList;
    }

    /**
     * Set the list of 'user' element items.
     *
     * @param list
     */
    public void setUserList(List<User> list){
        userList = list;
    }
}
