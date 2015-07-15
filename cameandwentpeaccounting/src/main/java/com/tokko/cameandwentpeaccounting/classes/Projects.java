package com.tokko.cameandwentpeaccounting.classes;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="projects">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="project" name="project" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
@Root
public class Projects{
    @ElementList(inline = true)
    private List<Project> projectList = new ArrayList<Project>();

    /**
     * Get the list of 'project' element items.
     *
     * @return list
     */
    public List<Project> getProjectList(){
        return projectList;
    }

    /**
     * Set the list of 'project' element items.
     *
     * @param list
     */
    public void setProjectList(List<Project> list){
        projectList = list;
    }
}
