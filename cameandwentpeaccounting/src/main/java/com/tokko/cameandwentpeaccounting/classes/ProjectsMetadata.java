package com.tokko.cameandwentpeaccounting.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="projects-metadata">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="project-metadata" name="project" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ProjectsMetadata{
    private List<ProjectMetadata> projectList = new ArrayList<ProjectMetadata>();

    /**
     * Get the list of 'project' element items.
     *
     * @return list
     */
    public List<ProjectMetadata> getProjectList(){
        return projectList;
    }

    /**
     * Set the list of 'project' element items.
     *
     * @param list
     */
    public void setProjectList(List<ProjectMetadata> list){
        projectList = list;
    }
}
