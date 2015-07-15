package com.tokko.cameandwentpeaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="event-readables">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="event-readable" name="event-readable" minOccurs="0"
 *     maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class EventReadables{
    private List<EventReadable> eventReadableList = new ArrayList<EventReadable>();

    /**
     * Get the list of 'event-readable' element items.
     *
     * @return list
     */
    public List<EventReadable> getEventReadableList(){
        return eventReadableList;
    }

    /**
     * Set the list of 'event-readable' element items.
     *
     * @param list
     */
    public void setEventReadableList(List<EventReadable> list){
        eventReadableList = list;
    }
}
